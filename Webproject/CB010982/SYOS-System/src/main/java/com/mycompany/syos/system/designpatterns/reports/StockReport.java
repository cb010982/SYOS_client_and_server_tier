/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.reports;

//import com.mycompany.syos.database.dao.ReportQueries;
//import com.mycompany.syos.database.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

//new imports are added after this
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.dao.ReportQueries;

/**
 *
 * @author User
 */
public class StockReport implements ReportStrategy {

    @Override
    public void generateReport() {
        int consoleWidth = 100;  
        String title = "--- Current Stock Batch-wise Report ---";
        String dateLine = "Date Generated: " + LocalDate.now();

      
        int titlePaddingSize = (consoleWidth - title.length()) / 2;
        String titlePadding = " ".repeat(Math.max(0, titlePaddingSize));

        
        int datePaddingSize = (consoleWidth - dateLine.length()) / 2;
        String datePadding = " ".repeat(Math.max(0, datePaddingSize));

   
        System.out.println("=====================================================================================================================");
        System.out.println(titlePadding + title);
        System.out.println(datePadding + dateLine);
        System.out.println("=====================================================================================================================");

      try (Connection conn = DatabaseConnection.getInstance().getConnection()) {

            System.out.println("\n--- Store Inventory Batches ---");
            System.out.printf("%-10s %-25s %-15s %-15s %-15s %-20s %-15s%n", "Batch ID", "Product Name", "Product Code", "Purchase Date", "Purchased Qty", "Quantity Available", "Expiry Date");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

            PreparedStatement pstmtStoreInventory = conn.prepareStatement(ReportQueries.GET_STORE_INVENTORY_BATCHES);
            ResultSet rsStoreInventory = pstmtStoreInventory.executeQuery();

            while (rsStoreInventory.next()) {
                int batchId = rsStoreInventory.getInt("batch_id");
                String productName = rsStoreInventory.getString("product_name");
                String productCode = rsStoreInventory.getString("product_code");
                String purchaseDate = rsStoreInventory.getString("purchase_date");
                int purchasedQuantity = rsStoreInventory.getInt("purchased_quantity"); 
                int quantityAvailable = rsStoreInventory.getInt("quantity_available");
                String expiryDate = rsStoreInventory.getString("expiry_date");

              
                System.out.printf("%-10d %-25s %-15s %-15s %-15d %-20d %-15s%n", batchId, productName, productCode, purchaseDate, purchasedQuantity, quantityAvailable, expiryDate);
            }

         
            System.out.println("\n--- Online Inventory Batches ---");
            System.out.printf("%-10s %-25s %-15s %-15s %-15s %-20s %-15s%n", "Batch ID", "Product Name", "Product Code", "Purchase Date", "Purchased Qty", "Quantity Available", "Expiry Date");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

            PreparedStatement pstmtOnlineInventory = conn.prepareStatement(ReportQueries.GET_ONLINE_INVENTORY_BATCHES);
            ResultSet rsOnlineInventory = pstmtOnlineInventory.executeQuery();

            while (rsOnlineInventory.next()) {
                int batchId = rsOnlineInventory.getInt("batch_id");
                String productName = rsOnlineInventory.getString("product_name");
                String productCode = rsOnlineInventory.getString("product_code");
                String purchaseDate = rsOnlineInventory.getString("purchase_date");
                int purchasedQuantity = rsOnlineInventory.getInt("purchased_quantity");  
                int quantityAvailable = rsOnlineInventory.getInt("quantity_available");
                String expiryDate = rsOnlineInventory.getString("expiry_date");

             
                System.out.printf("%-10d %-25s %-15s %-15s %-15d %-20d %-15s%n", batchId, productName, productCode, purchaseDate, purchasedQuantity, quantityAvailable, expiryDate);
            }

         
            System.out.println("\n--- Shelf Inventory Batches ---");
            System.out.printf("%-10s %-25s %-15s %-15s %-15s %-20s %-15s%n", "Batch ID", "Product Name", "Product Code", "Purchase Date", "Purchased Qty", "Quantity Available", "Expiry Date");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

            PreparedStatement pstmtShelfInventory = conn.prepareStatement(ReportQueries.GET_SHELF_INVENTORY_BATCHES);
            ResultSet rsShelfInventory = pstmtShelfInventory.executeQuery();

            while (rsShelfInventory.next()) {
                int batchId = rsShelfInventory.getInt("batch_id");
                String productName = rsShelfInventory.getString("product_name");
                String productCode = rsShelfInventory.getString("product_code");
                String purchaseDate = rsShelfInventory.getString("purchase_date");
                int purchasedQuantity = rsShelfInventory.getInt("purchased_quantity"); 
                int quantityAvailable = rsShelfInventory.getInt("quantity_available");
                String expiryDate = rsShelfInventory.getString("expiry_date");

             
                System.out.printf("%-10d %-25s %-15s %-15s %-15d %-20d %-15s%n", batchId, productName, productCode, purchaseDate, purchasedQuantity, quantityAvailable, expiryDate);
            }

         
            System.out.println("=====================================================================================================================");

        } catch (SQLException e) {
            System.out.println("Error generating Batch-wise Stock Report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
