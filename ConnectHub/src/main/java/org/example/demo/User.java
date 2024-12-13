package org.example.demo;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userID;
    private String username;
    private String name;
    private String email;
    private String password;
    private Date DOB;
    private boolean status;
    private String pfpPath;
    private String coverphotoPath;
    private String bio;
    private FriendManagement friends;
    private ArrayList<Post> posts;
    private ArrayList<Story> stories; // New list to manage user stories
    private ArrayList<Notification> notifications;
    private ArrayList<Group> groups;

    // Default constructor
    public User() {
        this.friends = new FriendManagement();
        this.posts = new ArrayList<>();
        this.stories = new ArrayList<>(); // Initialize stories list
        this.notifications = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.pfpPath = "";
        this.coverphotoPath = "";
        this.bio = "";
    }

    // Getters and setters for user attributes
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getStatus() {
        return status ? "Online" : "Offline";
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public FriendManagement getFriends() {
        return friends;
    }

    public void setFriends(FriendManagement friends) {
        this.friends = friends;
    }

    public String getPfpPath() {
        return pfpPath;
    }

    public void setPfpPath(String pfpPath) {
        this.pfpPath = "file:///" + pfpPath;
    }

    public String getCoverphotoPath() {
        return coverphotoPath;
    }

    public void setCoverphotoPath(String coverphotoPath) {
        this.coverphotoPath = "file:///" + coverphotoPath;
    }

    // Methods for managing posts
    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    // Methods for managing stories
    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

    public void addStory(Story story) {
        stories.add(story);
    }
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public ArrayList<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public void markAllAsRead() {
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
    }
    public ArrayList<Group> getGroups() {
        return groups;
    }
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    public void addGroup(Group group){
        groups.add(group);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Check if both references point to the same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Check if the other object is null or of a different class
        }
        User other = (User) obj; // Cast the object to a User
        return userID != null && userID.equals(other.userID); // Compare userID for equality
    }
}