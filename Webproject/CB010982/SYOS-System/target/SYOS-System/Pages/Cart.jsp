<%-- 
    Document   : Cart
    Created on : Jan 2, 2025, 6:33:12 PM
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
    <title>Your Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Cart.css">   
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

    <!-- Slideshow -->
    <div id="slideshow" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="${pageContext.request.contextPath}/images/8.png" class="d-block w-100" alt="Slide 1">
            </div>
            <div class="carousel-item">
                <img src="${pageContext.request.contextPath}/images/11.png" class="d-block w-100" alt="Slide 2">
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#slideshow" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#slideshow" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
            
    <!--Browse Categories-->
     <div class="container text-center mt-5">
        <h2 class="category-heading">Browse All Categories</h2>
        <p class="category-subtext">Sticky niche markets via goal-oriented networks. Completely recapitalize.</p>
        <div class="row mt-4 justify-content-center">
            <div class="col-md-2">
                <div class="category-card active-category">
                    <img src="/images/4.png" alt="Medicine">
                    <h5>Medicine</h5>             
                </div>
            </div>
            <div class="col-md-2">
                <div class="category-card active-category">
                    <img src="/images/13.png" alt="Makeup">
                    <h5>Makeup</h5>               
                </div>
            </div>
            <div class="col-md-2">
                <div class="category-card active-category">
                    <img src="/images/7.png" alt="Vegetables">
                    <h5>Vegetables</h5>               
                </div>
            </div>
            <div class="col-md-2">
                <div class="category-card active-category">
                    <img src="/images/5.png" alt="Stationary">
                    <h5>Stationary</h5>             
                </div>
            </div>
            <div class="col-md-2">
                <div class="category-card active-category">
                    <img src="/images/10.png" alt="Dessert">
                    <h5>Dessert</h5>               
                </div>
            </div>
            <div class="col-md-2">
                <div class="category-card active-category">
                    <img src="/images/9.png" alt="Toys">
                    <h5>Toys</h5>                 
                </div>
            </div>
        </div>
    </div>
    <div class="container mt-5">
        <div class="row">
            <!-- Fresh Vegetable Banner -->
            <div class="col-md-6">
                <div class="promo-card">
                    <img src="/images/shop1.png" alt="Fresh Vegetables" class="promo-image">
                    <div class="promo-overlay">
                        <h4>Enjoy up to 20%</h4>
                        <h2>Fresh Vegetable</h2>
                        <a href="#" class="btn btn-success">Shop Now</a>
                    </div>
                </div>
            </div>

            <!-- Organic Product Banner -->
            <div class="col-md-6">
                <div class="promo-card">
                    <img src="/images/oven.webp" alt="Organic Products" class="promo-image">
                    <div class="promo-overlay">
                        <h4>Enjoy up to 20% on all Products</h4>
                        <h2>All Tasted Organic & Fresh Product</h2>
                        <a href="#" class="btn btn-warning">Shop Now</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

