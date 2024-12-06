package org.example.demo;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.shape.Circle;

import javax.swing.*;
import java.util.List;

public class SFcell extends ListCell<User> {
    private HBox userInfo;
    private ImageView pfp;
    private Label name;
    private Button addButton;
    public SFcell(){
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        pfp.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        addButton = new Button();
        addButton.setText("Add");
        addButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");
        HBox text = new HBox(5,name,addButton);
        text.setSpacing(10);
        userInfo = new HBox(15,pfp,text);
        userInfo.setPadding(new Insets(10));
        addButton.setOnAction(e -> {
           User user = getItem();
           if(user != null){
               handleAddButton(user);
           }
        });
    }
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if (empty || user == null) {
            setGraphic(null);
        } else {
            pfp.setImage(new Image(user.getPfpPath()));
            name.setText(user.getName());
            setGraphic(userInfo);
        }
    }
    private void handleAddButton(User user){
        User currentUser = Application.getCurrentUser();
        if(currentUser != null){
            try {
                user.getFriends().getFriendRequests().add(currentUser);
                currentUser.getFriends().getSuggestedFriends().remove(user);
                JOptionPane.showMessageDialog(null,"Friend Request Sent!");
                Application.getDatabaseManager().writeUser(user);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("user not found");
        }

    }
}
