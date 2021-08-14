package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import java.sql.*;
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
        map.put("filterOptions",filterOptions);
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
}
