package com.csci310.cs310_groupproject;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.csci310.models.History;

import org.junit.Before;
import org.junit.runner.RunWith;
import junit.framework.TestCase;


public class EventHistoryTest extends TestCase{

    private History event_history;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        event_history = new History("trojan@usc.edu");
    }

    public void testAccept(){
        assertEquals("1",event_history.accept());
    }

    public void testDecline(){
        assertEquals("2",event_history.decline());
    }

    public void testWithdraw(){
        assertEquals("2",event_history.withdraw());
    }

    public void testPrint(){
        event_history.printEvent();
        assertEquals(1,event_history.GetHistorySize());
        assertEquals(2,event_history.GetOwnEventSize());
        assertEquals(1,event_history.GetInvitationSize());
    }

    public void testEdit(){
        assertEquals("trojan@usc.edu",event_history.Edit());
    }

}