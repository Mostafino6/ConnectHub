package org.example.demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {
    private static final String DATABASE_FILE = "users.json";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    // Parses a date string into a Date object
    public Date parseDate(String dateString) throws Exception {
        return dateFormat.parse(dateString);
    }

    // Formats a Date object into a string
    public String formatDate(Date date) {
        return dateFormat.format(date);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<User> readUsers() throws Exception {
        File file = new File(DATABASE_FILE);
        if (!file.exists()) {
            return null; // If the file does not exist, return null or an empty list
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));

        ArrayList<User> userList = new ArrayList<>();

        // Extract the main user information
        String userId = (String) jsonObject.get("userID");
        String username = (String) jsonObject.get("username");
        String name = (String) jsonObject.get("name");
        String email = (String) jsonObject.get("email");
        String password = (String) jsonObject.get("password");
        Date dob = parseDate((String) jsonObject.get("DOB"));
        boolean status = (boolean) jsonObject.get("status");
        String bio = (String) jsonObject.get("bio");

        // Create a main user object
        User mainUser = new User(username, name, email, dob);
        mainUser.setUserID(userId);
        mainUser.setPassword(password);  // Store the password (likely hashed in real usage)
        mainUser.setStatus(status);
        mainUser.setBio(bio);

        // Parsing the friends section (nested objects)
        JSONObject friendsObject = (JSONObject) jsonObject.get("friends");

        // Parse friends list
        JSONArray friendsList = (JSONArray) friendsObject.get("friendsList");
        mainUser.getFriends().setFriendsList(parseNestedUsers(friendsList));

        // Parse friend requests
        JSONArray friendRequests = (JSONArray) friendsObject.get("friendRequests");
        mainUser.getFriends().setFriendRequests(parseNestedUsers(friendRequests));

        // Parse blocked friends
        JSONArray blockedFriends = (JSONArray) friendsObject.get("blockedFriends");
        mainUser.getFriends().setBlockedFriends(parseNestedUsers(blockedFriends));

        // Add the main user to the list
        userList.add(mainUser);

        return userList;  // Return the list of users (only one in this case)
    }

    private ArrayList<User> parseNestedUsers(JSONArray jsonArray) throws Exception {
        ArrayList<User> users = new ArrayList<>();
        for (Object obj : jsonArray) {
            JSONObject jsonUser = (JSONObject) obj;

            String userId = (String) jsonUser.get("userID");
            String username = (String) jsonUser.get("username");
            String name = (String) jsonUser.get("name");
            String email = (String) jsonUser.get("email");
            Date dob = parseDate((String) jsonUser.get("DOB"));
            boolean status = (boolean) jsonUser.get("status");
            String bio = (String) jsonUser.get("bio");

            User user = new User(username, name, email, dob);
            user.setUserID(userId);
            user.setStatus(status);
            user.setBio(bio);
            users.add(user);
        }
        return (ArrayList<User>) users;
    }

    @SuppressWarnings("unchecked")
    public void writeUser(User user) throws IOException {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("userID", user.getUserID());
        jsonUser.put("username", user.getUsername());
        jsonUser.put("name", user.getName());
        jsonUser.put("email", user.getEmail());
        jsonUser.put("password", user.getPassword());
        jsonUser.put("DOB", formatDate(user.getDOB()));
        jsonUser.put("status", user.getStatus());
        jsonUser.put("bio", user.getBio());

        JSONObject friends = new JSONObject();
        friends.put("friendsList", serializeNestedUsers(user.getFriends().getFriendsList()));
        friends.put("friendRequests", serializeNestedUsers(user.getFriends().getFriendRequests()));
        friends.put("blockedFriends", serializeNestedUsers(user.getFriends().getBlockedFriends()));
        jsonUser.put("friends", friends);

        try (FileWriter writer = new FileWriter(DATABASE_FILE)) {
            writer.write(jsonUser.toJSONString());
        }
    }

    @SuppressWarnings("unchecked")
    private JSONArray serializeNestedUsers(ArrayList<User> users) {
        JSONArray jsonArray = new JSONArray();
        for (User user : users) {
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("userID", user.getUserID());
            jsonUser.put("username", user.getUsername());
            jsonUser.put("name", user.getName());
            jsonUser.put("email", user.getEmail());
            jsonUser.put("DOB", formatDate(user.getDOB()));
            jsonUser.put("status", user.getStatus());
            jsonUser.put("bio", user.getBio());
            jsonArray.add(jsonUser);
        }
        return jsonArray;
    }
}
