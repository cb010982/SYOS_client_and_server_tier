/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.users;

/**
 *
 * @author User
 */
//import com.mycompany.syos.system.exceptions.OnlineInventoryRestockException;
//import com.mycompany.syos.system.exceptions.ShelfException;
//import com.mycompany.syos.system.model.OnlineInventory;
//import com.mycompany.syos.system.model.Shelf;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.OnlineInventoryRestockService;
import com.mycompany.syos.system.service.OnlineInventoryService;
import com.mycompany.syos.system.service.ShelfService;
import com.mycompany.syos.system.service.ShelfRestockService;
import com.mycompany.syos.system.service.StoreInventoryService;
import java.util.List;
import java.util.Scanner;

////new imports are added after this
import com.mycompany.database_syos.exceptions.OnlineInventoryRestockException;
import com.mycompany.database_syos.exceptions.ShelfException;
import com.mycompany.database_syos.models.OnlineInventory;
import com.mycompany.database_syos.models.Shelf;
import com.mycompany.database_syos.models.UserModel;

public class AssistantStoreKeeper implements User {
    private UserModel userModel;
    private StoreInventoryService storeInventoryService;
    private ShelfRestockService shelfRestockService;
    private ShelfService shelfService;
    private OnlineInventoryRestockService onlineInventoryRestockService;
    private OnlineInventoryService onlineInventoryService; 

    
    public AssistantStoreKeeper(UserModel userModel, StoreInventoryService storeInventoryService, 
                                ShelfRestockService shelfRestockService, ShelfService shelfService, 
                                OnlineInventoryRestockService onlineInventoryRestockService, 
                                OnlineInventoryService onlineInventoryService) { 
        this.userModel = userModel;
        this.storeInventoryService = storeInventoryService;
        this.shelfRestockService = shelfRestockService;
        this.shelfService = shelfService;
        this.onlineInventoryRestockService = onlineInventoryRestockService;
        this.onlineInventoryService = onlineInventoryService; 
    }

    @Override
    public void login() {
        System.out.println(userModel.getUsername() + " (Assistant Store Keeper) logged in.");
    }

    @Override
    public void signUp() {
        System.out.println("Assistant Store Keeper signed up with email: " + userModel.getEmail());
    }

    @Override
    public String getRole() {
        return userModel.getRole();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    
    public void showStoreKeeperMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Assistant Store Keeper Menu ---");
            System.out.println("1. Add New Batch");
            System.out.println("2. View Inventory by Product");
            System.out.println("3. Refill Shelf"); 
            System.out.println("4. Refill Online Inventory"); 
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1/2/3/4/5): ");

            int choice = storeInventoryService.getValidIntInput(scanner);  

            switch (choice) {
                case 1:
                    storeInventoryService.addNewBatch(scanner);  
                    break;
                case 2:
                    storeInventoryService.viewInventoryByProduct(scanner);  
                    break;
                case 3:
                    refillShelf(scanner);  
                    break;
                case 4:
                    refillOnlineInventory(scanner);  
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting Assistant Store Keeper Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void refillShelf(Scanner scanner) {
        try {
      
            shelfService.displayShelves();

            System.out.print("Enter Shelf ID to refill: ");
            int shelfId = storeInventoryService.getValidIntInput(scanner);

            System.out.print("Enter Quantity to Refill: ");
            int quantity = storeInventoryService.getValidIntInput(scanner);

         
            Shelf shelf = shelfService.findShelfById(shelfId);
            int productId = shelf.getProductId();

    
            shelfService.refillShelf(shelfId, quantity, shelfRestockService, storeInventoryService, productId);

            System.out.println("Shelf refilled successfully.");
        } catch (ShelfException e) {
            System.out.println("Error refilling shelf: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }
    
     private void refillOnlineInventory(Scanner scanner) {
        try {
           
            List<OnlineInventory> onlineInventories = onlineInventoryService.getAllOnlineInventories(); 

           
            if (onlineInventories.isEmpty()) {
                System.out.println("No online inventories available.");
                return;
            }

            System.out.println("\n--- Online Inventories ---");
            for (OnlineInventory inventory : onlineInventories) {
                System.out.println("ID: " + inventory.getOnlineInventoryId() + ", Product ID: " + inventory.getProductId() + ", Current Quantity: " + inventory.getCurrentQuantity() + ", Max Capacity: " + inventory.getMaxCapacity());
            }

            System.out.print("Enter Online Inventory ID to refill: ");
            int onlineInventoryId = storeInventoryService.getValidIntInput(scanner);

            System.out.print("Enter Quantity to Refill: ");
            int quantity = storeInventoryService.getValidIntInput(scanner);

           
            onlineInventoryRestockService.refillOnlineInventory(onlineInventoryId, quantity);

            System.out.println("Online inventory refilled successfully.");
        } catch (OnlineInventoryRestockException e) {
            System.out.println("Error refilling online inventory: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}