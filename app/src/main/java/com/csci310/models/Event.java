package com.csci310.models;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Event {
    int eventID;
    String accessType; //public or private
    String location;
    String description;// which class for study group,..
    String eventType; //study group, small event, social event, sports event
    String dueTime;
    List<String> proposedTimeslots;
    String timeDecided;
    String owner;
    String ownerID;
    List<String> peopleInvited; //by the event owner, select userID from invitations where eventID = ""
    List<String> peopleSignedUp; //people who signed up, select userID from invitations where eventID = "" and acceptStatus = 1
    List<String> participants;//peopleSignedUp who are available at the decided time slot


    public Event(String eventType, String location, String timeslots, String dueTime, String description,
                 String invitedUserEmail, String accessType, String ownerID) {
        this.eventType = eventType;
        this.location = location;
        String[] timeslotArray = timeslots.split("\n");
        this.proposedTimeslots = Arrays.asList(timeslotArray);
        this.dueTime = dueTime;
        this.description = description;
        String[] people = invitedUserEmail.split("\n");
        this.peopleInvited = Arrays.asList(people);
        this.accessType = accessType;
        this.ownerID = ownerID;
    }

    public List<String> getPeopleInvited() {
        return peopleInvited;
    }
    public List<String> getProposedTimeslots() {
        return proposedTimeslots;
    }

    public List<String> showPublicEvents() {
        return new ArrayList<>();
    }

    public String viewDetail() {
        return "";
    }

    public String determineEventTime() {
        return "";
    }
}