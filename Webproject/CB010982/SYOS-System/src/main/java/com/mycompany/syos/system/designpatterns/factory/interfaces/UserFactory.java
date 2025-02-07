/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.factory.interfaces;

/**
 *
 * @author User
 */

import com.mycompany.syos.system.users.User;  
//import com.mycompany.syos.system.model.UserModel;

//new imports are added after this
import com.mycompany.database_syos.models.UserModel;


public interface UserFactory {
    User createUser(UserModel userModel);  
}
