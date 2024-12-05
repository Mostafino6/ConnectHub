//package org.example.demo;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.ListView;
//public class ViewFriends {
//    @FXML
//    private ListView<User> friendsListView;
//    @FXML
//    public void initialize() {
//        User friend = new User();
//        friend.setName("Mostafa Abayazeed");
//        friend.setStatus(false);
//        friend.setPfpPath("file:///C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\testPostjpg.jpg");
//        ObservableList<User> friends = FXCollections.observableArrayList(
//              friend
//        );
//        friendsListView.setItems(friends);
//        friendsListView.setCellFactory(listView -> new profileCell());
//    }
//}
