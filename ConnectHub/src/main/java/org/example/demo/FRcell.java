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

public class FRcell extends ListCell<User> {
    private HBox userInfo;
    private ImageView pfp;
    private Label name;
    private Button acceptButton;
    private Button declineButton;
    public FRcell(){
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        pfp.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        acceptButton = new Button();
        acceptButton.setText("Accept");
        acceptButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");
        declineButton = new Button();
        declineButton.setText("Decline");
        declineButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");
        HBox text = new HBox(5,name,acceptButton,declineButton);
        text.setSpacing(10);
        userInfo = new HBox(15,pfp,text);
        userInfo.setPadding(new Insets(10));
        acceptButton.setOnAction(e -> {
            User user = getItem();
            if(user != null){
                handleAcceptButton(user);
            }
        });
        declineButton.setOnAction(e -> {
            User user = getItem();
            if(user != null){
                handleDeclineButton(user);
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
    public void handleAcceptButton(User user){
        User currentUser = Application.getCurrentUser();
        if(currentUser != null){
            try {
                currentUser.getFriends().getFriendsList().add(user);
                user.getFriends().getFriendsList().add(currentUser);
                currentUser.getFriends().getFriendRequests().remove(user);
                JOptionPane.showMessageDialog(null,"Friend Request Accepted");
                Application.getDatabaseManager().writeUser(user);
                Application.getDatabaseManager().writeUser(currentUser);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("user not found");
        }
    }
    public void handleDeclineButton(User user){
        User currentUser = Application.getCurrentUser();
        if(currentUser != null){
            try {
                currentUser.getFriends().getSuggestedFriends().add(user);
                user.getFriends().getSuggestedFriends().add(currentUser);
                currentUser.getFriends().getFriendRequests().remove(user);
                JOptionPane.showMessageDialog(null,"Friend Request Declined");
                Application.getDatabaseManager().writeUser(user);
                Application.getDatabaseManager().writeUser(currentUser);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("user not found");
        }
        }
}
