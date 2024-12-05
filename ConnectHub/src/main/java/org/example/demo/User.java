package org.example.demo;

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
    private String pfpPath;
    private String coverphotoPath;
    private String bio;
    private FriendManagement friends;
    private ArrayList<Post> posts;
    public User(){
        this.friends = new FriendManagement();
    }
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

    public String getStatus() {
        if(status) return "Online";
        else return "Offline";
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
        this.pfpPath = pfpPath;
    }

    public String getCoverphotoPath() {
        return coverphotoPath;
    }

    public void setCoverphotoPath(String coverphotoPath) {
        this.coverphotoPath = coverphotoPath;
    }

    public void sendFriendRequest(User user) {
        friends.sendFriendRequest(this, user);
    }

    public void acceptFriendRequest(User user) {
        friends.acceptFriendRequest(this, user);
    }

    public void rejectFriendRequest(User user) {
        friends.rejectFriendRequest(this, user);
    }

    public void blockFriend(User user) {
        friends.blockFriend(this, user);
    }
}
