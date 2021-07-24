package com.my_pls.application.components;

import com.my_pls.MySqlConnection;
import com.my_pls.application.App;
import com.my_pls.securePassword;
import com.my_pls.sendEmail;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public static Pair postMethodDefaults(Map<String,String> formFields, App.CurrUser user, securePassword pwd_manager) throws UnsupportedEncodingException {
        Map<String,Object> map = new HashMap<>();
        map.put("actionLink", "/register");
        map.put("loginErr", "");

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
                String email = URLDecoder.decode(formFields.get("email"),"UTF-8");
                String password = formFields.get("pass");
                password = pwd_manager.hashPassword(password);
                String fName = URLDecoder.decode(formFields.get("firstName"),"UTF-8");
                String lName = URLDecoder.decode(formFields.get("lastName"), "UTF-8");
                Random theRandom = new Random();
                theRandom.nextInt(999999);
                String myHash = DigestUtils.md5Hex("" +	theRandom);

                try {

                    String sqlQuery = "insert into user_details (First_Name,Last_Name,Email,Password,Hash,Active) values(?,?,?,?,?,?)";
                    Connection conn = MySqlConnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement(sqlQuery);
                    pst.setString(1, fName);
                    pst.setString(2, lName);
                    pst.setString(3, email);
                    pst.setString(4, password);
                    pst.setString(5, myHash);
                    pst.setInt(6, 0);
                    int i = pst.executeUpdate();
                    String body =  "Click this link to confirm your email address and complete setup for your account." + "\n\nVerification Link: " + "http://localhost:8080/verify-register/confirm?key1=" + email + "&key2=" + myHash;
                    if (i != 0) {

                        sendEmail se = new sendEmail();
                        se.sendEmail_content(email,"Verify Email at MyPLS",body);
                        user.setAll(fName, lName, email, password);
                    }
                } catch (Exception e) {
                    System.out.println("Error at Registration: " + e);
                    map.put("dbErr", "true");
                    map.put("fname",formFields.get("firstName"));
                    map.put("lname",formFields.get("lastName"));
                    map.put("emailVal",email);
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
