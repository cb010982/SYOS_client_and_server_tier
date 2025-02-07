<%-- 
    Document   : Login
    Created on : Dec 23, 2024, 7:22:01 PM
    Author     : User
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <!-- Link to external CSS -->
    <link rel="stylesheet" href="../css/Login.css">
    <!-- font-awesome for icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="login-container">
        <div class="login-form">
            <h2 class="login-title">Login</h2>
            <p class="login-subtitle">Have an account at SYOS?</p>
            <form action="<%= request.getContextPath() %>/Authentication/Login" method="post">
                <div class="input-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="Enter your username" required>
                </div>
                <div class="input-group">
                    <label for="password">Password</label>
                    <div class="password-container">
                        <input type="password" id="password" name="password" placeholder="Enter your password" required>
                        <i class="fas fa-eye toggle-password"></i>
                    </div>
                </div>
                <div class="form-options">
                    <label>
                        <input type="checkbox" name="remember"> Remember Me
                    </label>
                    <a href="#" class="forgot-password">Forgot Password?</a>
                </div>
                <button type="submit" class="btn">Sign In</button>
            </form>
                        <!-- Add Sign-Up Button -->
                <div class="signup-option">
                    <p class="signup-text">Don't have an account? 
                        <a href="<%= request.getContextPath() %>/Authentication/SignUp" class="signup-link">Sign Up</a>
                    </p>
                </div>

            <p class="or-divider">— Check out our pages in —</p>
            <div class="social-login">
                <button class="btn btn-facebook"><i class="fab fa-facebook-f"></i> Facebook</button>
                <button class="btn btn-twitter"><i class="fab fa-twitter"></i> Twitter</button>
            </div>

        </div>
    </div>
    <!-- External JS -->
    <script src="../js/script.js"></script>
</body>
</html>
