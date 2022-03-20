package com.my_pls.application.components;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rating {
    public static Map<Integer, Map<String, Object>> getAllUserRatings(String searchText, String filter, Connection conn) {
        Map<Integer, Map<String, Object>> ratings = new HashMap<>();
        ArrayList<Integer> user_ids = DataMapper.getAllUserIDsFromRating(conn);
        for (int u : user_ids) {
            Map<String, Object> ratingObj = DataMapper.getRatingAndFeedbackOfUserGivenUserId(u, searchText, filter, conn);
            if (!ratingObj.isEmpty()) ratings.put(u, ratingObj);
        }
        return ratings;
    }

    public static Map<Integer, Map<String, Object>> getAllCourseRatings(String searchText, Connection conn) {
        Map<Integer, Map<String, Object>> ratings = new HashMap<>();
        ArrayList<Integer> course_ids = DataMapper.getAllCourseIDsFromRating(conn);
        for (int c : course_ids) {
            Map<String, Object> ratingObj = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(c, searchText, conn);
            if (!ratingObj.isEmpty()) {
                String pname = DataMapper.findProfName((Integer) ratingObj.get("prof_id"), conn);
                ratingObj.put("prof", pname);
                ratings.put(c, ratingObj);
            }
        }
        return ratings;
    }

    public static Map<String,String> getSearchOptions(String filterBy) {
        Map<String,String> options = new HashMap<String, String>();
        options.put("all", "All");
        options.put("prof","Professor");
        options.put("learner","Learner");
        options.put("admin", "Administrator");
        if (filterBy.length() > 0) options.remove(filterBy);
        return options;
    }

    public static Map<String,Object> getMethodFunctionality(String role, Connection conn) {
        Map<String,Object> map = new HashMap<>();
        map.put("ratings", true);
        map.put("searchOptions", getSearchOptions(""));
        Map<Integer, Map<String, Object>> users = new HashMap<>();
        Map<Integer, Map<String, Object>> courses = getAllCourseRatings("", conn);
        if (role.contentEquals("learner"))
            users = getAllUserRatings("","prof", conn);
        if (role.contentEquals("prof"))
            users = getAllUserRatings("","learner", conn);
        if (role.contentEquals("admin")) {
            users = getAllUserRatings("","", conn);
        }
        map.put("users", users);
        map.put("courses", courses);
        if (users.isEmpty()) map.put("userEmpty", true);
        if (courses.isEmpty()) map.put("courseEmpty", true);
        return map;
    }

    public static boolean addRating(Map<String, String> formFields, Connection conn) {
        boolean flag = false;
        try {
            String feedback = URLDecoder.decode(formFields.get("feedback"), "UTF-8");
            int id = Integer.parseInt(formFields.get("doneRating"));
            int rate_value = Integer.parseInt(formFields.get("Rating"));
            flag = DataMapper.rateUser(id, rate_value, feedback, conn);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static Map<String,Object> postMethodFunctionality(Map<String, String> formFields, String role, Connection conn) {
        Map<String,Object> map = new HashMap<>();
        Map<Integer, Map<String, Object>> users = getAllUserRatings("","", conn);
        Map<Integer, Map<String, Object>> courses = getAllCourseRatings("", conn);
        if (role.contentEquals("learner")) users = getAllUserRatings("", "prof", conn);
        if (role.contentEquals("prof")) users = getAllUserRatings("", "learner", conn);
        map.put("users", users);
        if (formFields == null) {
            map.put("ratings", true);
            map.put("users", users);
            map.put("courses", courses);
            map.put("searchOptions", getSearchOptions(""));
        } else {
            if (formFields.containsKey("userId"))
                map.put("feedback",users.get(Integer.parseInt(formFields.get("userId"))));
            else if (formFields.containsKey("courseId"))
                map.put("feedback", courses.get(Integer.parseInt(formFields.get("courseId"))));
            else {
                String searchVal = formFields.get("search");
                if (searchVal.contains("course")) {
                    String searchText = null;
                    try {
                        searchText = URLDecoder.decode(formFields.get("searchTextCourse"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    courses = getAllCourseRatings(searchText, conn);
                    map.put("searchTextCourse", searchText);
                    map.put("searchOptions", getSearchOptions(""));
                } else if (searchVal.contains("user"))
                {
                    String searchText = null;
                    try {
                        searchText = URLDecoder.decode(formFields.get("searchText"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String filter = "";
                    if (role.contentEquals("learner")) filter = "prof";
                    if (role.contentEquals("prof")) filter = "learner";
                    if (role.contentEquals("admin")) filter = formFields.get("filterBy");

                    users = getAllUserRatings(searchText, filter, conn);
                    map.put("searchText", searchText);
                    map.put("filterKey", filter);
                    map.put("filterVal", Admin.mapFilterKey(filter));
                    map.put("searchOptions", getSearchOptions(filter));
                }
                map.put("ratings", true);
                map.put("users", users);
                map.put("courses", courses);
            }
        }
        if (users.isEmpty()) map.put("userEmpty", true);
        if (courses.isEmpty()) map.put("courseEmpty", true);
        return map;
    }
}
