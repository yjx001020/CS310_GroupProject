package com.csci310.cs310_groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.*;
import java.util.ArrayList;
import android.widget.Button;
public class Notifications extends AppCompatActivity {
    private String user_id;
    Connection conn = null;
    private ArrayList<String> notifications;
    private Button BackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("email");
        notifications = new ArrayList<>();
        BackButton = findViewById(R.id.Notification_back);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notifications.this,EventHistory.class);
                intent.putExtra("email",user_id);
                startActivity(intent);
            }
        });
        PrintNotification print = new PrintNotification();
        print.execute();
    }

    public class PrintNotification extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password="+ MainActivity.PASSWORD);
                PreparedStatement st = conn.prepareStatement("select * from Notification where email =?");
                st.setString(1, user_id);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    String msg = rs.getString("message");
                    notifications.add(msg);
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout list = findViewById(R.id.notificationList);
                    if(notifications.size()>0){
                        for(int i =0;i<notifications.size();i++){
                            TextView tx = new TextView(Notifications.this);
                            tx.setText(notifications.get(i));
                            list.addView(tx);
                        }
                    }

                }
            });
        }
    }
}