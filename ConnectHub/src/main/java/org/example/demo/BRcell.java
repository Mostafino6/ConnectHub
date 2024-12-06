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

public class BRcell extends ListCell<User> {
    private HBox userInfo;
    private ImageView pfp;
    private Label name;
    private Button blockButton;
    private Button removeButton;
    public BRcell(){
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        pfp.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        blockButton = new Button();
        blockButton.setText("Block");
        blockButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");
        removeButton = new Button();
        removeButton.setText("Remove");
        removeButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");
        HBox text = new HBox(5,name,blockButton,removeButton);
        text.setSpacing(10);
        userInfo = new HBox(15,pfp,text);
        userInfo.setPadding(new Insets(10));
        blockButton.setOnAction(e -> {
           User user = getItem();
           if(user != null){
               handleBlockButton(user);
           }
        });
        removeButton.setOnAction(e -> {
            User user = getItem();
            if(user != null){
                handleRemoveButton(user);
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
    private void handleBlockButton(User user){
        User current = Application.getCurrentUser();
        if(current != null){
            JOptionPane.showMessageDialog(null,"User is Blocked!");
            current.getFriends().getBlockedFriends().add(user);
            current.getFriends().getFriendsList().remove(user);
            user.getFriends().getFriendsList().remove(current);
            try {
                Application.getDatabaseManager().writeUser(current);
                Application.getDatabaseManager().writeUser(user);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void handleRemoveButton(User user){
        User current = Application.getCurrentUser();
        if(current != null){
            JOptionPane.showMessageDialog(null,"User is Removed!");
            current.getFriends().getFriendsList().remove(user);
            user.getFriends().getFriendsList().remove(current);
            current.getFriends().getSuggestedFriends().add(user);
            user.getFriends().getSuggestedFriends().add(current);
            try{
                Application.getDatabaseManager().writeUser(current);
                Application.getDatabaseManager().writeUser(user);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
