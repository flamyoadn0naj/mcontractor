package com.my_pls.application;

import com.my_pls.*;
import com.my_pls.application.components.*;
import spark.ModelAndView;
import spark.Session;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

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
        User user_current = new User();

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
            User logUser = p.snd();
            user_current.setAll(logUser.firstName, logUser.lastName, logUser.password, logUser.email, logUser.id, logUser.role);
            Map<String, Object> finalMap = map;
            map.forEach((k, v)-> finalMap.put(k,v));
            if (logUser.firstName.length() > 0) {
                System.out.println(logUser);
                Session session = request.session(true);
                session.attribute("firstName", logUser.firstName);
                session.attribute("lastName", logUser.lastName);
                session.attribute("email", logUser.email);
                session.attribute("id", logUser.id);
                session.attribute("role", logUser.role);
                response.redirect("/");
            }
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
            User logUser = p.snd();

            user_current.setAll(logUser.firstName, logUser.lastName, logUser.password,
                    logUser.email, -1, "learner");
            if (logUser.firstName.length() > 0){
                request.session().attribute("email",logUser.email); //saved to session
                response.redirect("/verify-register/send");
            }
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
                boolean flag = DataMapper.verifyEmailofUser(email, hash);
            }

            return new ModelAndView(map,"verifyRegister.ftl");
        },engine);

        post("/verify-register/:type",((request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = new HashMap<>();
            if (formFields.containsKey("resend")) {
                boolean flag = DataMapper.resendEmailConfirmation(user_current.email);
                if (flag) map.put("resend", true);
                else map.put("resend", false);
            }
            return new ModelAndView(map,"verifyRegister.ftl");
        }),engine);

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

        get("/",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session session = request.session();
            if (session.attribute("firstName") == null) {
                response.redirect("/login/errAuth");
            } else {
                map.put("name", session.attribute("firstName").toString() + " "
                        + session.attribute("lastName").toString());
                map.put("role", session.attribute("role"));
            }
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/course/about",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session sess = request.session();
            map.put("role", sess.attribute("role"));
            return new ModelAndView(map,"courseAbout.ftl");
        }),engine);

        get("/course/learnMat",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session sess = request.session();
            map.put("role", sess.attribute("role"));
            return new ModelAndView(map,"courseLearnMat.ftl");
        }),engine);

        get("/course/quiz",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session sess = request.session();
            map.put("role", sess.attribute("role"));
            return new ModelAndView(map,"courseQuiz.ftl");
        }),engine);

        get("/course/grades",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session sess = request.session();
            map.put("role", sess.attribute("role"));
            return new ModelAndView(map,"courseGrade.ftl");
        }),engine);

        get("/course/grade/individual",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session sess = request.session();
            map.put("role", sess.attribute("role"));
            return new ModelAndView(map,"courseGradeIndividual.ftl");
        }),engine);

        get("/course/classlist",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session sess = request.session();
            map.put("role", sess.attribute("role"));
            return new ModelAndView(map,"courseClasslist.ftl");
        }),engine);

        get("/course/rate",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"courseRate.ftl");
        }),engine);

        get("/courses/all", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,Object> map = Courses.getMethodDefaults("");
            String e_id = request.queryParams("edit");
            String d_id = request.queryParams("del");
            boolean done = false;
            if (d_id != null) done = Courses.deleteCourse(d_id);
            if (e_id != null) response.redirect("/courses/create-course?e="+e_id);
            if(done) response.redirect("/courses/all");
            map.forEach((k,v)->map.put(k,v));
            map.put("role", role);
            return new ModelAndView(map,"coursesAll.ftl");
        },engine);

        post("/courses/all",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,String> formFields = extractFields(request.body());
            Map<String,Object> map = new HashMap<>();
            if (formFields.containsKey("filterBy")) map = Courses.getMethodDefaults(formFields.get("filterBy"));
            else map = Courses.getMethodDefaults("");
            if (formFields.containsKey("pre-reqs")) {
                System.out.println(formFields);
                map.put("prereq", true);
            }
            Map<String, Object> finalMap = map;
            map.forEach((k, v)-> finalMap.put(k,v));
            map.put("role",role);
            return new ModelAndView(map,"coursesAll.ftl");
        }),engine);

        get("/courses",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            map.put("role", role);
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        post("/courses",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            map.put("role", role);
            Map<String,String> formFields = extractFields(request.body());
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        get("/courses/create-course",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,Object> map = new HashMap<>();
            map.put("name","");
            map.put("obj","");
            map.put("profList", DataMapper.findAllProfs());
            map.put("e",-1);
            Map<Integer, String> allCourses = CreateCourse.allCourses();
            String e_id = request.queryParams("e");
            if (e_id != null) {
                map = CreateCourse.editCourse(map,e_id.replaceAll(" ",""), allCourses);
                Map<String, Object> finalMap = map;
                map.forEach((k, v)-> finalMap.put(k,v));
                map.put("e",e_id);
                allCourses.remove(e_id);
                map.put("currPreq", allCourses.get(map.get("prereq_course")));
                allCourses.remove(map.get("prereq_course"));
                map.put("course_id", map.get("prereq_course"));
                map.put("prereqs", allCourses);
                map.put("headingChange", true);
            } else {
                LinkedHashMap<String, Boolean> days = CreateCourse.findAllDays();
                System.out.println(days);
                map.put("days",days);
                map.put("prereqs", allCourses);
            };
            return new ModelAndView(map,"createCourse.ftl");
        }),engine);

        post("/courses/create-course", (request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            String edit = request.queryParams("e");
            Map<Integer, String> allCourses = CreateCourse.allCourses();
            Map<String,Object> map = CreateCourse.postMethodDefaults(formFields, edit);
            if((boolean)map.get("created") == true) response.redirect("/courses/all");
            if (map.containsKey("prereq_course")) {
                allCourses.remove(Integer.parseInt(edit));
                map.put("currPreq", allCourses.get(map.get("prereq_course")));
                allCourses.remove(map.get("prereq_course"));
                map.put("course_id", map.get("prereq_course"));
            }
            map.put("prereqs", allCourses);
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"createCourse.ftl");
        },engine);

        get("/course/create-quiz",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"createQuiz.ftl");
        }),engine);

        get("/enroll",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        post("/enroll",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            map.put("role", role);
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        get("/enroll/about",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"enrollAbout.ftl");
        }),engine);

        get("/ratings",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String, Object> map = Rating.getMethodFunctionality();
            map.forEach((k,v)->map.put(k,v));
            map.put("role", role);
            return new ModelAndView(map,"ratings.ftl");
        }),engine);

        post("/ratings",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> formFields = extractFields(request.body());
            Map<String,Object> map = Rating.postMethodFunctionality(formFields);
            map.forEach((k,v)->map.put(k,v));
            map.put("role", role);
            return new ModelAndView(map,"ratings.ftl");
        }),engine);

        get("/discussion-groups", (request, response) -> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            ArrayList<Map<String,Object>> groups = DataMapper.getMyDiscussionGroups(id);
            Map<Integer,Map<String, Object>> allGroups = DiscussionGroups.getGroups("", -1, id);
            Map<Integer, Object> requestedGroups = DataMapper.getPendingGroupRequests(id);
            map.put("groups", groups);
            map.put("allGroups", allGroups);
            map.put("role", role);
            map.put("filterOptions", DiscussionGroups.getSearchOptions(""));
            if (requestedGroups.isEmpty()) map.put("emptyReq", true);
            else map.put("requestedGroups",requestedGroups);
            return new ModelAndView(map,"discussionGroups.ftl");
        },engine);

        post("/discussion-groups",((request, response) -> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String role = sess.attribute("role").toString();
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = DiscussionGroups.postMethodFunctionality(formFields,id);
            map.forEach((k,v)->map.put(k,v));
            if (map.containsKey("refresh")) response.redirect("/discussion-groups");
            map.put("role", role);
            return new ModelAndView(map,"discussionGroups.ftl");
        }),engine);

        get("/discussion/create",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"createDiscussionGroup.ftl");
        }),engine);

        post("/discussion/create",((request, response) -> {
            Session sess = request.session();
            int id = sess.attribute("id");
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            boolean flag = DiscussionGroups.createGroup(formFields, id);
            if (flag) response.redirect("/discussion-groups");
            else map.put("err", true);
            return new ModelAndView(map,"createDiscussionGroup.ftl");
        }),engine);

        get("/discussion/group-desc",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"groupDesc.ftl");
        }),engine);

        get("/discussion/create-post",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"discussionPost.ftl");
        }),engine);

        get("/rating/individual",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role",role);
            return new ModelAndView(map,"ratingsIndividual.ftl");
        }),engine);

        get("/approval",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,Object> map = new HashMap<>();
            Map<Integer,String> profs = DataMapper.viewAllRequests();
            map.put("profs",profs);
            map.put("role", role);
            Map<String,String> searchOptions = Admin.getSearchOptions("");
            map.put("searchOptions",searchOptions);
            map.put("users",new HashMap<Integer,String>());
            return new ModelAndView(map,"approval.ftl");
        }),engine);

        post("/approval", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = Admin.postMethodFunctionality(formFields);
            if (map.containsKey("redirect") && (boolean) map.get(redirect)) response.redirect("/approval");
            map.forEach((k, v)-> map.put(k,v));
            map.put("role", role);
            return new ModelAndView(map,"approval.ftl");
        },engine);

        get("/apply-prof",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("learner")) response.redirect("/err");
            Map<String,Object> map = new HashMap<>();
            return new ModelAndView(map,"profApply.ftl");
        }),engine);

        post("/apply-prof", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("learner")) response.redirect("/err");
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = ApplyForProfessor.postMethodFunctionality(formFields,
                    sess.attribute("firstName").toString(),
                    sess.attribute("lastName").toString(),
                    sess.attribute("email").toString());
            map.forEach((k, v)-> map.put(k,v));
            return new ModelAndView(map,"profApply.ftl");
        },engine);

        get("/err",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            if (sess.attribute("firstName") == null) {
                response.redirect("/login/errAuth");
            } else {
                String role = sess.attribute("role").toString();
                String firstName = sess.attribute("firstName").toString();
                String lastName = sess.attribute("lastName").toString();
                map.put("name", firstName + " " + lastName);
                map.put("notAuthorized", "You have been redirected to the " +
                        "home page as you were not authorized to view the page" +
                        " you selected or something went wrong. Please email " +
                        "mypls@rit.edu for support");
                map.put("role", role);
            }
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/logout",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            request.session().invalidate();
            response.redirect("/login");
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

    }

}
