package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class groupCell extends ListCell<Group> {
    private HBox groupInfo;
    private ImageView groupIcon;
    private Label name;
    private Button viewButton;
    private Group currentGroup;
    public groupCell(){
        groupIcon = new ImageView();
        groupIcon.setFitHeight(50);
        groupIcon.setFitWidth(50);
        groupIcon.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        groupIcon.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        viewButton = new Button();
        viewButton.setText("View");
        viewButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");
        HBox text = new HBox(5,name, viewButton);
        text.setSpacing(10);
        groupInfo = new HBox(15, groupIcon,text);
        groupInfo.setPadding(new Insets(10));
        viewButton.setOnAction(event->{
            try {
                MainApplication app = MainApplication.getInstance();
                Application.setCurrentGroup(getItem());
                currentGroup = Application.getCurrentGroup();
                app.handleViewButton(Application.getPrimaryStage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Override
    protected void updateItem(Group group, boolean empty) {
        super.updateItem(group, empty);

        if (empty || group == null) {
            setGraphic(null);
        } else {
            groupIcon.setImage(new Image(group.getGroupIcon()));
            name.setText(group.getGroupName());
            setGraphic(groupInfo);
        }
    }

}