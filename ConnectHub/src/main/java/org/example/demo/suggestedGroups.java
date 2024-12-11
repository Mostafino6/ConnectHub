package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class suggestedGroups{
    @FXML
    private ListView<Group> sGroupsListView;
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
        ArrayList<Group> suggestedGroups = new ArrayList<>();
        User currentUser = Application.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null!");
            return;
        }
        for(Group group : groups) {
            if(!group.isMember(currentUser)) {
                suggestedGroups.add(group);
            }
        }
        ObservableList<Group> groupsList = FXCollections.observableArrayList(suggestedGroups);
        sGroupsListView.setItems(groupsList);
        sGroupsListView.setCellFactory(listView -> new sGroupCell());
    }
}
