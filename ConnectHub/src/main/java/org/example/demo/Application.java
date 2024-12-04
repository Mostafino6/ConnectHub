package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


import javafx.stage.FileChooser;

import java.io.File;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader home = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(home.load(), 950, 580);
        stage.setTitle("Home");
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        Button loginButton = (Button) home.getNamespace().get("loginButton");
        loginButton.setOnAction(event -> handleLoginClick(stage));
        Button SignUpButton = (Button) home.getNamespace().get("SignUpButton");
        SignUpButton.setOnAction(event -> handleSignClick(stage));

    }
    private void handleLoginClick(Stage stage) {
        try {
            FXMLLoader loginLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
            Scene loginScene = new Scene(loginLoader.load(), 950, 580);
            stage.setTitle("Log in");
            loginScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            stage.setScene(loginScene);
            Button loginDone = (Button) loginLoader.getNamespace().get("loginDone");
            loginDone.setOnAction(event ->handleHome(stage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleSignClick(Stage stage) {
        try {
            FXMLLoader signLoader = new FXMLLoader(Application.class.getResource("signUp.fxml"));
            Scene signUpScene = new Scene(signLoader.load(), 950, 580);
            stage.setTitle("Sign Up");
            signUpScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            stage.setScene(signUpScene);
            Button signupDone = (Button) signLoader.getNamespace().get("SignUpDone");
            signupDone.setOnAction(event ->handleProfile(stage));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleProfile(Stage stage)
    {
        try {
            FXMLLoader profileLoader = new FXMLLoader(Application.class.getResource("profile.fxml"));
            Scene profileScene = new Scene(profileLoader.load(), 950, 580);
            stage.setTitle("Profile");
            profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            stage.setScene(profileScene);
            Button manageFriends = (Button) profileLoader.getNamespace().get("manageFriends");
            Button addPost=(Button) profileLoader.getNamespace().get("addPost");
            VBox postContainer=(VBox) profileLoader.getNamespace().get("postContainer");
            addPost.setOnAction(event ->handleAddPost(stage,postContainer));
            manageFriends.setOnAction(event->friendsManager(stage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleHome(Stage stage)
    {
        try {
            FXMLLoader homeLoader = new FXMLLoader(Application.class.getResource("homePage.fxml"));
            Scene homeLoaderScene = new Scene(homeLoader.load(), 950, 580);
            stage.setTitle("Profile");
            homeLoaderScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            VBox postContainer=(VBox) homeLoader.getNamespace().get("postContainer");

            Button addPost=(Button) homeLoader.getNamespace().get("addPost");
            addPost.setOnAction(event ->handleAddPost(stage,postContainer));
            stage.setScene(homeLoaderScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void friendsManager(Stage stage) {
        try {
            FXMLLoader friendsLoader = new FXMLLoader(Application.class.getResource("FriendManager.fxml"));
            Scene friendsScene = new Scene(friendsLoader.load(), 321, 317);
            Stage newStage = new Stage();
            newStage.setTitle("Friends");
            friendsScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(friendsScene);
            newStage.initOwner(stage);
            newStage.show();
            Button viewFriends = (Button) friendsLoader.getNamespace().get("viewFriends");
            viewFriends.setOnAction(event -> viewFriendsList(stage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void viewFriendsList(Stage stage) {
        try {
            FXMLLoader friendsLoader = new FXMLLoader(Application.class.getResource("viewFriends.fxml"));
            Scene friendsListScene = new Scene(friendsLoader.load(), 528, 329);
            Stage newStage = new Stage();
            newStage.setTitle("Friends");
            friendsListScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(friendsListScene);
            newStage.initOwner(stage);
            newStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void handleAddPost(Stage stage,VBox postContainer)
    {
        try {
            FXMLLoader addPostLoader = new FXMLLoader(Application.class.getResource("addPost.fxml"));
            Scene addPostScene = new Scene(addPostLoader.load(), 650, 420);
            stage.setTitle("Add Post");
            addPostScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            stage.setScene(addPostScene);
            Button postDone=(Button) addPostLoader.getNamespace().get("postDone");
            Button cancelPost=(Button) addPostLoader.getNamespace().get("cancelPost");
            Button imageChooser=(Button) addPostLoader.getNamespace().get("imageChooser");
            TextField textPost=(TextField) addPostLoader.getNamespace().get("textPost");
            int[] textPostFlag = {0};
            int[] imgPostFlag = {0};

            postDone.setOnAction(event -> {
                if (!textPost.getText().equals("")) {
                    String textPostContent = textPost.getText();
                    Post post = new Post(textPostContent);
                    addTextPost(textPostContent,postContainer);
                    System.out.println(textPostContent);
                    textPostFlag[0] = 1;
                    imageChooser.setDisable(true);

                }

            });
            imageChooser.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );

                File file = fileChooser.showOpenDialog(imageChooser.getScene().getWindow());

                if (file != null) {
                    Image image = new Image(file.toURI().toString());
                    addImagePost(file,postContainer);
                }
            });




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTextPost(String content,VBox postContainer) {
        HBox textPostBox = new HBox();
        textPostBox.getStyleClass().add("textPost");
        ImageView profileImage = new ImageView(new Image(getClass().getResource("/org/example/demo/profile-icon.png").toExternalForm()));
        profileImage.setFitHeight(50);
        profileImage.setFitWidth(50);
        profileImage.setPreserveRatio(true);
        profileImage.getStyleClass().add("pp");
        Label postContent = new Label(content);
        postContent.getStyleClass().add("textPostContent");
        textPostBox.getChildren().addAll(profileImage, postContent);
        postContainer.getChildren().add(textPostBox);
    }
    private void addImagePost(File imageFile,VBox postContainer) {
        HBox imagePostBox = new HBox();
        imagePostBox.getStyleClass().add("imagePost");
        ImageView profileImage = new ImageView(new Image(getClass().getResource("/org/example/demo/profile-icon.png").toExternalForm()));
        profileImage.setFitHeight(50);
        profileImage.setFitWidth(50);
        profileImage.setPreserveRatio(true);
        profileImage.getStyleClass().add("pp");
        ImageView postImage = new ImageView(new Image(imageFile.toURI().toString()));
        postImage.setFitHeight(249);
        postImage.setFitWidth(311);
        postImage.setPreserveRatio(true);
        postImage.getStyleClass().add("postImage");

        imagePostBox.getChildren().addAll(profileImage, postImage);

        postContainer.getChildren().add(imagePostBox);
    }
    public static void main(String[] args) {
        launch();
    }
}