package com.my_pls.application;

import com.my_pls.*;
import com.my_pls.application.components.*;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.text.NumberFormat;
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
        String ip = "10.181.95.232:8080";
        final TemplateEngine engine = new FreeMarkerEngine();
        staticFileLocation("/public"); //So that it has access to the pubic resources(stylesheets, etc.)

        //file upload location
        String oldFileLoc = "uploadFolder";
        String newFileLoc = System.getProperty("user.dir") + "/src/main/resources/public";
        File uploadDir = new File(newFileLoc);
        uploadDir.mkdir(); // create the upload directory if it doesn't exist
        //folder is at the same hierarchy level as main
        staticFiles.externalLocation(newFileLoc);
        System.out.println(uploadDir.toPath().toString());
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
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", "/login");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            map.put("loginErr", "");
            map.put("emailVal","");
            map.put("pageType","Login");
            map.put("styleVal", "margin-top:5%; width:45%");
            map.put("errAuth","true");
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", "/login");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            map.put("loginErr", "");
            map.put("emailVal","");
            map.put("pageType","Login");
            map.put("styleVal", "margin-top:5%; width:45%");
            return new ModelAndView(map,"login.ftl");
        },engine);

        post("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Connection conn = MySqlConnection.getConnection();
            Map<String,String> formFields = extractFields(request.body());
            if (formFields.size() > 0) {
                user_current.setEmail(URLDecoder.decode(formFields.get("email"), "UTF-8"));
                user_current.setPassword(formFields.get("pass"));
                if (user_current.checkErrorEmail()) {
                    map.put("errorEmail", "display:list-item;margin-left:5%");
                    map.put("emailVal","");
                    map.put("loginErr", "");
                } else {
                    if (user_current.login(pwd_manager, conn)) {
                        map.put("loginErr", "");

                    } else {
                        map.put("loginErr", "display:list-item;margin-left:5%");
                    }
                }
                map.put("errorEmail", "");
                map.put("emailVal", user_current.getEmail());

            } else {
                map.put("loginErr", "");
                map.put("emailVal", "");
            }
            map.put("actionLink", "/login");
            map.put("errorPassMatch", "");
            map.put("pageType","Login");
            map.put("styleVal", "margin-top:5%; width:45%");
            if (user_current.getFirstName().length() > 0) {
                Session session = request.session(true);
                session.attribute("firstName", user_current.getFirstName());
                session.attribute("lastName", user_current.getLastName());
                session.attribute("email", user_current.getEmail());
                session.attribute("id", user_current.getId());
                session.attribute("role", user_current.getRole());
                response.redirect("/");
            }
            conn.close();
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/register",(request, response) -> {
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
            return new ModelAndView(map,"login.ftl");
        },engine);

        post("/register",(request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Connection conn = MySqlConnection.getConnection();
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", "/register");
            map.put("loginErr", "");
            user_current.setEmail(URLDecoder.decode(formFields.get("email"),"UTF-8"));
            user_current.setPassword(formFields.get("pass"));
            user_current.setFirstName(URLDecoder.decode(formFields.get("firstName"),"UTF-8").trim());
            user_current.setLastName(URLDecoder.decode(formFields.get("lastName"), "UTF-8").trim());
            boolean flag = true;
            map.put("fname",user_current.getFirstName());
            map.put("lname",user_current.getLastName());
            if (user_current.checkErrorEmail()) {
                map.put("errorEmail", "display:list-item;margin-left:5%");
                map.put("emailVal","");
                flag = false;
            } else {
                map.put("errorEmail", "");
                map.put("emailVal",user_current.getEmail());
            }
            if (user_current.checkPassword(formFields.get("retPass"))) {
                map.put("errorPassMatch", "");
            } else {
                flag = false;
                map.put("errorPassMatch", "display:list-item;margin-left:5%");
            }
            if (flag) {
                if (!user_current.register(pwd_manager, conn)) {
                    map.put("dbErr", "true");
                    map.put("emailVal",user_current.getEmail());
                    user_current.erase();
                }
            } else user_current.erase();
            map.put("pageType","Register");
            map.put("styleVal", "margin-top:5%; width:45%");

            if (user_current.getFirstName().length() > 0){
                request.session().attribute("email",user_current.getEmail()); //saved to session
                response.redirect("/verify-register/send");
            }
            conn.close();
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/verify-register/:type", (request, response) -> {
            String type = request.params(":type");
            Connection conn = MySqlConnection.getConnection();
            Map<String,String> map = new HashMap<>();
            map.put("type", type);

            if(type.equals("confirm")) {
                String email = request.queryParams("key1");
                email = URLDecoder.decode(email,"UTF-8");
                user_current.setEmail(email);
                String hash = request.queryParams("key2");
                user_current.verifyEmailofUser(hash, conn);
            }
            conn.close();
            return new ModelAndView(map,"verifyRegister.ftl");
        },engine);

        post("/verify-register/:type",((request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = new HashMap<>();
            String type = request.params(":type");
            map.put("type", type);
            if (formFields.containsKey("resend")) {
                Connection conn = MySqlConnection.getConnection();
                boolean flag = user_current.resendVerificationEmail(conn);
                if (flag) map.put("resend", true);
                else map.put("resend", false);
                conn.close();
            }
            return new ModelAndView(map,"verifyRegister.ftl");
        }),engine);

        get("/forgot-password/:type",((request, response) -> {
            String pageType = request.params(":type");
            Map<String,Object> map = new HashMap<>();
            map.put("actionLink", ("/forgot-password/" + pageType));
            map.put("success", "false");
            map.put("succMsg", "");
            map.put("errorEmail", "");
            map.put("errorPassMatch", "");
            map.put("pageType", pageType);
            return new ModelAndView(map,"forgotPassword.ftl");
        }),engine);

        post("/forgot-password/:type",((request, response) -> {
            String pageType = request.params(":type");
            Connection conn = MySqlConnection.getConnection();
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            map.put("emailVal","");
            map.put("success", "false");
            map.put("succMsg", "");
            map.put("pageType", pageType);
            map.put("actionLink", ("/forgot-password/" + pageType));
            user_current.setEmail(URLDecoder.decode(formFields.get("email"),"UTF-8"));
            if (pageType.equals("email")) {
                map.put("errorPassMatch", "");
                if (user_current.checkErrorEmail()) {
                    map.put("errorEmail", "display:block;margin-left:5%; width:90%");
                } else {
                    boolean flag = user_current.forgetPassword(conn);
                    if(!flag) {
                        map.put("errorEmail", "display:block;margin-left:5%; width:90%");
                    }
                    map.put("errorEmail", "");
                    map.put("success", "true");
                    map.put("succMsg", "A verification link has been emailed to you!");
                }
            } else if (pageType.equals("password")) {
                map.put("errorEmail", "");
                user_current.setPassword(formFields.get("pass"));
                String confirmCode = formFields.get("confirmCode");
                map.put("errorPassMatch", "");
                if (user_current.checkErrorEmail()) {
                    map.put("errorEmail", "display:block;margin-left:5%; width:90%");
                }
                if (user_current.checkPassword(formFields.get("retPass"))) {
                    if (confirmCode.length() == 4) {
                        boolean flag = user_current.changePassword(pwd_manager, confirmCode, conn);
                        if (flag) {
                            map.put("errorPassMatch", "");
                            map.put("success", "true");
                            map.put("succMsg", "Your password has been changed. Please log in again.");
                        } else {
                            map.put("errorLink","true");
                        }
                    } else map.put("errCode","display:block;margin-left:5%; width:90%" );
                } else {
                    map.put("errorPassMatch", "display:block;margin-left:5%; width:90%");
                }
            }
//            Map<String,Object> map = ForgetPassword.postMethodDefaults(pageType,
//                    formFields,pwd_manager, conn);
//            map.forEach((k,v)->map.put(k,v));
            conn.close();
            return new ModelAndView(map,"forgotPassword.ftl");
        }),engine);

        get("/",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session session = request.session();
            if (session.attribute("firstName") == null) {
                response.redirect("/login");
            } else {
                Connection conn = MySqlConnection.getConnection();
                String role = session.attribute("role").toString();
                int id = session.attribute("id");
                Home home = new Home(id, role, conn);
                map.put("name", session.attribute("firstName").toString() + " "
                        + session.attribute("lastName").toString());
                map.put("role", session.attribute("role"));
                ArrayList<Course> courses = home.getCourses();
                Map<String,Object> rating = home.getRating();
                ArrayList<Map<String,Object>> groups = home.getGroups();
                if (!courses.isEmpty()) map.put("courses", courses);
                if(!groups.isEmpty()) map.put("groups", groups);
                if (!rating.isEmpty()) {
                    map.put("rating", home.getRating());
                    if (((ArrayList<String>) rating.get("feedback")).size() > 5) {
                        map.put("seeMore", true);
                    }
                }
                conn.close();
            }
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/course/about/:number",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":number");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Connection conn = MySqlConnection.getConnection();
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("course",course);
            map.put("courseId", courseId);
//            Map<String,Object> rating = Proxy.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"",conn);
//            if (!rating.isEmpty()) map.put("rating", rating);
            if (course.allowRating()) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseAbout.ftl");
        }),engine);

        post("/course/about/:number",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            Connection conn = MySqlConnection.getConnection();
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":number");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() == id) map.put("role", "prof");
            else response.redirect("/err");
            boolean flag = false;
            if (formFields.containsKey("req")) {

               String req = URLDecoder.decode(formFields.get("req"), "UTF-8");
               flag = Proxy.updateCourseRequirements(Integer.parseInt(courseId), req, conn);

            }
            if (flag) {
                conn.close();
                response.redirect("/course/about/" + courseId);
            }
            else map.put("err", true);
            map.put("course",course);
            map.put("courseId", courseId);
