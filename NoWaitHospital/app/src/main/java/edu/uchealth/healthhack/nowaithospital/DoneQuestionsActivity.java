package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.graphics.Bitmap;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

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

    public void clickPic() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Utility.CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utility.CAMERA_REQUEST) {
            try {
//                final Uri imageUri = data.getData();
//                InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bundle extras = data.getExtras();
                Bitmap selectedImage = (Bitmap) extras.get("data");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                PatientData.imageb64_2 = encoded;
                goToHospital();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
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
            case R.id.take_pic_iv:
                clickPic();
                break;
        }
    }
}
