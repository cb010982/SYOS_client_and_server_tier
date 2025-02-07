/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.state;

import com.mycompany.syos.system.service.CartService;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author User
 */
public class CheckoutState implements CartState {

    @Override
    public void addToCart(CartService cartService, int productId, int quantity) {
        System.out.println("You cannot add items during checkout.");
    }

    @Override
    public void checkout(CartService cartService, String address, String phone) {
        try {  
            cartService.checkoutLogic(address, phone);
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CheckoutState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
