package com.my_pls.application.components;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Map;

public class Enrollment {


    private static ArrayList<Course> formatCourses(ArrayList<Course> courses, Connection conn) {
        for (Course c: courses) {
            c.checkCapacity();
            c.enrollmentStartDate();
            Map<String, Object> ratingObj = Proxy.getRatingAndFeedbackOfCourseGivenCourseId(c.getId(), "", conn);
            if (!ratingObj.isEmpty()) {
                c.setRating((Integer) ratingObj.get("rating"));
                c.setUnchecked((Integer) ratingObj.get("unchecked"));
            }
        }
        return courses;
    }

    public static ArrayList<Course> findAllAvailableCourses(Connection conn) {
        ArrayList<Course> current = Proxy.viewCourses("Current", conn);
        ArrayList<Course> upcoming = Proxy.viewCourses("Upcoming", conn);
        ArrayList<Course> all = new ArrayList<>();
        all.addAll(current);
        all.addAll(upcoming);


        all = formatCourses(all, conn);
        return all;
    }
}
