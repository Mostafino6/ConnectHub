package org.example.demo;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.Date;

public class Post{
    private User owner;
    private String content;
    private String image;
    private LocalDate datePosted;
    public Post( String content) {
        this.content = content;
        this.datePosted = LocalDate.now();
    }
    public Post(User owner, Image imageContent) {
        this.image = imageContent.toString();
        this.datePosted = LocalDate.now();
    }
}
