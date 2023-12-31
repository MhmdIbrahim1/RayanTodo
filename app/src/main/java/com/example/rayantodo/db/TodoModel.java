package com.example.rayantodo.db;

public class TodoModel {
    private int id;
    private int status;
    private String title;
    private String description;


    public TodoModel(int status, String title, String description) {
        this.status = status;
        this.title = title;
        this.description = description;
    }

    public TodoModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
