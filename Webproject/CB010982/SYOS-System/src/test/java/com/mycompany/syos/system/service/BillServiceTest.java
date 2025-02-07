/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.syos.system.service;
/**
 *
 * @author User
 */
//import com.mycompany.syos.database.dao.BillDAO;
//import com.mycompany.syos.database.dao.ProductDAO;
//import com.mycompany.syos.database.dao.ShelfDAO;
//import com.mycompany.syos.database.dao.ShelfItemDAO;
//import com.mycompany.syos.database.dao.UserDAO;
//import com.mycompany.syos.system.model.Bill;
//import com.mycompany.syos.system.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.dao.BillDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.dao.ShelfItemDAO;
import com.mycompany.database_syos.models.Bill;


public class BillServiceTest {

    private BillService billService;
    private BillDAO billDAOMock;
    private ProductDAO productDAOMock;
    private ShelfItemDAO shelfItemDAOMock;
    private ShelfDAO shelfDAOMock;
    private UserDAO userDAOMock;
    private UserModel currentUserMock;
    private Scanner scannerMock;

    @BeforeEach
    public void setUp() {
        billDAOMock = mock(BillDAO.class);
        productDAOMock = mock(ProductDAO.class);
        shelfItemDAOMock = mock(ShelfItemDAO.class);
        shelfDAOMock = mock(ShelfDAO.class);
        userDAOMock = mock(UserDAO.class);
        currentUserMock = mock(UserModel.class);
        scannerMock = mock(Scanner.class);

        billService = new BillService(
                billDAOMock, productDAOMock, shelfItemDAOMock, shelfDAOMock, userDAOMock, currentUserMock
        );
    }

    @Test
    public void should_ApplyDiscounts_When_ThresholdsAreMet() {
        Bill testBill = new Bill(
            1, "BILL_101", LocalDateTime.now(), 12000.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, "Cash"
        );
        billService.applyDiscountsToBill(testBill);
        assertEquals(12000.0 * 0.15, testBill.getDiscount(), 0.01);
    }

    @Test
    public void should_DisplayAllBills_When_ViewTransactionsIsCalled() {
        List<Bill> bills = new ArrayList<>();
        Bill bill1 = new Bill(1, "BILL_101", LocalDateTime.now(), 1000.0, 900.0, 100.0, 0.0, 900.0, 1000.0, 100.0, "Cash");
        Bill bill2 = new Bill(2, "BILL_102", LocalDateTime.now(), 2000.0, 1800.0, 200.0, 0.0, 1800.0, 2000.0, 200.0, "Card");
        bills.add(bill1);
        bills.add(bill2);
        when(billDAOMock.getAllBills()).thenReturn(bills);
        billService.viewTransactions(scannerMock);
        verify(billDAOMock, times(1)).getAllBills();
        assertEquals(2, bills.size());
    }

}
