/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.syos.system.controllers;


import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.databaseconnection.DatabaseConnection;
import com.mycompany.database_syos.models.UserModel;
import java.time.LocalDateTime;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class SignUpServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        userDAO = new UserDAO(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward GET requests to the SignUp.jsp
        request.getRequestDispatcher("/Authentication/SignUp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (username == null || email == null || password == null ||
            username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/Authentication/SignUp.jsp").forward(request, response);
            return;
        }

        try {
            UserModel existingUser = userDAO.getCurrentUser(username);
            if (existingUser != null) {
                request.setAttribute("error", "Username is already taken.");
                request.getRequestDispatcher("/Authentication/SignUp.jsp").forward(request, response);
                return;
            }

            UserModel newUser = new UserModel(
                0,
                username,
                hashPassword(password),
                "customer",
                email,
                LocalDateTime.now()
            );

            userDAO.createUser(newUser);
            response.sendRedirect("/Authentication/Login");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during registration.");
            request.getRequestDispatcher("/Authentication/SignUp.jsp").forward(request, response);
        }
    }

    private String hashPassword(String password) {
        return password; 
    }
}
