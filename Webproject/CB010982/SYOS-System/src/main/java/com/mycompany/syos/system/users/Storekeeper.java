/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.users;

/**
 *
 * @author User
 */

//import com.mycompany.syos.system.model.UserModel;

////new imports are added after this
import com.mycompany.database_syos.models.UserModel;


public class Storekeeper implements User {

    private UserModel userModel;

    public Storekeeper(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void login() {
        System.out.println(userModel.getUsername() + " (Storekeeper) logged in.");
    }

    @Override
    public void signUp() {
        System.out.println("Storekeeper signed up with email: " + userModel.getEmail());
    }

    @Override
    public String getRole() {
        return userModel.getRole();
    }
        @Override
    public String getUsername() {
        return userModel.getUsername();  
    }
}
