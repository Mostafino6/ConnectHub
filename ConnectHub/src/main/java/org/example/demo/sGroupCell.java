package org.example.demo;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.shape.Circle;

public class sGroupCell extends ListCell<Group> {
    private HBox groupInfo;
    private ImageView groupIcon;
    private Label name;
    private Button joinButton;
    public sGroupCell(){
        groupIcon = new ImageView();
        groupIcon.setFitHeight(50);
        groupIcon.setFitWidth(50);
        groupIcon.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        groupIcon.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        joinButton = new Button();
        joinButton.setText("Join");
        joinButton.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: 700; -fx-min-width: 50px;fx-min-height: 20px; -fx-background-color: #6135D2;");
        HBox text = new HBox(5,name, joinButton);
        text.setSpacing(10);
        groupInfo = new HBox(15, groupIcon,text);
        groupInfo.setPadding(new Insets(10));
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