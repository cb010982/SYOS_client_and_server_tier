/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

import com.mycompany.syos.system.designpatterns.billing.OnlineBill;
import com.mycompany.syos.system.designpatterns.state.AddingItemsState;
import com.mycompany.syos.system.designpatterns.state.CartState;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mycompany.database_syos.dao.CartDAO;
import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import com.mycompany.database_syos.models.Cart;
import com.mycompany.database_syos.models.CartItem;
import com.mycompany.database_syos.models.OnlineInventory;
import com.mycompany.database_syos.models.OnlineInventoryItem;
import com.mycompany.database_syos.models.Product;
import com.mycompany.database_syos.models.UserModel;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author User
 */
public class CartService {
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private OnlineInventoryItemDAO onlineInventoryItemDAO;
    private OnlineInventoryDAO onlineInventoryDAO;
    private UserModel currentUser;
    private Integer currentCartId = null;
    private CartState currentState;
              
    public CartService(ProductDAO productDAO, CartDAO cartDAO, OnlineInventoryItemDAO onlineInventoryItemDAO, OnlineInventoryDAO onlineInventoryDAO, UserModel currentUser) {
        this.productDAO = productDAO;
        this.cartDAO = cartDAO;
        this.onlineInventoryItemDAO = onlineInventoryItemDAO;
        this.onlineInventoryDAO = onlineInventoryDAO;
        this.currentUser = currentUser;
        this.currentState = new AddingItemsState(); 
    }
    public void setState(CartState state) {
        this.currentState = state;
    }

    public synchronized void addToCart(int productId, int quantity) {
    currentState.addToCart(this, productId, quantity);
}

    public void checkout(String address, String phone) {
        currentState.checkout(this, address, phone);  
    }

    private final ReentrantLock inventoryLock = new ReentrantLock();

    public void addItemsToCartLogic(int productId, int quantity) throws SQLException, ProductDatabaseException, InterruptedException {
        inventoryLock.lock(); // Lock the critical section
        Connection connection = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false); 

            // Ensure the cart is initialized in a thread-safe manner
            if (currentCartId == null) {
                synchronized (this) {
                    if (currentCartId == null) {
                        currentCartId = createCartIfNeeded();
                    }
                }
            }

            // Fetch batch ID
            int batchId = onlineInventoryItemDAO.getAvailableBatchId(productId, quantity);
            if (batchId == -1) {
                throw new ProductDatabaseException("No batch with sufficient stock for product ID: " + productId);
            }

           
            List<OnlineInventoryItem> inventoryItems = onlineInventoryItemDAO.getOnlineInventoryItemsForProduct(productId);
            if (inventoryItems.size() < quantity) {
                throw new SQLException("Not enough serialized items available for product ID: " + productId + " in batch ID: " + batchId);
            }

            // Add items to the cart and update inventory
            for (int i = 0; i < quantity; i++) {
                OnlineInventoryItem item = inventoryItems.get(i);

                // Add item to the cart
                cartDAO.addItemToCart(
                    currentCartId,
                    productId,
                    batchId,
                    item.getItemSerialNumber(), 
                    1, // Adding one item at a time
                    calculatePrice(productId, 1)
                );

                // Remove the item from online inventory
                onlineInventoryItemDAO.removeOnlineInventoryItem(item.getOnlineInventoryItemId());
            }

            // Reduce stock in online inventory
            boolean stockUpdated = onlineInventoryDAO.reduceStockByBatchId(batchId, quantity, connection);
            if (!stockUpdated) {
                throw new SQLException("Stock update failed for batch ID: " + batchId);
            }

