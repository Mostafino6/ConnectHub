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

public class SearchCell extends ListCell<User> {
    private HBox userInfo;
    private ImageView pfp;
    private Label name;
    private Button AddFriend;
    private Button RemoveFriend;
    private Button block;
    private Button removeFriend;
    private Button viewProfile;
    private User searched;
    public SearchCell(boolean friend, User searched){
        this.searched=searched;
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        Image img=new Image(searched.getPfpPath());
        pfp.setImage(img);
        pfp.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        pfp.setClip(clip);
        name = new Label();
        name.setText(searched.getName());
        name.setStyle("-fx-font-weight: bold;");
        AddFriend = new Button();
        AddFriend.setText("Add");
        AddFriend.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 70px;fx-min-height: 20px; -fx-background-color: #6135D2;");

        RemoveFriend = new Button();
        RemoveFriend.setText("Remove");
        RemoveFriend.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 70px;fx-min-height: 20px; -fx-background-color: #6135D2;");


        block = new Button();
        block.setText("Block");
        block.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 70px;fx-min-height: 20px; -fx-background-color: #6135D2;");

        viewProfile = new Button();
        viewProfile.setText("view");
        viewProfile.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 70px;fx-min-height: 20px; -fx-background-color: #6135D2;");

        removeFriend = new Button();
        removeFriend.setText("Remove");
        removeFriend.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 70px;fx-min-height: 20px; -fx-background-color: #6135D2;");

       if (friend){
           HBox text = new HBox(5,viewProfile,removeFriend,block);
           text.setSpacing(10);
           userInfo = new HBox(15,pfp,text);
           userInfo.setPadding(new Insets(10));
           setGraphic(userInfo);
           removeFriend.setOnAction(e -> {
               if(searched != null){
                   handleRemoveButton(searched);
               }
           });
           block.setOnAction(e -> {
               if(searched != null){
                   handleBlockButton(searched);
               }
           });

       }
       else
       {
           HBox text = new HBox(5,AddFriend,viewProfile,block);
           text.setSpacing(10);
           userInfo = new HBox(15,pfp,text);
           userInfo.setPadding(new Insets(10));
           setGraphic(userInfo);
           block.setOnAction(e -> {
               if(searched != null){
                   handleBlockButton(searched);
               }
           });
           AddFriend.setOnAction(e -> {
               if(searched != null){
                   handleAdd(searched);
               }
           });
           viewProfile.setOnAction(e -> {
               if(searched != null){
                   JOptionPane.showMessageDialog(null,"Will be done tommorow");
               }
           });
       }
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
            user.getFriends().getBlockedFriends().add(current);

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
    private void handleAdd(User user){
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
