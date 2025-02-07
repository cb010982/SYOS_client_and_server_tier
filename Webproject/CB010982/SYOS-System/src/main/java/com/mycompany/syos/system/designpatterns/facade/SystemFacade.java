/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.designpatterns.facade;

import com.mycompany.syos.system.users.AssistantStoreKeeper;
import com.mycompany.syos.system.users.Cashier;
import com.mycompany.syos.system.users.Manager;
import com.mycompany.syos.system.users.OnlineCustomer;
import com.mycompany.syos.system.users.SystemAdmin;
import com.mycompany.syos.system.users.User;
import com.mycompany.syos.system.service.AuthService;
import com.mycompany.syos.system.service.InventoryNotificationService;
import com.mycompany.syos.system.service.MenuService;
import com.mycompany.syos.system.service.UserService;
import com.mycompany.syos.system.service.*;
import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
import com.mycompany.syos.system.designpatterns.factory.users.AssistantStoreKeeperFactory;
import java.sql.Connection;
import java.util.Scanner;

//new imports are added after this
import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.ShelfRestockLogDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.dao.StoreInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.dao.ShelfItemDAO;
import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.exceptions.ProductDatabaseException;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;

/**
 *
 * @author User
 */

public class SystemFacade {
    private AuthService authService;
    private MenuService menuService;
    private UserService userService;
    private OnlineInventoryDAO onlineInventoryDAO;

    // Constructor initializes all components
    public SystemFacade() throws Exception {
        // Initialize database connection
        Connection connection = DatabaseConnection.getInstance().getConnection();

        // Initialize DAOs
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        StoreInventoryDAO storeInventoryDAO = new StoreInventoryDAO();
        ShelfDAO shelfDAO = new ShelfDAO();
        ShelfItemDAO shelfItemDAO = new ShelfItemDAO();
        ShelfRestockLogDAO shelfRestockLogDAO = new ShelfRestockLogDAO();
        OnlineInventoryDAO onlineInventoryDAO = new OnlineInventoryDAO();
        OnlineInventoryItemDAO onlineInventoryItemDAO = new OnlineInventoryItemDAO();

        // Initialize Services
        UserService userService = new UserService(userDAO);
        ProductManagerCLI productManager = new ProductManagerCLI();
        AuthService authService = new AuthService(userDAO, userService, productManager, connection);
        MenuService menuService = new MenuService(userService, productManager);
        StoreInventoryService storeInventoryService = new StoreInventoryService(storeInventoryDAO, productDAO, connection);
        ShelfService shelfService = new ShelfService(shelfDAO, shelfItemDAO);
        ShelfRestockService shelfRestockService = new ShelfRestockService(
            storeInventoryService, shelfService, shelfDAO, shelfRestockLogDAO, productDAO
        );
        OnlineInventoryService onlineInventoryService = new OnlineInventoryService(onlineInventoryDAO);
        OnlineInventoryRestockService onlineInventoryRestockService = new OnlineInventoryRestockService(
            storeInventoryService, onlineInventoryService, onlineInventoryDAO, productDAO, onlineInventoryItemDAO
        );

        AssistantStoreKeeperFactory assistantStoreKeeperFactory = new AssistantStoreKeeperFactory(
            storeInventoryService, shelfRestockService, shelfService, onlineInventoryRestockService, onlineInventoryService
        );

        // Assign components to the facade
        this.authService = authService;
        this.menuService = menuService;
        this.userService = userService;
        this.onlineInventoryDAO = onlineInventoryDAO;
    }

    // Start the system
    public void startSystem(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null in startSystem.");
        }

        System.out.println("Do you have an account? (yes/no)");
        String hasAccount = scanner.nextLine().trim().toLowerCase();

        if (hasAccount.equals("yes")) {
            User user = authService.login(scanner); // Use CLI login

            if (user != null) {
                handleUserRole(user, menuService, scanner); // Pass Scanner for role handling
            } else {
                System.out.println("Invalid credentials or unsupported role.");
            }
        } else {
            System.out.println("You need to sign up as a customer to create an account.");
            userService.signUpCustomer(scanner); // Use Scanner for sign-up
        }
    }


    private void handleUserRole(User user, MenuService menuService, Scanner scanner) {
        switch (user.getRole().toLowerCase()) {
            case "admin":
                if (user instanceof SystemAdmin) {
                    ((SystemAdmin) user).showAdminMenu(scanner);
                }
                break;
            case "assistant_store_keeper":
                if (user instanceof AssistantStoreKeeper) {
                    AssistantStoreKeeper storeKeeper = (AssistantStoreKeeper) user;
                    storeKeeper.showStoreKeeperMenu(scanner);

                    InventoryNotificationService notificationService = new InventoryNotificationService(onlineInventoryDAO);
                    notificationService.checkAndNotifyRestock();
                }
                break;
            case "manager":
                if (user instanceof Manager) {
                    ((Manager) user).showManagerMenu(scanner);
                }
                break;
            case "cashier":
                if (user instanceof Cashier) {
                    ((Cashier) user).showCashierMenu(scanner);
                }
                break;
            case "customer":
                if (user instanceof OnlineCustomer) {
                    try {
                        ((OnlineCustomer) user).showCustomerMenu(scanner);
                    } catch (ProductDatabaseException e) {
                        System.out.println("Error loading products: " + e.getMessage());
                    }
                }
                break;
            default:
                System.out.println("Unsupported role: " + user.getRole());
                break;
        }
    }
}
