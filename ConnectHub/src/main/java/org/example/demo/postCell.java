package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    private Label Name; // Name
    private Label userName; // UserName
    private Label postDate;
    private Label groupName;// Date the post was created
    private VBox postContentBox; // Contains text and image content
    private TextFlow postText;   // Flow for displaying text content
    private ImageView postImage;

    public postCell() {
        // Initialize components
        profilePic = new ImageView();
        profilePic.setFitHeight(50);
        profilePic.setFitWidth(50);
        profilePic.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25); // Circular clipping for profile picture
        profilePic.setClip(clip);

        Name = new Label();
        Name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        userName = new Label();
        userName.setStyle("-fx-font-weight: bold;-fx-font-size: 12px; -fx-text-fill: gray;");

        postDate = new Label(); // Label to display the post date
        postDate.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        groupName = new Label();
        groupName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Create the HBox for user and date with profile picture
        userBox = new HBox(10, profilePic, Name, userName, postDate, groupName);
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

        // Clear the graphic and content if the cell is empty
        if (empty || post == null) {
            setGraphic(null);
            return;
        }

        // Clear existing content for proper reuse of the cell
        userBox.getChildren().clear();

        // Add static content
        userBox.getChildren().add(profilePic);
        userBox.getChildren().add(Name);
        userBox.getChildren().add(userName);
        userBox.getChildren().add(postDate);
        userBox.getChildren().add(groupName);

        // Update dynamic content
        if (post.getOwner().getPfpPath() != null && !post.getOwner().getPfpPath().isEmpty()) {
            profilePic.setImage(new Image(post.getOwner().getPfpPath()));
        }

        Name.setText(post.getOwner().getName());
        userName.setText("@" + post.getOwner().getUsername());
        postDate.setText(post.getDatePosted().toString());

        try {
            if (post.getGroupFromGroupID() != null) {
                groupName.setText("Posted in: " + post.getGroupFromGroupID().getGroupName());
            } else {
                groupName.setText("");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Clear the post content area
        postText.getChildren().clear();
        if (post.hasContent()) {
            Text textContent = new Text(post.getContent());
            textContent.setStyle("-fx-font-size: 20px;");
            postText.getChildren().add(textContent);
        }

        // Handle post image visibility
        if (post.hasImage()) {
            postImage.setImage(new Image(post.getImage()));
            postImage.setVisible(true);
        } else {
            postImage.setVisible(false);
        }

        // Add conditional buttons
        try {
            addButtons(userBox, post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Set the updated container as the graphic
        setGraphic(cellContainer);
    }
    public void addButtons(HBox userBox, Post post) throws Exception {
        User currentUser = Application.getCurrentUser();

        // Clear any existing buttons (to avoid duplication)
        userBox.getChildren().removeIf(node -> node instanceof Button);

        // Create the buttons
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        editButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px; -fx-min-height: 20px; -fx-background-color: #6135D2;");
        deleteButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px; -fx-min-height: 20px; -fx-background-color: #6135D2;");

        // Add buttons based on the condition
        if (post.getGroupFromGroupID()!=null && (post.getGroupFromGroupID().getCreator().equals(currentUser) || post.getGroupFromGroupID().isAdmin(currentUser))){
            userBox.getChildren().add(editButton);
            userBox.getChildren().add(deleteButton);
        }
        else if(post.getOwner().equals(currentUser)) {
            userBox.getChildren().add(editButton);
            userBox.getChildren().add(deleteButton);
        }
    }

}


