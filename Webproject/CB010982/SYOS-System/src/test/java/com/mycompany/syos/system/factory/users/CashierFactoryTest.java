/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.factory.users;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.factory.users.CashierFactory;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.BillService;
import com.mycompany.syos.system.users.Cashier;
import com.mycompany.syos.system.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidRoleException;

class CashierFactoryTest {

    private CashierFactory cashierFactory;
    private BillService billService;

    private UserModel validCashierModel;
    private UserModel invalidUserModel;

    @BeforeEach
    public void setUp() {
        billService = mock(BillService.class);
        cashierFactory = new CashierFactory(billService);
        validCashierModel = new UserModel(1, "cashierUser", "passwordHash", "cashier", "cashier@syos.com", null);
        invalidUserModel = new UserModel(2, "invalidUser", "passwordHash", "admin", "admin@syos.com", null);
    }

    @Test
    public void should_CreateCashier_when_ValidUserModelIsProvided() {

        User cashier = cashierFactory.createUser(validCashierModel);

        assertNotNull(cashier);
        assertTrue(cashier instanceof Cashier);  
        assertEquals("cashier", cashier.getRole()); 
    }

    @Test
    public void should_ThrowInvalidRoleException_when_InvalidRoleIsProvided() {
        
        InvalidRoleException exception = assertThrows(InvalidRoleException.class, () -> {
            cashierFactory.createUser(invalidUserModel);
        });

        assertEquals("Invalid role provided: admin", exception.getMessage());
    }
}
