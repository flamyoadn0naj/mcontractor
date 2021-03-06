package com.my_pls.application.components;

import com.my_pls.securePassword;
import com.my_pls.sendEmail;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLDecoder;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.Date;
import java.util.function.Function;
//Proxy Pattern - changing the query to a prepared statement
public class Proxy {
    private static int MAXQUIZ = 200;
    private static Function<String,String> addQuotes = s -> "\"" + s + "\"";

    public static boolean createOrUpdateCourse(String edit, String name, int prof_id, String daysString,
                                               String startTime, String endTime, String startDate,
                                               String endDate, int credits, int capacity, String obj,
                                               Integer prereq, Connection conn) {
        boolean flag = false;
        String sqlQuery = "";

        if (prereq != -1) {
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
            if (prereq != -1) pst.setInt(13, prereq);
            int i = pst.executeUpdate();
            if (i != 0) {
                flag = true;
                updateCourses(conn);
            }
        } catch (Exception e) {
            System.out.println("Exception in createOrUpdateCourse " + e);
        }
        return flag;
    }

    public static Map<Integer, String> findAllProfs(Connection conn){
        Map<Integer,String> profs = new HashMap<Integer,String>();
        try {
            PreparedStatement pst = conn.prepareStatement("select profId from courses");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("profId");
                String name = findProfName(id, conn);
                profs.put(id,name);
            }
        } catch (SQLException throwables) {
            System.out.println("Exception at get prof name "+ throwables);
        }
        return profs;
    }

    public static int addDiscussionGroup(String name, int course_id, int privacy, Connection conn) {
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

    public static int findLastInsertedId(String table, Connection conn) {
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

    public static boolean addDGmember(int u_id, int dg_id, Connection conn) {
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

    public static Course findCourseByCourseId(String id, Connection conn) {
        id = id.replaceAll("\\s","");
        Course c = new Course();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from courses where id=?");
            pst.setInt(1, Integer.parseInt(id));
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                String prof = Proxy.findProfName(rs.getInt("profId"), conn);
                LocalDate startDate = LocalDate.parse(rs.getString("start_date"));
                String s = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                LocalDate endDate = LocalDate.parse(rs.getString("end_date"));
                String e = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

                DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter df2 = DateTimeFormatter.ofPattern("hh:mm a");
                LocalTime startTime = LocalTime.parse(String.valueOf(rs.getString("start_time")), df);
                String st = startTime.format(df2);
                LocalTime endTime = LocalTime.parse(String.valueOf(rs.getString("end_time")), df);
                String et = endTime.format(df2);
                String prereq = "None";
                Integer p = rs.getInt("prereq");

                c = new Course(rs.getString("course_name"), rs.getInt("id"), s, e, st, et,
                        rs.getString("meeting_days"), prof, rs.getInt("profId"),
                        rs.getInt("credits"), rs.getInt("total_capacity"), rs.getString("status"),
                        rs.getInt("enrolled"));
                if (p != null && p != 0) {
                    c.setPreReq(p);
                    prereq = String.valueOf(findCourseByCourseId(String.valueOf(p), conn).getName());
                    c.setPreReqName(prereq);
                }
                c.setRequirements(rs.getString("requirements"));
                c.setLearningObj(rs.getString("obj"));
                c.setOrigEndDate(rs.getString("end_date"));
                c.setOrigStartDate(rs.getString("start_date"));
                c.setOrigStartTime(rs.getString("start_time"));
                c.setOrigEndTime(rs.getString("end_time"));
            }
        } catch (Exception e) {
            System.out.println("Exception at findCourseByCourseId " + e);
        }
        return c;
    }

    public static Map<String, Object> getGroupDetailsByGroupId(int dg_id, Connection conn) {
        Map<String,Object> details = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from discussion_groups where id=" + dg_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                details.put("name", rs.getString("name"));
                if (rs.getInt("privacy") == 1) details.put("privacy", true);
                Integer course = rs.getInt("course_id");
                if (course != 0) {
                    details.put("course", true);
                    details.put("course_id", course);
                }
                details.put("id", dg_id);
            }
        } catch (Exception e) {
            System.out.println("Exception in getGroupDetailsByGroupId " + e);
        }
        return details;
    }

    public static ArrayList<Map<String, Object>> getMyDiscussionGroups(int id, Connection conn) {
        ArrayList<Map<String,Object>> allGroups = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select dg_id from dg_members where user_id=" + id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> group = Proxy.getGroupDetailsByGroupId(rs.getInt("dg_id"), conn);
                allGroups.add(group);
            }
        } catch (SQLException throwables) {
            System.out.println("Error in getMyDiscussionGroups " + throwables);
        }

        return allGroups;
    }

    public static ArrayList<Integer> getAllCourseIDsFromRating(Connection conn) {
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

    public static Map<String,Object> getRatingAndFeedbackOfCourseGivenCourseId(int id, String searchText, Connection conn) {
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
            if(feedback.size() > 0) {
                rating = rating / feedback.size();
                int unchecked = 5 - rating;
                ratingsObj.put("rating", rating);
                ratingsObj.put("feedback", feedback);
                ratingsObj.put("name", name);
                ratingsObj.put("unchecked", unchecked);
                ratingsObj.put("prof_id", prof);
            }
        } catch (Exception e) {
            System.out.println("Exception in getRatingAndFeedbackOfCourseGivenCourseId");
            System.out.println(e.getMessage());
        }
        return ratingsObj;
    }

    public static ArrayList<Integer> getAllUserIDsFromRating(Connection conn) {
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

    public static Map<String,Object> getRatingAndFeedbackOfUserGivenUserId(int id, String searchText, String filter, Connection conn) {
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
            if (!feedback.isEmpty()) {
                rating = rating / feedback.size();
                int unchecked = 5 - rating;
                ratingsObj.put("rating", rating);
                ratingsObj.put("feedback", feedback);
                ratingsObj.put("name", name);
                ratingsObj.put("unchecked", unchecked);
                ratingsObj.put("role", role);
            }
        } catch (Exception e) {
            System.out.println("Exception in getRatingAndFeedbackOfUser");
        }
        return ratingsObj;
    }

    public static String findProfName(int id, Connection conn) {
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

    public static boolean deleteDisscussionGroupAndmembers (int courseId, Connection conn) {
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

    public static boolean deleteCourse(int id, Connection conn) {
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

    public static Map<Integer, String> viewUsers(String searchText, String filterBy, Connection conn) {
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

    public static boolean authorize(String uid, Connection conn) {
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

    public static boolean approveProf(String pid, Connection conn) {
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

    public static boolean deleteReq(String pid, Connection conn) {
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

    public static Map<Integer,String> viewAllRequests(Connection conn) {
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

    public static ArrayList<Course> viewCourses(String filterstatus, Connection conn) {
        ArrayList<Course> courses = new ArrayList<>();

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
                String prof = Proxy.findProfName(rs.getInt("profId"), conn);
                LocalDate startDate = LocalDate.parse(rs.getString("start_date"));
                String s = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                LocalDate endDate = LocalDate.parse(rs.getString("end_date"));
                String e = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

                DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter df2 = DateTimeFormatter.ofPattern("hh:mm a");
                LocalTime startTime = LocalTime.parse(String.valueOf(rs.getString("start_time")), df);
                String st = startTime.format(df2);
                LocalTime endTime = LocalTime.parse(String.valueOf(rs.getString("end_time")), df);
                String et = endTime.format(df2);
                String prereq = "None";
                Integer p = rs.getInt("prereq");

                Course c = new Course(rs.getString("course_name"), rs.getInt("id"), s, e, st, et,
                        rs.getString("meeting_days"), prof, rs.getInt("profId"),
                        rs.getInt("credits"), rs.getInt("total_capacity"), rs.getString("status"),
                        rs.getInt("enrolled"));
                if (p != null && p != 0) {
                    c.setPreReq(p);
                    prereq = String.valueOf(findCourseByCourseId(String.valueOf(p), conn).getName());
                    c.setPreReqName(prereq);
                }

                courses.add(c);
            }
        } catch (Exception e) {
            System.out.println("Exception at courses "+e);
            filterstatus = "All";
        }
        return courses;
    }

    public static boolean register(String fName, String lName, String email, String password, Connection conn) {
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

    public static String applyProf(String email, Connection conn) {
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

    public static boolean forgetPasswordSendEmail(String email, Connection conn) {
        boolean flag = false;
        try {
            email = URLDecoder.decode(email, "UTF-8");
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
            System.out.println("Exception at Forget Password " + e);
        }
        return flag;
    }

    public static boolean resendEmailConfirmation(String email, Connection conn) {
        boolean flag = false;
        try {
            email = URLDecoder.decode(email, "UTF-8");
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

    public static boolean changePassword(String confirmCode, String email, String newPassword, Connection conn) {
        boolean flag = false;
        try {
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
                if (i != 0) flag = true;
            }
        } catch(Exception e){
            System.out.println("Error at Forget Password " + e);
        }
        return flag;
    }

    public static User login(String input_password, String emVal, securePassword pwd_manager, Connection conn) {
        User user = new User();
        try {
            emVal = URLDecoder.decode(emVal, "UTF-8");
            PreparedStatement pst = conn.prepareStatement("select * from user_details where Email=?");
            pst.setString(1, emVal);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                String db_password = rs.getString("Password");

                if (pwd_manager.comparePassword(db_password, input_password))
                    user = new User(rs.getString("First_Name"),rs.getString("Last_Name"),
                            db_password, emVal, rs.getInt("Id"), rs.getString("role"));
            }
        } catch (Exception e) {
            System.out.println("Error at Login call");
        }
        return user;
    }

    public static int findDiscussionGroupIdByCourseId(int course_id, Connection conn) {
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

    public static boolean updateDiscussionGroup(int id, String name, Connection conn) {
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

    public static boolean createQuestion(Quiz question1, Connection conn) {
        question1.answer = question1.answer.toUpperCase();
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("insert into quiz_questions (quizId,question,answer,mark,responseA,responseB,responseC,responseD) VALUES (?,?,?,?,?,?,?,?)");
            pst.setInt(1, question1.quizId);
            pst.setString(2, question1.questionText);
            pst.setString(3,question1.answer);
            pst.setInt(4,question1.mark);
            pst.setString(5,question1.responseA);
            pst.setString(6, question1.responseB);
            pst.setString(7, question1.responseC);
            pst.setString(8, question1.responseD);int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at createQuestion " + e);
        }
        return flag;
    }

    public static boolean updateQuestion(Quiz question1, Connection conn) {
        boolean flag = false;
        question1.answer = question1.answer.toUpperCase();
        try {
            PreparedStatement pst = conn.prepareStatement("update quiz_questions set question=?,answer=?,mark=?,responseA=?,responseB=?,responseC=?,responseD=? WHERE quizId=? and questionId=?");

            pst.setString(1, question1.questionText);
            pst.setString(2,question1.answer);
            pst.setInt(3,question1.mark);
            pst.setString(4,question1.responseA);
            pst.setString(5, question1.responseB);
            pst.setString(6, question1.responseC);
            pst.setString(7, question1.responseD);
            pst.setInt(8, question1.quizId);
            pst.setInt(9, question1.questionId);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at createQuestion " + e);
        }
        return flag;
    }

    public static boolean deleteQuestion(Quiz question1, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from quiz_questions where questionId=?");
            pst.setInt(1, question1.questionId);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at deleteQuestion " + e);
        }
        return flag;
    }
    public static boolean deleteQuiz(Quiz question1, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from quizzes where Id=?");
            pst.setInt(1, question1.quizId);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at deleteQuiz " + e);
        }
        return flag;
    }

    public static boolean deleteQuizAttempt(Quiz question, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from grades where userId=? AND courseId=? AND quizId=?");
            pst.setInt(1,question.learnerId);
            pst.setInt(2, question.courseId);
            pst.setInt(3, question.quizId);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at deleteQuizAttempt " + e);
        }
        return flag;
    }

    public static boolean deleteQuestionAttempts(Quiz question, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from question_grades where quizId=? AND learnerId=?");
            pst.setInt(1, question.quizId);
            pst.setInt(2,question.learnerId);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch(Exception e) {
            System.out.println("Exception at delete question attempts " + e);
        }
        return flag;
    }
    public static boolean updateQuiz(Quiz question1, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("update quizzes set lessonId=?,name=?,minimumMarks=? WHERE Id=?");

            pst.setInt(1, question1.lessonId);
            pst.setString(2,question1.quizName);
            pst.setInt(3,question1.MinMark);
            pst.setInt(4,question1.quizId);
            int i = pst.executeUpdate();
            if (i != 0) {
                flag = true;
                pst.close();
            }
        } catch(Exception e) {
            System.out.println("Exception at createQuestion " + e);
        }
        return flag;
    }

    public static int updateTotalMark(Quiz quiz, Connection conn){

        Map<Integer,Object> questions = new HashMap<>();
        int mark=0;
        try{
            PreparedStatement pst1 = conn.prepareStatement("select * from quiz_questions where quizId=?");
            pst1.setInt(1,quiz.quizId);
            ResultSet dbrs = pst1.executeQuery();
            while (dbrs.next()){
                Map<String, Object> question = new HashMap<>();
                mark = mark + dbrs.getInt("mark");
            }
            dbrs.close();
            pst1.close();
            PreparedStatement pst2 = conn.prepareStatement("update quizzes set totalMarks=? WHERE Id=?");
            pst2.setInt(1,mark);
            pst2.setInt(2,quiz.quizId);
            int i = pst2.executeUpdate();
            if (i != 0) {
                pst2.close();
            }

        } catch(Exception e) {
            System.out.println("Exception at update mark " + e);
        }
        return mark;
    }



    public static Map<Integer,Object> getQuestions(Quiz quiz, Connection conn){

        Map<Integer,Object> questions = new HashMap<>();
        int i=0;
        try{
            PreparedStatement pst1 = conn.prepareStatement("select * from quiz_questions where quizId=?");
            pst1.setInt(1,quiz.quizId);
            ResultSet dbrs = pst1.executeQuery();
            while (dbrs.next()){
                Map<String, Object> question = new HashMap<>();
                question.put("quizId",quiz.quizId);
                question.put("questionId",dbrs.getInt("questionId"));
                question.put("questionText",dbrs.getString("question"));
                question.put("answer", dbrs.getString("answer"));
                question.put("mark",dbrs.getInt("mark"));
                question.put("responseA",dbrs.getString("responseA"));
                question.put("responseB",dbrs.getString("responseB"));
                question.put("responseC",dbrs.getString("responseC"));
                question.put("responseD",dbrs.getString("responseD"));
                question.put("status",1);
                questions.put((Integer) question.get("questionId"),question);
            }

        } catch(Exception e) {
            System.out.println("Exception at getQuestions " + e);
        }
        return questions;
    }

    public static Map<Integer,Object> getQuestionAttempts(Quiz quiz,Map<Integer,Object> questions, Connection conn){

        int i=0;
        try{
            PreparedStatement pst1 = conn.prepareStatement("select * from question_grades where quizId=? AND learnerId=?");
            pst1.setInt(1,quiz.quizId);
            pst1.setInt(2,quiz.learnerId);
            ResultSet dbrs = pst1.executeQuery();
            while (dbrs.next()){
                int questionId = dbrs.getInt("questionId");
                String response = dbrs.getString("response");
                Map<String, Object> question = (Map<String, Object>) questions.get(questionId);
                question.put("status",2);
                question.replace("answer",response);
                questions.put(questionId,question);
            }

        } catch(Exception e) {
            System.out.println("Exception at getQuestions " + e);
        }
        return questions;
    }

    public static boolean takeQuestion(Quiz question,int grade,Connection conn){
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("insert into question_grades (quizId,learnerId,questionId,response,score) VALUES (?,?,?,?,?)");
            pst.setInt(1, question.quizId);
            pst.setInt(2, question.learnerId);
            pst.setInt(3,question.questionId);
            pst.setString(4,question.answer);
            pst.setInt(5, grade);
            int i = pst.executeUpdate();
            flag = true;
        } catch(Exception e) {
            System.out.println("Exception at take question " + e);
        }
        return flag;
    }
    public static boolean reTakeQuestion(Quiz question,int grade,Connection conn){
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("update question_grades set response = ?, score = ? where quizId = ? AND learnerId = ? AND questionId = ?");
            pst.setString(1,question.answer);
            pst.setInt(2,grade);
            pst.setInt(3,question.quizId);
            pst.setInt(4,question.learnerId);
            pst.setInt(5,question.questionId);

            int i = pst.executeUpdate();
            flag = true;
        } catch(Exception e) {
            System.out.println("Exception at re take question " + e);
        }
        return flag;
    }

    public static boolean calculateGrades(Quiz newQuiz,Connection conn){
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO grades(userId, courseId, lessonId, quizId, score) SELECT ?,?,?,?,SUM(score) from question_grades where quizId=? and learnerId=?;");
            pst.setInt(1,newQuiz.learnerId);
            pst.setInt(2,newQuiz.courseId);
            pst.setInt(3,newQuiz.lessonId);
            pst.setInt(4,newQuiz.quizId);
            pst.setInt(5,newQuiz.quizId);
            pst.setInt(6,newQuiz.learnerId);
            int i = pst.executeUpdate();
            flag = true;
        } catch(Exception e) {
            System.out.println("Exception at updating grades " + e);
        }
        return flag;
    }

    public static Map<Integer,Object> getQuizGrades(Quiz quiz,Map<Integer,Object> quizzes, Connection conn){

        try {
            PreparedStatement pst = conn.prepareStatement("select * from grades where userId=? and courseId=?");
            pst.setInt(1,quiz.learnerId);
            pst.setInt(2,quiz.courseId);
            ResultSet dbrs = pst.executeQuery();
            while (dbrs.next()) {
                int quizId = dbrs.getInt("quizId");
                int score = dbrs.getInt("score");
                Map<String, Object> question = (Map<String, Object>) quizzes.get(quizId);
                question.put("score",score);
                question.put("status",2);
                quizzes.put(quizId,question);
            }
        } catch(Exception e) {
            System.out.println("Exception at get quiz grades " + e);
        }
        return quizzes;
    }

    public static Map<String, Object> getQuestion(int QuestionId, Connection conn) {

        Map<String, Object> question = new HashMap<>();

        try {
            PreparedStatement pst1 = conn.prepareStatement("select * from quiz_questions where questionId=?");
            pst1.setInt(1, QuestionId);
            ResultSet dbrs = pst1.executeQuery();
            while (dbrs.next()) {
                question.put("quizId", dbrs.getInt("quizId"));
                question.put("questionId", dbrs.getInt("questionId"));
                question.put("questionText", dbrs.getString("question"));
                question.put("answer", dbrs.getString("answer"));
                question.put("mark", dbrs.getInt("mark"));
                question.put("responseA", dbrs.getString("responseA"));
                question.put("responseB", dbrs.getString("responseB"));
                question.put("responseC", dbrs.getString("responseC"));
                question.put("responseD", dbrs.getString("responseD"));
            }

        } catch (Exception e) {
            System.out.println("Exception at getQuestions " + e);
        }
        return question;
    }

    public static Map<Integer, Object>  viewQuizzes(int lessonID, Connection conn) throws Exception{
//        ArrayList<Map<String,String>> quizzes = new ArrayList<Map<String, String>>();
        Map<Integer,Object> quizzes = new HashMap<>();
        try {
            PreparedStatement pst1 = conn.prepareStatement("select * from quizzes where lessonId=?");
            pst1.setInt(1,lessonID);
            ResultSet rs = pst1.executeQuery();

            while(rs.next()) {
                Map<String, Object> quiz = new HashMap<>();
                quiz.put("lessonId",lessonID);
                quiz.put("quizId",rs.getInt("Id"));
                quiz.put("name",rs.getString("name"));
                quiz.put("minMark",rs.getInt("minimumMarks"));
                quiz.put("status",rs.getInt("enabled"));
                quiz.put("marks", rs.getInt("totalMarks"));
                quizzes.put(rs.getInt("Id"),quiz);
            }
        } catch (Exception e) {
            System.out.println("Exception at View Quizes");
            throw new Exception(e);
            /* System.out.println("Exception at view Quizes "+e); */
        }
        return quizzes;
    }

    public static String viewQuizResponse(Quiz question,Connection conn){
        String response = null;
        try {
            PreparedStatement pst1 = conn.prepareStatement("select * from question_grades where quizId=? AND learnerId=? AND questionId=?");
            pst1.setInt(1,question.quizId);
            pst1.setInt(2,question.learnerId);
            pst1.setInt(3,question.questionId);
            ResultSet rs = pst1.executeQuery();
            while(rs.next()) {
                response = rs.getString("response");
            }
        }catch (Exception e) {
                System.out.println("Exception at view Quiz Attempt "+e);
            }
        return response;
    }

    public static Map<Integer, Object>  viewQuiz(int quizId, Connection conn) throws Exception {
//        ArrayList<Map<String,String>> quizzes = new ArrayList<Map<String, String>>();
        Map<Integer,Object> quizzes = new HashMap<>();
        try {
            PreparedStatement pst1 = conn.prepareStatement("select * from quizzes where Id=?");
            pst1.setInt(1,quizId);
            ResultSet rs = pst1.executeQuery();

            while(rs.next()) {
                Map<String, Object> quiz = new HashMap<>();
                quiz.put("lessonId",rs.getString("lessonId"));
                quiz.put("quizId",rs.getInt("Id"));
                quiz.put("name",rs.getString("name"));
                quiz.put("minMark",rs.getInt("minimumMarks"));
                quiz.put("status",rs.getInt("enabled"));
                quiz.put("marks", rs.getInt("totalMarks"));
                quizzes.put(rs.getInt("Id"),quiz);
            }
        } catch (Exception e) {
            System.out.println("Exception at view Quiz");
            throw new Exception(e);
        }
        return quizzes;
    }

    public static Quiz viewSingleQuiz(int quizId, Connection conn) throws Exception {
//        ArrayList<Map<String,String>> quizzes = new ArrayList<Map<String, String>>();
        Map<Integer,Object> quizzes = new HashMap<>();
        Quiz singleQuiz = new Quiz();
        try {
            PreparedStatement pst1 = conn.prepareStatement("select * from quizzes where Id=?");
            pst1.setInt(1,quizId);
            ResultSet rs = pst1.executeQuery();

            while(rs.next()) {

                singleQuiz.lessonId =  rs.getInt("lessonId");
                singleQuiz.quizId = rs.getInt("Id");
                singleQuiz.quizName = rs.getString("name");
                singleQuiz.MinMark = rs.getInt("minimumMarks");
                singleQuiz.totalMark = rs.getInt("totalMarks");
            }
        } catch (Exception e) {
            System.out.println("Exception at view Single Quiz");
            throw new Exception(e);
        }
        return singleQuiz;
    }


    public static boolean createQuiz(Quiz newQuiz, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("insert into quizzes (lessonId,name,completed,minimumMarks,enabled) VALUES (?,?,?,?,?)");
            pst.setInt(1, newQuiz.lessonId);
            pst.setString(2, newQuiz.quizName);
            pst.setInt(3, 0);
            pst.setInt(4,newQuiz.MinMark);
            pst.setInt(5, 1);
            int i = pst.executeUpdate();
            flag = true;
        } catch(Exception e) {
            System.out.println("Exception at createQuiz " + e);
        }
        return flag;
    }


    public static boolean updateDGMembers(int old_prof_id, int new_prof_id, int d_id, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("update dg_members set user_id=? where dg_id=? and user_id=?");
            pst.setInt(1, new_prof_id);
            pst.setInt(2, d_id);
            pst.setInt(3, old_prof_id);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
            System.out.println("flag " + flag);
        } catch(Exception e) {
            System.out.println("Exception at updateDGMembers " + e);
        }
        return flag;
    }

    public static void updateCourses(Connection conn){
        String sql_statement = "Select * from courses where status<>'Completed'";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date today_date = new Date();
        try{
            Statement pst = conn.createStatement();
            ResultSet dbrs = pst.executeQuery(sql_statement);
            while (dbrs.next()){
                int course_id = dbrs.getInt("id");
                String endDate_str = dbrs.getString("end_date");
                Date end_date = formatter.parse(endDate_str);
                Date start_date = formatter.parse(dbrs.getString("start_date"));
                if (today_date.compareTo(end_date)>0){
//                    Update course status
                    sql_statement = "UPDATE courses SET status = 'Completed' WHERE id ="+course_id;
                    Statement stmt2 = conn.createStatement();
                    stmt2.executeUpdate(sql_statement);
                    stmt2.close();
                }else if(today_date.compareTo(start_date)>=0){
                    sql_statement = "UPDATE courses SET status = 'Current' WHERE id ="+course_id;
                    Statement stmt2 = conn.createStatement();
                    stmt2.executeUpdate(sql_statement);
                    stmt2.close();
                }
            }
            dbrs.close();
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

    }

    public static int getUserIdFromEmail(String email, Connection conn) {
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

    public static Map<Integer, Map<String, Object>> getAllDisscussionGroups(String searchText, int filter, Connection conn) {
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

    public static Map<Integer, Object> getPendingGroupRequests(int id, Connection conn) {
        Map<Integer, Object> groups = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select dg_id from discussion_group_requests where user_id="+ id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> details = new HashMap<>();
                int dg_id = rs.getInt("dg_id");
                details = getGroupDetailsByGroupId(dg_id, conn);
                groups.put(dg_id, details);
            }
        } catch (Exception e) {
            System.out.println("Exception at getPendingGroupRequests " + e);
        }
        return groups;
    }

    public static ArrayList<Lesson> getLessonsByCourseId(int id, Connection conn){
        ArrayList<Lesson> allLessons = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from lessons where courseId="+ id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Lesson temp = new Lesson(rs.getInt("Id"),
                        rs.getString("name"),
                        rs.getString("requirements"));
                temp.materials = getLearningMaterialsByLessonId(rs.getInt("Id"), conn);
                allLessons.add(temp);
            }
        } catch (Exception e) {
            System.out.println("Exception at getPendingGroupRequests " + e);
        }
        return allLessons;
    }

    public static Lesson getLessonById(int id, Connection conn){
        Lesson lesson = new Lesson(0,null,null);
        try {
            PreparedStatement pst = conn.prepareStatement("select * from lessons where Id="+ id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Lesson temp = new Lesson(rs.getInt("Id"),
                        rs.getString("name"),
                        rs.getString("requirements"));
                lesson = temp;
            }
        } catch (Exception e) {
            System.out.println("Exception at getPendingGroupRequests " + e);
        }
        return lesson;
    }

    public static void createOrUpdateLesson(Lesson value, int courseId, Connection conn){
        try {
            //check if lesson exists by id
            PreparedStatement existCheck = conn.prepareStatement("select * from lessons where Id="+value.getId());
            ResultSet exists = existCheck.executeQuery();
            PreparedStatement lessonStatement;
            if(exists.next()){
                //update
                lessonStatement = conn.prepareStatement("update lessons set name=\""+value.getName()+"\", requirements=\""+value.getRequirements()+"\" where Id="+value.getId());

            } else {
                //add
                lessonStatement = conn.prepareStatement("insert into lessons (courseId, name, requirements) values("+courseId+", "+value.getName()+", "+value.getRequirements()+") ");
            }
            lessonStatement.execute();

            //drop learning materials by lesson id
            PreparedStatement delMat = conn.prepareStatement("delete from learning_materials where lessonId="+value.getId());
            delMat.execute();
            //add all learning materials
            PreparedStatement addMat;
            for(String mat : value.getMaterials()){
                addMat = conn.prepareStatement("insert into learning_materials (lessonId, content) values("+value.getId()+", \""+mat+"\")");
                addMat.execute();
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void createLesson(String name, String req, int courseID, Connection conn){
        try {
            PreparedStatement maxId = conn.prepareStatement("select MAX(Id) from lessons");
            ResultSet rs = maxId.executeQuery();
            rs.next();
            int id = rs.getInt(1) + 1;
            PreparedStatement pst = conn.prepareStatement("insert into lessons (Id,courseId, name, requirements) values("+id+", "+courseID+", \""+name+"\", \""+req+"\")");
            pst.execute();

        } catch (Exception e) {
            System.out.println("Exception at createLesson " + e);
        }
    }
    public static void deleteLesson(int lessonId, Connection conn){
        try {
            //drop learning materials
            PreparedStatement pst = conn.prepareStatement("delete from learning_materials where lessonId="+lessonId);
            pst.execute();

            //drop lesson
            pst = conn.prepareStatement("delete from lessons where Id="+lessonId);
            pst.execute();

        } catch (Exception e) {
            System.out.println("Exception at deleteLesson " + e);
        }

    }

    public static List<String> getLearningMaterialsByLessonId(int id, Connection conn){
        List<String> materials = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from learning_materials where lessonId="+ id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                materials.add(rs.getString("content"));
            }
        } catch (Exception e) {
            System.out.println("Exception at getLearningMaterialsByLessonId " + e);
        }
        return materials;
    }

    public static void createLearningMaterial(int lessonId, String name, Connection conn){
        try{
            PreparedStatement pst = conn.prepareStatement("insert into learning_materials (lessonId, content) values ("+lessonId+", \""+name+"\")");
            pst.execute();
        } catch (Exception e){
            System.out.println("Exception at createLearningMaterial " + e);
        }
    }

    public static void deleteLearningMaterial(int lessonId, String name, Connection conn){
        try{
            PreparedStatement pst = conn.prepareStatement("delete from learning_materials where lessonId="+lessonId+" AND content=\""+name+"\"");
            pst.execute();
        } catch (Exception e){
            System.out.println("Exception at deleteLearningMaterial " + e);
        }
    }

    public static boolean requestToJoinGroup(int id, int dg_id, Connection conn) {
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

    public static boolean deleteRequestForGroup(int id, int dg_id, Connection conn) {
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

    public static boolean verifyEmailofUser(String email, String hash, Connection conn) {
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

    public static boolean deleteDGMember(int user_id, int dg_id, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("delete from dg_members where user_id=? and dg_id=?");
            pst.setInt(1, user_id);
            pst.setInt(2, dg_id);
            int i = pst.executeUpdate();
            if (i != 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at deleteDGMember " + e);
        }
        return flag;
    }

    public static boolean rateUser(int user_id, int rate_value, String feedback, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("insert into user_ratings (userId, score, feedback) VALUES (?,?,?)");
            pst.setInt(1, user_id);
            pst.setInt(2, rate_value);
            pst.setString(3, feedback);
            int i = pst.executeUpdate();
            if (i != 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at rateUser " + e);
        }
        return flag;
    }

    public static Map<Integer, String> viewAllGroupMembers(int dg_id, Connection conn) {
        Map<Integer, String> members = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select user_id, First_Name, Last_Name from " +
                    "dg_members, user_details where user_id=Id and dg_id="+ dg_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                int user_id = rs.getInt("user_id");
                members.put(user_id, name);
            }
        } catch (Exception e) {
            System.out.println("Exception at viewAllGroupMembers " + e);
        }
        return members;
    }

    public static Map<Integer, String> getAllPendingGroupRequestsOfGroup(int dg_id, Connection conn) {
        Map<Integer, String> requests = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select user_id, First_Name, Last_Name from " +
                    "discussion_group_requests, user_details where user_id=Id and dg_id="+ dg_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                int user_id = rs.getInt("user_id");
                requests.put(user_id, name);
            }
        } catch (Exception e) {
            System.out.println("Exception at getAllPendingGroupRequestsOfGroup " + e);
        }
        return requests;
    }

    public static ArrayList<Course> getMyCourses(int id, Connection conn) {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("select courseId from enrollments where userId="+ id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course details = findCourseByCourseId(String.valueOf(rs.getInt("courseId")), conn);
                courses.add(details);
            }

        } catch (Exception e) {
            System.out.println("Exception at getMyCourses");
        }
        return courses;
    }

    public static boolean updateCourseRequirements(int courseId, String req, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("update courses set requirements=? where id=?");
            pst.setString(1, req);
            pst.setInt(2, courseId);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch (Exception e) {
            System.out.println("Error at updateCourseRequirements " + e);
        }
        return flag;
    }

    public static boolean rateCourse(int courseId, int rate_value, String feedback, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("insert into course_ratings (course_id, score, feedback) VALUES (?,?,?)");
            pst.setInt(1, courseId);
            pst.setInt(2, rate_value);
            pst.setString(3, feedback);
            int i = pst.executeUpdate();
            if (i != 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("Exception at rateCourse " + e);
        }
        return flag;
    }

    public static Map<Integer, Map<String,String>> getClassList(int courseId, Connection conn) {
        Map<Integer, Map<String,String>> classList = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select userId, First_Name, Last_Name, Email from " +
                    "enrollments, user_details where enrollments.userId = user_details.Id and courseId="+ courseId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                String email = rs.getString("Email");
                int id = rs.getInt("userId");
                Map<String,String> details = new HashMap<>();
                details.put("name", name);
                details.put("email", email);
                classList.put(id, details);
            }
        } catch (Exception e) {
            System.out.println("Exception at getClassList " + e);
        }
        return classList;
    }

    public static int enroll(int course_id, int prof_id, Connection conn) {
       int i = 0;
        try {
            PreparedStatement pst = conn.prepareStatement("insert into enrollments (userId, courseId) VALUES (?,?)");
            pst.setInt(1, prof_id);
            pst.setInt(2, course_id);
            i = pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception at enroll " + e);
        }
        return i;
    }

    public static boolean updateEnroll(int course_id, int prof_id, int oldProfId, Connection conn) {
        boolean flag = false;
        try {
            PreparedStatement pst = conn.prepareStatement("update enrollments set userId=? where userId=? and courseId=?");
            pst.setInt(1, prof_id);
            pst.setInt(2, oldProfId);
            pst.setInt(3, course_id);
            int i = pst.executeUpdate();
            if (i != 0) flag = true;
        } catch (Exception e) {
            System.out.println("Exception at updateEnroll " + e);
        }
        return flag;
    }

    public static String getNameFromUserId(int learner_id, Connection conn) {
        String name = "";
        try {
            PreparedStatement pst = conn.prepareStatement("select First_Name, Last_Name from user_details where Id="+learner_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) name = rs.getString("First_Name") + " " + rs.getString("Last_Name");
        } catch (Exception e) {
            System.out.println("Exception at getNameFromUserId " + e);
        }
        return name;
    }

    public static ArrayList<DiscussionPost> getDGPostsById(int dgId, Connection conn){
        ArrayList<DiscussionPost> allPosts = new ArrayList<>();
        try {
            PreparedStatement pst = conn.prepareStatement("select * from discussion_group_content where group_id="+ dgId +" ORDER BY post_time DESC");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DiscussionPost temp = new DiscussionPost(
                        rs.getInt("group_id"),
                        getNameFromUserId(rs.getInt("user_id"),conn),
                        rs.getString("post_name"),
                        rs.getString("post_content"),
                        rs.getString("post_time"),
                        rs.getString("post_attachment"));
                allPosts.add(temp);
                System.out.println(temp.getPostAttachment());
            }
        } catch (Exception e) {
            System.out.println("Exception at getPendingGroupRequests " + e);
        }
        return allPosts;
    }

    public static void createDGPost(int groupId, int userId, String postName, String postContent, Connection conn){
        try{
            PreparedStatement pst = conn.prepareStatement("insert into discussion_group_content (group_id, user_id, post_time, post_name, post_content) VALUES (?,?,?,?,?)");
            pst.setInt(1, groupId);
            pst.setInt(2, userId);
            pst.setString(3, String.valueOf(java.time.LocalDateTime.now()));//GET TIME HERE
            pst.setString(4, postName);
            pst.setString(5, postContent);
            pst.execute();
        } catch( Exception e) {
            System.out.println("Exception at createDGPostLesson " + e);
        }
    }

    public static void createDGPostLesson(int groupId, int userId, String postName, String postContent, String postAttachment, Connection conn){
        try{
            PreparedStatement pst = conn.prepareStatement("insert into discussion_group_content (group_id, user_id, post_time, post_name, post_content, post_attachment) VALUES (?,?,?,?,?,?)");
            pst.setInt(1, groupId);
            pst.setInt(2, userId);
            pst.setString(3, String.valueOf(java.time.LocalDateTime.now()));//GET TIME HERE
            pst.setString(4, postName);
            pst.setString(5, postContent);
            pst.setString(6, postAttachment);
            pst.execute();
        } catch( Exception e) {
            System.out.println("Exception at createDGPostLesson " + e);
        }

    }

    public static int getDGIdByCourseId(int courseId,Connection conn){
        try{
            PreparedStatement pst = conn.prepareStatement("select id from discussion_groups where course_id=?");
            pst.setInt(1, courseId);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                return rs.getInt("id");
            }

        } catch( Exception e) {
            System.out.println("Exception at createDGPostLesson " + e);
        }
        return 0;
    }

    public static String getGradesOfUser(int userId, int courseId, int id, Integer quizId, Connection conn) {
        String grade = "Not Attempted";
        try{
            PreparedStatement pst = conn.prepareStatement("select score from grades where userId=? and courseId=? " +
                    "and lessonId=? and quizId=?");
            pst.setInt(1, userId);
            pst.setInt(2, courseId);
            pst.setInt(3, id);
            pst.setInt(4, quizId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) grade = String.valueOf(rs.getInt("score"));

        } catch( Exception e) {
            System.out.println("Exception at createDGPostLesson " + e);
        }
        return grade;
    }
}
