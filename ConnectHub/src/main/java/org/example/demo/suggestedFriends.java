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
        ArrayList<User> friends = new ArrayList<>();
        User friend = new User();
        friend.setName("John Pork");
        friend.setStatus(true);
        friend.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\artworks-D1z0mq71bQhEyABg-cL8hUA-t500x500.jpg");
        User friend2 = new User();
        friend2.setName("Chill Guy");
        friend2.setStatus(false);
        friend2.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\Screenshot 2024-12-05 123219.png");
        friends.add(friend);
        friends.add(friend2);
        ObservableList<User> friendss = FXCollections.observableArrayList(
                friends
        );
        suggestedFriends.setItems(friendss);
        suggestedFriends.setCellFactory(listView -> new SFcell());
    }
}
