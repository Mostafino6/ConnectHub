package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ViewFriends {
    @FXML
    private ListView<User> friendsListView;
    @FXML
    public void initialize() {
        User currentUser = Application.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null!");
            return;
        }
        ObservableList<User> friendsList = FXCollections.observableArrayList(currentUser.getFriends().getFriendsList());
        friendsListView.setItems(friendsList);
        friendsListView.setCellFactory(listView -> new profileCell());
    }
}