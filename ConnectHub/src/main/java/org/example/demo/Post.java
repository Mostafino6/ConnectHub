package org.example.demo;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class Post {
    private User owner;
    private String content;
    private File file;
    private LocalDate datePosted;
    private String imagePath;
    private HBox hBox;
    private ImageView imagePost;
    private ImageView pfp;

    // Constructor for content-based post
    public Post(String content) {
        this.content = content;
        this.datePosted = LocalDate.now();
        initializeTextPost();
    }

    // Constructor for image-based post
    public Post(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        this.file = file;
        this.datePosted = LocalDate.now();
        this.imagePath = file.getAbsolutePath();
        initializeImagePost();
    }
private void initializeTextPost()
{
    Label contentLabel = new Label(content);
    contentLabel.setWrapText(true);
    pfp = new ImageView(new Image("file:/C:/Users/eyada/OneDrive/Documents/GitHub/ConnectHub/ConnectHub/src/main/resources/org/example/demo/profile-icon.png"));
    pfp.setFitHeight(50);
    pfp.setFitWidth(50);
    pfp.setPreserveRatio(true);
    hBox = new HBox(5.0, pfp, contentLabel);

    hBox.setSpacing(10);
}
    private void initializeImagePost() {
        imagePost = new ImageView(new Image(file.toURI().toString()));
        imagePost.setFitWidth(200);
        imagePost.setPreserveRatio(true);

        // Profile picture initialization
        pfp = new ImageView(new Image("file:/C:/Users/eyada/OneDrive/Documents/GitHub/ConnectHub/ConnectHub/src/main/resources/org/example/demo/profile-icon.png"));
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);

        // Combine into HBox
        hBox = new HBox(5, pfp, imagePost);
    }

    // Getters and setters
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        if (file != null) {
            this.imagePath = file.getAbsolutePath();
            initializeImagePost();
        }
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted = datePosted;
    }

    public HBox getPostLayout() {
        return hBox;
    }

}
