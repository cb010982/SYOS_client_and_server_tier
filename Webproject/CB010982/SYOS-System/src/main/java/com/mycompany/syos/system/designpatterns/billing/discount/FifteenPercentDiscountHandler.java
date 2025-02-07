/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.billing.discount;

//import com.mycompany.syos.system.model.Bill;

//new imports are added after this
import com.mycompany.database_syos.models.Bill;

/**
 *
 * @author User
 */

public class FifteenPercentDiscountHandler extends DiscountHandler {
    private final double threshold = 10000.0;
    private final double discountRate = 0.15; 

    @Override
    public void applyDiscount(Bill bill) {
        if (bill.getGrossTotal() > threshold) {
            double discount = bill.getGrossTotal() * discountRate;
            bill.setDiscount(bill.getDiscount() + discount);
            System.out.printf("Applied 15%% discount: %.2f%n", discount);
        }
        if (nextHandler != null) {
            nextHandler.applyDiscount(bill);
        }
    }
}

