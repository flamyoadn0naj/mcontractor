package com.my_pls.application.components;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rating {
    public static Map<Integer, Map<String, Object>> getAllUserRatings(String searchText, String filter) {
        Map<Integer, Map<String, Object>> ratings = new HashMap<>();
        ArrayList<Integer> user_ids = DataMapper.getAllUserIDsFromRating();
        for (int u : user_ids) {
            Map<String, Object> ratingObj = DataMapper.getRatingAndFeedbackOfUserGivenUserId(u, searchText, filter);
            if (!ratingObj.isEmpty()) ratings.put(u, ratingObj);
        }
        return ratings;
    }

    public static Map<Integer, Map<String, Object>> getAllCourseRatings(String searchText) {
        Map<Integer, Map<String, Object>> ratings = new HashMap<>();
        ArrayList<Integer> course_ids = DataMapper.getAllCourseIDsFromRating();
        for (int c : course_ids) {
            Map<String, Object> ratingObj = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(c, searchText);
            if (!ratingObj.isEmpty()) {
                String pname = DataMapper.findProfName((Integer) ratingObj.get("prof_id"));
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

    public static Map<String,Object> getMethodFunctionality() {
        Map<String,Object> map = new HashMap<>();
        map.put("role","admin");
        map.put("ratings", true);
        map.put("searchOptions", getSearchOptions(""));
        Map<Integer, Map<String, Object>> users = getAllUserRatings("","");
        Map<Integer, Map<String, Object>> courses = getAllCourseRatings("");
        map.put("users", users);
        map.put("courses", courses);
        if (users.isEmpty()) map.put("userEmpty", true);
        if (courses.isEmpty()) map.put("courseEmpty", true);
        return map;
    }

    public static Map<String,Object> postMethodFunctionality(Map<String, String> formFields) {
        Map<String,Object> map = new HashMap<>();
        Map<Integer, Map<String, Object>> users = getAllUserRatings("","");
        Map<Integer, Map<String, Object>> courses = getAllCourseRatings("");
        System.out.println(formFields);
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
                    String searchText = formFields.get("searchTextCourse");
                    courses = getAllCourseRatings(searchText);
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
                    String filter = formFields.get("filterBy");
                    users = getAllUserRatings(searchText, filter);
                    map.put("searchText", searchText);
                    map.put("filterKey", filter);
                    map.put("filterVal", Admin.mapFilterKey(filter));
                    System.out.println( getSearchOptions(filter));
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
