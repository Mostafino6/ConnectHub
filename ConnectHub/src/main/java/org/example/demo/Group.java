package org.example.demo;

import javax.swing.*;
import java.util.ArrayList;

public class Group {
    private String groupID;
    private User creator;
    private String groupName;
    private String groupDescription;
    private String groupIcon;
    private  HierarchyManagement hierarchy;
    ArrayList<Post> posts;
    public Group() {
        hierarchy = new HierarchyManagement();
        posts = new ArrayList<>();
    }
    public String getGroupID() {
        return groupID;
    }
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupDescription() {
        return groupDescription;
    }
    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }
    public String getGroupIcon() {
        return groupIcon;
    }
    public void setGroupIcon(String groupIcon) {
        this.groupIcon = "file:///" + groupIcon;
    }
    public HierarchyManagement getHierarchy() {
        return hierarchy;
    }
    public void setHierarchy(HierarchyManagement hierarchy) {
        this.hierarchy = hierarchy;
    }
    public ArrayList<Post> getPosts() {
        return posts;
    }
    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }
    public boolean isMember(User user) {
        if (this.creator.equals(user) || hierarchy.getAdmins().contains(user) || hierarchy.getMembers().contains(user)) {
            return true;
        }
        return false;
    }
    public void addMember(User user) {
        if(!hierarchy.getMembers().contains(user)) {
            hierarchy.getMembers().add(user);
        }
        else JOptionPane.showMessageDialog(null, user.getUsername() + " is already member of this group");
    }
    public void removeMember(User user) {
        if(hierarchy.getMembers().contains(user)) {
            hierarchy.getMembers().remove(user);
        }
        else JOptionPane.showMessageDialog(null, user.getUsername() + " is not a member of this group");
    }
    public ArrayList<User> getAllMembers() {
        ArrayList<User> members = new ArrayList<>();
        members.add(creator);
        for(User user: hierarchy.getAdmins()){
            members.add(user);
        }
        for(User user : hierarchy.getMembers()) {
            members.add(user);
        }
        return members;
    }
    public void leaveGroup(User user) {
        if(creator.equals(user)) {
            setCreator(hierarchy.getAdmins().getFirst());
            hierarchy.getAdmins().remove(hierarchy.getAdmins().getFirst());
            hierarchy.getAdmins().remove(user);
            hierarchy.getMembers().remove(user);
        }
        else{
            hierarchy.getMembers().remove(user);
            hierarchy.getAdmins().remove(user);
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Check if both references point to the same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Check if the other object is null or of a different class
        }
        Group other = (Group) obj; // Cast the object to a User
        return groupID != null && groupID.equals(other.groupID); // Compare userID for equality
    }
}
