package com.my_pls.application;

import com.my_pls.*;
import com.my_pls.application.components.*;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
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

    public static void main(String[] args) {

        port(8080);

        final TemplateEngine engine = new FreeMarkerEngine();
        staticFileLocation("/public"); //So that it has access to the pubic resources(stylesheets, etc.)
        internalServerError((request, response) -> {
            response.redirect("/err");
            return "{\"message\":\"Server Error\"}";
        });
        notFound((request, response) -> {
            response.redirect("/err");
            return "{\"message\":\"Custom 404\"}";
        });


        securePassword pwd_manager = new securePassword();
        CurrUser user_current = new CurrUser();

        // Setting any route (or filter) in Spark triggers initialization of the embedded Jetty web server.
//        get("/", (request, response) -> {return new ModelAndView(new HashMap<>(),"sub.ftl");},engine);
//        get("/hello/:name",(request, response) -> {
//            String name = request.params(":name");
//            Map<String,Object> map = new HashMap<>();
//            map.put("title",name);
//            return new ModelAndView(map,"home.ftl");
//        },engine);
//       Route serverError = get("/server-error",(request, response) -> {
//            Map<String,Object> map = new HashMap<>();
//            map.put("notAuthorized", "Something went wrong. Please email mypls@rit.edu for support");
//            return new ModelAndView(map,"homePage.ftl");
//        });
        get("/login/errAuth",(request, response) -> {
            Map<String,Object> map = Login.getMethodDefaults();
            map.forEach((k,v)->map.put(k,v));
            map.put("errAuth","true");
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/login",(request, response) -> {
            Map<String,Object> map = Login.getMethodDefaults();
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"login.ftl");
        },engine);

        post("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("loading","true");
            Map<String,String> formFields = extractFields(request.body());
            Pair p = Login.postMethodDefaults(map, formFields, user_current, pwd_manager);
            map = p.fst();
            CurrUser logUser = p.snd();
            user_current.setAll(logUser.firstName, logUser.lastName, logUser.password, logUser.email);
            Map<String, Object> finalMap = map;
            map.forEach((k, v)-> finalMap.put(k,v));
            if (logUser.firstName.length() > 0) response.redirect("/");
            map.put("loading", "false");
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/register",(request, response) -> {
            Map<String,Object> map = Register.getMethodDefaults();
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"login.ftl");
        },engine);

        post("/register",(request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Pair p = Register.postMethodDefaults(formFields, user_current, pwd_manager);
            Map<String,Object> map = p.fst();
            map.forEach((k,v)->map.put(k,v));
            CurrUser logUser = p.snd();
            user_current.setAll(logUser.firstName, logUser.lastName, logUser.password, logUser.email);
            if (logUser.firstName.length() > 0) response.redirect("/verify-register/send");
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/verify-register/:type", (request, response) -> {
            String type = request.params(":type");
            Map<String,String> map = new HashMap<>();
            map.put("type", type);

            if(type.equals("confirm")) {
                String email = request.queryParams("key1");
                email = URLDecoder.decode(email,"UTF-8");
                String hash = request.queryParams("key2");
                Connection conn = MySqlConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement("select Email, Hash, Active from user_details where Email=? and Hash=? and Active='0'");
                pst.setString(1, email);
                pst.setString(2, hash);
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    PreparedStatement pst1 = conn.prepareStatement("update user_details set Active='1' where Email=? and Hash=?");
                    pst1.setString(1, email);
                    pst1.setString(2, hash);

                    int i = pst1.executeUpdate();
                }
            }
            return new ModelAndView(map,"verifyRegister.ftl");
        },engine);

        get("/forgot-password/:type",((request, response) -> {
            String pageType = request.params(":type");
            Map<String, Object> map = ForgetPassword.getMethodDefaults(pageType);
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"forgotPassword.ftl");
        }),engine);

        post("/forgot-password/:type",((request, response) -> {
            String pageType = request.params(":type");
            Map<String,String> formFields = extractFields(request.body());
            Map<String,Object> map = ForgetPassword.postMethodDefaults(pageType,
                    formFields,
                    pwd_manager);
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"forgotPassword.ftl");
        }),engine);

