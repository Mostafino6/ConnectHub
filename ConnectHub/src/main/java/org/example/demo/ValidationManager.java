package org.example.demo;

import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValidationManager {
    private final DatabaseManager databaseManager;

    public ValidationManager() {
        this.databaseManager = new DatabaseManager();
    }

    // Validate email format
    public boolean validateEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }

    // Validate user name
    public static boolean validateUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.matches("^[a-zA-Z0-9_]{3,}$");
    }

    // Validate name
    public static boolean validateName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z]+(?: [a-zA-Z]+)*$");
    }

    // Validate date of birth
    public static boolean validateDateOfBirth(Date dob) {
        return dob != null && dob.before(new Date()); // Ensure the date is in the past
    }

    // Validate password strength
    public static boolean validatePassword(String password) {
        if (password == null || password.length() < 8) {
            return false; // Check for minimum length
        }
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"); // Add special characters as needed

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    // Check if the passwords match
    public static boolean validatePasswordMatch(String password, String reenteredPassword) {
        return password.equals(reenteredPassword);
    }

    // Hash password using SHA-256
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Signup method
    public Boolean signup(String email, String username, String name, String password, String reenteredPassword, Date dateOfBirth) {
        try {
            if (!validateEmail(email)) {
                System.out.println("1");
                return false;
            } //Invalid email format.
            if (!validateUsername(username)) {
                System.out.println("2");
                return false;
            }
            ; //"Username must be at least 3 characters long and contain only letters, numbers, or underscores.";
            if (!validateName(name)) {
                System.out.println("3");
                return false;
            } //"Invalid name format.";
            if (!validateDateOfBirth(dateOfBirth)) {
                System.out.println("4");
                return false;
            } //"Date of birth must be in the past.";
            if (!validatePassword(password)) {
                System.out.println("5");
                return false;
            } //"Password must be at least 8 characters long, contain at least one uppercase letter, and one digit.";
            if (!validatePasswordMatch(password, reenteredPassword)) {
                System.out.println("6");
                return false;
            } //"Passwords do not match.";

            // Check for existing email and username
            ArrayList<User> users = databaseManager.readUsers();
            for (User user : users) {
                if (user.getEmail().equals(email)) return false; //"Email already exists.";
                if (user.getUsername().equals(username)) return false; //"Username already exists.";
            }

            String hashedPassword = hashPassword(password);
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setDOB(dateOfBirth);
            newUser.setPassword(hashedPassword);
            newUser.setUserID(databaseManager.getNextUserID(users));// Set hashed password

            users.add(newUser);
            databaseManager.writeUser(newUser); // Save new user
            return true; //"Signup successful!";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
            return false; //"An error occurred during signup.";
        }
    }
    // Login method
    public Boolean login(String email, String password) {
        try {
            ArrayList<User> users = databaseManager.readUsers();
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    if (user.getPassword().equals(hashPassword(password))) {
                        user.setStatus(true); // Set user status to online
                        databaseManager.writeUser(user); // Update user status
                        return true; //"Login successful!";
                    } else {
                        return false; //"Incorrect password.";
                    }
                }
            }
            return false; //"User not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return false; //"An error occurred during login.";
        }
    }
}
