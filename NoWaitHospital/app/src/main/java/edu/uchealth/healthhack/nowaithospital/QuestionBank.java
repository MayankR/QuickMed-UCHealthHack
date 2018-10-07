package edu.uchealth.healthhack.nowaithospital;

import java.util.Arrays;
import java.util.List;
import java.util.*;

import static edu.uchealth.healthhack.nowaithospital.keyWordsDetection.isBodyPart;
import static edu.uchealth.healthhack.nowaithospital.keyWordsDetection.isInjury;
import static edu.uchealth.healthhack.nowaithospital.keyWordsDetection.isNonInjury;

/**
 * Created by mayank on 06/10/18.
 */

public class QuestionBank {
    Question rootQuestion;

    public QuestionBank() {

        // Main Question
        List<String> keyInjury = Arrays.asList("cut", "shot", "blood", "bleeding", "wound", "laceration", "lacerated", "dislocation", "dislocated", "amputated", "injury");
        List<String> keyNonInjury = Arrays.asList("sick", "infection", "discomfort", "breathing", "dizzy", "nausea", "clammy", "fever");
        List<String> bodyParts = Arrays.asList("head", "face", "eye", "nose", "ear", "temple", "neck", "chest", "back", "arm", "hand",
                "finger", "thumb", "wrist", "elbow", "bone", "hip", "knee", "leg", "toe", "feet", "foot");
        List<String> keyPregnant = Arrays.asList("pregnant", "water broke");

//        rootQuestion = new TextQuestion("How can I help you?", keyInjury, keyNonInjury, bodyParts, keyPregnant);

        // -----------------------------------------------------------------------------------------------------------------
        // Check if it breaks for the case when these key words are part of some bigger words, which don't mean anything
        // earphones, headphones, facet, ... etc
        // So, is there a need to Find the words in a standalone fashion i.e. words should be separated by spaces
        // what about internal bleeding?

        // Yes, No questions
        List<String> ansYesNo = Arrays.asList("Yes", "No");

        // ------------------------------------------------------------
        // Main Category Question
        // ------------------------------------------------------------
        List<String> ansCategory = Arrays.asList("Injury", "Non Injury");
        Question category = new MCQQuestion("Which type of illness do you have?", ansCategory);







        // ------------------------------------------------------------
        // Injury Questions
        // ------------------------------------------------------------

        // --------------------------
        // Category Question
        // --------------------------
        List<String> ansICategory = Arrays.asList("Laceration", "Cut", "Dislocation", "Amputation (Cutt off)", "General External Pain", "Other");
        Question iCategory = new MCQQuestion("Please choose a category?", ansICategory); // can remove amputation

        // --------------------------
        // Body Part Question
        // --------------------------
        List<String> textOnlyAns = Arrays.asList("tp");
        Question bodypart = new TextOnlyQuestion("Which body part did you injure?", textOnlyAns);

        // --------------------------
        // Photograph
        // --------------------------
        List<String> picCategory = Arrays.asList("Click picture", "Upload from device", "Click more pictures...");
        Question photograph = new MCQQuestion("Please share a pic of your injury?", picCategory);







        // ------------------------------------------------------------
        // Non Injury Questions
        // ------------------------------------------------------------


        // --------------------------
        // Category Question
        // --------------------------
        List<String> ansNonICategory = Arrays.asList("Pregnant", "Breathing", "Heart", "Brain", "Temperature", "Other");
        Question nonICategory = new MCQQuestion("Please choose a category?", ansNonICategory); // pregnancy is not an illness


        // --------------------------
        // Pregnancy Questions
        // --------------------------
        Question pregDuration = new TextOnlyQuestion("How long have you been pregnant for?", textOnlyAns);

        Question pregPain = new MCQQuestion("Are you in pain?", ansYesNo);

        Question discomfort = new MCQQuestion("Are you in discomfort", ansYesNo);

        List<String> ansPregPain = Arrays.asList("Abdominal", "Other");
        Question pregPainType = new MCQQuestion("Which type of pain?", ansPregPain);





        List<String> ansBreathing = Arrays.asList("Normal", "Heavy", "Slow", "Fast", "Very Fast", "I don't know");
        Question breathing = new MCQQuestion("How is your breathing?", ansBreathing);

        List<String> ansPulse = Arrays.asList("Normal", "Slow", "Fast", "I don't know");
        Question pulse = new MCQQuestion("How is your pulse?", ansPulse);

        List<String> ansHeartRate = Arrays.asList("Normal", "High", "Slow", "I don't know");
        Question hearRate = new MCQQuestion("How is your heart rate?", ansHeartRate);

        // How to incorporate all the answers like Fast as well as High for above questions?
        // Maybe if no keyword matches match then just enter whatever the user said as it as ...



        Question pills = new MCQQuestion("Do you take pills or medication?", ansYesNo);
        Question diabetic = new MCQQuestion("Are you diabetic?", ansYesNo);
        Question drink = new MCQQuestion("Do you drink?", ansYesNo);
        Question drugs = new MCQQuestion("Do you take drugs?", ansYesNo);

        Question allergies = new MCQQuestion("Do you have any allergies?", ansYesNo);
        Question asthma = new MCQQuestion("Do you have asthma/bronchitis?", ansYesNo);
        Question smoke = new MCQQuestion("Do you smoke", ansYesNo);
        Question highBP = new MCQQuestion("Do you have a high blood presssure?", ansYesNo); // "Don't know" should also be an option
        Question heartAttack = new MCQQuestion("Have you had a heart attack before?", ansYesNo);
        Question stroke = new MCQQuestion("Have you had a stroke before?", ansYesNo);
        Question fall = new MCQQuestion("Have you ever suffered a fall before?", ansYesNo);
        Question pale = new MCQQuestion("Do you feel pale?", ansYesNo);
        Question sweaty = new MCQQuestion("Do you feel sweaty", ansYesNo);
        Question vomit = new MCQQuestion("Did you vomit?", ansYesNo);
        Question passout = new MCQQuestion("Did you pass out?", ansYesNo); // "How long ago?"
        Question fever = new MCQQuestion("Do you have fever", ansYesNo); // Don't know ....should be included
        Question stomachPain = new MCQQuestion(" Do you have stomach pain?", ansYesNo);

        Question howlongago = new TextOnlyQuestion("How long ago?", textOnlyAns);



        // Constructing the Map



        // ---------------------------------------------------------------------
        // Ready to construct the tree
        // ---------------------------------------------------------------------

        rootQuestion = new TextQuestion("How can I help you?", keyInjury, keyNonInjury, bodyParts, keyPregnant, bodypart, nonICategory, category, photograph);

//        if(isInjury && !isNonInjury){
//            if(!isBodyPart){
//                rootQuestion.addChildQuestion(0, bodypart);
//            }
//        }
//        else if (!isInjury && isNonInjury){
//            if(!isBodyPart){
//                rootQuestion.addChildQuestion(0, nonICategory);
//            }
//        }
//        else{
//            isInjury = Boolean.FALSE;
//            isNonInjury = Boolean.FALSE;
//
//            rootQuestion.addChildQuestion(0, category);
//
//        }
        rootQuestion.addChildQuestion(0, category);

        category.addChildQuestion(0, iCategory);
        category.addChildQuestion(1, nonICategory);

        iCategory.addChildQuestion(0, bodypart);
        iCategory.addChildQuestion(1, bodypart);
        iCategory.addChildQuestion(2, bodypart);
        iCategory.addChildQuestion(3, bodypart);
        iCategory.addChildQuestion(4, bodypart);
        iCategory.addChildQuestion(5, bodypart);

        bodypart.addChildQuestion(0, photograph);


        nonICategory.addChildQuestion(0, pregDuration);
            pregDuration.addChildQuestion(0, pregPain);
                pregPain.addChildQuestion(0, pregPainType);
                    pregPainType.addChildQuestion(1, nonICategory); // For 0, its baby coming ...
                pregPain.addChildQuestion(1, discomfort);
                    discomfort.addChildQuestion(0,nonICategory);


        nonICategory.addChildQuestion(1, breathing);
            breathing.addChildQuestion(0, allergies);
            breathing.addChildQuestion(1, allergies);
                allergies.addChildQuestion(0, asthma);
                allergies.addChildQuestion(1, asthma);
                    asthma.addChildQuestion(0, smoke);
                    asthma.addChildQuestion(1, smoke);
                        smoke.addChildQuestion(0, pulse);
                        smoke.addChildQuestion(1, pulse);
                            pulse.addChildQuestion(0, pills);
                            pulse.addChildQuestion(1, pills);
                            pulse.addChildQuestion(2, pills);
                            pulse.addChildQuestion(3, pills);
                                pills.addChildQuestion(0, diabetic);
                                pills.addChildQuestion(1, diabetic);
                                    diabetic.addChildQuestion(0, drink);
                                    diabetic.addChildQuestion(1, drink);
                                        drink.addChildQuestion(0, drugs);
                                        drink.addChildQuestion(1, drugs);
                                            drugs.addChildQuestion(0, passout);
                                            drugs.addChildQuestion(1, passout);



        nonICategory.addChildQuestion(2, heartAttack);
            heartAttack.addChildQuestion(0, highBP);
            heartAttack.addChildQuestion(1, highBP);
                highBP.addChildQuestion(0, pulse);
                highBP.addChildQuestion(1, pulse);
//                    pulse.addChildQuestion(0, pills);
//                    pulse.addChildQuestion(1, pills);
//                    pulse.addChildQuestion(2, pills);
//                    pulse.addChildQuestion(3, pills);
//                        pills.addChildQuestion(0, diabetic);
//                        pills.addChildQuestion(1, diabetic);
//                            diabetic.addChildQuestion(0, drink);
//                            diabetic.addChildQuestion(1, drink);
//                                drink.addChildQuestion(0, drugs);
//                                drink.addChildQuestion(1, drugs);
//                                    drugs.addChildQuestion(0, passout);
//                                    drugs.addChildQuestion(1, passout);



        nonICategory.addChildQuestion(3, stroke);
            stroke.addChildQuestion(0, fall);
            stroke.addChildQuestion(1, fall);
                fall.addChildQuestion(0, howlongago);
                    howlongago.addChildQuestion(0, pulse);
                fall.addChildQuestion(1, pulse);
//                    pulse.addChildQuestion(0, pills);
//                    pulse.addChildQuestion(1, pills);
//                    pulse.addChildQuestion(2, pills);
//                    pulse.addChildQuestion(3, pills);
//                        pills.addChildQuestion(0, diabetic);
//                        pills.addChildQuestion(1, diabetic);
//                            diabetic.addChildQuestion(0, drink);
//                            diabetic.addChildQuestion(1, drink);
//                                drink.addChildQuestion(0, drugs);
//                                drink.addChildQuestion(1, drugs);
//                                    drugs.addChildQuestion(0, passout);
//                                    drugs.addChildQuestion(1, passout);



        nonICategory.addChildQuestion(4, fever);
            fever.addChildQuestion(0, sweaty);
            fever.addChildQuestion(1, sweaty);
                sweaty.addChildQuestion(0, pale);
                sweaty.addChildQuestion(1, pale);
                    pale.addChildQuestion(0, pulse);
                    pale.addChildQuestion(1, pulse);
//                        pulse.addChildQuestion(0, pills);
//                        pulse.addChildQuestion(1, pills);
//                        pulse.addChildQuestion(2, pills);
//                        pulse.addChildQuestion(3, pills);
//                            pills.addChildQuestion(0, diabetic);
//                            pills.addChildQuestion(1, diabetic);
//                                diabetic.addChildQuestion(0, drink);
//                                diabetic.addChildQuestion(1, drink);
//                                    drink.addChildQuestion(0, drugs);
//                                    drink.addChildQuestion(1, drugs);
//                                        drugs.addChildQuestion(0, passout);
//                                        drugs.addChildQuestion(1, passout);




        nonICategory.addChildQuestion(5, vomit);
            vomit.addChildQuestion(0, stomachPain);
            vomit.addChildQuestion(1, stomachPain);
                stomachPain.addChildQuestion(0, pills);
                stomachPain.addChildQuestion(1, pills);
//                    pills.addChildQuestion(0, diabetic);
//                    pills.addChildQuestion(1, diabetic);
//                        diabetic.addChildQuestion(0, drink);
//                        diabetic.addChildQuestion(1, drink);
//                            drink.addChildQuestion(0, drugs);
//                            drink.addChildQuestion(1, drugs);
//                                drugs.addChildQuestion(0, passout);
//                                drugs.addChildQuestion(1, passout);






        // We should make the process simpler for the user, so maybe we can remove some of the questions

//        rootQuestion.addChildQuestion(1, q1);
//        rootQuestion.addChildQuestion(2, q1);
//
//        q1.addChildQuestion(0, q4);
//        q1.addChildQuestion(1, q3);
//        q1.addChildQuestion(1, q4);
//
//        q3.addChildQuestion(0, q5);
//        q3.addChildQuestion(1, q6);
//
//        q2.addChildQuestion(0, q4);
//        q2.addChildQuestion(1, q6);





        ////// IMP
        // In the end ask do you want to send any picture of you in the current sitation, any short video of urself
        // Or if u want to add just any other extra information about your condition right now or your past history .....
        // Any information about your Insurance because we may want to check if some hospital accepts your insurance or not ....
        // we will mention that info in the results of the suggested hospitals ....

        // Include Diabetic, D/D questions on one page itself ...
    }

    Question getRoot() {
        return rootQuestion;
    }
}
