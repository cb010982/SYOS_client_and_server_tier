/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.syos.system.controllers;

import com.mycompany.database_syos.dao.CartDAO;
import com.mycompany.database_syos.dao.OnlineInventoryDAO;
import com.mycompany.database_syos.dao.OnlineInventoryItemDAO;
import com.mycompany.database_syos.dao.ProductDAO;
import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.syos.system.designpatterns.cliview.ProductManagerCLI;
import com.mycompany.syos.system.service.AuthService;
import com.mycompany.syos.system.service.CartService;
import com.mycompany.syos.system.service.OnlineInventoryService;
import com.mycompany.syos.system.service.UserService;
import com.mycompany.syos.system.users.OnlineCustomer;
import com.mycompany.syos.system.users.User;
import com.mycompany.syos.system.users.UserHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class LoginServlet extends HttpServlet {

    private AuthService authService;
    private OnlineInventoryService onlineInventoryService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        try {
            System.out.println("Starting LoginServlet initialization...");
            Connection connection = DatabaseConnection.getInstance().getConnection();

            if (connection == null) {
                throw new ServletException("Database connection is null.");
            }
            System.out.println("Database connection successful.");

            UserDAO userDAO = new UserDAO();
            UserService userService = new UserService(userDAO);
            ProductDAO productDAO = new ProductDAO();
            CartDAO cartDAO = new CartDAO(); 

            OnlineInventoryDAO inventoryDAO = new OnlineInventoryDAO();
            OnlineInventoryItemDAO inventoryItemDAO = new OnlineInventoryItemDAO();
            ProductManagerCLI productManager = new ProductManagerCLI();

            onlineInventoryService = new OnlineInventoryService(inventoryDAO);
            cartService = new CartService(productDAO, cartDAO, inventoryItemDAO, inventoryDAO, null); // currentUser will be set during login
            authService = new AuthService(userDAO, userService, productManager, connection);

            System.out.println("LoginServlet initialization completed.");
        } catch (Exception e) {
            System.err.println("Error during LoginServlet initialization.");
            e.printStackTrace();
            throw new ServletException("Error initializing LoginServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Authenticate the user
            User user = authService.login(username, password);

            System.out.println("User returned by authService.login(): " + (user != null ? user.getClass().getName() : "null"));

            if (user != null) {
                // Store the authenticated user in the session
                HttpSession session = request.getSession(true);
                session.setAttribute("currentUser", user);

                if (user instanceof OnlineCustomer) {
                    UserModel userModel = ((OnlineCustomer) user).getUserModel();
                    cartService.setCurrentUser(userModel);
                    cartService.startNewCart(); // Create a new cart for the session
                } else if (user instanceof UserModel) {
                    cartService.setCurrentUser((UserModel) user);
                    cartService.startNewCart(); 
                }

                // Redirect to the cart page 
                response.sendRedirect(request.getContextPath() + "/Pages/cart?action=getProducts");
            } else {
                // Handle invalid credentials
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("/Authentication/Login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
   }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward GET requests to the login page
        request.getRequestDispatcher("/Authentication/Login.jsp").forward(request, response);
    }
}

