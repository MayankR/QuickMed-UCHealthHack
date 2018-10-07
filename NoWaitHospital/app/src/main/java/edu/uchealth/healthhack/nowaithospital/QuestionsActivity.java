package edu.uchealth.healthhack.nowaithospital;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    QuestionBank qb;
    Question curQuestion;
    List<Question> allQuestions;
    List<String> allAns;
    int curQuestionIndex;
    FloatingActionButton nine11FAB, nextFAB, speechFAB;
    TextView questionTV, patientNameTV;
    RelativeLayout questionRL;
    LinearLayout answerLL;
    EditText answerET;
    float x1, x2;
    final int SWIPE_THRESHOLD = 300;
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    static final int REQUEST_IMAGE_CAPTURE = 101;
    String mCurrentPhotoPath;

    TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_questions);

        myTTS = new TextToSpeech(this, this);

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
        allAns = new ArrayList<>();

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
            int px65 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    65,
                    r.getDisplayMetrics()
            );
            int px10 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10,
                    r.getDisplayMetrics()
            );

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            answerLL.removeAllViews();
            if(curQuestion.getQuestion().equals("Please share a pic of your injury.")) {
                layoutParams.setMargins(px65, px10, px65, 0);
                ImageView child = (ImageView) getLayoutInflater().inflate(R.layout.click_pic_img, null);
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickPic();
                    }
                });
                answerLL.addView(child, layoutParams);
            } else {
                layoutParams.setMargins(px35, px10, px35, 0);
                for(int i=0;i<answersList.size();i++) {
                    final int optionID = i;
                    TextView child = (TextView) getLayoutInflater().inflate(R.layout.answer_option, null);
                    child.setText(answersList.get(i));
                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(curQuestionIndex == allAns.size()-1) {
                                allAns.set(allAns.size()-1, "" + optionID);
                            } else {
                                allAns.add("" + optionID);
                            }
                            clickedOption(optionID);
                        }
                    });
                    answerLL.addView(child, layoutParams);
                }
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

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(px35, px20, px35, 0);
            answerLL.removeAllViews();
            answerET = (EditText)child;
            answerET.setImeOptions(EditorInfo.IME_ACTION_DONE);
            answerET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    switch (actionId) {

                        case EditorInfo.IME_ACTION_DONE:
                            Toast.makeText(getApplicationContext(), "yo", Toast.LENGTH_SHORT).show();
                            return true;

                        default:
                            return false;
                    }
                }
            });
            answerET.addTextChangedListener(new TextWatcher() {
                CharSequence cs;
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    cs = charSequence;
                }

                boolean _ignore = false;
                @Override
                public void afterTextChanged(Editable editable) {
                    if (_ignore) {
                        _ignore = false;
                        return;
                    }

                    if(cs.length() > 0) {
                        if (cs.charAt(cs.length() - 1) == '\n') {
                            _ignore = true;
                            answerET.setText(answerET.getText().toString().substring(0, answerET.getText().toString().length() - 1));
                            answerET.setSelection(answerET.getText().length());
                        }
                    }



                }
            });
            if(curQuestionIndex < allAns.size()) {
                answerET.setText(allAns.get(curQuestionIndex));
            }
            answerLL.addView(answerET, layoutParams);

            nextFAB.setVisibility(View.VISIBLE);
        }
        speakOutQuestion();
    }

    public void clickPic() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Utility.CAMERA_REQUEST);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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

    boolean backIgnore = true;
    @Override
    public void onBackPressed() {
        if(curQuestionIndex == 0) {
            if(backIgnore) {
                backIgnore = false;
                return;
            }
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
            backIgnore = true;
        }
        backIgnore = true;
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
        for(int i=allAns.size()-1;i>curQuestionIndex;i--) {
            allAns.remove(i);
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

    void speakOutQuestion() {
        myTTS.speak(curQuestion.getQuestion(), TextToSpeech.QUEUE_FLUSH, null, "messageID");
    }

    void registerResponse() {
        if(curQuestionIndex == allAns.size()-1) {
            allAns.set(allAns.size()-1, answerET.getText().toString().trim());
        } else {
            allAns.add(answerET.getText().toString().trim());
        }
        allQuestions.addAll(curQuestion.getNextQuestions(answerET.getText().toString().trim()));
        goToNextQuestion();
    }

    void registerResponse(String s) {
        if(curQuestion.getClass().getSimpleName().equals("TextQuestion")) {
            answerET.setText(s);
        }
        if(curQuestionIndex == allAns.size()-1) {
            allAns.set(allAns.size()-1, s);
        } else {
            allAns.add(s);
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
        } else if(requestCode == Utility.CAMERA_REQUEST) {
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
                PatientData.imageb64 = encoded;
                goToNextQuestion();
            } catch(Exception e) {
                e.printStackTrace();
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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
//            myTTS.setLanguage(new Locale("hi"));
            myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {
                    Log.d("QA", "TTS complete");
                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {
                    Log.d("QA", "TTS start");
                }
            });
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
