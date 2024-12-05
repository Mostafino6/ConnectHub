package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class PostControl {
    @FXML private VBox vbox;
    @FXML private HBox hbox;
    @FXML private Label label;
    @FXML private ListView<Post> postContainer; // The ListView from FXML
    @FXML private TextField textPost; // The text input for new post

    private ObservableList<Post> postList;

    public void initialize() {
        // Initialize the ObservableList and bind it to the ListView
        postList = FXCollections.observableArrayList();
        if (postContainer != null) {
            postContainer.setItems(postList);
        }

        // Set a custom ListCell renderer for posts
        postContainer.setCellFactory(listView -> new ListCell<Post>() {
            @Override
            protected void updateItem(Post post, boolean empty) {
                super.updateItem(post, empty);
                if (empty || post == null) {
                    setGraphic(null);
                } else {
                    setGraphic(post.getPostLayout()); // Use the layout from the Post class
                }
            }
        });
    }

    // Method to handle post creation
    @FXML
    private void addPost() {
        String content = textPost.getText().trim();
        if (!content.isEmpty()) {
            // Create a new text post with the content
            Post newPost = new Post(content);
            postList.add(newPost); // Add the new post to the list
            textPost.clear(); // Clear the text field after adding the post
        } else {
            // Show an alert if the post content is empty
            showAlert("Post Error", "Content cannot be empty.", AlertType.WARNING);
        }
    }

    // Method to handle image post (if you plan to allow image uploads)
    @FXML
    private void addImagePost() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Open the file chooser and get the selected file
        File file = fileChooser.showOpenDialog(vbox.getScene().getWindow());

        if (file != null) {
            // Create a new image post with the file
            Post newImagePost = new Post(file);
            postList.add(newImagePost); // Add the new image post to the list
        } else {
            // Optionally show a message for empty file selection
            showAlert("File Error", "No image selected.", AlertType.WARNING);
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
