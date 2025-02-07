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
public class ReshelveReport implements ReportStrategy {

    @Override
    public void generateReport() {
        int consoleWidth = 100;  
        String title = "--- Reshelve Report ---";
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

          
            String shelfSectionTitle = "--- Shelf Inventory Reshelve Report ---";
            int shelfTitlePaddingSize = (consoleWidth - shelfSectionTitle.length()) / 2;
            String shelfTitlePadding = " ".repeat(Math.max(0, shelfTitlePaddingSize));

           
            System.out.println("\n" + shelfTitlePadding + shelfSectionTitle);
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("%-25s %-15s %-15s%n", "Product Name", "Product Code", "Reshelve Amount");
            System.out.println("--------------------------------------------------------------------------------------------------");

            PreparedStatement pstmtShelf = conn.prepareStatement(ReportQueries.GET_SHELF_RESHELVE_REPORT);
            ResultSet rsShelf = pstmtShelf.executeQuery();

          
            while (rsShelf.next()) {
                String productName = rsShelf.getString("name");
                String productCode = rsShelf.getString("product_code");
                int reshelveAmount = rsShelf.getInt("reshelve_amount");

              
                System.out.printf("%-25s %-15s %-15d%n", productName, productCode, reshelveAmount);
            }

           
            String onlineSectionTitle = "--- Online Inventory Reshelve Report ---";
            int onlineTitlePaddingSize = (consoleWidth - onlineSectionTitle.length()) / 2;
            String onlineTitlePadding = " ".repeat(Math.max(0, onlineTitlePaddingSize));

         
            System.out.println("\n" + onlineTitlePadding + onlineSectionTitle);
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("%-25s %-15s %-15s%n", "Product Name", "Product Code", "Reshelve Amount");
            System.out.println("--------------------------------------------------------------------------------------------------");

            PreparedStatement pstmtOnline = conn.prepareStatement(ReportQueries.GET_ONLINE_RESHELVE_REPORT);
            ResultSet rsOnline = pstmtOnline.executeQuery();

           
            while (rsOnline.next()) {
                String productName = rsOnline.getString("name");
                String productCode = rsOnline.getString("product_code");
                int reshelveAmount = rsOnline.getInt("reshelve_amount");

                
                System.out.printf("%-25s %-15s %-15d%n", productName, productCode, reshelveAmount);
            }

       
            System.out.println("==================================================================================================");

        } catch (SQLException e) {
            System.out.println("Error generating Reshelve Report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
