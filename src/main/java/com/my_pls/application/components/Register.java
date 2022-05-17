package com.my_pls.application.components;

import com.my_pls.securePassword;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class Register {
    public static Map<String, Object> getMethodDefaults() {
        Map<String, Object> map = new HashMap<>();
        map.put("actionLink", "/register");
        map.put("loginErr", "");
        map.put("errorEmail", "");
        map.put("errorPassMatch", "");
        map.put("fname","");
        map.put("lname","");
        map.put("emailVal","");
        map.put("pageType","Register");
        map.put("loading","false");
        map.put("styleVal", "margin-top:5%; width:45%");
        return map;
    }

    public static Pair postMethodDefaults(Map<String, String> formFields, User user, securePassword pwd_manager, Connection conn) throws UnsupportedEncodingException {
        Map<String,Object> map = new HashMap<>();
        map.put("actionLink", "/register");
        map.put("loginErr", "");
        String email = URLDecoder.decode(formFields.get("email"),"UTF-8");
        String password = formFields.get("pass");
        password = pwd_manager.hashPassword(password);
        String fName = URLDecoder.decode(formFields.get("firstName"),"UTF-8").trim();
        String lName = URLDecoder.decode(formFields.get("lastName"), "UTF-8").trim();


        if (formFields.size() > 0) {
            Boolean flag = true;
            if (!formFields.get("email").contains("rit.edu")) {
                map.put("errorEmail", "display:list-item;margin-left:5%");
                map.put("emailVal","");
                flag = false;
            } else {
                map.put("errorEmail", "");
                String emVal = formFields.get("email");
                try {
                    emVal = URLDecoder.decode(emVal, "UTF-8");
                } catch (Exception e) {}
                map.put("emailVal",emVal);
            }
            if (formFields.get("pass").equals(formFields.get("retPass")) && formFields.get("pass").length() >= 6) {
                map.put("errorPassMatch", "");
            } else {
                flag = false;
                map.put("errorPassMatch", "display:list-item;margin-left:5%");
            }
            if (flag) {
                map.put("loading","true");
                if (!Proxy.register(fName, lName, email, password, conn)) {
                    map.put("dbErr", "true");
                    map.put("fname",formFields.get("firstName"));
                    map.put("lname",formFields.get("lastName"));
                    map.put("emailVal",email);
                } else {
                    user.setAll(fName, lName, email, password, -1, "learner");
                }

            } else {
                map.put("fname",formFields.get("firstName"));
                map.put("lname",formFields.get("lastName"));
            }
        }
        map.put("loading","false");
        map.put("pageType","Register");
        map.put("styleVal", "margin-top:5%; width:45%");

        return new Pair(map,user);
    }

}
