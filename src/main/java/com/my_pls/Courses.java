package com.my_pls;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class Courses {
    private static Connection conn = MySqlConnection.getConnection();

    public static String findProfName(int id) {
        String name = "";
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "select First_Name,Last_Name from user_details where Id="+id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                name = name + rs.getString("First_Name") + " " + rs.getString("Last_Name");
            }
        } catch (SQLException throwables) {
            System.out.println("Exception at get prof name "+ throwables);
        }
        return name;
    }

    public static Map<String,Object> getMethodDefaults(String filterstatus) {
        Map<String,Object> map = new HashMap<>();
        map.put("role","admin");
        map.put("filterStatus", "All");
        map.put("courses",new ArrayList<>());
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("All");
        filterOptions.add("Current");
        filterOptions.add("Upcoming");
        filterOptions.add("Completed");
        map.put("filterOptions",filterOptions);
        try {
            PreparedStatement pst;
            if(filterstatus.isEmpty() || filterstatus.contentEquals("All")) {
                pst = conn.prepareStatement("select * from courses");
            } else {
                pst = conn.prepareStatement("select * from courses where status=?");
                pst.setString(1, filterstatus);
            }
            ResultSet rs = pst.executeQuery();
            ArrayList<Map<String,String>> courses = new ArrayList<Map<String, String>>();
            while(rs.next()) {
                Map<String,String> details = new HashMap<>();
                details.put("name",rs.getString("course_name"));
                String prof = findProfName(rs.getInt("profId"));
                details.put("prof",prof);
                LocalDate startDate = LocalDate.parse(rs.getString("start_date"));
                String s = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                LocalDate endDate = LocalDate.parse(rs.getString("end_date"));
                String e = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                details.put("startDate",s);
                details.put("endDate",e);
                details.put("status",rs.getString("status"));
                details.put("id",String.valueOf(rs.getInt("id")));
                courses.add(details);
            }
            map.put("courses",courses);
        } catch (Exception e) {
            System.out.println("Exception at courses "+e);
            filterstatus = "All";
        }
        map.put("filterStatus",filterstatus);
        filterOptions.remove(new String(filterstatus));
        map.put("filterOptions",filterOptions);
        return map;
    }

    public static boolean deleteCourse(String id) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from courses where id=?");
            pst.setInt(1, Integer.parseInt(id));
            int i = pst.executeUpdate();
            if(i != 0) flag = true;
        } catch (Exception e) {
          System.out.println("Exception at delete courses " + e);
        }
        return flag;
    }
}
