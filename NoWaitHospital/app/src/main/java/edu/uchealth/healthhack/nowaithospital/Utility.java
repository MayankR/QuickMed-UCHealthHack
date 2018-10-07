package edu.uchealth.healthhack.nowaithospital;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mayank on 06/10/18.
 */

public class Utility {
    public static final String DATABASE_NAME = "PatientData.db";
    static SQLiteDatabase db;

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
        cursor.moveToFirst();
        Log.d("Utility", "Moving to first patient");
        List<List<String>> patients = new ArrayList<>();
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
}
