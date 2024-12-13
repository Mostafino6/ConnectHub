package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class JoinRequests {
    @FXML
    private ListView<User> requestsListView;
    @FXML
    public void initialize() {
        Group currentGroup = Application.getCurrentGroup();
        if (currentGroup == null) {
            System.out.println("Current Group is null!");
            return;
        }
        ObservableList<User> requestList = FXCollections.observableArrayList(currentGroup.getHierarchy().getRequests());
        requestsListView.setItems(requestList);
        requestsListView.setCellFactory(listView -> new requestCell(currentGroup));
    }
}