/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.users;

/**
 *
 * @author User
 */

import com.mycompany.syos.system.designpatterns.cliview.OnlineInventoryCLI;
import com.mycompany.syos.system.designpatterns.cliview.ShelfCLI;
//import com.mycompany.syos.system.model.UserModel;  
import java.util.Scanner;
import com.mycompany.syos.system.service.UserService;
import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
//import com.mycompany.syos.database.dao.OnlineInventoryDAO;
//import com.mycompany.syos.database.dao.ShelfDAO;
//import com.mycompany.syos.database.dao.ShelfItemDAO;
//import com.mycompany.syos.database.connection.DatabaseConnection;
import com.mycompany.syos.system.service.OnlineInventoryService;
import com.mycompany.syos.system.service.ShelfService;
import java.sql.Connection;

//new imports are added after this
import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.dao.ShelfItemDAO;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.models.UserModel; 


public class SystemAdmin implements User {

    private UserModel userModel;
    private UserService userService;
    private ProductManagerCLI productManager;
    private OnlineInventoryService onlineInventoryService;  

    
    public SystemAdmin(UserModel userModel, UserService userService, ProductManagerCLI productManager, OnlineInventoryService onlineInventoryService) {
        this.userModel = userModel;
        this.userService = userService;
        this.productManager = productManager;
        this.onlineInventoryService = onlineInventoryService;  
    }
    @Override
    public void login() {
        System.out.println(userModel.getUsername() + " (System Admin) logged in.");
       
    }

    @Override
    public void signUp() {
        System.out.println("System Admin signed up with email: " + userModel.getEmail());
    }

    @Override
    public String getRole() {
        return userModel.getRole();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();  
    }

    
public void showAdminMenu(Scanner scanner) {
    if (scanner == null) {
        throw new IllegalArgumentException("Scanner cannot be null in showAdminMenu.");
    }

    boolean running = true;

    while (running) {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. User Management");
        System.out.println("2. Product Management");
        System.out.println("3. Shelf Management");
        System.out.println("4. Online Inventory Management");
        System.out.println("5. Exit");
        System.out.print("Enter your choice (1/2/3/4/5): ");

        int choice = getValidIntInput(scanner);

        switch (choice) {
            case 1:
                manageUsers(scanner);
                break;

            case 2:
                manageProducts(scanner);
                break;

            case 3:
                manageShelves(scanner);
                break;

            case 4:
                manageOnlineInventory(scanner);
                break;

            case 5:
                running = false;
                System.out.println("Exiting Admin Menu.");
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

    private void manageUsers(Scanner scanner) {
        System.out.println("\n--- User Management ---");
        userService.showUserMenu(scanner);  
    }
    private void manageShelves(Scanner scanner) {
        try (Connection connection = getConnection()) {  
            ShelfDAO shelfDAO = new ShelfDAO();  
            ShelfItemDAO shelfItemDAO = new ShelfItemDAO();  
            ShelfService shelfService = new ShelfService(shelfDAO, shelfItemDAO);  
            ShelfCLI shelfCLI = new ShelfCLI(shelfService, shelfDAO);  
            shelfCLI.showShelfMenu(scanner);
        } catch (Exception e) {
            System.err.println("Error managing shelves: " + e.getMessage());
        }
    }

private Connection getConnection() {
    return DatabaseConnection.getInstance().getConnection(); // Use the Singleton instance
}


   
    private void manageProducts(Scanner scanner) {
        while (true) {
            System.out.println("--- Product Management ---");
            System.out.println("1. Add Product");
            System.out.println("2. Edit Product");
            System.out.println("3. Delete Product");
             System.out.println("4. View All Products");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice (1/2/3/4): ");

            int choice = getValidIntInput(scanner);

            switch (choice) {
                case 1:
                    productManager.addProduct(this); 
                    break;
                case 2:
                    productManager.editProduct(scanner);  
                    break;
                case 3:
                    productManager.deleteProduct(scanner); 
                    break;
                 case 4:
                productManager.viewAllProducts();  
                break;
            case 5:
                return; 
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private void manageOnlineInventory(Scanner scanner) {
        try (Connection connection = getConnection()) {
            OnlineInventoryDAO onlineInventoryDAO = new OnlineInventoryDAO();
            OnlineInventoryService onlineInventoryService = new OnlineInventoryService(onlineInventoryDAO);


            OnlineInventoryCLI onlineInventoryCLI = new OnlineInventoryCLI(onlineInventoryService, onlineInventoryDAO);

            onlineInventoryCLI.showOnlineInventoryMenu(scanner);
        } catch (Exception e) {
            System.err.println("Error managing online inventory: " + e.getMessage());
        }
    }
    
    private int getValidIntInput(Scanner scanner) {
        int input = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                input = scanner.nextInt();  
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();  
            }
        }

        return input;
    }
}

