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
import com.mycompany.syos.system.users.Manager;  
//import com.mycompany.syos.system.model.UserModel;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
import com.mycompany.syos.system.service.ReportService;

//new imports are added after this
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidRoleException;

public class ManagerFactory implements UserFactory {
    private ReportService reportService;

    public ManagerFactory(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public User createUser(UserModel userModel) {
        if (!"manager".equalsIgnoreCase(userModel.getRole())) {
            throw new InvalidRoleException("Invalid role provided: " + userModel.getRole());
        }
        return new Manager(userModel, reportService);
    }
}

