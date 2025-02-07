/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.users;

//import com.mycompany.syos.system.exceptions.ProductDatabaseException;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.service.CartService;
import com.mycompany.syos.system.service.OnlineInventoryService;
import java.util.Scanner;

////new imports are added after this
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.ProductDatabaseException;

/**
 *
 * @author User
 */

public class OnlineCustomer implements User {
    private UserModel userModel;
    private OnlineInventoryService onlineInventoryService;
    private CartService cartService;

    public OnlineCustomer(UserModel userModel, OnlineInventoryService onlineInventoryService, CartService cartService) {
        this.userModel = userModel;
        this.onlineInventoryService = onlineInventoryService;
        this.cartService = cartService;
    }

    @Override
    public void login() {
        System.out.println(userModel.getUsername() + " (Online Customer) logged in.");
    }

    @Override
    public void signUp() {
        System.out.println("Customer signed up with email: " + userModel.getEmail());
    }

    @Override
    public String getRole() {
        return userModel.getRole();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }
public UserModel getUserModel() {
    return this.userModel;
}


    public void showCustomerMenu(Scanner scanner) throws ProductDatabaseException {
   
        cartService.displayProductsForSelection();  

        boolean purchasing = true;

        while (purchasing) {
            System.out.println("\n--- Place Your Order ---");

           
            addToCart(scanner);

            System.out.println("Do you want to add another product? (yes/no)");
            String continueShopping = scanner.next().trim().toLowerCase();

            if (!continueShopping.equals("yes")) {
                
                System.out.println("Proceeding to checkout...");
                processOrder(scanner);
                purchasing = false; 
            }
        }
    }

    
    private void addToCart(Scanner scanner) throws ProductDatabaseException {
        System.out.println("Enter product ID:");
        int productId = scanner.nextInt();

        System.out.println("Enter quantity:");
        int quantity = scanner.nextInt();

        
        cartService.addToCart(productId, quantity); 
        System.out.println("Product added to cart.");
    }


    private void processOrder(Scanner scanner) {
        
        System.out.println("Enter cardholder name:");
        String cardholderName = scanner.next();

        System.out.println("Enter card number:");
        String cardNumber = scanner.next();

        System.out.println("Enter CVC:");
        String cvc = scanner.next();

        System.out.println("Enter expiry date (MM/YY):");
        String expiryDate = scanner.next();

      
        System.out.println("Enter delivery address:");
        String address = scanner.next();
        
        System.out.println("Enter phone number:");
        String phone = scanner.next();

       
        System.out.println("Processing payment...");
        System.out.println("Payment details:");
        System.out.println("Cardholder Name: " + cardholderName);
        System.out.println("Card Number: " + cardNumber);
        System.out.println("CVC: " + cvc);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("Payment successful!");

      
        cartService.checkout(address, phone);
        System.out.println("Order placed successfully! Thank you for shopping.");
    }}
