package org.example.demo;

import java.util.ArrayList;

public class FriendManagement {
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

    public void sendFriendRequest(User user) {
        if (!friendRequests.contains(user)) {
            friendRequests.add(user);
        }
    }

    public void acceptFriendRequest(User user) {
        if (friendRequests.contains(user)) {
            friendRequests.remove(user);
            friendsList.add(user);
        }
    }

    public void rejectFriendRequest(User user) {
        friendRequests.remove(user);
    }

    public void blockFriend(User user) {
        friendsList.remove(user);
        blockedFriends.add(user);
    }
}
