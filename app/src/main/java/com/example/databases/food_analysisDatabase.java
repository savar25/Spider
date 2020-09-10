package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class food_analysisDatabase extends SQLiteOpenHelper {
    public static final String Food_NAME = "food_name";
    public static final String FOOD_KCAL = "f_fcal";
    public static final String TABLE_NAME = "table2";
    public static final String FOOD_QUAN = "number";
    private static final String TAG = "food_analysisDatabase";

    public food_analysisDatabase(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+ "("+Food_NAME+" varchar,"+FOOD_KCAL+" real,"+FOOD_QUAN+" integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean setVals(FoodInfo foodInfo,Integer quant){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(Food_NAME,foodInfo.Name);
        contentValues.put(FOOD_KCAL,foodInfo.kcal);
        contentValues.put(FOOD_QUAN,quant);

        database.insert(TABLE_NAME,null,contentValues);
        database.close();
        return true;

    }

    public ArrayList<FoodInfo> getVals(){
        ArrayList<FoodInfo> foodList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String netQuery = "select * from " + TABLE_NAME;
        try {
            Cursor c = db.rawQuery(netQuery, null);
            if (c.moveToFirst()) {
                do{
                    FoodInfo mDateStepsModel = new FoodInfo("",c.getString(c.getColumnIndex(Food_NAME)),c.getDouble(c.getColumnIndex(FOOD_KCAL)));
                    Log.d(TAG, "readStepEntries: "+mDateStepsModel.Name);
                    foodList.add(mDateStepsModel);
                }
                while (c.moveToNext()) ;
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return foodList;


    }


    public ArrayList<Integer> getQuant(){
        ArrayList<Integer> foodList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String netQuery = "select * from " + TABLE_NAME;
        try {
            Cursor c = db.rawQuery(netQuery, null);
            if (c.moveToFirst()) {
                do{
                    Integer val=c.getInt(c.getColumnIndex(FOOD_QUAN));
                    foodList.add(val);
                }
                while (c.moveToNext()) ;
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return foodList;

    }


    public boolean delItem(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,Food_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public Double netCal(){
        SQLiteDatabase db=this.getReadableDatabase();
        String netQuery = "select * from " + TABLE_NAME;
        Cursor c = db.rawQuery(netQuery, null);
        Double net=0.00;
        if (c.moveToFirst()) {
            do{
                net+=(double)c.getInt(c.getColumnIndex(FOOD_QUAN))*c.getDouble(c.getColumnIndex(FOOD_KCAL));
            }
            while (c.moveToNext()) ;
        }
        db.close();
        return net;

    }

    public boolean modify(String name,Double cal){
        if (check(name)) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();

            contentValues.put(FOOD_KCAL,String.valueOf(cal));
            db.update(TABLE_NAME,contentValues,Food_NAME+" = '"+name+"'",null);
            Log.d(TAG, "modify: added");
            db.close();
            return true;
        }else {
            return false;
        }

    }

    public boolean check(String name){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery = "SELECT " + Food_NAME + " FROM "
                + TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
       Boolean net=false;
        if (c.moveToFirst()) {
            do{
                Log.d(TAG, "check: "+c.getString(c.getColumnIndex(Food_NAME))+":"+name);
                if(c.getString(c.getColumnIndex(Food_NAME)).equals(name)){
                    Log.d(TAG, "check: "+c.getColumnIndex(Food_NAME));
                    net=true;
                }
            }
            while (c.moveToNext()) ;
        }
        db.close();
        return net;

    }


}
