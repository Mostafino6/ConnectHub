package org.example.demo;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.shape.Circle;

public class groupMemberCell extends ListCell<User>{
    private HBox memberInfo;
    private ImageView pfp;
    private Label name;
    private Label status;
    private Group currentGroup;
    public groupMemberCell(Group group){
        currentGroup = group;
        pfp = new ImageView();
        pfp.setFitHeight(50);
        pfp.setFitWidth(50);
        pfp.setPreserveRatio(true);
        Circle clip = new Circle(25, 25, 25);
        pfp.setClip(clip);
        name = new Label();
        name.setStyle("-fx-font-weight: bold;");
        status = new Label();
        HBox text = new HBox(5,name,status);
        text.setSpacing(10);
        memberInfo = new HBox(15,pfp,text);
        memberInfo.setPadding(new Insets(10));
    }
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) {
            setGraphic(null);
        } else {
            pfp.setImage(new Image(user.getPfpPath()));
            name.setText(user.getName());
            if(currentGroup.getCreator().equals(user)){
                status.setText("Creator");
            }
            else if(currentGroup.getHierarchy().getAdmins().contains(user)) {
                status.setText("Admin");
            }
            else{
                status.setText("Member");
            }
            setGraphic(memberInfo);
        }
    }
}