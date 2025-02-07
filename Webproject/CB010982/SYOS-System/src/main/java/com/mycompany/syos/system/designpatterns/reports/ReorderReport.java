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
public class ReorderReport implements ReportStrategy {

    @Override
    public void generateReport() {
        int consoleWidth = 100;  
        String title = "--- Reorder Report ---";
        String dateLine = "Date Generated: " + LocalDate.now();


        int titlePaddingSize = (consoleWidth - title.length()) / 2;
        String titlePadding = " ".repeat(Math.max(0, titlePaddingSize));


        int datePaddingSize = (consoleWidth - dateLine.length()) / 2;
        String datePadding = " ".repeat(Math.max(0, datePaddingSize));


        System.out.println("==================================================================================================");
        System.out.println(titlePadding + title);
        System.out.println(datePadding + dateLine);
        System.out.println("==================================================================================================");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {


            PreparedStatement pstmtReorder = conn.prepareStatement(ReportQueries.GET_REORDER_REPORT);
            ResultSet rsReorder = pstmtReorder.executeQuery();


            System.out.printf("%-25s %-15s %-25s %-15s%n", "Product Name", "Product Code", "Main Category", "Current Quantity");
            System.out.println("--------------------------------------------------------------------------------------------------");


            while (rsReorder.next()) {
                String productName = rsReorder.getString("name");
                String productCode = rsReorder.getString("product_code");
                String mainCategory = rsReorder.getString("main_category_name");
                int currentQuantity = rsReorder.getInt("current_quantity");


                System.out.printf("%-25s %-15s %-25s %-15d%n", productName, productCode, mainCategory, currentQuantity);
            }

            System.out.println("==================================================================================================");

    
            System.out.println("\nReorder Thresholds:");
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println("Items with Quantity < 50: Food, Beverages, Stationery, Medicine, Baby Products, Kids Products");
            System.out.println("Items with Quantity < 30: Household, Beauty Products");
            System.out.println("Items with Quantity < 20: Electronics, Hardware");
            System.out.println("==================================================================================================");

        } catch (SQLException e) {
            System.out.println("Error generating Reorder Report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
