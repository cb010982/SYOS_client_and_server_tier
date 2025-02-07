<%-- 
    Document   : CartItems
    Created on : Jan 14, 2025, 10:51:37 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/CartItems.css">
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
<div class="container">
    <div class="row">
        <!-- Left Column: Delivery & Payment Details -->
        <div class="col-md-7">
            <form method="post" action="${pageContext.request.contextPath}/Checkout">
                <!-- Shipping Details -->
                <div class="details-section">
                    <div class="section-header" data-bs-toggle="collapse" data-bs-target="#shippingDetails" aria-expanded="false">
                        DELIVERY DETAILS
                        <i class="fas fa-chevron-down"></i>
                    </div>
                    <div id="shippingDetails" class="collapse show">
                     <div class="row">
                        <div class="col-md-6 form-group">
                            <label>First Name </label>
                            <input type="text" name="firstName" required pattern="[A-Za-z\-'\s]+" title="First name must only contain letters, spaces, hyphens, or apostrophes" class="form-control">
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Last Name </label>
                            <input type="text" name="lastName" required pattern="[A-Za-z\-'\s]+" title="Last name must only contain letters, spaces, hyphens, or apostrophes" class="form-control">
                        </div>
                    </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Address </label>
                                <input type="text" name="address" required class="form-control">
                            </div>
                            <div class="col-md-6 form-group">
                                <label>City </label>
                                <input type="text" name="city" required class="form-control">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Postal Code </label>
                                <input type="text" name="postalCode" required class="form-control">
                            </div>
                           <div class="col-md-6 form-group">
                                <label>Telephone </label>
                                <input type="text" name="telephone" required pattern="\d{10}" title="Telephone number must be 10 digits" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Payment Details -->
                <div class="details-section">
                    <div class="section-header" data-bs-toggle="collapse" data-bs-target="#paymentDetails" aria-expanded="false">
                        PAYMENT METHOD
                        <i class="fas fa-chevron-down"></i>
                    </div>
                    <div id="paymentDetails" class="collapse show">
                        <div class="row payment-options">
                            <div class="col-md-4 form-group">
                                <label class="payment-option">
                                    <input type="radio" name="paymentMethod" value="visa" checked>
                                    <img src="https://upload.wikimedia.org/wikipedia/commons/4/41/Visa_Logo.png" alt="Visa">
                                </label>
                            </div>
                            <div class="col-md-6 form-group">
                                <label class="payment-option">
                                    <input type="radio" name="paymentMethod" value="mastercard">
                                    <img src="https://upload.wikimedia.org/wikipedia/commons/b/b7/MasterCard_Logo.svg" alt="MasterCard">
                                </label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Card Name </label>
                                <input type="text" name="cardName" required pattern="^[A-Za-z\s]+$" title="Card name must not contain numbers" class="form-control">
                            </div>
                            <div class="col-md-6 form-group">
                                <label>Card Number </label>
                                <input type="text" name="cardNumber" required pattern="\d{16}" title="Card number must be 16 digits" maxlength="16" class="form-control">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Expiration Date </label>
                                <input type="text" id="expDate" name="expDate" required placeholder="MM/YY" class="form-control">
                            </div>
                            <div class="col-md-6 form-group">
                                <label>CVC</label>
                                <input type="tel" name="cvv" required pattern="\d{3}" title="CVV must be 3 digits" class="form-control" maxlength="3">
                            </div>
                        </div>
                    </div>
                </div>
        </div>

        <!-- Right Column: Order Summary -->
        <div class="col-md-5">
            <div class="cart-section">
                <h3>YOUR ORDER</h3>
                <c:if test="${not empty cartItems}">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Qty</th>
                                <th>Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>${item.productName}</td>
                                    <td>${item.quantity}</td>
                                    <td>$<fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2" /></td>
                                </tr>
                                <!-- Hidden Inputs to Submit Cart Data -->
                                <input type="hidden" name="productId" value="${item.productId}">
                                <input type="hidden" name="productName-${item.productId}" value="${item.productName}">
                                <input type="hidden" name="quantity-${item.productId}" value="${item.quantity}">
                                <input type="hidden" name="price-${item.productId}" value="${item.price}">
                            </c:forEach>
                        </tbody>
                    </table>
                    <input type="hidden" name="grossTotal" value="${grossTotal}">
                    <div class="total-box">
                        <strong>Total:</strong> $<fmt:formatNumber value="${grossTotal}" type="number" minFractionDigits="2" />
                    </div>
                </c:if>
                <c:if test="${empty cartItems}">
                    <p class="empty-cart">Your cart is empty. Start shopping now!</p>
                </c:if>
                
                <button type="submit" class="btn btn-dark w-100 mt-3">Proceed to Checkout</button>
            </div>
        </div>
    </div>
    </form>  
</div>

<!--footer-->
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
     <!--External script tag-->       
<script src="${pageContext.request.contextPath}/js/CartItems.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>