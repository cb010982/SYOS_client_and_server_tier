/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

package com.mycompany.syos.system.service;

//import com.mycompany.syos.database.dao.UserDAO;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.users.User;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.models.UserModel;

public class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new UserService(userDAO);
        scanner = mock(Scanner.class);
    }

    @Test
    public void should_ReturnAllUsers_when_ViewAllUsersIsCalled() {
        List<UserModel> mockUsers = Arrays.asList(
                new UserModel(1, "user1", "password1", "manager", "user1@syos.com", null),
                new UserModel(2, "user2", "password2", "cashier", "user2@syos.com", null)
        );

        when(userDAO.getAllUsers()).thenReturn(mockUsers);

        List<UserModel> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("manager", result.get(0).getRole());
    }

    @Test
    public void should_CreateNewUser_when_ValidInputIsProvided() {
        when(scanner.nextLine())
            .thenReturn("newUser") 
            .thenReturn("password")  
            .thenReturn("invalidRole")  
            .thenReturn("invalidRole2")  
            .thenReturn("customer")
            .thenReturn("invalidEmail")  
            .thenReturn("user@syos.co")  
            .thenReturn("user@syos.com");  

        UserModel newUser = new UserModel(0, "newUser", "password", "customer", "user@syos.com", null);

        doNothing().when(userDAO).createUser(any(UserModel.class));

        userService.createUser(scanner);

        verify(userDAO, times(1)).createUser(any(UserModel.class));
    }

    @Test
    public void should_DeleteUser_when_ValidUserIdIsProvided() {
        List<UserModel> mockUsers = Arrays.asList(
                new UserModel(1, "user1", "password1", "manager", "user1@syos.com", null)
        );

        when(userDAO.getAllUsers()).thenReturn(mockUsers);

        when(scanner.nextInt()).thenReturn(1); 
        when(scanner.nextLine()).thenReturn("yes");  

        userService.deleteUser(scanner);

        verify(userDAO, times(1)).deleteUser(1);
    }

}
