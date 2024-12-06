package org.example.demo;

import javafx.scene.image.Image;

import java.time.LocalDate;

public class Post {
    private String postID;
    private User owner;
    private String content; // For text content
    private String image;   // For image content
    private LocalDate datePosted;

    public Post(User owner, String content, Image imageContent) {
        this.owner = owner;
        this.content = content;
        this.image = (imageContent != null) ? imageContent.getUrl() : null; // Set image path if an image is provided
        this.datePosted = LocalDate.now(); // Automatically set the date
    }
    public String getPostID() {
        return postID;
    }
    public void setPostID(String postID) {
        this.postID = postID;
    }
    // Getters and setters
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public String getUserID(){
        return owner.getUserID();
    }
    public void setUserID(String userID){
        this.owner.setUserID(userID);
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImage() {
        return  "file:///" + image;
    }
    public void setImage(String image) {
        this.image = "file:///" + image;
    }
    public LocalDate getDatePosted() {
        return datePosted;
    }
    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }

    // Helper to check if the post has text
    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted = datePosted;
    }
}