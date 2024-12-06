package org.example.demo;

import javafx.scene.image.Image;

import java.time.LocalDate;

public class Story {
    private String storyId; // Unique ID for the story
    private User owner;     // The user who owns the story
    private String caption; // Caption for the story
    private String imageUrl; // URL of the image content
    private LocalDate dateCreated; // Date the story was created

    // Constructor for text-only stories
    public Story(User owner, String caption) {
        this.owner = owner;
        this.caption = caption;
        this.dateCreated = LocalDate.now();
    }

    // Constructor for image stories
    public Story(User owner, String caption, Image imageContent) {
        this.owner = owner;
        this.caption = caption;
        this.imageUrl = imageContent.getUrl(); // Use getUrl to store the image URL
        this.dateCreated = LocalDate.now();
    }

    // Getters and setters
    public User getOwner() {
        return owner;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getStoryId() {
        return storyId;
    }
}
