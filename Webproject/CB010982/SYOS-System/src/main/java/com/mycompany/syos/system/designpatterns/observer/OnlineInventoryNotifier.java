/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.observer;
/**
 *
 * @author User
 */
//import com.mycompany.syos.system.model.OnlineInventory;
import java.util.ArrayList;
import java.util.List;

//new imports are added after this
import com.mycompany.database_syos.models.OnlineInventory;


public class OnlineInventoryNotifier {
    private List<OnlineInventory> inventoryItems;

    public OnlineInventoryNotifier() {
        this.inventoryItems = new ArrayList<>();
    }

    public String checkRestockThreshold() {
        StringBuilder notifications = new StringBuilder();

        for (OnlineInventory item : inventoryItems) {
            System.out.println("Checking product: " + item.getProductId());
            System.out.println("Current Quantity: " + item.getCurrentQuantity() + " Restock Threshold: " + item.getRestockThreshold());

            if (item.getCurrentQuantity() <= item.getRestockThreshold()) {
                notifications.append("Item ").append(item.getProductId()).append(" has reached the restock threshold.\n");
            }
        }

        if (notifications.length() > 0) {
            return notifications.toString();
        }

        return null;  
    }

    public void addInventoryItem(OnlineInventory item) {
        inventoryItems.add(item);
    }

    public boolean isRestockThresholdReached() {
        for (OnlineInventory item : inventoryItems) {
            if (item.getCurrentQuantity() <= item.getRestockThreshold()) {
                return true;
            }
        }
        return false;
    }

    public void notifyObservers() {
        String message = checkRestockThreshold();
        if (message != null) {
            System.out.println("Notification: " + message);
        }
    }
}

