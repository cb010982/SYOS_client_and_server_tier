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
import com.mycompany.syos.system.designpatterns.billing.discount.TenPercentDiscountHandler;
//import com.mycompany.syos.system.model.Bill;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mycompany.database_syos.models.Bill; 

public class TenPercentDiscountHandlerTest {
    
    private TenPercentDiscountHandler discountHandler;
    private DiscountHandler nextHandler;  
    private Bill bill;

    @BeforeEach
    public void setUp() {
        discountHandler = new TenPercentDiscountHandler();
        nextHandler = mock(DiscountHandler.class);
        discountHandler.setNextHandler(nextHandler);
        bill = new Bill(1, "B002", LocalDateTime.now(), 7000.0, 0, 0, 0, 0, 0, 0, "cash");

        doNothing().when(nextHandler).applyDiscount(bill);
    }

    @Test
    public void should_ApplyTenPercentDiscount_when_GrossTotalIsBetween5000And10000() {
        discountHandler.applyDiscount(bill);
        double expectedDiscount = 7000.0 * 0.10;  
        assertEquals(expectedDiscount, bill.getDiscount(), 0.01);
        verify(nextHandler, times(1)).applyDiscount(bill);  
    }

    @Test
    public void should_NotApplyDiscount_when_GrossTotalIsLessThan5000() {
        bill.setGrossTotal(4000.0);

        discountHandler.applyDiscount(bill);

        assertEquals(0.0, bill.getDiscount(), 0.01);
        verify(nextHandler, times(1)).applyDiscount(bill);  
    }

    @Test
    public void should_PassToNextHandler_when_GrossTotalIsAbove10000() {
        bill.setGrossTotal(11000.0);

        discountHandler.applyDiscount(bill);

        assertEquals(0.0, bill.getDiscount(), 0.01); 
        verify(nextHandler, times(1)).applyDiscount(bill);  
    }
}
