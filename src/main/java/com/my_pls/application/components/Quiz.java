package com.my_pls.application.components;

import com.my_pls.MySqlConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quiz {
    public int quizId;
    public int lessonId;
    public String quizName;
    public int questionId;
    public int mark;
    public int MinMark;
    public String questionText;
    public String responseA;
    public String responseB;
    public String responseC;
    public String responseD;
    public String answer;

    public static Map<Integer, Object> getMyQuizzes(int lessonId) {
        Quiz MyQuizzes = new Quiz();
        MyQuizzes.lessonId = lessonId;
        ArrayList<Quiz> Quizzes_obj = DataMapper.getQuestions(MyQuizzes);

        Map<Integer,Object> quizzes = new HashMap<>();
        Map<Integer,Object> my_courses = DataMapper.getMyCourses(id);
        courses.putAll(my_courses);
        if (role.contentEquals("prof")) {
            Map<Integer,Object> taught_courses = DataMapper.getTaughtCourses(id);
            courses.putAll(taught_courses);
        }
        return courses;
    }

}
