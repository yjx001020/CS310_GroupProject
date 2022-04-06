package com.csci310.models;

import java.util.List;
import java.util.ArrayList;

public class User {
    private int id;
    String email; //userid
    private String password;
    String photoFilename;
    String firstName;
    String lastName;
    String major;
    String studyYear;

    public int getID() {
        return id;
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