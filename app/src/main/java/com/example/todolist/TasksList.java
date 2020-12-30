package com.example.todolist;

public class TasksList {

    private String id;


    private String title;
    private int size;
    private String uid;
    private String date;

    public TasksList(String id, String title, int size, String uid, String date) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.uid = uid;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TasksList() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
