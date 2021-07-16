package com.my_pls;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class Courses {

    public static Map<String,Object> getMethodDefaults(String filterstatus) {
        Map<String,Object> map = new HashMap<>();
        map.put("role","admin");
        map.put("filterStatus", "All");
        map.put("courses",new ArrayList<>());
        ArrayList<String> filterOptions = new ArrayList<String>();
        filterOptions.add("All");
        filterOptions.add("Current");
        filterOptions.add("Upcoming");
        filterOptions.add("Completed");
        map.put("filterOptions",filterOptions);
        try {
            Connection conn = MySqlConnection.getConnection();
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
                details.put("prof",rs.getString("professor"));
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
        filterOptions.remove(new String(filterstatus));
        map.put("filterOptions",filterOptions);
        map.put("filterStatus",filterstatus);
        return map;
    }

    public static boolean deleteCourse(String id) {
        boolean flag = false;
        try {
            Connection conn = MySqlConnection.getConnection();
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
