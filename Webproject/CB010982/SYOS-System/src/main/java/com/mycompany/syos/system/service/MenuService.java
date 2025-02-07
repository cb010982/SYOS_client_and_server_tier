package com.mycompany.syos.system.service;

import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
import com.mycompany.syos.system.users.User;



import java.util.Scanner;

public class MenuService {

    private final UserService userService;
    private final ProductManagerCLI productManager;

   
    public MenuService(UserService userService, ProductManagerCLI productManager) {
        this.userService = userService;
        this.productManager = productManager;
    }

    private int getValidIntInput(Scanner scanner) {
        int input = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                input = scanner.nextInt(); 
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();  
            }
        }

        return input;
    }

}

