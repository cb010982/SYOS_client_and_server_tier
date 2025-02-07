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


public abstract class DiscountHandler {
    protected DiscountHandler nextHandler;

    public void setNextHandler(DiscountHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void applyDiscount(Bill bill);
}

