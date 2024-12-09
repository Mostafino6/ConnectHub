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
import java.util.Map;

public class StoryManager {
    private static final String STORIES_DATABASE_FILE = "C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\java\\org\\example\\demo\\stories.json";  // Adjust path as needed
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

    // Method to read all stories and return them as an ArrayList of Story objects
    public ArrayList<Story> readStories() throws Exception {
        ArrayList<Story> storyList = new ArrayList<>();

        File file = new File(STORIES_DATABASE_FILE);
        if (!file.exists()) {
            return storyList;  // If the file doesn't exist, return an empty list
        }

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            // Extract story details
            String userID = (String) jsonObject.get("userID");
            String caption = (String) jsonObject.get("caption");
            String image = (String) jsonObject.get("image");
            String dateCreatedStr = (String) jsonObject.get("datePosted");
            LocalDateTime dateCreated = LocalDateTime.parse(dateCreatedStr);
            User user = getUserByID(userID);

            if (user != null) {
                Story story = new Story(user, caption, image);
                story.setDateCreated(dateCreated);
                if(!(Duration.between(story.getDateCreated(), LocalDateTime.now()).toHours() >= 24)) {
                    if(!storyList.contains(story)) storyList.add(story);
                    if(!user.getStories().contains(story)) user.addStory(story);
                }// Add story to the user's list
            }
        }
        return storyList;  // Return the list of stories
    }

    // Method to add a new story
    public void addStory(Story story) throws Exception {
        // Write the new story to the JSON file
        ArrayList<Story> storyList = readStories();
        storyList.add(story);

        // Prepare JSON to write
        JSONArray jsonArray = new JSONArray();
        for (Story s : storyList) {
            JSONObject jsonStory = new JSONObject();
            jsonStory.put("userID", s.getOwner().getUserID());
            jsonStory.put("caption", s.getCaption());
            jsonStory.put("datePosted", s.getDateCreated().toString());  // Store LocalDateTime as string
            String storyImage = s.getImageUrl();
            if (storyImage != null && storyImage.startsWith("file:///")) {
                storyImage = storyImage.substring(8);
            }
            if (storyImage != null) {
                storyImage = storyImage.replace("\\", "\\\\");
            }
            jsonStory.put("image", storyImage);
            jsonArray.add(jsonStory);
        }

        // Write to the file
        try (FileWriter writer = new FileWriter(STORIES_DATABASE_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonStory = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                writer.write("    \"userID\": \"" + jsonStory.get("userID") + "\",\n");
                writer.write("    \"caption\": \"" + jsonStory.get("caption") + "\",\n");
                writer.write("    \"image\": \"" + jsonStory.get("image") + "\",\n");
                writer.write("    \"datePosted\": \"" + jsonStory.get("datePosted") + "\"\n");

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
