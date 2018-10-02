package com.example.firdous.tuksup.Singletons;

import com.example.firdous.tuksup.models.Building;
import com.example.firdous.tuksup.models.Lesson;
import com.example.firdous.tuksup.models.Subject;

import java.util.List;

public class SessionSingleton  {

    private static SessionSingleton INSTANCE = new SessionSingleton();

    private String UserId;
    private List<Building> buildingsAvailable;
    private Building selectedBuilding;
    private Lesson selectedLesson;
    private Subject selectedSubject;

    // other instance variables can be here

    private SessionSingleton() {};

    public static SessionSingleton getInstance() {
        return(INSTANCE);
    }

    // other instance methods can follow


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public List<Building> getBuildingsAvailable() {
        return buildingsAvailable;
    }

    public void setBuildingsAvailable(List<Building> buildingsAvailable) {
        this.buildingsAvailable = buildingsAvailable;
    }

    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    public void setSelectedBuilding(Building selectedBuilding) {
        this.selectedBuilding = selectedBuilding;
    }

    public Lesson getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedLesson(Lesson selectedLesson) {
        this.selectedLesson = selectedLesson;
    }

    public Subject getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject = selectedSubject;
    }
}
