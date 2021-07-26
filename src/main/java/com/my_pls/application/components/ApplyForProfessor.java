package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ApplyForProfessor {
    public static Map<String,Object> checkForErrors(String fname, String lname,
                                        Map<String,String> formfields, Map<String, Object> map) {
        try {
            map.put("applyProfErr",false);
            String name = URLDecoder.decode(formfields.get("name"), "UTF-8");
            String fullName = fname + " " + lname;
            if (!name.equals(fullName)) {
                map.put("nameErr", true);
                map.put("applyProfErr", true);
            }
            if (formfields.get("customRadio").equals("no")) {
                String err_msg = "You have indicated that you do not be a professor. " +
                        "Please return to the homepage or fill in the form again.";
                map.put("errMsg", err_msg);
                map.put("applyProfErr", true);
            }
        } catch (Exception e) {
            System.out.println("Exception at apply Prof " + e);
        }
        return map;
    }

    public static String apply(String email) {
        String flag = "false";
        try {
            Connection conn = MySqlConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "select Id, First_Name, Last_Name from user_details where Email=?");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                PreparedStatement pst2 = conn.prepareStatement("select * from prof_requests where id="+ id);
                ResultSet rs2 = pst2.executeQuery();
                if (!rs2.next()) {
                    PreparedStatement pst3 = conn.prepareStatement("insert into prof_requests (id, name) VALUES (?,?)");
                    pst3.setInt(1, id);
                    pst3.setString(2, name);
                    int i = pst3.executeUpdate();
                    if (i != 0) flag = "true";
                } else flag = "exists";
            }
        } catch (Exception e) {
            System.out.println("Exception in apply prof in apply "+e);
        }
        return flag;
    }
}
