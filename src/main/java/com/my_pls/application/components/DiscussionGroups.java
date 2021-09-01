package com.my_pls.application.components;


import javax.persistence.criteria.CriteriaBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscussionGroups {
    public static boolean createGroup(Map<String, String> formFields, int user_id) {
        boolean flag = false;
        int privacy = Integer.parseInt(formFields.get("customRadio"));
        String name = null;
        try {
            name = URLDecoder.decode(formFields.get("name"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int i = DataMapper.addDiscussionGroup(name, -1, privacy);
        if (i != -1) {
            int d_id = DataMapper.findLastInsertedId("discussion_groups");
            if (d_id != -1) {
                flag = DataMapper.addDGmember(user_id, d_id);
            }
        }
        return flag;
    }

    public static Map<Integer,String> getSearchOptions(String filterBy) {
        Map<Integer,String> options = new HashMap<Integer, String>();
        options.put(-1, "All");
        options.put(0,"Public");
        options.put(1,"Private");
        if (filterBy.length() > 0) options.remove(Integer.parseInt(filterBy));
        return options;
    }

    public static Map<Integer,Map<String, Object>> getGroups(String searchText, int filter, int id) {
        Map<Integer,Map<String, Object>> allGroups = DataMapper.getAllDisscussionGroups(searchText, filter);
        ArrayList<Map<String, Object>> myGroups = DataMapper.getMyDiscussionGroups(id);
        for (Map<String, Object> g: myGroups) {
            int g_id = (int) g.get("id");
            if (allGroups.containsKey(g_id)) allGroups.remove(g_id);
        }
        return allGroups;
    }

    public static String mapFilterKey(String filterBy) {
        String val = "";
        switch (filterBy) {
            case "-1":
                val = "All";
                break;
            case "1":
                val = "Private";
                break;
            case "0":
                val = "Public";
                break;
            default:
                throw new IllegalArgumentException("Invalid filter val admin mapKeyFilter");
        }
        return val;
    }

    public static Map<String, Object> postMethodFunctionality (Map<String, String> formFields, int id) {
        Map<String,Object> map = new HashMap<>();
        ArrayList<Map<String,Object>> groups = DataMapper.getMyDiscussionGroups(141);
        Map<Integer, String> searchOptions = getSearchOptions("");
        Map<Integer,Map<String, Object>> allGroups = getGroups("", -1, id);

        if (formFields.containsKey("searchText")) {
            String searchText = formFields.get("searchText");
            String filter = formFields.get("filterBy");
            allGroups = getGroups(searchText, Integer.parseInt(filter), id);
            searchOptions = getSearchOptions(filter);
            map.put("searchText", searchText);
            map.put("filter", filter);
            map.put("filterStatus", DiscussionGroups.mapFilterKey(filter));
        }
        if (!allGroups.isEmpty()) map.put("allGroups", allGroups);

        map.put("groups", groups);
        map.put("filterOptions", searchOptions);
        return map;
    }
}
