package com.my_pls.demo;


import org.apache.commons.codec.digest.DigestUtils;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import com.my_pls.MySqlConnection;
import com.my_pls.sendEmail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.net.URLDecoder;

import static spark.Spark.*;

public class App {


    private static Map<String,String> extractFields(String body){
        try{
            Map<String,String> map = new HashMap<>(0);
            String[] pairs =  body.split("&");
            for (String pair : pairs){
                String[] keyAndValue = pair.split("=");
                map.put(keyAndValue[0],keyAndValue[1]);
            }

            return map;
        }catch (Exception ex){
            return null;
        }

    }


    public static void main(String[] args) throws IOException {

        port(8080);

        final TemplateEngine engine = new FreeMarkerEngine();
        internalServerError("<html><body>Something went wrong!</body></html>");
        staticFileLocation("/public"); //So that it has access to the pubic resources(stylesheets, etc.)



        post("/sub", ((request, response) -> {

            Map<String,String> map = extractFields(request.body());
            return map;
        }
        ));

        // Setting any route (or filter) in Spark triggers initialization of the embedded Jetty web server.
        get("/", (request, response) -> {return new ModelAndView(new HashMap<>(),"sub.ftl");},engine);
        get("/hello/:name",(request, response) -> {
            String name = request.params(":name");
            Map<String,Object> map = new HashMap<>();
            map.put("title",name);
            return new ModelAndView(map,"home.ftl");

        },engine);

        get("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", "/login");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            map.put("loginErr", "");
            map.put("emailVal","");
            map.put("pageType","Login");
            map.put("styleVal", "margin-top:12%; width:45%");
            return new ModelAndView(map,"login.ftl");

        },engine);

        post("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());

            if (formFields.size() > 0) {
                if (!formFields.get("email").contains("rit.edu")) {
                    map.put("errorEmail", "display:list-item;margin-left:5%");
                    map.put("emailVal","");
                    map.put("loginErr", "");
                } else {
                    map.put("loginErr", "display:list-item;margin-left:5%");
                    map.put("errorEmail", "");
                    String emVal = URLDecoder.decode(formFields.get("email"), "UTF-8");
                    map.put("emailVal",formFields.get("email"));
                }
            } else {
                map.put("loginErr", "");
                map.put("emailVal", "");
            }
            map.put("actionLink", "/login");
            map.put("errorPassMatch", "");
            map.put("pageType","Login");
            map.put("styleVal", "margin-top:12%; width:45%");
            return new ModelAndView(map,"login.ftl");

        },engine);

        get("/register",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", "/register");
            map.put("loginErr", "");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            map.put("fname","");
            map.put("lname","");
            map.put("emailVal","");
            map.put("pageType","Register");
            map.put("styleVal", "margin-top:5%; width:45%");
//            map.put("toast",(""))
            return new ModelAndView(map,"login.ftl");

        },engine);

        post("/register",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            map.put("actionLink", "/register");
            map.put("loginErr", "");
            if (formFields.size() > 0) {
                System.out.println(formFields);
                System.out.println(formFields.get("email"));
                Boolean flag = true;
                if (!formFields.get("email").contains("rit.edu")) {
                    map.put("errorEmail", "display:list-item;margin-left:5%");
                    map.put("emailVal","");
                    flag = false;
                } else {
                    map.put("errorEmail", "");
                    String emVal = URLDecoder.decode(formFields.get("email"), "UTF-8");
                    map.put("emailVal",formFields.get("email"));
                }
                if (formFields.get("pass").equals(formFields.get("retPass"))) {
                    map.put("errorPassMatch", "");
                } else {
                    flag = false;
                    map.put("errorPassMatch", "display:list-item;margin-left:5%");
                }
                if (flag) {

                    String email = formFields.get("email");
                    email = URLDecoder.decode(email,"UTF-8");
                    String password = formFields.get("pass");
                    String fName = formFields.get("firstName");
                    String lName = formFields.get("lastName");
                    String newPassword = DigestUtils.md5Hex(password); //Create random hash for verification link
                    Random theRandom = new Random();
                    theRandom.nextInt(999999);
                    String myHash = DigestUtils.md5Hex("" +	theRandom);
                    Connection conn = MySqlConnection.getConnection();
                    try {

                        String sqlQuery = "insert into user_details (First_Name,Last_Name,Email,Password,Hash,Active) values(?,?,?,?,?,?)";
                        PreparedStatement pst = conn.prepareStatement(sqlQuery);
                        pst.setString(1, fName);
                        pst.setString(2, lName);
                        pst.setString(3, email);
                        pst.setString(4, password);
                        pst.setString(5, myHash);
                        pst.setInt(6, 0);
                        int i = pst.executeUpdate();
                        String body =  "Click this link to confirm your email address and complete setup for your account." + "\n\nVerification Link: " + "http://localhost:8080/EmailVerification/ActivateAccount?key1=" + email + "&key2=" + myHash;
                        if (i != 0) {

                            sendEmail se = new sendEmail();
                            se.sendEmail_content(email,"Verify Email at MyPLS",body);

                        }
                    } catch (Exception e) {
                        System.out.println("Error at Registration: " + e);
                    }
                    response.redirect("/verify-register/send");
                } else {
                    map.put("fname",formFields.get("firstName"));
                    map.put("lname",formFields.get("lastName"));
                }
            }
            map.put("pageType","Register");
            map.put("styleVal", "margin-top:5%; width:45%");
            return new ModelAndView(map,"login.ftl");

        },engine);

        get("/verify-register/:type",((request, response) -> {
            String type = request.params(":type");
            Map<String,String> map = new HashMap<>();
            map.put("type", type);
            if(type == "confirmed") {
                //add account activation code here
            }
            return new ModelAndView(map,"verifyRegister.ftl");

        }),engine);

        get("/first/*/last/*",(request, response) -> {

            String firstName = request.splat()[0];
            String lastName = request.splat()[1];
            Map<String,Object> map = new HashMap<>();
            map.put("title",String.format("%s %s",firstName,lastName));
            return new ModelAndView(map,"home.ftl");

        },engine);



        Pokemon charmander  = new Pokemon();
        charmander.name = "Charry";
        charmander.type = "fire";

        Pokemon rattata = new Pokemon();
        rattata.name = "ratbi";
        rattata.type = "grass";

        get("/list",(request, response) -> {

            List<Pokemon> pokeList = new ArrayList<>(4);
            pokeList.add(charmander);
            pokeList.add(rattata);

            Map<String,Object> map = new HashMap<>();
            map.put("pokemon",pokeList);
            return new ModelAndView(map,"example.ftl");

        },engine);

        path("/user",()->{
            get("/",(req,res)-> req.session().attribute("name"));
            get("/update/:name",(req,res)->{
                String name = req.params(":name");
                req.session().attribute("name",name);
                return String.format("Value updated with %s",name);
            });

        });

    }

    public static class Pokemon{
        String name;
        String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }


}
