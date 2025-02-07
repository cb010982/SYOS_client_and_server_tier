/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.service;

/**
 *
 * @author User
 */

//import com.mycompany.syos.database.dao.OnlineInventoryDAO;
//import com.mycompany.syos.system.model.OnlineInventory;
import com.mycompany.syos.system.designpatterns.observer.OnlineInventoryNotifier;
import java.sql.SQLException;
import java.util.List;


import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.models.OnlineInventory;

public class InventoryNotificationService {
    private OnlineInventoryDAO onlineInventoryDAO;

    public InventoryNotificationService(OnlineInventoryDAO onlineInventoryDAO) {
        this.onlineInventoryDAO = onlineInventoryDAO;
    }
    public void checkAndNotifyRestock() {
        try {
            OnlineInventoryNotifier onlineInventoryNotifier = new OnlineInventoryNotifier();

            List<OnlineInventory> onlineInventoryItems = onlineInventoryDAO.getAllInventoryItems(100, 0);

            for (OnlineInventory inventory : onlineInventoryItems) {
                onlineInventoryNotifier.addInventoryItem(inventory);
            }

            if (onlineInventoryNotifier.isRestockThresholdReached()) {
                onlineInventoryNotifier.notifyObservers();
            }
        } catch (SQLException e) {
            System.err.println("Error fetching online inventory items: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}

