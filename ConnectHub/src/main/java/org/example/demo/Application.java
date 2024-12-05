    package org.example.demo;

    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextField;
    import javafx.scene.shape.Circle;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;

    import javax.swing.*;
    import java.io.File;
    import java.io.IOException;

    public class Application extends javafx.application.Application {
        private User currentUser;
        @Override
        public void start(Stage stage) throws IOException {
            User newUser = new User();
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
                Scene profileScene = new Scene(profileLoader.load(), 995, 800);
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
                Scene editProfileScene = new Scene(editProfileLoader.load(), 600, 400);
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

                Button updateCoverPhoto = (Button) editProfileLoader.getNamespace().get("updateCoverPhoto");
                updateCoverPhoto.setOnAction(event -> {
                    System.out.println("update cover photo Button Clicked");
                  //  handleupdateCoverPhoto(stage);
                });

                Button changeBioButton = (Button) editProfileLoader.getNamespace().get("changeBio");
                changeBioButton.setOnAction(event -> {
                    System.out.println("update bio button clicked");
                    handleUpdateBio(stage);
                });

                Button changePassword = (Button) editProfileLoader.getNamespace().get("changePassword");
                changePassword.setOnAction(event -> {
                    System.out.println("change password button clicked");
                    handleUpdatePassword(stage);
                });


            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
        }

        private void handleUpdatePassword(Stage stage) {
            try {
                // Load the password update scene
                FXMLLoader passwordLoader = new FXMLLoader(Application.class.getResource("password.fxml"));
                Scene passwordScene = new Scene(passwordLoader.load(), 600, 400);
                Stage newStage = new Stage();
                passwordScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                newStage.setScene(passwordScene);
                newStage.initOwner(stage);
                newStage.show();

                TextField newPasswordField = (TextField) passwordLoader.getNamespace().get("newpass");
                TextField confirmPasswordField = (TextField) passwordLoader.getNamespace().get("confirmpass");

                Button passDoneButton = (Button) passwordLoader.getNamespace().get("passdonebutton");
                passDoneButton.setOnAction(event -> {
                    String newPassword = newPasswordField.getText();
                    String confirmPassword = confirmPasswordField.getText();
                    // Validate that both passwords match
                    if (newPassword.equals(confirmPassword)) {
                        currentUser.setPassword(newPassword);
                        JOptionPane.showMessageDialog(null,"Password updated successfully");
                        System.out.println("Password updated successfully.");
                        newStage.close();
                    } else {
                        JOptionPane.showMessageDialog(null,"Passwords do not match. Please try again.");
                    System.out.println("Passwords do not match. Please try again.");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void handleUpdateBio(Stage stage) {
            try {
                // Load the Bio FXML (the scene where the user enters new bio)
                FXMLLoader bioLoader = new FXMLLoader(Application.class.getResource("Bio.fxml"));
                Scene bioScene = new Scene(bioLoader.load(), 600, 400);
                Stage newStage = new Stage();
                bioScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                newStage.setScene(bioScene);
                newStage.initOwner(stage);
                newStage.show();

                Button biodoneButton = (Button) bioLoader.getNamespace().get("BIODONE");
                biodoneButton.setOnAction(event -> {
                    try {
                        // Get the new bio from the text field in the Bio window
                        TextField bioTextField = (TextField) bioLoader.getNamespace().get("bioTextField");
                        String newBio = bioTextField.getText();
                        // Close the Bio window
                        newStage.close();
                        // Now update the Label in the existing profile scene
                        FXMLLoader profileLoader = new FXMLLoader(Application.class.getResource("profile.fxml"));
                        Scene profileScene = new Scene(profileLoader.load(), 995, 800);
                        profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                        // Access the Label from the profile scene
                        Label bioLabel = (Label) profileLoader.getNamespace().get("bioplace");
                        // Update the Label text with the new bio
                        bioLabel.setText(newBio);
                        // Set the updated profile scene to the stage
                        stage.setScene(profileScene);
                        stage.show();
                        currentUser.setBio(newBio);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } catch (IOException e) {
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
                    Scene profileScene = new Scene(profileLoader.load(), 995, 800);
                    ImageView imageView = (ImageView) profileLoader.getNamespace().get("imageView");
                    profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                    Circle clip = new Circle();
                    //making image in the Imageview circular shape
                    clip.setRadius(imageView.getFitWidth() / 2);
                    clip.setCenterX(imageView.getFitWidth() / 2);
                    clip.setCenterY(imageView.getFitHeight() / 2);
                    imageView.setClip(clip);
                    //setting image
                    imageView.setImage(newImage);
                    stage.setScene(profileScene);
                    currentUser.setPfpPath(selectedFile.getAbsolutePath());
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //        private void handleupdateCoverPhoto(Stage stage) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select cover photo File");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
//            );
//            File selectedFile = fileChooser.showOpenDialog(stage);
//            if (selectedFile != null) {
//                try {
//                    Image newImage = new Image(selectedFile.toURI().toString());
//                    FXMLLoader profileLoader = new FXMLLoader(Application.class.getResource("profile.fxml"));
//                    Scene profileScene = new Scene(profileLoader.load(), 995, 800);
//                    ImageView imageView = (ImageView) profileLoader.getNamespace().get("imageView");
//                    profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
//                    imageView.setImage(newImage);
//                    stage.setScene(profileScene);
//                    //user.setpfpPath(selectedfile.absouloutepath()); when user functionality is added
//                    stage.show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }



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