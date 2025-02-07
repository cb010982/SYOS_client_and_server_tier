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
//import com.mycompany.syos.system.model.OnlineInventory;
import com.mycompany.syos.system.designpatterns.observer.OnlineInventoryNotifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import com.mycompany.database_syos.models.OnlineInventory;
import com.mycompany.database_syos.dao.OnlineInventoryDAO;


class InventoryNotificationServiceTest {

    private OnlineInventoryDAO onlineInventoryDAOMock;
    private InventoryNotificationService inventoryNotificationService;
    private OnlineInventoryNotifier notifierMock;

    @BeforeEach
    public void setUp() {
        onlineInventoryDAOMock = mock(OnlineInventoryDAO.class);
        inventoryNotificationService = new InventoryNotificationService(onlineInventoryDAOMock);
        notifierMock = mock(OnlineInventoryNotifier.class);
    }

}
