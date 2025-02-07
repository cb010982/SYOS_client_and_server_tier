/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.syos.system.carttestsuite;

/**
 *
 * @author User
 */

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class TestClient {

    private static final String LOGIN_URL = "http://localhost:8080/Authentication/Login";
    private static final String CART_URL = "http://localhost:8080/Pages/cart";

  @Test
public void testConcurrentAddToCartActionsWithExistingUsers() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(8); // Simulate 8 concurrent users

    for (int i = 0; i < 10; i++) {
        int userId = i;
        String username = "user" + i;
        String password = "pass" + i;

        executor.submit(() -> {
            try {
                String sessionId = loginAndGetSessionId(username, password);

                // Adjusted productId and quantity
                sendAddToCartRequest(userId, 15, 1, sessionId); // Add 2 units of productId 15
            } catch (Exception e) {
                System.err.println("Error for user " + username + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.MINUTES);
}

private static String loginAndGetSessionId(String username, String password) throws Exception {
    // Prepare the login body
    String body = String.format("username=%s&password=%s", username, password);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(LOGIN_URL))
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build();

    // Send the login request
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    // Debug: Log headers and body
    System.out.println("Login Response Headers for " + username + ": " + response.headers());
    System.out.println("Login Response Body for " + username + ": " + response.body());

    // Check for a Set-Cookie header with JSESSIONID
    if ((response.statusCode() == 200 || response.statusCode() == 302) && 
        response.headers().firstValue("Set-Cookie").isPresent()) {
        return response.headers()
                .firstValue("Set-Cookie")
                .orElseThrow(() -> new RuntimeException("No session cookie found for user: " + username));
    } else {
        throw new RuntimeException("Login failed for user: " + username + " with response: " + response.body());
    }
}

private static void sendAddToCartRequest(int userId, int productId, int quantity, String sessionId) throws Exception {
    if (productId <= 0 || quantity <= 0) {
        throw new IllegalArgumentException("Invalid productId or quantity. Both must be greater than zero.");
    }

    // Add parameters in the URL
    String url = String.format("%s?action=add&productId=%d&quantity=%d", CART_URL, productId, quantity);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Cookie", sessionId) // Include session ID
            .build();

    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    // Validate the response
    if (response.statusCode() == 200) {
        System.out.println("Test passed for user " + userId + ": " + response.body());
    } else {
        System.err.println("Test failed for user " + userId + " with status code: " + response.statusCode());
    }
}


}

