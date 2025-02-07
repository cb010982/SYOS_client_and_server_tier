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
//import com.mycompany.syos.database.dao.ShelfDAO;
//import com.mycompany.syos.database.dao.ShelfDAO;
//import com.mycompany.syos.system.model.Shelf;
//import com.mycompany.syos.system.model.ShelfItem;
//import com.mycompany.syos.system.model.ShelfRestockLog;
//import com.mycompany.syos.system.model.StoreInventory;
import static org.mockito.Mockito.*;
import org.mockito.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;

import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.ShelfRestockLogDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.models.Shelf;
import com.mycompany.database_syos.models.ShelfItem;
import com.mycompany.database_syos.models.ShelfRestockLog;
import com.mycompany.database_syos.models.StoreInventory;


class ShelfRestockServiceTest {

    @Mock
    private StoreInventoryService storeInventoryService;

    @Mock
    private ShelfService shelfService;

    @Mock
    private ShelfDAO shelfDAO;

    @Mock
    private ShelfRestockLogDAO shelfRestockLogDAO;

    @Mock
    private ProductDAO productDAO;

    private ShelfRestockService shelfRestockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shelfRestockService = new ShelfRestockService(
            storeInventoryService, 
            shelfService, 
            shelfDAO, 
            shelfRestockLogDAO, 
            productDAO
        );
    }

    @Test
    public void should_NotRefill_When_ShelfIsAtOrAboveCapacity() throws Exception {
        Shelf shelf = new Shelf(1, 1, 10, 10, new Timestamp(System.currentTimeMillis()));  
        when(shelfDAO.findShelfById(1)).thenReturn(shelf);

        shelfRestockService.refillShelf(1, 5);

        verify(shelfService, never()).addShelfItem(any(ShelfItem.class));
        verify(storeInventoryService, never()).updateInventoryQuantity(anyInt(), anyInt());
        verify(shelfRestockLogDAO, never()).addRestockLog(any(ShelfRestockLog.class));
        System.out.println("Test: No refilling when shelf is already at capacity passed.");
    }

    @Test
    public void should_RefillShelfWithExpirableItems() throws Exception {
        Shelf shelf = new Shelf(1, 1, 10, 5, new Timestamp(System.currentTimeMillis()));  
        List<StoreInventory> expiringItems = List.of(
            new StoreInventory(1, 1, 1, 1, null, true, "PRODCODE123"),
            new StoreInventory(2, 1, 1, 1, null, true, "PRODCODE456")
        );

        when(shelfDAO.findShelfById(1)).thenReturn(shelf);
        when(productDAO.isProductExpirable(anyInt())).thenReturn(true);
        when(storeInventoryService.getExpiringItemsByProductId(anyInt())).thenReturn(expiringItems);

        shelfRestockService.refillShelf(1, 2);

        verify(shelfService, times(2)).addShelfItem(any(ShelfItem.class));
        verify(storeInventoryService, times(2)).updateInventoryQuantity(anyInt(), anyInt());
        verify(shelfRestockLogDAO, times(1)).addRestockLog(any(ShelfRestockLog.class));
    }

    @Test
    public void should_RefillShelfWithOldestItems_When_NoExpirableItems() throws Exception {
        Shelf shelf = new Shelf(1, 1, 10, 5, new Timestamp(System.currentTimeMillis()));  
        List<StoreInventory> oldestItems = List.of(
            new StoreInventory(1, 1, 1, 1, null, false, "PRODCODE789"),
            new StoreInventory(2, 1, 1, 1, null, false, "PRODCODE999")
        );
        when(shelfDAO.findShelfById(1)).thenReturn(shelf);
        when(productDAO.isProductExpirable(anyInt())).thenReturn(false); 
        when(storeInventoryService.getOldestItemsByDateReceived(anyInt())).thenReturn(oldestItems);

        shelfRestockService.refillShelf(1, 2);

        verify(shelfService, times(2)).addShelfItem(any(ShelfItem.class));
        verify(storeInventoryService, times(2)).updateInventoryQuantity(anyInt(), anyInt());
        verify(shelfRestockLogDAO, times(1)).addRestockLog(any(ShelfRestockLog.class));
    } 
}
