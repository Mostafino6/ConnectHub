package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;
import javafx.scene.shape.*;



import java.util.List;

public class NotificationWindow {

    @FXML
    private VBox notificationContainer; // Reference to the VBox in FXML

    @FXML
    private Button refreshButton; // Add a button in the FXML to refresh notifications

    private NotificationManager notificationManager = new NotificationManager();
    private User currentUser = Application.getCurrentUser();
    private FRcell fRcell = new FRcell();

    public void initialize() {
        loadNotifications();

        // Set refresh button action
        refreshButton.setOnAction(event -> loadNotifications());
    }

    private void loadNotifications() {
        notificationContainer.getChildren().clear();

        try {
            List<Notification> notifications = notificationManager.getNotificationsForUser(currentUser.getUserID());
            if (notifications != null && !notifications.isEmpty()) {
                for (Notification notification : notifications) {
                    HBox notificationBox = createNotificationBox(notification);
                    notificationContainer.getChildren().add(notificationBox);
                }
            } else {
                Label noNotificationsLabel = new Label("No new notifications.");
                notificationContainer.getChildren().add(noNotificationsLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error loading notifications.");
            notificationContainer.getChildren().add(errorLabel);
        }
    }

    private HBox createNotificationBox(Notification notification) {
        // Create circular image for the user (sender of the notification)
        ImageView pfpImageView = new ImageView(new Image(notification.getOwner().getPfpPath()));
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

        // Create the Accept/Decline buttons for Friend Request notifications
        HBox actionButtons = new HBox(10);
        Button acceptButton = new Button("Accept");
        Button declineButton = new Button("Decline");

        if (notification.getType().equals("Friend Request")) {
            // Add the buttons for Friend Request notifications
            acceptButton.setOnAction(e -> fRcell.handleAcceptButton(currentUser));
            declineButton.setOnAction(e -> fRcell.handleDeclineButton(currentUser));
            actionButtons.getChildren().addAll(acceptButton, declineButton);
        }

        // Combine elements into a VBox
        VBox notificationContent = new VBox(10);
        notificationContent.getChildren().addAll(messageLabel, infoLabel, actionButtons);
        notificationContent.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray;");

        // Create the HBox to hold the profile picture and notification content
        HBox notificationBox = new HBox(15); // 15px spacing
        notificationBox.getChildren().addAll(pfpImageView, notificationContent);
        notificationBox.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray;");

        // Add click event to mark notification as read
        notificationBox.setOnMouseClicked(event -> {
            notification.setRead(true);
            try {
                notificationManager.addNotification(notification);  // Update the notification list
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            notificationBox.setStyle("-fx-background-color: #e0e0e0;"); // Change background to indicate read
        });

        return notificationBox;
    }
}
