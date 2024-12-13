package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;

public class AllPosts {
    @FXML
    private ListView<Post> postListView;
    private static final PostManager postManager = new PostManager();
    @FXML
    public void initialize() {
        User currentUser = Application.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null!");
            return;
        }
        try {
            ArrayList<Post> posts = postManager.readPosts();
            ArrayList<Post> friendPosts = new ArrayList<>();
            for(Post post : posts) {
                if(currentUser.getUserID().equals(post.getOwner().getUserID()) || (post.getGroupFromGroupID()!=null && post.getGroupFromGroupID().isMember(currentUser))){
                    friendPosts.add(post);
                }
                for(User users : currentUser.getFriends().getFriendsList()){
                    if(users.getUserID().equals(post.getOwner().getUserID()) && !friendPosts.contains(post)){
                        if(post.getGroupFromGroupID()!=null && !post.getGroupFromGroupID().isMember(currentUser)){}
                        else {
                            friendPosts.add(post);
                        }
                    }
                }
            }
            Collections.reverse(friendPosts);
            ObservableList<Post> postsList = FXCollections.observableArrayList(friendPosts);
            postListView.setItems(postsList);
            postListView.setCellFactory(listView -> new postCell());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
