package org.example.demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DatabaseManager {
    private static final String DATABASE_FILE = "C:\\Users\\ADMIN\\OneDrive\\Documents\\New folder\\ConnectHub\\ConnectHub\\src\\main\\java\\org\\example\\demo\\users.json";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Parses a date string into a Date object
    public Date parseDate(String dateString) throws Exception {
        return dateFormat.parse(dateString);
    }

    // Formats a Date object into a string
    public String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public ArrayList<User> readUsers() throws Exception {
        File file = new File(DATABASE_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); // If the file does not exist, return an empty list
        }

        // Parse the JSON file
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));

        // Create a HashMap to store userID -> User mapping
        HashMap<String, User> userMap = new HashMap<>();
        ArrayList<User> userList = new ArrayList<>();

        // First pass: create User objects and store them in the map
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            // Extract main user information
            String userId = (String) jsonObject.get("userID");
            String username = (String) jsonObject.get("username");
            String name = (String) jsonObject.get("name");
            String email = (String) jsonObject.get("email");
            String password = (String) jsonObject.get("password");
            Date dob = parseDate((String) jsonObject.get("DOB"));
            boolean status = (boolean) jsonObject.get("status");
            String bio = (String) jsonObject.get("bio");
            String pfpPath = (String) jsonObject.get("profilePicture");
            String coverphotoPath = (String) jsonObject.get("coverPhoto");

            // Create the User object
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setDOB(dob);
            user.setUserID(userId);
            user.setPassword(password); // Store the password (likely hashed)
            user.setStatus(status);
            user.setBio(bio);
            user.setPfpPath(pfpPath);
            user.setCoverphotoPath(coverphotoPath);

            // Add user to the map and list
            userMap.put(userId, user);
            userList.add(user);
        }

        // Second pass: Populate friend-related lists
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            String userId = (String) jsonObject.get("userID");
            User user = userMap.get(userId);

            JSONObject friendsObject = (JSONObject) jsonObject.get("friends");

            JSONArray friendsList = (JSONArray) friendsObject.get("friendsList");
            user.getFriends().setFriendsList(getUserObjectsFromIDs(friendsList, userMap));

            JSONArray friendRequests = (JSONArray) friendsObject.get("friendRequests");
            user.getFriends().setFriendRequests(getUserObjectsFromIDs(friendRequests, userMap));

            JSONArray blockedFriends = (JSONArray) friendsObject.get("blockedFriends");
            user.getFriends().setBlockedFriends(getUserObjectsFromIDs(blockedFriends, userMap));
        }
        // Suggest friends for each user
        for (User mainUser : userList) {
            if (mainUser.getFriends().getSuggestedFriends() == null) {
                mainUser.getFriends().setSuggestedFriends(new ArrayList<>());
            }
            ArrayList<String> allFriendIDs = new ArrayList<>();
            ArrayList<String> allRequestIDs = new ArrayList<>();
            ArrayList<String> allBlockedIDs = new ArrayList<>();

            for (User friend : mainUser.getFriends().getFriendsList()) {
                allFriendIDs.add(friend.getUserID());}
            for (User request : mainUser.getFriends().getFriendRequests()) {
                allRequestIDs.add(request.getUserID());}
            for (User blocked : mainUser.getFriends().getBlockedFriends()) {
                allBlockedIDs.add(blocked.getUserID());}
            for (User otherUser : userList) {
                if (!mainUser.getUserID().equals(otherUser.getUserID()) &&
                        !allFriendIDs.contains(otherUser.getUserID()) &&
                        !allRequestIDs.contains(otherUser.getUserID()) &&
                        !allBlockedIDs.contains(otherUser.getUserID())) {
                    mainUser.getFriends().getSuggestedFriends().add(otherUser);
                }
            }
        }

        return userList;
    }

    private ArrayList<User> getUserObjectsFromIDs(JSONArray ids, HashMap<String, User> userMap) {
        ArrayList<User> users = new ArrayList<>();
        for (Object idObj : ids) {
            String userId = (String) idObj;
            User user = userMap.get(userId);
            if (user != null) {
                users.add(user); // Add the User object if it exists in the map
            }
        }
        return users;
    }
    public void writeUser(User user) throws Exception {
        ArrayList<User> userList = readUsers();
        if (userList == null) {
            userList = new ArrayList<>();
        }

        // If it's a new user, generate a unique userID
        if (user.getUserID() == null || user.getUserID().isEmpty()) {
            String newUserID = getNextUserID(userList);
            user.setUserID(newUserID);
        }

        // Update existing user or add a new user
        boolean userExists = false;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserID().equals(user.getUserID())) {
                userList.set(i, user);
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            userList.add(user);
        }

        // Prepare the JSON array
        JSONArray jsonArray = new JSONArray();
        for (User existingUser : userList) {
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("userID", existingUser.getUserID());
            jsonUser.put("username", existingUser.getUsername());
            jsonUser.put("name", existingUser.getName());
            jsonUser.put("email", existingUser.getEmail());
            jsonUser.put("password", existingUser.getPassword());
            jsonUser.put("DOB", formatDate(existingUser.getDOB()));

            // Handle status as a boolean value
            if ("Online".equals(existingUser.getStatus())) {
                jsonUser.put("status", true);
            } else {
                jsonUser.put("status", false);
            }
            String pfpPath = existingUser.getPfpPath();
            if (pfpPath != null && pfpPath.startsWith("file:///")) {
                pfpPath = pfpPath.substring(8);
            }
            if(pfpPath != null) {
                pfpPath = pfpPath.replace("\\", "\\\\");
            }
            String coverphotoPath = existingUser.getCoverphotoPath();
            if (coverphotoPath != null && coverphotoPath.startsWith("file:///")) {
                coverphotoPath = coverphotoPath.substring(8);
            }
            if(coverphotoPath != null) {
                coverphotoPath = coverphotoPath.replace("\\", "\\\\");
            }
            jsonUser.put("bio", existingUser.getBio());
            jsonUser.put("profilePicture", pfpPath);
            jsonUser.put("coverPhoto", coverphotoPath);

            // Friends information
            JSONObject friendsObject = new JSONObject();
            friendsObject.put("friendsList", getIDsFromUserObjects(existingUser.getFriends().getFriendsList()));
            friendsObject.put("friendRequests", getIDsFromUserObjects(existingUser.getFriends().getFriendRequests()));
            friendsObject.put("blockedFriends", getIDsFromUserObjects(existingUser.getFriends().getBlockedFriends()));
            jsonUser.put("friends", friendsObject);
            jsonArray.add(jsonUser);
        }

        // Write to the file with proper formatting
        try (FileWriter writer = new FileWriter(DATABASE_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonUser = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                // Write user details with new lines for readability
                writer.write("    \"userID\": \"" + jsonUser.get("userID") + "\",\n");
                writer.write("    \"username\": \"" + jsonUser.get("username") + "\",\n");
                writer.write("    \"name\": \"" + jsonUser.get("name") + "\",\n");
                writer.write("    \"email\": \"" + jsonUser.get("email") + "\",\n");
                writer.write("    \"password\": \"" + jsonUser.get("password") + "\",\n");
                writer.write("    \"DOB\": \"" + jsonUser.get("DOB") + "\",\n");
                writer.write("    \"status\": " + jsonUser.get("status") + ",\n");
                writer.write("    \"bio\": \"" + jsonUser.get("bio") + "\",\n");
                writer.write("    \"profilePicture\": \"" + jsonUser.get("profilePicture") + "\",\n");
                writer.write("    \"coverPhoto\": \"" + jsonUser.get("coverPhoto") + "\",\n");

                // Write friends object
                JSONObject friendsObject = (JSONObject) jsonUser.get("friends");
                writer.write("    \"friends\": {\n");
                writer.write("      \"friendsList\": " + friendsObject.get("friendsList").toString() + ",\n");
                writer.write("      \"friendRequests\": " + friendsObject.get("friendRequests").toString() + ",\n");
                writer.write("      \"blockedFriends\": " + friendsObject.get("blockedFriends").toString() + "\n");
                writer.write("    }\n");

                // Close the user object
                if (i < jsonArray.size() - 1) {
                    writer.write("  },\n");
                } else {
                    writer.write("  }\n");
                }
            }
            writer.write("]");
        }
    }


    // Generate the next available userID
    public String getNextUserID(ArrayList<User> userList) {
        int maxID = 0;
        for (User user : userList) {
            try {
                int userID = Integer.parseInt(user.getUserID());
                maxID = Math.max(maxID, userID);
            } catch (NumberFormatException e) {
                // Handle invalid userID format
                continue;
            }
        }
        return String.valueOf(maxID + 1);
    }
    private JSONArray getIDsFromUserObjects(ArrayList<User> users) {
        JSONArray ids = new JSONArray();
        for (User user : users) {
            // Ensure the user has a valid userID before adding it to the list
            if (user.getUserID() != null && !user.getUserID().isEmpty()) {
                ids.add(user.getUserID());
            }
        }
        return ids;
    }
}
