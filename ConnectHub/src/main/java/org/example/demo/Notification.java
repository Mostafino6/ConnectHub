package org.example.demo;

import java.time.LocalDate;
import java.util.Date;

public class Notification {
    private String type;
    private String message;
    private LocalDate timestamp;
    private boolean read;

    public Notification(String type, String message) {
        this.type = type;
        this.message = message;
        this.timestamp = LocalDate.now();
        this.read = false;
    }


    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String toString(User user) {
        return "[" + type + "] " + message + " " + user.getUsername() + " - " + timestamp;
    }
}
