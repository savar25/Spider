package com.example.workncardio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class StepDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Sdb5.db";
    private static final String TABLE_STEPS_SUMMARY = "StepsSummary5";
    private static final String ID = "id";
    private static final String DATE="date";
    private static final String STEPS_COUNT = "stepscount";
    private static final String CREATION_DATE = "creationdate";
    private static final String TAG = "StepDatabase";


    public StepDatabase(Context context,String name) {
        super(context, name, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "
                + TABLE_STEPS_SUMMARY + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CREATION_DATE + " TEXT,"+ STEPS_COUNT + " INTEGER,"+DATE+" INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

    public boolean createStepsEntry() {
        boolean isDateAlreadyPresent = false;
        boolean createSuccessful = false;
        int currentDateStepCounts = 0;
        Calendar calendar = Calendar.getInstance();
        Log.d(TAG, "createStepsEntry: "+calendar.get(Calendar.DAY_OF_MONTH));
        String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(Calendar.MONTH)) + "/" + String.valueOf(calendar.get(Calendar.YEAR));
        String selectQuery = "SELECT " + STEPS_COUNT + " FROM "
                + TABLE_STEPS_SUMMARY + " WHERE " + CREATION_DATE + " = '" + today + "'";
        try {
            SQLiteDatabase sqLiteDatabase1 = this.getReadableDatabase();
            Cursor c = sqLiteDatabase1.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    isDateAlreadyPresent = true;
                    currentDateStepCounts =
                            c.getInt((c.getColumnIndex(STEPS_COUNT)));
                } while (c.moveToNext());
            }
            sqLiteDatabase1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CREATION_DATE, today);
            values.put(DATE,String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            if(isDateAlreadyPresent)
            {
                values.put(STEPS_COUNT, ++currentDateStepCounts);
                int row = db.update(TABLE_STEPS_SUMMARY, values,
                        CREATION_DATE +" = '"+ today+"'", null);
                if(row == 1)
                {
                    createSuccessful = true;
                }
                db.close();
            }
            else
            {
                values.put(STEPS_COUNT, 1);
                values.put(DATE,String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                stepFragment.check11=false;
                stepFragment.check12=false;
                stepFragment.check13=false;
                long row = db.insert(TABLE_STEPS_SUMMARY, null,
                        values);
                if(row!=-1)
                {
                    createSuccessful = true;
                }
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return createSuccessful;
    }

    public ArrayList<DataStepsModel> readStepEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataStepsModel> stepList = new ArrayList();
        String netQuery = "select * from " + TABLE_STEPS_SUMMARY;
        try {
            Cursor c = db.rawQuery(netQuery, null);
            if (c.moveToFirst()) {
                do{
                    DataStepsModel mDateStepsModel = new DataStepsModel(c.getInt(c.getColumnIndex(DATE)),c.getString((c.getColumnIndex(CREATION_DATE))),c.getInt((c.getColumnIndex(STEPS_COUNT))));
                    Log.d(TAG, "readStepEntries: "+mDateStepsModel.date);
                    stepList.add(mDateStepsModel);
                }
                while (c.moveToNext()) ;
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Collections.reverse(stepList);
        return stepList;

    }

    public int getSteps(String today){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "
                + TABLE_STEPS_SUMMARY + " WHERE " + CREATION_DATE + " = '" + today + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(STEPS_COUNT));
    }
}
