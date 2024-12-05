    package org.example.demo;

    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;

    import java.io.File;
    import java.io.IOException;

    public class Application extends javafx.application.Application {
        private User currentUser;
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
        private void handleProfile(Stage stage) {
            try {
                FXMLLoader profileLoader = new FXMLLoader(Application.class.getResource("profile.fxml"));
                Scene profileScene = new Scene(profileLoader.load(), 950, 580);
                stage.setTitle("Profile");
                profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                stage.setScene(profileScene);

                Button manageFriends = (Button) profileLoader.getNamespace().get("manageFriends");
                manageFriends.setOnAction(event -> friendsManager(stage));

                Button editProfileButton = (Button) profileLoader.getNamespace().get("editProfileButton");
                editProfileButton.setOnAction(event -> {
                    System.out.println("Edit Profile Button Clicked");
                    handleEditProfile(stage);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleEditProfile(Stage stage) {
            try {
                FXMLLoader editProfileLoader = new FXMLLoader(Application.class.getResource("editProfile.fxml"));
                Scene editProfileScene = new Scene(editProfileLoader.load(), 950, 580);
                Stage newStage = new Stage();
                stage.setTitle("Edit Profile");
                editProfileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                newStage.setScene(editProfileScene);
                newStage.initOwner(stage);
                newStage.show();
                Button updatePfpButton = (Button) editProfileLoader.getNamespace().get("updatePfp");
                updatePfpButton.setOnAction(event -> {
                    System.out.println("update Profile pic clicked");
                    handleUpdatefpfp(stage);
                });

            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
        }

        private void handleUpdatefpfp(Stage stage) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Profile Picture");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    Image newImage = new Image(selectedFile.toURI().toString());
                    FXMLLoader profileLoader = new FXMLLoader(Application.class.getResource("profile.fxml"));
                    Scene profileScene = new Scene(profileLoader.load(), 950, 580);
                    ImageView imageView = (ImageView) profileLoader.getNamespace().get("imageView");
                    imageView.setImage(newImage);
                    Stage newStage = new Stage();
                    newStage.setScene(profileScene);
                    newStage.initOwner(stage);
                    newStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void handleHome(Stage stage)
        {
            try {
                FXMLLoader homeLoader = new FXMLLoader(Application.class.getResource("homePage.fxml"));
                Scene homeLoaderScene = new Scene(homeLoader.load(), 950, 580);
                stage.setTitle("Profile");
                homeLoaderScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            launch();
        }
    }