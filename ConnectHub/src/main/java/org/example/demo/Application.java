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
    private static User currentUser;
    public Application() {
        currentUser = new User();
        User friend = new User();
        friend.setName("Mostafa Abayazeed");
        friend.setStatus(true);
        friend.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\418082197_838209324966104_1493215685447874660_n.jpg");
        User friend2 = new User();
        friend2.setName("John Smith");
        friend2.setStatus(false);
        friend2.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\testPostjpg.jpg");
        currentUser.getFriends().getFriendsList().add(friend);
        currentUser.getFriends().getFriendsList().add(friend2);
        User friend3 = new User();
        friend3.setName("John Pork");
        friend3.setStatus(true);
        friend3.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\artworks-D1z0mq71bQhEyABg-cL8hUA-t500x500.jpg");
        User friend4 = new User();
        friend4.setName("Chill Guy");
        friend4.setStatus(false);
        friend4.setPfpPath("C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\resources\\org\\example\\demo\\Screenshot 2024-12-05 123219.png");
        currentUser.getFriends().getFriendRequests().add(friend3);
        currentUser.getFriends().getFriendRequests().add(friend4);
    }
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
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
            Scene profileScene = new Scene(profileLoader.load(), 995,800);
            stage.setTitle("Profile");
            profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            stage.setScene(profileScene);
            Button addPost=(Button) profileLoader.getNamespace().get("addPost");
            VBox postContainer=(VBox) profileLoader.getNamespace().get("postContainer");
            addPost.setOnAction(event ->handleAddPost(stage,postContainer));
            Button manageFriends = (Button) profileLoader.getNamespace().get("manageFriends");
            manageFriends.setOnAction(event -> friendsManager(stage));
            Button viewSuggested = (Button) profileLoader.getNamespace().get("viewSuggested");
            viewSuggested.setOnAction(event -> handleSuggested(stage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleHome(Stage stage)
    {
        try {
            FXMLLoader homeLoader = new FXMLLoader(Application.class.getResource("homePage.fxml"));
            Scene homeLoaderScene = new Scene(homeLoader.load(), 995, 800);
            stage.setTitle("Profile");
            homeLoaderScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            VBox postContainer=(VBox) homeLoader.getNamespace().get("postContainer");

            Button addPost=(Button) homeLoader.getNamespace().get("addPost");
            addPost.setOnAction(event ->handleAddPost(stage,postContainer));
            stage.setScene(homeLoaderScene);
            Button manageFriends = (Button) homeLoader.getNamespace().get("manageFriends");
            manageFriends.setOnAction(event -> friendsManager(stage));
            Button viewSuggested = (Button) homeLoader.getNamespace().get("viewSuggested");
            viewSuggested.setOnAction(event -> handleSuggested(stage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void viewFriendsList(Stage stage) {
        try {
            FXMLLoader friendsLoader = new FXMLLoader(Application.class.getResource("viewFriends.fxml"));
            Scene friendsListScene = new Scene(friendsLoader.load(), 289, 189);
            Stage newStage = new Stage();
            newStage.setTitle("Friends");
            friendsListScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(friendsListScene);
            newStage.initOwner(stage);
            newStage.show();
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
            Button cancel = (Button) friendsLoader.getNamespace().get("cancel");
            cancel.setOnAction(event -> newStage.close());
            Button friendRequests = (Button) friendsLoader.getNamespace().get("viewFriendRequests");
            friendRequests.setOnAction(event -> handleFR(stage));
            Button blockRemove = (Button) friendsLoader.getNamespace().get("blockRemove");
            blockRemove.setOnAction(event -> handleBlockRemove(stage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleSuggested(Stage stage) {
        try {
            FXMLLoader suggestedLoader = new FXMLLoader(Application.class.getResource("suggestedFriends.fxml"));
            Scene suggestedListScene = new Scene(suggestedLoader.load(), 289, 189);
            Stage newStage = new Stage();
            newStage.setTitle("Suggested Friends");
            suggestedListScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(suggestedListScene);
            newStage.initOwner(stage);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleFR(Stage stage) {
        try {
            FXMLLoader FRLoader = new FXMLLoader(Application.class.getResource("friendRequests.fxml"));
            Scene FRScene = new Scene(FRLoader.load(), 289, 189);
            Stage newStage = new Stage();
            newStage.setTitle("Friend Requests");
            FRScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(FRScene);
            newStage.initOwner(stage);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleBlockRemove(Stage stage) {
        try {
            FXMLLoader BRLoader = new FXMLLoader(Application.class.getResource("blockRemove.fxml"));
            Scene BRScene = new Scene(BRLoader.load(), 376, 225);
            Stage newStage = new Stage();
            newStage.setTitle("Block / Remove");
            BRScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(BRScene);
            newStage.initOwner(stage);
            newStage.show();
        } catch (IOException e) {
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