//        get("/first/*/last/*",(request, response) -> {
//
//            String firstName = request.splat()[0];
//            String lastName = request.splat()[1];
//            Map<String,Object> map = new HashMap<>();
//            map.put("title",String.format("%s %s",firstName,lastName));
//            return new ModelAndView(map,"home.ftl");
//        },engine);

        get("/",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            if (user_current.firstName.length() == 0) {
                response.redirect("/login/errAuth");
            }
            map.put("name", user_current.firstName +" " + user_current.lastName);
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/course/about",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "student");
            return new ModelAndView(map,"courseAbout.ftl");
        }),engine);

        get("/course/learnMat",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "student");
            return new ModelAndView(map,"courseLearnMat.ftl");
        }),engine);

        get("/course/quiz",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseQuiz.ftl");
        }),engine);

        get("/course/grades",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseGrade.ftl");
        }),engine);

        get("/course/grade/individual",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "prof");
            return new ModelAndView(map,"courseGradeIndividual.ftl");
        }),engine);

        get("/course/classlist",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            map.put("role", "student");
            return new ModelAndView(map,"courseClasslist.ftl");
        }),engine);

        get("/course/rate",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"courseRate.ftl");
        }),engine);

        get("/courses/all", (request, response) -> {
            Map<String,Object> map = Courses.getMethodDefaults("");
            String e_id = request.queryParams("edit");
            String d_id = request.queryParams("del");
            boolean done = false;
            if (d_id != null) done = Courses.deleteCourse(d_id);
            if (e_id != null) response.redirect("/courses/create-course?e="+e_id);
            if(done) response.redirect("/courses/all");
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"coursesAll.ftl");
        },engine);

        post("/courses/all",((request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Map<String,Object> map = Courses.getMethodDefaults(formFields.get("filterBy"));
            map.forEach((k,v)->map.put(k,v));
            map.put("role","admin");
            return new ModelAndView(map,"coursesAll.ftl");
        }),engine);

        get("/courses",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("role","admin");
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        post("/courses",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            map.put("role","admin");

            return new ModelAndView(map,"courses.ftl");
        }),engine);


        get("/courses/create-course",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("name","");
            map.put("obj","");
            map.put("profList", DataMapper.findAllProfs());
            map.put("e",-1);
            String e_id = request.queryParams("e");
            if (e_id != null) {
                map = CreateCourse.editCourse(map,e_id.replaceAll(" ",""));
                Map<String, Object> finalMap = map;
                map.forEach((k, v)-> finalMap.put(k,v));
                map.put("e",e_id);
            } else {
                LinkedHashMap<String, Boolean> days = CreateCourse.findAllDays();
                System.out.println(days);
                map.put("days",days);
            };
            return new ModelAndView(map,"createCourse.ftl");
        }),engine);

        post("/courses/create-course", (request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            String edit = request.queryParams("e");
            Map<String,Object> map = CreateCourse.postMethodDefaults(formFields, edit);
            if((boolean)map.get("created") == true) response.redirect("/courses/all");
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"createCourse.ftl");
        },engine);

        get("/course/create-quiz",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"createQuiz.ftl");
        }),engine);

//        path("/user",()->{
//            get("/",(req,res)-> req.session().attribute("name"));
//            get("/update/:name",(req,res)->{
//                String name = req.params(":name");
//                req.session().attribute("name",name);
//                return String.format("Value updated with %s",name);
//            });
//
//        });

        get("/enroll",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        post("/enroll",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        get("/enroll/about",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"enrollAbout.ftl");
        }),engine);

        get("/ratings",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("role","admin");
            map.put("users", Rating.getAllUserRatings());
            map.put("courses", Rating.getAllCourseRatings());
            return new ModelAndView(map,"ratings.ftl");
        }),engine);

        post("/ratings",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            map.put("role","admin");
            map.put("users", Rating.getAllUserRatings());
            map.put("courses", Rating.getAllCourseRatings());
            return new ModelAndView(map,"ratings.ftl");
        }),engine);

        get("/discussion-groups",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            ArrayList<Map<String,Object>> groups = DataMapper.getMyDiscussionGroups(141);
            map.put("groups", groups);
            return new ModelAndView(map,"discussionGroups.ftl");
        }),engine);

        get("/discussion/group-desc",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            return new ModelAndView(map,"groupDesc.ftl");
        }),engine);

        get("/discussion/create-post",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"discussionPost.ftl");
        }),engine);

        get("/rating/individual",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"ratingsIndividual.ftl");
        }),engine);

        get("/approval",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<Integer,String> profs = Admin.viewAllRequests();
            map.put("profs",profs);
            map.put("role", "admin");
            Map<String,String> searchOptions = Admin.getSearchOptions("");
            map.put("searchOptions",searchOptions);
            map.put("users",new HashMap<Integer,String>());
            return new ModelAndView(map,"approval.ftl");
        }),engine);

        post("/approval", (request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = Admin.postMethodFunctionality(formFields);
            if (map.containsKey("redirect") && (boolean) map.get(redirect)) response.redirect("/approval");
            map.forEach((k, v)-> map.put(k,v));
            return new ModelAndView(map,"approval.ftl");
        },engine);

        get("/apply-prof",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            return new ModelAndView(map,"profApply.ftl");
        }),engine);

        post("/apply-prof", (request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = ApplyForProfessor.postMethodFunctionality(formFields,
                    user_current.firstName, user_current.lastName, user_current.email);
            map.forEach((k, v)-> map.put(k,v));
            return new ModelAndView(map,"profApply.ftl");
        },engine);

        get("/err",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            if (user_current.firstName.length() == 0) {
                response.redirect("/login/errAuth");
            }
            map.put("name", user_current.firstName + " " + user_current.lastName);
            map.put("notAuthorized", "You have been redirected to the " +
                    "home page as you were not authorized to view the page" +
                    " you selected or something went wrong. Please email " +
                    "mypls@rit.edu for support");
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

    }

    public static class CurrUser {
        String firstName = "";
        String lastName = "";
        String password = "";
        String email = "";

        public CurrUser(String firstName, String lastName, String password, String email) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
        }
        public CurrUser() {
            this.email = "";
            this.firstName = "";
            this.lastName = "";
            this.password = "";
        }
        public void setAll(String firstName, String lastName, String password, String email) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
        }
    }
}
