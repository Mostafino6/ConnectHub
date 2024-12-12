    package org.example.demo;

    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
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
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.Date;

    public class Application extends javafx.application.Application {
        private static final ValidationManager validationManager = new ValidationManager();
        private static User currentUser;
        private static final DatabaseManager databaseManager = new DatabaseManager();
        private static final PostManager postManager = new PostManager();
        private static final StoryManager storyManager = new StoryManager();

        public static void setCurrentUser(User user) {
            currentUser = user;
        }

        public static User getCurrentUser() {
            return currentUser;
        }

        public static DatabaseManager getDatabaseManager() {
            return databaseManager;
        }

        public static ValidationManager getValidationManager() {
            return validationManager;
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
                    LocalDate dob = dobField.getValue();

                    if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || dob == null) {
                        showAlert(Alert.AlertType.ERROR, "Missing Fields", "All fields are required.");
                        return;
                    }
                    try {
                        DatabaseManager dbManager = new DatabaseManager();
                        Date dateOfBirth = dbManager.parseDate(dob.toString());

                        boolean signUpSuccessful = validationManager.signup(email, username, name, password, rewritePassword, dateOfBirth);

                        if (signUpSuccessful) {
                            handleHome(stage); // Navigate to profile on success
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

        private void handleProfile(Stage stage) {
            try {
                FXMLLoader profileLoader = new FXMLLoader(Application.class.getResource("profile.fxml"));
                Scene profileScene = new Scene(profileLoader.load(), 1200, 816);
                stage.setTitle("Profile");
                profileScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                stage.setScene(profileScene);
                Label nameLabel = (Label) profileLoader.getNamespace().get("nameuser");
                if (nameLabel != null) {
                    String username = currentUser.getName();
                    System.out.println(username);
                    nameLabel.setText(username);
                } else {
                    System.out.println("nameLabel is null");
                }
                ImageView imageView = (ImageView) stage.getScene().lookup("#imageView");
                if (imageView != null) {
                    if (!currentUser.getPfpPath().isEmpty()) {
                        Circle clip = new Circle();
                        clip.setRadius(imageView.getFitWidth() / 2);
                        clip.setCenterX(imageView.getFitWidth() / 2);
                        clip.setCenterY(imageView.getFitHeight() / 2);
                        imageView.setClip(clip);
                        imageView.setImage(new Image(currentUser.getPfpPath()));
                    }
                }
                ImageView coverPhotoView = (ImageView) stage.getScene().lookup("#coverPhoto");
                if (coverPhotoView != null) {
                    if (!currentUser.getCoverphotoPath().isEmpty()) {
                        coverPhotoView.setImage(new Image(currentUser.getCoverphotoPath()));
                    }
                }
                Label bioLabel = (Label) stage.getScene().lookup("#bioplace");
                if (bioLabel != null) {
                    if (!currentUser.getBio().isEmpty()) {
                        bioLabel.setText(currentUser.getBio());
                    }
                }
                VBox postContainer = (VBox) profileLoader.getNamespace().get("postContainer");
                Button manageFriends = (Button) profileLoader.getNamespace().get("manageFriends");
                manageFriends.setOnAction(event -> friendsManager(stage));
                Button viewSuggested = (Button) profileLoader.getNamespace().get("viewSuggested");
                viewSuggested.setOnAction(event -> handleSuggested(stage));
                Button viewHome = (Button) profileLoader.getNamespace().get("viewHome");
                viewHome.setOnAction(event -> handleHome(stage));
                Button editProfileButton = (Button) profileLoader.getNamespace().get("editProfileButton");
                editProfileButton.setOnAction(event -> {
                    System.out.println("Edit Profile Button Clicked");
                    handleEditProfile(stage);
                });

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
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
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.show();

                // Locate the necessary fields and buttons from the FXML
                PasswordField newPasswordField = (PasswordField) passwordLoader.getNamespace().get("newpass");
                PasswordField confirmPasswordField = (PasswordField) passwordLoader.getNamespace().get("confirmpass");
                Button passDoneButton = (Button) passwordLoader.getNamespace().get("passdonebutton");

                // Handle the "Done" button action
                passDoneButton.setOnAction(event -> {
                    try {
                        String newPassword = newPasswordField.getText();
                        // Hash the new password before saving
                        newPassword = validationManager.hashPassword(newPassword);

                        String confirmPassword = confirmPasswordField.getText();
                        // Hash the confirm password before checking
                        confirmPassword = validationManager.hashPassword(confirmPassword);

                        // Check if the passwords match
                        if (newPassword.equals(confirmPassword)) {
                            // Update the user's password
                            currentUser.setPassword(newPassword);

                            // Find the password Label in the current scene and update it
                            Label passwordLabel = (Label) stage.getScene().lookup("#passwordLabel");
                            if (passwordLabel != null) {
                                passwordLabel.setText("Password updated");
                            }

                            // Notify the user
                            JOptionPane.showMessageDialog(null, "Password updated successfully.");
                            System.out.println("Password updated successfully.");

                            // Close the Password window
                            newStage.close();

                            // Save the updated user profile
                            databaseManager.writeUser(currentUser);
                        } else {
                            // Notify the user if passwords do not match
                            JOptionPane.showMessageDialog(null, "Passwords do not match. Please try again.");
                            System.out.println("Passwords do not match. Please try again.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
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
                        databaseManager.writeUser(currentUser);
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
                        try {
                            databaseManager.writeUser(currentUser);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
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
                        try {
                            databaseManager.writeUser(currentUser);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("Cover ImageView not found in the current scene.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleHome(Stage stage) {
            try {
                FXMLLoader homeLoader = new FXMLLoader(Application.class.getResource("homePage.fxml"));
                Scene homeLoaderScene = new Scene(homeLoader.load(), 1200, 900);
                stage.setTitle("Profile");
                homeLoaderScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                VBox postContainer = (VBox) homeLoader.getNamespace().get("postContainer");
                stage.setScene(homeLoaderScene);
                Button manageFriends = (Button) homeLoader.getNamespace().get("manageFriends");
                manageFriends.setOnAction(event -> friendsManager(stage));
                Button viewSuggested = (Button) homeLoader.getNamespace().get("viewSuggested");
                viewSuggested.setOnAction(event -> handleSuggested(stage));
                Button viewProfile = (Button) homeLoader.getNamespace().get("viewProfile");
                viewProfile.setOnAction(event -> handleProfile(stage));
                Button searchBtn=(Button) homeLoader.getNamespace().get("searchBtn");
                searchBtn.setOnAction(event ->{
                    try {
                        handleSearch(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                Button addPost = (Button) homeLoader.getNamespace().get("addPost");
                addPost.setOnAction(event -> {
                    try {
                        handleAddPost(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                Button addStory = (Button) homeLoader.getNamespace().get("addStory");
                addStory.setOnAction(event -> {
                    try {
                        handleAddStory(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                Button refresh = (Button) homeLoader.getNamespace().get("refresh");
                refresh.setOnAction(event -> {
                    try {
                        ArrayList<User> updatedUsers = databaseManager.readUsers();
                        // Update the current user object with the refreshed data
                        for (User user : updatedUsers) {
                            if (user.getUserID().equals(currentUser.getUserID())) {
                                currentUser = user;
                                Application.setCurrentUser(currentUser);
                                break;
                            }
                        }
                        // Refresh the home page with the updated currentUser
                        handleHome(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                Button logout = (Button) homeLoader.getNamespace().get("logOut");
                logout.setOnAction(event -> {
                    stage.close();
                    currentUser.setStatus(false);
                    try {
                        databaseManager.writeUser(currentUser);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                });
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
                Scene FRScene = new Scene(FRLoader.load(), 389, 225);
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
        private void handleSearch(Stage stage) throws IOException {
            try {

                FXMLLoader searchLoader = new FXMLLoader(Application.class.getResource("searchPage.fxml"));
                Scene searcSecene = new Scene(searchLoader.load(), 600, 400);
                Stage newStage = new Stage();
                newStage.setTitle("Search");
                searcSecene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                newStage.setScene(searcSecene);
                newStage.initOwner(stage);
                newStage.show();

                // Accessing FXML elements
                TextField searchField = (TextField) searchLoader.getNamespace().get("searchField");
                Button searchDone = (Button) searchLoader.getNamespace().get("searchDone");
                ListView<SearchCell> searchListView = (ListView<SearchCell>) searchLoader.getNamespace().get("searchListView");

                if (searchDone == null) {
                    System.out.println("searchDone button not found!");
                }

                searchDone.setOnAction(event -> {
                    searchListView.getItems().clear(); // Clear previous search results
                    boolean isFound = false;
                    boolean isFriend = true;
                    boolean blocked=false;

                    try {
                        User currentUser = Application.getCurrentUser();
                        User searchedUser = null;
                        ArrayList<User> userList = databaseManager.readUsers();

                        // Check if search field is not empty
                        if (!searchField.getText().isEmpty()) {
                            String username = searchField.getText();

                            // Search through the list of users
                            for (int i = 0; i < userList.size(); i++) {
                                if (username.equals(userList.get(i).getUsername())) {
                                    searchedUser = userList.get(i);
                                    isFound = true;
                                    break; // Exit the loop once the user is found
                                }
                            }
                            if (isFound) {
                                if (!currentUser.getFriends().getBlockedFriends().contains(searchedUser)) {
//                                    showAlert(Alert.AlertType.WARNING, "Warning",Boolean.toString(currentUser.getFriends().isBlocked(searchedUser)));

                                    SearchCell searchCell = new SearchCell(isFriend, searchedUser,stage);
                                    searchListView.getItems().add(searchCell); // Add to ListView
                                } else {
                                    showAlert(Alert.AlertType.WARNING, "Warning", "User not found.");
                                }
                            } else {
                                showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
                            }
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Enter a username.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing the search.");
                    }
                });
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
        private void handleAddPost(Stage stage) throws IOException {
            FXMLLoader postLoader = new FXMLLoader(Application.class.getResource("addPost.fxml"));
            Scene addPostScene = new Scene(postLoader.load(), 600, 400);
            Stage addPostStage = new Stage();
            addPostStage.setTitle("Add Post");
            addPostScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            addPostStage.setScene(addPostScene);
            String selectedImagePath[] = {null};
            TextField addText = (TextField) postLoader.getNamespace().get("addText");
            Button addImage = (Button) postLoader.getNamespace().get("addImage");
            addImage.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(addPostStage);
                if (file != null) {
                    selectedImagePath[0] = file.getAbsolutePath();
                }
            });
            Button addPostButton = (Button) postLoader.getNamespace().get("post");
            addPostButton.setOnAction(event -> {
                String text = addText.getText();
                if (text.isEmpty() && selectedImagePath[0] == null) {
                    JOptionPane.showMessageDialog(null, "Empty Story");
                } else {
                    Post newPost = new Post(currentUser, text, selectedImagePath[0]);
                    try {
                        postManager.addPost(newPost);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    addPostStage.close();
                }

            });
            addPostStage.show();
        }

        private void handleAddStory(Stage stage) throws IOException {
            FXMLLoader postLoader = new FXMLLoader(Application.class.getResource("addStory.fxml"));
            Scene addStoryScene = new Scene(postLoader.load(), 600, 400);
            Stage addStoryStage = new Stage();
            addStoryStage.setTitle("Add Story");
            addStoryScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
            addStoryStage.setScene(addStoryScene);
            String selectedImagePath[] = {null};
            TextField addText = (TextField) postLoader.getNamespace().get("addText");
            Button addImage = (Button) postLoader.getNamespace().get("addImage");
            addImage.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(addStoryStage);
                if (file != null) {
                    selectedImagePath[0] = file.getAbsolutePath();
                }
            });
            Button addPostButton = (Button) postLoader.getNamespace().get("post");
            addPostButton.setOnAction(event -> {
                String text = addText.getText();
                if (text.isEmpty() && selectedImagePath[0] == null) {
                    JOptionPane.showMessageDialog(null, "Empty Story");}
                else if (selectedImagePath[0] == null) {
                    JOptionPane.showMessageDialog(null, "Story has to include image");
                }
                    else {
                    Story newStory = new Story(currentUser, text, selectedImagePath[0]);
                    try {
                        storyManager.addStory(newStory);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    addStoryStage.close();
                }
            });
            addStoryStage.show();
        }
    public static void main(String[] args) {
        launch();
    }
}
