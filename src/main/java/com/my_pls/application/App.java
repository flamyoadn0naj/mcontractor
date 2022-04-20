package com.my_pls.application;

import com.my_pls.*;
import com.my_pls.application.components.*;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;


//things for file upload
import javax.servlet.MultipartConfigElement;
import javax.xml.crypto.Data;
import java.io.*;

//end things for file upload

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.text.NumberFormat;
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
        String ip = "10.181.95.232:8080";
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
            Connection conn = MySqlConnection.getConnection();
            Map<String,String> formFields = extractFields(request.body());
            Pair p = Login.postMethodDefaults(map, formFields, user_current, pwd_manager, conn);
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
            conn.close();
            return new ModelAndView(map,"login.ftl");
        },engine);

        get("/register",(request, response) -> {
            Map<String,Object> map = Register.getMethodDefaults();
            map.forEach((k,v)->map.put(k,v));
            return new ModelAndView(map,"login.ftl");
        },engine);

        post("/register",(request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Connection conn = MySqlConnection.getConnection();
            Pair p = Register.postMethodDefaults(formFields, user_current, pwd_manager,conn);
            Map<String,Object> map = p.fst();
            map.forEach((k,v)->map.put(k,v));
            User logUser = p.snd();

            user_current.setAll(logUser.firstName, logUser.lastName, logUser.password,
                    logUser.email, -1, "learner");
            if (logUser.firstName.length() > 0){
                request.session().attribute("email",logUser.email); //saved to session
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
                String hash = request.queryParams("key2");
                boolean flag = DataMapper.verifyEmailofUser(email, hash, conn);
                if (flag) {
                    int id = DataMapper.getUserIdFromEmail(email, conn);
                    DataMapper.addDGmember(id,311,conn);
                }
            }
            conn.close();
            return new ModelAndView(map,"verifyRegister.ftl");
        },engine);

        post("/verify-register/:type",((request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Map<String, Object> map = new HashMap<>();
            if (formFields.containsKey("resend")) {
                Connection conn = MySqlConnection.getConnection();
                boolean flag = DataMapper.resendEmailConfirmation(user_current.email, conn);
                if (flag) map.put("resend", true);
                else map.put("resend", false);
                conn.close();
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
            Connection conn = MySqlConnection.getConnection();
            Map<String,String> formFields = extractFields(request.body());
            Map<String,Object> map = ForgetPassword.postMethodDefaults(pageType,
                    formFields,pwd_manager, conn);
            map.forEach((k,v)->map.put(k,v));
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
                Map<Integer, Object> courses = home.getCourses();
                Map<String,Object> rating = home.getRating();
                ArrayList<Map<String,Object>> groups = home.getGroups();
                if (!courses.isEmpty()) map.put("courses", home.getCourses());
                if(!groups.isEmpty()) map.put("groups", home.getGroups());
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("course",course);
            map.put("courseId", courseId);
            Map<String,Object> rating = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"",conn);
            if (!rating.isEmpty()) map.put("rating", rating);
            if (Courses.allowRating(course)) map.put("viewRate", true);
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else response.redirect("/err");
            boolean flag = false;
            if (formFields.containsKey("req")) {

               String req = URLDecoder.decode(formFields.get("req"), "UTF-8");
               flag = DataMapper.updateCourseRequirements(Integer.parseInt(courseId), req, conn);

            }
            if (flag) {
                conn.close();
                response.redirect("/course/about/" + courseId);
            }
            else map.put("err", true);
            map.put("course",course);
            map.put("courseId", courseId);
            Map<String,Object> rating = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"",conn);
            if (!rating.isEmpty()) map.put("rating", rating);
            if (Courses.allowRating(course)) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseAbout.ftl");
        }),engine);

