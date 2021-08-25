package com.my_pls.application.components;


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

}
