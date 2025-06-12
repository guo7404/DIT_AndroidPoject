package com.example.dailyup;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotes")
public class Quote {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;
    private String author;
    private String category;
    private boolean isBookmarked;

    public Quote() {}

    public Quote(String text, String author, String category) {
        this.text = text;
        this.author = author;
        this.category = category;
        this.isBookmarked = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}