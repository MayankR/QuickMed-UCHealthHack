package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button proceedButton;
    List<List<String>> patients;
    LinearLayout patientLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Utility.setupDB(this);

        proceedButton = (Button) findViewById(R.id.proceed_button);
        proceedButton.setOnClickListener(this);

        patientLL = (LinearLayout) findViewById(R.id.patient_d_ll);

        patients = Utility.getPeople();
        Log.d("MA", patients.size() + " number of people!");
        if(patients.size() == 0) {
            goToSignUp();
        }
        displayPatients();
    }

    void displayPatients() {
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
        int px15 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15,
                r.getDisplayMetrics()
        );

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, px10, 0, 0);

        patientLL.removeAllViews();
        for(int i=0;i<patients.size();i++) {
            final int patientID = i;
            View child = getLayoutInflater().inflate(R.layout.patient_detail, null);
            ((TextView)child.findViewById(R.id.pat_name_tv)).setText(patients.get(i).get(0));
            ((TextView)child.findViewById(R.id.pat_age_g_tv)).setText(patients.get(i).get(2) + ", " + patients.get(i).get(1) + " years");


            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPatient(patientID);
                }
            });
            patientLL.addView(child, layoutParams);
        }

    }

    void selectedPatient(int id) {
        Intent it = new Intent(this, QuestionsActivity.class);
        PatientData.name = patients.get(id).get(0);
        PatientData.age = patients.get(id).get(1);
        PatientData.gender = patients.get(id).get(2);
        startActivity(it);
        finish();
    }

    void goToSignUp() {
        Intent it = new Intent(this, UserDataActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.proceed_button:
                Intent it = new Intent(this, UserDataActivity.class);
                startActivity(it);
        }
    }
}
