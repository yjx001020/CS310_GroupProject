package com.csci310.cs310_groupproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private String idd;
    private String info;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private Button btnSub;
    private Button btnren;
    private String timeslot;
    private ArrayList<String> times;
    private Spinner mySpinner;
    private String ddate;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsign);
        Intent data = getIntent();
        userid = data.getStringExtra("email");
        mySpinner = (Spinner) findViewById(R.id.spinner);
        btnSub = (Button)findViewById(R.id.button2);
        btnren = (Button)findViewById(R.id.returnback);
        idd = data.getStringExtra("id");
        info = data.getStringExtra("info");
        String alldata[] = info.split(",");
        textView1 = (TextView) findViewById(R.id.textView5);
        textView1.setText("EventID: " + idd);
        textView2 = (TextView) findViewById(R.id.textView6);
        textView2.setText("Type: " + alldata[0]);
        textView3 = (TextView) findViewById(R.id.textView7);
        textView3.setText("Location: " + "(" + alldata[1] + ", " + alldata[2] + ")");
        textView4 = (TextView) findViewById(R.id.textView8);
        textView4.setText("EventType: " + alldata[3]);
        Adddata connectMySql = new Adddata();
        connectMySql.execute("");
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                ddate = mySpinner.getSelectedItem().toString();
                textView5 = (TextView) findViewById(R.id.textView9);
                textView5.setText("Time: " + ddate);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Senddata cm = new Senddata();
                cm.execute("");
                Intent i = new Intent(SignupActivity.this, ViewActivity.class);
                i.putExtra("email", userid);
                startActivity(i);
            }
        });
        btnren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, ViewActivity.class);
                i.putExtra("email", userid);
                startActivity(i);
            }
        });
    }
    public class Adddata extends AsyncTask<String, Void, String> {
        String msg = "";
        @Override
        protected String doInBackground(String...strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password="  + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";
                if(conn == null){
                    msg = "failed";
                }else{
                    Log.d("DATACONNECT", "connected");
                    String query = "SELECT timeslots FROM Timeslots WHERE eventID = " + idd + " AND chosen = " + 0;
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    times = new ArrayList<String>();
                    while(rs.next()){
                        times.add(rs.getString(1).toString());
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return msg;
        }
        @Override
        protected void onPostExecute(String result) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignupActivity.this,
                    android.R.layout.simple_spinner_item, times);
            adapter.setDropDownViewResource(R.layout.spinner_value);
            mySpinner.setAdapter(adapter);
        }
    }
    public class Senddata extends AsyncTask<String, Void, String>{
        String msg = "";
        @Override
        protected String doInBackground(String...strings){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";
                if(conn == null){
                    msg = "failed";
                }else{
                    Log.d("DATACONNECT", "connected");
                    String sql = "INSERT INTO Invitation(userID, eventID, acceptStatus) "
                            + "VALUES(?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, userid);
                    pstmt.setInt(2, Integer.parseInt(idd));
                    pstmt.setInt(3, 1);
                    pstmt.executeUpdate();

                    String sql2 = "INSERT INTO Notification(email, owner, message) "
                            + "VALUES(?,?, ?)";
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2,
                            Statement.RETURN_GENERATED_KEYS);
                    pstmt2.setString(1, userid);
                    pstmt2.setString(2, idd);
                    pstmt2.setString(3, "new signup");
                    pstmt2.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();
                    int invitationID = 0;
                    if (rs.next()) {
                        invitationID = rs.getInt(1);
                        System.out.println(invitationID);
                    }
                    PreparedStatement ps = null;
                    ps = conn.prepareStatement("Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (?, ?, ?, ?);");
                    ps.setInt(1, invitationID);
                    ps.setString(2, ddate);
                    ps.setInt(3, 1);
                    ps.setInt(4, Integer.parseInt(idd));
                    int count = ps.executeUpdate();
                    if (count > 0) {
                        System.out.println("insert timeslot success");
                    } else {
                        System.out.println("insert timeslot failed");
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return msg;
        }
    }
}
