package com.my_pls.application.components;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Enrollment {

    private static Map<String, Object> checkCapacity(Map<String, Object>c) {
        int capacity = Integer.parseInt(c.get("cap").toString());
        int enrolled = Integer.parseInt(c.get("enrolled").toString());
        if (enrolled < capacity) c.put("state", "Open");
        else c.put("state", "Closed");

        return c;
    }

    private static Map<String, Object> checkStartDate(Map<String, Object> c) {
        String status = c.get("status").toString();
        String state = c.get("state").toString();
        if (status.contentEquals("Current")
                && state.contentEquals("Open")) {
            LocalDate startDate = LocalDate.parse((CharSequence) c.get("startDate"), DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            LocalDate comparingDate = LocalDate.now().plusDays(7);
            if (startDate.isBefore(comparingDate)) c.put("state", "Open");
            else c.put("state", "Closed");
        }
        return c;
    }

    private static ArrayList<Map<String, Object>> formatCourses(ArrayList<Map<String, Object>> courses, Connection conn) {
        for (Map<String,Object> c: courses) {
            c = checkCapacity(c);
            c = checkStartDate(c);
            int id = Integer.parseInt(c.get("id").toString());
            Map<String, Object> ratingObj = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(id, "", conn);
            if (ratingObj.isEmpty()) c.put("noRating", true);
            else {
                c.put("rating", ratingObj.get("rating"));
                c.put("unchecked", ratingObj.get("unchecked"));
            }
////            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
////            DateTimeFormatter df2 = DateTimeFormatter.ofPattern("h:m a");
//////            LocalTime startTime = LocalTime.parse(String.valueOf(c.get("startTime")), df);
////            String s = startTime.format(df2);
////            LocalTime endTime = LocalTime.parse(String.valueOf(c.get("endTime")), df);
////            String e = endTime.format(df2);
//            c.put("startTimeF", s);
//            c.put("endTimeF", e);
        }
        return courses;
    }

    public static ArrayList<Map<String, Object>> findAllAvailableCourses(Connection conn) {
        ArrayList<Map<String,String>> current = DataMapper.viewCourses("Current", conn);
        ArrayList<Map<String,String>> upcoming = DataMapper.viewCourses("Upcoming", conn);
        ArrayList<Map<String, Object>> all = new ArrayList<>();

        for(Map<String, String> m: upcoming) {
            Map<String, Object> o = new HashMap<>();
            o.putAll(m);
            all.add(o);
        }
        for(Map<String, String> m: current) {
            Map<String, Object> o = new HashMap<>();
            o.putAll(m);
            all.add(o);
        }
        all = formatCourses(all, conn);
        return all;
    }
}
