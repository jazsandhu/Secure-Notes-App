package com.example.securenotesapp;

public class Notes {
    private String title;
    private String noteContent;

    public Notes(String description) {
        this.title = description;
        this.noteContent = null;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return noteContent;
    }

    public void setContent(String noteContent) {
        this.noteContent = noteContent;
        return;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}