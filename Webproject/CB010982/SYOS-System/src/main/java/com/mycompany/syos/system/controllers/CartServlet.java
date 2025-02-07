/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.syos.system.controllers;

import com.mycompany.database_syos.dao.CartDAO;
import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.models.Cart;
import com.mycompany.database_syos.models.CartItem;
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.syos.system.service.CartService;
import java.sql.Connection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mycompany.database_syos.models.OnlineInventoryItem;
import com.mycompany.database_syos.models.Product;
import com.mycompany.syos.system.users.OnlineCustomer;
import com.mycompany.syos.system.users.User;
import jakarta.servlet.AsyncContext;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author User
 */

public class CartServlet extends HttpServlet {
    private CartService cartService;
    private ProductDAO productDAO;

    @Override
    public void init() {
    try {      
        productDAO = new ProductDAO();
        CartDAO cartDAO = new CartDAO(); 
        OnlineInventoryDAO inventoryDAO = new OnlineInventoryDAO();
        OnlineInventoryItemDAO inventoryItemDAO = new OnlineInventoryItemDAO();
        cartService = new CartService(productDAO, cartDAO, inventoryItemDAO, inventoryDAO, null);
    } catch (Exception e) {
        throw new RuntimeException("Failed to initialize CartServlet dependencies.", e);
    }
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        System.out.println("Action received: " + action);

        if (action == null || action.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

        try {
            switch (action) {
                case "add":
                    handleAddToCart(request, response);
                    break;
                case "remove":
                    handleRemoveFromCart(request, response);
                    break;
                case "fetch":
                    handleFetchCart(request, response);
                    break;
                case "confirm":
                    handleConfirmCart(request, response);
                    break;
                case "getProducts":
                    handleFetchProducts(request, response);
                    break;
                case "checkout":
                    handleUpdateQuantity(request, response);
                    break;
                case "sendToCartItems":
                    handleSendToCartItems(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ProductDAO productDAO = new ProductDAO(); 
    OnlineInventoryDAO inventoryDAO = new OnlineInventoryDAO();

    try {
        // Fetch all products
        List<Product> products = productDAO.getAllProductsWithSubCategory();

        // Create a map to store current_quantity for each product
        Map<Integer, Integer> productQuantities = new HashMap<>();
        for (Product product : products) {
            int currentQuantity = inventoryDAO.getCurrentQuantityByProductId(product.getId());
            productQuantities.put(product.getId(), currentQuantity);
        }

        // Set products and quantities as attributes
        request.setAttribute("products", products);
        request.setAttribute("productQuantities", productQuantities);

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Pages/Cart.jsp");
        dispatcher.forward(request, response);
    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching products and inventory.");
    }
}

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;

        return (User) session.getAttribute("currentUser");
    }

    private synchronized void handleAddToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Thread-safe addition to cart
            cartService.addToCart(productId, quantity);

            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Cart updated successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating cart.");
        }
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));

            cartService.removeFromCart(productId);

            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Product removed from cart.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error removing product from cart.");
        }
    }

    private void handleFetchCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not logged in.");
            return;
        }

        try {
            synchronized (this) { // Ensure thread-safe operations on cartService
                if (currentUser instanceof OnlineCustomer) {
                    cartService.setCurrentUser(((OnlineCustomer) currentUser).getUserModel());
                } else if (currentUser instanceof UserModel) {
                    cartService.setCurrentUser((UserModel) currentUser);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user type.");
                    return;
                }

                List<CartItem> cartItems = cartService.getCartItems();
                double grossTotal = cartService.calculateGrossTotal();

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Gson gson = new Gson();
                JsonObject responseData = new JsonObject();
                responseData.add("cartItems", gson.toJsonTree(cartItems));
                responseData.addProperty("grossTotal", grossTotal);

                response.getWriter().write(responseData.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching cart.");
        }
    }

    private void handleConfirmCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not logged in.");
            return;
        }

        try {
            
            if (currentUser instanceof OnlineCustomer) {
                cartService.setCurrentUser(((OnlineCustomer) currentUser).getUserModel());
            } else if (currentUser instanceof UserModel) {
                cartService.setCurrentUser((UserModel) currentUser);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user type.");
                return;
            }

            cartService.confirmCart(((UserModel) currentUser).getUserId());

            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Cart confirmed successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error confirming cart.");
        }
    }

    private void handleFetchProducts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Fetch product details
            List<Product> products = productDAO.getAllProductsWithSubCategory();

            // Fetch product quantities
            Map<Integer, Integer> productQuantities = productDAO.getProductQuantities();

            // Pass products and quantities to the JSP
            request.setAttribute("products", products);
            request.setAttribute("productQuantities", productQuantities);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/Pages/Cart.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching products.");
        }
    }

    private void handleUpdateQuantity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String productIdStr = request.getParameter("productId");
        String changeStr = request.getParameter("change");

        // Validation
        if (action == null || !action.equals("updateQuantity")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or missing action parameter.");
            return;
        }

        // Validate productId and change
        if (productIdStr == null || changeStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing productId or change parameter.");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            int change = Integer.parseInt(changeStr);

            // Log received data
            System.out.println("Updating quantity for productId: " + productId + ", change: " + change);

            // Retrieve the current user
            User currentUser = getCurrentUser(request);
            if (currentUser == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not logged in.");
                return;
            }

            if (currentUser instanceof OnlineCustomer) {
                cartService.setCurrentUser(((OnlineCustomer) currentUser).getUserModel());
            }

            // Update the cart
            cartService.addToCart(productId, change);

            // Send success response
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid productId or change parameter.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating cart.");
        }
    }
 
    private void handleSendToCartItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false); 

            User currentUser = getCurrentUser(request);
            if (currentUser == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
                return;
            }

            cartService.setCurrentUser((currentUser instanceof OnlineCustomer) ? ((OnlineCustomer) currentUser).getUserModel() : (UserModel) currentUser);

            String[] productIds = request.getParameterValues("productId");
            if (productIds == null || productIds.length == 0) {
                request.setAttribute("errors", List.of("No products selected."));
                return;
            }

            List<CartItem> cartItems = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            Map<Integer, Integer> processedProducts = new HashMap<>();

            for (String productIdStr : productIds) {
                try {
                    int productId = Integer.parseInt(productIdStr);
                    int quantity = Integer.parseInt(request.getParameter("quantity-" + productId));

                    if (processedProducts.containsKey(productId)) {
                        System.out.println("Skipping duplicate processing for product ID: " + productId);
                        continue;
                    }

                    int currentQuantity = cartService.getOnlineInventoryDAO().getCurrentQuantityByProductId(productId);
                    if (currentQuantity < quantity) {
                        errors.add("Insufficient stock for product ID: " + productId);
                        continue;
                    }

                    boolean stockReduced = cartService.getOnlineInventoryDAO().reduceStockByProductId(productId, quantity, connection);
                    if (!stockReduced) {
                        errors.add("Stock reduction failed for product ID: " + productId);
                        continue;
                    }

                    cartService.addToCart(productId, quantity);
                    processedProducts.put(productId, quantity);
                    System.out.println("Added " + quantity + " items of product ID " + productId + " to cart.");
                } catch (NumberFormatException e) {
                    errors.add("Invalid product ID or quantity.");
                }
            }

            if (errors.isEmpty()) {
                connection.commit(); 
            } else {
                connection.rollback();
            }

            request.setAttribute("cartItems", cartService.getCartItems());
            request.setAttribute("grossTotal", cartService.calculateGrossTotal());
            request.setAttribute("errors", errors);

            request.getRequestDispatcher("/Pages/CartItems.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating cart.");
        }
    }
}
