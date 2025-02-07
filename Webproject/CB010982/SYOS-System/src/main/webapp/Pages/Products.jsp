<%-- 
    Document   : Products
    Created on : Dec 25, 2024, 8:36:45 PM
    Author     : User
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!--<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
     Include Bootstrap CSS 
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Products.css">
    
</head>
<body>
     Navbar 
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand" href="#">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="SYOS Grocery Logo">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="#">Home</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">Products</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">Cart</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">Contact</a></li>
                </ul>
            </div>
        </div>
    </nav>
    
     Slideshow 
    <div id="slideshow" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="${pageContext.request.contextPath}/images/slideshow1.png" class="d-block w-100" alt="Slide 1">
            </div>
            <div class="carousel-item">
                <img src="${pageContext.request.contextPath}/images/slideshow2.png" class="d-block w-100" alt="Slide 2">
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

     Product Grid 
 
    <div class="container">
        <h1 class="text-center my-4">Our Products</h1>
        <h1 class="subtext">Check out our latest stock!</h1>
        <div class="row">
            <c:forEach var="product" items="${products}">
                <div class="col-md-4 mb-4">
                    <div class="product-card">
                        <div class="product-image">
                            <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid">
                            <div class="price-tag">
                          
                                <span class="price-value">$${product.finalPrice}</span>
                            </div>
                        </div>
                        <div class="product-info">
                            <p class="subcategory-name">${product.subCategory.name}</p>
                            <h5 class="product-name">${product.name}</h5>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <c:if test="${empty products}">
            <p class="text-center text-danger">No products available.</p>
        </c:if>
    </div>

         Second Carousel 
    <div id="carousel-2" class="carousel slide my-4" data-bs-ride="carousel">
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="images/carousal4.jpg" class="d-block w-100" alt="Slide 1">
            </div>
            <div class="carousel-item">
                <img src="images/carousal5.png" class="d-block w-100" alt="Slide 2">
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carousel-2" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carousel-2" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
        
             Product Grid 
 
    <div class="container">
        <div class="row">
            <c:forEach var="product" items="${products}">
                <div class="col-md-4 mb-4">
                    <div class="product-card">
                        <div class="product-image">
                            <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid">
                            <div class="price-tag">
                          
                                <span class="price-value">$${product.finalPrice}</span>
                            </div>
                        </div>
                        <div class="product-info">
                            <p class="subcategory-name">${product.subCategory.name}</p>
                            <h5 class="product-name">${product.name}</h5>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <c:if test="${empty products}">
            <p class="text-center text-danger">No products available.</p>
        </c:if>
    </div>



     Include Bootstrap JS 
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>-->
