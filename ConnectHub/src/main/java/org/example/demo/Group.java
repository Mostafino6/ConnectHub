package org.example.demo;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class Group {
    private String groupID;
    private User creator;
    private String groupName;
    private String groupDescription;
    private String groupIcon;
    private HierarchyManagement hierarchy;
    private ArrayList<Post> posts;

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

    // Set the group icon path correctly, ensuring it is platform-independent
    public void setGroupIcon(String groupIcon) {
        // Normalize the path and make it URI safe
        this.groupIcon = new File(groupIcon).toURI().toString();
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
        return this.creator.equals(user) || hierarchy.getAdmins().contains(user) || hierarchy.getMembers().contains(user);
    }

    public boolean isAdmin(User user) {
        return hierarchy.getAdmins().contains(user);
    }

    public void addMember(User user) {
        if (!hierarchy.getMembers().contains(user)) {
            hierarchy.getMembers().add(user);
        } else {
            JOptionPane.showMessageDialog(null, user.getUsername() + " is already a member of this group");
        }
    }

    public void removeMember(User user) {
        if (hierarchy.getMembers().contains(user)) {
            hierarchy.getMembers().remove(user);
        } else {
            JOptionPane.showMessageDialog(null, user.getUsername() + " is not a member of this group");
        }
    }

    public ArrayList<User> getAllMembers() {
        ArrayList<User> members = new ArrayList<>();
        members.add(creator);
        members.addAll(hierarchy.getAdmins());
        members.addAll(hierarchy.getMembers());
        return members;
    }
    public ArrayList<User> getAllMembersOnlyNoAdmins() {
        ArrayList<User> members = new ArrayList<>();
        members.add(creator);
        members.addAll(hierarchy.getMembers());
        return members;
    }

    public void leaveGroup(User user) {
        if (creator.equals(user)) {
            setCreator(hierarchy.getAdmins().isEmpty() ? null : hierarchy.getAdmins().get(0));
            hierarchy.getAdmins().remove(user);
            hierarchy.getMembers().remove(user);
        } else {
            hierarchy.getMembers().remove(user);
            hierarchy.getAdmins().remove(user);
        }
    }
    public void promoteMember(User user){
        if(hierarchy.getMembers().contains(user)) {
            hierarchy.getMembers().remove(user);
            hierarchy.getAdmins().add(user);
        }
        else{
            JOptionPane.showMessageDialog(null, user.getUsername() + " is not a member of this group");
        }
    }
    public void demoteAdmin(User user){
        if(hierarchy.getAdmins().contains(user)) {
            hierarchy.getAdmins().remove(user);
            hierarchy.getMembers().add(user);
        }
        else{
            JOptionPane.showMessageDialog(null, user.getUsername() + " is not an admin of this group");
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
        Group other = (Group) obj; // Cast the object to a Group
        return groupID != null && groupID.equals(other.groupID); // Compare groupID for equality
    }
}
