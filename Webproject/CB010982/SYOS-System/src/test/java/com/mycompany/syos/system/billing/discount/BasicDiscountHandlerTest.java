/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.billing.discount;
/**
 *
 * @author User
 */
import com.mycompany.syos.system.designpatterns.billing.discount.DiscountHandler;
import com.mycompany.syos.system.designpatterns.billing.discount.BasicDiscountHandler;
//import com.mycompany.syos.system.model.Bill;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mycompany.database_syos.models.Bill; 

public class BasicDiscountHandlerTest {

    private BasicDiscountHandler basicDiscountHandler;
    private DiscountHandler nextHandler; 
    private Bill bill;

    @BeforeEach
    public void setUp() {

        basicDiscountHandler = new BasicDiscountHandler();
        nextHandler = mock(DiscountHandler.class);
        basicDiscountHandler.setNextHandler(nextHandler);
        bill = new Bill(1, "B001", LocalDateTime.now(), 5000.0, 0, 0, 0, 0, 0, 0, "cash");
        doNothing().when(nextHandler).applyDiscount(bill);
    }

    @Test
    public void should_NotApplyAnyDiscount() {
        basicDiscountHandler.applyDiscount(bill);
        assertEquals(0.0, bill.getDiscount(), 0.01);
    }

    @Test
    public void should_PassToNextHandlerInChain() {
        basicDiscountHandler.applyDiscount(bill);
        verify(nextHandler, times(1)).applyDiscount(bill);
    }

    @Test
    public void should_NotCallNextHandler_When_NextHandlerIsNull() {
        basicDiscountHandler.setNextHandler(null);
        basicDiscountHandler.applyDiscount(bill);
        assertEquals(0.0, bill.getDiscount(), 0.01);
    }
}

