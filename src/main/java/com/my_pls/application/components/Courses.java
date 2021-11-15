package com.my_pls.application.components;

import com.my_pls.MySqlConnection;
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Courses {
    private static Connection conn = MySqlConnection.getConnection();

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
        ArrayList<Map<String, String>> courses = DataMapper.viewCourses(filterstatus);
        if (courses.isEmpty()) filterstatus = "All";
        else  map.put("courses",courses);
        map.put("filterStatus",filterstatus);
        filterOptions.remove(new String(filterstatus));
        map.put("filterOptions",filterOptions);
        return map;
    }

    public static boolean deleteCourse(String id) {
        boolean flag = false;
        int course_id = Integer.parseInt(id);
        boolean flag2 = DataMapper.deleteDisscussionGroupAndmembers(course_id);
        if (flag2) flag = DataMapper.deleteCourse(course_id);
        return flag;
    }

    public static Map<Integer, Object> getMyCourses(int id, String role) {
        Map<Integer,Object> courses = new HashMap<>();
        Map<Integer,Object> my_courses = DataMapper.getMyCourses(id);
        courses.putAll(my_courses);
        if (role.contentEquals("prof")) {
            Map<Integer,Object> taught_courses = DataMapper.getTaughtCourses(id);
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

    public static Map<String, Object> getCourse(String courseId) {
        Map<String,Object> course = DataMapper.findCourseByCourseId(courseId);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("h:m a");
        LocalTime startTime = LocalTime.parse(String.valueOf(course.get("start_time")), df);
        String s = startTime.format(df2);
        LocalTime endTime = LocalTime.parse(String.valueOf(course.get("end_time")), df);
        String e = endTime.format(df2);
        course.put("start_time", s);
        course.put("end_time", e);
        course.put("prof_name", DataMapper.findProfName((Integer) course.get("prof_id")));
        String prereq = "None";
        Integer p = (Integer) course.get("prereq_course");
        if (p != null && p != 0)
            prereq = String.valueOf(DataMapper.findCourseByCourseId(String.valueOf(p)).get("name"));
        course.put("preReq", prereq);
        Map<String, Object> rating = DataMapper
                .getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"");
        if (!rating.isEmpty()) course.put("rating", rating);
        return course;
    }
}
