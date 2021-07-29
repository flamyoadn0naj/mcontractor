package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscussionGroups {

    private static Connection conn = MySqlConnection.getConnection();

    public static ArrayList<Map<String, Object>> getMyDiscussionGroups(int id) {
        ArrayList<Map<String,Object>> allGroups = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select dg_id from dg_members where user_id=" + id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> group = getGroupDetailsByGroupId(rs.getInt("dg_id"));
                allGroups.add(group);
            }
        } catch (SQLException throwables) {
            System.out.println("Error in getMyDiscussionGroups " + throwables);
        }

        return allGroups;
    }

    public static Map<String, Object> getGroupDetailsByGroupId(int dg_id) {
        Map<String,Object> details = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from discussion_groups where id=" + dg_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                details.put("name", rs.getString("name"));
                if (rs.getInt("privacy") == 1) details.put("privacy", true);
                if (rs.getInt("course_id") != 0) details.put("course", true);
                details.put("id", dg_id);
            }
        } catch (Exception e) {
            System.out.println("Exception in getGroupDetailsByGroupId " + e);
        }
        return details;
    }
}
