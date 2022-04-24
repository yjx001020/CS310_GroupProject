package com.csci310.models;

import java.util.List;
import java.util.ArrayList;

public class User {
    public String email;
    private int id;
    private String password;
    public String photoFilename;
    public String fname;
    public String lname;
    public String major;
    public String studyYear;

    public String getUserSQL(String emailId) {
        String query = "Select * from Users where email = " + "'" + emailId + "'";
        return query;
    }

    public void userSignUp() {

    }

    public void userLogin() {

    }

    public void editEvent(int eventID) {

    }

    public void createEventInvitation() {

    }

    public void signUpEvent(int eventID) {

    }

    public void acceptEventInvitation(int eventID) {

    }

    public void rejectEventInvitation(int eventID) {

    }

    public void withdrawEvent(int eventID) {

    }

    public List<Event> getEventsCreated() {
        return new ArrayList<>();
    }

    public List<Event> getEventsReceived() {
        return new ArrayList<>();
    }

    public List<Event> getEventsSignedUp() {
        return new ArrayList<>();
    }
}