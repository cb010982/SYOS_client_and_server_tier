/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

/**
 *
 * @author User
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.StoreInventoryDAO;
import com.mycompany.database_syos.exceptions.InventoryException;
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import com.mycompany.database_syos.models.Product;
import com.mycompany.database_syos.models.ShelfItem;
import com.mycompany.database_syos.models.StoreInventory;

public class StoreInventoryService {
    private final StoreInventoryDAO storeInventoryDAO;
    private final ProductDAO productDAO;
    private Connection connection;

  
    public StoreInventoryService(StoreInventoryDAO storeInventoryDAO, ProductDAO productDAO, Connection connection) {
        this.storeInventoryDAO = storeInventoryDAO;
        this.productDAO = productDAO;
        this.connection = connection;

        }

    public synchronized void addNewBatch(Scanner scanner) {
        try {
            System.out.println("--- Add New Batch ---");

            System.out.print("Enter Product Code (e.g., F-FR-IC-IM-1L): ");
            String productCode = scanner.nextLine().trim();

            System.out.print("Enter Purchase Price: ");
            double purchasePrice = getValidDoubleInput(scanner);

            System.out.print("Enter Expiry Date (yyyy-mm-dd) or leave blank if not applicable: ");
            String expiryInput = scanner.nextLine().trim();
            LocalDate expiryDate = expiryInput.isEmpty() ? null : LocalDate.parse(expiryInput);

            System.out.print("Enter Manufactured Date (yyyy-mm-dd): ");
            LocalDate manufacturedDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter Purchased Quantity: ");
            int purchasedQuantity = getValidIntInput(scanner);

            System.out.print("Enter Supplier Name: ");
            String supplierName = scanner.nextLine().trim();

            addBatchToInventory(productCode, purchasePrice, expiryDate, manufacturedDate, purchasedQuantity, supplierName);
            System.out.println("Batch added to inventory successfully!");

        } catch (Exception e) {
            System.err.println("Error adding batch: " + e.getMessage());
        }
    }


    public void addBatchToInventory(String productCode, double purchasePrice, LocalDate expiryDate, LocalDate manufacturedDate,
                                    int purchasedQuantity, String supplierName) throws InventoryException {
        try {

            Product product = productDAO.getProductByCode(productCode);
            if (product == null) {
                throw new InventoryException("Invalid Product Code: " + productCode);
            }


            int batchId = addBatchToBatchesTable(product.getId(), purchasePrice, expiryDate, manufacturedDate, purchasedQuantity, supplierName);
            System.out.println("Generated Batch ID: " + batchId);


            int lastSerialNumber = getLastSerialNumberForProduct(productCode);
            LocalDateTime dateReceived = LocalDateTime.now();


            for (int i = 1; i <= purchasedQuantity; i++) {
                int newSerialNumber = lastSerialNumber + i;
                String itemSerialNumber = productCode + "-" + newSerialNumber;
                System.out.println("Creating StoreInventory object for item serial number: " + itemSerialNumber);

                StoreInventory inventory = new StoreInventory(
                    0, 
                    batchId, 
                    product.getId(),  
                    1, 
                    expiryDate, 
                    expiryDate != null, 
                    itemSerialNumber
                );

                System.out.println("Adding inventory to database...");
                storeInventoryDAO.addInventory(inventory);
                System.out.println("Batch ID: " + batchId + ", Item Serial Number: " + itemSerialNumber + " added to inventory.");
            }
        } catch (ProductDatabaseException e) {
            e.printStackTrace();
            throw new InventoryException("Error retrieving product details for code: " + productCode, e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InventoryException("Unexpected error occurred while adding batch to inventory", e);
        }
    }


    private int getLastSerialNumberForProduct(String productCode) throws InventoryException {
        String query = "SELECT MAX(CAST(SUBSTRING_INDEX(item_serial_number, '-', -1) AS UNSIGNED)) AS max_serial " +
                       "FROM store_inventory WHERE item_serial_number LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, productCode + "-%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_serial");
                } else {
                    return 0; 
                }
            }
        } catch (SQLException e) {
            throw new InventoryException("Error fetching last serial number for product: " + productCode, e);
        }
    }


    private int addBatchToBatchesTable(int productId, double purchasePrice, LocalDate expiryDate, LocalDate manufacturedDate, 
                                       int purchasedQuantity, String supplierName) throws InventoryException {

        System.out.println("Checking if product ID " + productId + " exists in the product table...");
        try {
            String productCheckQuery = "SELECT COUNT(*) FROM product WHERE product_id = ?";
            try (PreparedStatement pstmtCheck = connection.prepareStatement(productCheckQuery)) {
                pstmtCheck.setInt(1, productId);
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new InventoryException("Product ID " + productId + " does not exist in the product table.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  
            throw new InventoryException("Error verifying product ID in product table", e);
        }


        String sql = "INSERT INTO batches (product_id, purchase_price, expiry_date, manufactured_date, purchased_quantity, supplier_name, date_received) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println("Inserting into batches table: product_id = " + productId + 
                               ", purchase_price = " + purchasePrice + 
                               ", expiry_date = " + expiryDate + 
                               ", manufactured_date = " + manufacturedDate + 
                               ", purchased_quantity = " + purchasedQuantity + 
                               ", supplier_name = " + supplierName);  

            pstmt.setInt(1, productId);
            pstmt.setDouble(2, purchasePrice);
            if (expiryDate != null) {
                pstmt.setDate(3, java.sql.Date.valueOf(expiryDate));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }
            pstmt.setDate(4, java.sql.Date.valueOf(manufacturedDate));
            pstmt.setInt(5, purchasedQuantity);
            pstmt.setString(6, supplierName); 
            pstmt.setDate(7, java.sql.Date.valueOf(LocalDate.now()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InventoryException("Failed to add batch to batches table.");
            }


            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new InventoryException("Failed to retrieve batch ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  
            throw new InventoryException("Error adding batch to batches table", e);
        }
    }

  
    public void viewInventoryByProduct(Scanner scanner) {
        try {
            System.out.print("Enter Product ID to view inventory: ");
            int productId = getValidIntInput(scanner);
            var inventoryList = getInventoryByProductId(productId);

            if (inventoryList.isEmpty()) {
                System.out.println("No inventory found for Product ID: " + productId);
            } else {
                System.out.println("Inventory for Product ID " + productId + ":");
                for (StoreInventory inventory : inventoryList) {
                    System.out.println(inventory);
                }
            }
        } catch (Exception e) {
            System.err.println("Error viewing inventory: " + e.getMessage());
        }
    }

 
    public List<StoreInventory> getInventoryByProductId(int productId) throws InventoryException {
        return storeInventoryDAO.getInventoryByProductId(productId);
    }

    
    private int getSupplierIdByName(String supplierName) throws InventoryException {
        
        return 1;  
    }


    private int generateNextBatchId() throws InventoryException {
 
        return 1;  
    }

  
    public int getValidIntInput(Scanner scanner) {
        int input = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                input = scanner.nextInt();
                scanner.nextLine(); 
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); 
            }
        }

        return input;
    }

  
    public double getValidDoubleInput(Scanner scanner) {
        double input = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                input = scanner.nextDouble();
                scanner.nextLine();
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); 
            }
        }

        return input;
    }
    
    public void moveItemsToShelf(int shelfId, List<StoreInventory> items, ShelfService shelfService) throws InventoryException {
        try {
            for (StoreInventory item : items) {

                if (item.getQuantity() <= 1) {

                    storeInventoryDAO.deleteInventoryById(item.getInventoryId());
                    System.out.println("Inventory ID " + item.getInventoryId() + " deleted as it was moved fully to the shelf.");
                } else {

                    reduceInventoryQuantity(item.getInventoryId(), 1);
                }


                ShelfItem shelfItem = new ShelfItem(shelfId, item.getBatchId(), item.getItemSerialNumber(), item.getProductId());
                shelfService.addShelfItem(shelfItem);
            }
            System.out.println("Items moved to shelf successfully.");
        } catch (Exception e) {
            throw new InventoryException("Error moving items to shelf: " + e.getMessage(), e);
        }
    }

         
    public List<StoreInventory> getInventoryByBatchId(int batchId) throws InventoryException {
        try {
            return storeInventoryDAO.getInventoryByBatchId(batchId);
        } catch (Exception e) {
            throw new InventoryException("Error retrieving inventory by batch ID: " + e.getMessage(), e);
        }
    }

    public List<StoreInventory> getItemsByProductIdSortedByExpiry(int productId) throws InventoryException {
        try {
            return storeInventoryDAO.getExpiringItemsByProductId(productId);
        } catch (Exception e) {
            throw new InventoryException("Error retrieving items by product ID sorted by expiry: " + e.getMessage(), e);
        }
    }
    
    public void reduceInventoryQuantity(int inventoryId, int quantity) throws InventoryException {
        try {
            StoreInventory inventory = storeInventoryDAO.getInventoryById(inventoryId);
            if (inventory == null) {
                throw new InventoryException("Inventory item with ID " + inventoryId + " not found.");
            }
            if (inventory.getQuantity() < quantity) {
                throw new InventoryException("Insufficient quantity in inventory. Available: " + inventory.getQuantity());
            }
            int newQuantity = inventory.getQuantity() - quantity;
            storeInventoryDAO.updateInventoryQuantity(inventoryId, newQuantity);
            System.out.println("Inventory quantity updated successfully.");
        } catch (Exception e) {
            throw new InventoryException("Error reducing inventory quantity: " + e.getMessage(), e);
        }
    }

    public List<StoreInventory> getExpiringItemsByProductId(int productId) throws InventoryException {
        return storeInventoryDAO.getExpiringItemsByProductId(productId);
    }

    public List<StoreInventory> getOldestItemsByDateReceived(int productId) throws InventoryException {
        return storeInventoryDAO.getOldestItemsByDateReceived(productId);
    }


    public List<StoreInventory> getOldestItemsByProductId(int productId) throws InventoryException {
        return storeInventoryDAO.getItemsSortedByOldest(productId);
    }


     public void updateInventoryQuantity(int inventoryId, int newQuantity) throws InventoryException {
        try {
            if (newQuantity <= 0) {

                storeInventoryDAO.deleteInventoryById(inventoryId);
                System.out.println("Inventory ID " + inventoryId + " deleted as quantity reached zero.");
            } else {

                storeInventoryDAO.updateInventoryQuantity(inventoryId, newQuantity);
            }
        } catch (InventoryException e) {
            throw new InventoryException("Error updating inventory quantity for inventory ID: " + inventoryId, e);
        }
    }

}
