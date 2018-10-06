package edu.uchealth.healthhack.nowaithospital;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mayank on 06/10/18.
 */

public class QuestionBank {
    Question rootQuestion;

    public QuestionBank() {
        List<String> ansRoot = Arrays.asList("Good", "Normal", "Bad");
        rootQuestion = new MCQQuestion("How are you feeling?", ansRoot);

        List<String> keyQ0 = Arrays.asList("cut", "shot");
        Question q0 = new TextQuestion("What is wrong?", keyQ0);

        List<String> ansQ1 = Arrays.asList("Normal", "Heavy");
        Question q1 = new MCQQuestion("How is your breathing?", ansQ1);

        List<String> ansQ2 = Arrays.asList("Normal", "High");
        Question q2 = new MCQQuestion("How is your heart rate?", ansQ2);

        List<String> ansYesNo = Arrays.asList("Yes", "No");
        Question q3 = new MCQQuestion("How Q3?", ansYesNo);
        Question q4 = new MCQQuestion("How Q4?", ansYesNo);
        Question q5 = new MCQQuestion("How Q5?", ansYesNo);
        Question q6 = new MCQQuestion("How Q6?", ansYesNo);

        rootQuestion.addChildQuestion(1, q0);
        rootQuestion.addChildQuestion(2, q0);

        q0.addChildQuestion(0, q1);
        q0.addChildQuestion(1, q2);

        q1.addChildQuestion(0, q4);
        q1.addChildQuestion(1, q3);
        q1.addChildQuestion(1, q4);

        q3.addChildQuestion(0, q5);
        q3.addChildQuestion(1, q6);

        q2.addChildQuestion(0, q4);
        q2.addChildQuestion(1, q6);
    }

    Question getRoot() {
        return rootQuestion;
    }
}
