package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;

public class GroupPosts {
    @FXML
    private ListView<Post> groupPostListView;
    private static final PostManager postManager = new PostManager();
    @FXML
    public void initialize() {
        Group currentGroup = Application.getCurrentGroup();
        if(currentGroup == null){
            System.out.println("No group selected");
            return;
        }
        try {
            ArrayList<Post> posts = postManager.readPosts();
            ArrayList<Post> groupPosts = new ArrayList<>();
            for(Post post : posts) {
                if(currentGroup.equals(post.getGroupFromGroupID())) {
                    groupPosts.add(post);
                }
            }
            Collections.reverse(groupPosts);
            ObservableList<Post> postsList = FXCollections.observableArrayList(groupPosts);
            groupPostListView.setItems(postsList);
            groupPostListView.setCellFactory(listView -> new postCell());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}