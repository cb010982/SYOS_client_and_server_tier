/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

/**
 *
 * @author User
 */

import java.util.List;


import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.models.OnlineInventory;
import com.mycompany.database_syos.exceptions.OnlineInventoryDatabaseException;
import com.mycompany.database_syos.exceptions.OnlineInventoryException;
import com.mycompany.database_syos.exceptions.OnlineInventoryNotFoundException;

public class OnlineInventoryService {
    private final OnlineInventoryDAO onlineInventoryDAO;

    public OnlineInventoryService(OnlineInventoryDAO onlineInventoryDAO) {
        this.onlineInventoryDAO = onlineInventoryDAO;
    }

    public void addOnlineInventory(OnlineInventory onlineInventory) throws OnlineInventoryException, InterruptedException {
        try {
            onlineInventoryDAO.addOnlineInventory(onlineInventory);
        } catch (OnlineInventoryDatabaseException e) {
            throw new OnlineInventoryException("Error adding online inventory to the database.", e);
        }
    }

    public List<OnlineInventory> getAllOnlineInventories() throws OnlineInventoryException {
        try {
            return onlineInventoryDAO.getAllOnlineInventories();
        } catch (OnlineInventoryDatabaseException e) {
            throw new OnlineInventoryException("Error retrieving online inventories from the database.", e);
        }
    }

    public OnlineInventory findOnlineInventoryById(int onlineInventoryId) throws OnlineInventoryException, OnlineInventoryNotFoundException {
        try {
            OnlineInventory onlineInventory = onlineInventoryDAO.findOnlineInventoryById(onlineInventoryId);
            if (onlineInventory == null) {
                throw new OnlineInventoryNotFoundException("Online inventory with ID " + onlineInventoryId + " not found.");
            }
            return onlineInventory;
        } catch (OnlineInventoryDatabaseException e) {
            throw new OnlineInventoryException("Error retrieving online inventory from the database.", e);
        }
    }
    
    public void updateOnlineInventory(OnlineInventory onlineInventory) throws OnlineInventoryException, OnlineInventoryNotFoundException {
        try {
            boolean updated = onlineInventoryDAO.updateOnlineInventory(onlineInventory);
            if (!updated) {
                throw new OnlineInventoryNotFoundException("Online inventory with ID " + onlineInventory.getOnlineInventoryId() + " not found.");
            }
        } catch (OnlineInventoryDatabaseException e) {
            throw new OnlineInventoryException("Error updating online inventory in the database.", e);
        }
    }

    public void deleteOnlineInventory(int onlineInventoryId) throws OnlineInventoryException, OnlineInventoryNotFoundException {
        try {
            boolean deleted = onlineInventoryDAO.deleteOnlineInventory(onlineInventoryId);
            if (!deleted) {
                throw new OnlineInventoryNotFoundException("Online inventory with ID " + onlineInventoryId + " not found.");
            }
        } catch (OnlineInventoryDatabaseException e) {
            throw new OnlineInventoryException("Error deleting online inventory from the database.", e);
        }
    }
}
