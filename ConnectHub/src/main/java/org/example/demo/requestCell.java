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

public class requestCell extends ListCell<User> {
    private HBox memberInfo;
    private ImageView pfp;
    private Label name;
    private Button acceptButton;
    private Button declineButton;
    private Group currentGroup;
    private static final User currentUser = Application.getCurrentUser();
    private static final NotificationManager notificationManager = Application.getNotificationManager();
    private static final GroupManager groupManager = Application.getGroupManager();

    public requestCell(Group group) {
        currentGroup = group;
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        pfp.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        acceptButton = new Button("Accept");
        declineButton = new Button("Decline");
        acceptButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px; -fx-min-height: 20px; -fx-background-color: #6135D2;");
        declineButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px; -fx-min-height: 20px; -fx-background-color: #6135D2;");
        HBox text = new HBox(5, name, acceptButton, declineButton);
        text.setSpacing(10);
        memberInfo = new HBox(15, pfp, text);
        memberInfo.setPadding(new Insets(10));
        acceptButton.setOnAction(e -> {
            try {
                handleAcceptButton(getItem());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        declineButton.setOnAction(e -> {
            currentGroup.getHierarchy().getRequests().remove(getItem());
            JOptionPane.showMessageDialog(null,"User Declined");
            try {
                groupManager.writeGroup(currentGroup);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
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
            setGraphic(memberInfo);
        }
    }

    public void handleAcceptButton(User user) throws Exception {
        currentGroup.addMember(user);
        currentGroup.getHierarchy().getRequests().remove(user);
        JOptionPane.showMessageDialog(null,"User Accepted");
        groupManager.writeGroup(currentGroup);
        Notification noti = new Notification();
        noti.setSender(user);
        noti.setType("Group Activity - " + currentGroup.getGroupID());
        noti.setMessage("You have joined " + currentGroup.getGroupName());
        noti.addReciever(user);
        notificationManager.addNotification(noti);
        noti.getRecievers().clear();
        noti.setMessage(user.getName() + " has joined " + currentGroup.getGroupName());
        for(User member : currentGroup.getAllMembers()){
            if(!user.equals(member)){
                noti.addReciever(member);
            }
        }
        notificationManager.addNotification(noti);
    }
}