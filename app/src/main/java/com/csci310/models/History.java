package com.csci310.models;


import com.csci310.cs310_groupproject.MainActivity;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class History {
    private String user_id;
    private int event_id;
    Connection conn;
    private ArrayList<ArrayList<String>> Invitations = new ArrayList<>();
    private ArrayList<ArrayList<String>> History = new ArrayList<>();
    private ArrayList<ArrayList<String>> OwnEvent = new ArrayList<>();

    public History(String userID){
        user_id = userID;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/CS310Project?user=root&password=" + MainActivity.PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void printEvent(){

        try {

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
        }

    }

    public String accept(){
        int msg = 0;
        try {
            PreparedStatement ps = conn.prepareStatement("update Invitation set acceptStatus = 1 where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,1);
            ps.executeUpdate();

            ps = conn.prepareStatement("select * from Invitation  where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,1);
            ResultSet rs = ps.executeQuery();
            rs.next();
            msg = rs.getInt("acceptStatus");

            ps = conn.prepareStatement("update Invitation set acceptStatus = 0 where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,1);
            ps.executeUpdate();
        } catch (SQLException  throwables) {
            throwables.printStackTrace();
        }
        return String.valueOf(msg);
    }

    public String decline(){
        int msg = 0;
        try {
            PreparedStatement ps = conn.prepareStatement("update Invitation set acceptStatus = 2 where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,1);
            ps.executeUpdate();

            ps = conn.prepareStatement("select * from Invitation  where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,1);
            ResultSet rs = ps.executeQuery();
            rs.next();
            msg = rs.getInt("acceptStatus");

            ps = conn.prepareStatement("update Invitation set acceptStatus = 0 where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,1);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return String.valueOf(msg);
    }

    public String withdraw(){
        int msg = 0;
        try {
            PreparedStatement ps = conn.prepareStatement("update Invitation set acceptStatus = 2 where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,3);
            ps.executeUpdate();

            ps = conn.prepareStatement("select * from Invitation where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,3);
            ResultSet rs = ps.executeQuery();
            rs.next();
            msg = rs.getInt("acceptStatus");

            ps = conn.prepareStatement("update Invitation set acceptStatus = 1 where userID = ? and eventID = ?");
            ps.setString(1,user_id);
            ps.setInt(2,3);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return String.valueOf(msg);
    }

    public int GetHistorySize(){
        return History.size();
    }

    public int GetInvitationSize(){
        return Invitations.size();
    }

    public int GetOwnEventSize(){
        return OwnEvent.size();
    }

    public String Edit(){
        return user_id;
    }

}
