package com.my_pls.application.components;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class Learner {
    public static Map<String,Object> checkForErrors(String fname, String lname,
                                        Map<String,String> formfields) {
        Map <String,Object> map = new HashMap<String, Object>();
        try {
            map.put("applyProfErr",false);
            String name = URLDecoder.decode(formfields.get("name"), "UTF-8");
            String fullName = fname + " " + lname;
            if (!name.equals(fullName)) {
                map.put("nameErr", true);
                map.put("applyProfErr", true);
            }
            if (formfields.get("customRadio").equals("no")) {
                String err_msg = "You have indicated that you do not be a professor. " +
                        "Please return to the homepage or fill in the form again.";
                map.put("errMsg", err_msg);
                map.put("applyProfErr", true);
            }
        } catch (Exception e) {
            System.out.println("Exception at apply Prof " + e);
        }
        return map;
    }


    public static Map<String, Object> applyForProfessor(Map<String, String> formFields,
                                                        String fname, String lname, String email, Connection conn) {
        Map<String,Object> map = checkForErrors(fname, lname, formFields);
        boolean err = (boolean) map.get("applyProfErr");
        if (!err) {
            String applied = Proxy.applyProf(email, conn);
            if (applied.equals("true")) {
                map.put("success", true);
                map.put("disable",true);
            }
            else if (applied.equals("false")) map.put("errMsg", "Something went wrong, please try again.");
            else {
                map.put("errMsg", "You have already applied to be a professor");
                map.put("disable",true);
            }
        }
        return map;
    }
}
