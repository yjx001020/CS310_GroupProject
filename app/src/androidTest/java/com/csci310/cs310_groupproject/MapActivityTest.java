package com.csci310.cs310_groupproject;

import static org.junit.Assert.*;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.filters.SmallTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import androidx.test.rule.ActivityTestRule;
import androidx.test.core.app.ApplicationProvider;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class MapActivityTest {
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), MapActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("coord", "23,45;67,89;34,118");
    }
    @Rule
    public ActivityScenarioRule<MapActivity> map =
            new ActivityScenarioRule(intent);
    String input;
    private List<LatLng> all = new ArrayList<>();
    List<LatLng> testcase = new ArrayList<>();

    @Before
    public void setup(){
        LatLng data1 = new LatLng(23, 45);
        LatLng data2 = new LatLng(67, 89);
        LatLng data3 = new LatLng(34, 118);
        all.add(data1);
        all.add(data2);
        all.add(data3);

    }

    @Test
    public void MapActivityTest(){
        map.getScenario().onActivity(activity -> {
            testcase = activity.processingloc(MapActivity.newinput);});
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i), testcase.get(i));
        }
    }
}