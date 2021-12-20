package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Courses {
    private static Connection conn = MySqlConnection.getConnection();

    public static Map<String,Object> getMethodDefaults(String filterstatus, Connection conn) {
        Map<String,Object> map = new HashMap<>();
        map.put("role","admin");
        map.put("filterStatus", "All");
        map.put("courses",new ArrayList<>());
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("All");
        filterOptions.add("Current");
        filterOptions.add("Upcoming");
        filterOptions.add("Completed");
        ArrayList<Map<String, String>> courses = DataMapper.viewCourses(filterstatus, conn);
        if (courses.isEmpty()) filterstatus = "All";
        else  map.put("courses",courses);
        map.put("filterStatus",filterstatus);
        filterOptions.remove(new String(filterstatus));
        map.put("filterOptions",filterOptions);
        return map;
    }

    public static boolean deleteCourse(String id, Connection conn) {
        boolean flag = false;
        int course_id = Integer.parseInt(id);
        boolean flag2 = DataMapper.deleteDisscussionGroupAndmembers(course_id, conn);
        if (flag2) flag = DataMapper.deleteCourse(course_id, conn);
        return flag;
    }

    public static Map<Integer, Object> getMyCourses(int id, String role, Connection conn) {
        Map<Integer,Object> courses = new HashMap<>();
        Map<Integer,Object> my_courses = DataMapper.getMyCourses(id, conn);
        courses.putAll(my_courses);
        if (role.contentEquals("prof")) {
            Map<Integer,Object> taught_courses = DataMapper.getTaughtCourses(id, conn);
            courses.putAll(taught_courses);
        }
        return courses;
    }

    public static Map<Integer, Object> filterCourses(String filterBy, Map<Integer, Object> courses) {
        Map<Integer, Object> allCourses = new HashMap<>();
        for(Integer i : courses.keySet()) {
            Map<String,Object> c = (Map<String, Object>) courses.get(i);
            String status = String.valueOf(c.get("status"));
            if (status.contentEquals(filterBy)) allCourses.put(i,c);
        }
        return allCourses;
    }

    public static Map<String, Object> getCourse(String courseId, Connection conn) {
        try {
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String,Object> course = DataMapper.findCourseByCourseId(courseId, conn);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("h:m a");
        LocalTime startTime = LocalTime.parse(String.valueOf(course.get("start_time")), df);
        String s = startTime.format(df2);
        LocalTime endTime = LocalTime.parse(String.valueOf(course.get("end_time")), df);
        String e = endTime.format(df2);
        course.put("start_time", s);
        course.put("end_time", e);
        course.put("prof_name", DataMapper.findProfName((Integer) course.get("prof_id"), conn));
        String prereq = "None";
        Integer p = (Integer) course.get("prereq_course");
        if (p != null && p != 0)
            prereq = String.valueOf(DataMapper.findCourseByCourseId(String.valueOf(p), conn).get("name"));
        course.put("preReq", prereq);
        Map<String, Object> rating = DataMapper
                .getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"", conn);
        if (!rating.isEmpty()) course.put("rating", rating);
        return course;
    }

    public static boolean addRating(Map<String, String> formFields, String courseId, Connection conn) {
        boolean flag = false;
        try {
            String feedback = URLDecoder.decode(formFields.get("feedback"), "UTF-8");
            int rate_value = Integer.parseInt(formFields.get("Rating"));
            if (formFields.containsKey("doneRating"))
                flag = DataMapper.rateCourse(Integer.parseInt(courseId), rate_value, feedback, conn);
            else
                flag = DataMapper.rateUser(Integer.parseInt(formFields.get("doneRatingProf")), rate_value, feedback, conn);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return flag;
    }
}
