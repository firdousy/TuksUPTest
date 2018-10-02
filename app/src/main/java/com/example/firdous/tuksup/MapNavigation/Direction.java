package com.example.firdous.tuksup.MapNavigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Direction {
    List<List<HashMap<String, String>>> routes;
    ArrayList<String> directions;
    int totalTime;

    public Direction(){
        routes = new ArrayList<>();
        directions = new ArrayList<>();
        totalTime = 0;
    }

    public Direction(List<List<HashMap<String, String>>> routes, ArrayList<String> directions, int totalTime) {
        this.routes = routes;
        this.directions = directions;
        this.totalTime = totalTime;
    }

    public List<List<HashMap<String, String>>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<HashMap<String, String>>> routes) {
        this.routes = routes;
    }

    public ArrayList<String> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
