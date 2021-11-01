package com.my_pls.application.components;

import com.my_pls.MySqlConnection;
import com.my_pls.application.App;
import com.my_pls.securePassword;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

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


    public static Pair postMethodDefaults(Map<String, Object> map, Map<String,String> formFields, User user, securePassword pwd_manager) {
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
                user = DataMapper.login(input_password, emVal, pwd_manager);
                if (user.firstName.isEmpty()) {
                    map.put("loginErr", "display:list-item;margin-left:5%");
                    map.put("errorEmail", "");
                    map.put("emailVal", emVal);
                } else {
                    Connection conn = MySqlConnection.getConnection();
                    DataMapper.updateCourses(conn);
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
