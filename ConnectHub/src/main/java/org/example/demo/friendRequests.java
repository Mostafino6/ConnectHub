package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class friendRequests {
    @FXML
    private ListView<User> friendRequests;
    @FXML
    public void initialize() {
        User currentUser = Application.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null!");
            return;
        }
        ObservableList<User> friendsList = FXCollections.observableArrayList(currentUser.getFriends().getFriendRequests());
        friendRequests.setItems(friendsList);
        friendRequests.setCellFactory(listView -> new FRcell());
    }
}