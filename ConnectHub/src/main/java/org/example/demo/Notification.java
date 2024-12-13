package org.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Notification {
    private User sender;
    private ArrayList<User> recievers;
    private String type;
    private String message;
    private LocalDateTime timestamp;
    private ArrayList<User> readBy;

    public Notification() {
        this.recievers = new ArrayList<>();
        this.type = type;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.readBy = new ArrayList<>();
    }
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public ArrayList<User> getRecievers() {
        return recievers;
    }
    public void setRecievers(ArrayList<User> recievers) {
        this.recievers = recievers;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public ArrayList<User> getReadBy() {
        return readBy;
    }
    public void setReadBy(ArrayList<User> readBy) {
        this.readBy = readBy;
    }
    public String formatDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm     dd/MM/yyyy");
        return this.getTimestamp().format(formatter);
    }
    public void addReciever(User user) {
        this.recievers.add(user);
    }
    public void readByUser(User user) {
        this.readBy.add(user);
        this.recievers.remove(user);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // If both references point to the same object
        if (obj == null || getClass() != obj.getClass()) return false; // Ensure obj is not null and of the same class

        // Cast the object to a Notification
        Notification that = (Notification) obj;

        // Compare sender (use the userID comparison)
        if (sender != null ? !sender.equals(that.sender) : that.sender != null){
            System.out.println("Sender mismatch");
            return false;
        }
        // Compare type, message, and timestamp fields
        return Objects.equals(type, that.type) &&
                Objects.equals(message, that.message) &&
                Objects.equals(timestamp, that.timestamp);
    }
    @Override
    public int hashCode() {
        return Objects.hash(sender, recievers, readBy, type, message, timestamp);
    }
}
