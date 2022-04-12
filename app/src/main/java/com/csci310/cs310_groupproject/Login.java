package com.csci310.cs310_groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    private EditText et1;
    private EditText et2;
    private TextView tx;
    private Button btn;
    String email;
    String password;
    boolean s = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et1 = (EditText) findViewById(R.id.editTextTextEmailAddress);
        et2 = (EditText) findViewById(R.id.editTextTextPassword);
        tx = (TextView) findViewById(R.id.warn);
        btn = (Button) findViewById(R.id.buttonlog);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et1.getText().toString();
                password = et2.getText().toString();
                Logadd connectMySql = new Logadd();
                connectMySql.execute("");
            }
        });
    }
    public class Logadd extends AsyncTask<String, Void, String>{
        String success = "";
        @Override
        protected String doInBackground(String...strings){
            s = false;
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
                String result = "Database Connection Successful\n";
                if(conn == null){
                    success = "failed";
                }else{
                    Log.d("DATACONNECT", "connected");
                    String query = "SELECT * FROM Users WHERE email = " + "'" + email + "'" + " AND " + "UserPassword = " + "'" + password + "'";
                    System.out.println(query);
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    if (rs.next()){
                        s = true;
                    }else{
                        s = false;
                    }
                    System.out.println(s);
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
            if(s){
                tx.setText("Success Login");
                Intent intent = new Intent(Login.this, ProfileActivity.class);
                intent.putExtra("email", email);
                System.out.println(email);
                startActivity(intent);
            }else{
                tx.setText("Incorrect Credentials");
            }

        }
    }
}