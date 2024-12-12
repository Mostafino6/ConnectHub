package org.example.demo;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;

public class Post {
    private User owner;
    private String content; // For text content
    private String image;   // For image content
    private LocalDate datePosted;
    private String groupID;

    public Post(User owner, String content, String image) {
        this.owner = owner;
        this.content = content;
        this.image = image;
        this.datePosted = LocalDate.now(); // Automatically set the date
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
        this.image = image;
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
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    public String getGroupID() {
        if(groupID == null || groupID.isEmpty()){
            return "";
        }
        return groupID;
    }
    public Group getGroupFromGroupID() throws Exception {
        ArrayList<Group> groups = MainApplication.getGroupManager().readGroups();
        for(Group group : groups){
            if(group.getGroupID().equals(groupID)){
                return group;
            }
        }
        return null;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if the objects are the same reference
        if (obj == null || getClass() != obj.getClass()) return false; // Ensure the same class type

        Post other = (Post) obj;

        // Compare all relevant fields
        return (owner != null ? owner.equals(other.owner) : other.owner == null) &&
                (content != null ? content.equals(other.content) : other.content == null) &&
                (image != null ? image.equals(other.image) : other.image == null) &&
                (datePosted != null ? datePosted.equals(other.datePosted) : other.datePosted == null) &&
                (groupID != null ? groupID.equals(other.groupID) : other.groupID == null);
    }
    @Override
    public int hashCode() {
        int result = (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (datePosted != null ? datePosted.hashCode() : 0);
        result = 31 * result + (groupID != null ? groupID.hashCode() : 0);
        return result;
    }
}