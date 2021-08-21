package com.my_pls.application.components;

import com.my_pls.MySqlConnection;
import com.my_pls.application.App;
import com.my_pls.securePassword;

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
        map.put("loading","false");
        map.put("styleVal", "margin-top:5%; width:45%");
        return map;
    }

    public static void updateCourses(Connection sqlconnection){
        String sql_statement = "Select * from courses where status<>'Completed'";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date today_date = new Date();
        try{
            Statement pst = sqlconnection.createStatement();
            ResultSet dbrs = pst.executeQuery(sql_statement);
            while (dbrs.next()){
                int course_id = dbrs.getInt("id");
                String endDate_str = dbrs.getString("end_date");
                Date end_date = formatter.parse(endDate_str);
                Date start_date = formatter.parse(dbrs.getString("start_date"));
                if (today_date.compareTo(end_date)>0){
//                    Update course status
                    sql_statement = "UPDATE courses SET status = 'Completed' WHERE id ="+course_id;
                    Statement stmt2 = sqlconnection.createStatement();
                    stmt2.executeUpdate(sql_statement);
                    stmt2.close();
                }else if(today_date.compareTo(start_date)>0){
                    sql_statement = "UPDATE courses SET status = 'Current' WHERE id ="+course_id;
                    Statement stmt2 = sqlconnection.createStatement();
                    stmt2.executeUpdate(sql_statement);
                    stmt2.close();
                }
            }
            dbrs.close();
            sqlconnection.close();
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("Updates");
    }

    public static Pair postMethodDefaults(Map<String, Object> map, Map<String,String> formFields, App.CurrUser user, securePassword pwd_manager) {
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
                            user.setAll(rs.getString("First_Name"), rs.getString("Last_Name"), db_password, emVal);
                            updateCourses(conn);
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
                        rs.close();
                        pst.close();
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
