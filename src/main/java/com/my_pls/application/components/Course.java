package com.my_pls.application.components;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Course {

    private String name = "";
    private int id = -1;
    private String startTime = "";
    private String endTime = "";
    private String startDate = "";
    private String endDate = "";
    private int professorId = -1;
    private String professorName = "";
    private String meeting_days = "";
    private int credits = 3;
    private int capacity = 0;
    private int preReq = -1;
    private String preReqName = "None";
    private String status = "";
    private int enrolled = 0;
    private String state = "Open";
    private int rating = 0;
    private int unchecked = 0;
    private String learningObj = "";
    private String requirements = "";
    private String feedback = "";
    private ArrayList<Course> courses = new ArrayList<>();

    public Course(String name, int id, String startDate, String endDate, String startTime,
                  String endTime, String meeting_days, String professorName, int professorId,
                  int credits, int capacity, String status, int enrolled) {
        this.name = name;
        this.capacity = capacity;
        this.credits = credits;
        this.endDate = endDate;
        this.endTime = endTime;
        this.startDate = startDate;
        this.startTime = startTime;
        this.meeting_days = meeting_days;
        this.professorName = professorName;
        this.professorId = professorId;
        this.status = status;
        this.enrolled = enrolled;
        this.id = id;
    }

    public Course() {

    }

    public int getId() {
        return id;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getCredits() {
        return credits;
    }

    public int getProfessorId() {
        return professorId;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getMeeting_days() {
        return meeting_days;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getPreReq() {
        return preReq;
    }

    public String getPreReqName() {
        return preReqName;
    }

    public void setPreReq(int preReq) {
        this.preReq = preReq;
    }

    public void setPreReqName(String preReqName) {
        this.preReqName = preReqName;
    }

    public String getStatus() {
        return status;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public int getRating() {
        return rating;
    }

    public int getUnchecked() {
        return unchecked;
    }

    public String getLearningObj() {
        return learningObj;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setUnchecked(int unchecked) {
        this.unchecked = unchecked;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setLearningObj(String learningObj) {
        this.learningObj = learningObj;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ArrayList<Course> getCourses(String filterstatus, Connection conn) {
        courses = Proxy.viewCourses(filterstatus, conn);
        return courses;
    }

    public static ArrayList<String> filterOptions(String currOption) {
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("All");
        filterOptions.add("Current");
        filterOptions.add("Upcoming");
        filterOptions.add("Completed");
        filterOptions.remove(new String(currOption));
        return filterOptions;
    }


    public boolean deleteCourse(Connection conn) {
        boolean flag = Proxy.deleteDisscussionGroupAndmembers(id, conn);
        if (flag) flag = Proxy.deleteCourse(id, conn);
        return flag;
    }

    public static ArrayList<Course> getMyCourses(int id, Connection conn) {
        ArrayList<Course> my_courses = Proxy.getMyCourses(id, conn);
        return my_courses;
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

    public static Course getCourse(String courseId, Connection conn) {
        try {
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Course course = Proxy.findCourseByCourseId(courseId, conn);
        Map<String, Object> rating = Proxy
                .getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"", conn);
        if (!rating.isEmpty()) {

            course.setRating((Integer) rating.get("rating"));
            course.setUnchecked((Integer) rating.get("unchecked"));
            course.setFeedback(rating.get("feedback").toString());
        }
        return course;
    }

    public static boolean addRating(Map<String, String> formFields, String courseId, Connection conn) {
        boolean flag = false;
        try {
            String feedback = URLDecoder.decode(formFields.get("feedback"), "UTF-8");
            int rate_value = Integer.parseInt(formFields.get("Rating"));
            if (formFields.containsKey("doneRating"))
                flag = Proxy.rateCourse(Integer.parseInt(courseId), rate_value, feedback, conn);
            else
                flag = Proxy.rateUser(Integer.parseInt(formFields.get("doneRatingProf")), rate_value, feedback, conn);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean allowRating() {
        LocalDate et = LocalDate.parse(endDate, DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        long days = ChronoUnit.DAYS.between(LocalDate.now(), et);
        if (Math.abs(days) <= 7) return true;
        else return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void checkCapacity() {
        if (enrolled >= capacity) setState("Closed");
    }

    public void enrollmentStartDate() {
        if (status.contentEquals("Current")
                && state.contentEquals("Open")) {
            LocalDate startDate = LocalDate.parse((CharSequence) getStartDate(), DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            LocalDate comparingDate = LocalDate.now().plusDays(7);
            if (!startDate.isBefore(comparingDate)) setState("Closed");
        }
    }
}
