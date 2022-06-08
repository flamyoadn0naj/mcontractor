package com.my_pls.application.components;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Home {
    private int id = -1;
    private String role = "learner";
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Map<String,Object>> groups = new ArrayList<>();
    private Map<String,Object> rating = new HashMap<>();

    public Home(int id, String role, Connection conn) {
        id = id;
        role = role;

        courses = Course.getMyCourses(id, conn);
        if (courses.size() > 5) courses = new ArrayList<>(courses.subList(0, 5));

        groups = Proxy.getMyDiscussionGroups(id, conn);
        if (groups.size() > 5) groups = new ArrayList<>(groups.subList(0, 5));
        rating = Proxy.getRatingAndFeedbackOfUserGivenUserId(id, "", "", conn);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Map<String,Object>> getGroups() {
        return groups;
    }

    public Map<String,Object> getRating() {
        return rating;
    }
}
