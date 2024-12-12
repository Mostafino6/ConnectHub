package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ViewMembers {
    @FXML
    private ListView<User> membersListView;
    @FXML
    public void initialize() {
        Group currentGroup = Application.getCurrentGroup();
        if (currentGroup == null) {
            System.out.println("Current Group is null!");
            return;
        }
        ObservableList<User> membersList = FXCollections.observableArrayList(currentGroup.getAllMembers());
        membersListView.setItems(membersList);
        membersListView.setCellFactory(listView -> new groupMemberCell(currentGroup));
    }
}
