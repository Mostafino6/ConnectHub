package org.example.demo;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    // Add a notification to a specific user
    public void addNotification(User user, String type, String message) {
        Notification notification = new Notification(type, message);
        user.addNotification(notification);
    }

    // Get all notifications for a specific user
    public ArrayList<Notification> getNotifications(User user) {
        return user.getNotifications();
    }

    // Mark all notifications as read for a specific user
    public void markAllAsRead(User user) {
        user.markAllAsRead();
    }


}
