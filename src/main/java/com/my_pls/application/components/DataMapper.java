package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DataMapper {
    private static Connection conn = MySqlConnection.getConnection();

    public static boolean createOrUpdateCourse(String edit, String name, int prof_id, String daysString,
                                               String startTime, String endTime, String startDate,
                                               String endDate, int credits, int capacity, String obj) {
        boolean flag = false;
        String sqlQuery = "insert into courses (course_name, profId, meeting_days, " +
                "start_time, end_time, start_date, end_date, credits, total_capacity, enrolled, " +
                "status, obj) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        if(!edit.contentEquals("-1")) {
            int id = Integer.parseInt(edit.replaceAll(" ",""));

            sqlQuery = "update courses set course_name=?, profId=?, meeting_days=?, " +
                    "start_time=?, end_time=?, start_date=?, end_date=?, credits=?, " +
                    "total_capacity=?, enrolled=?, status=?, obj=? where id=" + id;
        }
        try {
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
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch (Exception e) {
            System.out.println("Exception in createOrUpdateCourse " + e);
        }
        return flag;
    }
    public static Map<Integer, String> findAllProfs(){
        Map<Integer,String> profs = new HashMap<Integer,String>();
        try {
            PreparedStatement pst = conn.prepareStatement("select profId from courses");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("profId");
                String name = findProfName(id);
                profs.put(id,name);
            }
        } catch (SQLException throwables) {
            System.out.println("Exception at get prof name "+ throwables);
        }
        return profs;
    }

    public static int findCourseIdFromCourseDetails (String name, int prof_id, String daysString,
                                                     String startTime, String endTime, String startDate,
                                                     String endDate, int credits, int capacity, String obj) {
        int id = -1;
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "select id from courses where course_name=? and profId=? and meeting_days=? and " +
                            "start_time=? and end_time=? and start_date=? and end_date=? and credits=? and " +
                            "total_capacity=? and obj=?");
            pst.setString(1, name);
            pst.setInt(2, prof_id);
            pst.setString(3, daysString);
            pst.setString(4, startTime);
            pst.setString(5, endTime);
            pst.setString(6, startDate);
            pst.setString(7, endDate);
            pst.setInt(8, credits);
            pst.setInt(9, capacity);
            pst.setString(10, obj);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (Exception e ) {
            System.out.println("Exception at findCourseIdFromCourseDetails " + e);
        }
        return id;
    }

    public static int addDiscussionGroup (String name, int course_id, int privacy) {
        int i = -1;
        try {
            PreparedStatement pst2 = conn.prepareStatement(
                    "insert into discussion_groups (name, course_id, privacy) VALUES (?,?,?)");
            pst2.setString(1, name);
            pst2.setInt(2, course_id);
            pst2.setInt(3, privacy);
            i = pst2.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception at addDiscussionGroup " + e);
        }
        return i;
    }

    public static int findLastInsertedId() {
        int i = -1;
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT LAST_INSERT_ID();");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                i = rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println("Exception at addDiscussionGroup " + e);
        }
        return i;
    }

    public static boolean addDGmember(int u_id, int dg_id) {
        boolean flag = false;
        try {
            PreparedStatement pst4 = conn.prepareStatement("insert into dg_members (user_id, dg_id) VALUES (?,?)");
            pst4.setInt(1,u_id);
            pst4.setInt(2,dg_id);
            int j = pst4.executeUpdate();
            if (j != 0) flag = true;
        } catch (Exception e) {
            System.out.println("Exception in addDGmember " + e);
        }
        return flag;
    }

    public static Map<String, Object> findCourseByCourseId (String id){
        Map<String, Object> map = new HashMap<>();
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
                int prof_id = rs.getInt("profId");
                map.put("prof_id", prof_id);
                String meeting_days = rs.getString("meeting_days");
                map.put("meeting_days", meeting_days);
            }
        } catch (Exception e) {
            System.out.println("Exception at findCourseByCourseId " + e);
        }
        return map;
    }

    public static Map<String, Object> getGroupDetailsByGroupId(int dg_id) {
        Map<String,Object> details = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from discussion_groups where id=" + dg_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                details.put("name", rs.getString("name"));
                if (rs.getInt("privacy") == 1) details.put("privacy", true);
                Integer course = rs.getInt("course_id");
                if (course != null) details.put("course", true);
                details.put("id", dg_id);
            }
        } catch (Exception e) {
            System.out.println("Exception in getGroupDetailsByGroupId " + e);
        }
        return details;
    }

    public static ArrayList<Map<String, Object>> getMyDiscussionGroups(int id) {
        ArrayList<Map<String,Object>> allGroups = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select dg_id from dg_members where user_id=" + id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> group = DataMapper.getGroupDetailsByGroupId(rs.getInt("dg_id"));
                allGroups.add(group);
            }
        } catch (SQLException throwables) {
            System.out.println("Error in getMyDiscussionGroups " + throwables);
        }

        return allGroups;
    }

    public static ArrayList<Integer> getAllCourseIDsFromRating() {
        ArrayList<Integer> course_ids = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select course_id from course_ratings GROUP BY course_id");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                course_ids.add(rs.getInt("course_id"));
            }
        } catch (Exception e) {
            System.out.println("Exception in getAllCourseIDsFromRating" + e );
        }
        return course_ids;
    }

    public static Map<String,Object> getRatingAndFeedbackOfCourseGivenCourseId(int id, String searchText) {
        Map<String,Object> ratingsObj = new HashMap<>();
        String sqlQuery = "select course_name, profId, score, feedback from course_ratings, courses where course_id=? and id=?";
        if (searchText.length() > 0) {
            Function<String,String> addQuotes = s -> "\"" + s + "\"";
            searchText = "%" + searchText + "%";
            searchText = addQuotes.apply(searchText);
            sqlQuery = "select course_name, profId, score, feedback from course_ratings, courses " +
                    "where course_id=? and id=? and courses.course_name like " + searchText;
        }
        try {
            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            pst.setInt(1, id);
            pst.setInt(2, id);
            ResultSet rs = pst.executeQuery();
            int rating = 0;
            ArrayList<String> feedback = new ArrayList<>();
            String name = "";
            int prof = -1;
            while(rs.next()) {
                name = rs.getString("course_name");
                rating += rs.getInt("score");
                feedback.add(rs.getString("feedback"));
                prof = rs.getInt("profId");

            }
            rating = rating / feedback.size();
            int unchecked = 5 - rating;
            ratingsObj.put("rating", rating);
            ratingsObj.put("feedback", feedback);
            ratingsObj.put("name", name);
            ratingsObj.put("unchecked", unchecked);
            ratingsObj.put("prof_id", prof);
        } catch (Exception e) {
            System.out.println("Exception in getRatingAndFeedbackOfCourseGivenCourseId");
        }
        return ratingsObj;
    }

    public static ArrayList<Integer> getAllUserIDsFromRating() {
        ArrayList<Integer> user_ids = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select userId from user_ratings GROUP BY userId");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                user_ids.add(rs.getInt("userId"));
            }
        } catch (Exception e) {
            System.out.println("Exception in getAllUserIDsFromRating" + e );
        }
        return user_ids;
    }

    public static Map<String,Object> getRatingAndFeedbackOfUserGivenUserId(int id, String searchText, String filter) {
        Map<String,Object> ratingsObj = new HashMap<>();
        String sqlQuery = "select score, feedback, First_Name, Last_Name, role from user_ratings, user_details where userId="
        + id + " and Id=" + id;
        Function<String,String> addQuotes = s -> "\"" + s + "\"";
        if (filter.contentEquals("all")) {
            if (searchText.length() > 0) {
                searchText = "%" + searchText + "%";
                searchText = addQuotes.apply(searchText);
                sqlQuery = "select score, feedback, First_Name, Last_Name, role from user_ratings, user_details " +
                        "where userId=" + id + " and Id=" + id + " and user_details.First_Name like " + searchText +
                        "or userId=" + id + " and Id=" + id +" and user_details.Last_Name like " + searchText;
            }
        } else if (filter.length() > 0) {
            searchText = "%" + searchText + "%";
            searchText = addQuotes.apply(searchText);
            filter = addQuotes.apply(filter);
            sqlQuery = "select score, feedback, First_Name, Last_Name, role from user_ratings, user_details " +
                    "where userId=" + id + " and Id=" + id +" and role=" + filter + " and user_details.First_Name like "
                    + searchText + "or userId=" + id + " and Id=" + id + " and user_details.Last_Name like "
                    + searchText + "and role=" + filter;
        }
        try {
            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            ResultSet rs = pst.executeQuery();
            int rating = 0;
            ArrayList<String> feedback = new ArrayList<>();
            String name = "";
            String role = "";
            while(rs.next()) {
                name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                role = Admin.mapFilterKey(rs.getString("role"));
                rating += rs.getInt("score");
                feedback.add(rs.getString("feedback"));
            }
            rating = rating / feedback.size();
            int unchecked = 5 - rating;
            ratingsObj.put("rating", rating);
            ratingsObj.put("feedback", feedback);
            ratingsObj.put("name", name);
            ratingsObj.put("unchecked", unchecked);
            ratingsObj.put("role", role);
        } catch (Exception e) {
            System.out.println("Exception in getRatingAndFeedbackOfUser");
        }
        return ratingsObj;
    }

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

}
