    package org.example.demo;

    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextField;
    import javafx.scene.shape.Circle;
    import javafx.stage.FileChooser;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import javax.swing.*;
    import java.io.File;
    import java.io.IOException;
    import java.util.Date;

public class Application extends javafx.application.Application {
    private final ValidationManager validationManager = new ValidationManager();
    private static User currentUser;
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
            TextField emailField = (TextField) loginLoader.getNamespace().get("LoginEmailField");
            PasswordField passwordField = (PasswordField) loginLoader.getNamespace().get("LoginPassField");

            loginDone.setOnAction(event -> {
                String email = emailField.getText();
                String password = passwordField.getText();

                boolean loginSuccessful = validationManager.login(email, password);

                if (loginSuccessful) {
                    handleHome(stage); // Navigate to home on successful login
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred with your login.");
        }
    }

    public void showAlert(Alert.AlertType type, String title, String content) {
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
                        handleProfile(stage); // Navigate to profile on success
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your signup.");
                }
            });} catch (IOException e) {
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
                Button addPost=(Button) profileLoader.getNamespace().get("addPost");
                VBox postContainer=(VBox) profileLoader.getNamespace().get("postContainer");
                addPost.setOnAction(event ->handleAddPost(stage,postContainer));
                Button manageFriends = (Button) profileLoader.getNamespace().get("manageFriends");
                manageFriends.setOnAction(event -> friendsManager(stage));
                Button viewSuggested = (Button) profileLoader.getNamespace().get("viewSuggested");
                viewSuggested.setOnAction(event -> handleSuggested(stage));
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
                    handleupdateCoverPhoto(stage);
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
            // Load the password update FXML
            FXMLLoader passwordLoader = new FXMLLoader(Application.class.getResource("password.fxml"));
            Scene passwordScene = new Scene(passwordLoader.load(), 600, 400);
            Stage newStage = new Stage();
            newStage.setTitle("Update Password");
            passwordScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(passwordScene);
            newStage.initOwner(stage);
            newStage.initModality(Modality.WINDOW_MODAL); // Make the Password window modal
            newStage.show();

            // Locate the necessary fields and buttons
            TextField newPasswordField = (TextField) passwordLoader.getNamespace().get("newpass");
            TextField confirmPasswordField = (TextField) passwordLoader.getNamespace().get("confirmpass");
            Button passDoneButton = (Button) passwordLoader.getNamespace().get("passdonebutton");

            // Handle the "Done" button action
            passDoneButton.setOnAction(event -> {
                String newPassword = newPasswordField.getText();
                String confirmPassword = confirmPasswordField.getText();

                if (newPassword.equals(confirmPassword)) {
                    // Update the user's password
                    currentUser.setPassword(newPassword);

                    // Notify the user
                    JOptionPane.showMessageDialog(null, "Password updated successfully.");
                    System.out.println("Password updated successfully.");

                    // Close the Password window
                    newStage.close();
                } else {
                    // Notify the user of the mismatch
                    JOptionPane.showMessageDialog(null, "Passwords do not match. Please try again.");
                    System.out.println("Passwords do not match. Please try again.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateBio(Stage stage) {
        try {
            // Load the Bio FXML (for entering the new bio)
            FXMLLoader bioLoader = new FXMLLoader(Application.class.getResource("Bio.fxml"));
            Scene bioScene = new Scene(bioLoader.load(), 600, 400);
            Stage newStage = new Stage();
            newStage.setTitle("Update Bio");
            bioScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            newStage.setScene(bioScene);
            newStage.initOwner(stage);
            newStage.initModality(Modality.WINDOW_MODAL); // Make the Bio window modal
            newStage.show();
            Button biodoneButton = (Button) bioLoader.getNamespace().get("BIODONE");
            biodoneButton.setOnAction(event -> {
                try {
                    // Get the new bio from the text field
                    TextField bioTextField = (TextField) bioLoader.getNamespace().get("bioTextField");
                    String newBio = bioTextField.getText();
                    // Update the bio in the current user's profile
                    currentUser.setBio(newBio);
                    // Find the bio Label in the current scene and update it
                    Label bioLabel = (Label) stage.getScene().lookup("#bioplace");
                    if (bioLabel != null) {
                        bioLabel.setText(newBio);
                    }
                    // Close the Bio window
                    newStage.close();
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
                // Convert the file path to an image
                String newImagePath = selectedFile.toURI().toString();
                Image newImage = new Image(newImagePath);

                // Locate the ImageView in the current scene
                ImageView imageView = (ImageView) stage.getScene().lookup("#imageView");
                if (imageView != null) {
                    // Make the ImageView circular
                    Circle clip = new Circle();
                    clip.setRadius(imageView.getFitWidth() / 2);
                    clip.setCenterX(imageView.getFitWidth() / 2);
                    clip.setCenterY(imageView.getFitHeight() / 2);
                    imageView.setClip(clip);
                    // Set the new image
                    imageView.setImage(newImage);
                    // Update the user's profile picture path (stored as a String)
                    currentUser.setPfpPath(selectedFile.getAbsolutePath());
                } else {
                    System.out.println("ImageView not found in the current scene.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleupdateCoverPhoto(Stage stage) {
        // Open file chooser for selecting a cover photo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Cover Photo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                String newCoverPhotoPath = selectedFile.getAbsolutePath();
                Image newImage = new Image(selectedFile.toURI().toString());
                ImageView coverPhotoView = (ImageView) stage.getScene().lookup("#coverPhoto");
                if (coverPhotoView != null) {
                    coverPhotoView.setImage(newImage);
                    currentUser.setCoverphotoPath(newCoverPhotoPath);
                } else {
                    System.out.println("Cover ImageView not found in the current scene.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    private void handleAddPost(Stage stage, VBox postContainer) {
        try {
            FXMLLoader addPostLoader = new FXMLLoader(Application.class.getResource("addPost.fxml"));
            Scene addPostScene = new Scene(addPostLoader.load(), 650, 420);
            Stage addPostStage = new Stage();
            addPostStage.setTitle("Add Post");
            addPostScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            addPostStage.setScene(addPostScene);

            Button postDone = (Button) addPostLoader.getNamespace().get("postDone");
            Button cancelPost = (Button) addPostLoader.getNamespace().get("cancelPost");
            Button imageChooser = (Button) addPostLoader.getNamespace().get("imageChooser");
            TextField textPost = (TextField) addPostLoader.getNamespace().get("textPost");

            // Variable to hold the selected image path
            String[] selectedImagePath = {null};

            // Image chooser button action
            imageChooser.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                File file = fileChooser.showOpenDialog(addPostStage);
                if (file != null) {
                    selectedImagePath[0] = file.toURI().toString(); // Store the image path
                }
            });
            // Post done button action
            postDone.setOnAction(event -> {
                String textContent = textPost.getText();
                if (textContent.isEmpty() && selectedImagePath[0] == null) {
                    showAlert(Alert.AlertType.ERROR, "Empty Post", "Post content cannot be empty.");
                } else {
                    // Create a new post based on content
                    Post newPost;
                    if (textContent.isEmpty()) {
                        newPost = new Post(currentUser, new Image(selectedImagePath[0])); // For image post only
                    } else {
                        newPost = new Post(currentUser, textContent); // For text post
                    }
                    addPostToContainer(newPost, postContainer);
                    addPostStage.close();
                }
            });

            cancelPost.setOnAction(event -> addPostStage.close());

            addPostStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addPostToContainer(Post post, VBox postContainer) {
        HBox postBox = new HBox();
        postBox.getStyleClass().add("postBox");
        ImageView profileImage = new ImageView(new Image(getClass().getResource("/org/example/demo/profile-icon.png").toExternalForm()));
        profileImage.setFitHeight(50);
        profileImage.setFitWidth(50);
        profileImage.setPreserveRatio(true);
        VBox postContentContainer = new VBox();
        // Add content if available
        if (post.getContent() != null) {
            Label postContent = new Label(post.getContent());
            postContent.getStyleClass().add("textPostContent");
            postContentContainer.getChildren().add(postContent);
        }

        // Add image if available
        if (post.getImage() != null) {
            ImageView postImage = new ImageView(new Image(post.getImage()));
            postImage.setFitHeight(250);
            postImage.setFitWidth(250);
            postImage.setPreserveRatio(true);
            postContentContainer.getChildren().add(postImage);
        }

        // Add date
        Label postDate = new Label(post.getDatePosted().toString()); // Format as needed
        postDate.getStyleClass().add("postDate"); // Add a style class for the date
        postContentContainer.getChildren().add(postDate);

        Label postUsername = new Label(post.getOwner().getUsername()); // Format as needed
        postUsername.getStyleClass().add("postDate"); // Add a style class for the date
        postContentContainer.getChildren().add(postUsername);

        postBox.getChildren().addAll(profileImage, postContentContainer);
        postContainer.getChildren().add(postBox);
    }
//    private void addTextPost(String content,VBox postContainer) {
//        HBox textPostBox = new HBox();
//        textPostBox.getStyleClass().add("textPost");
//        ImageView profileImage = new ImageView(new Image(getClass().getResource("/org/example/demo/profile-icon.png").toExternalForm()));
//        profileImage.setFitHeight(50);
//        profileImage.setFitWidth(50);
//        profileImage.setPreserveRatio(true);
//        profileImage.getStyleClass().add("pp");
//        Label postContent = new Label(content);
//        postContent.getStyleClass().add("textPostContent");
//        textPostBox.getChildren().addAll(profileImage, postContent);
//        postContainer.getChildren().add(textPostBox);
//    }
//    private void addImagePost(File imageFile,VBox postContainer) {
//        HBox imagePostBox = new HBox();
//        imagePostBox.getStyleClass().add("imagePost");
//        ImageView profileImage = new ImageView(new Image(getClass().getResource("/org/example/demo/profile-icon.png").toExternalForm()));
//        profileImage.setFitHeight(50);
//        profileImage.setFitWidth(50);
//        profileImage.setPreserveRatio(true);
//        profileImage.getStyleClass().add("pp");
//        ImageView postImage = new ImageView(new Image(imageFile.toURI().toString()));
//        postImage.setFitHeight(249);
//        postImage.setFitWidth(311);
//        postImage.setPreserveRatio(true);
//        postImage.getStyleClass().add("postImage");
//
//        imagePostBox.getChildren().addAll(profileImage, postImage);
//
//        postContainer.getChildren().add(imagePostBox);
//    }
    public static void main(String[] args) {
        launch();
    }
}
