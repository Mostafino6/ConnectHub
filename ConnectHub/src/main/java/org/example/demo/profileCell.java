package org.example.demo;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

public class profileCell extends ListCell<User>{
    private HBox userInfo;
    private ImageView pfp;
    private Label name;
    private Label status;
    public profileCell(){
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);
        name = new Label();
        status = new Label();
        HBox text = new HBox(5,name,status);
        text.setSpacing(10);
        userInfo = new HBox(10,pfp,text);
        userInfo.setPadding(new Insets(5));
    }
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if (empty || user == null) {
            setGraphic(null);
        } else {
            pfp.setImage(new Image(user.getPfpPath()));
            name.setText(user.getName());
            status.setText(user.getStatus());
            setGraphic(userInfo);
        }
    }

}
