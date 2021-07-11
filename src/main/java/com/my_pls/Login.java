package com.my_pls;

import com.my_pls.demo.App;
import com.my_pls.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        map.put("styleVal", "margin-top:12%; width:45%");
        return map;
    }

    public static Pair postMethodDefaults(Map<String,String> formFields, App.CurrUser user,securePassword pwd_manager) {
        Map<String,Object> map = new HashMap<>();
        if (formFields.size() > 0) {
            if (!formFields.get("email").contains("rit.edu")) {
                map.put("errorEmail", "display:list-item;margin-left:5%");
                map.put("emailVal","");
                map.put("loginErr", "");
            } else {
                String emVal = formFields.get("email");
                try {
                    emVal = URLDecoder.decode(emVal, "UTF-8");
                    Connection conn = MySqlConnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement("select * from user_details where Email=?");
                    pst.setString(1, emVal);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()) {
                        String db_password = rs.getString("Password");
                        String input_password = formFields.get("pass");
                        if (pwd_manager.comparePassword(db_password,input_password)){
                            //everything good password matches with db
                            user.setAll(rs.getString("First_Name"),
                                    rs.getString("Last_Name"), db_password, emVal);
                        }
                        else
                        {
                            map.put("loginErr", "display:list-item;margin-left:5%");
                            map.put("errorEmail", "");
                            map.put("emailVal",emVal);
                        }
                    }
                    else{
                        map.put("loginErr", "display:list-item;margin-left:5%");
                        map.put("errorEmail", "");
                        map.put("emailVal",emVal);
                    }
                } catch (Exception e) {
                    System.out.println("Error at Login post");
                }
            }
        } else {
            map.put("loginErr", "");
            map.put("emailVal", "");
        }
        map.put("actionLink", "/login");
        map.put("errorPassMatch", "");
        map.put("pageType","Login");
        map.put("styleVal", "margin-top:12%; width:45%");
        return new Pair(map,user);
    }
}