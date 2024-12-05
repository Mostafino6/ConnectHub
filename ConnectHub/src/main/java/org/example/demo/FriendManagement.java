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

    public void sendFriendRequest(User sender, User receiver) {
        if (!receiver.getFriends().getFriendRequests().contains(sender) &&
                !receiver.getFriends().isBlocked(sender)) {
            receiver.getFriends().getFriendRequests().add(sender);
        }
    }

    public void acceptFriendRequest(User receiver, User sender) {
        if (receiver.getFriends().getFriendRequests().contains(sender)) {
            receiver.getFriends().getFriendRequests().remove(sender);
            receiver.getFriends().getFriendsList().add(sender);
            sender.getFriends().getFriendsList().add(receiver); // Symmetry
        }
    }

    public void rejectFriendRequest(User receiver, User sender) {
        receiver.getFriends().getFriendRequests().remove(sender);
    }

    public void blockFriend(User blocker, User blocked) {
        if (blocker.getFriends().getFriendsList().contains(blocked)) {
            blocker.getFriends().getFriendsList().remove(blocked);
        }
        if (!blocker.getFriends().getBlockedFriends().contains(blocked)) {
            blocker.getFriends().getBlockedFriends().add(blocked);
        }
    }

    public boolean isFriend(User user) {
        return friendsList.contains(user);
    }

    public boolean hasPendingRequest(User user) {
        return friendRequests.contains(user);
    }

    public boolean isBlocked(User user) {
        return blockedFriends.contains(user);
    }
}
