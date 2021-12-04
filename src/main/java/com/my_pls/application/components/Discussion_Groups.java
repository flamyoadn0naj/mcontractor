package com.my_pls.application.components;


//import javax.persistence.criteria.CriteriaBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Discussion_Groups {
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
        Map<Integer, Object> requestedGroups = DataMapper.getPendingGroupRequests(id);
        for (Map<String, Object> g: myGroups) {
            int g_id = (int) g.get("id");
            if (allGroups.containsKey(g_id)) allGroups.remove(g_id);
        }
        for (Integer i : requestedGroups.keySet()) {
            if (allGroups.containsKey(i)) allGroups.remove(i);
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
        Map<Integer, Object> requestedGroups = DataMapper.getPendingGroupRequests(id);

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

        if (formFields.containsKey("join")) {
            int groupId = Integer.parseInt(formFields.get("join"));
            boolean flag = DataMapper.addDGmember(id, groupId);
            map.put("refresh", flag);
        }

        if (formFields.containsKey("request")) {
            int groupId = Integer.parseInt(formFields.get("request"));
            boolean flag = DataMapper.requestToJoinGroup(id, groupId);
            map.put("refresh", flag);
        }

        if (formFields.containsKey("cancel")) {
            int groupId = Integer.parseInt(formFields.get("cancel"));
            boolean flag = DataMapper.deleteRequestForGroup(id, groupId);
            map.put("refresh", flag);
        }

        if (formFields.containsKey("leave")) {
            int groupId = Integer.parseInt(formFields.get("leave"));
            boolean flag = DataMapper.deleteDGMember(id, groupId);
            map.put("refresh", flag);
        }

        map.put("groups", groups);
        map.put("filterOptions", searchOptions);
        if (requestedGroups.isEmpty()) map.put("emptyReq", true);
        else map.put("requestedGroups",requestedGroups);
        return map;
    }

    public static String isMemberOfGroup(int user_id, int id) {
        String member = "no";
        ArrayList<Map<String, Object>> myGroups = DataMapper.getMyDiscussionGroups(user_id);
        Map<Integer, Object> requestedGroups = DataMapper.getPendingGroupRequests(user_id);
        for (Map<String, Object> g: myGroups) {
            int g_id = (int) g.get("id");
            if (g_id == id) member = "yes";
        }
        for (Integer i : requestedGroups.keySet()) {
            if (i == id) member = "req";
        }
        return member;
    }

    public static int findProfofCourse(Map<String, Object> group) {
        int id = 0;
        if (group.containsKey("course")) {
            int courseId = (int) group.get("course_id");
            Map<String, Object> course = DataMapper.findCourseByCourseId(String.valueOf(courseId));
            id = (int) course.get("prof_id");
        }
        return id;
    }
}
