/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.service;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
//import com.mycompany.syos.database.dao.UserDAO;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.users.Manager;
import com.mycompany.syos.system.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;



import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.exceptions.InvalidUserException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthServiceTest {

    private UserDAO userDAO;
    private UserService userService;
    private ProductManagerCLI productManager;
    private Connection connection;
    private AuthService authService;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        userDAO = mock(UserDAO.class);
        userService = mock(UserService.class);
        productManager = mock(ProductManagerCLI.class);
        connection = mock(Connection.class);
        authService = new AuthService(userDAO, userService, productManager, connection);
        scanner = mock(Scanner.class);
    }

    @Test
    public void should_ReturnNull_When_InvalidCredentialsProvided() throws Exception {
        when(scanner.nextLine()).thenReturn("invalidUsername", "invalidPassword");
        when(userDAO.getUserByUsernameAndPassword("invalidUsername", "invalidPassword")).thenReturn(null);
        User result = authService.login(scanner);
        assertNull(result);
    }

    @Test
    public void should_CreateManager_When_RoleIsManager() throws Exception {
        when(scanner.nextLine()).thenReturn("managerUsername", "managerPassword");
        UserModel mockUserModel = new UserModel(1, "managerUsername", "passwordHash", "manager", "manager@example.com", null);
        when(userDAO.getUserByUsernameAndPassword("managerUsername", "managerPassword")).thenReturn(mockUserModel);

        User result = authService.login(scanner);

        assertTrue(result instanceof Manager);
    }

}
