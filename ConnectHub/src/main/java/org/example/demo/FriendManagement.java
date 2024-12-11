package org.example.demo;

import java.util.ArrayList;

public class FriendManagement {
    private ArrayList<User> friendsList;
    private ArrayList<User> friendRequests;
    private ArrayList<User> blockedFriends;
    private ArrayList<User> suggestedFriends;


    public FriendManagement(){
        this.friendsList = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
        this.blockedFriends = new ArrayList<>();
        this.suggestedFriends = new ArrayList<>();
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
    public ArrayList<User> getSuggestedFriends(){
        return suggestedFriends;
    }
    public void setSuggestedFriends(ArrayList<User> suggestedFriends) {
        this.suggestedFriends = suggestedFriends;
    }
    public ArrayList<User> getBlockedFriends() {
        return blockedFriends;
    }
    public void setBlockedFriends(ArrayList<User> blockedFriends) {
        this.blockedFriends = blockedFriends;
    }
    public void setBlockedFriends(User blockedFriend) {
       blockedFriends.add(blockedFriend);
    }

    public boolean isFriend(User user) {
        return friendsList.contains(user);
    }

    public boolean hasPendingRequest(User user) {
        return friendRequests.contains(user);
    }

    public void removeFriend(User user) {
        friendsList.remove(user);
    }

    public boolean isBlocked(User user) {
        return blockedFriends.contains(user);
    }
}