//        get("/course/learnMat",((request, response) -> {
//            Map<String,String> map = new HashMap<>();
//            Session sess = request.session();
//            map.put("role", sess.attribute("role"));
//            return new ModelAndView(map,"courseLearnMat.ftl");
//        }),engine);

        get("/course/learnMat/:number",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":number");;
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Map<String,Object> course = DataMapper.findCourseByCourseId(courseId, conn);
            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else map.put("role","learner");
            //add each lesson
            map.put("lessons",DataMapper.getLessonsByCourseId(Integer.parseInt(courseId), conn));
            map.put("courseNumber",courseId);
            map.put("name", course.get("name"));
            if (Courses.allowRating(course)) map.put("viewRate", true);
            map.put("ip",ip);
            conn.close();
            return new ModelAndView(map,"courseLearnMatS.ftl");
        }),engine);

        post("/lesson/save/:courseId", (request,response)-> {
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");;
            Connection conn = MySqlConnection.getConnection();
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") != id) {
                conn.close();
                response.redirect("/err");
            }
            Map<String,String> formFields = extractFields(request.body());

            //System.out.println(formFields);
            //System.out.println(URLDecoder.decode(formFields.get("req"),"UTF-8"));
            Lesson temp = new Lesson(Integer.parseInt(URLDecoder.decode(formFields.get("lessonId"),"UTF-8")),

            URLDecoder.decode(formFields.get("name"),"UTF-8"),
            URLDecoder.decode(formFields.get("req"),"UTF-8"));

            //System.out.println(temp.getId());
            for(Map.Entry<String, String> element : formFields.entrySet()){
                String k = URLDecoder.decode(element.getKey(),"UTF-8");
                String v = URLDecoder.decode(element.getValue(),"UTF-8");
                if(k.equals(v)){
                    temp.materials.add(v);
                }
            }
            //branch based on which button was pressed
            if( formFields.containsKey("saveButton")){
                DataMapper.createOrUpdateLesson(temp,Integer.parseInt(courseId), conn);

            }else if(formFields.containsKey("deleteButton")){
                DataMapper.deleteLesson(temp.getId(), conn);

            }else if(formFields.containsKey("dlButton")){

                    FileManager.downloadFile(request,response,URLDecoder.decode(formFields.get("dlButton"),"UTF-8"));
                    return null;

            }else if(formFields.containsKey("deleteLMButton")){
                DataMapper.deleteLearningMaterial(temp.getId(),URLDecoder.decode(formFields.get("deleteLMButton"),"UTF-8"), conn);
            }else if(formFields.containsKey("shareButton")){
                System.out.println("Share lesson!");
                DataMapper.createDGPostLesson(
                        DataMapper.getDGIdByCourseId(Integer.parseInt(courseId),conn)
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") != id) response.redirect("/err");
            DataMapper.createLesson("New Lesson","Lesson Requirements", Integer.parseInt(request.params(":courseId")), conn);
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
             courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));

             if((int)course.get("prof_id") == id) map.put("role", "prof");
             else map.put("role","learner");
             map.put("courseId", courseId);
             map.put("name", course.get("name"));
            if (Courses.allowRating(course)) map.put("viewRate", true);
            Map<Integer, Object>  quizzes = Quiz.getQuizzes(Integer.parseInt(courseId), conn);
                if (!quizzes.isEmpty()) map.put("quizzes",quizzes);
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("courseId", courseId);
            map.put("name", course.get("name"));
            String edit = request.queryParams("e");

            String quizId = request.queryParams("quizId");
            ArrayList<Lesson> lessons = DataMapper.getLessonsByCourseId(Integer.parseInt(courseId), conn);

            Quiz quizEdit = new Quiz();
            if (edit == null || edit.contains("-1")) edit="c";
            switch (edit){
                case "e":
                    map.put("e",1);
                    int quizIdInt = Integer.parseInt(quizId);
                    map.put("quizId",quizId);
                    quizEdit =  DataMapper.viewSingleQuiz(quizIdInt,conn);
                    Lesson lesson = DataMapper.getLessonById(quizEdit.lessonId,conn);
                    map.put("currLesson",quizEdit.lessonId);
                    map.put("currLessonName", lesson.name);
                    lessons.removeIf(l -> l.getId() == lesson.getId());
                    map.put("quizName",quizEdit.quizName);
                    map.put("minMark",quizEdit.MinMark);
                    map.put("title","Modify");
                    break;
                case "d":
                    quizEdit.quizId = Integer.parseInt(quizId);
                    DataMapper.deleteQuiz(quizEdit,conn);
                    response.redirect("/course/quiz/"+courseId+"/");
                    break;
                case "c":
                    map.put("e",-1);
                    map.put("quizName","");
                    map.put("minMark",0);
                    map.put("title", "Create");
                    break;
            }
            map.put("lessons",DataMapper.getLessonsByCourseId(Integer.parseInt(courseId), conn));
            if (Courses.allowRating(course)) map.put("viewRate", true);
            map.put("lessons",lessons);
            conn.close();
            return new ModelAndView(map,"createQuiz.ftl");
        }),engine);

        post("/course/:courseId/create-quiz",((request, response) -> {
            Map<String,String> formFields = extractFields(request.body());
            Quiz newQuiz = new Quiz();
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            newQuiz.lessonId = Integer.parseInt(formFields.get("linkedLesson"));
            newQuiz.quizName = URLDecoder.decode(formFields.get("quizName"), "UTF-8");
            newQuiz.MinMark = Integer.parseInt(formFields.get("minMark"));
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            String edit = request.queryParams("e");
            if (edit == null || edit.contains("-1")) {
                DataMapper.createQuiz(newQuiz, conn);
            } else{
                newQuiz.quizId = Integer.parseInt(formFields.get("action"));
                DataMapper.updateQuiz(newQuiz, conn);
                DataMapper.updateTotalMark(newQuiz,conn);
            }
            conn.close();
            response.redirect("/course/quiz/"+courseId);

            return null;

        }),engine);

        get("/course/quiz/:courseId/:quizId",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");

            Connection conn = MySqlConnection.getConnection();
            String courseId = request.params(":courseId");
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            String quizId = request.params(":quizId");
            quizId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(quizId));

            Map<String,Object> course = Courses.getCourse(courseId, conn);

            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("courseId", courseId);
            Quiz quiz = DataMapper.viewSingleQuiz(Integer.parseInt(quizId),conn);
            Map<Integer,Object> questions = DataMapper.getQuestions(quiz,conn);
            map.put("quizName", quiz.quizName);
            map.put("quiz",quiz);
            map.put("quizId",quizId);
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

            Map<String,Object> course = Courses.getCourse(courseId, conn);

            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else
            {
                conn.close();
                response.redirect("/err");
            }
            map.put("courseId", courseId);
            map.put("name", course.get("name"));
            String edit = request.queryParams("e");
            String quizId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("quizId")));
            map.put("quizId",quizId);

            Quiz quiz = DataMapper.viewSingleQuiz(Integer.parseInt(quizId),conn);
            map.put("tot",quiz.totalMark);
            map.put("min",quiz.MinMark);
            map.put("lessonId", quiz.lessonId);

            if (edit == null || edit.contains("-1")) edit = "c";

            switch (edit){
                case "c":
                    map.put("e",-1);
                    map.put("mark",0);
                    map.put("title", "Create");
                    break;
                case "e":
                    map.put("e",1);
                    String questionId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("questionId")));
                    Map<String, Object> question = DataMapper.getQuestion(Integer.parseInt(questionId), conn);
                    Object questionText = question.get("questionText");
                    sess.attribute("questionId",question);
                    map.put("title","Modify "+questionText);
                    map.put("questionText",questionText);
                    map.put("question",question);
                    map.put("questionId",questionId);
                    break;
                case "d":
                    quiz.questionId = Integer.parseInt(String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("questionId"))));
                    DataMapper.deleteQuestion(quiz,conn);
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
            System.out.println(formFields);
            String edit = request.queryParams("e");

            String quizId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(request.queryParams("quizId")));

            newQuiz.questionText = URLDecoder.decode(formFields.get("QText"), "UTF-8");

            newQuiz.mark = Integer.parseInt(formFields.get("marks"));
            newQuiz.quizId = Integer.parseInt(quizId);
            newQuiz.responseA = URLDecoder.decode(formFields.get("QA"), "UTF-8");
            newQuiz.responseB = URLDecoder.decode(formFields.get("QB"), "UTF-8");
            newQuiz.responseC = URLDecoder.decode(formFields.get("QC"), "UTF-8");
            newQuiz.responseD = URLDecoder.decode(formFields.get("QD"), "UTF-8");
            newQuiz.answer = URLDecoder.decode(formFields.get("ans"), "UTF-8");
            if (edit == null || edit.contains("-1"))
            {
                DataMapper.createQuestion(newQuiz,conn);
                DataMapper.updateTotalMark(newQuiz,conn);
            }
            else
            {
                int questionId = Integer.parseInt(formFields.get("action"));
                newQuiz.questionId = questionId;
                sess.removeAttribute("questionId");
                DataMapper.updateQuestion(newQuiz,conn);
                DataMapper.updateTotalMark(newQuiz,conn);
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            if((int)course.get("prof_id") != id) response.redirect("/err");
            DataMapper.createLesson("New Lesson","Lesson Requirements", Integer.parseInt(request.params(":courseId")), conn);
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else {
                conn.close();
                response.redirect("/course/grade/individual/" + courseId);
            }
            map.put("courseId", courseId);
            map.put("name", course.get("name"));
            if (Courses.allowRating(course)) map.put("viewRate", true);
            conn.close();
            return new ModelAndView(map,"courseGrade.ftl");
        }),engine);

        get("/course/grade/individual/:courseId",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            int id = sess.attribute("id");
            String courseId = request.params(":courseId");
            Connection conn = MySqlConnection.getConnection();
            courseId = String.valueOf(NumberFormat.getNumberInstance(Locale.US).parse(courseId));
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("courseId", courseId);
            map.put("name", course.get("name"));
            if (Courses.allowRating(course)) map.put("viewRate", true);
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) map.put("role", "prof");
            else map.put("role","learner");
            map.put("courseId", courseId);
            map.put("name", course.get("name"));
            Map<Integer, Map<String,String>> classList = DataMapper.getClassList(Integer.parseInt(courseId), conn);
            map.put("classList", classList);
            map.put("profId", course.get("prof_id"));
            if (Courses.allowRating(course)) map.put("viewRate", true);
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) {
                map.put("role", "prof");
                Map<Integer, Map<String,String>> classList = DataMapper.getClassList(Integer.parseInt(courseId), conn);
                classList.remove((int)course.get("prof_id"));
                map.put("classList", classList);
            }
            else {
                map.put("role","learner");
                Map<String,Object> rating = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"", conn);
                if (!rating.isEmpty()) map.put("course_rate", rating);
                rating = DataMapper.getRatingAndFeedbackOfUserGivenUserId((int)course.get("prof_id"), "", "", conn);
                if (!rating.isEmpty()) map.put("prof", rating);
                map.put("profId", course.get("prof_id"));
            }

            map.put("courseId", courseId);
            map.put("name", course.get("name"));
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
            Map<String,Object> course = Courses.getCourse(courseId, conn);
            if((int)course.get("prof_id") == id) {
                map.put("role", "prof");
                if(formFields.containsKey("doneRating")) {
                    boolean flag = Rating.addRating(formFields, conn);
                    if (flag) response.redirect("/course/rate/" + courseId);
                    else {
                        map.put("err", true);
                    }
                }
                int learner_id = Integer.parseInt(formFields.get("rateLearner"));
                Map<String, Object> rating = DataMapper.getRatingAndFeedbackOfUserGivenUserId(id,"","",conn);
                if (!rating.isEmpty()) map.put("learner", rating);
                map.put("learnerId", learner_id);
                String name = DataMapper.getNameFromUserId(learner_id, conn);
                map.put("learner_name", name);
                map.put("rateLearner", true);
            }
            else {
                map.put("role","learner");
                boolean flag = Courses.addRating(formFields, courseId, conn);
                if (flag) map.put("success", true);
                else map.put("err", true);
                Map<String,Object> rating = DataMapper.getRatingAndFeedbackOfCourseGivenCourseId(Integer.parseInt(courseId),"", conn);
                if (!rating.isEmpty()) map.put("course_rate", rating);
                rating = DataMapper.getRatingAndFeedbackOfUserGivenUserId((int)course.get("prof_id"), "", "", conn);
                if (!rating.isEmpty()) map.put("prof", rating);
                map.put("profId", course.get("prof_id"));
            }

            map.put("courseId", courseId);
            map.put("name", course.get("name"));
            conn.close();
            return new ModelAndView(map,"courseRate.ftl");
        }),engine);

        get("/courses/all", (request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Connection conn = MySqlConnection.getConnection();
            Map<String,Object> map = Courses.getMethodDefaults("", conn);
            String e_id = request.queryParams("edit");
            String d_id = request.queryParams("del");
            boolean done = false;
            if (d_id != null) done = Courses.deleteCourse(d_id, conn);
            if (e_id != null) {
                conn.close();
                response.redirect("/courses/create-course?e="+e_id);
            }
            if(done) {
                conn.close();
                response.redirect("/courses/all");
            }
            map.forEach((k,v)->map.put(k,v));
            map.put("role", role);
            conn.close();
            return new ModelAndView(map,"coursesAll.ftl");
        },engine);

        post("/courses/all",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,String> formFields = extractFields(request.body());
            Map<String,Object> map = new HashMap<>();
            Connection conn = MySqlConnection.getConnection();
            if (formFields.containsKey("filterBy")) map = Courses.getMethodDefaults(formFields.get("filterBy"), conn);
            else map = Courses.getMethodDefaults("", conn);
            Map<String, Object> finalMap = map;
            map.forEach((k, v)-> finalMap.put(k,v));
            map.put("role",role);
            conn.close();
            return new ModelAndView(map,"coursesAll.ftl");
        }),engine);

        get("/courses",((request, response) -> {
            Map<String,Object> map = new HashMap<>();
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            int id = sess.attribute("id");
            Connection conn = MySqlConnection.getConnection();
            Map<Integer, Object> courses = Courses.getMyCourses(id, role, conn);
            map.put("filterStatus", "All");
            ArrayList<String> filterOptions = new ArrayList<>();
            filterOptions.add("Current");
            filterOptions.add("Upcoming");
            filterOptions.add("Completed");
            map.put("filterOptions",filterOptions);
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
            Map<Integer, Object> courses = Courses.getMyCourses(id, role, conn);
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
            conn.close();
            return new ModelAndView(map,"courses.ftl");
        }),engine);

        get("/courses/create-course",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            if (!role.contentEquals("admin")) response.redirect("/err");
            Map<String,Object> map = new HashMap<>();
            map.put("name","");
            map.put("obj","");
            map.put("role", "admin");
            Connection conn = MySqlConnection.getConnection();
            map.put("profList", DataMapper.findAllProfs(conn));
            map.put("e",-1);
            Map<Integer, String> allCourses = CreateCourse.allCourses(conn);
            map.put("title","Create Course");
            String e_id = request.queryParams("e");
            if (e_id != null) {
                map = CreateCourse.editCourse(map,e_id.replaceAll(" ",""), allCourses, conn);
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
            Map<Integer, String> allCourses = CreateCourse.allCourses(conn);
            Map<String,Object> map = CreateCourse.postMethodDefaults(formFields, edit, conn);
            if((boolean)map.get("created") == true) {
                conn.close();
                response.redirect("/courses/all");
            }
            if (map.containsKey("prereq_course")) {
                allCourses.remove(Integer.parseInt(edit));
                map.put("currPreq", allCourses.get(map.get("prereq_course")));
                allCourses.remove(map.get("prereq_course"));
                map.put("course_id", map.get("prereq_course"));
            }
            map.put("prereqs", allCourses);
            map.forEach((k,v)->map.put(k,v));
            map.put("role", "admin");
            conn.close();
            return new ModelAndView(map,"createCourse.ftl");
        },engine);



        get("/enroll",((request, response) -> {
            Session sess = request.session();
            String role = sess.attribute("role").toString();
            Map<String,Object> map = new HashMap<>();
            Connection conn = MySqlConnection.getConnection();
            ArrayList<Map<String,Object>> courses = Enrollment.findAllAvailableCourses(conn);
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
            Map<String,String> map = new HashMap<>();
            map.put("role", role);

            //display course specific info
            map.put("courseNumber", request.params(":number"));
            Connection conn = MySqlConnection.getConnection();
            Map<String, Object> tempCourse = DataMapper.findCourseByCourseId(request.params(":number"), conn);
            for(Map.Entry<String, Object> entry: tempCourse.entrySet()){
                map.put(entry.getKey(), entry.getValue().toString());
            }
            map.put("profName",DataMapper.findProfName(Integer.parseInt(map.get("prof_id")), conn));
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
            ArrayList<Map<String,Object>> groups = DataMapper.getMyDiscussionGroups(id, conn);
            Map<Integer,Map<String, Object>> allGroups = DiscussionGroups.getGroups("", -1, id, conn);
            Map<Integer, Object> requestedGroups = DataMapper.getPendingGroupRequests(id, conn);
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
            Map<String, Object> group = DataMapper.getGroupDetailsByGroupId(id, conn);
            String member = DiscussionGroups.isMemberOfGroup(user_id, id, conn);
            Map<Integer, String> members = DataMapper.viewAllGroupMembers(id, conn);
            Map<Integer, String> requests = DataMapper.getAllPendingGroupRequestsOfGroup(id, conn);
            int prof = DiscussionGroups.findProfofCourse(group, conn);
            Map<String,Object> map = new HashMap<>();
            if (prof != 0) map.put("prof", prof);
            map.put("status", member);
            map.put("group", group);
            map.put("role", role);
            map.put("members", members);
            if (!requests.isEmpty()) map.put("reqs", requests);
            map.put("id", id);
            map.put("posts",DataMapper.getDGPostsById(Integer.parseInt(URLDecoder.decode(request.params(":id"),"UTF-8")),conn));
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
                boolean flag = DataMapper.addDGmember(u_id, id, conn);
                boolean flag2 = false;
                if (flag) flag2 = DataMapper.deleteRequestForGroup(u_id, id, conn);
                if (flag2) response.redirect("/discussion/group-desc/"+id);
            } else if (formFields.containsKey("del")) {
                int u_id = Integer.parseInt(formFields.get("del"));
                boolean flag = DataMapper.deleteRequestForGroup(u_id, id, conn);
                if (flag) response.redirect("/discussion/group-desc/"+id);
            } else if (formFields.containsKey("leave")) {
                boolean flag = DataMapper.deleteDGMember(user_id, id, conn);
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
            DataMapper.createDGPost(Integer.parseInt(
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
            Map<Integer,String> profs = DataMapper.viewAllRequests(conn);
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
            Map<String, Object> map = ApplyForProfessor.postMethodFunctionality(formFields,
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
                conn.close();
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

        post("/upload/:courseId/:lessonId", (request, response) -> {

            // tempFile = Files.createFile(uploadDir.toPath(), "test", ".pdf");

            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = request.raw().getPart("uploadFile").getInputStream()) { // getPart needs to use same "name" as input field in form
                Connection conn = MySqlConnection.getConnection();
                File newFile = new File(uploadDir.toPath().toString(),request.raw().getPart("uploadFile").getSubmittedFileName());
                Files.copy(input, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                DataMapper.createLearningMaterial(Integer.parseInt(request.params(":lessonId")),
                        request.raw().getPart("uploadFile").getSubmittedFileName(),
                        conn
                );
                conn.close();
            }
            System.out.println();
            response.redirect("/course/learnMat/"+request.params(":courseId"));
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