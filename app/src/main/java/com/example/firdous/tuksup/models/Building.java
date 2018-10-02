package com.example.firdous.tuksup.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Building {

    private String id;
    private String name;
    private String shortName;
    //private List<String> rooms;
    private ArrayList<String> rooms;
    private GeoPoint location;

    public Building(){
        rooms = new ArrayList<>();
    }

    public Building(String id, String name, String shortName, GeoPoint location, ArrayList<String> rooms/*, List<String> rooms*/){
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.rooms = rooms;

//        for (int i = 0; i < rooms.size(); i++) {
//            this.rooms.put(i, rooms.get(i));
//        }


        this.location = location;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<String> rooms) {
        this.rooms = rooms;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
