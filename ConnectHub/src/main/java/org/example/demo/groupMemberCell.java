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
import java.io.IOException;

public class groupMemberCell extends ListCell<User>{
    private HBox memberInfo;
    private ImageView pfp;
    private Label name;
    private Label status;
    private Group currentGroup;
    private static final GroupManager groupManager;
    static {
        try {
            groupManager = new GroupManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static final NotificationManager notificationManager = Application.getNotificationManager();
    public groupMemberCell(Group group){
        currentGroup = group;
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        pfp.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        status = new Label();
        HBox text = new HBox(5,name,status);
        text.setSpacing(10);
        memberInfo = new HBox(15,pfp,text);
        memberInfo.setPadding(new Insets(10));
    }
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) {
            setGraphic(null);
        } else {
            pfp.setImage(new Image(user.getPfpPath()));
            name.setText(user.getName());
            if(currentGroup.getCreator().equals(user)){
                status.setText("Creator");
            }
            else if(currentGroup.getHierarchy().getAdmins().contains(user)) {
                status.setText("Admin");
            }
            else{
                status.setText("Member");
            }
            try {
                addButtons(memberInfo,user);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            setGraphic(memberInfo);
        }
    }
    public void addButtons(HBox userBox, User user) throws Exception {
        User currentUser = Application.getCurrentUser();
        // Clear any existing buttons (to avoid duplication)
        userBox.getChildren().removeIf(node -> node instanceof Button);
        // Create the buttons
        Button promoteButton = new Button("Promote");
        Button demoteButton = new Button("Demote");
        Button removeButton = new Button("Remove");

        promoteButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px; -fx-min-height: 20px; -fx-background-color: #6135D2;");
        demoteButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px; -fx-min-height: 20px; -fx-background-color: #6135D2;");
        removeButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px; -fx-min-height: 20px; -fx-background-color: #6135D2;");

        // Add buttons based on the condition
        if (currentGroup.getCreator().equals(currentUser)){
            if(currentGroup.isAdmin(user)) {
                userBox.getChildren().add(demoteButton);
                userBox.getChildren().add(removeButton);
            }
            else if(currentGroup.getCreator().equals(user)) {}
            else{
                userBox.getChildren().add(promoteButton);
                userBox.getChildren().add(removeButton);
            }
        }
        else if(currentGroup.isAdmin(currentUser)) {
            if(currentGroup.getCreator().equals(user)) {}
            else if(currentGroup.isAdmin(user)) {}
            else{
                userBox.getChildren().add(removeButton);
            }
        }
        User chosen = getItem();
        promoteButton.setOnAction(event -> {
            try {
                handlePromoteButton(chosen);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } );

        demoteButton.setOnAction(event -> {
            try {
                handledemoteButton(chosen);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } );
        removeButton.setOnAction(event -> {
            try {
                handleRemoveButton(chosen);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } );

    }

    private void handleRemoveButton(User user) throws Exception {
        if(currentGroup.isAdmin(user)){currentGroup.getHierarchy().getAdmins().remove(user);}
        else if(currentGroup.isMember(user)){currentGroup.getHierarchy().getMembers().remove(user);}
        groupManager.writeGroup(currentGroup);
        Notification noti = new Notification();
        noti.setSender(user);
        noti.setType("Group Activity - " + currentGroup.getGroupID());
        noti.setMessage("You have been removed from " + currentGroup.getGroupName());
        noti.addReciever(user);
        notificationManager.addNotification(noti);
        JOptionPane.showMessageDialog(null,"Member Removed!");
    }

    private void handledemoteButton(User user)  throws Exception {
        currentGroup.getHierarchy().getMembers().add(user);
        currentGroup.getHierarchy().getAdmins().remove(user);
        groupManager.writeGroup(currentGroup);
        Notification noti = new Notification();
        noti.setSender(user);
        noti.setType("Group Activity - " + currentGroup.getGroupID());
        noti.setMessage("You have been demoted to member in " + currentGroup.getGroupName());
        noti.addReciever(user);
        notificationManager.addNotification(noti);
        JOptionPane.showMessageDialog(null,"Member Demoted!");
    }

    private void handlePromoteButton(User user) throws Exception {
        currentGroup.getHierarchy().getAdmins().add(user);
        currentGroup.getHierarchy().getMembers().remove(user);
        groupManager.writeGroup(currentGroup);
        Notification noti = new Notification();
        noti.setSender(user);
        noti.setType("Group Activity - " + currentGroup.getGroupID());
        noti.setMessage("You have been promoted to admin in " + currentGroup.getGroupName());
        noti.addReciever(user);
        notificationManager.addNotification(noti);
        JOptionPane.showMessageDialog(null,"Member Promoted!");
    }
}