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
//import com.mycompany.syos.database.dao.OnlineInventoryItemDAO;
//import com.mycompany.syos.database.dao.ProductDAO;
//import com.mycompany.syos.system.exceptions.OnlineInventoryRestockException;
//import com.mycompany.syos.system.model.OnlineInventory;
//import com.mycompany.syos.system.model.OnlineInventoryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.exceptions.OnlineInventoryRestockException;
import com.mycompany.database_syos.models.OnlineInventory;
import com.mycompany.database_syos.models.OnlineInventoryItem;
import com.mycompany.database_syos.dao.ProductDAO;


class OnlineInventoryRestockServiceTest {

    private OnlineInventoryRestockService restockService;
    private StoreInventoryService storeInventoryService;
    private OnlineInventoryService onlineInventoryService;
    private OnlineInventoryDAO onlineInventoryDAO;
    private ProductDAO productDAO;
    private OnlineInventoryItemDAO onlineInventoryItemDAO;

    @BeforeEach
    public void setUp() {
        storeInventoryService = mock(StoreInventoryService.class);
        onlineInventoryService = mock(OnlineInventoryService.class);
        onlineInventoryDAO = mock(OnlineInventoryDAO.class);
        productDAO = mock(ProductDAO.class);
        onlineInventoryItemDAO = mock(OnlineInventoryItemDAO.class);
        restockService = new OnlineInventoryRestockService(
                storeInventoryService, 
                onlineInventoryService, 
                onlineInventoryDAO, 
                productDAO, 
                onlineInventoryItemDAO
        );
    }

    @Test
    public void should_NotRefillOnlineInventory_When_InventoryIsFull() throws Exception {
        OnlineInventory onlineInventory = new OnlineInventory(1, 101, 100, 100, 10, null);  
        when(onlineInventoryService.findOnlineInventoryById(1)).thenReturn(onlineInventory);
        restockService.refillOnlineInventory(1, 20);  
        verify(onlineInventoryService, never()).updateOnlineInventory(any(OnlineInventory.class));
        verify(onlineInventoryItemDAO, never()).addOnlineInventoryItem(any(OnlineInventoryItem.class));
    }

    @Test
    public void should_ThrowException_When_RefillFails() throws Exception {
        OnlineInventory onlineInventory = new OnlineInventory(1, 101, 100, 50, 10, null);
        when(onlineInventoryService.findOnlineInventoryById(1)).thenReturn(onlineInventory);
        when(storeInventoryService.getOldestItemsByDateReceived(101)).thenThrow(new RuntimeException("Database error"));
        assertThrows(OnlineInventoryRestockException.class, () -> restockService.refillOnlineInventory(1, 30));
        verify(onlineInventoryItemDAO, never()).addOnlineInventoryItem(any(OnlineInventoryItem.class));
        verify(onlineInventoryService, never()).updateOnlineInventory(any(OnlineInventory.class));
    }


}
