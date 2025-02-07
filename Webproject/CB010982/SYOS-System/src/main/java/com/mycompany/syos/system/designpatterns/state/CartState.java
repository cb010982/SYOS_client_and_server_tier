/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.state;

import com.mycompany.syos.system.service.CartService;
/**
 *
 * @author User
 */
public interface CartState {
    void addToCart(CartService cartService, int productId, int quantity);
    void checkout(CartService cartService, String address, String phone);    
}
