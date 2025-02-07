/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.factory.users;

/**
 *
 * @author User
 */
import com.mycompany.syos.system.users.User;  
import com.mycompany.syos.system.designpatterns.factory.interfaces.UserFactory;
import com.mycompany.syos.system.users.AssistantStoreKeeper;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.OnlineInventoryRestockService;
import com.mycompany.syos.system.service.OnlineInventoryService;
import com.mycompany.syos.system.service.ShelfRestockService;
import com.mycompany.syos.system.service.ShelfService;
import com.mycompany.syos.system.service.StoreInventoryService;

//new imports are added after this
import com.mycompany.database_syos.models.UserModel;



public class AssistantStoreKeeperFactory implements UserFactory {
    private final StoreInventoryService storeInventoryService;
    private final ShelfRestockService shelfRestockService;
    private final ShelfService shelfService;
    private final OnlineInventoryRestockService onlineInventoryRestockService; 
    private final OnlineInventoryService onlineInventoryService; 

    public AssistantStoreKeeperFactory(StoreInventoryService storeInventoryService, ShelfRestockService shelfRestockService, 
                                       ShelfService shelfService, OnlineInventoryRestockService onlineInventoryRestockService, 
                                       OnlineInventoryService onlineInventoryService) {
        this.storeInventoryService = storeInventoryService;
        this.shelfRestockService = shelfRestockService;
        this.shelfService = shelfService;
        this.onlineInventoryRestockService = onlineInventoryRestockService; 
        this.onlineInventoryService = onlineInventoryService; 
    }

    @Override
    public User createUser(UserModel userModel) {
        return new AssistantStoreKeeper(userModel, storeInventoryService, shelfRestockService, shelfService, 
                                        onlineInventoryRestockService, onlineInventoryService); 
    }
}
