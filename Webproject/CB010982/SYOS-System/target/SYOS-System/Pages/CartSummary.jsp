<%-- 
    Document   : CartSummary
    Created on : Feb 2, 2025, 5:32:51 PM
    Author     : User
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
   <link rel="stylesheet" href="${pageContext.request.contextPath}/css/CartSummary.css">

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart Summary</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
   <header style="background-color: #121212; padding: 10px 20px; font-family: Arial, sans-serif; color: #fff; font-size: 16px;">
    <div style="max-width: 1200px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center;">
        <!-- Logo -->
        <div style="font-size: 22px; font-weight: bold;">
            <a href="#" style="color: #fff; text-decoration: none;">SYOS</a>
        </div>

        <!-- Navigation Links -->
        <nav>
            <ul style="list-style: none; display: flex; margin: 0; padding: 0;">
                <li style="margin: 0 15px;">
                    <a href="#" style="color: #fff; text-decoration: none; padding: 5px 10px; transition: color 0.3s ease;">Home</a>
                </li>
                <li style="margin: 0 15px;">
                    <a href="#" style="color: #fff; text-decoration: none; padding: 5px 10px; transition: color 0.3s ease;">About</a>
                </li>
                <li style="margin: 0 15px;">
                    <a href="#" style="color: #fff; text-decoration: none; padding: 5px 10px; transition: color 0.3s ease;">Services</a>
                </li>
                <li style="margin: 0 15px;">
                    <a href="#" style="color: #fff; text-decoration: none; padding: 5px 10px; transition: color 0.3s ease;">Contact</a>
                </li>
            </ul>
        </nav>

        <div>
            <a href="#" style="color: #fff; text-decoration: none; padding: 8px 15px; border: 1px solid #fff; border-radius: 5px; transition: background-color 0.3s ease, color 0.3s ease;">
                Get Started
            </a>
        </div>
    </div>
</header>
<div class="firstcontainer">
<div class="maincontainer">
    <!-- Invoice Header -->
    <div class="invoice-header">
        <h2>RECEIPT</h2>
        <div class="company-info"><br>
            <span><strong>SYOS STORE</strong></span><br>
            <span>REG: 824178210293</span><br>
            <span>support@syos.com | +94 11 234 5678</span><br>
        </div>
    </div>
        <!-- Order Summary -->
    <div class="cart-section">
        <table class="table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Qty</th>
                    <th>Price</th>
                    <th>Amount</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${sessionScope.cartItems}">
                    <tr>
                        <td>${item.productName}</td>
                        <td>${item.quantity}</td>
                        <td>$<fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2" /></td>
                        <td>$<fmt:formatNumber value="${item.quantity * item.price}" type="number" minFractionDigits="2" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="total-box">
            <span>Total:</span> $<fmt:formatNumber value="${sessionScope.grossTotal}" type="number" minFractionDigits="2" />
        </div>
    </div>
        <div class="col-md-4">
            <div class="details-box">
                <h6>Delivery Details</h6>
                <span><strong>Name:</strong> ${sessionScope.firstName} ${sessionScope.lastName}</span><br>
                <span><strong>Address:</strong> ${sessionScope.address}, ${sessionScope.city}</span><br>
                <span><strong>Postal Code:</strong> ${sessionScope.postalCode}</span><br>
                <span><strong>Telephone:</strong> ${sessionScope.telephone}</span>                            
            </div>
        </div>
  
        <div class="footer">
            <div class="shop-info">
                <h6>SYOS Store - Colombo</h6><br>
                <span><strong>Location:</strong> 123 Main Street, Colombo, Sri Lanka</span><br>
                <span><strong>Contact:</strong> +94 11 234 5678</span><br>
                <span><strong>Email:</strong> support@syos.com</span><br>
                <span>Visit our website: <a href="https://www.syos.com">www.syos.com</a></span>
            </div>
        <p>For any questions, please contact us at support@syos.com</p>
        </div>
    </div>
</div>
    <!-- Footer -->
<footer style="background-color: #121212; color: #fff; padding: 40px 20px; font-family: Arial, sans-serif; font-size: 14px;">
    <div style="max-width: 1200px; margin: 0 auto; display: flex; flex-wrap: wrap; justify-content: space-between; align-items: flex-start;">
        <!-- Logo Section -->
        <div style="flex: 1; min-width: 200px; margin-bottom: 20px;">
            <h2 style="margin: 0; font-size: 20px;">SYOS</h2>
            <p style="margin-top: 5px; font-size: 12px; color: #999;">GROCERY STORE</p>
        </div>

        <!-- Links Section -->
        <div style="flex: 3; display: flex; justify-content: space-between; flex-wrap: wrap; min-width: 600px;">
            <div style="flex: 1; min-width: 150px; margin-bottom: 20px;">
                <ul style="list-style: none; padding: 0;">
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">FAQs</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Online Support</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Submit a Complaint</a></li>
                </ul>
            </div>
            <div style="flex: 1; min-width: 150px; margin-bottom: 20px;">
                <ul style="list-style: none; padding: 0;">
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Services</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Products</a></li>
                </ul>
            </div>
            <div style="flex: 1; min-width: 150px; margin-bottom: 20px;">
                <ul style="list-style: none; padding: 0;">
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Showcase</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Delivery</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Support</a></li>
                </ul>
            </div>
            <div style="flex: 1; min-width: 150px; margin-bottom: 20px;">
                <ul style="list-style: none; padding: 0;">
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">About Us</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Contact Us</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Affiliates</a></li>
                    <li><a href="#" style="color: #fff; text-decoration: none; font-size: 14px;">Resources</a></li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Social Media Section -->
    <div style="text-align: center; margin-top: 30px;">
        <a href="#" style="margin: 0 10px; color: #fff; font-size: 16px;"><i class="fab fa-facebook-f"></i></a>
        <a href="#" style="margin: 0 10px; color: #fff; font-size: 16px;"><i class="fab fa-twitter"></i></a>
        <a href="#" style="margin: 0 10px; color: #fff; font-size: 16px;"><i class="fab fa-instagram"></i></a>
        <a href="#" style="margin: 0 10px; color: #fff; font-size: 16px;"><i class="fab fa-google"></i></a>
        <a href="#" style="margin: 0 10px; color: #fff; font-size: 16px;"><i class="fab fa-linkedin-in"></i></a>
    </div>

    <!-- Copyright Section -->
    <div style="text-align: center; margin-top: 20px; font-size: 12px; color: #999;">
        ©Copyright. All rights reserved.
    </div>
</footer>
  <!--Scripts-->          
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>