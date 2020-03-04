package com.example.firebasefirestore_notebook.model;

//Model class
public class Note {
    private String title;
    private String description;

    public Note()
    {
        //Empty constructor needed for firebase to later create new note objects
    }

    //Constructor
    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
