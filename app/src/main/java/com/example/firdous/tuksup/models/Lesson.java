package com.example.firdous.tuksup.models;

public class Lesson
{

    private String id;
    private String name;
    private int sessionId;
    private String buildingId;
    private int lectureTypeId;
    private int lectureNumber;
    private String subjectId;
    private String userId;
    private int weekDayId;

    public Lesson(){
    }

    public Lesson(String id, String name, int sessionId, String buildingId, int lectureTypeId, int lecturenumber, String subjectId, String UserId, int weekdayId)
    {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
        this.buildingId = buildingId;
        this.lectureTypeId = lectureTypeId;
        this.lectureNumber = lecturenumber;
        this.subjectId = subjectId;
        this.userId = UserId;
        this.weekDayId = weekdayId;
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

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public int getLectureTypeId() {
        return lectureTypeId;
    }

    public void setLectureTypeId(int lectureTypeId) {
        this.lectureTypeId = lectureTypeId;
    }

    public int getLectureNumber() {
        return lectureNumber;
    }

    public void setLectureNumber(int lectureNumber) {
        this.lectureNumber = lectureNumber;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWeekDayId() {
        return weekDayId;
    }

    public void setWeekDayId(int weekDayId) {
        this.weekDayId = weekDayId;
    }


}
