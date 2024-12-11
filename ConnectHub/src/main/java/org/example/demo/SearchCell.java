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
        AddFriend.setText("AddFriend");
        AddFriend.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");

        RemoveFriend = new Button();
        RemoveFriend.setText("RemoveFriend");
        RemoveFriend.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");


        block = new Button();
        block.setText("block");
        block.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");

        viewProfile = new Button();
        viewProfile.setText("viewProfile");
        viewProfile.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");

        removeFriend = new Button();
        removeFriend.setText("removeFriend");
        removeFriend.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");

       if (friend){
           HBox text = new HBox(5,name,viewProfile,removeFriend,block);
           text.setSpacing(10);
           userInfo = new HBox(15,pfp,text);
           userInfo.setPadding(new Insets(10));
           setGraphic(userInfo);
           removeFriend.setOnAction(e -> {
               if(searched != null){
                   handleRemove(searched);
               }
           });
           block.setOnAction(e -> {
               if(searched != null){
                   handleBlock(searched);
               }
           });

       }
       else
       {
           HBox text = new HBox(5,name,AddFriend,viewProfile,block);
           text.setSpacing(10);
           userInfo = new HBox(15,pfp,text);
           userInfo.setPadding(new Insets(10));
           setGraphic(userInfo);
           removeFriend.setOnAction(e -> {
               if(searched != null){
                   handleRemove(searched);
               }
           });
           block.setOnAction(e -> {
               if(searched != null){
                   handleBlock(searched);
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

    private void handleRemove(User searched){
        User currentUser = Application.getCurrentUser();
        if(currentUser != null){
            try {
                currentUser.getFriends().removeFriend(searched);
                JOptionPane.showMessageDialog(null,"Friend Removed");
                Application.getDatabaseManager().writeUser(currentUser);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("user not found");
        }

    }
    private void handleBlock(User searched){
        User currentUser = Application.getCurrentUser();
        if(currentUser != null){
            try {
                currentUser.getFriends().setBlockedFriends(searched);
                JOptionPane.showMessageDialog(null,"User Blocked");
                Application.getDatabaseManager().writeUser(currentUser);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("user not found");
        }

    }
    private void handleAdd(User searched){
        User currentUser = Application.getCurrentUser();
        currentUser.getFriends().getFriendsList().add(searched);
        JOptionPane.showMessageDialog(null,"Friend Added");
    }

}
