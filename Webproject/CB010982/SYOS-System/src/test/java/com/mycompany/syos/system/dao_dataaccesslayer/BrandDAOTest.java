/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.dao_dataaccesslayer;


/**
 *
 * @author User
 */

import com.mycompany.database_syos.dao.BrandDAO;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import com.mycompany.database_syos.models.Brand;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
public class BrandDAOTest {

    @Mock
    private Connection connection; // Mock the Connection

    @InjectMocks
    private BrandDAO brandDAO;

    @BeforeAll
    public void setUpClass() throws Exception {
        MockitoAnnotations.openMocks(this);
   
        DatabaseConnection mockDatabaseConnection = mock(DatabaseConnection.class);
        when(mockDatabaseConnection.getConnection()).thenReturn(connection);

        Field instanceField = DatabaseConnection.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, mockDatabaseConnection);
    }


    @Test
    public void should_ThrowException_when_DatabaseQueryFailsForGetBrands() throws Exception {
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(ProductDatabaseException.class, () -> brandDAO.getBrands());
    }

    @Test
    public void should_ReturnBrand_when_ValidIdIsProvided() throws Exception {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("brand_id")).thenReturn(1);
        when(rs.getString("brand_name")).thenReturn("Brand A");
        when(rs.getString("brand_code")).thenReturn("BA");

        Brand brand = brandDAO.getBrandById(1);

        assertNotNull(brand);
        assertEquals(1, brand.getId());
        assertEquals("Brand A", brand.getName());
        assertEquals("BA", brand.getCode());
    }

    @Test
    public void should_ThrowException_when_CreateBrandFails() throws Exception {
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);

        assertThrows(ProductDatabaseException.class, () -> brandDAO.createBrand("Brand A", "BA"));
    }
}
