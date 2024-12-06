package org.example.demo;

import javafx.scene.control.Alert;

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
        Application app = new Application();
        try {
            if (!validateEmail(email)) {
                app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "Invalid email must contain '@'.");
                return false;
            }
            if (!validateUsername(username)) {
                app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "Invalid username.");
                return false;
            }
            if (!validateName(name)) {
                app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "Invalid name.");
                return false;
            }
            if (!validateDateOfBirth(dateOfBirth)) {
                app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "Invalid date of birth.");
                return false;
            }
            if (!validatePassword(password)) {
                app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "Invalid password, must be at least 8 characters with upper and lower case letters and special characters.");
                return false;
            }
            if (!validatePasswordMatch(password, reenteredPassword)) {
                app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "passwords do not match.");
                return false;
            }

            ArrayList<User> users = databaseManager.readUsers();
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "Username already in use.");
                    return false;
                }
                if (user.getUsername().equals(username)) {
                    app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "Email already in use.");
                    return false;
                }
            }

            String hashedPassword = hashPassword(password);
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setDOB(dateOfBirth);
            newUser.setPassword(hashedPassword);
            newUser.setUserID(databaseManager.getNextUserID(users));
            newUser.setStatus(true);
            users.add(newUser);
            Application.setCurrentUser(newUser);
            databaseManager.writeUser(newUser);
            return true; //"Signup successful!";
        } catch (Exception e) {
            e.printStackTrace();
            app.showAlert(Alert.AlertType.ERROR, "Signup Failed", "An error occurred during signup.");
            return false; //"An error occurred during signup.";
        }
    }
    // Login method
    public Boolean login(String email, String password) {
        Application app = new Application();
        try {
            ArrayList<User> users = databaseManager.readUsers();
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    if (user.getPassword().equals(hashPassword(password))) {
                        user.setStatus(true);
                        Application.setCurrentUser(user);
                        databaseManager.writeUser(user);
                        return true; //"Login successful!";
                    } else {
                        app.showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect password.");
                        return false;
                    }
                }
            }
            app.showAlert(Alert.AlertType.ERROR, "Login Failed", "User not found.");
            return false; //User not found
        } catch (Exception e) {
            e.printStackTrace();
            app.showAlert(Alert.AlertType.ERROR, "Login Failed", "An error occurred during login.");
            return false; //An error occurred during login
        }
    }
    public static void main(String[] args) throws NoSuchAlgorithmException {
        ValidationManager manager = new ValidationManager();
        System.out.println(manager.hashPassword("chill"));
    }
}
