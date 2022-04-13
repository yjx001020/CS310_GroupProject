package com.csci310.cs310_groupproject;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

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
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

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
    static String idd;
    String allcoord = "";
    private String useremail;
    private String tt = "public";
    static TextView tx1;
    List<Date> at = new ArrayList<>();
    static long di;
    private Button btnprofile;
    Connection conn = null;



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
        btnprofile = (Button) findViewById(R.id.backtoprofile);
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
                System.out.println(i2);
                di = i2-i/100;
                Callgetmode(conn, idd);
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
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewActivity.this, ProfileActivity.class);
                intent.putExtra("email", useremail);
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
        intent.putExtra("email", useremail);

        startActivity(intent);
    }

    public class InfoAsyncTask extends AsyncTask<String, Void, String> {
        String res = "";
        boolean success;
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

    public String Callgetmode(Connection cc, String id){
        String re = null;
        try {
            getmode co = new getmode(cc, id);
            co.execute();
            re = co.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(re);
        return re;
    }


    public class getmode extends AsyncTask<String, Void, String> {
        Connection conn;
        String nid;
        String result;
        public getmode(Connection c, String s) {
            this.conn = c;
            this.nid = s;
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";
//                MakeConnection(conn);

                if (conn == null) {
                } else {
                    Log.d("DATACONNECT", "connected");
                    String query = "SELECT timeslots FROM Timeslots WHERE eventID = " + nid;
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    at = new ArrayList<Date>();
                    while (rs.next()) {
                        at.add(rs.getDate("timeslots"));
                        result += rs.getDate("timeslots").toString();
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            System.out.println(at.size());
            Date re = Getmost(at);
            System.out.println(idd);
            if (di > 0) {
                tx1.setText("Undecided!");
            }else{
                tx1.setText(re.toString());
            }
        }

    }
    static public Date Getmost(List<Date> at){
        String result;
        int max = 0;
        int index = 0;
        for(int i = 0; i < at.size(); i++){
            Date temp = at.get(i);
            System.out.println(temp);
            int count = 0;
            for(int j = 0; j < at.size(); j++){
                if(temp.toString().equals(at.get(j).toString())){
                    count++;
                }
            }
            System.out.println(count);
            if(count > max){
                max = count;
                index = i;
            }
        }
        return at.get(index);
    }
//    static String MakeConnection(Connection conn){
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        String result = "Database Connection Successful\n";
//        return result;
//    }

}
