package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    QuestionBank qb;
    Question curQuestion;
    List<Question> allQuestions;
    int curQuestionIndex;
    FloatingActionButton nine11FAB, nextFAB, speechFAB;
    TextView questionTV, patientNameTV;
    RelativeLayout questionRL;
    LinearLayout answerLL;
    EditText answerET;
    float x1, x2;
    final int SWIPE_THRESHOLD = 300;
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_questions);

        qb = new QuestionBank();
        curQuestion = qb.getRoot();

        nine11FAB = (FloatingActionButton) findViewById(R.id.nine11_fab);
        nine11FAB.setOnClickListener(this);
        nextFAB = (FloatingActionButton) findViewById(R.id.next_fab);
        nextFAB.setOnClickListener(this);
        speechFAB = (FloatingActionButton) findViewById(R.id.speech_fab);
        speechFAB.setOnClickListener(this);

        questionTV = (TextView) findViewById(R.id.question_tv);
        questionTV.setOnClickListener(this);
        patientNameTV = (TextView) findViewById(R.id.user_name_tv);
        patientNameTV.setText(PatientData.name);

        answerLL = (LinearLayout) findViewById(R.id.answer_ll);

        allQuestions = new ArrayList<>();
        allQuestions.add(curQuestion);
        curQuestionIndex = 0;

        renderScreen();
    }

    void renderScreen() {
        questionTV.setText(curQuestion.getQuestion());
        Log.d("QA", curQuestion.getClass().getSimpleName());

        if(curQuestion.getClass().getSimpleName().equals("MCQQuestion")) {
            List<String> answersList = ((MCQQuestion)curQuestion).getAnswers();

            Resources r = getResources();
            int px35 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    35,
                    r.getDisplayMetrics()
            );
            int px10 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10,
                    r.getDisplayMetrics()
            );

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(px35, px10, px35, 0);

            answerLL.removeAllViews();
            for(int i=0;i<answersList.size();i++) {
                final int optionID = i;
                TextView child = (TextView) getLayoutInflater().inflate(R.layout.answer_option, null);
                child.setText(answersList.get(i));
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedOption(optionID);
                    }
                });
                answerLL.addView(child, layoutParams);
            }

            answerET = null;
            nextFAB.setVisibility(View.GONE);
        } else {
            View child = getLayoutInflater().inflate(R.layout.answer_et, null);
            Resources r = getResources();
            int px35 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    35,
                    r.getDisplayMetrics()
            );
            int px20 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    20,
                    r.getDisplayMetrics()
            );
//            int px25 = (int) TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    35,
//                    r.getDisplayMetrics()
//            );

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(px35, px20, px35, 0);
            answerLL.removeAllViews();
            answerLL.addView(child, layoutParams);
            answerET = (EditText)child;
            nextFAB.setVisibility(View.VISIBLE);
        }
        speakOutQuestion();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (deltaX > SWIPE_THRESHOLD) {
                    goToPrevQuestion();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if(curQuestionIndex == 0) {
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
        }
        goToPrevQuestion();
    }

    void goToPrevQuestion() {
        if(curQuestionIndex == 0) {
            return;
        }
        curQuestionIndex--;
        curQuestion = allQuestions.get(curQuestionIndex);
        for(int i=allQuestions.size()-1;i>curQuestionIndex;i--) {
            allQuestions.remove(i);
        }
        renderScreen();
    }

    void goToNextQuestion() {
        curQuestionIndex++;
        Log.d("QA", curQuestionIndex + " " + allQuestions.size());
        if(curQuestionIndex == allQuestions.size()) {
            Intent it = new Intent(this, DoneQuestionsActivity.class);
            startActivity(it);
            finish();
            return;
        }
        curQuestion = allQuestions.get(curQuestionIndex);
        renderScreen();
    }

    void clickedOption(int i) {
        Log.d("QA", "Clicked option " + i);
        if(curQuestion.getClass().getSimpleName().equals("MCQQuestion")) {
            allQuestions.addAll(((MCQQuestion)curQuestion).getNextQuestions(i));
            goToNextQuestion();
        }
    }

    // TODO
    void speakOutQuestion() {

    }

    void registerResponse() {
        allQuestions.addAll(curQuestion.getNextQuestions(answerET.getText().toString().trim()));
        goToNextQuestion();
    }

    void registerResponse(String s) {
        if(curQuestion.getClass().getSimpleName().equals("TextQuestion")) {
            answerET.setText(s);
        }
        allQuestions.addAll(curQuestion.getNextQuestions(s));
        goToNextQuestion();
    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent
                (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, curQuestion.getQuestion());
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                String mAnswer = results.get(0);

                Toast.makeText(this, mAnswer, Toast.LENGTH_SHORT).show();
                registerResponse(mAnswer);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.nine11_fab:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:811"));
                startActivity(callIntent);
                break;
            case R.id.question_tv:
                speakOutQuestion();
                break;
            case R.id.next_fab:
                registerResponse();
                break;
            case R.id.speech_fab:
                startSpeechRecognizer();
                break;
        }
    }
}
