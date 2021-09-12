package com.my_pls.application.components;

import com.my_pls.MySqlConnection;
import com.my_pls.securePassword;
import com.my_pls.sendEmail;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLDecoder;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.Date;
import java.util.function.Function;

public class DataMapper {
    private static Connection conn = MySqlConnection.getConnection();
    private static Function<String,String> addQuotes = s -> "\"" + s + "\"";

    public static boolean createOrUpdateCourse(String edit, String name, int prof_id, String daysString,
                                               String startTime, String endTime, String startDate,
                                               String endDate, int credits, int capacity, String obj,
                                               Integer prereq) {
        boolean flag = false;
        String sqlQuery = "";
        if (prereq != null) {
            sqlQuery = "insert into courses (course_name, profId, meeting_days, " +
                    "start_time, end_time, start_date, end_date, credits, total_capacity, enrolled, " +
                    "status, obj, prereq) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            if(!edit.contentEquals("-1")) {
                int id = Integer.parseInt(edit.replaceAll(" ",""));

                sqlQuery = "update courses set course_name=?, profId=?, meeting_days=?, " +
                        "start_time=?, end_time=?, start_date=?, end_date=?, credits=?, " +
                        "total_capacity=?, enrolled=?, status=?, obj=?, prereq=? where id=" + id;
            }
        } else {
            sqlQuery = "insert into courses (course_name, profId, meeting_days, " +
                    "start_time, end_time, start_date, end_date, credits, total_capacity, enrolled, " +
                    "status, obj) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            if(!edit.contentEquals("-1")) {
                int id = Integer.parseInt(edit.replaceAll(" ",""));

                sqlQuery = "update courses set course_name=?, profId=?, meeting_days=?, " +
                        "start_time=?, end_time=?, start_date=?, end_date=?, credits=?, " +
                        "total_capacity=?, enrolled=?, status=?, obj=? where id=" + id;
            }
        }

        try {
            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            pst.setString(1, name);
            pst.setInt(2, prof_id);
            pst.setString(3, daysString);
            pst.setString(4, startTime);
            pst.setString(5, endTime);
            pst.setString(6, startDate);
            pst.setString(7, endDate);
            pst.setInt(8, credits);
            pst.setInt(9, capacity);
            pst.setInt(10, 0);
            pst.setString(11, "Upcoming");
            pst.setString(12, obj);
            if (prereq != null) pst.setInt(13, prereq);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch (Exception e) {
            System.out.println("Exception in createOrUpdateCourse " + e);
        }
        return flag;
    }
    public static Map<Integer, String> findAllProfs(){
        Map<Integer,String> profs = new HashMap<Integer,String>();
        try {
            PreparedStatement pst = conn.prepareStatement("select profId from courses");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("profId");
                String name = findProfName(id);
                profs.put(id,name);
            }
        } catch (SQLException throwables) {
            System.out.println("Exception at get prof name "+ throwables);
        }
        return profs;
    }

    public static int findCourseIdFromCourseDetails (String name, int prof_id, String daysString,
                                                     String startTime, String endTime, String startDate,
                                                     String endDate, int credits, int capacity, String obj) {
        int id = -1;
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "select id from courses where course_name=? and profId=? and meeting_days=? and " +
                            "start_time=? and end_time=? and start_date=? and end_date=? and credits=? and " +
                            "total_capacity=? and obj=?");
            pst.setString(1, name);
            pst.setInt(2, prof_id);
            pst.setString(3, daysString);
            pst.setString(4, startTime);
            pst.setString(5, endTime);
            pst.setString(6, startDate);
            pst.setString(7, endDate);
            pst.setInt(8, credits);
            pst.setInt(9, capacity);
            pst.setString(10, obj);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (Exception e ) {
            System.out.println("Exception at findCourseIdFromCourseDetails " + e);
        }
        return id;
    }

    public static int addDiscussionGroup (String name, int course_id, int privacy) {
        int i = -1;
        String sqlQuery = "insert into discussion_groups (name, course_id, privacy) VALUES (?,?,?)";
        try {
            if (course_id == -1) {
                sqlQuery = "insert into discussion_groups (name,course_id, privacy) VALUES (?,null,?)";
            }
            PreparedStatement pst2 = conn.prepareStatement(sqlQuery);
            pst2.setString(1, name);
            if (course_id != -1) {
                pst2.setInt(2, course_id);
                pst2.setInt(3, privacy);
            } else pst2.setInt(2, privacy);
            i = pst2.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception at addDiscussionGroup " + e);
        }
        return i;
    }

    public static int findLastInsertedId(String table) {
        int i = -1;
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT MAX(id) AS LastID FROM " + table);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                i = rs.getInt("LastID");
            }
        } catch (Exception e) {
            System.out.println("Exception at addDiscussionGroup " + e);
        }
        return i;
    }

    public static boolean addDGmember(int u_id, int dg_id) {
        boolean flag = false;
        try {
            PreparedStatement pst4 = conn.prepareStatement("insert into dg_members (user_id, dg_id) VALUES (?,?)");
            pst4.setInt(1,u_id);
            pst4.setInt(2,dg_id);
            int j = pst4.executeUpdate();
            if (j != 0) flag = true;
        } catch (Exception e) {
            System.out.println("Exception in addDGmember " + e);
        }
        return flag;
    }

    public static Map<String, Object> findCourseByCourseId (String id){
        Map<String, Object> map = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from courses where id=?");
            pst.setInt(1, Integer.parseInt(id));
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                map.put("name",rs.getString("course_name"));
                map.put("obj",rs.getString("obj"));
                map.put("start_date",rs.getString("start_date"));
                map.put("end_date",rs.getString("end_date"));
                map.put("start_time",rs.getString("start_time"));
                map.put("end_time",rs.getString("end_time"));
                map.put("credits",rs.getInt("credits"));
                map.put("cap",rs.getInt("total_capacity"));
                int prof_id = rs.getInt("profId");
                map.put("prof_id", prof_id);
                String meeting_days = rs.getString("meeting_days");
                map.put("meeting_days", meeting_days);
                map.put("prereq_course", rs.getInt("prereq"));
            }
        } catch (Exception e) {
            System.out.println("Exception at findCourseByCourseId " + e);
        }
        return map;
    }

    public static Map<String, Object> getGroupDetailsByGroupId(int dg_id) {
        Map<String,Object> details = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from discussion_groups where id=" + dg_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                details.put("name", rs.getString("name"));
                if (rs.getInt("privacy") == 1) details.put("privacy", true);
                Integer course = rs.getInt("course_id");
                if (course != 0) details.put("course", true);
                details.put("id", dg_id);
            }
        } catch (Exception e) {
            System.out.println("Exception in getGroupDetailsByGroupId " + e);
        }
        return details;
    }

    public static ArrayList<Map<String, Object>> getMyDiscussionGroups(int id) {
        ArrayList<Map<String,Object>> allGroups = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select dg_id from dg_members where user_id=" + id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> group = DataMapper.getGroupDetailsByGroupId(rs.getInt("dg_id"));
                allGroups.add(group);
            }
        } catch (SQLException throwables) {
            System.out.println("Error in getMyDiscussionGroups " + throwables);
        }

        return allGroups;
    }

    public static ArrayList<Integer> getAllCourseIDsFromRating() {
        ArrayList<Integer> course_ids = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select course_id from course_ratings GROUP BY course_id");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                course_ids.add(rs.getInt("course_id"));
            }
        } catch (Exception e) {
            System.out.println("Exception in getAllCourseIDsFromRating" + e );
        }
        return course_ids;
    }

    public static Map<String,Object> getRatingAndFeedbackOfCourseGivenCourseId(int id, String searchText) {
        Map<String,Object> ratingsObj = new HashMap<>();
        String sqlQuery = "select course_name, profId, score, feedback from course_ratings, courses where course_id=? and id=?";
        if (searchText.length() > 0) {
            searchText = "%" + searchText + "%";
            searchText = addQuotes.apply(searchText);
            sqlQuery = "select course_name, profId, score, feedback from course_ratings, courses " +
                    "where course_id=? and id=? and courses.course_name like " + searchText;
        }
        try {
            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            pst.setInt(1, id);
            pst.setInt(2, id);
            ResultSet rs = pst.executeQuery();
            int rating = 0;
            ArrayList<String> feedback = new ArrayList<>();
            String name = "";
            int prof = -1;
            while(rs.next()) {
                name = rs.getString("course_name");
                rating += rs.getInt("score");
                feedback.add(rs.getString("feedback"));
                prof = rs.getInt("profId");

            }
            rating = rating / feedback.size();
            int unchecked = 5 - rating;
            ratingsObj.put("rating", rating);
            ratingsObj.put("feedback", feedback);
            ratingsObj.put("name", name);
            ratingsObj.put("unchecked", unchecked);
            ratingsObj.put("prof_id", prof);
        } catch (Exception e) {
            System.out.println("Exception in getRatingAndFeedbackOfCourseGivenCourseId");
        }
        return ratingsObj;
    }

    public static ArrayList<Integer> getAllUserIDsFromRating() {
        ArrayList<Integer> user_ids = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select userId from user_ratings GROUP BY userId");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                user_ids.add(rs.getInt("userId"));
            }
        } catch (Exception e) {
            System.out.println("Exception in getAllUserIDsFromRating" + e );
        }
        return user_ids;
    }

    public static Map<String,Object> getRatingAndFeedbackOfUserGivenUserId(int id, String searchText, String filter) {
        Map<String,Object> ratingsObj = new HashMap<>();
        String sqlQuery = "select score, feedback, First_Name, Last_Name, role from user_ratings, user_details where userId="
        + id + " and Id=" + id;
        if (filter.contentEquals("all")) {
            if (searchText.length() > 0) {
                searchText = "%" + searchText + "%";
                searchText = addQuotes.apply(searchText);
                sqlQuery = "select score, feedback, First_Name, Last_Name, role from user_ratings, user_details " +
                        "where userId=" + id + " and Id=" + id + " and user_details.First_Name like " + searchText +
                        "or userId=" + id + " and Id=" + id +" and user_details.Last_Name like " + searchText;
            }
        } else if (filter.length() > 0) {
            searchText = "%" + searchText + "%";
            searchText = addQuotes.apply(searchText);
            filter = addQuotes.apply(filter);
            sqlQuery = "select score, feedback, First_Name, Last_Name, role from user_ratings, user_details " +
                    "where userId=" + id + " and Id=" + id +" and role=" + filter + " and user_details.First_Name like "
                    + searchText + "or userId=" + id + " and Id=" + id + " and user_details.Last_Name like "
                    + searchText + "and role=" + filter;
        }
        try {
            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            ResultSet rs = pst.executeQuery();
            int rating = 0;
            ArrayList<String> feedback = new ArrayList<>();
            String name = "";
            String role = "";
            while(rs.next()) {
                name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                role = Admin.mapFilterKey(rs.getString("role"));
                rating += rs.getInt("score");
                feedback.add(rs.getString("feedback"));
            }
            rating = rating / feedback.size();
            int unchecked = 5 - rating;
            ratingsObj.put("rating", rating);
            ratingsObj.put("feedback", feedback);
            ratingsObj.put("name", name);
            ratingsObj.put("unchecked", unchecked);
            ratingsObj.put("role", role);
        } catch (Exception e) {
            System.out.println("Exception in getRatingAndFeedbackOfUser");
        }
        return ratingsObj;
    }

    public static String findProfName(int id) {
        String name = "";
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "select First_Name,Last_Name from user_details where Id="+id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                name = name + rs.getString("First_Name") + " " + rs.getString("Last_Name");
            }
        } catch (SQLException throwables) {
            System.out.println("Exception at get prof name "+ throwables);
        }
        return name;
    }

    public static boolean deleteDisscussionGroupAndmembers (int courseId) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("select id from discussion_groups where course_id=" + courseId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int d_id = rs.getInt("id");
                PreparedStatement pst2 = conn.prepareStatement("delete from dg_members where dg_id=" + d_id);
                int i = pst2.executeUpdate();
                if (i != 0) {
                    PreparedStatement pst3 = conn.prepareStatement("delete from discussion_groups where id=" + d_id);
                    int j = pst3.executeUpdate();
                    if (j != 0) flag = true;
                }
            }

        } catch (Exception e) {
            System.out.println("Error at delete Discussion group " + e);
        }
        return flag;
    }

    public static boolean deleteCourse(int id) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from courses where id=?");
            pst.setInt(1, id);
            int i = pst.executeUpdate();
            if(i != 0) flag = true;
        } catch (Exception e) {
            System.out.println("Exception at delete courses " + e);
        }
        return flag;
    }

    public static Map<Integer, String> viewUsers(String searchText, String filterBy) {
        Map<Integer,String> users = new HashMap<>();
        searchText = "%" + searchText + "%";
        searchText = addQuotes.apply(searchText);
        String admin = addQuotes.apply("admin");
        try {
            String sqlQuery = "";
            if (!filterBy.equals("all")) {
                filterBy = addQuotes.apply(filterBy);
                sqlQuery = "select Id, First_Name, Last_Name from user_details where role=" + filterBy +
                        " and First_Name like " + searchText + " or role=" + filterBy + "and Last_Name like " + searchText;
            }
            else sqlQuery = "select Id, First_Name, Last_Name from user_details where role!=" + admin +" and First_Name like "
                    + searchText + " or role!=" + admin +" and Last_Name like " + searchText;

            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                users.put(id,name);
            }
        } catch(Exception e) {
            System.out.println("Exception at get all requests prof "+ e);
        }
        return users;
    }

    public static boolean authorize(String uid) {
        boolean flag = false;
        int id = Integer.parseInt(uid);
        try {
            PreparedStatement pst = conn.prepareStatement("update user_details set role=? where id=?");
            pst.setString(1,"admin");
            pst.setInt(2, id);
            int j = pst.executeUpdate();
            if (j != 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at assign admin " + e);
        }
        return flag;
    }

    public static boolean approveProf(String pid) {
        boolean flag = false;
        int id = Integer.parseInt(pid);
        try {
            PreparedStatement pst = conn.prepareStatement("update user_details set role=? where id=?");
            pst.setString(1,"prof");
            pst.setInt(2, id);
            int i = pst.executeUpdate();
            if (i != 0) {
                PreparedStatement pst2 = conn.prepareStatement("delete from prof_requests where id=?");
                pst2.setInt(1, id);
                int j = pst2.executeUpdate();
                if (j != 0) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in approve prof " + e);
        }
        return flag;
    }

    public static boolean deleteReq(String pid) {
        boolean flag = false;
        int id = Integer.parseInt(pid);
        try {
            PreparedStatement pst = conn.prepareStatement("delete from prof_requests where id=?");
            pst.setInt(1, id);
            int j = pst.executeUpdate();
            if (j != 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at delete request " + e);
        }
        return flag;
    }

    public static Map<Integer,String> viewAllRequests() {
        Map<Integer,String> profs = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from prof_requests");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                profs.put(id,name);
            }
        } catch(Exception e) {
            System.out.println("Exception at get all requests prof "+ e);
        }
        return profs;
    }

    public static ArrayList<Map<String,String>> viewCourses(String filterstatus) {
        ArrayList<Map<String,String>> courses = new ArrayList<Map<String, String>>();

        try {
            PreparedStatement pst;
            if(filterstatus.isEmpty() || filterstatus.contentEquals("All")) {
                pst = conn.prepareStatement("select * from courses order by start_date");
            } else {
                pst = conn.prepareStatement("select * from courses where status=? order by start_date");
                pst.setString(1, filterstatus);
            }
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                Map<String,String> details = new HashMap<>();
                details.put("name",rs.getString("course_name"));
                String prof = DataMapper.findProfName(rs.getInt("profId"));
                details.put("prof",prof);
                LocalDate startDate = LocalDate.parse(rs.getString("start_date"));
                String s = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                LocalDate endDate = LocalDate.parse(rs.getString("end_date"));
                String e = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                details.put("startDate",s);
                details.put("endDate",e);
                details.put("status",rs.getString("status"));
                details.put("id",String.valueOf(rs.getInt("id")));
                String prereq = "None";
                Integer p = rs.getInt("prereq");
                if (p != null && p != 0) prereq = String.valueOf(findCourseByCourseId(String.valueOf(p)).get("name"));
                details.put("prereq", prereq);
                courses.add(details);
            }
        } catch (Exception e) {
            System.out.println("Exception at courses "+e);
            filterstatus = "All";
        }
        return courses;
    }

    public static boolean register(String fName, String lName, String email, String password) {
        Random theRandom = new Random();
        theRandom.nextInt(999999);
        String myHash = DigestUtils.md5Hex("" +	theRandom);
        boolean flag = false;
        try {
            String sqlQuery = "insert into user_details (First_Name,Last_Name,Email,Password,Hash,Active) values(?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            pst.setString(1, fName);
            pst.setString(2, lName);
            pst.setString(3, email);
            pst.setString(4, password);
            pst.setString(5, myHash);
            pst.setInt(6, 0);
            int i = pst.executeUpdate();
            String body =  "Click this link to confirm your email address and complete setup for your account."
                    + "\n\nVerification Link: " + "http://localhost:8080/verify-register/confirm?key1=" + email
                    + "&key2=" + myHash;
            if (i != 0) {
                sendEmail se = new sendEmail();
                se.sendEmail_content(email,"Verify Email at MyPLS",body);
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Error at Registration: " + e);
        }
        return flag;
    }

    public static String applyProf(String email) {
        String flag = "false";
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "select Id, First_Name, Last_Name from user_details where Email=?");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                PreparedStatement pst2 = conn.prepareStatement("select * from prof_requests where id="+ id);
                ResultSet rs2 = pst2.executeQuery();
                if (!rs2.next()) {
                    PreparedStatement pst3 = conn.prepareStatement("insert into prof_requests (id, name) VALUES (?,?)");
                    pst3.setInt(1, id);
                    pst3.setString(2, name);
                    int i = pst3.executeUpdate();
                    if (i != 0) flag = "true";
                } else flag = "exists";
            }
        } catch (Exception e) {
            System.out.println("Exception in apply prof in apply "+e);
        }
        return flag;
    }

    public static boolean forgetPasswordSendEmail(String email) {
        boolean flag = false;
        try {
            email = URLDecoder.decode(email, "UTF-8");
            Connection conn = MySqlConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("select Email, Hash, Active from user_details where Email=?");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Random theRandom = new Random();
                String code = String.format("%04d", theRandom.nextInt(10000));
                PreparedStatement pst1 = conn.prepareStatement("update user_details set Active='0',Hash=? where Email=?");
                pst1.setString(1, code);
                pst1.setString(2, email);
                int i = pst1.executeUpdate();
                String body = "Here is the confirmation code to reset your password at MyPLS. Confirmation code is " + code + "\n\nVisit: " + "http://localhost:8080/forgot-password/password to reset your password";
                if (i != 0) {
                    sendEmail se = new sendEmail();
                    se.sendEmail_content(email, "Reset Password Email at MyPLS - Confirmation Code " + code, body);
                    flag = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception at Forget Passoword " + e);
        }
        return flag;
    }

    public static boolean resendEmailConfirmation(String email) {
        boolean flag = false;
        try {
            email = URLDecoder.decode(email, "UTF-8");
            Connection conn = MySqlConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("select Hash from user_details where Email=?");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String myHash = rs.getString("Hash");
                String body =  "Click this link to confirm your email address and complete setup for your account."
                        + "\n\nVerification Link: " + "http://localhost:8080/verify-register/confirm?key1=" + email
                        + "&key2=" + myHash;
                sendEmail se = new sendEmail();
                se.sendEmail_content(email,"Verify Email at MyPLS",body);
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at Resend Email " + e);
        }
        return flag;
    }

    public static boolean changePassword(String confirmCode, String email, String newPassword ) {
        boolean flag = false;
        try {
            Connection conn = MySqlConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("select * from user_details where Hash=? and email=? and Active='0'");
            pst.setString(1, confirmCode);
            pst.setString(2, email);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                PreparedStatement pst1 = conn.prepareStatement("update user_details set Active='1',Password=? where Hash=? and email=?");
                pst1.setString(1, newPassword);
                pst1.setString(2, confirmCode);
                pst1.setString(3, email);
                int i = pst1.executeUpdate();
                flag = true;
            }
        } catch(Exception e){
            System.out.println("Error at Forget Password " + e);
        }
        return flag;
    }

    public static Pair login(String input_password, String emVal, securePassword pwd_manager) {
        Pair p = new Pair();
        User user = new User();
        try {
            emVal = URLDecoder.decode(emVal, "UTF-8");
            PreparedStatement pst = conn.prepareStatement("select * from user_details where Email=?");
            pst.setString(1, emVal);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                String db_password = rs.getString("Password");

                if (pwd_manager.comparePassword(db_password, input_password)) {
                    user.setAll(rs.getString("First_Name"),rs.getString("Last_Name"),
                            db_password, emVal, rs.getInt("Id"), rs.getString("role"));
                    p = new Pair(null, user);
                } else {
                    p = new Pair(null, new User());
                }
            }
        } catch (Exception e) {
            System.out.println("Error at Login post");
        }
        return p;
    }

    public static int findDiscussionGroupIdByCourseId(int course_id) {
        int dg_id = -1;
        try {
            PreparedStatement pst = conn.prepareStatement("select id from discussion_groups where course_id=" + course_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) dg_id = rs.getInt("id");
        } catch (Exception e) {
            System.out.println("Exception at findDiscussionGroupIdByCourseId " + e);
        }
        return dg_id;
    }

    public static boolean updateDiscussionGroup(int id, String name) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("update discussion_groups set name=? where id=?");
            pst.setString(1, name);
            pst.setInt(2, id);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at updateDiscussionGroup " + e);
        }
        return flag;
    }

    public static boolean updateDGMembers(int old_prof_id, int new_prof_id, int d_id) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("update dg_members set user_id=? where dg_id=? and user_id=?");
            pst.setInt(1, new_prof_id);
            pst.setInt(2, d_id);
            pst.setInt(3, old_prof_id);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at updateDGMembers " + e);
        }
        return flag;
    }

    public static void updateCourses(Connection sqlconnection){
        String sql_statement = "Select * from courses where status<>'Completed'";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date today_date = new Date();
        try{
            Statement pst = sqlconnection.createStatement();
            ResultSet dbrs = pst.executeQuery(sql_statement);
            while (dbrs.next()){
                int course_id = dbrs.getInt("id");
                String endDate_str = dbrs.getString("end_date");
                Date end_date = formatter.parse(endDate_str);
                Date start_date = formatter.parse(dbrs.getString("start_date"));
                if (today_date.compareTo(end_date)>0){
//                    Update course status
                    sql_statement = "UPDATE courses SET status = 'Completed' WHERE id ="+course_id;
                    Statement stmt2 = sqlconnection.createStatement();
                    stmt2.executeUpdate(sql_statement);
                    stmt2.close();
                }else if(today_date.compareTo(start_date)>=0){
                    sql_statement = "UPDATE courses SET status = 'Current' WHERE id ="+course_id;
                    Statement stmt2 = sqlconnection.createStatement();
                    stmt2.executeUpdate(sql_statement);
                    stmt2.close();
                }
            }
            dbrs.close();
            sqlconnection.close();
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

    }

    public static int getUserIdFromEmail(String email) {
        int id = -1;
        try {
            PreparedStatement pst = conn.prepareStatement("select * from user_details where Email=?");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                id = rs.getInt("Id");
            }
        } catch (SQLException throwables) {
            System.out.println("Exception at getUserIdFromEmail "+ throwables);
        }
        return id;
    }

    public static Map<Integer, Map<String, Object>> getAllDisscussionGroups(String searchText, int filter) {
        boolean flag = false;
        Map<Integer, Map<String, Object>> groups = new HashMap<>();
        String sqlQuery = "select * from discussion_groups where ISNULL(course_id)";
        try {
            if (searchText.length() > 0) {
                searchText = "%" + searchText + "%";
                searchText = addQuotes.apply(searchText);
                if (filter != -1) {
                    sqlQuery = "select * from discussion_groups where ISNULL(course_id) and name like "+
                            searchText +" and privacy=" + filter;
                } else {
                    sqlQuery = "select * from discussion_groups where ISNULL(course_id) and name like "+
                            searchText;
                }
            }
            PreparedStatement pst = conn.prepareStatement(sqlQuery);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> details = new HashMap<>();
                int id = rs.getInt("id");
                if (rs.getInt("privacy") == 1) details.put("privacy", true);
                details.put("name",rs.getString("name"));
                groups.put(id, details);
            }
        } catch (Exception e) {
            System.out.println("Exception at getAllDisscussionGroups " + e);
        }
        return groups;
    }

    public static Map<Integer, Object> getPendingGroupRequests(int id) {
        Map<Integer, Object> groups = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select dg_id from discussion_group_requests where user_id="+ id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> details = new HashMap<>();
                int dg_id = rs.getInt("dg_id");
                details = getGroupDetailsByGroupId(dg_id);
                groups.put(dg_id, details);
            }
        } catch (Exception e) {
            System.out.println("Exception at getPendingGroupRequests " + e);
        }
        return groups;
    }

    public static boolean requestToJoinGroup(int id, int dg_id) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("insert into discussion_group_requests (user_id, dg_id) VALUES (?,?)");
            pst.setInt(1, id);
            pst.setInt(2, dg_id);
            int i = pst.executeUpdate();
            if (i != 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at getPendingGroupRequests " + e);
        }
        return flag;
    }

    public static boolean deleteRequestForGroup(int id, int dg_id) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from discussion_group_requests where user_id=? and dg_id=?");
            pst.setInt(1, id);
            pst.setInt(2, dg_id);
            int i = pst.executeUpdate();
            if (i != 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at deleteRequestForGroup " + e);
        }
        return flag;
    }

    public static boolean verifyEmailofUser(String email, String hash) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("select Email, Hash, Active from user_details where Email=? and Hash=? and Active='0'");
            pst.setString(1, email);
            pst.setString(2, hash);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                PreparedStatement pst1 = conn.prepareStatement("update user_details set Active='1' where Email=? and Hash=?");
                pst1.setString(1, email);
                pst1.setString(2, hash);

                int i = pst1.executeUpdate();
                if (i != 0) flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at verifyEmailofUser " + e);
        }
        return flag;
    }
}
