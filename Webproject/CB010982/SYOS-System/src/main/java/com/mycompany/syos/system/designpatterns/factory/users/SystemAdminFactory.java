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
import com.mycompany.syos.system.users.SystemAdmin;
//import com.mycompany.syos.system.model.UserModel;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
import com.mycompany.syos.system.service.UserService;
import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
import com.mycompany.syos.system.service.OnlineInventoryService;

//new imports are added after this
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidRoleException;

public class SystemAdminFactory implements UserFactory {

    private final UserService userService;
    private final ProductManagerCLI productManager;
    private final OnlineInventoryService onlineInventoryService;  

   
    public SystemAdminFactory(UserService userService, ProductManagerCLI productManager, OnlineInventoryService onlineInventoryService) {
        this.userService = userService;
        this.productManager = productManager;
        this.onlineInventoryService = onlineInventoryService;
    }

    @Override
    public User createUser(UserModel userModel) {
        if (!"admin".equalsIgnoreCase(userModel.getRole())) {
            throw new InvalidRoleException("Invalid role provided: " + userModel.getRole());
        }
        return new SystemAdmin(userModel, userService, productManager, onlineInventoryService);  
    }
}
