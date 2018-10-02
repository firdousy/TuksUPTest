package com.example.firdous.tuksup.models;

public class Session {
    private String id;
    private String timeSlot;

    public Session(){

    }

    public Session(String id, String timeSlot){
        this.id = id;
        this.timeSlot = timeSlot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
