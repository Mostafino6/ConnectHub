package org.example.demo;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Story {
    private User owner;     // The user who owns the story
    private String caption; // Caption for the story
    private String imageUrl; // URL of the image content
    private LocalDateTime dateCreated; // Date the story was created

    // Constructor for image stories
    public Story(User owner, String caption, String imageUrl) {
        this.owner = owner;
        this.caption = caption;
        this.imageUrl = imageUrl; // Use getUrl to store the image URL
        this.dateCreated = LocalDateTime.now();
    }

    // Getters and setters
    public User getOwner() {
        return owner;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return "file:///" + imageUrl;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String formatDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm     dd/MM/yyyy");
        return this.getDateCreated().format(formatter);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Story story = (Story) obj;

        // Compare fields that uniquely identify the story
        return owner.getUserID().equals(story.owner.getUserID()) &&
                caption.equals(story.caption) &&
                imageUrl.equals(story.imageUrl) &&
                dateCreated.truncatedTo(ChronoUnit.SECONDS).equals(story.dateCreated.truncatedTo(ChronoUnit.SECONDS));
    }

}
