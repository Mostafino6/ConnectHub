package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

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
            postManager.readPosts();
            System.out.println(currentUser.getPosts());
            ObservableList<Post> postsList = FXCollections.observableArrayList(currentUser.getPosts());
            postListView.setItems(postsList);
            postListView.setCellFactory(listView -> new postCell());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}