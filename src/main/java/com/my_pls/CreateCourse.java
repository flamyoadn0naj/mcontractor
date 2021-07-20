package com.my_pls;

import java.net.URLDecoder;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class CreateCourse {

    private static Connection conn = MySqlConnection.getConnection();

    public static LinkedHashMap<String,Boolean> findAllDays() {
        LinkedHashMap<String, Boolean> allDays = new LinkedHashMap<String, Boolean>();
        allDays.put("Monday",false);
        allDays.put("Tuesday",false);
        allDays.put("Wednesday",false);
        allDays.put("Thursday",false);
        allDays.put("Friday",false);
        return allDays;
    }

    public static Map<Integer, String> findAllProfs(){
        Map<Integer,String> profs = new HashMap<Integer,String>();
        try {
            PreparedStatement pst = conn.prepareStatement("select profId from courses");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("profId");
                String name = Courses.findProfName(id);
                profs.put(id,name);
            }
        } catch (SQLException throwables) {
            System.out.println("Exception at get prof name "+ throwables);
        }
        return profs;
    }

    public static Map<String,Object> postMethodDefaults(Map<String, String> formFields, String edit) {
        Map<String,Object> map = new HashMap<>();
        boolean flag = true;
        Map<Integer,String> profs = findAllProfs();
        Map.Entry<Integer,String> entry = profs.entrySet().iterator().next();
        int prof_id = entry.getKey();
        LinkedHashMap<String, Boolean> allDays = findAllDays();
        map.put("e",edit);
        try {
            String startTime = URLDecoder.decode(formFields.get("start_time"), "UTF-8");
            String endTime = URLDecoder.decode(formFields.get("end_time"), "UTF-8");
            String startDate = formFields.get("start_date");
            String endDate = formFields.get("end_date");
            String name = URLDecoder.decode(formFields.get("name"), "UTF-8");
            prof_id = Integer.parseInt(formFields.get("prof"));
            String obj = URLDecoder.decode(formFields.get("obj"), "UTF-8");
            ArrayList<String> days = new ArrayList<String>();
            if (formFields.containsKey("Monday")) days.add("Monday");
            if (formFields.containsKey("Tuesday")) days.add("Tuesday");
            if (formFields.containsKey("Wednesday")) days.add("Wednesday");
            if (formFields.containsKey("Thursday")) days.add("Thursday");
            if (formFields.containsKey("Friday")) days.add("Friday");
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

            allDays.forEach((k, v)-> {
                if (days.contains(k)) allDays.put(k,true);
            });

            if(LocalTime.parse(endTime).isBefore(LocalTime.parse(startTime))) {
                map.put("errTime", "true");
                map.put("start_time","");
                map.put("end_time","");
                flag = false;
            }
            if (!edit.contentEquals("-1")) {
                if(LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate))) {
                    map.put("errDate", "true");
                    map.put("start_date","");
                    map.put("end_date","");
                    flag = false;
                }
            } else {
                if(LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate)) || LocalDate.parse(startDate).isBefore(LocalDate.now())) {
                    map.put("errDate", "true");
                    map.put("start_date","");
                    map.put("end_date","");
                    flag = false;
                }
            }

            if(flag) {
                String sqlQuery = "insert into courses (course_name, profId, meeting_days, " +
                        "start_time, end_time, start_date, end_date, credits, total_capacity, enrolled, " +
                        "status, obj) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                if(!edit.contentEquals("-1")) {
                    int id = Integer.parseInt(edit.replaceAll(" ",""));

                    sqlQuery = "update courses set course_name=?, profId=?, meeting_days=?, " +
                            "start_time=?, end_time=?, start_date=?, end_date=?, credits=?, " +
                            "total_capacity=?, enrolled=?, status=?, obj=? where id=" + id;
                }
                PreparedStatement pst = conn.prepareStatement(sqlQuery);
                pst.setString(1, name);
                pst.setInt(2, prof_id);
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
                System.out.println(pst);
                int i = pst.executeUpdate();
                if(i != 0) {
                    System.out.println("done");
                    flag = true;
                }
            }

        } catch (Exception e) {
            System.out.println("Exception at create courses " + e);
            flag = false;
        }
        map.put("days",allDays);
        map.put("currProf",profs.get(prof_id));
        map.put("prof_id",prof_id);
        profs.remove(prof_id);
        map.put("profList",profs);
        map.put("created",flag);
        return map;
    }

    public static Map<String,Object> editCourse(Map<String,Object> map,String id) {
        Map<Integer,String> profs = findAllProfs();
        Map.Entry<Integer,String> entry = profs.entrySet().iterator().next();
        int prof_id = entry.getKey();
        LinkedHashMap<String, Boolean> days = findAllDays();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from courses where id=?");
            pst.setInt(1, Integer.parseInt(id));
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                map.put("name",rs.getString("course_name"));
                map.put("obj",rs.getString("obj"));
                map.put("start_date",rs.getString("start_date"));
                map.put("end_date",rs.getString("end_date"));
                map.put("start_time",rs.getString("start_time"));
                map.put("end_time",rs.getString("end_time"));
                map.put("credits",rs.getInt("credits"));
                map.put("cap",rs.getInt("total_capacity"));
                prof_id = rs.getInt("profId");
                String meeting_days = rs.getString("meeting_days");
                days.forEach((k, v)-> {
                    if (meeting_days.contains(k)) days.put(k,true);
                });
            }
        } catch (Exception e) {
            System.out.println("Exception at edit course " + e);
        }
        map.put("currProf",profs.get(prof_id));
        map.put("prof_id",prof_id);
        profs.remove(prof_id);
        map.put("days",days);
        map.put("profList",profs);
        return map;
    }
}
