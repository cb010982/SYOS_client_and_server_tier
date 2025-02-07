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
import com.mycompany.syos.system.designpatterns.billing.discount.FifteenPercentDiscountHandler;
//import com.mycompany.syos.system.model.Bill;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.mycompany.database_syos.models.Bill; 

class FifteenPercentDiscountHandlerTest {

    private FifteenPercentDiscountHandler discountHandler;
    private DiscountHandler nextHandler;
    private Bill bill;

    @BeforeEach
    public void setUp() {
        discountHandler = new FifteenPercentDiscountHandler();
        nextHandler = mock(DiscountHandler.class);
        discountHandler.setNextHandler(nextHandler);
        bill = new Bill(1, "B001", LocalDateTime.now(), 12000.0, 0, 0, 0, 0, 0, 0, "cash");
        doNothing().when(nextHandler).applyDiscount(bill);
    }

    @Test
    public void should_ApplyFifteenPercentDiscount_when_GrossTotalExceedsThreshold() {
        bill.setGrossTotal(15000.0);
        discountHandler.applyDiscount(bill);
        assertEquals(2250.0, bill.getDiscount());
        verify(nextHandler, times(1)).applyDiscount(bill);
    }

    @Test
    public void should_NotApplyFifteenPercentDiscount_when_GrossTotalBelowThreshold() {
        bill.setGrossTotal(5000.0);
        discountHandler.applyDiscount(bill);
        assertEquals(0.0, bill.getDiscount());
        verify(nextHandler, times(1)).applyDiscount(bill);
    }

    @Test
    public void should_ApplyDiscountAndCallNextHandler_when_NextHandlerExists() {
        bill.setGrossTotal(12000.0);
        discountHandler.applyDiscount(bill);
        assertEquals(1800.0, bill.getDiscount());
        verify(nextHandler, times(1)).applyDiscount(bill);
    }

    @Test
    public void should_NotCallNextHandler_when_NoNextHandlerExists() {
        bill.setGrossTotal(11000.0);
        discountHandler.setNextHandler(null);
        discountHandler.applyDiscount(bill);
        assertEquals(1650.0, bill.getDiscount());
        verifyNoInteractions(nextHandler);
    }
}
