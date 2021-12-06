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

    public static Map<Integer, Object> getQuizzes(int lessonId) {
        Map<Integer,Object> quizzes = new HashMap<>();
        Map<Integer,Object> MyQuizzes = DataMapper.viewQuizzes(lessonId);

        quizzes.putAll(MyQuizzes);

        return quizzes;
    }

}
