<%-- 
    Document   : SignUp
    Created on : Dec 23, 2024, 7:22:25 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <!-- Link to external CSS -->
    <link rel="stylesheet" href="../css/Login.css">
    <!-- font-awesome for icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="login-container">
                    <div class="login-form">
                        <h2 class="login-title">Sign Up</h2>
                        <p class="login-subtitle">Create your account at SYOS</p>
                <form action="SignUp" method="post">
                 <div class="input-group">
                     <label for="username">Username</label>
                     <input type="text" id="username" name="username" placeholder="Enter your username" required>
                 </div>
                 <div class="input-group">
                     <label for="email">Email</label>
                     <input type="email" id="email" name="email" placeholder="Enter your email" required>
                 </div>
                 <div class="input-group">
                     <label for="password">Password</label>
                     <div class="password-container">
                         <input type="password" id="password" name="password" placeholder="Enter your password" required>
                         <i class="fas fa-eye toggle-password"></i>
                     </div>
                 </div>
                 <button type="submit" class="btn">Sign Up</button>
                  <!-- Login Link -->
            <div class="login-option">
                <p class="login-text">Already have an account? 
                    <a href="<%= request.getContextPath() %>/Authentication/Login" class="login-link">Login</a>
                </p>
            </div>
                <p class="or-divider">— Check out our pages in —</p>
                <div class="social-login">
                    <button class="btn btn-facebook"><i class="fab fa-facebook-f"></i> Facebook</button>
                    <button class="btn btn-twitter"><i class="fab fa-twitter"></i> Twitter</button>
                </div>
             </form>
        </div>
    </div>
    <!-- external JS -->
    <script src="../js/script.js"></script>
</body>
</html>
