/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

/**
 *
 * @author User
 */

import com.mycompany.syos.system.service.StoreInventoryService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.dao.ShelfRestockLogDAO;
import com.mycompany.database_syos.exceptions.ShelfRestockException;
import com.mycompany.database_syos.models.Shelf;
import com.mycompany.database_syos.models.ShelfItem;
import com.mycompany.database_syos.models.ShelfRestockLog;
import com.mycompany.database_syos.models.StoreInventory;

public class ShelfRestockService {
    private final StoreInventoryService storeInventoryService;
    private final ShelfService shelfService;
    private final ShelfDAO shelfDAO;
    private final ShelfRestockLogDAO shelfRestockLogDAO;
    private final ProductDAO productDAO; 

    public ShelfRestockService(StoreInventoryService storeInventoryService, ShelfService shelfService, ShelfDAO shelfDAO, ShelfRestockLogDAO shelfRestockLogDAO, ProductDAO productDAO) {
        this.storeInventoryService = storeInventoryService;
        this.shelfService = shelfService;
        this.shelfDAO = shelfDAO;
        this.shelfRestockLogDAO = shelfRestockLogDAO;
        this.productDAO = productDAO;
    }

    public void refillShelf(int shelfId, int quantityToRefill) throws ShelfRestockException { 
        try {

            Shelf shelf = shelfDAO.findShelfById(shelfId);
            int requiredQuantity = shelf.getMaxCapacity() - shelf.getCurrentQuantity();

            if (requiredQuantity <= 0) {
                System.out.println("Shelf is already at or above maximum capacity.");
                return;
            }

            int quantityToBeFilled = Math.min(requiredQuantity, quantityToRefill);
            System.out.println("Quantity to be filled: " + quantityToBeFilled);

            List<StoreInventory> inventoryItems;

            boolean isExpirable = productDAO.isProductExpirable(shelf.getProductId());
            System.out.println("Is product expirable: " + isExpirable);

     
            if (isExpirable) {
                inventoryItems = storeInventoryService.getExpiringItemsByProductId(shelf.getProductId());
            } else {
                inventoryItems = new ArrayList<>();
            }

            if (inventoryItems.isEmpty()) {
                inventoryItems = storeInventoryService.getOldestItemsByDateReceived(shelf.getProductId());
            }

            int filledQuantity = 0;

            for (StoreInventory item : inventoryItems) {
                if (filledQuantity >= quantityToBeFilled) break; 

                ShelfItem shelfItem = new ShelfItem(shelfId, item.getBatchId(), item.getItemSerialNumber(), item.getProductId());
                shelfService.addShelfItem(shelfItem);

                storeInventoryService.updateInventoryQuantity(item.getInventoryId(), item.getQuantity() - 1);

                filledQuantity++;
            }

            shelfDAO.updateShelfQuantity(shelfId, shelf.getCurrentQuantity() + filledQuantity);
            System.out.println("Updated shelf quantity: " + (shelf.getCurrentQuantity() + filledQuantity));

            ShelfRestockLog log = new ShelfRestockLog();
            log.setShelfId(shelfId);
            log.setQuantity(filledQuantity);
            log.setRestockTimestamp(new Timestamp(System.currentTimeMillis()));
            shelfRestockLogDAO.addRestockLog(log);

            System.out.println("Shelf refilled successfully with " + filledQuantity + " items.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ShelfRestockException("Error refilling the shelf.", e);
        }
    }


}

