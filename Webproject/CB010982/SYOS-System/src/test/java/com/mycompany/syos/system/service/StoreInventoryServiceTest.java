/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.service;
/**
 *
 * @author User
 */
//import com.mycompany.syos.database.dao.ProductDAO;
//import com.mycompany.syos.database.dao.StoreInventoryDAO;
//import com.mycompany.syos.system.exceptions.InventoryException;
//import com.mycompany.syos.system.model.Product;
//import com.mycompany.syos.system.model.ShelfItem;
//import com.mycompany.syos.system.model.StoreInventory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.StoreInventoryDAO;
import com.mycompany.database_syos.exceptions.InventoryException;
import com.mycompany.database_syos.models.ShelfItem;
import com.mycompany.database_syos.models.StoreInventory;

class StoreInventoryServiceTest {

    @Mock
    private StoreInventoryDAO storeInventoryDAO;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private Connection connection;

    @InjectMocks
    private StoreInventoryService storeInventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        storeInventoryService = new StoreInventoryService(storeInventoryDAO, productDAO, connection);
    }
    
    @Test
    public void should_GetInventoryByProductIdSuccessfully() throws InventoryException {

        int productId = 1;
        List<StoreInventory> mockInventoryList = new ArrayList<>();
        mockInventoryList.add(new StoreInventory(1, 1, 1, 10, LocalDate.now(), true, "SN123"));
        when(storeInventoryDAO.getInventoryByProductId(productId)).thenReturn(mockInventoryList);

        List<StoreInventory> result = storeInventoryService.getInventoryByProductId(productId);

        assertEquals(1, result.size());
        verify(storeInventoryDAO, times(1)).getInventoryByProductId(productId);
    }

    @Test
    public void should_ThrowInventoryException_When_GetInventoryFails() throws InventoryException {
        when(storeInventoryDAO.getInventoryByProductId(anyInt())).thenThrow(new InventoryException("Error retrieving inventory"));

        assertThrows(InventoryException.class, () -> storeInventoryService.getInventoryByProductId(1));
    }

    @Test
    public void should_GetOldestItemsByProductIdSuccessfully() throws InventoryException {

        int productId = 1;
        List<StoreInventory> mockInventoryList = new ArrayList<>();
        mockInventoryList.add(new StoreInventory(1, 1, 1, 10, LocalDate.now(), true, "SN123"));
        when(storeInventoryDAO.getItemsSortedByOldest(productId)).thenReturn(mockInventoryList);

        List<StoreInventory> result = storeInventoryService.getOldestItemsByProductId(productId);

        assertEquals(1, result.size());
        verify(storeInventoryDAO, times(1)).getItemsSortedByOldest(productId);
    }

    @Test
    public void should_RefillShelfSuccessfully() throws Exception {

        List<StoreInventory> mockInventoryList = new ArrayList<>();
        mockInventoryList.add(new StoreInventory(1, 1, 1, 1, LocalDate.now(), true, "SN123"));
        when(storeInventoryDAO.getInventoryByBatchId(1)).thenReturn(mockInventoryList);

        ShelfService shelfService = mock(ShelfService.class);

        storeInventoryService.moveItemsToShelf(1, mockInventoryList, shelfService);

        verify(storeInventoryDAO, times(1)).deleteInventoryById(1);
        verify(shelfService, times(1)).addShelfItem(any(ShelfItem.class));
    }

    @Test
    public void should_ThrowInventoryException_When_RefillFails() throws Exception {

        when(storeInventoryDAO.getInventoryByBatchId(anyInt())).thenThrow(new InventoryException("Error"));

        assertThrows(InventoryException.class, () -> storeInventoryService.getInventoryByBatchId(1));
    }

    @Test
    public void should_UpdateInventoryQuantitySuccessfully() throws InventoryException {
      
        StoreInventory inventory = new StoreInventory(1, 1, 1, 5, LocalDate.now(), true, "SN123");
        when(storeInventoryDAO.getInventoryById(1)).thenReturn(inventory);

        storeInventoryService.updateInventoryQuantity(1, 3);

        verify(storeInventoryDAO, times(1)).updateInventoryQuantity(1, 3);
    }

}
