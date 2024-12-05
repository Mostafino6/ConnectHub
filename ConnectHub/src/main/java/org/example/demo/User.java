package org.example.demo;

import java.awt.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Map;

public class User {
    private String userID;
    private String username;
    private String name;
    private String email;
    private String password;
    private Date DOB;
    private boolean status;
    //pfp
    private Image profilePicture;
    //cover
    private String bio;
    private FriendManagement friends;
    //posts
    //constructor

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
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
    public boolean isStatus() {
        return status;
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
    public Image getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }
}
