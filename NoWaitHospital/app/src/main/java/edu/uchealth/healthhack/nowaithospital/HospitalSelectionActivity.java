package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HospitalSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    TextView h1, h2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_selection);
        h1 = (TextView) findViewById(R.id.h1);
        h1.setOnClickListener(this);
        h2 = (TextView) findViewById(R.id.h2);
        h2.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.h1:
                Utility.uploadUserData(1);
                break;
            case R.id.h2:
                Utility.uploadUserData(2);
                break;
        }
        Toast.makeText(this, "Your data has been shared!", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(this, AllDoneActivity.class);
        startActivity(it);
        finish();
    }
}