            connection.commit();
            System.out.println("Added " + quantity + " items of product ID " + productId + " to cart " + currentCartId);

        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (connection != null) connection.setAutoCommit(true);
            inventoryLock.unlock();
        }
    }
 
    public double calculatePrice(int productId, int quantity) throws SQLException, ProductDatabaseException {
        // Fetch the product details from the ProductDAO
        Product product = productDAO.getProductDetailsForCart(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with ID " + productId + " does not exist.");
        }

        // Calculate the total price based on the final price and quantity
        double finalPrice = product.getFinalPrice();
        return finalPrice * quantity;
    }

    public void checkoutLogic(String address, String phone) throws SQLException, InterruptedException {
        System.out.println("Checkout initiated for delivery to: " + address);

        Connection connection = null;
        boolean acquired = inventoryLock.tryLock(); // Lock the inventory

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false); 

            int cartId = createCartIfNeeded(); // Ensure cart exists

            // Add address and phone details to the cart
            boolean updated = cartDAO.updateCartContactInfo(cartId, phone, address);
            if (!updated) {
                throw new SQLException("Failed to update contact information.");
            }

            // Fetch cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cartId);
            if (cartItems.isEmpty()) {
                throw new SQLException("Cart is empty. Cannot proceed to checkout.");
            }

            // Deduct stock for each item in the cart
            for (CartItem item : cartItems) {
                boolean stockUpdated = onlineInventoryDAO.reduceStockByBatchId(item.getBatchId(), item.getQuantity(), connection);
                if (!stockUpdated) {
                    throw new SQLException("Stock update failed for batch ID: " + item.getBatchId());
                }
            }

            // Save the cart as an order
            cartDAO.saveCartAsOrder(currentUser.getUserId(), cartDAO.getCartById(cartId));

            // Clear the cart
            cartDAO.clearCart(cartId);

            connection.commit(); 
            System.out.println("Checkout completed. Order saved, and stock updated.");

        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (connection != null) connection.setAutoCommit(true);
            inventoryLock.unlock();
        }
    }

    public void processPaymentLogic(Scanner scanner) {
        System.out.println("Processing payment...");
        System.out.println("Enter cardholder name:");
        String cardholderName = scanner.next();
        System.out.println("Enter card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter CVC:");
        String cvc = scanner.next();
        System.out.println("Enter expiry date (MM/YY):");
        String expiryDate = scanner.next();
        System.out.println("Processing payment...");
        System.out.println("Payment details:");
        System.out.println("Cardholder Name: " + cardholderName);
        System.out.println("Card Number: " + cardNumber);
        System.out.println("CVC: " + cvc);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("Payment successful!");
    }

    public void displayProductsForSelection() {
        try {
            List<Product> products = productDAO.getAllProducts();
            System.out.println("\n--- Available Products ---");
            for (Product product : products) {
                System.out.printf("ID: %d | Product: %s | Price: %.2f\n", 
                                  product.getId(), product.getName(), product.getFinalPrice());
            }
        } catch (ProductDatabaseException e) {
            System.out.println("Error fetching product list.");
            e.printStackTrace();
        }
    }

    private int createCartIfNeeded() {

         if (currentCartId != null) {
             return currentCartId;
         }
         currentCartId = createNewCart(); 
         return currentCartId;
     }
   
   private int createNewCart() {
    Cart newCart = new Cart();
    newCart.setUserId(currentUser.getUserId()); 
    newCart.setCartNo("CART_" + System.currentTimeMillis());
    newCart.setDate(new java.sql.Date(System.currentTimeMillis()));
    newCart.setTotal(0.0);
    newCart.setGrossTotal(0.0);
    newCart.setNetTotal(0.0);
    newCart.setCreatedAt(new Timestamp(System.currentTimeMillis()));

    int cartId = cartDAO.saveCart(newCart); // Save the cart and get its ID
    return cartId;
}

    private int createCartIfNotExist() {
        if (currentCartId == null) { 
            synchronized (this) { 
                if (currentCartId == null) { 
                    int cartId = cartDAO.getCartIdByUserId(currentUser.getUserId());
                    if (cartId == -1) { 
                        cartId = createNewCart();
                    }
                    currentCartId = cartId; 
                }
            }
        }
        return currentCartId; 
    }

    public void setDeliveryDetails(Scanner scanner) {
        System.out.println("Enter delivery address:");
        String address = scanner.next();
        
        System.out.println("Enter phone number:");
        String phone = scanner.next();

        checkout(address, phone);
    }

    private List<CartItem> processOnlineInventoryItems(int cartId, int productId, int quantityRequired) throws Exception {
        List<CartItem> cartItems = new ArrayList<>();
        List<OnlineInventoryItem> inventoryItems = onlineInventoryItemDAO.getOnlineInventoryItemsForProduct(productId);  

        int remainingQuantity = quantityRequired;  

        for (OnlineInventoryItem item : inventoryItems) {
            if (remainingQuantity <= 0) break;  

            int quantityToUse = 1; 
            double price = productDAO.getPriceByProductId(productId); 

            LocalDateTime createdAt = LocalDateTime.now();  

            CartItem cartItem = new CartItem(0, cartId, productId, item.getBatchId(), item.getItemSerialNumber(), quantityToUse, createdAt, price);

            cartItems.add(cartItem);  

            onlineInventoryItemDAO.removeOnlineInventoryItem(item.getOnlineInventoryItemId());

            OnlineInventory onlineInventory = onlineInventoryDAO.getOnlineInventoryById(item.getOnlineInventoryId());
            int newQuantity = onlineInventory.getCurrentQuantity() - quantityToUse;
            onlineInventoryDAO.updateOnlineInventoryQuantity(item.getOnlineInventoryId(), newQuantity);

            remainingQuantity -= quantityToUse; 
        }

        if (remainingQuantity > 0) {
            System.out.println("Insufficient stock for product ID: " + productId);
        }

        return cartItems;  
    }
    
    
    public void addToCartCLI(int productId, int quantity) throws SQLException {
        int cartId = createCartIfNotExist();
        cartDAO.addProductToCart(cartId, productId, quantity);
    }

    public synchronized void removeFromCart(int productId) throws SQLException {
    int cartId = createCartIfNotExist();
    cartDAO.removeProductFromCart(cartId, productId);
}
    
    public Cart getCurrentCart() {
        int cartId = createCartIfNotExist();
        return cartDAO.getCartById(cartId);
    }
    
 
    public synchronized List<CartItem> getCartItems() {
        int currentCartId = getCurrentCartId(); 
        return cartDAO.getCartItemsByCartId(currentCartId); 
    }


    public void confirmCart(int userId) throws SQLException {
        int cartId = createCartIfNotExist();
        Cart cart = cartDAO.getCartById(cartId);

        // Save the cart as an order in the database
        cartDAO.saveCartAsOrder(userId, cart);

        // Clear the cart after confirmation
        cartDAO.clearCart(cartId);
    }
 
    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
        this.currentCartId = null; 
        startNewCart(); 
    }

    public UserModel getCurrentUser(HttpSession session) {
        if (this.currentUser == null) {
            this.currentUser = (UserModel) session.getAttribute("currentUser");
        }
        return this.currentUser;
    }
    
    public double calculateGrossTotal() {
        List<CartItem> cartItems = getCartItems();
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public void removeItemsFromCartLogic(int productId, int quantity) throws ProductDatabaseException {
        Product product = productDAO.getProductById(productId);

        if (product != null) {
            int cartId = createCartIfNotExist();
            cartDAO.removeItemsFromCart(cartId, productId, quantity);
            System.out.println("Removed " + quantity + " of " + product.getName() + " from the cart.");
        } else {
            System.out.println("Product not found!");
        }
    }

    public synchronized int startNewCart() {
        currentCartId = createNewCart();
        return currentCartId;
    }

    public int getCurrentCartId() {
        return createCartIfNotExist();
    }
    
    public void processCheckout(List<CartItem> cartItems, String address, String phone) throws SQLException, ProductDatabaseException {
        synchronized (this) { // Synchronize the entire checkout process
            int cartId = createCartIfNotExist(); 

            // Process each cart item
            for (CartItem item : cartItems) {
                int batchId = onlineInventoryItemDAO.getAvailableBatchId(item.getProductId(), item.getQuantity());
                if (batchId == -1) {
                    throw new SQLException("Insufficient stock for product ID: " + item.getProductId());
                }

                // Fetch  item for the product
                List<OnlineInventoryItem> inventoryItems = onlineInventoryItemDAO.getOnlineInventoryItemsForProduct(item.getProductId());
                if (inventoryItems.isEmpty()) {
                    throw new SQLException("No serialized items available for product ID: " + item.getProductId());
                }

                // Add items to the cart
                for (int i = 0; i < item.getQuantity(); i++) {
                    OnlineInventoryItem inventoryItem = inventoryItems.get(i);

                    cartDAO.addItemToCart(
                        cartId,
                        item.getProductId(),
                        batchId,
                        inventoryItem.getItemSerialNumber(),
                        1,
                        calculatePrice(item.getProductId(), 1)
                    );

                    // Remove the item from online inventory
                    onlineInventoryItemDAO.removeOnlineInventoryItem(inventoryItem.getOnlineInventoryItemId());
                }
            }

            // Add address and phone if provided
            if (address != null && phone != null) {
                cartDAO.updateCartContactInfo(cartId, phone, address);
            } else {
                System.out.println("Address and phone will be collected later.");
            }

            System.out.println("Cart processed for cart ID: " + cartId);
        }
    }

    public OnlineInventoryDAO getOnlineInventoryDAO() {
        return this.onlineInventoryDAO;
    }

    public OnlineInventoryItemDAO getOnlineInventoryItemDAO() {
        return this.onlineInventoryItemDAO;
    }
}


