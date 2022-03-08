package com.my_pls.application.components;

import com.my_pls.securePassword;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String,Object> postMethodDefaults(String pageType, Map<String, String> formFields, securePassword pwd_manager, Connection conn) {
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
                boolean flag = DataMapper.forgetPasswordSendEmail(email, conn);
                if(!flag) {
                    map.put("errorEmail", "display:block;margin-left:5%; width:90%");
                    map.put("emailVal", "");
                    map.put("success", "false");
                    map.put("succMsg", "");
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
                String newPassword = formFields.get("pass");
                newPassword = pwd_manager.hashPassword(newPassword);
                boolean flag = DataMapper.changePassword(confirmCode, email, newPassword, conn);
                if (flag) {
                    map.put("errorPassMatch", "");
                    map.put("success", "true");
                    map.put("succMsg", "Your password has been changed. Please log in again.");
                } else {
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
