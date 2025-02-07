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
//import com.mycompany.syos.database.dao.ShelfItemDAO;
//import com.mycompany.syos.system.exceptions.ShelfDatabaseException;
//import com.mycompany.syos.system.exceptions.ShelfException;
//import com.mycompany.syos.system.exceptions.ShelfNotFoundException;
//import com.mycompany.syos.system.exceptions.InventoryException;
//import com.mycompany.syos.system.model.Shelf;
//import com.mycompany.syos.system.model.StoreInventory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.exceptions.ShelfDatabaseException;
import com.mycompany.database_syos.exceptions.ShelfException;
import com.mycompany.database_syos.exceptions.ShelfNotFoundException;
import com.mycompany.database_syos.exceptions.InventoryException;
import com.mycompany.database_syos.dao.ShelfItemDAO;
import com.mycompany.database_syos.models.Shelf;
import com.mycompany.database_syos.models.StoreInventory;

class ShelfServiceTest {

    @Mock
    private ShelfDAO shelfDAO;

    @Mock
    private ShelfItemDAO shelfItemDAO;

    @Mock
    private ShelfRestockService restockService;

    @Mock
    private StoreInventoryService inventoryService;

    @Mock
    private ProductDAO productDAO;

    private ShelfService shelfService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shelfService = new ShelfService(shelfDAO, shelfItemDAO);
    }

    @Test
    public void should_AddShelfSuccessfully() throws ShelfException, ShelfDatabaseException {
     
        Shelf shelf = new Shelf(1, 1, 50, 20, new Timestamp(System.currentTimeMillis()));

        shelfService.addShelf(shelf);

        verify(shelfDAO, times(1)).addShelf(shelf);
    }

    @Test
    public void should_ThrowShelfException_When_AddingShelfFails() throws ShelfException, ShelfDatabaseException {

        Shelf shelf = new Shelf(1, 1, 50, 20, new Timestamp(System.currentTimeMillis()));
        doThrow(new ShelfDatabaseException("Database error")).when(shelfDAO).addShelf(any(Shelf.class));

        assertThrows(ShelfException.class, () -> shelfService.addShelf(shelf));
    }

    @Test
    public void should_GetAllShelvesSuccessfully() throws ShelfException, ShelfDatabaseException {

        List<Shelf> shelves = new ArrayList<>();
        shelves.add(new Shelf(1, 1, 50, 20, new Timestamp(System.currentTimeMillis())));
        when(shelfDAO.getAllShelves()).thenReturn(shelves);

        List<Shelf> result = shelfService.getAllShelves();

        assertEquals(1, result.size());
        verify(shelfDAO, times(1)).getAllShelves();
    }

    @Test
    public void should_ThrowShelfException_When_GetAllShelvesFails() throws ShelfException, ShelfDatabaseException {
 
        when(shelfDAO.getAllShelves()).thenThrow(new ShelfDatabaseException("Database error"));

        assertThrows(ShelfException.class, () -> shelfService.getAllShelves());
    }

    @Test
    public void should_FindShelfByIdSuccessfully() throws ShelfException, ShelfNotFoundException, ShelfDatabaseException {

        Shelf shelf = new Shelf(1, 1, 50, 20, new Timestamp(System.currentTimeMillis()));
        when(shelfDAO.findShelfById(1)).thenReturn(shelf);

        Shelf result = shelfService.findShelfById(1);

        assertNotNull(result);
        assertEquals(1, result.getShelfId());
        verify(shelfDAO, times(1)).findShelfById(1);
    }

    @Test
    public void should_ThrowShelfNotFoundException_When_ShelfNotFound() throws ShelfException, ShelfDatabaseException {

        when(shelfDAO.findShelfById(1)).thenReturn(null);

        assertThrows(ShelfNotFoundException.class, () -> shelfService.findShelfById(1));
    }

    @Test
    public void should_ThrowShelfException_When_FindShelfByIdFails() throws ShelfException, ShelfDatabaseException {

        when(shelfDAO.findShelfById(1)).thenThrow(new ShelfDatabaseException("Database error"));

        assertThrows(ShelfException.class, () -> shelfService.findShelfById(1));
    }

    @Test
    public void should_UpdateShelfSuccessfully() throws ShelfException, ShelfDatabaseException {

        Shelf shelf = new Shelf(1, 1, 50, 20, new Timestamp(System.currentTimeMillis()));
        when(shelfDAO.updateMaxCapacity(shelf)).thenReturn(true);

        shelfService.updateShelf(shelf);

        verify(shelfDAO, times(1)).updateMaxCapacity(shelf);
    }

    @Test
    public void should_ThrowShelfException_When_UpdateShelfFails() throws ShelfException, ShelfDatabaseException {

        Shelf shelf = new Shelf(1, 1, 50, 20, new Timestamp(System.currentTimeMillis()));
        doThrow(new ShelfDatabaseException("Database error")).when(shelfDAO).updateMaxCapacity(shelf);

        assertThrows(ShelfException.class, () -> shelfService.updateShelf(shelf));
    }

    @Test
    public void should_DeleteShelfSuccessfully() throws ShelfException, ShelfDatabaseException {

        when(shelfDAO.deleteShelf(1)).thenReturn(true);

        shelfService.deleteShelf(1);

        verify(shelfDAO, times(1)).deleteShelf(1);
    }

    @Test
    public void should_ThrowShelfException_When_DeleteShelfFails() throws ShelfException, ShelfDatabaseException {
 
        when(shelfDAO.deleteShelf(1)).thenThrow(new ShelfDatabaseException("Database error"));

        assertThrows(ShelfException.class, () -> shelfService.deleteShelf(1));
    }

    @Test
    public void should_ThrowShelfException_When_InsufficientItemsForRefill() throws ShelfException, InventoryException, ShelfNotFoundException, ShelfDatabaseException {

        Shelf shelf = new Shelf(1, 1, 50, 20, new Timestamp(System.currentTimeMillis()));
        List<StoreInventory> inventoryItems = new ArrayList<>();

        when(shelfDAO.findShelfById(1)).thenReturn(shelf);
        when(inventoryService.getItemsByProductIdSortedByExpiry(1)).thenReturn(inventoryItems);

        assertThrows(ShelfException.class, () -> shelfService.refillShelf(1, 5, restockService, inventoryService, 1));
    }
}
