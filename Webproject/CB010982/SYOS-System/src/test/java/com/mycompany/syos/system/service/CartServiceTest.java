/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.service;

/**
 *
 * @author User
 */
//import com.mycompany.syos.database.dao.CartDAO;
//import com.mycompany.syos.database.dao.OnlineInventoryDAO;
//import com.mycompany.syos.database.dao.OnlineInventoryItemDAO;
//import com.mycompany.syos.database.dao.ProductDAO;
//import com.mycompany.syos.system.model.Product;
//import com.mycompany.syos.system.model.UserModel;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.dao.CartDAO;
import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.models.Product;
import com.mycompany.database_syos.dao.ProductDAO;

public class CartServiceTest {

    @Mock
    private ProductDAO productDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private OnlineInventoryItemDAO onlineInventoryItemDAO;

    @Mock
    private OnlineInventoryDAO onlineInventoryDAO;

    @Mock
    private UserModel currentUser;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  

        currentUser = new UserModel(1, "customer", "hashedPassword123", "customer", "customer@example.com", LocalDateTime.now());
    }

    @Test
    public void should_ProcessPaymentSuccessfully_when_PaymentDetailsAreValid(){
        Scanner scanner = mock(Scanner.class);
        when(scanner.next()).thenReturn("John Doe").thenReturn("1234567890123456").thenReturn("123").thenReturn("12/25");

        cartService.processPaymentLogic(scanner);

        verify(scanner, times(4)).next();
    }

    @Test
    public void should_DisplayAllProducts_when_ProductsAreAvailable() throws Exception {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(101);
        product1.setName("Product A");
        product1.setFinalPrice(100.0);

        Product product2 = new Product();
        product2.setId(102);
        product2.setName("Product B");
        product2.setFinalPrice(200.0);

        products.add(product1);
        products.add(product2);

        when(productDAO.getAllProducts()).thenReturn(products);

        cartService.displayProductsForSelection();

        verify(productDAO, times(1)).getAllProducts();
    }


}
