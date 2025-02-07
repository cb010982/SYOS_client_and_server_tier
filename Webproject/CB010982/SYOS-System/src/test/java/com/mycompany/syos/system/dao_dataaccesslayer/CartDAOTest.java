/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.dao_dataaccesslayer;


/**
 *
 * @author User
 */

import com.mycompany.database_syos.dao.CartDAO;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.models.Cart;
import com.mycompany.database_syos.models.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartDAOTest {

    private Connection connection;
    private CartDAO cartDAO;
    private DatabaseConnection mockDatabaseConnection; 

    @BeforeEach

public void setUp() throws SQLException {
    MockitoAnnotations.openMocks(this);

    DatabaseConnection mockDatabaseConnection = mock(DatabaseConnection.class);


    MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class);
    mockedStatic.when(DatabaseConnection::getInstance).thenReturn(mockDatabaseConnection);


    connection = mock(Connection.class);
    when(mockDatabaseConnection.getConnection()).thenReturn(connection);

    
    cartDAO = new CartDAO();
}
  @Test
    public void should_UpdateCartTotals_when_ValidCartIdAndTotalsProvided() throws Exception {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(pstmt);

        cartDAO.updateCartTotals(1, 100.0, 110.0, 90.0);

        verify(pstmt, times(1)).setDouble(1, 100.0);
        verify(pstmt, times(1)).setDouble(2, 110.0);
        verify(pstmt, times(1)).setDouble(3, 90.0);
        verify(pstmt, times(1)).setInt(4, 1);
        verify(pstmt, times(1)).executeUpdate();
    }
}
