package org.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.Duration;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Stories {
    @FXML
    private HBox storyHBox; // Reference to the HBox in FXML
    private static final StoryManager storyManager = new StoryManager();
    private static final DatabaseManager databaseManager = new DatabaseManager();
    private static final User currentUser = Application.getCurrentUser();
    private static int clickCount = 0;

    public void initialize() throws Exception {
        ArrayList<Story> stories = storyManager.readStories();
        ArrayList<String> postedUserIDs = new ArrayList<>();  // List to track users who have posted a story
        ArrayList<User> users = databaseManager.readUsers();
        try {
            System.out.println(stories.size());
            ArrayList<Story> friendStories = new ArrayList<>();
            for (Story story : stories) {
                if (currentUser.getUserID().equals(story.getOwner().getUserID())) {
                    friendStories.add(story);
                }
                for (User user : currentUser.getFriends().getFriendsList()) {
                    if (user.getUserID().equals(story.getOwner().getUserID())) {
                        friendStories.add(story);
                    }
                }
            }
            // Populate the HBox with stories, avoiding repetition
            System.out.println(friendStories.size());
            for (Story story : friendStories) {
                String userID = story.getOwner().getUserID();
                // Check if the user has already posted a story
                if (!postedUserIDs.contains(userID)) {
                    VBox storyBox = createStory(story);
                    storyHBox.getChildren().add(storyBox);
                    postedUserIDs.add(userID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createStory(Story story) {
        // Create an ImageView
        ImageView imageView = new ImageView(new Image(story.getOwner().getPfpPath()));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        // Clip the image into a circular shape
        Circle clip = new Circle(25, 25, 25); // Center (x=25, y=25), radius=25
        imageView.setClip(clip);

        // Optional: Add a circular border
        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setStyle("-fx-border-color: purple; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;");

        // Add the username text
        Text userName = new Text(story.getOwner().getUsername());

        // Combine the image and username in a VBox
        VBox storyBox = new VBox(5); // Spacing of 5px between image and text
        storyBox.getChildren().addAll(imageContainer, userName);
        storyBox.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: transparent;");

        storyBox.setOnMouseClicked(event -> {
            clickCount++;
            User poster = story.getOwner();
            System.out.println("Story clicked: " + poster.getUsername());
            showStoryWindow(story, story.getOwner().getStories(), 0);
            if(clickCount>0){
            imageContainer.setStyle("-fx-border-color: grey; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;");}
        });
        return storyBox;
    }

    private void showStoryWindow(Story story, ArrayList<Story> stories, int currentIndex) {
        Stage storyStage = new Stage();
        storyStage.setTitle("Story of " + story.getOwner().getUsername());

        Image image = new Image(story.getImageUrl());
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(true);  // Maintain aspect ratio

        // Create a VBox to hold the ImageView, caption, and button
        VBox storyContent = new VBox();
        storyContent.setSpacing(10);  // Set some space between image and caption

        // Add the image and caption to the VBox
        HBox text = new HBox(5);
        Text caption = new Text(story.getCaption());
        caption.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Text datePosted = new Text(story.formatDateTime());
        datePosted.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
        Text countdownLabel = new Text();
        countdownLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray; -fx-font-style: italic;");
        updateCountdown(story,countdownLabel);
        text.getChildren().addAll(caption, datePosted, countdownLabel);
        storyContent.getChildren().addAll(imageView, text);

        // Create the "Next Story" button
        Button prevStoryButton = new Button("Previous Story");
        prevStoryButton.setOnAction(e -> {
            int previousIndex = currentIndex - 1;
            if (previousIndex >= 0) {
                // Show the previous story
                showStoryWindow(stories.get(previousIndex), stories, previousIndex);
                storyStage.close();  // Close the current window
            } else {
                // No more previous stories, close the window
                storyStage.close();
            }
        });
        prevStoryButton.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 150px;fx-min-height: 100px; -fx-background-color: #6135D2;");
        Button nextStoryButton = new Button("Next Story");
        nextStoryButton.setOnAction(e -> {
            int nextIndex = currentIndex + 1;
            if (nextIndex < stories.size()) {
                // Show the next story
                showStoryWindow(stories.get(nextIndex), stories, nextIndex);
                storyStage.close();  // Close the current window
            } else {
                // No more stories, close the window
                storyStage.close();
            }
        });
        nextStoryButton.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 150px;fx-min-height: 100px; -fx-background-color: #6135D2;");
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(prevStoryButton, nextStoryButton);
        storyContent.getChildren().add(buttons);

        // Set the scene and size of the Stage
        Scene storyScene = new Scene(storyContent, imageWidth, imageHeight + 100);  // Add space for caption and button
        storyStage.setScene(storyScene);

        // Show the new window
        storyStage.show();
    }
    private void updateCountdown(Story story, Text countdownLabel) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = story.getDateCreated().plusHours(24); // 24 hours from the story creation time

        // ScheduledExecutorService to update the countdown every second
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            // Calculate the remaining time every second
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(currentTime, targetTime);

            if (duration.isNegative()) {
                countdownLabel.setText("Time's up!");
                scheduler.shutdown();  // Stop the scheduler once the time is up
            } else {
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                long seconds = duration.getSeconds() % 60;

                countdownLabel.setText(String.format("Time remaining: %02d:%02d:%02d", hours, minutes, seconds));
            }
        }, 0, 1, TimeUnit.SECONDS);  // Start immediately, update every second
    }
}
