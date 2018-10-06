package edu.uchealth.healthhack.nowaithospital;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayank on 06/10/18.
 */

public class MCQQuestion extends Question {
    List<String> answers;

    public MCQQuestion(String q, List<String> ans) {
        this.questionStr = q;
        this.answers = ans;
        nextQuestions = new ArrayList<>();

        for(int i=0;i<ans.size();i++) {
            List<Question> e = new ArrayList<>();
            nextQuestions.add(e);
        }
    }

    public List<Question> getNextQuestions(int ansNum) {
        return nextQuestions.get(ansNum);
    }
}
