package com.my_pls.application.components;

import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
public class CreateCourse {

    public static LinkedHashMap<String,Boolean> findAllDays() {
        LinkedHashMap<String, Boolean> allDays = new LinkedHashMap<String, Boolean>();
        allDays.put("Monday",false);
        allDays.put("Tuesday",false);
        allDays.put("Wednesday",false);
        allDays.put("Thursday",false);
        allDays.put("Friday",false);
        return allDays;
    }

    public static Map<String,Object> postMethodDefaults(Map<String, String> formFields, String edit) {
        Map<String,Object> map = new HashMap<>();
        boolean flag = true;
        Map<Integer,String> profs = DataMapper.findAllProfs();
        Map.Entry<Integer,String> entry = profs.entrySet().iterator().next();
        int prof_id = entry.getKey();
        LinkedHashMap<String, Boolean> allDays = findAllDays();
        map.put("e",edit);
        try {
            String startTime = URLDecoder.decode(formFields.get("start_time"), "UTF-8");
            String endTime = URLDecoder.decode(formFields.get("end_time"), "UTF-8");
            String startDate = formFields.get("start_date");
            String endDate = formFields.get("end_date");
            String name = URLDecoder.decode(formFields.get("name"), "UTF-8");
            prof_id = Integer.parseInt(formFields.get("prof"));
            String obj = URLDecoder.decode(formFields.get("obj"), "UTF-8");
            ArrayList<String> days = new ArrayList<String>();
            if (formFields.containsKey("Monday")) days.add("Monday");
            if (formFields.containsKey("Tuesday")) days.add("Tuesday");
            if (formFields.containsKey("Wednesday")) days.add("Wednesday");
            if (formFields.containsKey("Thursday")) days.add("Thursday");
            if (formFields.containsKey("Friday")) days.add("Friday");
            String daysString = String.join(", ", days);
            int credits = Integer.parseInt(formFields.get("credits"));
            int capacity = Integer.parseInt(formFields.get("capacity"));
            Integer prereq = Integer.parseInt(formFields.get("reqs"));
            if (prereq == -1) prereq = null;
            else map.put("prereq_course", prereq);
            int old_profId = -1;

            map.put("name",name);
            map.put("obj",obj);
            map.put("start_date",startDate);
            map.put("end_date",endDate);
            map.put("start_time",startTime);
            map.put("end_time",endTime);
            map.put("credits",String.valueOf(credits));
            map.put("cap",String.valueOf(capacity));

            allDays.forEach((k, v)-> {
                if (days.contains(k)) allDays.put(k,true);
            });

            if(LocalTime.parse(endTime).isBefore(LocalTime.parse(startTime))) {
                map.put("errTime", "true");
                map.put("start_time","");
                map.put("end_time","");
                flag = false;
            }
            if (!edit.contentEquals("-1")) {
                if(LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate))) {
                    map.put("errDate", "true");
                    map.put("start_date","");
                    map.put("end_date","");
                    flag = false;
                }
                old_profId = (int) DataMapper.findCourseByCourseId(edit).get("prof_id");
            } else {
                if(LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate))
                        || LocalDate.parse(startDate).isBefore(LocalDate.now())) {
                    map.put("errDate", "true");
                    map.put("start_date","");
                    map.put("end_date","");
                    flag = false;
                }
            }

            if(flag) {
                boolean flag2 = DataMapper.createOrUpdateCourse(edit, name, prof_id, daysString, startTime, endTime,
                        startDate, endDate, credits, capacity, obj, prereq);
                if(flag2) {
                    if (edit.contentEquals("-1"))
                        flag = addDiscussionGroupForCourse(name, prof_id);
                    else flag = editDiscussionGroupForCourse(name, prof_id, Integer.parseInt(edit), old_profId);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception at create courses " + e);
            flag = false;
        }
        map.put("days",allDays);
        map.put("currProf",profs.get(prof_id));
        map.put("prof_id",prof_id);
        profs.remove(prof_id);
        map.put("profList",profs);
        map.put("created",flag);
        return map;
    }

    public static Map<String,Object> editCourse(Map<String,Object> map,String id, Map<Integer, String> allCourses) {
        Map<Integer,String> profs = DataMapper.findAllProfs();
        Map.Entry<Integer,String> entry = profs.entrySet().iterator().next();
        int prof_id = entry.getKey();
        LinkedHashMap<String, Boolean> days = findAllDays();
        map.putAll(DataMapper.findCourseByCourseId(id));
        prof_id = (int) map.get("prof_id");
        String meeting_days = String.valueOf(map.get("meeting_days"));
        days.forEach((k, v)-> {
            if (meeting_days.contains(k)) days.put(k,true);
        });
        map.put("currProf",profs.get(prof_id));
        profs.remove(prof_id);
        map.put("days",days);
        map.put("profList",profs);
        return map;
    }

    public static boolean addDiscussionGroupForCourse(String name, int prof_id) {
        boolean flag = false;
        int id = DataMapper.findLastInsertedId("courses");
        if (id != -1) {
            int i = DataMapper.addDiscussionGroup(name, id, 1);
            if (i != 0) {
                int d_id = DataMapper.findLastInsertedId("discussion_groups");
                if (d_id != -1) {
                    flag = DataMapper.addDGmember(prof_id, d_id);
                }
            }
        }
        return  flag;
    }

    private static boolean editDiscussionGroupForCourse(String name, int prof_id, int course_id, int old_profId) {
        boolean flag = false;
        int d_id = DataMapper.findDiscussionGroupIdByCourseId(course_id);
        boolean flag2 = DataMapper.updateDGMembers(old_profId, prof_id, d_id);
        if (flag2) flag = DataMapper.updateDiscussionGroup(d_id, name);
        return flag;
    }

    public static Map<Integer, String> allCourses() {
        Map<Integer, String> all_Courses = new HashMap<>();
        ArrayList<Map<String, String>> courses = DataMapper.viewCourses("Completed");
        for (Map<String, String> c: courses) {
            int id = Integer.parseInt(c.get("id"));
            String name = c.get("name");
            all_Courses.put(id, name);
        }
        return all_Courses;
    }

}
