package com.example.firdous.tuksup.models;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private String id;
    private String name;
    private String userId;
    private String lecturer;
    private ArrayList<String> lessons;
    private String code;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Subject(){

    }

    public Subject(String id, String name, String userId, String lecturer, ArrayList<String> lessons, String code) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.lecturer = lecturer;
        this.lessons = lessons;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public ArrayList<String> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<String> lessons) {
        this.lessons = lessons;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
