package com.my_pls.application.components;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscussionGroups {
    public static boolean createGroup(Map<String, String> formFields, String email) {
        boolean flag = false;
        int privacy = Integer.parseInt(formFields.get("customRadio"));
        String name = formFields.get("name");
        int i = DataMapper.addDiscussionGroup(name, -1, privacy);
        if (i != -1) {
            int d_id = DataMapper.findLastInsertedId("discussion_groups");
            if (d_id != -1) {
                int user_id = DataMapper.getUserIdFromEmail(email);
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

    public static Map<Integer,Map<String, Object>> getGroups(String searchText, int filter, String email) {
        Map<Integer,Map<String, Object>> allGroups = DataMapper.getAllDisscussionGroups(searchText, filter);
        int id = DataMapper.getUserIdFromEmail(email);
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

    public static Map<String, Object> postMethodFunctionality (Map<String, String> formFields, String email) {
        Map<String,Object> map = new HashMap<>();
        ArrayList<Map<String,Object>> groups = DataMapper.getMyDiscussionGroups(141);
        Map<Integer, String> searchOptions = getSearchOptions("");
        Map<Integer,Map<String, Object>> allGroups = getGroups("", -1, email);

        if (formFields.containsKey("searchText")) {
            String searchText = formFields.get("searchText");
            String filter = formFields.get("filterBy");
            allGroups = getGroups(searchText, Integer.parseInt(filter), email);
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
