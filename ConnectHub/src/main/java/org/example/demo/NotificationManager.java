package org.example.demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

            // Extract notification details
            String senderID = (String) jsonObject.get("sender");
            String type = (String) jsonObject.get("type");
            String message = (String) jsonObject.get("message");
            String timeStampStr = (String) jsonObject.get("timestamp");
            JSONArray recieversArray = (JSONArray) jsonObject.get("recievers");
            JSONArray readByArray = (JSONArray) jsonObject.get("readBy");

            LocalDateTime timeStamp = LocalDateTime.parse(timeStampStr);

            // Create notification based on the extracted data
            Notification notification = new Notification();
            notification.setSender(getUserByID(senderID));
            notification.setType(type);
            notification.setMessage(message);
            notification.setTimestamp(timeStamp);

            // Set recievers and readBy as lists of User objects
            ArrayList<User> recievers = new ArrayList<>();
            for (Object receiverID : recieversArray) {
                String userID = (String) receiverID;
                User user = getUserByID(userID);
                if (user != null) {
                    recievers.add(user);
                }
            }
            notification.setRecievers(recievers);

            ArrayList<User> readBy = new ArrayList<>();
            for (Object readUserID : readByArray) {
                String userID = (String) readUserID;
                User user = getUserByID(userID);
                if (user != null) {
                    readBy.add(user);
                }
            }
            notification.setReadBy(readBy);

            notificationList.add(notification);
        }

        return notificationList;
    }

    public void addNotification(Notification notification) throws Exception {
        ArrayList<Notification> notificationList = readNotifications();
        notificationList.add(notification);

        JSONArray jsonArray = new JSONArray();
        for (Notification n : notificationList) {
            JSONObject jsonNotification = new JSONObject();
            jsonNotification.put("sender", n.getSender().getUserID());  // Save sender's ID
            jsonNotification.put("type", n.getType());
            jsonNotification.put("message", n.getMessage());
            jsonNotification.put("timestamp", n.getTimestamp().toString());

            // Add recievers and readBy as arrays of user IDs
            JSONArray recieversArray = new JSONArray();
            for (User receiver : n.getRecievers()) {
                recieversArray.add(receiver.getUserID());  // Add userID of each receiver
            }
            jsonNotification.put("recievers", recieversArray);

            JSONArray readByArray = new JSONArray();
            for (User reader : n.getReadBy()) {
                readByArray.add(reader.getUserID());  // Add userID of each user who has read
            }
            jsonNotification.put("readBy", readByArray);

            jsonArray.add(jsonNotification);
        }

        // Write to the file
        try (FileWriter writer = new FileWriter(NOTIFICATION_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonNotification = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                writer.write("    \"sender\": \"" + jsonNotification.get("sender") + "\",\n");
                writer.write("    \"type\": \"" + jsonNotification.get("type") + "\",\n");
                writer.write("    \"message\": \"" + jsonNotification.get("message") + "\",\n");
                writer.write("    \"timestamp\": \"" + jsonNotification.get("timestamp") + "\",\n");
                writer.write("    \"recievers\": " + jsonNotification.get("recievers") + ",\n");
                writer.write("    \"readBy\": " + jsonNotification.get("readBy") + "\n");

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

    public ArrayList<Notification> getNotificationsForUser(User user) throws Exception {
        ArrayList<Notification> allNotifications = readNotifications();
        ArrayList<Notification> userNotifications = new ArrayList<>();
        for (Notification notification : allNotifications) {
            if(notification.getRecievers().contains(user) || notification.getReadBy().contains(user)) {
                userNotifications.add(notification);
            }
        }

        return userNotifications;
    }

    public void removeNotification(Notification notification) throws Exception {
        // Step 1: Remove the notification from the list
        ArrayList<Notification> notificationList = readNotifications();

        // Find the notification to remove
        notificationList.removeIf(n -> n.equals(notification));

        // Step 2: Write the updated list back to the JSON file
        JSONArray jsonArray = new JSONArray();
        for (Notification n : notificationList) {
            JSONObject jsonNotification = new JSONObject();
            jsonNotification.put("sender", n.getSender().getUserID());  // Save sender's ID
            jsonNotification.put("type", n.getType());
            jsonNotification.put("message", n.getMessage());
            jsonNotification.put("timestamp", n.getTimestamp().toString());

            // Add recievers and readBy as arrays of user IDs
            JSONArray recieversArray = new JSONArray();
            for (User receiver : n.getRecievers()) {
                recieversArray.add(receiver.getUserID());  // Add userID of each receiver
            }
            jsonNotification.put("recievers", recieversArray);

            JSONArray readByArray = new JSONArray();
            for (User reader : n.getReadBy()) {
                readByArray.add(reader.getUserID());  // Add userID of each user who has read
            }
            jsonNotification.put("readBy", readByArray);

            jsonArray.add(jsonNotification);
        }

        // Write updated JSON data back to the file
        try (FileWriter writer = new FileWriter(NOTIFICATION_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonNotification = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                writer.write("    \"sender\": \"" + jsonNotification.get("sender") + "\",\n");
                writer.write("    \"type\": \"" + jsonNotification.get("type") + "\",\n");
                writer.write("    \"message\": \"" + jsonNotification.get("message") + "\",\n");
                writer.write("    \"timestamp\": \"" + jsonNotification.get("timestamp") + "\",\n");
                writer.write("    \"recievers\": " + jsonNotification.get("recievers") + ",\n");
                writer.write("    \"readBy\": " + jsonNotification.get("readBy") + "\n");

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
    public void updateNotification(Notification updatedNotification) throws Exception {
        // Step 1: Read the current notifications from the JSON file
        ArrayList<Notification> notificationList = readNotifications();
        // Step 2: Find the notification to update
        for (int i = 0; i < notificationList.size(); i++) {
            Notification existingNotification = notificationList.get(i);
            System.out.println(existingNotification.equals(updatedNotification));
            // Compare notifications by their unique fields (e.g., sender, type, timestamp)
            if (existingNotification.equals(updatedNotification)) {
                // Step 3: Update the notification
                notificationList.set(i, updatedNotification);  // Replace the old notification with the updated one
                break;
            }
        }
        // Step 4: Rewrite the entire notification list back to the JSON file
        JSONArray jsonArray = new JSONArray();
        for (Notification n : notificationList) {
            JSONObject jsonNotification = new JSONObject();
            jsonNotification.put("sender", n.getSender().getUserID());  // Save sender's ID
            jsonNotification.put("type", n.getType());
            jsonNotification.put("message", n.getMessage());
            jsonNotification.put("timestamp", n.getTimestamp().toString());

            // Add recievers and readBy as arrays of user IDs
            JSONArray recieversArray = new JSONArray();
            for (User receiver : n.getRecievers()) {
                recieversArray.add(receiver.getUserID());  // Add userID of each receiver
            }
            jsonNotification.put("recievers", recieversArray);

            JSONArray readByArray = new JSONArray();
            for (User reader : n.getReadBy()) {
                readByArray.add(reader.getUserID());  // Add userID of each user who has read
            }
            jsonNotification.put("readBy", readByArray);

            jsonArray.add(jsonNotification);
        }

        // Step 5: Write the updated notifications back to the file
        try (FileWriter writer = new FileWriter(NOTIFICATION_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonNotification = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                writer.write("    \"sender\": \"" + jsonNotification.get("sender") + "\",\n");
                writer.write("    \"type\": \"" + jsonNotification.get("type") + "\",\n");
                writer.write("    \"message\": \"" + jsonNotification.get("message") + "\",\n");
                writer.write("    \"timestamp\": \"" + jsonNotification.get("timestamp") + "\",\n");
                writer.write("    \"recievers\": " + jsonNotification.get("recievers") + ",\n");
                writer.write("    \"readBy\": " + jsonNotification.get("readBy") + "\n");

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
