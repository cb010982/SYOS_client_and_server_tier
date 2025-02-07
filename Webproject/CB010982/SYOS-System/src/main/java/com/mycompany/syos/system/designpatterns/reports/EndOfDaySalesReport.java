/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.reports;
/**
 *
 * @author User
 */
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

public class EndOfDaySalesReport implements ReportStrategy {

    @Override
    public void generateReport() {
        int consoleWidth = 120;
        String title = "--- End of Day Sales Report ---";
        String dateLine = "Date Generated: " + LocalDate.now();


        int titlePaddingSize = (consoleWidth - title.length()) / 2;
        String titlePadding = " ".repeat(Math.max(0, titlePaddingSize));


        int datePaddingSize = (consoleWidth - dateLine.length()) / 2;
        String datePadding = " ".repeat(Math.max(0, datePaddingSize));


        System.out.println("=================================================================================================================================");
        System.out.println(titlePadding + title);
        System.out.println(datePadding + dateLine);
        System.out.println("=================================================================================================================================");
        System.out.printf("%-25s %-15s %-12s %-12s %-12s %-15s %-15s %-15s%n", "Product Name", "Product Code", "Online Qty", "Shelf Qty", "Total Qty", "Online Revenue", "Shelf Revenue", "Total Revenue");
        System.out.println("=================================================================================================================================");

        double onlineSalesRevenue = 0.0; 
        double instoreSalesRevenue = 0.0; 
        double grandTotalRevenue = 0.0; 

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {

            PreparedStatement pstmtOnline = conn.prepareStatement(ReportQueries.GET_ONLINE_SALES_REPORT);
            ResultSet rsOnline = pstmtOnline.executeQuery();


            PreparedStatement pstmtShelf = conn.prepareStatement(ReportQueries.GET_SHELF_SALES_REPORT);
            ResultSet rsShelf = pstmtShelf.executeQuery();


            PreparedStatement pstmtOnlineRevenue = conn.prepareStatement(ReportQueries.GET_ONLINE_SALES_REVENUE);
            ResultSet rsOnlineRevenue = pstmtOnlineRevenue.executeQuery();
            if (rsOnlineRevenue.next()) {
                onlineSalesRevenue = rsOnlineRevenue.getDouble("online_sales_revenue");
            }


            PreparedStatement pstmtInstoreRevenue = conn.prepareStatement(ReportQueries.GET_INSTORE_SALES_REVENUE);
            ResultSet rsInstoreRevenue = pstmtInstoreRevenue.executeQuery();
            if (rsInstoreRevenue.next()) {
                instoreSalesRevenue = rsInstoreRevenue.getDouble("instore_sales_revenue");
            }


            while (rsOnline.next() && rsShelf.next()) {
                String productNameOnline = rsOnline.getString("name");
                String productCodeOnline = rsOnline.getString("product_code");
                int onlineQuantitySold = rsOnline.getInt("online_quantity_sold");
                double onlineRevenue = rsOnline.getDouble("online_revenue");

                String productNameShelf = rsShelf.getString("name");
                String productCodeShelf = rsShelf.getString("product_code");
                int shelfQuantitySold = rsShelf.getInt("shelf_quantity_sold");
                double shelfRevenue = rsShelf.getDouble("shelf_revenue");


                int totalQuantitySold = onlineQuantitySold + shelfQuantitySold;
                double totalRevenue = onlineRevenue + shelfRevenue;


                System.out.printf("%-25s %-15s %-12d %-12d %-12d %-15.2f %-15.2f %-15.2f%n", 
                                  productNameOnline, productCodeOnline, onlineQuantitySold, shelfQuantitySold, totalQuantitySold, onlineRevenue, shelfRevenue, totalRevenue);
            }


            grandTotalRevenue = onlineSalesRevenue + instoreSalesRevenue;

            
            System.out.println("=================================================================================================================================");
            System.out.printf("%-30s $%.2f%n", "Online Sales Revenue:", onlineSalesRevenue);
            System.out.printf("%-30s $%.2f%n", "In-Store Sales Revenue:", instoreSalesRevenue);
            System.out.printf("%-30s $%.2f%n", "Total Revenue for the Day:", grandTotalRevenue);
            System.out.println("=================================================================================================================================");

        } catch (SQLException e) {
            System.out.println("Error generating End of Day Sales Report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
