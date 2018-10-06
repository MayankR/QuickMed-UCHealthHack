package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    QuestionBank qb;
    Question curQuestion;
    FloatingActionButton nine11FAB;
    TextView questionTV;
    RelativeLayout questionRL;
    LinearLayout answerLL;


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

        questionTV = (TextView) findViewById(R.id.question_tv);
        questionTV.setOnClickListener(this);

        answerLL = (LinearLayout) findViewById(R.id.answer_ll);

        renderScreen();
    }

    void renderScreen() {
        questionTV.setText(curQuestion.getQuestion());
        Log.d("QA", curQuestion.getClass().getSimpleName());

        if(curQuestion.getClass().getSimpleName().equals("TextQuestion")) {
            View child = getLayoutInflater().inflate(R.layout.answer_et, null);
            answerLL.addView(child);
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
            int px25 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    25,
                    r.getDisplayMetrics()
            );

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(px35, px20, px25, 0);
            answerLL.addView(child, layoutParams);
        }
        speakOutQuestion();
    }

    void speakOutQuestion() {

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
        }
    }
}
