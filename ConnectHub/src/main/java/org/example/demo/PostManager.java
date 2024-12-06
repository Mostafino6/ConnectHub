package org.example.demo;

import javafx.scene.image.Image;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostManager {
    private static final String POSTS_DATABASE_FILE = "C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\java\\org\\example\\demo\\posts.json";  // Adjust path as needed
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
    // Method to read all posts and return them as an ArrayList of Post objects
    public ArrayList<Post> readPosts() throws Exception {
        ArrayList<Post> postList = new ArrayList<>();

        File file = new File(POSTS_DATABASE_FILE);
        if (!file.exists()) {
            return postList;  // If the file doesn't exist, return an empty list
        }

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            // Extract post details
            String userID = (String) jsonObject.get("userID");
            String content = (String) jsonObject.get("content");
            String image = (String) jsonObject.get("image");
            String datePostedStr = (String) jsonObject.get("datePosted");
            LocalDate datePosted = LocalDate.parse(datePostedStr);
            User user = getUserByID(userID);

            if (user != null) {
                // Handle image path correctly
                Image postImage = null;
                if (image != null) {
                    if (image.startsWith("/")) {  // It's a classpath resource
                        postImage = new Image(getClass().getResource(image).toExternalForm());
                    } else {  // It's an absolute file path
                        postImage = new Image(new File(image).toURI().toString());
                    }
                }
                if (user != null) {
                    System.out.println("Found User: " + user.getUserID());  // Debugging line
                    Post post = new Post(user, content, postImage);
                    post.setDatePosted(datePosted);
                    postList.add(post);
                    user.addPost(post);  // Add post to the user's list

                    // Log the number of posts for this user
                    System.out.println("User " + user.getUserID() + " posts count: " + user.getPosts().size());
                }

            }
        }
        return postList;  // Return the list of posts
    }

    // Method to add a new post
    public void addPost(Post post) throws Exception {
        // Write the new post to the JSON file
        ArrayList<Post> postList = readPosts();
        postList.add(post);

        // Prepare JSON to write
        JSONArray jsonArray = new JSONArray();
        for (Post p : postList) {
            JSONObject jsonPost = new JSONObject();
            jsonPost.put("userID", p.getOwner().getUserID());
            jsonPost.put("content", p.getContent());
            jsonPost.put("image", p.getImage());
            jsonPost.put("datePosted", p.getDatePosted().toString());  // Store LocalDate as string
            jsonArray.add(jsonPost);
        }

        // Write to the file
        try (FileWriter writer = new FileWriter(POSTS_DATABASE_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonPost = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                writer.write("    \"userID\": \"" + jsonPost.get("userID") + "\",\n");
                writer.write("    \"content\": \"" + jsonPost.get("content") + "\",\n");
                writer.write("    \"image\": \"" + jsonPost.get("image") + "\",\n");
                writer.write("    \"datePosted\": \"" + jsonPost.get("datePosted") + "\"\n");

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
