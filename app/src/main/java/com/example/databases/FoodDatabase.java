package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.workncardio.DataStepsModel;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class FoodDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "foodDB2.db";
    public static final String FOOD_NAME = "fname";
    public static final String FOOD_ID = "fid";
    public static final String FOOD_CALORIE = "calorie";
    public static final String TABLE_NAME="food_table_main2";
    private static final String TAG = "FoodDatabase";
    static int k=1;
    public FoodDatabase(@Nullable Context context,String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+ "("+FOOD_NAME+" varchar,"+FOOD_CALORIE+" real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        
    }

    public boolean initiate(FoodInfo foodInfo){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(FOOD_NAME,foodInfo.Name);
        contentValues.put(FOOD_CALORIE,foodInfo.kcal);

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
                    FoodInfo mDateStepsModel = new FoodInfo("",c.getString(c.getColumnIndex(FOOD_NAME)),c.getDouble(c.getColumnIndex(FOOD_CALORIE)));
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

    public boolean delItem(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,FOOD_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public boolean modify(String name,Double cal){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(FOOD_CALORIE,cal);
        db.update(TABLE_NAME,contentValues,FOOD_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }



}
