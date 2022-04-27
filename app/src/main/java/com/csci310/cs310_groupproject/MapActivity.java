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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MapActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {
    private String[] loc;
    private Button goback;
    private String info;
    String email = "";
    List<LatLng> all = new ArrayList<>();
    static String newinput;
    TextView tw;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent data = getIntent();
        tw = findViewById(R.id.textView12);
        String temp = data.getStringExtra("coord");
        email = data.getStringExtra("email");
        info = data.getStringExtra("info");
        System.out.println(temp);
//        loc = temp.split(";");
        all = processingloc(temp);
        goback = (Button) findViewById(R.id.button8);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, ViewActivity.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        for(int i = 0; i< all.size(); i++){
            googleMap.addMarker(new MarkerOptions()
                    .position(all.get(i))
                    .title("Event in Map"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(all.get(i)));
        }
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getBaseContext(), marker.getTitle() +
                " has been clicked ", Toast.LENGTH_LONG).show();
        tw.setText(info);
        return false;
    }

    static public List<LatLng> processingloc(String ss){
        String [] tloc;
        if(ss != null){
            tloc = ss.split(";");
        }else{
            tloc = MapActivity.newinput.split(";");
        }
        List<LatLng> tt = new ArrayList<>();
        for(int i = 0; i< tloc.length; i++) {
            int f = Integer.parseInt(tloc[i].split(",")[0]);
            int s = Integer.parseInt(tloc[i].split(",")[1]);
            LatLng sydney = new LatLng(f, s);
            tt.add(sydney);
        }
        return tt;
    }

}
