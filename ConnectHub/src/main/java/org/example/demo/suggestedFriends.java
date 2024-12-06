package org.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.util.ArrayList;

public class suggestedFriends extends User{
    @FXML
    private ListView<User> suggestedFriends;
    @FXML
    public void initialize() {
        User currentUser = Application.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null!");
            return;
        }
        ObservableList<User> friendsList = FXCollections.observableArrayList(currentUser.getFriends().getSuggestedFriends());
        suggestedFriends.setItems(friendsList);
        suggestedFriends.setCellFactory(listView -> new SFcell());
    }
}
