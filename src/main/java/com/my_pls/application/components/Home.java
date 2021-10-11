package com.my_pls.application.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Home {
    private int id = -1;
    private String role = "learner";
    private Map<Integer,Object> courses = new HashMap<>();
    private ArrayList<Map<String,Object>> groups = new ArrayList<>();
    private Map<String,Object> rating = new HashMap<>();

    public Home(int id, String role) {
        id = id;
        role = role;

        Map<Integer,Object> allCourses = Courses.getMyCourses(id, role);
        if (allCourses.size() > 5) {
            Iterator it = allCourses.keySet().iterator();
            for (int j = 0; j < 5; j++) {
                Integer i = (Integer) it.next();
                courses.put(i, allCourses.get(i));
            }
        } else courses.putAll(allCourses);

        groups = DataMapper.getMyDiscussionGroups(id);
        if (groups.size() > 5) groups = (ArrayList<Map<String, Object>>) groups.subList(0, 5);
        rating = DataMapper.getRatingAndFeedbackOfUserGivenUserId(id, "", "");
    }

    public Map<Integer,Object> getCourses() {
        return courses;
    }

    public ArrayList<Map<String,Object>> getGroups() {
        return groups;
    }

    public Map<String,Object> getRating() {
        return rating;
    }
}
