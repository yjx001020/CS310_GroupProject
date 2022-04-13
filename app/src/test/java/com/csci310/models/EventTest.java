package com.csci310.models;

import com.csci310.cs310_groupproject.CreateEventInvitationActivity;

import junit.framework.TestCase;

import java.util.List;

public class EventTest extends TestCase {
    Event event;
    public void setUp() throws Exception {
        super.setUp();
        event = new Event("study group", "12,118", "2022-4-11\t22:21:00\n2022-4-11\t20:21:00\n", "2022-4-6\t00:00:00", "csci 270", "trojan@usc.edu\ntony@usc.edu\n", "public", "taylor@usc.edu");
    }

    public void testGetPeopleInvited() {
        assertEquals("trojan@usc.edu", event.getPeopleInvited().get(0));
        assertEquals("tony@usc.edu", event.getPeopleInvited().get(1));
    }

    public void testGetProposedTimeslots() {
        List<String> proposedTimeslots = event.getProposedTimeslots();
        assertEquals("2022-4-11\t22:21:00", proposedTimeslots.get(0));
        assertEquals("2022-4-11\t20:21:00", proposedTimeslots.get(1));
    }

    public void testFormatDate() {
        assertEquals("2022-1-31", Event.formatDate(2022, 0, 31));
        assertEquals("2022-4-1", Event.formatDate(2022, 3, 1));
    }

    public void testFormatTime() {
        assertEquals("00:31:00", Event.formatTime(0, 31));
        assertEquals("09:08:00", Event.formatTime(9, 8));
    }
}