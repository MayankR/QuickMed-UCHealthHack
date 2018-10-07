package edu.uchealth.healthhack.nowaithospital;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mayank on 06/10/18.
 */

public class Utility {
    public static final String DATABASE_NAME = "PatientData.db";
    static SQLiteDatabase db;
    private final static OkHttpClient client = new OkHttpClient();

    public static void setupDB(Context ctx) {
        // TODO Auto-generated method stub
        db = ctx.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        db.execSQL(
                "create table IF NOT EXISTS people " +
                        "(id integer primary key, name text, age text, gender text)"
        );
    }

    public static void insertPatient(String name, int age, String gender) {
        String sql = "insert into people (name, age, gender) values('" + name + "', '" + age + "', '" + gender + "');";
        Log.d("Utility", sql);
        db.execSQL(sql);
    }

    public static List<List<String>> getPeople() {
        Cursor cursor = db.rawQuery("select * from people;", null);
        List<List<String>> patients = new ArrayList<>();
        cursor.moveToFirst();
        if(cursor.getCount() == 0) {
            return patients;
        }
        Log.d("Utility", "Moving to first patient");
        do {
            Log.d("Utility", "Moving to next patient");
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            String gender = cursor.getString(3);
            List<String> curData = Arrays.asList(name, age, gender);
            patients.add(curData);
        } while (cursor.moveToNext());
        Log.d("Utility", "Returning patient");
        return patients;
    }

    public static void uploadUserData() {

         MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", PatientData.name)
                .addFormDataPart("age", PatientData.age)
                .addFormDataPart("gender", PatientData.gender)
                .addFormDataPart("problem", PatientData.mainProblem);

//        PatientData.pData.put("Breath", "Heavy");
        Iterator it = PatientData.pData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            builder.addFormDataPart("key_" + pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        RequestBody requestBody = builder.build();

        final Request request = new Request.Builder()
                .url("http://100.81.96.40:8000/addPatientData")
                .post(requestBody)
                .build();

        Log.d("Utility", "Sending data");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        Log.d("Utility", "Sent data");
                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        System.out.println(response.body().string());
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}
