package edu.uchealth.healthhack.nowaithospital;

import android.util.Log;

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

    public List<String> getAnswers() {
        return answers;
    }

    public List<Question> getNextQuestions(int ansNum) {
        List<Question> nextStuff = nextQuestions.get(ansNum);
        Log.d("MCQQuestions", nextStuff.size() + " more questions");
        return nextStuff;
    }

    // TODO
    int getOptionFromResponse(String response) {
        return 0;
    }

    public List<Question> getNextQuestions(String response) {
        int keywordsIndex = getOptionFromResponse(response);
        return getNextQuestions(keywordsIndex);
    }
}
