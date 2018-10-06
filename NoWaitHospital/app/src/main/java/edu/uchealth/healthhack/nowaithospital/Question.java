package edu.uchealth.healthhack.nowaithospital;

import java.util.List;

/**
 * Created by mayank on 06/10/18.
 */

public class Question {
    String questionStr;
    List<List<Question>> nextQuestions;

    public Question() {
        questionStr = "Undefined Question!!! :(";
    }

    public Question(String q) {
        this.questionStr = q;
    }

    public String getQuestion() {
        return questionStr;
    }

    public void addChildQuestion(int ansNum, Question q) {
        nextQuestions.get(ansNum).add(q);
    }
}
