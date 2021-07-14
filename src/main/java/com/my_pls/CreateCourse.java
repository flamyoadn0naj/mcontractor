package com.my_pls;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateCourse {
    public static Map<String,String> postMethodDefaults(Map<String, String> formFields) {
        Map<String,String> map = new HashMap<>();
        try {
            boolean flag = true;
            String startTime = URLDecoder.decode(formFields.get("start_time"), "UTF-8");
            String endTime = URLDecoder.decode(formFields.get("end_time"), "UTF-8");
            String startDate = formFields.get("start_date");
            String endDate = formFields.get("end_date");
            String name = URLDecoder.decode(formFields.get("name"), "UTF-8");
            String prof = URLDecoder.decode(formFields.get("prof"), "UTF-8");
            String obj = URLDecoder.decode(formFields.get("obj"), "UTF-8");
            ArrayList<String> days = new ArrayList<String>();
            if (formFields.containsKey("mon")) days.add("Monday");
            if (formFields.containsKey("tue")) days.add("Tuesday");
            if (formFields.containsKey("wed")) days.add("Wednesday");
            if (formFields.containsKey("thr")) days.add("Thursday");
            if (formFields.containsKey("fri")) days.add("Friday");
            String daysString = String.join(", ", days);
            int credits = Integer.parseInt(formFields.get("credits"));
            int capacity = Integer.parseInt(formFields.get("capacity"));

            map.put("name",name);
            map.put("obj",obj);
            map.put("start_date",startDate);
            map.put("end_date",endDate);
            map.put("start_time",startTime);
            map.put("end_time",endTime);
            map.put("credits",String.valueOf(credits));
            map.put("cap",String.valueOf(capacity));

            if(LocalTime.parse(endTime).isBefore(LocalTime.parse(startTime))) {
                map.put("errTime", "true");
                map.put("start_time","");
                map.put("end_time","");
                flag = false;
            }
            if(LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate)) || LocalDate.parse(startDate).isBefore(LocalDate.now())) {
                map.put("errDate", "true");
                map.put("start_date","");
                map.put("end_date","");
                flag = false;
            }
            if(flag) {
                String sqlQuery = "insert into courses (course_name, professor, meeting_days, start_time, end_time, start_date, end_date, credits, total_capacity, enrolled, status, obj) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                Connection conn = MySqlConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sqlQuery);
                pst.setString(1, name);
                pst.setString(2, prof);
                pst.setString(3, daysString);
                pst.setString(4, startTime);
                pst.setString(5, endTime);
                pst.setString(6, startDate);
                pst.setString(7, endDate);
                pst.setInt(8, credits);
                pst.setInt(9, capacity);
                pst.setInt(10, 0);
                pst.setString(11, "Upcoming");
                pst.setString(12, obj);
                int i = pst.executeUpdate();
                if(i != 0) {
                    System.out.println("done");
                }
            }

        } catch (Exception e) {
            System.out.println("Exception at create courses " + e);
        }
        return map;
    }
}
