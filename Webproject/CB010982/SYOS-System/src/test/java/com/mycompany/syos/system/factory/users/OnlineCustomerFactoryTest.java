/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.factory.users;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.factory.users.OnlineCustomerFactory;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.CartService;
import com.mycompany.syos.system.service.OnlineInventoryService;
import com.mycompany.syos.system.users.OnlineCustomer;
import com.mycompany.syos.system.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidRoleException;

class OnlineCustomerFactoryTest {

    private OnlineCustomerFactory onlineCustomerFactory;
    private OnlineInventoryService onlineInventoryService;
    private CartService cartService;
    private UserModel validCustomerModel;
    private UserModel invalidUserModel;

    @BeforeEach
    public void setUp() {
        onlineInventoryService = mock(OnlineInventoryService.class);
        cartService = mock(CartService.class);
        onlineCustomerFactory = new OnlineCustomerFactory(onlineInventoryService, cartService);
        validCustomerModel = new UserModel(1, "customerUser", "passwordHash", "customer", "customer@syos.com", null);
        invalidUserModel = new UserModel(2, "invalidUser", "passwordHash", "admin", "admin@syos.com", null);
    }

    @Test
    public void should_CreateOnlineCustomer_when_ValidUserModelIsProvided() {

        User onlineCustomer = onlineCustomerFactory.createUser(validCustomerModel);
        assertNotNull(onlineCustomer);
        assertTrue(onlineCustomer instanceof OnlineCustomer);  
        assertEquals("customer", onlineCustomer.getRole());  
    }

    @Test
    public void should_ThrowInvalidRoleException_when_InvalidRoleIsProvided() {

        InvalidRoleException exception = assertThrows(InvalidRoleException.class, () -> {
            onlineCustomerFactory.createUser(invalidUserModel);
        });
        assertEquals("Invalid role provided: admin", exception.getMessage());
    }
}
