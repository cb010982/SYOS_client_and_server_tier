/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.observer;

/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.observer.OnlineInventoryNotifier;
//import com.mycompany.syos.system.model.OnlineInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.mycompany.database_syos.models.OnlineInventory;



class OnlineInventoryNotifierTest {

    private OnlineInventoryNotifier notifier;
    private OnlineInventory mockInventoryItem;

    @BeforeEach
    public void setUp() {
        notifier = new OnlineInventoryNotifier();
        mockInventoryItem = mock(OnlineInventory.class);  
    }

    @Test
    public void should_AddInventoryItem_when_ItemIsAdded() {
        notifier.addInventoryItem(mockInventoryItem);
        assertTrue(notifier.isRestockThresholdReached());  
    }

    @Test
    public void should_ReturnNotification_when_RestockThresholdIsReached() {
        when(mockInventoryItem.getProductId()).thenReturn(1);
        when(mockInventoryItem.getCurrentQuantity()).thenReturn(5);
        when(mockInventoryItem.getRestockThreshold()).thenReturn(10);
        
        notifier.addInventoryItem(mockInventoryItem);
        
        String notification = notifier.checkRestockThreshold();
        assertNotNull(notification);
        assertTrue(notification.contains("Item 1 has reached the restock threshold."));
    }

    @Test
    public void should_NotReturnNotification_when_RestockThresholdIsNotReached() {
        when(mockInventoryItem.getProductId()).thenReturn(1);
        when(mockInventoryItem.getCurrentQuantity()).thenReturn(15);
        when(mockInventoryItem.getRestockThreshold()).thenReturn(10);
        
        notifier.addInventoryItem(mockInventoryItem);
        
        String notification = notifier.checkRestockThreshold();
        assertNull(notification); 
    }

    @Test
    public void should_NotifyObservers_when_RestockThresholdIsReached() {
        when(mockInventoryItem.getProductId()).thenReturn(1);
        when(mockInventoryItem.getCurrentQuantity()).thenReturn(5);
        when(mockInventoryItem.getRestockThreshold()).thenReturn(10);

        notifier.addInventoryItem(mockInventoryItem);
        
        assertDoesNotThrow(() -> notifier.notifyObservers());
    }

    @Test
    public void should_ReturnTrue_when_AtLeastOneItemHasReachedRestockThreshold() {
        when(mockInventoryItem.getCurrentQuantity()).thenReturn(5);
        when(mockInventoryItem.getRestockThreshold()).thenReturn(10);
        
        notifier.addInventoryItem(mockInventoryItem);
        assertTrue(notifier.isRestockThresholdReached());
    }

    @Test
    public void should_ReturnFalse_when_NoItemHasReachedRestockThreshold() {
        when(mockInventoryItem.getCurrentQuantity()).thenReturn(15);
        when(mockInventoryItem.getRestockThreshold()).thenReturn(10);

        notifier.addInventoryItem(mockInventoryItem);
        assertFalse(notifier.isRestockThresholdReached());
    }
}
