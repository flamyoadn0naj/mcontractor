package com.my_pls.application.components;

import com.my_pls.MySqlConnection;
import com.my_pls.securePassword;
import com.my_pls.sendEmail;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgetPassword {
    public static Map<String,Object> getMethodDefaults(String pageType) {
        Map<String,Object> map = new HashMap<>();
        map.put("actionLink", ("/forgot-password/" + pageType));
        map.put("success", "false");
        map.put("succMsg", "");
        map.put("errorEmail", "");
        map.put("errorPassMatch", "");
        map.put("pageType", pageType);
        return map;
    }

    public static Map<String,Object> postMethodDefaults(String pageType, Map<String,String> formFields, securePassword pwd_manager) {
        Map<String,Object> map = new HashMap<>();
        if (pageType.equals("email")) {
            map.put("errorPassMatch", "");
            String email = formFields.get("email");
            if (!email.contains("rit.edu")) {
                map.put("errorEmail", "display:block;margin-left:5%; width:90%");
                map.put("emailVal","");
                map.put("success", "false");
                map.put("succMsg", "");
            } else {
                try {
                    email = URLDecoder.decode(email, "UTF-8");
                    Connection conn = MySqlConnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement("select Email, Hash, Active from user_details where Email=?");
                    pst.setString(1, email);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        Random theRandom = new Random();
                        String code = String.format("%04d", theRandom.nextInt(10000));
                        PreparedStatement pst1 = conn.prepareStatement("update user_details set Active='0',Hash=? where Email=?");
                        pst1.setString(1, code);
                        pst1.setString(2, email);
                        int i = pst1.executeUpdate();
                        String body = "Here is the confirmation code to reset your password at MyPLS. Confirmation code is " + code + "\n\nVisit: " + "http://localhost:8080/forgot-password/password to reset your password";
                        if (i != 0) {
                            sendEmail se = new sendEmail();
                            se.sendEmail_content(email, "Reset Password Email at MyPLS - Confirmation Code " + code, body);
                        }
                    } else {
                        //Fail message if email is not found
                        map.put("errorEmail", "display:block;margin-left:5%; width:90%");
                        map.put("emailVal", "");
                        map.put("success", "false");
                        map.put("succMsg", "");
                    }
                } catch (Exception e) {
                    System.out.println("Exception at Forget Passoword " + e);
                }
                map.put("errorEmail", "");
                map.put("success", "true");
                map.put("succMsg", "A verification link has been emailed to you!");
            }
        }
        if (pageType.equals("password")) {
            map.put("errorEmail", "");
//               Did not work, when page refreshed (i.e. clicking button) this info is lost.
//               String email = request.queryParams("key1");
//               email = URLDecoder.decode(email,"UTF-8");
//               String hash = request.queryParams("key2");
//               System.out.println(hash);
//               System.out.println(email);
            String confirmCode = formFields.get("confirmCode");
            String email = formFields.get("email");
            try {
                email = URLDecoder.decode(email,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (formFields.get("pass").equals(formFields.get("retPass")) && formFields.get("pass").length() >= 6 && confirmCode.length() == 4) {
                try {
                    Connection conn = MySqlConnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement("select * from user_details where Hash=? and email=? and Active='0'");
                    pst.setString(1, confirmCode);
                    pst.setString(2, email);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()) {
                        String newPassword = formFields.get("pass");
                        newPassword = pwd_manager.hashPassword(newPassword);
                        PreparedStatement pst1 = conn.prepareStatement("update user_details set Active='1',Password=? where Hash=? and email=?");
                        pst1.setString(1, newPassword);
                        pst1.setString(2, confirmCode);
                        pst1.setString(3, email);
                        int i = pst1.executeUpdate();
                        map.put("errorPassMatch", "");
                        map.put("success", "true");
                        map.put("succMsg", "Your password has been changed. Please log in again.");
                    }
                } catch(Exception e){
                    System.out.println("Error at Forget Password " + e);
                    map.put("errorLink","true");
                    map.put("success", "false");
                    map.put("succMsg", "");
                }
                map.put("errorPassMatch", "");
            } else {
                map.put("errorPassMatch", "display:block;margin-left:5%; width:90%");
                map.put("success", "false");
                map.put("succMsg", "");
            }
        }
        map.put("pageType", pageType);
        map.put("actionLink", ("/forgot-password/" + pageType));
        return map;
    }
}
