package com.csci310.models;
import java.util.List;
import java.util.ArrayList;

public class Invitation{
    int invitationID;
    String userEmail;
    int eventID;
    int acceptStatus; //accept = 1, reject = 2, undecided = 0


    public Invitation(int eventID, String userEmail) {
        this.acceptStatus = 0;
        this.eventID = eventID;
        this.userEmail = userEmail;
    }
}