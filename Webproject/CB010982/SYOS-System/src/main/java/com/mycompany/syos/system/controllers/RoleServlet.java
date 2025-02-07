/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.syos.system.controllers;

import com.mycompany.syos.system.users.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 *
 * @author User
 */
public class RoleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("loggedInUser");

        // Check if the user is logged in
        if (user == null) {
            // If no user is logged in, redirect to the login page
            response.sendRedirect("login.jsp");
            return;
        }

        // Redirect based on the user's role
        switch (user.getRole().toLowerCase()) {
            case "admin" -> response.sendRedirect("Dashboards/adminDashboard.jsp");
            case "customer" -> response.sendRedirect("Pages/Products"); 
            case "storekeeper" -> response.sendRedirect("Dashboards/storekeeperDashboard.jsp");
            case "cashier" -> response.sendRedirect("Dashboards/cashierDashboard.jsp");
            case "manager" -> response.sendRedirect("Dashboards/managerDashboard.jsp");
            default -> 
                response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "RoleServlet handles role-based redirection";
    }
}

