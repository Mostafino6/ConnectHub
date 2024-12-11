package org.example.demo;

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
        return "file:///" + groupIcon;
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
}