//            Map<String,Object> rating = Proxy.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"",conn);
//            if (!rating.isEmpty()) map.put("rating", rating);
            if (course.allowRating()) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseAbout.ftl");
        }),engine);

        get("/course/learnMat/:number",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":number");;
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Proxy.findCourseByCourseId(courseId, conn);
            if((int)course.getProfessorId() == id) map.put("role", "prof");
            else map.put("role","learner");
            //add each lesson
            ArrayList<Lesson> lessons = Proxy.getLessonsByCourseId(Integer.parseInt(courseId), conn);
            if (!lessons.isEmpty()) map.put("lessons",lessons);
            map.put("courseNumber",courseId);
            map.put("name", course.getName());
            if (course.allowRating()) map.put("viewRate", true);
            map.put("ip",ip);
            conn.close();
            return new ModelAndView(map,"courseLearnMatS.ftl");
        }),engine);

        post("/lesson/save/:courseId", (request,response)-> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");;
            Connection conn = MySqlConnection.getConnection();
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() != id) {
                conn.close();
                response.redirect("/err");
            }
            Map<String,String> formFields = extractFields(request.body());
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
            //branch based on which button was pressed
            if( formFields.containsKey("saveButton")){
                Proxy.createOrUpdateLesson(temp,Integer.parseInt(courseId), conn);

            }else if(formFields.containsKey("deleteButton")){
                Proxy.deleteLesson(temp.getId(), conn);

            }else if(formFields.containsKey("dlButton")){

                    FileManager.downloadFile(request,response,URLDecoder.decode(formFields.get("dlButton"),"UTF-8"));
                    return null;

            }else if(formFields.containsKey("deleteLMButton")){
                Proxy.deleteLearningMaterial(temp.getId(),URLDecoder.decode(formFields.get("deleteLMButton"),"UTF-8"), conn);
            }else if(formFields.containsKey("shareButton")){
                System.out.println("Share lesson!");
                Proxy.createDGPostLesson(
                        Proxy.getDGIdByCourseId(Integer.parseInt(courseId),conn)
                        ,sess.attribute("id"),
                        URLDecoder.decode(formFields.get("name"),"UTF-8"),
                        "Check out this lesson!",
                        "/course/learnMat/"+courseId,
                        conn);
            }
            conn.close();
            response.redirect("/course/learnMat/"+courseId);
            return null;
        });

        post("/lesson/add/:courseId", (request,response)-> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");;
            Connection conn = MySqlConnection.getConnection();
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() != id) response.redirect("/err");
            Proxy.createLesson("New Lesson","Lesson Requirements", Integer.parseInt(request.params(":courseId")), conn);
            conn.close();
            response.redirect("/course/learnMat/"+courseId);
            return null;
        });

        get("/course/quiz/:courseId",((request, response) -> {

             Map<String,Object> map = new HashMap<>();
             Session sess = request.session();
             int id = sess.attribute("id");
             String courseId = request.params(":courseId");
             Connection conn = MySqlConnection.getConnection();

            Course course = Course.getCourse(courseId, conn);
            map.put("lessonCount",Proxy.getLessonsByCourseId(course.getId(),conn).size());
             courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
//             if((int)course.getProfessorId() == id) map.put("role", "prof");
//             else map.put("role","learner");
             String role;
             if((int)course.getProfessorId() == id) role = "prof";
             else role = "learner";
             map.put("role",role);

             map.put("courseId", courseId);
             map.put("name", course.getName());
            if (course.allowRating()) map.put("viewRate", true);
            Map<Integer, Object>  quizzes = Quiz.getQuizzes(Integer.parseInt(courseId), conn);
            if (!quizzes.isEmpty()) {
                if (role.equals("learner")){
                    Quiz course_details = new Quiz();
                    course_details.learnerId = id;
                    course_details.courseId = Integer.parseInt(courseId);
                    quizzes = Proxy.getQuizGrades(course_details,quizzes,conn);
                }
                map.put("quizzes",quizzes);
            }
            conn.close();
            return new ModelAndView(map,"courseQuiz.ftl");
        }),engine);

        get("/course/:courseId/create-quiz",((request, response) -> {

            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            Connection conn = MySqlConnection.getConnection();
            String courseId = request.params(":courseId");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("courseId", courseId);
            map.put("name", course.getName());
            String edit = request.queryParams("e");

            String quizId = request.queryParams("quizId");
            ArrayList<Lesson> lessons = Proxy.getLessonsByCourseId(Integer.parseInt(courseId), conn);
            
            Quiz quizEdit = new Quiz();
            if (edit == null || edit.contains("-1")) edit="c";
            switch (edit){
                case "e":
                    map.put("e",1);
                    int quizIdInt = Integer.parseInt(quizId);
                    map.put("quizId",quizId);
                    quizEdit =  Proxy.viewSingleQuiz(quizIdInt,conn);
                    Lesson lesson = Proxy.getLessonById(quizEdit.lessonId,conn);
                    map.put("currLesson",quizEdit.lessonId);
                    map.put("currLessonName", lesson.name);
                    lessons.removeIf(l -> l.getId() == lesson.getId());
                    map.put("quizName",quizEdit.quizName);
                    map.put("minMark",quizEdit.MinMark);
                    map.put("title","Modify");
                    break;
                case "d":
                    quizEdit.quizId = Integer.parseInt(quizId);
                    Proxy.deleteQuiz(quizEdit,conn);
                    response.redirect("/course/quiz/"+courseId+"/");
                    break;
                case "c":
                    map.put("e",-1);
                    map.put("quizName","");
                    map.put("minMark",0);
                    map.put("title", "Create");
                    break;
            }
            map.put("lessons",Proxy.getLessonsByCourseId(Integer.parseInt(courseId), conn));
            if (course.allowRating()) map.put("viewRate", true);
            map.put("lessons",lessons);
            conn.close();
            return new ModelAndView(map,"createQuiz.ftl");
        }),engine);

        post("/course/:courseId/create-quiz",((request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Quiz newQuiz = new Quiz();
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            if(course.getProfessorId() != id) response.redirect("/err");
            newQuiz.lessonId = Integer.parseInt(formFields.get("linkedLesson"));
            newQuiz.quizName = URLDecoder.decode(formFields.get("quizName"), "UTF-8");
            newQuiz.MinMark = Integer.parseInt(formFields.get("minMark"));
            Map<String,Object> map = new HashMap<>();
            String edit = request.queryParams("e");
            if (edit == null || edit.contains("-1")) {
                Proxy.createQuiz(newQuiz, conn);
            } else{
                newQuiz.quizId = Integer.parseInt(formFields.get("action"));
                Proxy.updateQuiz(newQuiz, conn);
                Proxy.updateTotalMark(newQuiz,conn);
            }
            conn.close();
            response.redirect("/course/quiz/"+courseId);

            return null;

        }),engine);

        get("/course/quiz/:courseId/:quizId",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            String edit = request.queryParams("e");
            Connection conn = MySqlConnection.getConnection();
            String courseId = request.params(":courseId");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            String quizId = request.params(":quizId");
            quizId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(quizId));

            Course course = Course.getCourse(courseId, conn);
            String role;
            if(course.getProfessorId() == id) role = "prof";
            else role = "learner";
            map.put("courseId", courseId);
            map.put("role",role);
            Quiz quiz = Proxy.viewSingleQuiz(Integer.parseInt(quizId),conn);
            Map<Integer,Object> questions = Proxy.getQuestions(quiz,conn);
            map.put("quizName", quiz.quizName);
            map.put("quiz",quiz);
            map.put("quizId",quizId);

            quiz.learnerId = id;
            quiz.quizId = Integer.parseInt(quizId);
            quiz.courseId = Integer.parseInt(courseId);
            if (role.equals("learner")){
                questions = Proxy.getQuestionAttempts(quiz, questions,conn);
                Proxy.calculateGrades(quiz,conn);
            }
            if (edit != null){
                if (edit.equals("rt"))
                {
                    Proxy.deleteQuizAttempt(quiz,conn);
                    Proxy.deleteQuestionAttempts(quiz,conn);
                }
                if (edit.equals("t")){
                    questions = Proxy.getQuestionAttempts(quiz, questions,conn);
                }
            }
            if (!questions.isEmpty()) {
                map.put("questions",questions);
                map.put("quizName", quiz.quizName);
            }
            conn.close();
            return new ModelAndView(map,"quizQuestions.ftl");

        }),engine);

        get("/course/create-question",((request, response) -> {

            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            Connection conn = MySqlConnection.getConnection();
            String courseId = request.queryParams("courseId");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));

            Course course = Course.getCourse(courseId, conn);
            String edit = request.queryParams("e");
            if(course.getProfessorId() == id) map.put("role", "prof");
            else if (edit.length()>0) map.put("role", "learner");
            else
            {
                conn.close();
                response.redirect("/err");
            }
            map.put("courseId", courseId);
            map.put("name", course.getName());

            String quizId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("quizId")));
            map.put("quizId",quizId);

            Quiz quiz = Proxy.viewSingleQuiz(Integer.parseInt(quizId),conn);
            map.put("tot",quiz.totalMark);
            map.put("min",quiz.MinMark);
            map.put("lessonId", quiz.lessonId);
            quiz.learnerId = id;
            if (edit == null || edit.contains("-1")) edit = "c";
            String questionId;
            Map<String, Object> question;
            Object questionText;
            quiz.quizId = Integer.parseInt(quizId);
            switch (edit){
                case "c":
                    map.put("e",-1);
                    map.put("mark",0);
                    map.put("title", "Create");
                    break;
                case "e":
                    map.put("e",1);
                    questionId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("questionId")));
                    question = Proxy.getQuestion(Integer.parseInt(questionId), conn);
                    questionText = question.get("questionText");
                    sess.attribute("questionId",question);
                    map.put("title","Modify "+questionText);
                    map.put("questionText",questionText);
                    map.put("question",question);
                    map.put("questionId",questionId);
                    break;
                case "t":
                    map.put("e","t");
                    questionId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("questionId")));
                    question = Proxy.getQuestion(Integer.parseInt(questionId), conn);
                    quiz.questionId = Integer.parseInt(questionId);
                    question.replace("answer","");
                    questionText = question.get("questionText");
                    sess.attribute("questionId",question);
                    map.put("title","Modify "+questionText);
                    map.put("questionText",questionText);
                    map.put("question",question);
                    map.put("questionId",questionId);
                    break;
                case "rt":
                    map.put("e","rt");
                    questionId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("questionId")));
                    question = Proxy.getQuestion(Integer.parseInt(questionId), conn);
                    quiz.questionId = Integer.parseInt(questionId);
                    question.replace("answer",Proxy.viewQuizResponse(quiz,conn));
                    questionText = question.get("questionText");
                    sess.attribute("questionId",question);
                    map.put("title","Modify "+questionText);
                    map.put("questionText",questionText);
                    map.put("question",question);
                    map.put("questionId",questionId);
                    break;
                case "d":
                    quiz.questionId = Integer.parseInt(String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("questionId"))));
                    Proxy.deleteQuestion(quiz,conn);
                    response.redirect("/course/quiz/"+courseId+"/"+quizId);
                    break;
            }

            conn.close();
            return new ModelAndView(map,"question.ftl");
        }),engine);

        post("/course/create-question",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> formFields = extractFields(request.body());
            Session sess = request.session();
            int id = sess.attribute("id");

            Quiz newQuiz = new Quiz();
            String courseId = request.queryParams("courseId");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Connection conn = MySqlConnection.getConnection();
            String edit = request.queryParams("e");

            String quizId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("quizId")));

            if (!edit.contains("t")){
                newQuiz.questionText = URLDecoder.decode(formFields.get("QText"), "UTF-8");
                newQuiz.mark = Integer.parseInt(formFields.get("marks"));

                newQuiz.responseA = URLDecoder.decode(formFields.get("QA"), "UTF-8");
                newQuiz.responseB = URLDecoder.decode(formFields.get("QB"), "UTF-8");
                newQuiz.responseC = URLDecoder.decode(formFields.get("QC"), "UTF-8");
                newQuiz.responseD = URLDecoder.decode(formFields.get("QD"), "UTF-8");

            }
            newQuiz.quizId = Integer.parseInt(quizId);
            newQuiz.answer = URLDecoder.decode(formFields.get("customRadio"), "UTF-8");
            if (edit == null || edit.contains("-1"))
            {
                Proxy.createQuestion(newQuiz,conn);
                Proxy.updateTotalMark(newQuiz,conn);
            }
            else if (edit.contains("t") || edit.contains("rt")){
                newQuiz.learnerId = id;
                int questionId = Integer.parseInt(formFields.get("action"));
                newQuiz.questionId = questionId;
                Map<String, Object> dbQuestion = Proxy.getQuestion(questionId,conn);
                String correctAnswer = String.valueOf(dbQuestion.get("answer"));
                int grade = 0;
                if (newQuiz.answer.equals(correctAnswer))
                    grade = (int) dbQuestion.get("mark");

                switch (edit){
                    case "t":
                        Proxy.takeQuestion(newQuiz,grade,conn);
                        break;
                    case "rt":
                        Proxy.reTakeQuestion(newQuiz,grade,conn);
                        break;
                }
            }
            else
            {
                int questionId = Integer.parseInt(formFields.get("action"));
                newQuiz.questionId = questionId;
                sess.removeAttribute("questionId");
                Proxy.updateQuestion(newQuiz,conn);
                Proxy.updateTotalMark(newQuiz,conn);
            }
            conn.close();
            response.redirect("/course/quiz/"+courseId+"/"+quizId);
            return null;

        }),engine);


        post("/course/add/:courseId", (request,response)-> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            Course course = Course.getCourse(courseId, conn);
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            if(course.getProfessorId() != id) response.redirect("/err");
            Proxy.createLesson("New Lesson","Lesson Requirements", Integer.parseInt(request.params(":courseId")), conn);
            response.redirect("/course/learnMat/"+courseId);
            conn.close();
            return null;
        });

        get("/course/grades/:courseId",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            Connection conn = MySqlConnection.getConnection();
            String courseId = request.params(":courseId");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() == id) map.put("role", "prof");
            else {
                conn.close();
                response.redirect("/course/grade/individual/" + courseId);
            }
            Map<Integer, Map<String,String>> classList = Proxy.getClassList(Integer.parseInt(courseId), conn);
            classList.remove(id);
            if (!classList.isEmpty()) map.put("classList", classList);
            map.put("courseId", courseId);
            map.put("name", course.getName());
            if (course.allowRating()) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseGrade.ftl");
        }),engine);

        get("/course/grade/individual/:courseId/:userId",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            int userId = Integer.parseInt(request.params(":userId"));
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            String name = Proxy.getNameFromUserId(userId, conn);
            if((int)course.getProfessorId() != id) {
                conn.close();
                response.redirect("/err");
            }
            map.put("role", "prof");
            Grades grades = new Grades(Integer.valueOf(courseId), userId, conn);
            Map<String,Object> g = grades.getGrades();
            if (!g.isEmpty()) map.put("grades", g);
            map.put("learnerName", name);
            map.put("courseId", courseId);
            map.put("name", course.getName());
            if (course.allowRating()) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseGradeIndividual.ftl");
        }),engine);

        get("/course/grade/individual/:courseId",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            String name = sess.attribute("firstName").toString() + " "
                    + sess.attribute("lastName").toString();
            if((int)course.getProfessorId() == id) {
                conn.close();
                response.redirect("/err");
            }
            map.put("role","learner");
            Grades grades = new Grades(Integer.valueOf(courseId), id, conn);
            Map<String,Object> g = grades.getGrades();
            if (!g.isEmpty()) map.put("grades", g);
            map.put("learnerName", name);
            map.put("courseId", courseId);
            map.put("name", course.getName());
            if (course.allowRating()) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseGradeIndividual.ftl");
        }),engine);

        get("/course/classlist/:courseId",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");

            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("courseId", courseId);
            map.put("name", course.getName());
            Map<Integer, Map<String,String>> classList = Proxy.getClassList(Integer.parseInt(courseId), conn);
            map.put("classList", classList);
            map.put("profId", course.getProfessorId());
            if (course.allowRating()) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseClasslist.ftl");
        }),engine);

        get("/course/rate/:courseId",((request, response) -> {
            Session sess = request.session();
            Map<String,Object> map = new HashMap<>();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() == id) {
                map.put("role", "prof");
                Map<Integer, Map<String,String>> classList = Proxy.getClassList(Integer.parseInt(courseId), conn);
                classList.remove(course.getProfessorId());
                map.put("classList", classList);
            }
            else {
                map.put("role","learner");
                Map<String,Object> rating = Proxy.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"", conn);
                if (!rating.isEmpty()) map.put("course_rate", rating);
                rating = Proxy.getRatingAndFeedbackOfUserGivenUserId(course.getProfessorId(), "", "", conn);
                if (!rating.isEmpty()) map.put("prof", rating);
                map.put("profId", course.getProfessorId());
            }

            map.put("courseId", courseId);
            map.put("name", course.getName());
            conn.close();
            return new ModelAndView(map,"courseRate.ftl");
        }),engine);

        post("/course/rate/:courseId",((request, response) -> {
            Session sess = request.session();
            Map<String,Object> map = new HashMap<>();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Map<String,String> formFields = extractFields(request.body());
            Course course = Course.getCourse(courseId, conn);
            if((int)course.getProfessorId() == id) {
                map.put("role", "prof");
                if(formFields.containsKey("doneRating")) {
                    boolean flag = Rating.addRating(formFields, conn);
                    if (flag) response.redirect("/course/rate/" + courseId);
                    else {
                        map.put("err", true);
                    }
                }
                int learner_id = Integer.parseInt(formFields.get("rateLearner"));
                Map<String, Object> rating = Proxy.getRatingAndFeedbackOfUserGivenUserId(id,"","",conn);
                if (!rating.isEmpty()) map.put("learner", rating);
                map.put("learnerId", learner_id);
                String name = Proxy.getNameFromUserId(learner_id, conn);
                map.put("learner_name", name);
                map.put("rateLearner", true);
            }
            else {
                map.put("role","learner");
                boolean flag = Course.addRating(formFields, courseId, conn);
                if (flag) map.put("success", true);
                else map.put("err", true);
                Map<String,Object> rating = Proxy.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"", conn);
                if (!rating.isEmpty()) map.put("course_rate", rating);
                rating = Proxy.getRatingAndFeedbackOfUserGivenUserId(course.getProfessorId(), "", "", conn);
                if (!rating.isEmpty()) map.put("prof", rating);
                map.put("profId", course.getProfessorId());
            }

            map.put("courseId", courseId);
            map.put("name", course.getName());
            conn.close();
            return new ModelAndView(map,"courseRate.ftl");
        }),engine);

        get("/courses/all", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            if (!role.contentEquals("admin")) response.redirect("/err");
            map.put("role","admin");
            Connection conn = MySqlConnection.getConnection();
            Course course = new Course();
            map.put("filterStatus", "All");
            map.put("filterOptions",course.filterOptions("All"));
            ArrayList<Course> courses = course.getCourses("All", conn);
            if (!courses.isEmpty()) map.put("courses",courses);

            String e_id = request.queryParams("edit");
            String d_id = request.queryParams("del");
            boolean done = false;

            if (d_id != null) {
                course.setId(Integer.parseInt(d_id));
                done = course.deleteCourse(conn);
                if(done) {
                    conn.close();
                    response.redirect("/courses/all");
                }
            }
            if (e_id != null) {
                conn.close();
                response.redirect("/courses/create-course?e="+e_id);
            }
            conn.close();
            return new ModelAndView(map,"coursesAll.ftl");
        },engine);

        post("/courses/all",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            if (!role.contentEquals("admin")) response.redirect("/err");
            map.put("role","admin");
            Connection conn = MySqlConnection.getConnection();
            Course course = new Course();
            ArrayList<Course> courses = new ArrayList<>();
            Map<String,String> formFields = extractFields(request.body());
            String filterStatus = "All";
            if (formFields.containsKey("filterBy")) {
                courses = course.getCourses(formFields.get("filterBy"), conn);
                filterStatus = formFields.get("filterBy");
            }
            else courses = course.getCourses("All", conn);
            map.put("filterStatus", filterStatus);
            map.put("filterOptions",course.filterOptions(filterStatus));
            if (!courses.isEmpty()) map.put("courses",courses);
            conn.close();
            return new ModelAndView(map,"coursesAll.ftl");
        }),engine);

        get("/courses",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int id = sess.attribute("id");
            Connection conn = MySqlConnection.getConnection();
            Course c = new Course();
            ArrayList<Course> courses = Course.getMyCourses(id, conn);
            map.put("filterStatus", "All");
            map.put("filterOptions", c.filterOptions("All"));
            if (!courses.isEmpty()) map.put("courses", courses);
            map.put("role", role);
            conn.close();
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        post("/courses",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int id = sess.attribute("id");
            Connection conn = MySqlConnection.getConnection();
            Course course = new Course();
            ArrayList<Course> courses = new ArrayList<>();
            Map<String,String> formFields = extractFields(request.body());
            String filterStatus = "All";
            if (formFields.containsKey("filterBy")) {
                courses = course.getCourses(formFields.get("filterBy"), conn);
                filterStatus = formFields.get("filterBy");
            }
            else courses = course.getCourses("All", conn);
            map.put("filterStatus", filterStatus);
            map.put("filterOptions",course.filterOptions(filterStatus));
            if (!courses.isEmpty()) map.put("courses",courses);
            map.put("role", role);
            conn.close();
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        get("/courses/create-course",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,Object> map = new HashMap<>();
            map.put("role", "admin");
            Connection conn = MySqlConnection.getConnection();
            map.put("profList", Proxy.findAllProfs(conn));
            map.put("e",-1);
            Map<Integer, String> allCourses = Course.allCourses(conn);
            map.put("title","Create Course");
            String e_id = request.queryParams("e");
            if (e_id != null) {
                Map<Integer,String> profs = Proxy.findAllProfs(conn);
                Map.Entry<Integer,String> entry = profs.entrySet().iterator().next();
                int prof_id = entry.getKey();
                Course course = new Course();
                LinkedHashMap<String, Boolean> days = Course.findAllDays();
                String editId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(e_id));
                course.setId(Integer.parseInt(editId.replaceAll(" ","")));
                course = course.editCourse(conn);
                prof_id = course.getProfessorId();
                String meeting_days = course.getMeeting_days();
                map.put("course", course);
                days.forEach((k, v)-> {
                    if (meeting_days.contains(k)) days.put(k,true);
                });
                map.put("currProf",course.getProfessorName());
                map.put("prof_id", prof_id);
                profs.remove(prof_id);
                map.put("days",days);
                map.put("profList",profs);
                map.put("e",e_id);
                allCourses.remove(e_id);
                map.put("currPreq", course.getPreReqName());
                allCourses.remove(course.getPreReq());
                map.put("course_id",course.getPreReq());
                map.put("prereqs", allCourses);
                map.put("headingChange", true);

            } else {
                LinkedHashMap<String, Boolean> days = Course.findAllDays();
                System.out.println(days);
                map.put("days",days);
                map.put("prereqs", allCourses);
            };
            conn.close();
            return new ModelAndView(map,"createCourse.ftl");
        }),engine);

        post("/courses/create-course", (request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            String edit = request.queryParams("e");
            Connection conn = MySqlConnection.getConnection();
            Map<String,Object> map = new HashMap<>();
            boolean flag = true;
            Map<Integer,String> profs = Proxy.findAllProfs(conn);
            LinkedHashMap<String, Boolean> allDays = Course.findAllDays();
            edit = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(edit));
            map.put("e",edit);
            Map<Integer, String> allCourses = Course.allCourses(conn);
            int old_profId = -1;
            Course course = new Course();
            course.createCourseObj(formFields);
            allDays.forEach((k, v)-> {
                if (course.getMeeting_days().contains(k)) allDays.put(k,true);
            });

            if(course.checkTimeError()) {
                map.put("errTime", "true");
                flag = false;
            }
            if(course.checkNameError()) {
                flag = false;
                map.put("nameErr", true);
            }
            if (!edit.contentEquals("-1")) {
                if(course.checkDateErrorEdit()) {
                    map.put("errDate", "true");
                    flag = false;
                }
                old_profId = (int) Proxy.findCourseByCourseId(edit, conn).getProfessorId();
            } else {
                if(course.checkDateError()) {
                    map.put("errDate", "true");
                    flag = false;
                }
            }

            if (flag) flag = course.createOrUpdateCourse(edit, old_profId, conn);
            if(flag) {
                conn.close();
                response.redirect("/courses/all");
            }
            if (course.getPreReq() != -1) {
                allCourses.remove(Integer.parseInt(edit));
                map.put("currPreq", course.getPreReqName());
                allCourses.remove(course.getPreReq());
                map.put("course_id", course.getPreReq());
            }
            map.put("days",allDays);
            map.put("currProf",profs.get(course.getProfessorId()));
            map.put("prof_id",course.getProfessorId());
            profs.remove(course.getProfessorId());
            map.put("profList",profs);
            map.put("prereqs", allCourses);
            map.put("role", "admin");
            map.put("course", course);
            conn.close();
            return new ModelAndView(map,"createCourse.ftl");
        },engine);

        get("/enroll",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            Connection conn = MySqlConnection.getConnection();
            ArrayList<Course> courses = Enrollment.findAllAvailableCourses(conn);
            conn.close();
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
            Map<String,Object> map = new HashMap<>();
            map.put("role", role);

            map.put("courseNumber", request.params(":number"));
            Connection conn = MySqlConnection.getConnection();
            Course tempCourse = Proxy.findCourseByCourseId(request.params(":number"), conn);
            map.put("c", tempCourse);
//            for(Map.Entry<String, Object> entry: tempCourse.entrySet()){
//                map.put(entry.getKey(), entry.getValue().toString());
//            }
//            map.put("profName", Proxy.findProfName(Integer.parseInt(map.get("prof_id")), conn));
            conn.close();
            return new ModelAndView(map,"enrollAbout.ftl");
        }),engine);

        get("/ratings",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Connection conn = MySqlConnection.getConnection();
            Map<String, Object> map = Rating.getMethodFunctionality(role, conn);
            map.forEach((k,v)->map.put(k,v));
            map.put("role", role);
            conn.close();
            return new ModelAndView(map,"ratings.ftl");
        }),engine);

        post("/ratings",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> formFields = extractFields(request.body());
            Connection conn = MySqlConnection.getConnection();
            Map<String,Object> map = Rating.postMethodFunctionality(formFields, role, conn);
            map.forEach((k,v)->map.put(k,v));
            map.put("role", role);
            conn.close();
            return new ModelAndView(map,"ratings.ftl");
        }),engine);

        get("/discussion-groups", (request, response) -> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            Connection conn = MySqlConnection.getConnection();
            ArrayList<Map<String,Object>> groups = Proxy.getMyDiscussionGroups(id, conn);
            Map<Integer,Map<String, Object>> allGroups = DiscussionGroups.getGroups("", -1, id, conn);
            Map<Integer, Object> requestedGroups = Proxy.getPendingGroupRequests(id, conn);
            map.put("groups", groups);
            map.put("allGroups", allGroups);
            map.put("role", role);
            map.put("filterOptions", DiscussionGroups.getSearchOptions(""));
            if (requestedGroups.isEmpty()) map.put("emptyReq", true);
            else map.put("requestedGroups",requestedGroups);
            conn.close();
            return new ModelAndView(map,"discussionGroups.ftl");
        },engine);

        post("/discussion-groups",((request, response) -> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String role = sess.attribute("role").toString();
            Map<String,String> formFields = extractFields(request.body());
            Connection conn = MySqlConnection.getConnection();
            Map<String, Object> map = DiscussionGroups.postMethodFunctionality(formFields,id, conn);
            map.forEach((k,v)->map.put(k,v));
            if (map.containsKey("refresh")) {
                conn.close();
                response.redirect("/discussion-groups");
            }
            map.put("role", role);
            conn.close();
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
            Connection conn = MySqlConnection.getConnection();
            Map<String,String> formFields = extractFields(request.body());
            boolean flag = DiscussionGroups.createGroup(formFields, id, conn);
            if (flag) {
                conn.close();
                response.redirect("/discussion-groups");
            }
            else map.put("err", true);
            conn.close();
            return new ModelAndView(map,"createDiscussionGroup.ftl");
        }),engine);

        get("/discussion/group-desc/:id",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int user_id = sess.attribute("id");
            int id = Integer.parseInt(request.params(":id"));
            Connection conn = MySqlConnection.getConnection();
            Map<String, Object> group = Proxy.getGroupDetailsByGroupId(id, conn);
            String member = DiscussionGroups.isMemberOfGroup(user_id, id, conn);
            Map<Integer, String> members = Proxy.viewAllGroupMembers(id, conn);
            Map<Integer, String> requests = Proxy.getAllPendingGroupRequestsOfGroup(id, conn);
            int prof = DiscussionGroups.findProfofCourse(group, conn);
            Map<String,Object> map = new HashMap<>();
            if (prof != 0) map.put("prof", prof);
            map.put("status", member);
            map.put("group", group);
            map.put("role", role);
            map.put("members", members);
            if (!requests.isEmpty()) map.put("reqs", requests);
            map.put("id", id);
            map.put("posts",Proxy.getDGPostsById(Integer.parseInt(URLDecoder.decode(request.params(":id"),"UTF-8")),conn));
            conn.close();
            return new ModelAndView(map,"groupDesc.ftl");
        }),engine);

        post("/discussion/group-desc/:id", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int user_id = sess.attribute("id");
            int id = Integer.parseInt(request.params(":id"));
            Connection conn = MySqlConnection.getConnection();
            Map<String,String> formFields = extractFields(request.body());
            if (formFields.containsKey("add")) {
                int u_id = Integer.parseInt(formFields.get("add"));
                boolean flag = Proxy.addDGmember(u_id, id, conn);
                boolean flag2 = false;
                if (flag) flag2 = Proxy.deleteRequestForGroup(u_id, id, conn);
                if (flag2) response.redirect("/discussion/group-desc/"+id);
            } else if (formFields.containsKey("del")) {
                int u_id = Integer.parseInt(formFields.get("del"));
                boolean flag = Proxy.deleteRequestForGroup(u_id, id, conn);
                if (flag) response.redirect("/discussion/group-desc/"+id);
            } else if (formFields.containsKey("leave")) {
                boolean flag = Proxy.deleteDGMember(user_id, id, conn);
                if (flag) response.redirect("/discussion-groups");
            }
            Map<String,Object> map = new HashMap<>();
            conn.close();
            return new ModelAndView(map,"groupDesc.ftl");
        },engine);

        get("/discussion/create-post/:dgId",((request, response) -> {
            //need course and user Id
            Connection conn = MySqlConnection.getConnection();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            map.put("dgName", "Test Name"); //CHANGE
            map.put("dgId",URLDecoder.decode(request.params(":dgId"),"UTF-8"));
            conn.close();

            return new ModelAndView(map,"discussionPost.ftl");
        }),engine);

        post("/discussion/create-post/:dgId",((request, response) -> {
            Session sess = request.session();
            Connection conn = MySqlConnection.getConnection();
            String role = sess.attribute("role").toString();
            Map<String,String> map = new HashMap<>();
            map.put("role", role);
            map.put("dgName", "Test Name"); //CHANGE
            map.put("dgId",URLDecoder.decode(request.params(":dgId"),"UTF-8"));
            Map<String,String> formFields = extractFields(request.body());
            System.out.println(formFields);
            Proxy.createDGPost(Integer.parseInt(
                    URLDecoder.decode(request.params(":dgId"),"UTF-8")),
                    sess.attribute("id"),
                    URLDecoder.decode(formFields.get("name"), "UTF-8"),
                    URLDecoder.decode(formFields.get("content"),"UTF-8"),
                    conn);
            conn.close();
            response.redirect("/discussion/group-desc/"+request.params(":dgId"));
            return new ModelAndView(map,"discussionPost.ftl");
        }),engine);

        get("/approval",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,Object> map = new HashMap<>();
            Connection conn = MySqlConnection.getConnection();
            Map<Integer,String> profs = Proxy.viewAllRequests(conn);
            map.put("profs",profs);
            map.put("role", role);
            Map<String,String> searchOptions = Admin.getSearchOptions("");
            map.put("searchOptions",searchOptions);
            map.put("users",new HashMap<Integer,String>());
            conn.close();
            return new ModelAndView(map,"approval.ftl");
        }),engine);

        post("/approval", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,String> formFields = extractFields(request.body());
            Connection conn = MySqlConnection.getConnection();
            Map<String, Object> map = Admin.postMethodFunctionality(formFields, conn);
            if (map.containsKey("redirect") && (boolean) map.get("redirect")) {
                conn.close();
                response.redirect("/approval");
            }
            map.forEach((k, v)-> map.put(k,v));
            map.put("role", role);
            conn.close();
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
            Connection conn = MySqlConnection.getConnection();
            Map<String, Object> map = Learner.applyForProfessor(formFields,
                    sess.attribute("firstName").toString(),
                    sess.attribute("lastName").toString(),
                    sess.attribute("email").toString(),
                    conn);
            map.forEach((k, v)-> map.put(k,v));
            return new ModelAndView(map,"profApply.ftl");
        },engine);

        get("/err",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session session = request.session();
            if (session.attribute("firstName") == null) {
                response.redirect("/login/errAuth");
            } else {
                Connection conn = MySqlConnection.getConnection();
                String role = session.attribute("role").toString();
                int id = session.attribute("id");
                Home home = new Home(id, role, conn);
                map.put("name", session.attribute("firstName").toString() + " "
                        + session.attribute("lastName").toString());
                map.put("role", session.attribute("role"));
                ArrayList<Course> courses = home.getCourses();
                Map<String,Object> rating = home.getRating();
                ArrayList<Map<String,Object>> groups = home.getGroups();
                if (!courses.isEmpty()) map.put("courses", courses);
                if(!groups.isEmpty()) map.put("groups", groups);
                if (!rating.isEmpty()) map.put("rating", home.getRating());
                map.put("notAuthorized", "You have been redirected to the " +
                        "home page as you were not authorized to view the page" +
                        " you selected or something went wrong. Please email " +
                        "mypls@rit.edu for support");
                conn.close();
            }
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/logout",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            user_current.erase();
            request.session().invalidate();
            response.redirect("/login");
            return new ModelAndView(map,"homePage.ftl");
        }),engine);

        get("/upload",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            return new ModelAndView(map,"upload.ftl");
        }),engine);

        post("/upload/:courseId/:lessonId", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = request.raw().getPart("uploadFile").getInputStream()) { // getPart needs to use same "name" as input field in form
                Connection conn = MySqlConnection.getConnection();
                File newFile = new File(uploadDir.toPath().toString(),request.raw().getPart("uploadFile").getSubmittedFileName());
                Files.copy(input, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Proxy.createLearningMaterial(Integer.parseInt(request.params(":lessonId")),
                        request.raw().getPart("uploadFile").getSubmittedFileName(),
                        conn
                );
                conn.close();
            }
            System.out.println();
            response.redirect("/course/learnMat/"+request.params(":courseId"));
            return "Success!";
        });

        get("/view/:courseNum/:fileName",((request, response) -> {
            Map<String,String> map = new HashMap<>();
            String[] vals =request.params(":fileName").split("\\.");
            map.put("fileName", vals[0]);
            map.put("fileType", "."+vals[1]);
            map.put("filePath", "/"+request.params(":fileName"));
            map.put("courseNumber", request.params(":courseNum"));
            return new ModelAndView(map,"viewFile.ftl");
        }),engine);
    }
}