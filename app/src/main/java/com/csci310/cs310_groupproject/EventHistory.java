package com.csci310.cs310_groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.csci310.models.Event;
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
    private ArrayList<Button> acceptButtons = new ArrayList<>();
    private ArrayList<Button> declineButtons = new ArrayList<>();
    private ArrayList<Button> withdrawButtons = new ArrayList<>();
    private ArrayList<Button> editButtons = new ArrayList<>();
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


    }//end onCreate

    public class PrintEvent extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                if (conn == null) {

                } else {
                    PreparedStatement st = conn.prepareStatement("select * from Invitation left join Event on Invitation.eventID = Event.eventID where acceptStatus !=2 and userID =?");
                    st.setString(1, user_id);
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        String userID = rs.getString("userID");
                        int event_id = rs.getInt("eventID");
                        String invitation_id = rs.getString("invitationID");
                        int accept_status = rs.getInt("acceptStatus");
                        String description = rs.getString("Event.description");
                        String owner = rs.getString("Event.ownerEmail");
                        System.out.println("user: "+ user_id);
                        System.out.println("owner: "+ owner);
                        String acceptStatus = String.valueOf(accept_status);
                        if (accept_status == 1) {
                            if(user_id != owner){
                                ArrayList<String> temp = new ArrayList<>();
                                temp.add(userID);
                                temp.add(Integer.toString(event_id));
                                temp.add(invitation_id);
                                temp.add(description);
                                temp.add(owner);
                                History.add(temp);
                            }
                        } else if(accept_status == 0){
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
                    st.setString(1, user_id);
                    ResultSet result = st.executeQuery();
                    while (result.next()) {
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout list = findViewById(R.id.HistoryList);
                    LinearLayout buffer = new LinearLayout(EventHistory.this);
                    buffer.setOrientation(LinearLayout.HORIZONTAL);
                    TextView bufferText = new TextView(EventHistory.this);
                    bufferText.setText("           \n");
                    buffer.addView(bufferText);
                    list.addView(buffer);
                    for (int i = 0; i < History.size(); i++) {
                        LinearLayout event = new LinearLayout(EventHistory.this);
                        event.setOrientation(LinearLayout.HORIZONTAL);
                        TextView tx = new TextView(EventHistory.this);
                        tx.setText("    (Withdraw)Event: " + History.get(i).get(3));
                        int index = i;
                        Button withdraw = new Button(EventHistory.this);
                        withdraw.setText("Withdraw");
                        withdraw.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = History.get(index).get(1);
                                mIndex = index;
                                WithdrawEvent withdraw_event = new WithdrawEvent();
                                withdraw_event.execute();
//                                LinearLayout temp = findViewById(R.id.HistoryList);
//                                temp.removeAllViewsInLayout();
//                                PrintEvent print = new PrintEvent();
//                                print.execute("");
                            }
                        });
                        withdrawButtons.add(withdraw);
                        tx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = History.get(index).get(1);
                                mIndex = index;
                            }
                        });
                        event.addView(tx);
                        event.addView(withdraw);
                        list.addView(event);
                    }


                    for (int i = 0; i < Invitations.size(); i++) {
                        list = findViewById(R.id.HistoryList);
                        LinearLayout event = new LinearLayout(EventHistory.this);
                        event.setOrientation(LinearLayout.HORIZONTAL);
                        TextView tx = new TextView(EventHistory.this);
                        tx.setText("    (Accept or Decline)Event: " + Invitations.get(i).get(3)+" ");//get event description
                        Button accept = new Button(EventHistory.this);
                        Button decline = new Button(EventHistory.this);
                        accept.setText("Accept");
                        decline.setText("Decline");
                        int index = i;
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = Invitations.get(index).get(1);
                                AcceptInvitation accept_invitation = new AcceptInvitation();
                                accept_invitation.execute();
//                                LinearLayout temp = findViewById(R.id.HistoryList);
//                                temp.removeView(event);
                            }
                        });

                        decline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = Invitations.get(index).get(1);
                                DeclineInvitation decline_invitation = new DeclineInvitation();
                                decline_invitation.execute();
//                                LinearLayout temp = findViewById(R.id.HistoryList);
//                                temp.removeAllViewsInLayout();
//                                PrintEvent print = new PrintEvent();
//                                print.execute("");
                            }
                        });
                        tx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = Invitations.get(index).get(1);
                            }
                        });
                        acceptButtons.add(accept);
                        declineButtons.add(decline);
                        event.addView(tx);
                        event.addView(accept);
                        event.addView(decline);
                        list.addView(event);
                    }

                    for (int i = 0; i < OwnEvent.size(); i++) {
                        list = findViewById(R.id.HistoryList);
                        LinearLayout event = new LinearLayout(EventHistory.this);
                        event.setOrientation(LinearLayout.HORIZONTAL);
                        TextView tx = new TextView(EventHistory.this);
                        tx.setText("       (Edit)Event: " + OwnEvent.get(i).get(1));//get description
                        int index = i;
                        Button edit = new Button (EventHistory.this);
                        edit.setText("Edit");
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = OwnEvent.get(index).get(1);
                                EditOwnEvent edit_own = new EditOwnEvent();
                                edit_own.execute();
                                PrintEvent print = new PrintEvent();
                                print.execute("");
                            }
                        });
                        tx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                event_id = OwnEvent.get(index).get(1);
                            }
                        });
                        event.addView(tx);
                        event.addView(edit);
                        list.addView(event);
                    }

                }
            });


            return null;
        }


        @Override
        protected void onPostExecute(String result) {


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