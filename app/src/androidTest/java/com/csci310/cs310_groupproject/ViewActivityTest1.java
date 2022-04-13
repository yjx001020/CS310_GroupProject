package com.csci310.cs310_groupproject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotEquals;

public class ViewActivityTest1 {
    List<java.sql.Date> alldate = new ArrayList();
    List<java.sql.Date> alldate2 = new ArrayList();
    java.sql.Date result;
    java.sql.Date result2;
    java.sql.Date result3;

    @Before
    public void setup(){
        String date_string1 = "1989-09-26";
        String date_string2 = "1989-09-26";
        String date_string3 = "2000-09-26";
        String date_string4 = "2000-09-26";
        String date_string5 = "2010-09-26";
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
            Date date5 = formatter.parse(date_string5);
            java.sql.Date d5 = new java.sql.Date(date5.getTime());
            alldate.add(d1);
            alldate.add(d2);
            alldate.add(d3);
            alldate.add(d4);
            alldate2.add(d3);
            alldate2.add(d4);
            alldate2.add(d5);
            result = d1;
            result2 = d3;
            result3 = d5;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void GetModeDateTest(){
        java.sql.Date test = ViewActivity.Getmost(alldate);
        assertThat(test, anyOf(is(result), is(result2)));
    }
    @Test
    public void GetModeDateTest2(){
        java.sql.Date test = ViewActivity.Getmost(alldate2);
        assertNotEquals(test, result3);
    }

}