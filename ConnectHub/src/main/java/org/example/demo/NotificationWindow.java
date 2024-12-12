package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;

public class NotificationWindow {
    @FXML
    private VBox notificationContainer; // Reference to the VBox in FXML


    private static final User currentUser = Application.getCurrentUser();

    public void initialize() {
        notificationContainer.getChildren().clear();
        // Get the notifications of the current user
        ArrayList<Notification> notifications = currentUser.getNotifications();
        System.out.println("Initializing NotificationWindow");
        // Check if there are notifications to display
        if (notifications != null && !notifications.isEmpty()) {
            for (Notification notification : notifications) {
                HBox notificationBox = createNotificationBox(notification
                ); // Spacing between elements
//                notificationBox.getChildren().add(createNotificationBox(notification));
                notificationContainer.getChildren().add(notificationBox);
            }
        } else {
            Label noNotificationsLabel = new Label("No new notifications.");
            notificationContainer.getChildren().add(noNotificationsLabel);
        }
    }

    private HBox createNotificationBox(Notification notification) {
        // Create a Label for the notification message
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        // Create a Label for the notification type and timestamp
        Label infoLabel = new Label("[" + notification.getType() + "] " + notification.getTimestamp());
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Combine the labels in a VBox
        HBox notificationBox = new HBox(5); // Spacing of 5px between elements
        notificationBox.getChildren().addAll(messageLabel, infoLabel);
        notificationBox.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray;");

        // Add click event to mark notification as read
        notificationBox.setOnMouseClicked(event -> {
            notification.setRead(true);
            System.out.println("Notification clicked: " + notification.getMessage());
            notificationBox.setStyle("-fx-background-color: #e0e0e0;"); // Change background to indicate read
        });

        return notificationBox;
    }
}
