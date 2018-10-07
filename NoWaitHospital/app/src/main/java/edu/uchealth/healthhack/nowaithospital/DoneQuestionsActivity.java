package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoneQuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView addimageIV;
    TextView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_questions);

        addimageIV = (ImageView) findViewById(R.id.take_pic_iv);
        addimageIV.setOnClickListener(this);

        nextButton = (TextView) findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
    }

    void goToHospital() {
        Intent it = new Intent(this, HospitalSelectionActivity.class);
        startActivity(it);
        finish();
    }

    boolean backIgnore = true;
    @Override
    public void onBackPressed() {
        if(backIgnore) {
            Toast.makeText(this, "Press back again to quit", Toast.LENGTH_SHORT).show();
            backIgnore = false;
            return;
        }
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
        backIgnore = true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.next_button:
                goToHospital();
                break;
        }
    }
}
