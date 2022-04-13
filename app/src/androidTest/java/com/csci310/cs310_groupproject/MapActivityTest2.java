package com.csci310.cs310_groupproject;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MapActivityTest2 {
    String input;
    private List<LatLng> all = new ArrayList<>();
    List<LatLng> testcase = new ArrayList<>();

    @Before
    public void setup(){
        input = "23,45;67,89;34,118";
        LatLng data1 = new LatLng(23, 45);
        LatLng data2 = new LatLng(67, 89);
        LatLng data3 = new LatLng(34, 118);
        all.add(data1);
        all.add(data2);
        all.add(data3);
    }

    @Test
    public void MapActivityTest(){
        testcase = MapActivity.processingloc(input);
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i), testcase.get(i));
        }
    }
}