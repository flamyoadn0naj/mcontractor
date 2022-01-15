package com.my_pls.application.components;

import com.my_pls.securePassword;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Login {
    public static Map<String,Object> getMethodDefaults() {
        Map<String,Object> map = new HashMap<>();
        map.put("actionLink", "/login");
        map.put("errorEmail", "");
        map.put("errorPassMatch", "");
        map.put("loginErr", "");
        map.put("emailVal","");
        map.put("pageType","Login");
        map.put("styleVal", "margin-top:5%; width:45%");
        return map;
    }


    public static Pair postMethodDefaults(Map<String, Object> map, Map<String, String> formFields, User user, securePassword pwd_manager, Connection conn) {
        if (formFields.size() > 0) {
            if (!formFields.get("email").contains("rit.edu")) {
                map.put("errorEmail", "display:list-item;margin-left:5%");
                map.put("emailVal","");
                map.put("loginErr", "");
            } else {
                String emVal = null;
                try {
                    emVal = URLDecoder.decode(formFields.get("email"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String input_password = formFields.get("pass");
                user = Proxy.login(input_password, emVal, pwd_manager, conn);
                if (user.firstName.isEmpty()) {
                    map.put("loginErr", "display:list-item;margin-left:5%");
                    map.put("errorEmail", "");
                    map.put("emailVal", emVal);
                } else {
                    Proxy.updateCourses(conn);
                    map.put("loginErr", "");
                    map.put("errorEmail", "");
                    map.put("emailVal", emVal);
                }
            }
        } else {
            map.put("loginErr", "");
            map.put("emailVal", "");
        }
        map.put("actionLink", "/login");
        map.put("errorPassMatch", "");
        map.put("pageType","Login");
        map.put("styleVal", "margin-top:5%; width:45%");

        return new Pair(map,user);
    }
}
