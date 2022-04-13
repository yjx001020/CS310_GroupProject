package com.csci310.cs310_groupproject;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MapActivityTest2 {
    String input;
    String input2;
    private List<LatLng> all = new ArrayList<>();
    private List<LatLng> all2 = new ArrayList<>();
    List<LatLng> testcase = new ArrayList<>();

    @Before
    public void setup(){
        input = "23,45;67,89;34,118";
        input2 = "23,45;23,45;34,118";
        LatLng data1 = new LatLng(23, 45);
        LatLng data2 = new LatLng(67, 89);
        LatLng data3 = new LatLng(34, 118);
        all.add(data1);
        all.add(data2);
        all.add(data3);
        LatLng d1 = new LatLng(23, 45);
        LatLng d2 = new LatLng(23, 45);
        LatLng d3 = new LatLng(34, 118);
        all2.add(d1);
        all2.add(d2);
        all2.add(d3);
    }

    @Test
    public void MapActivityTest(){
        testcase = MapActivity.processingloc(input);
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i), testcase.get(i));
        }
    }
    @Test
    public void MapActivityTest2(){
        testcase = MapActivity.processingloc(input2);
        for(int i = 0; i < all.size(); i++){
            assertEquals(all2.get(i), testcase.get(i));
        }
    }
}