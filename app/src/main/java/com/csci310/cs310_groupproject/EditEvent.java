package com.csci310.cs310_groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.csci310.models.Event;
import com.csci310.models.Invitation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;


public class EditEvent extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    //need owner id from the last activity
    String ownerId;
    int event_id;
    private ArrayList<String> PeopleToNotify = new ArrayList<String>();

    int dateButtonClicked;
    int timeButtonClicked;
    private String email;
    boolean emailExists;

    String eventTypeSubmit;
    String locationSubmit;
    String timeslotsSubmit;
    String dueTimeSubmit;
    String descriptionSubmit;
    String invitedUserEmailSubmit;
    String accessTypeSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_invitation);
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        ownerId = bundle.getString("ownerID");
        event_id = bundle.getInt("eventID");
    }


    public class FindUser extends AsyncTask<String, Void, String> {
        String msg = "";
        @Override
        protected String doInBackground(String... strings) {
            emailExists = false;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                if (conn == null) {
                    msg = "failed";
                } else {
                    Log.d("DATACONNECT", "connected");
                    String query = "Select * from Users where email = " + "'" + email + "'";

                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        emailExists = true;
                        return "success";
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
            TextView people = findViewById(R.id.InvitedPeopleListTextView);
            EditText edit = (EditText)findViewById(R.id.inviteEmailEditText);
            System.out.println(emailExists);

            if (emailExists) {
                people.append(email + "\n");
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditEvent.this);
                builder1.setMessage("The user does not exist");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            edit.setText("");
            emailExists = false;
        }
    }

    public class InsertEvent extends AsyncTask<String, Void, String> {
        String msg = "";
        @Override
        protected String doInBackground(String... strings) {
            emailExists = false;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";
                if (conn == null) {
                    msg = "failed";
                } else {
                    Log.d("DATACONNECT", "connected");
                    PreparedStatement ps = null;
                    ps = conn.prepareStatement("update CS310Project.Event set accessType =?, location =?, description=?, eventType=?, dueTime=?, timeDecided=? where eventID = ?;",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, accessTypeSubmit);
                    ps.setString(2, locationSubmit);
                    ps.setString(3, descriptionSubmit);
                    ps.setString(4, eventTypeSubmit);
                    ps.setString(5, dueTimeSubmit);
                    ps.setString(6, null);
                    ps.setInt(7,event_id);

                    int count = ps.executeUpdate();

                    ResultSet rs = ps.getGeneratedKeys();
                    int eventId = event_id;
                    if (rs.next()) {
                        eventId = rs.getInt(1);
                        System.out.println(eventId);
                    }

                    Event event = new Event(eventTypeSubmit, locationSubmit, timeslotsSubmit, dueTimeSubmit,
                            descriptionSubmit, invitedUserEmailSubmit, accessTypeSubmit, ownerId);
                    for (String user_email: event.getPeopleInvited()) {
                        ps = conn.prepareStatement("Insert into CS310Project.Invitation (userID, eventID, acceptStatus) values (?, ?, ?) ;",
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, user_email);
                        ps.setInt(2, event_id);
                        ps.setInt(3, 0);
                        count = ps.executeUpdate();
                        if (count > 0) {
                            System.out.println("insert invitation success");
                            rs = ps.getGeneratedKeys();
                            int invitationID = 0;
                            if (rs.next()) {
                                invitationID = rs.getInt(1);
                                System.out.println(invitationID);
                            }

                            //insert into timeslots
                            for (String timeslot: event.getProposedTimeslots()) {

                                ps = conn.prepareStatement("Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (?, ?, ?, ?);");
                                ps.setInt(1, invitationID);
                                ps.setString(2, timeslot);
                                ps.setInt(3, 0);
                                ps.setInt(4, event_id);
                                count = ps.executeUpdate();
                                if (count > 0) {
                                    System.out.println("insert timeslot success");
                                } else {
                                    System.out.println("insert timeslot failed");
                                }
                            }
                        } else {
                            System.out.println("insert invitation failed");
                        }
                    }

                    ps = conn.prepareStatement("select * from Invitation where eventID = ? and acceptStatus = ?");
                    ps.setInt(1,event_id);
                    ps.setInt(2,1);
                    ResultSet result_temp = ps.executeQuery();
                    while(result_temp.next()){
                        String participant = result_temp.getString("userID");
                        PeopleToNotify.add(participant);
                    }

                    for(int i =0;i<PeopleToNotify.size();i++){
                        ps = conn.prepareStatement("insert into Notification (email,owner,message) value(?,?,?)");
                        ps.setString(1,PeopleToNotify.get(1));
                        ps.setString(2,ownerId);
                        ps.setString(3,"EOne Event you attend was changed, see details in event list");
                        ps.executeUpdate();
                    }

                    Intent i = new Intent(EditEvent.this, ViewActivity.class);
                    i.putExtra("email", ownerId);
                    startActivity(i);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return msg;
        }
    }

    public void cancelCreate(View v) {
        Intent i = new Intent(EditEvent.this, ViewActivity.class);
        i.putExtra("email", ownerId);
        System.out.println(ownerId);
        startActivity(i);
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        dateButtonClicked = 0;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        timeButtonClicked = 0;
    }

    public void showDueTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        timeButtonClicked = 1;
    }

    public void showDueDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        dateButtonClicked = 1;
    }

    public void displayAlertIfEmpty(String alertMessage) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditEvent.this);
        builder1.setMessage(alertMessage);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void submitCreateEvent(View v) {
        Switch simpleSwitch = (Switch) findViewById(R.id.privateSwitch);
        boolean isPrivate = simpleSwitch.isChecked();
        accessTypeSubmit = "public";
        if (isPrivate) {
            accessTypeSubmit = "private";
        }

        EditText editTextLocation = (EditText)findViewById(R.id.editTextLocation);
        locationSubmit = editTextLocation.getText().toString();
        if (locationSubmit.isEmpty()) {
            displayAlertIfEmpty("Location cannot be empty");
            return;
        }

        EditText editTextTextMultiLine = (EditText)findViewById(R.id.editTextTextMultiLine);
        descriptionSubmit = editTextTextMultiLine.getText().toString();
        if (descriptionSubmit.isEmpty()) {
            displayAlertIfEmpty("Other information cannot be empty");
            return;
        }

        Spinner eventTypeSpinner = (Spinner) findViewById(R.id.eventTypeSpinner);
        eventTypeSubmit = String.valueOf(eventTypeSpinner.getSelectedItem());

        TextView dueTimeTextView = findViewById(R.id.dueTimeTextView);
        dueTimeSubmit = dueTimeTextView.getText().toString();
        if (dueTimeSubmit.length() < 15) {
            displayAlertIfEmpty("Please choose due date and time");
            return;
        }

        TextView allTimeSlotsTextView = findViewById(R.id.allTimeSlotsTextView);
        timeslotsSubmit = allTimeSlotsTextView.getText().toString();
        if (timeslotsSubmit.isEmpty()) {
            displayAlertIfEmpty("Please choose at least one event timeslot");
            return;
        }

        TextView invitedPeopleListTextView = findViewById(R.id.InvitedPeopleListTextView);
        invitedUserEmailSubmit = invitedPeopleListTextView.getText().toString();
        if (invitedUserEmailSubmit.isEmpty()) {
            displayAlertIfEmpty("Please invite at least one people");
            return;
        }
        InsertEvent connectMySql = new InsertEvent();
        connectMySql.execute("");
    }


    public void invitePeople(View v) {
        TextView people = findViewById(R.id.InvitedPeopleListTextView);
        EditText edit = (EditText)findViewById(R.id.inviteEmailEditText);
        email = edit.getText().toString();
        FindUser connectMySql = new FindUser();
        connectMySql.execute("");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (dateButtonClicked == 0) {
            TextView timeSlotsChosenTextView = findViewById(R.id.timeSlotsChosenTextView);
            timeSlotsChosenTextView.setText(String.format("%d-%d-%d", year, month+1, day));
        } else {
            TextView dueTimeTextView = findViewById(R.id.dueTimeTextView);
            dueTimeTextView.setText(String.format("%d-%d-%d", year, month+1, day) + "\t");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView timeChosenTextView;
        if (timeButtonClicked == 0) {
            timeChosenTextView = findViewById(R.id.timeChosenTextView);
        } else {
            timeChosenTextView = findViewById(R.id.dueTimeTextView);
        }
        if (hourOfDay < 10) {
            timeChosenTextView.append("0");
        }
        timeChosenTextView.append(String.format("%d:", hourOfDay));
        if (minute < 10) {
            timeChosenTextView.append("0");
        }
        timeChosenTextView.append(String.format("%d:00", minute));
    }


    public void addTimeSlots(View v) {
        TextView allTimeSlotsTextView = findViewById(R.id.allTimeSlotsTextView);
        TextView timeSlotsChosenTextView = findViewById(R.id.timeSlotsChosenTextView);

        TextView timeChosenTextView = findViewById(R.id.timeChosenTextView);
        if (timeSlotsChosenTextView.getText().toString().isEmpty() || timeChosenTextView.getText().toString().isEmpty()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(EditEvent.this);
            builder1.setMessage("Please choose event date and time");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            allTimeSlotsTextView.append(timeSlotsChosenTextView.getText().toString() + "\t");
            allTimeSlotsTextView.append(timeChosenTextView.getText().toString() + "\n");
            timeSlotsChosenTextView.setText("");
            timeChosenTextView.setText("");
        }

    }
}