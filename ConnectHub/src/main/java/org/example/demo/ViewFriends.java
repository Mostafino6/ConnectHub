package org.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ViewFriends extends User{
    @FXML
    private ListView<User> friendsListView;
    @FXML
    public void initialize() {
        User user =  new User();
        User friend = new User();
        friend.setName("Mostafa Abayazeed");
        friend.setStatus(true);
        friend.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\418082197_838209324966104_1493215685447874660_n.jpg");
        User friend2 = new User();
        friend2.setName("John Smith");
        friend2.setStatus(false);
        friend2.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\testPostjpg.jpg");
        user.getFriends().getFriendsList().add(friend);
        user.getFriends().getFriendsList().add(friend2);
        ObservableList<User> friendss = FXCollections.observableArrayList(
                user.getFriends().getFriendsList()
        );
        friendsListView.setItems(friendss);
        friendsListView.setCellFactory(listView -> new profileCell());
    }
}