<section style="background-color: #ffffff; padding: 40px 20px;">
    <div style="max-width: 1200px; margin: 0 auto;">
        <h2 style="text-align: center; color: #d57b1f;">Our Products</h2>

        <!-- Product Form -->
    <form id="cartForm" method="post" action="${pageContext.request.contextPath}/Pages/cart">
        <input type="hidden" name="action" value="sendToCartItems" />

        <div class="row row-cols-1 row-cols-md-4 g-4" id="productGrid">
            <c:forEach var="product" items="${products}">
                <div class="col">
                    <div class="product-card p-3 original-product">
                        
                       <!-- Stock and Sale Badges -->
                        <span class="badge-stock">IN STOCK</span>
                        <span class="badge-sale">SALE</span>
                        <div class="image-container">
                            <img src="${product.imageUrl}" alt="${product.name}" class="product-img">
                        </div>
                        
                        <!-- Hover Overlay (Icons) -->
                                <div class="hover-overlay">
                                    <div class="icon-btn"><i class="fa-regular fa-heart"></i></div>  
                                    <div class="icon-btn"><i class="fa-solid fa-shuffle"></i></div>  
                                    <div class="icon-btn"><i class="fa-regular fa-eye"></i></div>  
                                    <div class="icon-btn"><i class="fa-solid fa-cart-plus"></i></div>  
                                </div>
                                <div class="star-rating">★ ★ ★ ★ ☆ (4.5)</div>
                            <p class="card-title mb-1">${product.name}</p>
                            
                        <!-- Price Display -->
                        <p class="price">
                            $<span class="product-price">
                                <fmt:formatNumber value="${product.finalPrice}" type="number" minFractionDigits="2" maxFractionDigits="2" />
                                 <span class="old-price">$150.00</span>
                            </span>
                        </p>
                        
                        <!-- Quantity Controls -->
                        <div class="quantity-container" style="display: none;">
                            <button type="button" class="quantity-btn decrement">-</button>
                            <input type="number" name="quantity-${product.id}" value="0" min="0" class="quantity-input">
                            <button type="button" class="quantity-btn increment">+</button>

                            <button type="button" class="btn btn-danger trash-btn" style="margin-left: 10px;">
                         <i class="fa-solid fa-trash"></i>
                         </button>
                        </div>

                        <!-- Total Display -->
                      <p class="fw-bold text-center total-price-container" style="display: none;">
                            Total: $<span class="total-price">0.00</span>
                        </p>

                        <!-- Clone Button -->
                        <div class="text-center mt-2">
                            <button type="button" class="btn btn-primary test-btn">Add To Cart</button>
                        </div>

                        <input type="hidden" name="productId" value="${product.id}" />
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Checkout Button -->
        <div class="text-center mt-3">
            <button type="submit" class="btn btn-success checkout-btn">Checkout to Cart</button>
        </div>
    </form>

    <!-- Toggle Cart Button (Will Change Icon Dynamically) -->
    <button id="toggleCartBtn" style="
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 1000;
        background: #ffffff;
        border-color: grey;
        border-width: thin 1px;
        color: black;
        padding: 10px 15px;
        font-size: 16px;
        cursor: pointer;
        border-radius: 5px;
        display: flex;
        align-items: center;
        justify-content: center;
    ">
        <i id="cartIcon" class="fa-solid fa-cart-shopping"></i> <!-- FontAwesome Cart Icon -->
    </button>

    <!-- Side Panel for Cloned Products -->
    <div id="sidePanel" style="
        position: fixed;
        top: 0;
        right: -600px;
        width: 600px;
        height: 100vh;
        background: white;
        border-left: 2px solid #ddd;
        box-shadow: -3px 0 5px rgba(0, 0, 0, 0.1);
        overflow-y: auto;
        padding: 15px;
        transition: right 0.3s ease-in-out;
    ">
        <h3 class="cart-heading">Cart Items</h3>

        <div id="sidePanelContent"></div>
        <hr>
        <!-- Grand Total Section -->
        <div class="container mt-4">
            <h3 class="cart-heading2">
                Grand Total: <span id="grandTotal">0.00</span>
            </h3>
        </div>
    </div>

 <!-- Image Carousel -->
    <div class="container mt-5">
        <div id="multiItemCarousel" class="carousel slide" data-bs-ride="carousel">
            
            <div class="carousel-inner">
                <!-- First Slide -->
                <div class="carousel-item active">
                    <div class="row">
                        <div class="col-md-4">
                            <img src="/images/Nivea_skincare.JPG" class="d-block w-100 carousel-img" alt="Luxury Beauty">
                        </div>
                        <div class="col-md-4">
                            <img src="/images/Imorich_vanilla_ice_cream.jpg" class="d-block w-100 carousel-img" alt="Makeup Brushes">
                        </div>
                        <div class="col-md-4">
                            <img src="/images/scent.webp" class="d-block w-100 carousel-img" alt="Natural Beauty">
                        </div>
                    </div>
                </div>

                <!-- Second Slide -->
                <div class="carousel-item">
                    <div class="row">
                        <div class="col-md-4">
                            <img src="/images/frozen_parata_krest.webp" class="d-block w-100 carousel-img" alt="Skincare Tips">
                        </div>
                        <div class="col-md-4">
                            <img src="/images/Sunquick_bottle.jpg" class="d-block w-100 carousel-img" alt="Beauty Hacks">
                        </div>
                        <div class="col-md-4">
                            <img src="/images/ponds1.jpg" class="d-block w-100 carousel-img" alt="Self-Care">
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Carousel Controls -->
            <button class="carousel-control-prev" type="button" data-bs-target="#multiItemCarousel" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#multiItemCarousel" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
    </div>
            
 <!-- Top Divider for a Rich Look -->
    <div class="blog-divider"></div>

    <div class="container text-center mt-5">
        <h2 class="blog-heading">From The Blog</h2>
        <p class="blog-subtext">Get all the latest skincare essentials news, tips, and tricks to nurture your skin glowy, dewy best before you even think makeup.</p>

        <div class="row mt-4 justify-content-center">
            <!-- Blog Post 1 -->
            <div class="col-md-4">
                <div class="blog-card">
                    <img src="/images/27.png" alt="Luxury Beauty" class="blog-image">
                    <h5 class="blog-title">Tips & procedure to apply luxury beauty cosmetic cream</h5>
                    <p class="blog-date">APRIL 2, 2021</p>
                </div>
            </div>

            <!-- Blog Post 2 -->
            <div class="col-md-4">
                <div class="blog-card">
                    <img src="/images/25.png" alt="Makeup Brushes" class="blog-image">
                    <h5 class="blog-title">The best part about makeup is brush and its types</h5>
                    <p class="blog-date">APRIL 2, 2021</p>
                </div>
            </div>

            <!-- Blog Post 3 -->
            <div class="col-md-4">
                <div class="blog-card">
                    <img src="/images/32.png" alt="Lightweight Makeup" class="blog-image">
                    <h5 class="blog-title">Lightweight makeup to enhance your natural beauty</h5>
                    <p class="blog-date">APRIL 2, 2021</p>
                </div>
            </div>
        </div>
    </div>

    <div class="blog-bottom-space"></div>
</section>

<!--Footer-->
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

<!--External Scripts-->
 <script src="${pageContext.request.contextPath}/js/cart.js"></script>
 <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
 <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
 <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


