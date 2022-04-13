package com.csci310.models;

import junit.framework.TestCase;

public class UserTest extends TestCase {

    public void testGetUserSQL() {
        User user = new User();
        String query = user.getUserSQL("tony@usc.edu");
        assertEquals("Select * from Users where email = 'tony@usc.edu'", query);
    }
}