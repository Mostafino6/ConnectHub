package org.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Notification {
    private User owner;
    private String type;
    private String message;
    private LocalDateTime timestamp;
    private boolean read;

    public Notification(User owner, String type, String message) {
        this.owner = owner;
        this.type = type;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public User getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String formatDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm     dd/MM/yyyy");
        return this.getTimestamp().format(formatter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Notification notification = (Notification) obj;

        // Compare fields that uniquely identify the Notification
        return owner.getUserID().equals(notification.owner.getUserID()) &&
                type.equals(notification.type) &&
                message.equals(notification.message) &&
                timestamp.truncatedTo(ChronoUnit.SECONDS).equals(notification.timestamp.truncatedTo(ChronoUnit.SECONDS));
    }

    public String toString(User user) {
        return "[" + type + "] " + message + " " + user.getUsername() + " - " + timestamp;
    }
}
