/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.users;

/**
 *
 * @author User
 */

//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.BillService;
import java.util.Scanner;

////new imports are added after this
import com.mycompany.database_syos.models.UserModel;


public class Cashier implements User {
    private UserModel userModel;
    private BillService billService;

    
    public Cashier(UserModel userModel, BillService billService) {
        this.userModel = userModel;
        this.billService = billService;
    }

    @Override
    public void login() {
        System.out.println(userModel.getUsername() + " (Cashier) logged in.");
    }

    @Override
    public void signUp() {
        System.out.println("Cashier signed up with email: " + userModel.getEmail());
    }

    @Override
    public String getRole() {
        return userModel.getRole();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    public void showCashierMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Cashier Menu ---");
            System.out.println("1. Process Sale");
            System.out.println("2. View Transactions");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1/2/3): ");

            int choice = scanner.nextInt();  

            switch (choice) {
                case 1:
                    processSale(scanner);
                    break;
                case 2:
                    viewTransactions(scanner);
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting Cashier Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void processSale(Scanner scanner) {
        billService.processSale(scanner);
    }

    private void viewTransactions(Scanner scanner) {
        billService.viewTransactions(scanner);
    }
}
