package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationWindow {

    @FXML
    private ScrollPane notificationContainer; // Reference to the ScrollPane in FXML

    @FXML
    private Button refreshButton; // Button to refresh notifications

    private final NotificationManager notificationManager = new NotificationManager();
    private final User currentUser = Application.getCurrentUser();
    private final FRcell fRcell = new FRcell();
    private static final GroupManager groupManager;
    static {
        try {
            groupManager = new GroupManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    MainApplication mainApp = new MainApplication();

    public void initialize() {
        loadNotifications();
        // Set refresh button action
        refreshButton.setOnAction(event -> loadNotifications());
    }

    private void loadNotifications() {
        // Clear existing content (VBox) inside the ScrollPane
        VBox contentBox = new VBox(10);
        notificationContainer.setContent(contentBox);

        try {
            ArrayList<Notification> notifications = notificationManager.getNotificationsForUser(currentUser);
            if (notifications != null && !notifications.isEmpty()) {
                Collections.reverse(notifications);
                for (Notification notification : notifications) {
                    HBox notificationBox = createNotificationBox(notification);
                    if (!notification.getRecievers().contains(currentUser) && notification.getReadBy().contains(currentUser)) {
                        notificationBox.setStyle("-fx-background-color: #e0e0e0;");
                    }
                    contentBox.getChildren().add(notificationBox);
                }
            } else {
                Label noNotificationsLabel = new Label("No new notifications.");
                contentBox.getChildren().add(noNotificationsLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error loading notifications.");
            contentBox.getChildren().add(errorLabel);
        }
    }

    private HBox createNotificationBox(Notification notification) {
        // Create circular image for the user (sender of the notification)
        ImageView pfpImageView = new ImageView(new Image(notification.getSender().getPfpPath()));
        pfpImageView.setFitHeight(50);
        pfpImageView.setFitWidth(50);
        Circle clip = new Circle(25, 25, 25);
        pfpImageView.setClip(clip);

        // Create the message Label
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        // Create Label for the notification type and timestamp
        Label infoLabel = new Label("[" + notification.getType() + "] " + notification.formatDateTime());
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Combine elements into a VBox
        VBox notificationContent = new VBox(10);
        notificationContent.getChildren().addAll(messageLabel, infoLabel);
        notificationContent.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray;");

        // Create the HBox to hold the profile picture and notification content
        HBox notificationBox = new HBox(15); // 15px spacing
        notificationBox.getChildren().addAll(pfpImageView, notificationContent);
        notificationBox.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray;");

        // Add click event to mark notification as read
        notificationBox.setOnMouseClicked(event -> {
            if(!notification.getReadBy().contains(currentUser)) {
                notification.getReadBy().add(currentUser);
            }
            notification.getRecievers().remove(currentUser);
            notificationBox.setStyle("-fx-background-color: #e0e0e0;");
            try {
                notificationManager.updateNotification(notification);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(notification.getType().equals("Friend Request")){
                mainApp.handleFR(Application.getPrimaryStage());
            }
            if(notification.getType().startsWith("Group Activity")){
                String groupID = notification.getType();
                groupID = groupID.substring(17);
                try {
                    Group group = groupManager.getGroupById(groupID);
                    Application.setCurrentGroup(group);
                    mainApp.handleViewButton(Application.getPrimaryStage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return notificationBox;
    }
}
