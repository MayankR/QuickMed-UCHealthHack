package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UserDataActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nameET, ageET;
    RadioGroup genderRG;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        nameET = (EditText) findViewById(R.id.name_et);
        ageET = (EditText) findViewById(R.id.age_et);

        genderRG = (RadioGroup) findViewById(R.id.gender_rg);

        doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
    }

    void donePressed() {
        String nameStr = nameET.getText().toString().trim();
        if(ageET.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid age", Toast.LENGTH_SHORT).show();
            return;
        }
        int ageNum = Integer.parseInt(ageET.getText().toString());
        if(nameStr.length() == 0) {
            Toast.makeText(this, "Enter patient's name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ageNum < 0) {
            Toast.makeText(this, "Enter valid age", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedId = genderRG.getCheckedRadioButtonId();
        if(selectedId == -1) {
            Toast.makeText(this, "Select the patient's gender", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("UDA", nameStr + " " + ageNum + " " + selectedId);
        String genderStr = "";
        if(selectedId == R.id.radio_f) {
            genderStr = "Female";
        } else if(selectedId == R.id.radio_m) {
            genderStr = "Male";
        } else {
            genderStr = "Other";
        }

        Utility.insertPatient(nameStr, ageNum, genderStr);
        Intent it = new Intent(this, QuestionsActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.done_button:
                donePressed();
                break;
        }
    }
}
