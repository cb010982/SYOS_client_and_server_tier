/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.syos.system.controllers;
/**
 *
 * @author User
 */
import com.mycompany.database_syos.models.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Store shipping details in session
        session.setAttribute("firstName", request.getParameter("firstName"));
        session.setAttribute("lastName", request.getParameter("lastName"));
        session.setAttribute("address", request.getParameter("address"));
        session.setAttribute("city", request.getParameter("city"));
        session.setAttribute("postalCode", request.getParameter("postalCode"));
        session.setAttribute("telephone", request.getParameter("telephone"));

        // Store payment details in session
        session.setAttribute("cardName", request.getParameter("cardName"));
        session.setAttribute("cardNumber", request.getParameter("cardNumber"));
        session.setAttribute("expDate", request.getParameter("expDate"));
        session.setAttribute("cvv", request.getParameter("cvv"));
        session.setAttribute("paymentMethod", request.getParameter("paymentMethod"));

        // Retrieve cart items from form submission
        String[] productIds = request.getParameterValues("productId");
        List<CartItem> cartItems = new ArrayList<>();
        double grossTotal = 0.0;

        if (productIds != null) {
            for (String productIdStr : productIds) {
                int productId = Integer.parseInt(productIdStr);
                int quantity = Integer.parseInt(request.getParameter("quantity-" + productId));
                double price = Double.parseDouble(request.getParameter("price-" + productId));
                String productName = request.getParameter("productName-" + productId);      
                CartItem item = new CartItem(0, 0, productId, 0, "N/A", quantity, LocalDateTime.now(), price);                
                item.setProductName(productName);
                cartItems.add(item);
                grossTotal += (quantity * price);
            }
        }

        // Store cart items in session
        session.setAttribute("cartItems", cartItems);
        session.setAttribute("grossTotal", grossTotal);

        // Redirect to Cart Summary Page
        response.sendRedirect(request.getContextPath() + "/Pages/CartSummary.jsp");
    }
}
