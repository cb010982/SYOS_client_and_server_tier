/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

/**
 *
 * @author User
 */

import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.dao.ShelfItemDAO;
import com.mycompany.database_syos.exceptions.InventoryException;
import com.mycompany.database_syos.exceptions.ShelfDatabaseException;
import com.mycompany.database_syos.exceptions.ShelfException;
import com.mycompany.database_syos.exceptions.ShelfNotFoundException;
import com.mycompany.database_syos.models.Shelf;
import com.mycompany.database_syos.models.ShelfItem;
import com.mycompany.database_syos.models.StoreInventory;

import java.util.List;

public class ShelfService {
    private final ShelfDAO shelfDAO;
    private final ShelfItemDAO shelfItemDAO;
    
  public ShelfService(ShelfDAO shelfDAO, ShelfItemDAO shelfItemDAO) {
    this.shelfDAO = shelfDAO;
    this.shelfItemDAO = shelfItemDAO;
}

    public void addShelf(Shelf shelf) throws ShelfException {
        try {
            shelfDAO.addShelf(shelf); 
        } catch (ShelfDatabaseException e) { 
            throw new ShelfException("Error adding shelf to the database.", e); 
        }
    }

    public List<Shelf> getAllShelves() throws ShelfException {
        try {
            return shelfDAO.getAllShelves(); 
        } catch (ShelfDatabaseException e) { 
            throw new ShelfException("Error retrieving shelves from the database.", e); 
        }
    }

    public Shelf findShelfById(int shelfId) throws ShelfNotFoundException, ShelfException {
        try {
            Shelf shelf = shelfDAO.findShelfById(shelfId);
            if (shelf == null) {
                throw new ShelfNotFoundException("Shelf with ID " + shelfId + " not found.");
            }
            return shelf;
        } catch (ShelfDatabaseException e) { 
            throw new ShelfException("Error retrieving shelf from the database.", e);
        }
    }
        public void addShelfItem(ShelfItem shelfItem) throws ShelfException {
        try {
            shelfItemDAO.addShelfItem(shelfItem); 
        } catch (ShelfDatabaseException e) {
            throw new ShelfException("Error adding shelf item to the database.", e);
        }
    }

    public synchronized void updateShelf(Shelf shelf) throws ShelfException {
        try {
            boolean updated = shelfDAO.updateMaxCapacity(shelf);
            if (!updated) {
                throw new ShelfNotFoundException("Shelf with ID " + shelf.getShelfId() + " not found.");
            }
        } catch (ShelfDatabaseException e) {
            throw new ShelfException("Error updating shelf in the database.", e);
        }
    }

    public void updateShelfQuantity(int shelfId, int newQuantity) throws ShelfException {
        try {
            boolean updated = shelfDAO.updateShelfQuantity(shelfId, newQuantity);
            if (!updated) {
                throw new ShelfNotFoundException("Shelf with ID " + shelfId + " not found.");
            }
        } catch (ShelfDatabaseException e) {
            throw new ShelfException("Error updating shelf quantity in the database.", e);
        }
    }
    
    public void refillShelf(int shelfId, int quantity, ShelfRestockService restockService, StoreInventoryService inventoryService, int productId) throws ShelfException {
        try {
            Shelf shelf = findShelfById(shelfId);
            int availableSpace = shelf.getMaxCapacity() - shelf.getCurrentQuantity();

            if (quantity > availableSpace) {
                throw new ShelfException("Cannot refill more than available space. Available space: " + availableSpace);
            }

            List<StoreInventory> itemsToRestock = inventoryService.getItemsByProductIdSortedByExpiry(productId);

            if (itemsToRestock.isEmpty()) {
                itemsToRestock = inventoryService.getOldestItemsByDateReceived(productId);
            }

            if (itemsToRestock.size() < quantity) {
                throw new ShelfException("Insufficient items available in inventory for refilling. Requested: " + quantity + ", Available: " + itemsToRestock.size());
            }

            List<StoreInventory> itemsForShelf = itemsToRestock.subList(0, quantity);
            inventoryService.moveItemsToShelf(shelfId, itemsForShelf, this);

            updateShelfQuantity(shelfId, shelf.getCurrentQuantity() + quantity);

            System.out.println("Shelf refilled successfully with sorted items.");
        } catch (ShelfNotFoundException e) {
            throw new ShelfException("Shelf not found.", e);
        } catch (InventoryException e) {
            throw new ShelfException("Error retrieving items from inventory for refilling.", e);
        }
    }


    public void deleteShelf(int shelfId) throws ShelfException {
        try {
            boolean deleted = shelfDAO.deleteShelf(shelfId);
            if (!deleted) {
                throw new ShelfNotFoundException("Shelf with ID " + shelfId + " not found.");
            }
        } catch (ShelfDatabaseException e) {
            throw new ShelfException("Error deleting shelf from the database.", e);
        }
    }

    public void displayShelves() throws ShelfException {
        try {
            List<Shelf> shelves = getAllShelves();
            System.out.println("\n--- Available Shelves ---");
            for (Shelf shelf : shelves) {
                System.out.printf("Shelf ID: %d | Product ID: %d | Current Capacity: %d | Max Capacity: %d%n",
                        shelf.getShelfId(), shelf.getProductId(), shelf.getCurrentQuantity(), shelf.getMaxCapacity());
            }
        } catch (ShelfException e) {
            throw new ShelfException("Error displaying shelves.", e);
        }
    }
}

