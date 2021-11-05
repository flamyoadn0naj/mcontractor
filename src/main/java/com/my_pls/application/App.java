package com.my_pls.application;

import com.my_pls.*;
import com.my_pls.application.components.*;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;


//things for file upload
import spark.utils.IOUtils;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.*;

//end things for file upload


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

        //file upload location
        File uploadDir = new File("uploadFolder");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist
        //folder is at the same hierarchy level as main
        staticFiles.externalLocation("uploadFolder");


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
            Map<String,Object> map = new HashMap<>();
            Session session = request.session();
            if (session.attribute("firstName") == null) {
                response.redirect("/login/errAuth");
            } else {
                String role = session.attribute("role").toString();
                int id = session.attribute("id");
                Home home = new Home(id, role);
                map.put("name", session.attribute("firstName").toString() + " "
                        + session.attribute("lastName").toString());
                map.put("role", session.attribute("role"));
                Map<Integer, Object> courses = home.getCourses();
                Map<String,Object> rating = home.getRating();
                ArrayList<Map<String,Object>> groups = home.getGroups();
                if (!courses.isEmpty()) map.put("courses", home.getCourses());
                if(!groups.isEmpty()) map.put("groups", home.getGroups());
                if (!rating.isEmpty()) map.put("rating", home.getRating());
            }
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/course/about/:number",((request, response) -> {
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

        get("/course/learnMat/:number",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            map.put("role", sess.attribute("role").toString());
            //add each lesson
            map.put("lessons",DataMapper.getLessonsByCourseId(Integer.parseInt(request.params(":number"))));
            map.put("courseNumber",request.params(":number"));
            return new ModelAndView(map,"courseLearnMatS.ftl");
        }),engine);

        post("/lesson/save/:courseId", (request,response)-> {
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            System.out.println(URLDecoder.decode(formFields.get("req"),"UTF-8"));
            Lesson temp = new Lesson(Integer.parseInt(URLDecoder.decode(formFields.get("lessonId"),"UTF-8")),
                                    URLDecoder.decode(formFields.get("name"),"UTF-8"),
                                    URLDecoder.decode(formFields.get("req"),"UTF-8"));
            for(Map.Entry<String, String> element : formFields.entrySet()){
                String k = URLDecoder.decode(element.getKey(),"UTF-8");
                String v = URLDecoder.decode(element.getValue(),"UTF-8");
                if(k.equals(v)){
                    temp.materials.add(v);
                }
            }
            //add learning materials
            DataMapper.createOrUpdateLesson(temp);
            response.redirect("/course/learnMat/"+request.params(":courseId"));
            return null;
        });

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
//            if (formFields.containsKey("pre-reqs")) {
//                System.out.println(formFields);
//                map.put("prereq", true);
//            }
            Map<String, Object> finalMap = map;
            map.forEach((k, v)-> finalMap.put(k,v));
            map.put("role",role);
            return new ModelAndView(map,"coursesAll.ftl");
        }),engine);

        get("/courses",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int id = sess.attribute("id");
            Map<Integer, Object> courses = Courses.getMyCourses(id, role);
            map.put("filterStatus", "All");
            ArrayList<String> filterOptions = new ArrayList<>();
            filterOptions.add("Current");
            filterOptions.add("Upcoming");
            filterOptions.add("Completed");
            map.put("filterOptions",filterOptions);
            if (!courses.isEmpty()) map.put("courses", courses);
            map.put("role", role);
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        post("/courses",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int id = sess.attribute("id");
            Map<Integer, Object> courses = Courses.getMyCourses(id, role);
            Map<String,String> formFields = extractFields(request.body());
            String filterStatus = "All";
            ArrayList<String> filterOptions = new ArrayList<>();
            filterOptions.add("All");
            filterOptions.add("Current");
            filterOptions.add("Upcoming");
            filterOptions.add("Completed");

            if (formFields.containsKey("filterBy")) {
                if (!formFields.get("filterBy").contentEquals("All"))
                    courses = Courses.filterCourses(formFields.get("filterBy"), courses);
                filterStatus = formFields.get("filterBy");
            }
            map.put("filterStatus",filterStatus);
            filterOptions.remove(new String(filterStatus));
            map.put("courses", courses);
            map.put("filterOptions",filterOptions);
            map.put("role", role);
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
            Map<String,Object> map = new HashMap<>();
            ArrayList<Map<String,Object>> courses = Enrollment.findAllAvailableCourses();
            map.put("courses", courses);
            map.put("role", role);
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        post("/enroll",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            map.put("role", role);
            return new ModelAndView(map,"enroll.ftl");
        }),engine);

        get("/enroll/about/:number",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);

            //display course specific info
            map.put("courseNumber", request.params(":number"));
            Map<String, Object> tempCourse = DataMapper.findCourseByCourseId(request.params(":number"));
            for(Map.Entry<String, Object> entry: tempCourse.entrySet()){
                map.put(entry.getKey(), entry.getValue().toString());
                System.out.println(entry.getKey() + ", " + entry.getValue().toString());
            }
            map.put("profName",DataMapper.findProfName(Integer.parseInt(map.get("prof_id"))));
            return new ModelAndView(map,"enrollAbout.ftl");
        }),engine);

        get("/ratings",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String, Object> map = Rating.getMethodFunctionality(role);
            map.forEach((k,v)->map.put(k,v));
            map.put("role", role);
            return new ModelAndView(map,"ratings.ftl");
        }),engine);

        post("/ratings",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> formFields = extractFields(request.body());
            Map<String,Object> map = Rating.postMethodFunctionality(formFields, role);
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

        get("/discussion/group-desc/:id",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int user_id = sess.attribute("id");
            int id = Integer.parseInt(request.params(":id"));
            Map<String, Object> group = DataMapper.getGroupDetailsByGroupId(id);
            String member = DiscussionGroups.isMemberOfGroup(user_id, id);
            Map<Integer, String> members = DataMapper.viewAllGroupMembers(id);
            Map<Integer, String> requests = DataMapper.getAllPendingGroupRequestsOfGroup(id);
            int prof = DiscussionGroups.findProfofCourse(group);
            Map<String,Object> map = new HashMap<>();
            if (prof != 0) map.put("prof", prof);
            map.put("status", member);
            map.put("group", group);
            map.put("role", role);
            map.put("members", members);
            map.put("reqs", requests);
            map.put("id", id);
            return new ModelAndView(map,"groupDesc.ftl");
        }),engine);

        post("/discussion/group-desc/:id", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int user_id = sess.attribute("id");
            int id = Integer.parseInt(request.params(":id"));
            Map<String,String> formFields = extractFields(request.body());
            if (formFields.containsKey("add")) {
                int u_id = Integer.parseInt(formFields.get("add"));
                boolean flag = DataMapper.addDGmember(u_id, id);
                boolean flag2 = false;
                if (flag) flag2 = DataMapper.deleteRequestForGroup(u_id, id);
                if (flag2) response.redirect("/discussion/group-desc/"+id);
            } else if (formFields.containsKey("del")) {
                int u_id = Integer.parseInt(formFields.get("del"));
                boolean flag = DataMapper.deleteRequestForGroup(u_id, id);
                if (flag) response.redirect("/discussion/group-desc/"+id);
            } else if (formFields.containsKey("leave")) {
                boolean flag = DataMapper.deleteDGMember(user_id, id);
                if (flag) response.redirect("/discussion-groups");
            }
            Map<String,Object> map = new HashMap<>();
            return new ModelAndView(map,"groupDesc.ftl");
        },engine);

        get("/discussion/create-post",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            return new ModelAndView(map,"discussionPost.ftl");
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
            Session session = request.session();
            if (session.attribute("firstName") == null) {
                response.redirect("/login/errAuth");
            } else {
                String role = session.attribute("role").toString();
                int id = session.attribute("id");
                Home home = new Home(id, role);
                map.put("name", session.attribute("firstName").toString() + " "
                        + session.attribute("lastName").toString());
                map.put("role", session.attribute("role"));
                Map<Integer, Object> courses = home.getCourses();
                Map<String,Object> rating = home.getRating();
                ArrayList<Map<String,Object>> groups = home.getGroups();
                if (!courses.isEmpty()) map.put("courses", home.getCourses());
                if(!groups.isEmpty()) map.put("groups", home.getGroups());
                if (!rating.isEmpty()) map.put("rating", home.getRating());
                map.put("notAuthorized", "You have been redirected to the " +
                        "home page as you were not authorized to view the page" +
                        " you selected or something went wrong. Please email " +
                        "mypls@rit.edu for support");
            }
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/logout",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            request.session().invalidate();
            response.redirect("/login");
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/upload",((request, response) -> {
            //Session sess = request.session();
            //String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            //map.put("role", role);
            return new ModelAndView(map,"upload.ftl");
        }),engine);

        post("/upload", (request, response) -> {

            // tempFile = Files.createFile(uploadDir.toPath(), "test", ".pdf");

            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = request.raw().getPart("myfile").getInputStream()) { // getPart needs to use same "name" as input field in form
                //Path tempFile = Files.createTempFile(uploadDir.toPath(), "test", ".pdf");
                File newFile = new File(uploadDir.toPath().toString(),request.raw().getPart("myfile").getSubmittedFileName());
                //System.out.println(uploadDir.toPath());
                //System.out.println(request.raw().getPart("myfile").getSubmittedFileName());
                Files.copy(input, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println();
            response.redirect("/upload");
            return "Success!";
        });

        post("/download", (request,response)-> {
            File file = new File("uploadFolder/PDFTest.pdf");
            response.raw().setContentType("application/octet-stream");
            response.raw().setHeader("Content-Disposition","attachment; filename="+file.getName()+".zip");
            try {

                try(ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.raw().getOutputStream()));
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file)))
                {
                    ZipEntry zipEntry = new ZipEntry(file.getName());

                    zipOutputStream.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bufferedInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer,0,len);
                    }
                    zipOutputStream.flush();
                    zipOutputStream.close();
                }
            } catch (Exception e) {

            }

            return null;
        });
    }
}
