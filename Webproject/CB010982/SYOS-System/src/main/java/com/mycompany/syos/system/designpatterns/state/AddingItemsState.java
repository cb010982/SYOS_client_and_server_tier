/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.state;

//import com.mycompany.syos.system.exceptions.ProductDatabaseException;
import com.mycompany.syos.system.service.CartService;

//new imports are added after this
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author User
 */
public class AddingItemsState implements CartState {


@Override
public void addToCart(CartService cartService, int productId, int quantity) {
    try {
        // Validate product ID
        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID: " + productId);
        }

        // Add items to cart
        if (quantity > 0) {
            cartService.addItemsToCartLogic(productId, quantity);
        }
        // Remove items from cart
        else if (quantity < 0) {
            cartService.removeItemsFromCartLogic(productId, Math.abs(quantity));
        } else {
            System.out.println("No change in quantity for product ID " + productId);
        }
    } catch (ProductDatabaseException e) {
        System.err.println("Database error while updating cart: " + e.getMessage());
        e.printStackTrace();
    } catch (SQLException e) {
        System.err.println("SQL error while updating cart: " + e.getMessage());
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        System.err.println("Validation error: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Unexpected error: " + e.getMessage());
        e.printStackTrace();
    }
}



    @Override
    public void checkout(CartService cartService, String address, String phone) {
        System.out.println("Transitioning to CheckoutState...");
        cartService.setState(new CheckoutState());  
        cartService.checkout(address, phone);  
    }

}
