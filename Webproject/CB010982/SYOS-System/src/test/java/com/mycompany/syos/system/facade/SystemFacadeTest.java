/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.facade;

import com.mycompany.syos.system.designpatterns.facade.SystemFacade;
//import com.mycompany.syos.database.dao.OnlineInventoryDAO;
import com.mycompany.syos.system.service.AuthService;
import com.mycompany.syos.system.service.MenuService;
import com.mycompany.syos.system.service.UserService;
import com.mycompany.syos.system.users.Cashier;
import com.mycompany.syos.system.users.OnlineCustomer;
import com.mycompany.syos.system.users.SystemAdmin;
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;        
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.Scanner;


import com.mycompany.database_syos.dao.OnlineInventoryDAO;

public class SystemFacadeTest {

    private SystemFacade systemFacade; // The facade under test
    private AuthService authService;   // Mocked AuthService
    private MenuService menuService;   // Mocked MenuService
    private UserService userService;   // Mocked UserService
    private OnlineInventoryDAO onlineInventoryDAO; // Mocked DAO
    private Scanner scanner;           // Mocked Scanner

    @BeforeEach
    public void setUp() throws Exception {
      
        systemFacade = Mockito.spy(new SystemFacade());

        // Mock dependencies
        authService = mock(AuthService.class);
        menuService = mock(MenuService.class);
        userService = mock(UserService.class);
        onlineInventoryDAO = mock(OnlineInventoryDAO.class);
        scanner = mock(Scanner.class);

      
        injectMock(systemFacade, "authService", authService);
        injectMock(systemFacade, "menuService", menuService);
        injectMock(systemFacade, "userService", userService);
        injectMock(systemFacade, "onlineInventoryDAO", onlineInventoryDAO);
    }

    @Test
    public void should_StartSystem_when_UserHasAccountAndInvalidCredentials() {
        when(scanner.nextLine()).thenReturn("yes");
        when(authService.login(scanner)).thenReturn(null);

        systemFacade.startSystem(scanner);

        verify(authService, times(1)).login(scanner);
    }

    @Test
    public void should_SignUpCustomer_when_UserDoesNotHaveAccount() {
        when(scanner.nextLine()).thenReturn("no");

        systemFacade.startSystem(scanner);

        verify(userService, times(1)).signUpCustomer(scanner);
    }

    @Test
    public void should_HandleAdminRole_when_UserIsAdmin() {
        when(scanner.nextLine()).thenReturn("yes");
        SystemAdmin mockAdmin = mock(SystemAdmin.class);
        when(authService.login(scanner)).thenReturn(mockAdmin);
        when(mockAdmin.getRole()).thenReturn("admin");

        systemFacade.startSystem(scanner);

        verify(mockAdmin, times(1)).showAdminMenu(scanner);
    }

    @Test
    public void should_HandleCashierRole_when_UserIsCashier() {
        when(scanner.nextLine()).thenReturn("yes");
        Cashier mockCashier = mock(Cashier.class);
        when(authService.login(scanner)).thenReturn(mockCashier);
        when(mockCashier.getRole()).thenReturn("cashier");

        systemFacade.startSystem(scanner);

        verify(mockCashier, times(1)).showCashierMenu(scanner);
    }

    @Test
    public void should_HandleCustomerRole_when_UserIsCustomer() throws Exception {
        when(scanner.nextLine()).thenReturn("yes");
        OnlineCustomer mockCustomer = mock(OnlineCustomer.class);
        when(authService.login(scanner)).thenReturn(mockCustomer);
        when(mockCustomer.getRole()).thenReturn("customer");

        systemFacade.startSystem(scanner);

        verify(mockCustomer, times(1)).showCustomerMenu(scanner);
    }

    
    private void injectMock(Object target, String fieldName, Object mockValue) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mockValue);
    }
}
