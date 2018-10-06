package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    QuestionBank qb;
    Question curQuestion;
    FloatingActionButton nine11FAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_questions);

        qb = new QuestionBank();
        curQuestion = qb.getRoot();

        nine11FAB = (FloatingActionButton) findViewById(R.id.nine11_fab);
        nine11FAB.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.nine11_fab:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:811"));
                startActivity(callIntent);
        }
    }
}
