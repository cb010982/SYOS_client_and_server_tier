package com.mycompany.syos.system.service;

//import com.mycompany.syos.database.dao.UserDAO;
//import com.mycompany.syos.system.model.UserModel;
import com.mycompany.syos.system.users.User;
//import com.mycompany.syos.system.exceptions.InvalidUserException;
//import com.mycompany.syos.system.exceptions.InvalidRoleException;
import com.mycompany.syos.system.utils.ValidationUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


import com.mycompany.database_syos.dao.UserDAO;
import com.mycompany.database_syos.models.UserModel;
import com.mycompany.database_syos.exceptions.InvalidUserException;
import com.mycompany.database_syos.exceptions.InvalidRoleException;
import java.util.concurrent.Semaphore;



public class UserService {

    private final UserDAO userDAO;
    private User loggedInUser;  
    private static final Semaphore semaphore = new Semaphore(5);

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void showUserMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- User Management Menu ---");
            System.out.println("1. View all users");
            System.out.println("2. Create a new user");
            System.out.println("3. Edit a user");
            System.out.println("4. Delete a user");
            System.out.println("5. Exit");
            System.out.println("Enter your choice (1/2/3/4/5):");

            int choice = getValidIntInput(scanner);

            try {
                switch (choice) {
                    case 1:  
                        viewAllUsers();
                        break;
                    case 2:  
                        createUser(scanner);
                        break;
                    case 3: 
                        viewAllUsers();  
                        editUser(scanner);
                        break;
                    case 4:  
                        viewAllUsers();  
                        deleteUser(scanner);
                        break;
                    case 5: 
                        running = false;
                        System.out.println("Exiting User Management Menu.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private int getValidIntInput(Scanner scanner) {
        int input = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                input = scanner.nextInt();
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); 
            }
        }
        return input;
    }

    private void viewAllUsers() {
        System.out.println("Viewing all users...");

        List<UserModel> users = userDAO.getAllUsers();
        for (UserModel user : users) {
            System.out.println("ID: " + user.getUserId() + ", Username: " + user.getUsername() + ", Role: " + user.getRole());
        }
    }

    public void createUser(Scanner scanner) {
        try {
            semaphore.acquire(); // Acquire a permit

            scanner.nextLine();
            String newUsername;
            do {
                System.out.println("Enter the new user's username:");
                newUsername = scanner.nextLine().trim();
                if (newUsername.isEmpty()) {
                    System.out.println("Username cannot be empty. Please enter a valid username.");
                }
            } while (newUsername.isEmpty());

            System.out.println("Enter the new user's password:");
            String newPassword = scanner.nextLine().trim();
            String passwordHash = newPassword; // Add actual hashing logic here

            String newUserRole;
            do {
                try {
                    System.out.println("Available Roles: manager, cashier, storekeeper, supervisor, admin, customer");
                    System.out.println("Enter the new user's role:");
                    newUserRole = scanner.nextLine().trim().toLowerCase();

                    if (!ValidationUtils.VALID_ROLES.contains(newUserRole)) {
                        throw new InvalidRoleException("Invalid role. Please choose a valid role.");
                    }
                } catch (InvalidRoleException e) {
                    System.out.println(e.getMessage());
                    newUserRole = null;
                }
            } while (newUserRole == null);

            String newEmail;
            do {
                try {
                    System.out.println("Enter the new user's email:");
                    newEmail = scanner.nextLine().trim();

                    if (!ValidationUtils.isValidEmail(newEmail)) {
                        throw new InvalidUserException("Invalid email format. Email must contain '@' and end with 'syos.com'.");
                    }
                } catch (InvalidUserException e) {
                    System.out.println(e.getMessage());
                    newEmail = null;
                }
            } while (newEmail == null);

            UserModel newUserModel = new UserModel(0, newUsername, passwordHash, newUserRole, newEmail, LocalDateTime.now());

            userDAO.createUser(newUserModel);

            System.out.println("New user '" + newUsername + "' with role '" + newUserRole + "' created successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("User creation operation interrupted.");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the user.");
            e.printStackTrace();
        } finally {
            semaphore.release(); // Release the permit
        }
    }

  public void editUser(Scanner scanner) {
        try {
            semaphore.acquire(); // Acquire a permit

            System.out.println("Enter the ID of the user you want to edit:");
            int userIdToEdit = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the new username:");
            String editUsername = scanner.nextLine().trim();

            System.out.println("Enter the new password (leave blank to keep unchanged):");
            String editPassword = scanner.nextLine().trim();
            String editPasswordHash = editPassword.isEmpty() ? null : editPassword;

            String editRole;
            do {
                try {
                    System.out.println("Available Roles: manager, cashier, storekeeper, supervisor, admin, customer");
                    System.out.println("Enter the new role:");
                    editRole = scanner.nextLine().trim();

                    if (!ValidationUtils.VALID_ROLES.contains(editRole)) {
                        throw new InvalidRoleException("Invalid role. Please choose a valid role.");
                    }
                } catch (InvalidRoleException e) {
                    System.out.println(e.getMessage());
                    editRole = null;
                }
            } while (editRole == null);

            String editEmail;
            do {
                try {
                    System.out.println("Enter the new email:");
                    editEmail = scanner.nextLine().trim();

                    if (!ValidationUtils.isValidEmail(editEmail)) {
                        throw new InvalidUserException("Invalid email format. Email must contain '@' and end with 'syos.com'.");
                    }
                } catch (InvalidUserException e) {
                    System.out.println(e.getMessage());
                    editEmail = null;
                }
            } while (editEmail == null);

            UserModel existingUser = userDAO.getAllUsers().stream().filter(u -> u.getUserId() == userIdToEdit).findFirst().orElse(null);

            if (existingUser != null) {
                existingUser.setUsername(editUsername);
                if (editPasswordHash != null) {
                    existingUser.setPasswordHash(editPasswordHash);
                }
                existingUser.setRole(editRole);
                existingUser.setEmail(editEmail);

                userDAO.updateUser(existingUser);
                System.out.println("User updated successfully!");
            } else {
                throw new InvalidUserException("User with ID " + userIdToEdit + " not found.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("User editing operation interrupted.");
        } catch (Exception e) {
            System.out.println("An error occurred while editing the user.");
            e.printStackTrace();
        } finally {
            semaphore.release(); // Release the permit
        }
    }


    public void deleteUser(Scanner scanner) {
        try {
            semaphore.acquire(); // Acquire a permit

            System.out.println("Enter the ID of the user you want to delete:");
            int userIdToDelete = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Are you sure you want to delete user with ID " + userIdToDelete + "? (yes/no)");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("yes")) {
                System.out.println("User deletion cancelled.");
                return;
            }

            UserModel userToDelete = userDAO.getAllUsers().stream().filter(u -> u.getUserId() == userIdToDelete).findFirst().orElse(null);
            if (userToDelete == null) {
                throw new InvalidUserException("User with ID " + userIdToDelete + " not found.");
            }

            userDAO.deleteUser(userIdToDelete);
            System.out.println("User with ID " + userIdToDelete + " deleted successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("User deletion operation interrupted.");
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the user.");
            e.printStackTrace();
        } finally {
            semaphore.release(); // Release the permit
        }
    }

    public List<UserModel> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public void signUpCustomer(Scanner scanner) {
            try {
                semaphore.acquire(); // Acquire a permit

                scanner.nextLine();

                String username;
                do {
                    System.out.println("Enter your username:");
                    username = scanner.nextLine().trim();
                    if (username.isEmpty()) {
                        System.out.println("Username cannot be empty. Please enter a valid username.");
                    }
                } while (username.isEmpty());

                System.out.println("Enter your password:");
                String password = scanner.nextLine().trim();
                String passwordHash = password;  

                String email;
                do {
                    System.out.println("Enter your email:");
                    email = scanner.nextLine().trim();
                    if (!ValidationUtils.isValidCustomerEmail(email)) {
                        System.out.println("Invalid email format. Please enter a valid email.");
                        email = null;  
                    }
                } while (email == null);

                String role = "customer";

                UserModel newCustomer = new UserModel(0, username, passwordHash, role, email, LocalDateTime.now());

                userDAO.createUser(newCustomer);
                System.out.println("Customer account created successfully. You can now log in.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Sign-up operation interrupted.");
            } catch (Exception e) {
                System.out.println("An error occurred during sign up: " + e.getMessage());
                e.printStackTrace();
            } finally {
                semaphore.release(); // Release the permit
            }
        }

}
