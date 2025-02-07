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
import com.mycompany.syos.system.users.OnlineCustomer; 
//import com.mycompany.syos.system.model.UserModel;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
import com.mycompany.syos.system.service.CartService;
import com.mycompany.syos.system.service.OnlineInventoryService;

//new imports are added after this
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidRoleException;

public class OnlineCustomerFactory implements UserFactory {
    private OnlineInventoryService onlineInventoryService;
    private CartService cartService;

    public OnlineCustomerFactory(OnlineInventoryService onlineInventoryService, CartService cartService) {
        this.onlineInventoryService = onlineInventoryService;
        this.cartService = cartService;
    }

    @Override
    public User createUser(UserModel userModel) {
        if (!"customer".equalsIgnoreCase(userModel.getRole())) {
            throw new InvalidRoleException("Invalid role provided: " + userModel.getRole());
        }

        return new OnlineCustomer(userModel, onlineInventoryService, cartService);
    }

}
