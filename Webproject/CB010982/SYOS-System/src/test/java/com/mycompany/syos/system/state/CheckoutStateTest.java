/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.state;

/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.state.CheckoutState;
//import com.mycompany.syos.system.exceptions.ProductDatabaseException;
import com.mycompany.syos.system.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

//new imports are added after this
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import java.sql.SQLException;
//
//class CheckoutStateTest {
//
//    private CartService cartService;
//    private CheckoutState checkoutState;
//
//    @BeforeEach
//    public void setUp() {
//        cartService = mock(CartService.class);  
//        checkoutState = new CheckoutState();  
//    }
//
//    @Test
//    public void should_NotAddItemsToCart_when_AddToCartIsCalledDuringCheckout() throws ProductDatabaseException, SQLException {    
//        int productId = 1;
//        int quantity = 2;
//     
//        checkoutState.addToCart(cartService, productId, quantity);
//
//        verify(cartService, never()).addItemsToCartLogic(anyInt(), anyInt()); 
//    }
//
//    @Test
//    public void should_CallCheckoutLogic_when_CheckoutIsCalled() throws SQLException {
//        String address = "123 Main St";
//        String phone = "555-555-5555";
//
//        checkoutState.checkout(cartService, address, phone);
//
//        verify(cartService, times(1)).checkoutLogic(address, phone);  
//    }
//}
