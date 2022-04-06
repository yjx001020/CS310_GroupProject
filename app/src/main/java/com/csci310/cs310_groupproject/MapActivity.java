package com.csci310.cs310_groupproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String[] loc;
    private Button goback;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent data = getIntent();
        String temp = data.getStringExtra("coord");
        System.out.println(temp);
        loc = temp.split(";");
        goback = (Button) findViewById(R.id.button8);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, ViewActivity.class);
                startActivity(i);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        for(int i = 0; i< loc.length; i++){
            int f = Integer.parseInt(loc[i].split(",")[0]);
            int s = Integer.parseInt(loc[i].split(",")[1]);
            LatLng sydney = new LatLng(f, s);
            googleMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Event in Map"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }


    }
}
