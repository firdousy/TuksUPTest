package com.example.firdous.tuksup.models;

public class Weekday {

    private String id;
    private String name;

    public  Weekday(){

    }

    public Weekday(String id, String name)
    {
        this.id = id;
        this.name = name;
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
}
