package edu.uchealth.healthhack.nowaithospital;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

import static edu.uchealth.healthhack.nowaithospital.PatientData.pData;

/**
 * Created by mayank on 06/10/18.
 */

public class MCQQuestion extends Question {
    List<String> answers;
    Map<String, String> qmap;

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
        String[] parts;
        if(response.contains(" ")) {
            parts = response.split(Pattern.quote(" "));
        } else {
            parts = new String[] {response};
        }
        int minDist = 1000000, minInd=0;
        for(int i=0;i<answers.size();i++) {
            int yoDist = Utility.minDistance(response, answers.get(i));
            if(yoDist < minDist) {
                minDist = yoDist;
                minInd = i;
            }
        }
        return minInd;
    }



    public List<Question> getNextQuestions(String response) {

        pData.put(questionStr, response);
        int keywordsIndex = getOptionFromResponse(response);
        return getNextQuestions(keywordsIndex);
    }
}
