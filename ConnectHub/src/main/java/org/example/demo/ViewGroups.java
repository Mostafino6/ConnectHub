package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class ViewGroups {
    @FXML
    private ListView<Group> groupsListView;
    private static final GroupManager groupManager;
    static {
        try {
            groupManager = new GroupManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void initialize() throws Exception {
        ArrayList<Group> groups = groupManager.readGroups();
        User currentUser = Application.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null!");
            return;
        }
        for(Group group : groups) {
            if(group.isMember(currentUser)) {
                currentUser.addGroup(group);
            }
        }
        ObservableList<Group> groupsList = FXCollections.observableArrayList(currentUser.getGroups());
        groupsListView.setItems(groupsList);
        groupsListView.setCellFactory(listView -> new groupCell());
    }
}
