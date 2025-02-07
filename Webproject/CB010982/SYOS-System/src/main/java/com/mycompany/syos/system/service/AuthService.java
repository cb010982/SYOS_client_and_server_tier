package com.mycompany.syos.system.service;

import com.mycompany.syos.system.designpatterns.factory.users.CashierFactory;
import com.mycompany.syos.system.designpatterns.factory.users.AssistantStoreKeeperFactory;
import com.mycompany.syos.system.designpatterns.factory.users.StorekeeperFactory;
import com.mycompany.syos.system.designpatterns.factory.users.SystemAdminFactory;
import com.mycompany.syos.system.designpatterns.factory.users.OnlineCustomerFactory;
import com.mycompany.syos.system.designpatterns.factory.users.ManagerFactory;
import com.mycompany.syos.system.designpatterns.factory.users.SupervisorFactory;
import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
import com.mycompany.syos.system.users.User;
//import com.mycompany.syos.system.exceptions.InvalidUserException;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
import com.mycompany.syos.system.designpatterns.factory.interfaces.UserFactory;
import com.mycompany.syos.system.users.SystemAdmin;
import java.sql.Connection;
import java.util.Scanner;


import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.ShelfDAO;
import com.mycompany.database_syos.dao.ShelfItemDAO;
import com.mycompany.database_syos.dao.ShelfRestockLogDAO;
import com.mycompany.database_syos.dao.StoreInventoryDAO;
import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidUserException;
import com.mycompany.database_syos.exceptions.InvalidRoleException;
import com.mycompany.database_syos.dao.BillDAO;
import com.mycompany.database_syos.dao.CartDAO;
import java.util.concurrent.Semaphore;



public class AuthService {

    private static final Semaphore loginSemaphore = new Semaphore(10);
    private final UserDAO userDAO;
    private final UserService userService;
    private final ProductManagerCLI productManager;
    private final Connection connection;  
  
    public AuthService(UserDAO userDAO, UserService userService, ProductManagerCLI productManager, Connection connection) {
        this.userDAO = userDAO;
        this.userService = userService;
        this.productManager = productManager;
        this.connection = connection;
    }

       // New login for web
    public User login(String username, String password) {
        try {
            loginSemaphore.acquire(); // Acquire a permit to proceed

            if (username == null || username.trim().isEmpty()) {
                throw new InvalidUserException("Username cannot be empty.");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new InvalidUserException("Password cannot be empty.");
            }

            String passwordHash = password; 
            UserModel userModel = userDAO.getUserByUsernameAndPassword(username.trim(), passwordHash);

            if (userModel == null) {
                throw new InvalidUserException("Invalid username or password.");
            }

            // No Scanner required for web
            return createUserBasedOnRole(userModel, null);

        } catch (InvalidUserException | InvalidRoleException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Login operation interrupted.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during login.");
            e.printStackTrace();
        } finally {
            loginSemaphore.release(); // Release the permit
        }
        return null;
    }

    public User login(Scanner scanner) {
       try {
           System.out.println("Enter your username:");
           String username = scanner.nextLine().trim();
           if (username.isEmpty()) {
               throw new InvalidUserException("Username cannot be empty.");
           }

           System.out.println("Enter your password:");
           String password = scanner.nextLine().trim();
           if (password.isEmpty()) {
               throw new InvalidUserException("Password cannot be empty.");
           }

           String passwordHash = password; 
           UserModel userModel = userDAO.getUserByUsernameAndPassword(username, passwordHash);

           if (userModel == null) {
               throw new InvalidUserException("Invalid username or password.");
           }

           return createUserBasedOnRole(userModel, scanner); // Pass Scanner for CLI
       } catch (InvalidUserException | InvalidRoleException e) {
           System.out.println(e.getMessage());
       } catch (Exception e) {
           System.out.println("An unexpected error occurred during login.");
           e.printStackTrace();
       }
       return null;
   }


    private User createUserBasedOnRole(UserModel userModel, Scanner scanner) throws InvalidRoleException {
        UserFactory userFactory = null;

        if (connection == null) {
            throw new InvalidRoleException("Database connection is not initialized.");
        }
        OnlineInventoryDAO onlineInventoryDAO = new OnlineInventoryDAO();
        OnlineInventoryService onlineInventoryService = new OnlineInventoryService(onlineInventoryDAO);
        OnlineInventoryItemDAO onlineInventoryItemDAO = new OnlineInventoryItemDAO();
    
        UserModel currentUser = userModel;

        switch (userModel.getRole().toLowerCase()) {
       
        case "manager":
            ReportService reportService = new ReportService();  
            userFactory = new ManagerFactory(reportService);
            break;

        case "assistant_store_keeper":
               
                StoreInventoryDAO storeInventoryDAO = new StoreInventoryDAO();
                ProductDAO productDAO = new ProductDAO();
                ShelfDAO shelfDAO = new ShelfDAO();
                ShelfItemDAO shelfItemDAO = new ShelfItemDAO();
                ShelfRestockLogDAO shelfRestockLogDAO = new ShelfRestockLogDAO();

              
                StoreInventoryService storeInventoryService = new StoreInventoryService(storeInventoryDAO, productDAO, connection);
                ShelfService shelfService = new ShelfService(shelfDAO, shelfItemDAO);  

               
                ShelfRestockService shelfRestockService = new ShelfRestockService(storeInventoryService, shelfService, shelfDAO, shelfRestockLogDAO, productDAO);
              
                
                OnlineInventoryRestockService onlineInventoryRestockService = new OnlineInventoryRestockService(
                    storeInventoryService, onlineInventoryService, onlineInventoryDAO, productDAO, onlineInventoryItemDAO
                );
              
                   InventoryNotificationService notificationService = new InventoryNotificationService(onlineInventoryDAO);
                   notificationService.checkAndNotifyRestock();
            
                userFactory = new AssistantStoreKeeperFactory(storeInventoryService, shelfRestockService, shelfService, onlineInventoryRestockService, onlineInventoryService);
                break;

            case "cashier":
             
                BillDAO billDAO = new BillDAO();
                ProductDAO productDAOForCashier = new ProductDAO();
                ShelfItemDAO shelfItemDAOForCashier = new ShelfItemDAO();
                ShelfDAO shelfDAOForCashier = new ShelfDAO();

               
                BillService billService = new BillService(
                billDAO, 
                productDAOForCashier, 
                shelfItemDAOForCashier, 
                shelfDAOForCashier, 
                userDAO,  
                userModel
            );

                userFactory = new CashierFactory(billService);
                break;

            case "storekeeper":
                userFactory = new StorekeeperFactory();
                break;

            case "supervisor":
                userFactory = new SupervisorFactory();
                break;

        case "customer":

            ProductDAO productDAOForCustomer = new ProductDAO();  
           CartDAO cartDAO = new CartDAO(); 


            CartService cartService = new CartService(
                productDAOForCustomer, 
                cartDAO, 
                onlineInventoryItemDAO, 
                onlineInventoryDAO, 
                currentUser
            );

            userFactory = new OnlineCustomerFactory(onlineInventoryService, cartService);
            break;

            case "admin":

             userFactory = new SystemAdminFactory(userService, productManager, onlineInventoryService);
             break;

            default:
                throw new InvalidRoleException("Unsupported role: " + userModel.getRole());
        }

        User user = userFactory.createUser(userModel);

        if ("admin".equalsIgnoreCase(userModel.getRole())) {
            SystemAdmin admin = (SystemAdmin) user;
            userService.setLoggedInUser(admin);  
            admin.showAdminMenu(scanner); 
        }

        return user;
    }
}

