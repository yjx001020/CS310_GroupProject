package com.csci310.cs310_groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUp extends AppCompatActivity {
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private Button btn;
    String username;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //comment
        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.email);
        et3 = (EditText) findViewById(R.id.password);
        btn = (Button) findViewById(R.id.signup_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et1.getText().toString();
                email = et2.getText().toString();
                password = et3.getText().toString();
                Signupadd connectMySql = new Signupadd();
                connectMySql.execute("");
            }
        });
    }
    public class Signupadd extends AsyncTask<String, Void, String>{
        String success;
        @Override
        protected String doInBackground(String...strings){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";
                if(conn == null){
                    success = "failed";
                }else{
                    Log.d("DATACONNECT", "connected");
                    String sql = "INSERT INTO Users(email, UserPassword) "
                            + "VALUES(?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, email);
                    pstmt.setString(2, password);
                    pstmt.executeUpdate();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return success;
        }
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SignUp.this, ViewActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }

    }
}