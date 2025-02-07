/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.service;
/**
 *
 * @author User
 */
//import com.mycompany.syos.database.dao.OnlineInventoryDAO;
//import com.mycompany.syos.system.exceptions.OnlineInventoryDatabaseException;
//import com.mycompany.syos.system.exceptions.OnlineInventoryException;
//import com.mycompany.syos.system.exceptions.OnlineInventoryNotFoundException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.exceptions.OnlineInventoryDatabaseException;
import com.mycompany.database_syos.exceptions.OnlineInventoryNotFoundException;
import com.mycompany.database_syos.exceptions.OnlineInventoryException;



public class OnlineInventoryServiceTest {

    @Mock
    private OnlineInventoryDAO onlineInventoryDAO;

    @InjectMocks
    private OnlineInventoryService onlineInventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  
    }

    @Test
    public void Should_ThrowOnlineInventoryException_When_DAOThrowsException() throws Exception {
        when(onlineInventoryDAO.getAllOnlineInventories()).thenThrow(new OnlineInventoryDatabaseException("DB error"));

        OnlineInventoryException exception = assertThrows(OnlineInventoryException.class, () -> {
            onlineInventoryService.getAllOnlineInventories();
        });

        assertEquals("Error retrieving online inventories from the database.", exception.getMessage());
    }


    @Test
    public void Should_ThrowOnlineInventoryNotFoundException_When_NotFound() throws Exception {
        when(onlineInventoryDAO.findOnlineInventoryById(1)).thenReturn(null);

        OnlineInventoryNotFoundException exception = assertThrows(OnlineInventoryNotFoundException.class, () -> {
            onlineInventoryService.findOnlineInventoryById(1);
        });

        assertEquals("Online inventory with ID 1 not found.", exception.getMessage());
    }

    @Test
    public void Should_DeleteInventorySuccessfully() throws Exception {
        when(onlineInventoryDAO.deleteOnlineInventory(1)).thenReturn(true);

        assertDoesNotThrow(() -> onlineInventoryService.deleteOnlineInventory(1));

        verify(onlineInventoryDAO, times(1)).deleteOnlineInventory(1);
    }

    @Test
    public void Should_ThrowOnlineInventoryNotFoundException_When_DeletionFails() throws Exception {
        when(onlineInventoryDAO.deleteOnlineInventory(1)).thenReturn(false);

        OnlineInventoryNotFoundException exception = assertThrows(OnlineInventoryNotFoundException.class, () -> {
            onlineInventoryService.deleteOnlineInventory(1);
        });

        assertEquals("Online inventory with ID 1 not found.", exception.getMessage());
    }
}
