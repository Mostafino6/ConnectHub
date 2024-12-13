package org.example.demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {
    private static final String NOTIFICATION_FILE = "C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\java\\org\\example\\demo\\notifications.json";
    private static final DatabaseManager databaseManager = new DatabaseManager();
    private Map<String, User> userMap = new HashMap<>();

    public User getUserByID(String userID) throws Exception {
        if (!userMap.containsKey(userID)) {
            // Load users into the map if they aren't already loaded
            ArrayList<User> userList = databaseManager.readUsers();
            for (User user : userList) {
                userMap.put(user.getUserID(), user);  // Store in the map
            }
        }
        return userMap.get(userID);  // Return the user from the map
    }

    public ArrayList<Notification> readNotifications() throws Exception {
        ArrayList<Notification> notificationList = new ArrayList<>();

        File file = new File(NOTIFICATION_FILE);
        if (!file.exists()) {
            return notificationList;  // If the file doesn't exist, return an empty list
        }

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            // Extract story details
            String userID = (String) jsonObject.get("userID");
            String type = (String) jsonObject.get("type");
            String message = (String) jsonObject.get("message");
            String timeStampStr = (String) jsonObject.get("timeStamp");
            boolean read = Boolean.parseBoolean(jsonObject.get("read").toString());
            LocalDateTime timeStamp = LocalDateTime.parse(timeStampStr);
            User user = getUserByID(userID);

            if (user != null) {
                Notification notification = new Notification(user, type, message);
                notification.setTimestamp(timeStamp);
                if (!notificationList.contains(notification)) notificationList.add(notification);
                if (!user.getNotifications().contains(notification)) user.addNotification(notification);
            }
        }
        return notificationList;
    }

    public void addNotification(Notification notification) throws Exception {
        ArrayList<Notification> notificationList = readNotifications();
        notificationList.add(notification);

        JSONArray jsonArray = new JSONArray();
        for (Notification n : notificationList) {
            JSONObject jsonNotification = new JSONObject();
            jsonNotification.put("userID", n.getOwner().getUserID());
            jsonNotification.put("type", n.getType());
            jsonNotification.put("message", n.getMessage());
            jsonNotification.put("timeStamp", n.getTimestamp().toString());
            jsonNotification.put("read", n.isRead());
            jsonArray.add(jsonNotification);
        }

        // Write to the file
        try (FileWriter writer = new FileWriter(NOTIFICATION_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonNotification = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                writer.write("    \"userID\": \"" + jsonNotification.get("userID") + "\",\n");
                writer.write("    \"type\": \"" + jsonNotification.get("type") + "\",\n");
                writer.write("    \"message\": \"" + jsonNotification.get("message") + "\",\n");
                writer.write("    \"timeStamp\": \"" + jsonNotification.get("timeStamp") + "\",\n");
                writer.write("    \"read\": " + jsonNotification.get("read") + "\n");

                writer.write("  }");

                if (i < jsonArray.size() - 1) {
                    writer.write(",\n");
                } else {
                    writer.write("\n");
                }
            }
            writer.write("]");
        }
    }

    public ArrayList<Notification> getNotificationsForUser(String userID) throws Exception {
        ArrayList<Notification> allNotifications = readNotifications();
        ArrayList<Notification> userNotifications = new ArrayList<>();

        for (Notification notification : allNotifications) {
            if (notification.getOwner().getUserID().equals(userID)) {
                userNotifications.add(notification);
            }
        }

        return userNotifications;
    }

    public void removeNotification(Notification notification) throws Exception {
        // Step 1: Remove the notification from the user's notification list
        ArrayList<Notification> notificationList = readNotifications();

        // Find the notification to remove in the list
        notificationList.removeIf(n -> n.equals(notification));  // Uses equals() method to match notifications

        // Step 2: Write the updated list back to the JSON file
        JSONArray jsonArray = new JSONArray();
        for (Notification n : notificationList) {
            JSONObject jsonNotification = new JSONObject();
            jsonNotification.put("userID", n.getOwner().getUserID());
            jsonNotification.put("type", n.getType());
            jsonNotification.put("message", n.getMessage());
            jsonNotification.put("timeStamp", n.getTimestamp().toString());
            jsonNotification.put("read", n.isRead());
            jsonArray.add(jsonNotification);
        }

        // Write updated JSON data back to the file
        try (FileWriter writer = new FileWriter(NOTIFICATION_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonNotification = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                writer.write("    \"userID\": \"" + jsonNotification.get("userID") + "\",\n");
                writer.write("    \"type\": \"" + jsonNotification.get("type") + "\",\n");
                writer.write("    \"message\": \"" + jsonNotification.get("message") + "\",\n");
                writer.write("    \"timeStamp\": \"" + jsonNotification.get("timeStamp") + "\",\n");
                writer.write("    \"read\": " + jsonNotification.get("read") + "\n");

                writer.write("  }");

                if (i < jsonArray.size() - 1) {
                    writer.write(",\n");
                } else {
                    writer.write("\n");
                }
            }
            writer.write("]");
        }
    }


}
