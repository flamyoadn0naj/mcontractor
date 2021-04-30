package com.my_pls.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

import static spark.Spark.*;

public class App {
    public static String firebase_auth_json = "json_auth/mypls-added-value-firebase-adminsdk-6j3xx-5271a3d0db.json";

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
//    Map<String,String> map = extractFields(request.body());
//        return map;

    public static void main(String[] args) throws IOException, FirebaseAuthException {

        port(8080);

        final TemplateEngine engine = new FreeMarkerEngine();
        internalServerError("<html><body>Something went wrong!</body></html>");
        staticFileLocation("/public"); //So that it has access to the pubic resources(stylesheets, etc.)


        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebase_auth_json))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setDatabaseUrl("https://mypls-added-value.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();


//        mAuth.createUser(user_request);
//        UserRecord user = mAuth.getUser("TG4b6yMyPYaHiW2yYpyBfU6lAki2");
//        System.out.println(user.getEmail());

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
                    UserRecord.CreateRequest new_user = new UserRecord.CreateRequest();
                    String email = formFields.get("email");
                    email.replace("%40","@");
                    new_user.setEmail(email);
                    String display_name = formFields.get("fname") +" " +  formFields.get("lname");
                    new_user.setDisplayName(display_name);
                    new_user.setPassword(formFields.get("password"));
                    mAuth.createUser(new_user);
                    response.redirect("/verify-register");
                } else {
                    map.put("fname",formFields.get("firstName"));
                    map.put("lname",formFields.get("lastName"));
                }
            }
            map.put("pageType","Register");
            map.put("styleVal", "margin-top:5%; width:45%");
            return new ModelAndView(map,"login.ftl");

        },engine);

        get("/verify-register",((request, response) -> {
            Map<String,String> map = new HashMap<>();
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
    static void authExplicit(String jsonPath) throws IOException {
        // You can specify a credential file by providing a path to GoogleCredentials.
        // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebase_auth_json))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }

}
