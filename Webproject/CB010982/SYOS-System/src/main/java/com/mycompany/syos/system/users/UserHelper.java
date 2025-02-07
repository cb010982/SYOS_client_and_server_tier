/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.users;

import com.mycompany.database_syos.models.UserModel;


/**
 *
 * @author User
 */
public class UserHelper {
    public static UserModel convertToUserModel(User user) {
        if (user instanceof OnlineCustomer) {
            return ((OnlineCustomer) user).getUserModel(); // Extract UserModel from OnlineCustomer
        } else if (user instanceof UserModel) {
            return (UserModel) user; 
        } else {
            System.err.println("User is not an instance of UserModel or OnlineCustomer. Received: " + user.getClass().getName());
            return null;
        }
    }
}
