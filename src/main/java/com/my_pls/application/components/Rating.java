package com.my_pls.application.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rating {
    public static Map<Integer, Map<String, Object>> getAllUserRatings() {
        Map<Integer, Map<String, Object>> ratings = new HashMap<>();
        ArrayList<Integer> user_ids = DataMapper.getAllUserIDsFromRating();
        for (int u : user_ids) {
            Map<String, Object> ratingObj = DataMapper.getRatingAndFeedbackOfUserGivenUserId(u);
            ratings.put(u, ratingObj);
        }
        return ratings;
    }

    public static Map<Integer, Map<String, Object>> getAllCourseRatings() {
        Map<Integer, Map<String, Object>> ratings = new HashMap<>();
        ArrayList<Integer> course_ids = DataMapper.getAllCourseIDsFromRating();
        for (int c : course_ids) {
            Map<String, Object> ratingObj = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(c);
            ratings.put(c, ratingObj);
        }
        return ratings;
    }
}
