/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

import java.util.ArrayList;
import java.util.List;


import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.exceptions.OnlineInventoryRestockException;
import com.mycompany.database_syos.models.OnlineInventory;
import com.mycompany.database_syos.models.OnlineInventoryItem;
import com.mycompany.database_syos.models.StoreInventory;

/**
 *
 * @author User
 */
public class OnlineInventoryRestockService {
    private final StoreInventoryService storeInventoryService;
    private final OnlineInventoryService onlineInventoryService;
    private final OnlineInventoryDAO onlineInventoryDAO;
    private final ProductDAO productDAO; 
    private final OnlineInventoryItemDAO onlineInventoryItemDAO; 

    public OnlineInventoryRestockService(StoreInventoryService storeInventoryService, OnlineInventoryService onlineInventoryService, 
                                         OnlineInventoryDAO onlineInventoryDAO, ProductDAO productDAO, OnlineInventoryItemDAO onlineInventoryItemDAO) {
        this.storeInventoryService = storeInventoryService;
        this.onlineInventoryService = onlineInventoryService;
        this.onlineInventoryDAO = onlineInventoryDAO;
        this.productDAO = productDAO; 
        this.onlineInventoryItemDAO = onlineInventoryItemDAO; 
    }

    public void refillOnlineInventory(int onlineInventoryId, int quantityToRefill) throws OnlineInventoryRestockException { 
        try {
            OnlineInventory onlineInventory = onlineInventoryService.findOnlineInventoryById(onlineInventoryId);
            int requiredQuantity = onlineInventory.getMaxCapacity() - onlineInventory.getCurrentQuantity();

            if (requiredQuantity <= 0) {
                System.out.println("Online inventory is already at or above maximum capacity.");
                return;
            }

            int quantityToBeFilled = Math.min(requiredQuantity, quantityToRefill);
            System.out.println("Quantity to be filled: " + quantityToBeFilled);

            List<StoreInventory> inventoryItems;

            boolean isExpirable = productDAO.isProductExpirable(onlineInventory.getProductId());
            System.out.println("Is product expirable: " + isExpirable);

            if (isExpirable) {
                inventoryItems = storeInventoryService.getExpiringItemsByProductId(onlineInventory.getProductId());
            } else {
                inventoryItems = new ArrayList<>();
            }

            if (inventoryItems.isEmpty()) {
                inventoryItems = storeInventoryService.getOldestItemsByDateReceived(onlineInventory.getProductId());
            }

            int filledQuantity = 0;


            for (StoreInventory item : inventoryItems) {
                if (filledQuantity >= quantityToBeFilled) break; 

                OnlineInventoryItem onlineInventoryItem = new OnlineInventoryItem(
                        0, 
                        onlineInventoryId,
                        item.getItemSerialNumber(),
                        item.getBatchId(),
                        item.getProductId()
                );
                onlineInventoryItemDAO.addOnlineInventoryItem(onlineInventoryItem);

                storeInventoryService.updateInventoryQuantity(item.getInventoryId(), item.getQuantity() - 1);

                filledQuantity++;
            }

            onlineInventory.setCurrentQuantity(onlineInventory.getCurrentQuantity() + filledQuantity);
            onlineInventoryService.updateOnlineInventory(onlineInventory);

            System.out.println("Online inventory refilled successfully with " + filledQuantity + " items.");

        } catch (Exception e) {
            e.printStackTrace();  
            throw new OnlineInventoryRestockException("Error refilling the online inventory.", e);
        }
    }
    
}

