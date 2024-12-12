package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

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
    private Stage stage;
    private boolean blocked = false;
    public SearchCell(boolean friend, User searched,Stage stage) {
        this.searched=searched;
        this.stage=stage;
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
        User current = Application.getCurrentUser();
        boolean isFriend=false;
        for (int i = 0; i < current.getFriends().getFriendsList().size(); i++) {
            if (current.getFriends().getFriendsList().get(i).getUsername().equals(searched.getUsername()))
            {
                isFriend=true;
                break;
            }

        }
       if (isFriend){
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
           viewProfile.setOnAction(e -> {
               if(searched != null){
                                      handleProfile(searched);

               }
           });

       }
       if (!isFriend)
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
                   handleProfile(searched);
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




    private void handleRemoveButton(User user) {
        User current = Application.getCurrentUser();

        if (current == null) {
            JOptionPane.showMessageDialog(null, "No current user is logged in!");
            return;
        }

        boolean userFound = false;

        // Loop through the current user's friend list
        for (int i = 0; i < current.getFriends().getFriendsList().size(); i++) {
            if (current.getFriends().getFriendsList().get(i).getUsername().equals(user.getUsername())) {
                userFound = true;

                JOptionPane.showMessageDialog(null,current.getFriends().getFriendsList().get(i).getUserID());
                // Notify user of removal
                JOptionPane.showMessageDialog(null, "User is Removed!");

                // Remove from friend lists
                current.getFriends().removeFriend(searched);
                current.getFriends().getFriendsList().remove(searched);

                current.getFriends().getFriendsList().remove(user);
                user.getFriends().getFriendsList().remove(current);

                // Add to suggested friends lists
                current.getFriends().getSuggestedFriends().add(user);
                user.getFriends().getSuggestedFriends().add(current);

                break;
            }
        }

        if (!userFound) {
            JOptionPane.showMessageDialog(null, "User is not in your friend list!");
            return;
        }

        // Write updated data to the database
        try {
            Application.getDatabaseManager().writeUser(current);
            Application.getDatabaseManager().writeUser(user);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while updating user data.");
            e.printStackTrace(); // Replace with proper logging
        }
    }

    private void handleAdd(User user){
        User currentUser = Application.getCurrentUser();
        if(currentUser != null){
            try {
                user.getFriends().getFriendRequests().add(currentUser);
                currentUser.getFriends().getSuggestedFriends().remove(currentUser);
                currentUser.getFriends().getSuggestedFriends().remove(user);
                JOptionPane.showMessageDialog(null,"Friend Request Sent!");
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
    private void handleProfile(User searched) {
        try {
            // Load FXML and create a new stage
            Stage st2 = new Stage();
            FXMLLoader profileLoader = new FXMLLoader(Application.class.getResource("viewProfile.fxml"));
            Scene profileScene = new Scene(profileLoader.load(), 800, 600);

            // Set the scene and apply styles
            st2.setTitle("Profile");
            profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            st2.setScene(profileScene);


            Label nameLabel = (Label) profileLoader.getNamespace().get("nameuser");
            if (nameLabel != null) {
                String username = searched.getName();
                System.out.println(username);
                nameLabel.setText(username);
            } else {
                System.out.println("nameLabel is null");
            }
            ImageView pfpView=(ImageView) profileLoader.getNamespace().get("pfpView");
//            ImageView pfpView = (ImageView) st2.getScene().lookup("pfpView");
            if (pfpView != null) {
                if (searched.getPfpPath() !=null) {
                    String pfpPath = searched.getPfpPath();
                    System.out.println(pfpPath);
                    Circle clip = new Circle();
                    clip.setRadius(pfpView.getFitWidth() / 2);
                    clip.setCenterX(pfpView.getFitWidth() / 2);
                    clip.setCenterY(pfpView.getFitHeight() / 2);
                    pfpView.setClip(clip);
                    pfpView.setImage(new Image(searched.getPfpPath()));
                }
            }
            ImageView coverPhotoView = (ImageView) profileLoader.getNamespace().get("coverPhoto");
            if (coverPhotoView != null) {
                if (!searched.getCoverphotoPath().isEmpty()) {
                    coverPhotoView.setImage(new Image(searched.getCoverphotoPath()));
                }
            }
            Label bioLabel = (Label) profileLoader.getNamespace().get("bioplace");
            if (bioLabel != null) {
                if (!searched.getBio().isEmpty()) {
                    bioLabel.setText(searched.getBio());
                }
            }
            VBox postContainer = (VBox) profileLoader.getNamespace().get("postContainer");
            Button addFriend = (Button) profileLoader.getNamespace().get("addFriend");
            Button block = (Button) profileLoader.getNamespace().get("block");
            addFriend.setOnAction(e -> {
             handleAdd(searched);

            });

            block.setOnAction(e -> {
            handleBlockButton(searched);
            st2.close();
            });
                                st2.show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    



}
