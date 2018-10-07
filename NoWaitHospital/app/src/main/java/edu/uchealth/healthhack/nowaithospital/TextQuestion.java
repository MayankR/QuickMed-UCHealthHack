package edu.uchealth.healthhack.nowaithospital;

import java.util.ArrayList;
import java.util.List;

import static edu.uchealth.healthhack.nowaithospital.PatientData.pData;

/**
 * Created by mayank on 06/10/18.
 */

public class TextQuestion extends Question {
    List<String> keywordsInjury;
    List<String> keywordsNonInjury;
    List<String> bodyParts;
    List<String> keyPregnant;

    public TextQuestion(String q, List<String> k, List<String> n, List<String> b, List<String> p) {
        this.questionStr = q;
        this.keywordsInjury = k;
        this.keywordsNonInjury = n;
        this.bodyParts = b;
        this.keyPregnant = p;
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

//        pData.put(questionStr, response);

        PatientData.mainProblem = response;
        List<Integer> keywordsIndexes = getKeyWordsFromResponse(response);
        List<Question> allNext = new ArrayList<>();


        for(int i=0;i<keywordsIndexes.size();i++) {
            allNext.addAll(nextQuestions.get(keywordsIndexes.get(i)));
        }
        return allNext;
    }
}
