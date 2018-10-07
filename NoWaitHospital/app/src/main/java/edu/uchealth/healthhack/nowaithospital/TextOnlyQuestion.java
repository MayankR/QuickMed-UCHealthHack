package edu.uchealth.healthhack.nowaithospital;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshansh on 06/10/18.
 */

public class TextOnlyQuestion extends Question {

    public TextOnlyQuestion(String q) {
        this.questionStr = q;

        nextQuestions = new ArrayList<>();
    }

    List<Integer> getKeyWordsFromResponse(String response) {
        List<Integer> keywordsIndex = new ArrayList<>();
        keywordsIndex.add(0);
        return keywordsIndex;
    }

    public List<Question> getNextQuestions(String response) {
        List<Integer> keywordsIndexes = getKeyWordsFromResponse(response);
        List<Question> allNext = new ArrayList<>();
        for(int i=0;i<keywordsIndexes.size();i++) {
            allNext.addAll(nextQuestions.get(keywordsIndexes.get(i)));
        }
        return allNext;
    }
}
