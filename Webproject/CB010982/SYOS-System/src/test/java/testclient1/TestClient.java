/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testclient1;

/**
 *
 * @author User
 */
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestClient {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 10 concurrent threads

        // HttpClient that follows redirects automatically
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        String signUpUrl = "http://localhost:8080/SignUp";
        String loginUrl = "http://localhost:8080/Login";

        for (int i = 0; i < 10; i++) {
            int userId = i;

            executor.submit(() -> {
                try {
                    System.out.println("Processing user" + userId + "...");

                    // Step 1: Sign up the user
                    HttpRequest signUpRequest = HttpRequest.newBuilder()
                            .uri(URI.create(signUpUrl))
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    "username=user" + userId +
                                    "&password=pass" + userId +
                                    "&role=customer&email=user" + userId + "@syos.com"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .build();

                    HttpResponse<String> signUpResponse = client.send(signUpRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Sign-Up Response for user" + userId + ": " + signUpResponse.statusCode());
                    if (signUpResponse.statusCode() == 409) { // Assuming 409 for existing user
                        System.out.println("User " + userId + " already exists. Skipping sign-up.");
                    } else if (signUpResponse.statusCode() != 200) {
                        System.out.println("Sign-Up Failed for user" + userId + ": " + signUpResponse.body());
                        return;
                    }

                    // Step 2: Log in the user
                    HttpRequest loginRequest = HttpRequest.newBuilder()
                            .uri(URI.create(loginUrl))
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    "username=user" + userId + "&password=pass" + userId))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .build();

                    HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Login Response for user" + userId + ": " + loginResponse.statusCode());

                    // Log redirect location if it's a 302 response
                    if (loginResponse.statusCode() == 302) {
                        String location = loginResponse.headers().firstValue("Location").orElse("No Location Header");
                        System.out.println("Login Successful for user" + userId + ". Redirecting to: " + location);
                    } else if (loginResponse.statusCode() == 401) {
                        System.out.println("Login Failed for user" + userId + ": Invalid credentials.");
                    } else {
                        System.out.println("Unexpected response for user" + userId + ": " + loginResponse.body());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Optional: Add delay between requests
            try {
                Thread.sleep(100); // 100ms delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in time.");
            }
        } catch (InterruptedException e) {
            System.err.println("Executor interrupted during shutdown.");
        }
    }
}
