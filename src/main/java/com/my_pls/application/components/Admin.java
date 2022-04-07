package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class Admin {

    private static Connection conn = MySqlConnection.getConnection();

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

    public static Map<String, Object> postMethodFunctionality(Map<String, String> formFields, Connection conn) {
        Map<String,Object> map = new HashMap<>();
        String filterBy = "";
        String searchText = "";
        boolean flag = false;

        Map<Integer,String> profs = DataMapper.viewAllRequests(conn);
        map.put("profs",profs);

        Map<String,String> searchOptions = getSearchOptions("");
        map.put("searchOptions",searchOptions);
        if (formFields.containsKey("approve")) flag = DataMapper.approveProf(formFields.get("approve"), conn);
        if (formFields.containsKey("deny")) flag = DataMapper.deleteReq(formFields.get("deny"), conn);
        if (flag) map.put("redirect",true);
        if (formFields.containsKey("approve") || formFields.containsKey("deny") && !flag) map.put("err",true);
        if (formFields.containsKey("searchText")) {
            filterBy = formFields.get("filterBy");
            try {
                searchText = URLDecoder.decode(formFields.get("searchText"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Map<Integer, String> users = DataMapper.viewUsers(searchText, filterBy, conn);
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
            if (DataMapper.authorize(formFields.get("admin"), conn)) {
                map.put("success", true);
                Map<Integer, String> users = DataMapper.viewUsers("", "", conn);
                if (!users.isEmpty()) {
                    map.put("users", users);
                }
            }
            else map.put("err", true);
        map.put("role", "admin");

        return map;
    }
}
