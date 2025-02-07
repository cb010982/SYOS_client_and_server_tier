/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.cliview;
/**
 *
 * @author User
 */
//import com.mycompany.syos.database.dao.OnlineInventoryDAO;
//import com.mycompany.syos.system.exceptions.OnlineInventoryException;
//import com.mycompany.syos.system.exceptions.OnlineInventoryNotFoundException;
//import com.mycompany.syos.system.model.OnlineInventory;
import com.mycompany.syos.system.service.OnlineInventoryService;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

//new imports are added after this
import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.exceptions.OnlineInventoryException;
import com.mycompany.database_syos.exceptions.OnlineInventoryNotFoundException;
import com.mycompany.database_syos.models.OnlineInventory;

public class OnlineInventoryCLI {
    private final OnlineInventoryService onlineInventoryService;
    private final OnlineInventoryDAO onlineInventoryDAO; 

    public OnlineInventoryCLI(OnlineInventoryService onlineInventoryService, OnlineInventoryDAO onlineInventoryDAO) {
        this.onlineInventoryService = onlineInventoryService;
        this.onlineInventoryDAO = onlineInventoryDAO; 
    }

    public void showOnlineInventoryMenu(Scanner scanner) throws OnlineInventoryNotFoundException {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Online Inventory Management Menu ---");
            System.out.println("1. Add Online Inventory");
            System.out.println("2. View All Online Inventories");
            System.out.println("3. Update Online Inventory");
            System.out.println("4. Delete Online Inventory");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1/2/3/4/5): ");

            int choice = getValidIntInput(scanner);

            switch (choice) {
                case 1:
                    addOnlineInventory(scanner);
                    break;

                case 2:
                    viewAllOnlineInventories();
                    break;

                case 3:
                    updateOnlineInventory(scanner);
                    break;

                case 4:
                    deleteOnlineInventory(scanner);
                    break;

                case 5:
                    running = false;
                    System.out.println("Exiting Online Inventory Management Menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addOnlineInventory(Scanner scanner) {
          try {
              System.out.println("\n--- Available Products ---");
              onlineInventoryDAO.displayAllProducts();

              System.out.print("Enter Product ID: ");
              int productId = getValidIntInput(scanner);

              System.out.print("Enter Max Capacity: ");
              int maxCapacity = getValidIntInput(scanner);

              System.out.print("Enter Restock Threshold: ");
              int restockThreshold = getValidIntInput(scanner);

              OnlineInventory onlineInventory = new OnlineInventory(
                      0,
                      productId,
                      maxCapacity,
                      0,
                      restockThreshold,
                      new Timestamp(System.currentTimeMillis())
              );
              onlineInventoryService.addOnlineInventory(onlineInventory);
              System.out.println("Online inventory added successfully!");

          } catch (Exception e) {
              System.err.println("Error adding online inventory: " + e.getMessage());
          }
    }

    private int getValidIntInput(Scanner scanner) {
        int input = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                if (scanner.hasNextInt()) {
                    input = scanner.nextInt();
                    validInput = true;
                    scanner.nextLine(); 
                } else {
                    System.out.print("Invalid input. Please enter a valid number: ");
                    scanner.next();
                }
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a valid number: ");
                scanner.next();
            }
        }

        return input;
    }
    
    private void viewAllOnlineInventories() {
        try {
            List<OnlineInventory> onlineInventories = onlineInventoryService.getAllOnlineInventories();
            if (onlineInventories.isEmpty()) {
                System.out.println("No online inventories found.");
            } else {
                System.out.println("\n--- Online Inventory List ---");
                for (OnlineInventory onlineInventory : onlineInventories) {
                    System.out.println(onlineInventory);
                }
            }
        } catch (OnlineInventoryException e) {
            System.err.println("Error retrieving online inventories: " + e.getMessage());
        }
    }

    private void updateOnlineInventory(Scanner scanner) throws OnlineInventoryNotFoundException {
        try {

            viewAllOnlineInventories();  

            System.out.print("Enter Online Inventory ID to Update: ");
            int onlineInventoryId = getValidIntInput(scanner);

            OnlineInventory existingOnlineInventory = onlineInventoryService.findOnlineInventoryById(onlineInventoryId);
            if (existingOnlineInventory == null) {
                System.out.println("Online inventory with ID " + onlineInventoryId + " not found.");
                return;
            }

            System.out.println("\n--- Current Inventory Details ---");
            System.out.println("Product ID: " + existingOnlineInventory.getProductId());
            System.out.println("Max Capacity: " + existingOnlineInventory.getMaxCapacity());
            System.out.println("Current Quantity: " + existingOnlineInventory.getCurrentQuantity());
            System.out.println("Restock Threshold: " + existingOnlineInventory.getRestockThreshold());

            System.out.print("Enter New Max Capacity: ");
            int maxCapacity = getValidIntInput(scanner);

            System.out.print("Enter New Restock Threshold: ");
            int restockThreshold = getValidIntInput(scanner);

            existingOnlineInventory.setMaxCapacity(maxCapacity);
            existingOnlineInventory.setRestockThreshold(restockThreshold);

            onlineInventoryService.updateOnlineInventory(existingOnlineInventory);
            System.out.println("Online inventory updated successfully!");

        } catch (OnlineInventoryException e) {
            System.err.println("Error updating online inventory: " + e.getMessage());
        }
    }

    private void deleteOnlineInventory(Scanner scanner) throws OnlineInventoryNotFoundException {
        try {

            viewAllOnlineInventories(); 

            System.out.print("Enter Online Inventory ID to Delete: ");
            int onlineInventoryId = getValidIntInput(scanner);

            System.out.print("Are you sure you want to delete online inventory ID " + onlineInventoryId + "? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("yes")) {
                System.out.println("Online inventory deletion cancelled.");
                return;
            }

            onlineInventoryService.deleteOnlineInventory(onlineInventoryId);
            System.out.println("Online inventory deleted successfully!");

        } catch (OnlineInventoryException e) {
            System.err.println("Error deleting online inventory: " + e.getMessage());
        }
    }

}
