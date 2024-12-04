package org.example.demo;

import java.util.ArrayList;

public class FriendManagement extends User{
    private ArrayList<User> friendsList;
    private ArrayList<User> friendRequests;
    private ArrayList<User> blockedFriends;
    public FriendManagement(){
        this.friendsList = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
        this.blockedFriends = new ArrayList<>();
    }
    public ArrayList<User> getFriendsList() {
        return friendsList;
    }
    public void setFriendsList(ArrayList<User> friends) {
        this.friendsList = friends;
    }
    public ArrayList<User> getFriendRequests() {
        return friendRequests;
    }
    public void setFriendRequests(ArrayList<User> friendRequests) {
        this.friendRequests = friendRequests;
    }
    public ArrayList<User> getBlockedFriends() {
        return blockedFriends;
    }
    public void setBlockedFriends(ArrayList<User> blockedFriends) {
        this.blockedFriends = blockedFriends;
    }
    public void sendFriendRequest(User user){
        if(!user.getFriends().getFriendRequests().contains(this)) {
            user.getFriends().getFriendRequests().add(this);
        }
        else return;
    }
    public void acceptFriendRequest(User user){
        if(this.getFriendRequests().contains(user)) {
            this.getFriendRequests().remove(user);
            this.getFriendsList().add(user);
        }
    }
    public void rejectFriendRequest(User user){
        if(this.getFriendRequests().contains(user)) {
            this.getFriendRequests().remove(user);
        }
    }
    public void blockFriend(User user){
        if(this.getFriendsList().contains(user)) this.getFriendsList().remove(user);
        this.getBlockedFriends().add(user);
    }
}
