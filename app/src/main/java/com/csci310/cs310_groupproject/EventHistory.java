package com.csci310.cs310_groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.csci310.models.Invitation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class EventHistory extends AppCompatActivity {


    private String user_id;
    private String event_id;
    private Button BackHome;
    private ArrayList<ArrayList<String>> Invitations = new ArrayList<>();
    private ArrayList<ArrayList<String>> History = new ArrayList<>();
    private ArrayList<ArrayList<String>> OwnEvent = new ArrayList<>();
    private Button AcceptButton;
    private Button DeclineButton;
    private Button EditButton;
    private Button WithdrawButton;
    private Button NotificationButton;
    private int mIndex;

    Connection conn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);
        Intent data = getIntent();
        user_id = data.getStringExtra("email");
        PrintEvent mysql = new PrintEvent();
        mysql.execute("");
        BackHome = (Button) findViewById(R.id.History_to_home_button);
        AcceptButton = new Button(EventHistory.this);
        AcceptButton.setText("Accept");
        DeclineButton= new Button(EventHistory.this);
        DeclineButton.setText("Decline");
        EditButton = new Button(EventHistory.this);
        EditButton.setText("Edit");
        WithdrawButton = new Button(EventHistory.this);
        WithdrawButton.setText("Withdraw");
        NotificationButton = findViewById(R.id.Notification);
        NotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventHistory.this,Notifications.class);
                intent.putExtra("email",user_id);
                startActivity(intent);
            }
        });

        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventHistory.this,ProfileActivity.class);
                intent.putExtra("email", user_id);
                startActivity(intent);
            }
        });
        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptInvitation accept = new AcceptInvitation();
                accept.execute();
            }
        });

        DeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeclineInvitation decline = new DeclineInvitation();
                decline.execute();
            }
        });

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditOwnEvent edit = new EditOwnEvent();
                edit.execute();
            }
        });

        WithdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WithdrawEvent withdraw = new WithdrawEvent();
                withdraw.execute();
            }
        });


    }//end onCreate

    public class PrintEvent extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password="+ MainActivity.PASSWORD);
                if (conn == null) {

                } else {
                    PreparedStatement st = conn.prepareStatement("select * from Invitation left join Event on Invitation.eventID = Event.eventID where acceptStatus !=2 and userID =?");
                    st.setString(1,user_id);
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        String userID = rs.getString("userID");
                        int event_id = rs.getInt("eventID");
                        String invitation_id = rs.getString("invitationID");
                        System.out.println(invitation_id);
                        String accept_status = rs.getString("acceptStatus");
                        String description = rs.getString("Event.description");
                        String owner = rs.getString("Event.ownerEmail");
                        int acceptStatus = Integer.parseInt(accept_status);
                        if(acceptStatus == 1 && !userID.equals(owner)){
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(userID);
                            temp.add(Integer.toString(event_id));
                            temp.add(invitation_id);
                            temp.add(description);
                            temp.add(owner);
                            History.add(temp);
                        }
                        else {
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(userID);
                            temp.add(Integer.toString(event_id));
                            temp.add(invitation_id);
                            temp.add(description);
                            Invitations.add(temp);
                        }

                    }//end while

                    //fine events created by the user.
                    st = conn.prepareStatement("select * from Event where ownerEmail = ?");
                    st.setString(1,user_id);
                    ResultSet result = st.executeQuery();
                    while(result.next()){
                        String ownerID = result.getString("ownerEmail");
                        int eventID = result.getInt("eventID");
                        String description = result.getString("description");
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(ownerID);
                        temp.add(Integer.toString(eventID));
                        temp.add(description);
                        OwnEvent.add(temp);
                    }



                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout list = findViewById(R.id.HistoryList);
                    LinearLayout buttons = new LinearLayout(EventHistory.this);
                    buttons.setOrientation(LinearLayout.HORIZONTAL);
                    buttons.addView((AcceptButton));
                    buttons.addView((DeclineButton));
                    buttons.addView((EditButton));
                    buttons.addView((WithdrawButton));
                    list.addView(buttons);
                    for(int i = 0;i<History.size();i++){
                        LinearLayout event = new LinearLayout(EventHistory.this);
                        event.setOrientation(LinearLayout.HORIZONTAL);
                        TextView tx = new TextView(EventHistory.this);
                        tx.setText(" (Withdraw)Event: " + History.get(i).get(3));
                        int index = i;
                        tx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = History.get(index).get(1);
                                mIndex = index;
                            }
                        });
                        event.addView(tx);
                        list.addView(event);
                    }


                    for(int i = 0; i< Invitations.size(); i++){
                        list = findViewById(R.id.HistoryList);
                        LinearLayout event = new LinearLayout(EventHistory.this);
                        event.setOrientation(LinearLayout.HORIZONTAL);
                        TextView tx = new TextView(EventHistory.this);
                        tx.setText(" (Accept or Decline)Event: " + Invitations.get(i).get(3));//get event description
                        int index = i;
                        tx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = Invitations.get(index).get(1);
                            }
                        });
                        event.addView(tx);
                        list.addView(event);
                    }

                    for(int i = 0;i<OwnEvent.size();i++){
                        list = findViewById(R.id.HistoryList);
                        LinearLayout event = new LinearLayout(EventHistory.this);
                        event.setOrientation(LinearLayout.HORIZONTAL);
                        TextView tx = new TextView(EventHistory.this);
                        tx.setText(" (Edit)Event: " + OwnEvent.get(i).get(1));//get description
                        int index = i;
                        tx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = OwnEvent.get(index).get(1);
                            }
                        });
                        event.addView(tx);

                        list.addView(event);
                    }

                }
            });

        }

    }



    public class AcceptInvitation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("update Invitation set acceptStatus = 1 where userID = ? and eventID = ?");
                ps.setString(1,user_id);
                ps.setInt(2,Integer.parseInt(event_id));
                ps.executeUpdate();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
    }

    public class DeclineInvitation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                PreparedStatement ps = conn.prepareStatement("update Invitation set acceptStatus = 2 where userID = ? and eventID = ?");
                ps.setString(1,user_id);
                ps.setInt(2,Integer.parseInt(event_id));
                ps.executeUpdate();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
    }


    public class WithdrawEvent extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("update Invitation set acceptStatus = 2 where userID = ? and eventID = ?");
                ps.setString(1,user_id);
                ps.setInt(2,Integer.parseInt(event_id));
                ps.executeUpdate();
                ps = conn.prepareStatement("insert into Notification(email,owner,message) values(?,?,?)");
                ps.setString(1,History.get(mIndex).get(4));
                ps.setString(2,user_id);
                ps.setString(3,History.get(mIndex).get(4)+" withdraw from " + History.get(mIndex).get(3));
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
    }

    public class EditOwnEvent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String ownerID = user_id;
            String eventID = event_id;
            Intent intent = new Intent(EventHistory.this,EditEvent.class);
            intent.putExtra("ownerID",user_id);
            intent.putExtra("eventID",Integer.parseInt(eventID));
            startActivity(intent);
            return null;
        }
    }
}