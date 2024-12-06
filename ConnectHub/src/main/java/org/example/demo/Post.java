package org.example.demo;

import javafx.scene.image.Image;

import java.time.LocalDate;

public class Post {
    private String postId;
    private User owner;
    private String content; // For text content
    private String image;   // For image content
    private LocalDate datePosted;

    // Constructor for text posts
    public Post(User owner, String content) {
        this.owner = owner;
        this.content = content;
        this.datePosted = LocalDate.now();
    }

    // Constructor for image posts
    public Post(User owner, Image imageContent) {
        this.owner = owner;
        this.image = imageContent.getUrl(); // Use getUrl to store the image URL
        this.datePosted = LocalDate.now();
    }

    // Getters and setters
    public User getOwner() {
        return owner;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }
}