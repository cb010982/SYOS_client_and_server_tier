/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.factory.users;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.factory.users.ManagerFactory;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.ReportService;
import com.mycompany.syos.system.users.Manager;
import com.mycompany.syos.system.users.User;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidRoleException;

class ManagerFactoryTest {

    private ManagerFactory managerFactory;
    private ReportService reportService;
    private UserModel validManagerUserModel;
    private UserModel invalidUserModel;

    @BeforeEach
    public void setUp() {
        reportService = mock(ReportService.class);
        managerFactory = new ManagerFactory(reportService);
        validManagerUserModel = new UserModel(1, "managerUser", "passwordHash", "manager", "manager@syos.com", null);
        invalidUserModel = new UserModel(2, "cashierUser", "passwordHash", "cashier", "cashier@syos.com", null);
    }

    @Test
    public void should_CreateManager_when_ValidUserModelIsProvided() {

        User user = managerFactory.createUser(validManagerUserModel);

        assertNotNull(user);
        assertTrue(user instanceof Manager);
        assertEquals("manager", user.getRole()); 
    }

    @Test
    public void should_ThrowInvalidRoleException_when_InvalidUserModelIsProvided() {

        InvalidRoleException exception = assertThrows(
            InvalidRoleException.class,
            () -> managerFactory.createUser(invalidUserModel)
        );

        assertEquals("Invalid role provided: cashier", exception.getMessage());
    }
}
