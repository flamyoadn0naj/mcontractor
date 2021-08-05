package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Admin {

    private static Connection conn = MySqlConnection.getConnection();

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

    public static Map<Integer, String> viewUsers(String searchText, String filterBy) {
        Map<Integer,String> users = new HashMap<>();
        Function<String,String> addQuotes = s -> "\"" + s + "\"";
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

    public static Map<String,String> getSearchOptions(String filterBy) {
        Map<String,String> options = new HashMap<String, String>();
        options.put("all", "All");
        options.put("prof","Professor");
        options.put("learner","Learner");
        if (filterBy.length() > 0) options.remove(filterBy);
        return options;
    }

    public static String mapFilterKey(String filterBy) {
        String val = "";
        switch (filterBy) {
            case "all":
                val = "All";
                break;
            case "prof":
                val = "Professor";
                break;
            case "learner":
                val = "Learner";
                break;
            case "admin":
                val = "Administrator";
                break;
            default:
                throw new IllegalArgumentException("Invalid filter val admin mapKeyFilter");
        }
        return val;
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

    public static Map<String, Object> postMethodFunctionality(Map<String, String> formFields) {
        Map<String,Object> map = new HashMap<>();
        String filterBy = "";
        String searchText = "";
        boolean flag = false;

        Map<Integer,String> profs = viewAllRequests();
        map.put("profs",profs);

        Map<String,String> searchOptions = getSearchOptions("");
        map.put("searchOptions",searchOptions);
        if (formFields.containsKey("approve")) flag = approveProf(formFields.get("approve"));
        if (formFields.containsKey("deny")) flag = deleteReq(formFields.get("deny"));
        if (flag) map.put("redirect",true);
        if (formFields.containsKey("approve") || formFields.containsKey("deny") && !flag) map.put("err",true);
        if (formFields.containsKey("searchText")) {
            filterBy = formFields.get("filterBy");
            try {
                searchText = URLDecoder.decode(formFields.get("searchText"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Map<Integer, String> users = viewUsers(searchText, filterBy);
            map.put("searchText",searchText);
            searchOptions = getSearchOptions(filterBy);
            map.put("searchOptions",searchOptions);
            map.put("filterKey",filterBy);
            map.put("filterVal",Admin.mapFilterKey(filterBy));
            if (!users.isEmpty()) {
                map.put("users",users);
            } else {
                map.put("noUser",true);
            }
        }
        if (formFields.containsKey("admin"))
            if (Admin.authorize(formFields.get("admin"))) {
                map.put("success", true);
                Map<Integer, String> users = viewUsers(searchText, filterBy);
                if (!users.isEmpty()) {
                    map.put("users", users);
                }
            }
            else map.put("err", true);
        map.put("role", "admin");

        return map;
    }
}
