package com.mycompany.syos.system.designpatterns.cliview;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
//import com.mycompany.syos.database.dao.ShelfDAO;
//import com.mycompany.syos.system.exceptions.ShelfException;
//import com.mycompany.syos.system.exceptions.ShelfNotFoundException;
//import com.mycompany.syos.system.model.Shelf;
import com.mycompany.syos.system.service.ShelfService;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

//new imports are added after this
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.exceptions.ShelfException;
import com.mycompany.database_syos.exceptions.ShelfNotFoundException;
import com.mycompany.database_syos.models.Shelf;

public class ShelfCLI {
    private final ShelfService shelfService;
    private final ShelfDAO shelfDAO;

    public ShelfCLI(ShelfService shelfService, ShelfDAO shelfDAO) {
        this.shelfService = shelfService;
        this.shelfDAO = shelfDAO; 
    }

    public void showShelfMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Shelf Management Menu ---");
            System.out.println("1. Add Shelf");
            System.out.println("2. View All Shelves");
            System.out.println("3. Update Shelf");
            System.out.println("4. Delete Shelf");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1/2/3/4/5): ");

            int choice = getValidIntInput(scanner);

            switch (choice) {
                case 1:
                    addShelf(scanner);
                    break;

                case 2:
                    viewAllShelves();
                    break;

                case 3:
                    updateShelf(scanner);
                    break;

                case 4:
                    deleteShelf(scanner);
                    break;

                case 5:
                    running = false;
                    System.out.println("Exiting Shelf Management Menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void addShelf(Scanner scanner) {
        try {
            System.out.println("\n--- Available Products ---");
            shelfDAO.displayAllProducts(); 

            System.out.print("Enter Product ID: ");
            int productId = getValidIntInput(scanner);

            System.out.print("Enter Max Capacity: ");
            int maxCapacity = getValidIntInput(scanner);

           
            Shelf shelf = new Shelf(productId, maxCapacity, new Timestamp(System.currentTimeMillis()));
            shelfService.addShelf(shelf);

            System.out.println("Shelf added successfully!");

        } catch (Exception e) {
            System.err.println("Error adding shelf: " + e.getMessage());
        }
    }


    private void viewAllShelves() {
        try {
            List<Shelf> shelves = shelfService.getAllShelves();
            if (shelves.isEmpty()) {
                System.out.println("No shelves found.");
            } else {
                System.out.println("\n--- Shelf List ---");
                for (Shelf shelf : shelves) {
                    System.out.println(shelf);
                }
            }
        } catch (ShelfException e) {
            System.err.println("Error retrieving shelves: " + e.getMessage());
        }
    }

    private void updateShelf(Scanner scanner) {
        try {

            System.out.println("\n--- Available Shelves ---");
            List<Shelf> shelves = shelfService.getAllShelves(); 
            if (shelves.isEmpty()) {
                System.out.println("No shelves available for update.");
                return; 
            }

            for (Shelf shelf : shelves) {

                System.out.println("Shelf ID: " + shelf.getShelfId() +
                                   ", Product ID: " + shelf.getProductId() +
                                   ", Max Capacity: " + shelf.getMaxCapacity() +
                                   ", Current Quantity: " + shelf.getCurrentQuantity());
            }


            System.out.print("Enter Shelf ID to Update: ");
            int shelfId = getValidIntInput(scanner);

            Shelf existingShelf = shelfService.findShelfById(shelfId);
            if (existingShelf == null) {
                System.out.println("Shelf with ID " + shelfId + " not found.");
                return;
            }

            System.out.print("Enter New Max Capacity: ");
            int maxCapacity = getValidIntInput(scanner);

  
            existingShelf.setMaxCapacity(maxCapacity);

            shelfService.updateShelf(existingShelf);
            System.out.println("Shelf updated successfully!");

        } catch (ShelfNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ShelfException e) {
            System.err.println("Error updating shelf: " + e.getMessage());
        }
    }


    private void deleteShelf(Scanner scanner) {
        try {
           
            System.out.println("\n--- Available Shelves ---");
            List<Shelf> shelves = shelfService.getAllShelves(); 
            if (shelves.isEmpty()) {
                System.out.println("No shelves available for deletion.");
                return; 
            }

            for (Shelf shelf : shelves) {
               
                System.out.println("Shelf ID: " + shelf.getShelfId() +
                                   ", Product ID: " + shelf.getProductId() +
                                   ", Max Capacity: " + shelf.getMaxCapacity() +
                                   ", Current Quantity: " + shelf.getCurrentQuantity());
            }

           
            System.out.print("Enter Shelf ID to Delete: ");
            int shelfId = getValidIntInput(scanner);

           
            System.out.print("Are you sure you want to delete shelf ID " + shelfId + "? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("yes")) {
                System.out.println("Shelf deletion cancelled.");
                return; 
            }

            
            shelfService.deleteShelf(shelfId);
            System.out.println("Shelf deleted successfully!");

        } catch (ShelfNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ShelfException e) {
            System.err.println("Error deleting shelf: " + e.getMessage());
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
}
