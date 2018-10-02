package com.example.firdous.tuksup.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String userName;
    private String email;
    private ArrayList<String> roles;
//    private List<String> subjectIds;
//    private List<String> lessonIds;

    public  User(){

    }

    public User(String id, String userName, String email, ArrayList<String> roles){
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }


}
