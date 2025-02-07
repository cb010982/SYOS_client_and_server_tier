/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.users;

/**
 *
 * @author User
 */


import com.mycompany.syos.system.service.ReportService;
import java.util.Scanner;

////new imports are added after this
import com.mycompany.database_syos.models.UserModel;

public class Manager implements User {
    private UserModel userModel;
    private ReportService reportService;

    public Manager(UserModel userModel, ReportService reportService) {
        this.userModel = userModel;
        this.reportService = reportService;
    }

    @Override
    public void login() {
        System.out.println(userModel.getUsername() + " (Manager) logged in.");
    }

    @Override
    public void signUp() {
        System.out.println("Manager signed up with email: " + userModel.getEmail());
    }

    @Override
    public String getRole() {
        return userModel.getRole();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

   
    public void showManagerMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. View End Of Day Sales Report");
            System.out.println("2. View Reshelve Report");
            System.out.println("3. View Reorder Report");
            System.out.println("4. View Stock Report");
            System.out.println("5. View Bill Report");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1/2/3/4/5/6): ");

            int choice = scanner.nextInt();  

            switch (choice) {
                case 1:
                    viewEndOfDayTotalSalesReport();
                    break;
                case 2:
                    viewReshelveReport();
                    break;
                case 3:
                    viewReorderReport();
                    break;
                case 4:
                    viewStockReport();
                    break;
                case 5:
                    viewBillReport();
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting Manager Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewEndOfDayTotalSalesReport() {
        reportService.generateEndOfDaySalesReport();
    }

    private void viewReshelveReport() {
        reportService.generateReshelveReport();
    }

    private void viewReorderReport() {
        reportService.generateReorderReport();
    }

    private void viewStockReport() {
        reportService.generateStockReport();
    }

    private void viewBillReport() {
        reportService.generateBillReport();
    }
}
