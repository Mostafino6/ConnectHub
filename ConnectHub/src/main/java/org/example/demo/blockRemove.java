package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class blockRemove {
    @FXML
    private ListView<User> blockRemove;
    @FXML
    public void initialize() {
        User currentUser = Application.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null!");
            return;
        }
        ObservableList<User> friendsList = FXCollections.observableArrayList(currentUser.getFriends().getFriendsList());
        blockRemove.setItems(friendsList);
        blockRemove.setCellFactory(listView -> new BRcell());
    }
}