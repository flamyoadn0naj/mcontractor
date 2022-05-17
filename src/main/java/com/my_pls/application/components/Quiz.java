package com.my_pls.application.components;

import java.sql.Connection;
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
    public int totalMark;
    public String questionText;
    public String responseA;
    public String responseB;
    public String responseC;
    public String responseD;
    public String answer;

    public static Map<Integer, Object> getQuizzes(int courseId, Connection conn) {
        Map<Integer,Object> quizzes = new HashMap<>();

        ArrayList<Lesson> lessons = Proxy.getLessonsByCourseId(courseId, conn);
        for (Lesson lesson:lessons){
            Map<Integer,Object> MyQuizzes = Proxy.viewQuizzes(lesson.getId(), conn);
            quizzes.putAll(MyQuizzes);
        }
        return quizzes;
    }


    public static Map<Integer, Object> getQuizzesbyId(int quizId, Connection conn) {
        Map<Integer,Object> quizzes = new HashMap<>();
        Map<Integer,Object> MyQuizzes = Proxy.viewQuiz(quizId, conn);
        Object currentQuiz = MyQuizzes.get(quizId);
        return quizzes;
    }

}
