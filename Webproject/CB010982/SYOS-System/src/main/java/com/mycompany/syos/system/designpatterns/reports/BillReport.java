package com.mycompany.syos.system.designpatterns.reports;

//import com.mycompany.syos.database.dao.ReportQueries;
//import com.mycompany.syos.database.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */

//new imports are added after this
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.dao.ReportQueries;


public class BillReport implements ReportStrategy {

    @Override
    public void generateReport() {
        int consoleWidth = 100;  
        String title = "--- Customer Transactions Report ---";
        String dateLine = "Date Generated: " + LocalDate.now();

    
        int titlePaddingSize = (consoleWidth - title.length()) / 2;
        String titlePadding = " ".repeat(Math.max(0, titlePaddingSize));

   
        int datePaddingSize = (consoleWidth - dateLine.length()) / 2;
        String datePadding = " ".repeat(Math.max(0, datePaddingSize));

   
        System.out.println("=".repeat(consoleWidth));
        System.out.println(titlePadding + title);
        System.out.println(datePadding + dateLine);
        System.out.println("=".repeat(consoleWidth));

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {

            System.out.println("\n--- Bill Transactions ---");

         
            PreparedStatement pstmtBills = conn.prepareStatement(ReportQueries.GET_BILL_TRANSACTIONS);
            ResultSet rsBills = pstmtBills.executeQuery();

            double totalBillAmount = 0;

            while (rsBills.next()) {
           
                String billNo = rsBills.getString("bill_no");
                String date = rsBills.getString("date");
                double grossTotal = rsBills.getDouble("gross_total");
                double discount = rsBills.getDouble("discount");
                double netTotal = rsBills.getDouble("net_total");
                double amountReceived = rsBills.getDouble("cash_received");
                double changeGiven = rsBills.getDouble("change_given");
                String paymentType = rsBills.getString("payment_type");

                totalBillAmount += netTotal;

              
                System.out.printf("Bill No: %-10s Date: %-15s Payment: %-10s%n", billNo, date, paymentType);
                System.out.printf("Gross Total: $%-10.2f Discount: $%-10.2f%n", grossTotal, discount);
                if ("Cash".equalsIgnoreCase(paymentType)) {
                    System.out.printf("Net Total: $%-10.2f Amount Received: $%-10.2f Change Given: $%-10.2f%n", netTotal, amountReceived, changeGiven);
                } else {
                    System.out.printf("Net Total: $%-10.2f Amount Received (Card): $%-10.2f%n", netTotal, amountReceived);
                }

                System.out.println("-".repeat(consoleWidth));
                System.out.printf("%-25s %-20s %-10s %-10s%n", "Item Name", "Product Code", "Quantity", "Price");
                System.out.println("-".repeat(consoleWidth));

             
                PreparedStatement pstmtBillItems = conn.prepareStatement(ReportQueries.GET_BILL_ITEMS);
                pstmtBillItems.setString(1, billNo);
                ResultSet rsBillItems = pstmtBillItems.executeQuery();

                while (rsBillItems.next()) {
                    String productName = rsBillItems.getString("product_name");
                    String productCode = rsBillItems.getString("product_code");
                    int quantity = rsBillItems.getInt("quantity");
                    double price = rsBillItems.getDouble("price");

              
                    System.out.printf("%-25s %-20s %-10d $%-9.2f%n", productName, productCode, quantity, price);
                }

                System.out.println();
            }

            System.out.printf("\nTotal for all Bills: $%.2f%n", totalBillAmount);
            System.out.println("=".repeat(consoleWidth));

      
            System.out.println("\n--- Cart Transactions ---");

         
            PreparedStatement pstmtCarts = conn.prepareStatement(ReportQueries.GET_CART_TRANSACTIONS);
            ResultSet rsCarts = pstmtCarts.executeQuery();

            double totalCartAmount = 0;

            while (rsCarts.next()) {
       
                String cartNo = rsCarts.getString("cart_no");
                String date = rsCarts.getString("date");
                double grossTotal = rsCarts.getDouble("gross_total");
                double discount = rsCarts.getDouble("discount");
                double netTotal = rsCarts.getDouble("net_total");
                String telephone = rsCarts.getString("telephone");
                String address = rsCarts.getString("address");

                totalCartAmount += netTotal;

                
                System.out.printf("Cart No: %-10s Date: %-15s Telephone: %-12s%n", cartNo, date, telephone);
                System.out.printf("Gross Total: $%-10.2f Discount: $%-10.2f Net Total: $%-10.2f%n", grossTotal, discount, netTotal);
                System.out.printf("Address: %-20s%n", address);

                System.out.println("-".repeat(consoleWidth));
                System.out.printf("%-25s %-20s %-10s %-10s%n", "Item Name", "Product Code", "Quantity", "Price");
                System.out.println("-".repeat(consoleWidth));

           
                PreparedStatement pstmtCartItems = conn.prepareStatement(ReportQueries.GET_CART_ITEMS);
                pstmtCartItems.setString(1, cartNo);
                ResultSet rsCartItems = pstmtCartItems.executeQuery();

                while (rsCartItems.next()) {
                    String productName = rsCartItems.getString("product_name");
                    String productCode = rsCartItems.getString("product_code");
                    int quantity = rsCartItems.getInt("quantity");
                    double price = rsCartItems.getDouble("price");

                  
                    System.out.printf("%-25s %-20s %-10d $%-9.2f%n", productName, productCode, quantity, price);
                }

                System.out.println(); 
            }

            System.out.printf("\nTotal for all Carts: $%.2f%n", totalCartAmount);
            System.out.println("=".repeat(consoleWidth));

        } catch (SQLException e) {
            System.out.println("Error generating Customer Transactions Report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
