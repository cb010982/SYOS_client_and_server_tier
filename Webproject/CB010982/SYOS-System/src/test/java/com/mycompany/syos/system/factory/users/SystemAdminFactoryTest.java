/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.factory.users;


/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.factory.users.SystemAdminFactory;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.OnlineInventoryService;
import com.mycompany.syos.system.service.UserService;
import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
import com.mycompany.syos.system.users.SystemAdmin;
import com.mycompany.syos.system.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidRoleException;

class SystemAdminFactoryTest {

    private SystemAdminFactory systemAdminFactory;
    private UserService userService;
    private ProductManagerCLI productManager;
    private OnlineInventoryService onlineInventoryService;

    private UserModel validAdminModel;
    private UserModel invalidUserModel;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        productManager = mock(ProductManagerCLI.class);
        onlineInventoryService = mock(OnlineInventoryService.class);
        systemAdminFactory = new SystemAdminFactory(userService, productManager, onlineInventoryService);
        validAdminModel = new UserModel(1, "adminUser", "passwordHash", "admin", "admin@syos.com", null);
        invalidUserModel = new UserModel(2, "invalidUser", "passwordHash", "customer", "customer@syos.com", null);
    }

    @Test
    public void should_CreateSystemAdmin_when_ValidUserModelIsProvided() {

        User systemAdmin = systemAdminFactory.createUser(validAdminModel);

        assertNotNull(systemAdmin);
        assertTrue(systemAdmin instanceof SystemAdmin);  
        assertEquals("admin", systemAdmin.getRole()); 
    }

    @Test
    public void should_ThrowInvalidRoleException_when_InvalidRoleIsProvided() {

        InvalidRoleException exception = assertThrows(InvalidRoleException.class, () -> {
            systemAdminFactory.createUser(invalidUserModel);
        });

        assertEquals("Invalid role provided: customer", exception.getMessage());
    }
}
