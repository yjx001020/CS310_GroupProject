package com.csci310.cs310_groupproject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewActivityTest1 {
    List<java.sql.Date> alldate = new ArrayList();
    java.sql.Date result;

    @Before
    public void setup(){
        String date_string1 = "1989-09-26";
        String date_string2 = "1989-09-26";
        String date_string3 = "2000-09-26";
        String date_string4 = "2000-09-26";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = formatter.parse(date_string1);
            java.sql.Date d1 = new java.sql.Date(date1.getTime());
            Date date2 = formatter.parse(date_string2);
            java.sql.Date d2 = new java.sql.Date(date2.getTime());
            Date date3 = formatter.parse(date_string3);
            java.sql.Date d3 = new java.sql.Date(date3.getTime());
            Date date4 = formatter.parse(date_string4);
            java.sql.Date d4 = new java.sql.Date(date4.getTime());
            alldate.add(d1);
            alldate.add(d2);
            alldate.add(d3);
            alldate.add(d4);
            result = d1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void GetModeDateTest(){
        java.sql.Date test = ViewActivity.Getmost(alldate);
        assertEquals(test, result);
    }

}