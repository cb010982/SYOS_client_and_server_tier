/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.dao_dataaccesslayer;


/**
 *
 * @author User
 */

import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.exceptions.OnlineInventoryDatabaseException;
import com.mycompany.database_syos.models.OnlineInventory;
import com.mycompany.database_syos.models.OnlineInventoryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OnlineInventoryDAOTest {

    private Connection connection;
    private OnlineInventoryDAO onlineInventoryDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        connection = mock(Connection.class);
        onlineInventoryDAO = new OnlineInventoryDAO();
    }

    @Test
    public void testRemoveOnlineInventoryItem_Failure() throws Exception {
        int inventoryItemId = 1;
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(OnlineInventoryDatabaseException.class, () -> {
            onlineInventoryDAO.removeOnlineInventoryItem(inventoryItemId);
        });
    }


}
