package com.csci310.cs310_groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.csci310.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileActivity extends AppCompatActivity {
    User user = new User();
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent data = getIntent();
        user.email = data.getStringExtra("email");

        TextView emailProfileTextView = findViewById(R.id.emailProfileTextView);
        emailProfileTextView.setText(user.email);
        GetUserInfo connectMySql = new GetUserInfo();
        connectMySql.execute("");
    }


    public void updateInfo(View view) {
        EditText firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);
        user.fname = firstNameEditText.getText().toString();
        EditText lastNameEditText = (EditText)findViewById(R.id.lastNameEditText);
        user.lname = lastNameEditText.getText().toString();
        EditText majorEditText = (EditText)findViewById(R.id.majorEditText);
        user.major = majorEditText.getText().toString();
        EditText studyYearEditText = (EditText)findViewById(R.id.studyYearEditText);
        user.studyYear = studyYearEditText.getText().toString();
        UpdateUserInfo connectMySql = new UpdateUserInfo();
        connectMySql.execute("");
        //dismiss keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void goToEventHistory(View view) {
        Intent i = new Intent(ProfileActivity.this, EventHistory.class);
        i.putExtra("email",user.email);
        startActivity(i);
    }

    public void goToViewActivity(View view) {
        Intent intent = new Intent(ProfileActivity.this, ViewActivity.class);
        intent.putExtra("email", user.email);
        startActivity(intent);
    }

    public void logOut(View view){
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public class UpdateUserInfo extends AsyncTask<String, Void, String> {
        String msg = "";
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password="  + MainActivity.PASSWORD);
                if (conn == null) {
                    msg = "failed";
                } else {
                    Log.d("DATACONNECT", "connected");
                    PreparedStatement ps = conn.prepareStatement("Update Users Set Fname = ?, Lname = ?, studyYear = ?, major = ? where id = ?;");
                    ps.setString(1, user.fname);
                    ps.setString(2, user.lname);
                    ps.setString(3, user.studyYear);
                    ps.setString(4, user.major);
                    ps.setInt(5, userId);
                    int count = ps.executeUpdate();
                    if (count > 0) {
                        System.out.println("update user info success");
                        msg = "success";
                    } else {
                        System.out.println("update user info failed");
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
            if (msg == "success") {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
                builder1.setMessage("Your information has been updated!");
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
        }
    }

    public class GetUserInfo extends AsyncTask<String, Void, String> {
        String msg = "";
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password="  + MainActivity.PASSWORD);
                if (conn == null) {
                    msg = "failed";
                } else {
                    Log.d("DATACONNECT", "connected");
                    String query = user.getUserSQL(user.email);
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        userId = rs.getInt("id");
                        System.out.println(userId);
                        user.fname = rs.getString("Fname");
                        user.lname = rs.getString("Lname");
                        user.studyYear = rs.getString("studyYear");
                        user.major = rs.getString("major");
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

            EditText firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);
            firstNameEditText.setText(user.fname);

            EditText lastNameEditText = (EditText)findViewById(R.id.lastNameEditText);
            lastNameEditText.setText(user.lname);

            EditText majorEditText = (EditText)findViewById(R.id.majorEditText);
            majorEditText.setText(user.major);

            EditText studyYearEditText = (EditText)findViewById(R.id.studyYearEditText);
            studyYearEditText.setText(user.studyYear);

        }
    }
}