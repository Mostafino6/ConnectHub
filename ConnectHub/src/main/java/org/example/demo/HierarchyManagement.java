package org.example.demo;

import java.util.ArrayList;

public class HierarchyManagement {
    private ArrayList<User> admins;
    private ArrayList<User> members;
    private ArrayList<User> requests;
    public HierarchyManagement() {
        this.admins = new ArrayList<>();
        this.members = new ArrayList<>();
        this.requests = new ArrayList<>();
    }
    public ArrayList<User> getAdmins() {
        return admins;
    }
    public void setAdmins(ArrayList<User> admins) {
        this.admins = admins;
    }
    public void addAdmin(User user) {
        this.admins.add(user);
    }
    public void removeAdmin(User user) {
        this.admins.remove(user);
    }
    public ArrayList<User> getMembers() {
        return members;
    }
    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
    public void addMember(User user) {
        this.members.add(user);
    }
    public void removeMember(User user) {
        this.members.remove(user);
    }
    public ArrayList<User> getRequests() {
        return requests;
    }
    public void setRequests(ArrayList<User> requests) {
        this.requests = requests;
    }
    public void addRequest(User user) {
        this.requests.add(user);
    }
    public void removeRequest(User user) {
        this.requests.remove(user);
    }
}
