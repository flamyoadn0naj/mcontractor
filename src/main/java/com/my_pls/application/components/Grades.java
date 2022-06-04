package com.my_pls.application.components;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Grades {
    private int courseId = -1;
    private int userId = -1;
    private ArrayList<Lesson> lessons = new ArrayList<>();
    private Map<Integer, Object> quizzes = new HashMap<>();
    private Map<String, Object> grades = new HashMap<String, Object>();

    public Grades(int courseId, int userId, Connection conn) throws Exception {
        courseId = courseId;
        userId = userId;
        lessons = Proxy.getLessonsByCourseId(courseId,conn);
        for (Lesson l: lessons) {
            int id = l.getId();
            quizzes = Proxy.viewQuizzes(id,conn);
            for (Integer quizId : quizzes.keySet()) {
                String quizName = ((Map<String, Object>) quizzes.get(quizId)).get("name").toString();
                String quizMarks = ((Map<String, Object>) quizzes.get(quizId)).get("marks").toString();
                String grade = Proxy.getGradesOfUser(userId, courseId, id, quizId, conn);
                Map<String, String> g = new HashMap<>();
                g.put("marks", quizMarks);
                g.put("score", grade);
                grades.put(quizName, g);
            }
        }
    }
    public Map<String, Object> getGrades() {
        return grades;
    }
}

