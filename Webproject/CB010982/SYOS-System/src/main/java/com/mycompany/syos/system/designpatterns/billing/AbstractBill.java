/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.billing;

/**
 *
 * @author User
 */
public abstract class AbstractBill {

   
    public final void generateBill() {
        printHeader();
        printItems();
        printFooter();
    }

    protected abstract void printHeader();
    protected abstract void printItems();
    protected abstract void printFooter();

}
