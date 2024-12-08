package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;

public class profilePosts {
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
            ArrayList<Post> allPosts = postManager.readPosts();
            ArrayList<Post> cuPosts = new ArrayList<>();
            for (Post post : allPosts) {
                if(post.getOwner().getUserID().equals(currentUser.getUserID())) {
                    cuPosts.add(post);
                }
            }
            Collections.reverse(cuPosts);
            ObservableList<Post> postsList = FXCollections.observableArrayList(cuPosts);
            postListView.setItems(postsList);
            postListView.setCellFactory(listView -> new postCell());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}