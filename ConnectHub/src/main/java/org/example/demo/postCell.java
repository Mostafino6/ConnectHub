package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.shape.Circle;

public class postCell extends ListCell<Post> {
    private VBox cellContainer;  // Main container for the cell (VBox)
    private HBox userBox;        // HBox for the user, profile picture, and date posted
    private ImageView profilePic; // User profile picture
    private Label userName;      // User's name
    private Label postDate;      // Date the post was created
    private VBox postContentBox; // Contains text and image content
    private TextFlow postText;   // Flow for displaying text content
    private ImageView postImage; // For displaying image content

    public postCell() {
        // Initialize components
        profilePic = new ImageView();
        profilePic.setFitHeight(50);
        profilePic.setFitWidth(50);
        profilePic.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25); // Circular clipping for profile picture
        profilePic.setClip(clip);

        userName = new Label();
        userName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        postDate = new Label(); // Label to display the post date
        postDate.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        // Create the HBox for user and date with profile picture
        userBox = new HBox(10, profilePic, userName, postDate);
        userBox.setAlignment(Pos.CENTER_LEFT);

        postText = new TextFlow();
        postText.setPadding(new Insets(5, 0, 10, 0));

        postImage = new ImageView();
        postImage.setPreserveRatio(true);
        postImage.setFitWidth(620);  // Set max width for images
        postImage.setFitHeight(postImage.getFitHeight()); // Adjust height to prevent distortion

        postContentBox = new VBox(5, postText, postImage);
        postContentBox.setPadding(new Insets(5, 0, 0, 10));
        postContentBox.setAlignment(Pos.TOP_LEFT);

        // The VBox will contain the userBox (HBox) and postContentBox (VBox)
        cellContainer = new VBox(10, userBox, postContentBox);
        cellContainer.setPadding(new Insets(10));
        cellContainer.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-width: 0 0 1 0;"); // Styling for each cell
        cellContainer.setPrefWidth(550);
        cellContainer.setPrefHeight(30);
    }

    @Override
    protected void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);

        if (empty || post == null) {
            setGraphic(null);
        } else {
            // Update profile picture
            if (post.getOwner().getPfpPath() != null && !post.getOwner().getPfpPath().isEmpty()) {
                profilePic.setImage(new Image(post.getOwner().getPfpPath()));
            }
            userName.setText(post.getOwner().getName());

            // Update post date (assuming you have a LocalDate for the datePosted)
            postDate.setText(post.getDatePosted().toString());

            // Update post content (text or image)
            postText.getChildren().clear();
            if (post.hasContent()) {
                Text textContent = new Text(post.getContent());
                textContent.setStyle("-fx-font-size: 20px;");
                postText.getChildren().add(textContent);
            }

            // Check if the post has an image and update accordingly
            if (post.hasImage()) {
                postImage.setImage(new Image(post.getImage()));
                postImage.setVisible(true);
            } else {
                postImage.setVisible(false);
            }
            setGraphic(cellContainer);
        }
    }
}
