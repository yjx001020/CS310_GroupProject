package com.csci310.cs310_groupproject;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.io.PrintWriter;
import java.io.Writer;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import com.google.android.material.chip.Chip;

public class ViewActivity extends AppCompatActivity {
    private ListView listview;
    private boolean success = false;
    private TextView txtData;
    private Button btnFetch;
    private Button btnsign;
    private Button btnmap;
    private Button btncreate;
    SimpleAdapter ADAhere;
    private Map<Integer, String> eventdetail;
    TextView textView;
    String information;
    String idd;
    String allcoord = "";
    private String useremail;
    private String tt = "public";
    TextView tx1;
    List<Date> at;
    long di;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent data = getIntent();
        useremail = data.getStringExtra("email");
        listview = (ListView) findViewById(R.id.eventlist);
        textView = (TextView) findViewById(R.id.detail);
        tx1 = (TextView) findViewById(R.id.timedecided);
        eventdetail = new HashMap<Integer, String>();
        btnFetch = (Button) findViewById(R.id.button3);
        btnsign = (Button) findViewById(R.id.sign);
        btnmap = (Button) findViewById(R.id.button4);
        btncreate = (Button) findViewById(R.id.create);
        listview = (ListView) findViewById(R.id.eventlist);
        InfoAsyncTask connectMySql = new InfoAsyncTask();
        connectMySql.execute("");

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InfoAsyncTask connectMySql = new InfoAsyncTask();
                connectMySql.execute("");
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                String temp = o.toString().split("=")[1];
                String temp2 = temp.replace("}", "");
                String []temp3 = temp2.split(":");
                String temp4 = temp3[1];
                String temp5 = temp4.split(",")[0];
                idd = temp5;

                information = eventdetail.get(Integer.parseInt(temp5));
                String[] temp10 = information.split(",");
                textView.setText("Access Type: " + temp10[0] + "\n" + "Location: " + temp10[1] + "," +
                        temp10[2] + "\n" + "Event Type: " + temp10[3] + "\n" + "Time: " + temp10[4] + "\n" + "OwnerID: " + temp10[5]);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String s = now.toString().replace("T", "");
                s = s.replace("-", "");
                s = s.replace(":", "");
                s = s.replace(".", "");
                long i = Long.parseLong(s);
                String s2 = temp10[4].replace(":", "");
                s2 = s2.replace("-", "");
                s2 = s2.replace(" ", "");
                s2 = s2.replace(".", "");
                long i2 = Long.parseLong(s2);
                System.out.println(i);
                di = i2-i;
                getmode co = new getmode();
                co.execute();

            }
        });
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity2();
            }
        });
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewActivity.this, CreateEventInvitationActivity.class);
                intent.putExtra("email", useremail);
                System.out.println(useremail);
                startActivity(intent);
            }
        });


        Log.d("ONCREATION", "SUCCESS");
    }

    public void openNewActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("email", useremail);
        intent.putExtra("info", information);
        intent.putExtra("id", idd);
        startActivity(intent);
    }
    public void openNewActivity2(){
        Intent intent = new Intent(this, MapActivity.class);
        for (Map.Entry<Integer, String> entry : eventdetail.entrySet()) {
            String v = entry.getValue();
            String[] temp = v.split(",");
            allcoord += temp[1] +","+ temp[2];
            allcoord += ";";
        }
        System.out.println(allcoord);
        intent.putExtra("coord", allcoord);

        startActivity(intent);
    }

    public class InfoAsyncTask extends AsyncTask<String, Void, String> {
        String res = "";
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Toast.makeText(ViewActivity.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();
        }
        @Override
        protected String doInBackground(String...strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password="  + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";

                if(conn == null){
                    success = false;
                }else{
                    Log.d("DATACONNECT", "connected");
                    String query = "SELECT eventID, accessType, location, eventType, dueTime, ownerEmail FROM Event WHERE accessType = " + "'" + tt + "'";
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    List<Map<String, String>> data = null;
                    data = new ArrayList<Map<String, String>>();
                    if(rs != null){
                        while(rs.next()){
                            String temp = "";
                            try{
                                Map<String, String> datanum = new HashMap<String, String>();
                                datanum.put("A", "Event:" + rs.getInt(1) + ", Click to View Details");
                                temp += rs.getString(2).toString() + "," + rs.getString(3).toString()  + "," +  rs.getString(4).toString()  + "," +  rs.getString(5).toString()
                                        + "," +  rs.getString(6);
                                eventdetail.put(rs.getInt(1), temp);
                                data.add(datanum);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                        String[] fromwhere = { "A" };
                        int[] viewswhere = { R.id.textView4 };
                        ADAhere = new SimpleAdapter(ViewActivity.this, data,
                                R.layout.activity_listelement, fromwhere, viewswhere);
                        while (rs.next()) {
                            result += rs.getString(1).toString() + "\n";
                        }
                        res = result;
                        success = true;
                    }else{
                        success = false;
                    }

                }

            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
                res = throwables.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            listview.setAdapter(ADAhere);
        }
    }
    public class getmode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";

                if (conn == null) {
                    success = false;
                } else {
                    Log.d("DATACONNECT", "connected");
                    String query = "SELECT timeslots FROM Timeslots WHERE eventID = " + idd;
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    at = new ArrayList<Date>();
                    while (rs.next()) {
                        at.add(rs.getDate("timeslots"));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String result){
            System.out.println(idd);
            if (di > 0) {
                tx1.setText("Undecided!");
            }else{
                tx1.setText(at.get(0).toString());
            }
        }

    }
}
