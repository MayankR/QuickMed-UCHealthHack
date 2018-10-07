package edu.uchealth.healthhack.nowaithospital;

import java.util.ArrayList;
import java.util.List;

import static edu.uchealth.healthhack.nowaithospital.PatientData.pData;
import static edu.uchealth.healthhack.nowaithospital.keyWordsDetection.isBodyPart;
import static edu.uchealth.healthhack.nowaithospital.keyWordsDetection.isInjury;
import static edu.uchealth.healthhack.nowaithospital.keyWordsDetection.isNonInjury;

/**
 * Created by mayank on 06/10/18.
 */

public class TextQuestion extends Question {
    List<String> keywordsInjury;
    List<String> keywordsNonInjury;
    List<String> bodyParts;
    List<String> keyPregnant;
    Question body, nonI, cat, photo;

    public TextQuestion(String q, List<String> k, List<String> n, List<String> b, List<String> p, Question body, Question nonI, Question cat, Question photo) {
        this.questionStr = q;
        this.keywordsInjury = k;
        this.keywordsNonInjury = n;
        this.bodyParts = b;
        this.keyPregnant = p;
        this.body = body;
        this.nonI = nonI;
        this.cat = cat;
        this.photo = photo;
        nextQuestions = new ArrayList<>();

        for(int i=0;i<k.size();i++) {
            List<Question> e = new ArrayList<>();
            nextQuestions.add(e);
        }


//        this.keywordsNonInjury.add(this.keyPregnant.get(0));
//        this.keywordsNonInjury.add(this.keyPregnant.get(1));
    }

    List<Integer> getKeyWordsFromResponse(String response) {
        List<Integer> keywordsIndex = new ArrayList<>();
        keywordsIndex.add(0);
        return keywordsIndex;
    }

    public List<Question> getNextQuestions(String response) {

        PatientData.mainProblem = response;

        //--------------------------------------------------------------------------------
        for (int i = 0; i < keywordsInjury.size(); i++) {
            if(response.indexOf(keywordsInjury.get(i)) != -1){
                isInjury = Boolean.TRUE;
                break;
            }
        }

        for (int i = 0; i < keywordsNonInjury.size(); i++) {
            if(response.indexOf(keywordsNonInjury.get(i)) != -1){
                isNonInjury = Boolean.TRUE;
                break;
            }
        }

        for (int i = 0; i < bodyParts.size(); i++) {
            if(response.indexOf(bodyParts.get(i)) != -1){
                isBodyPart = Boolean.TRUE;
                keyWordsDetection.bodyParts.add(bodyParts.get(i));
            }
        }

        nextQuestions = new ArrayList<>();

        for(int i=0;i<keywordsInjury.size();i++) {
            List<Question> e = new ArrayList<>();
            nextQuestions.add(e);
        }

        if(isInjury && !isNonInjury){
            if(!isBodyPart){
                nextQuestions.get(0).add(body);
//                rootQuestion.addChildQuestion(0, bodypart);
            }
            else{
                nextQuestions.get(0).add(photo);
            }
        }
        else if (!isInjury && isNonInjury){
            if(!isBodyPart){
                nextQuestions.get(0).add(nonI);
//                rootQuestion.addChildQuestion(0, nonICategory);
            }
            else{
                nextQuestions.get(0).add(photo);
            }
        }
        else{
            isInjury = Boolean.FALSE;
            isNonInjury = Boolean.FALSE;

            nextQuestions.get(0).add(cat);
//            rootQuestion.addChildQuestion(0, category);

        }


        //--------------------------------------------------------------------------------

        List<Integer> keywordsIndexes = getKeyWordsFromResponse(response);
        List<Question> allNext = new ArrayList<>();

        for(int i=0;i<keywordsIndexes.size();i++) {
            allNext.addAll(nextQuestions.get(keywordsIndexes.get(i)));
        }
        return allNext;
    }



    // ----------------------------------------------------------------------------------
    // Saving the previous simple version

//    List<Integer> getKeyWordsFromResponse(String response) {
//        List<Integer> keywordsIndex = new ArrayList<>();
//        keywordsIndex.add(0);
//        return keywordsIndex;
//    }
//
//    public List<Question> getNextQuestions(String response) {
//
//        PatientData.mainProblem = response;
//        List<Integer> keywordsIndexes = getKeyWordsFromResponse(response);
//        List<Question> allNext = new ArrayList<>();
//
//
//        for(int i=0;i<keywordsIndexes.size();i++) {
//            allNext.addAll(nextQuestions.get(keywordsIndexes.get(i)));
//        }
//        return allNext;
//    }



}
