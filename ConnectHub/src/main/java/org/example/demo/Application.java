package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


import javafx.stage.FileChooser;

import java.io.File;
import java.util.Date;

public class Application extends javafx.application.Application {

    private final ValidationManager validationManager = new ValidationManager();

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
            TextField emailField = (TextField) loginLoader.getNamespace().get("LoginEmailField");
            PasswordField passwordField = (PasswordField) loginLoader.getNamespace().get("LoginPassField");

            loginDone.setOnAction(event -> {
                String email = emailField.getText();
                String password = passwordField.getText();

                boolean loginSuccessful = validationManager.login(email, password);

                if (loginSuccessful) {
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome back!");
                    handleHome(stage); // Navigate to home on successful login
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password. Please try again.");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void handleSignClick(Stage stage) {
        try {
            FXMLLoader signLoader = new FXMLLoader(Application.class.getResource("signUp.fxml"));
            Scene signUpScene = new Scene(signLoader.load(), 950, 580);
            stage.setTitle("Sign Up");
            signUpScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            stage.setScene(signUpScene);

            Button signUpDone = (Button) signLoader.getNamespace().get("SignUpDone");
            TextField usernameField = (TextField) signLoader.getNamespace().get("signUpUsernameField");
            TextField nameField = (TextField) signLoader.getNamespace().get("signUpNameField");
            DatePicker dobField = (DatePicker) signLoader.getNamespace().get("signUpDobField");
            TextField emailField = (TextField) signLoader.getNamespace().get("signUpemailField");
            PasswordField passwordField = (PasswordField) signLoader.getNamespace().get("signUpPasswordField");
            PasswordField RewritePasswordField = (PasswordField) signLoader.getNamespace().get("signUpRewritePasswordField");

            signUpDone.setOnAction(event -> {
                String username = usernameField.getText();
                String name = nameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String rewritePassword = RewritePasswordField.getText();
                java.time.LocalDate dob = dobField.getValue();

                if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || dob == null) {
                    showAlert(Alert.AlertType.ERROR, "Missing Fields", "All fields are required.");
                    return;
                }

                try {
                    DatabaseManager dbManager = new DatabaseManager();
                    Date dateOfBirth = dbManager.parseDate(dob.toString());

                    boolean signUpSuccessful = validationManager.signup(email, username, name, password, rewritePassword, dateOfBirth);

                    if (signUpSuccessful) {
                        showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "Welcome! Your account has been created.");
                        handleProfile(stage); // Navigate to profile on success
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Signup Failed", "Signup failed. Please check your inputs and try again.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your signup.");
                }
            });
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