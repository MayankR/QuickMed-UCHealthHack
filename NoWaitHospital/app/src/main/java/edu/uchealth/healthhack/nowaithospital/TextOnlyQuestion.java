package edu.uchealth.healthhack.nowaithospital;

import java.util.ArrayList;
import java.util.List;

import static edu.uchealth.healthhack.nowaithospital.PatientData.pData;

/**
 * Created by Akshansh on 06/10/18.
 */

public class TextOnlyQuestion extends Question {

    public TextOnlyQuestion(String q, List<String> k) {
        this.questionStr = q;
        nextQuestions = new ArrayList<>();

        for(int i=0;i<k.size();i++) {
            List<Question> e = new ArrayList<>();
            nextQuestions.add(e);
        }
    }

    List<Integer> getKeyWordsFromResponse(String response) {
        List<Integer> keywordsIndex = new ArrayList<>();
        keywordsIndex.add(0);
        return keywordsIndex;
    }

    public List<Question> getNextQuestions(String response) {

        pData.put(questionStr, response);
        List<Integer> keywordsIndexes = getKeyWordsFromResponse(response);
        List<Question> allNext = new ArrayList<>();
        for(int i=0;i<keywordsIndexes.size();i++) {
            allNext.addAll(nextQuestions.get(keywordsIndexes.get(i)));
        }
        return allNext;
    }
}
