package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.workncardio.DataStepsModel;

import androidx.annotation.Nullable;

public class UserDets extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stepDB0.db";
    public static final String USER_NAME = "name";
    public static final String USER_EID = "email";
    public static final String USER_AGE = "age";
    public static final String USER_WEIGHT = "weight";
    public static final String USER_HEIGHT = "height";
    public static final String USER_WORKHOUR = "work_hour";
    public static final String USER_NOM = "nom";
    public static final String USER_baseStep = "baseStep";
    public static final String USER_BMI = "BMI";
    public static final String USER_PIC="profile_pic";
    public static final String USER_BACK="background";
    public static final String TABLE_NAME = "User_Table1";
    public static final Integer DatabaseVersion = 1;
    private static final String TAG = "UserDets";

    public UserDets(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+ "("+USER_NAME+" varchar," +USER_EID+" varchar,"+ USER_AGE+" integer,"+USER_PIC+" integer,"+USER_BACK+" integer,"+USER_WEIGHT+" float,"+USER_HEIGHT+" float,"+USER_WORKHOUR+" integer,"+USER_NOM+" integer,"+USER_baseStep+" integer,"+USER_BMI+" real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

   public boolean initiate(UserInfo userInfo){
       SQLiteDatabase database=this.getWritableDatabase();
       ContentValues contentValues=new ContentValues();

       contentValues.put(USER_NAME,userInfo.name);
       contentValues.put(USER_EID,userInfo.eID);
       contentValues.put(USER_AGE,userInfo.age);
       contentValues.put(USER_WEIGHT,userInfo.Weight);
       contentValues.put(USER_HEIGHT,userInfo.Height);
       contentValues.put(USER_NOM,userInfo.NOM);
       contentValues.put(USER_BMI,userInfo.BMI);
       contentValues.put(USER_WORKHOUR,userInfo.workHour);
       contentValues.put(USER_baseStep,userInfo.BaseStepGoal);
       contentValues.put(USER_PIC,String.valueOf(0));
       contentValues.put(USER_BACK,String.valueOf(4));

        database.insert(TABLE_NAME,null,contentValues);
        database.close();
        return true;



   }


   public boolean checkExistence(String name,String email){ 
        SQLiteDatabase database=this.getReadableDatabase();
        Boolean flag=false;
       String selectQuery = "SELECT "+USER_NAME+","+USER_EID+" FROM "
               + TABLE_NAME;
       Cursor c=database.rawQuery(selectQuery,null);
       if (c.moveToFirst()) {
           do{
               Log.d(TAG, "checkExistence: "+c.getString(c.getColumnIndex(USER_NAME))+" "+c.getString(c.getColumnIndex(USER_EID)));
               if(c.getString(c.getColumnIndex(USER_NAME)).toLowerCase().equals(name.toLowerCase()) && c.getString(c.getColumnIndex(USER_EID)).toLowerCase().equals(email.toLowerCase())) {
                   flag=true;
               }
           }
           while (c.moveToNext()) ;
       }
       return flag;


   }


   public Integer getBaseSteps(String name){
       Integer val=0;
        SQLiteDatabase db=this.getReadableDatabase();
       String selectQuery = "SELECT " + USER_baseStep + " FROM "
               + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
       Cursor c=db.rawQuery(selectQuery,null);
       while(c.moveToNext()){
           val=c.getInt(c.getColumnIndex(USER_baseStep));
       }

       return val;
   }

   public Integer getPicInfo(String name){
       Integer val=0;
       SQLiteDatabase db=this.getReadableDatabase();
       String selectQuery = "SELECT " + USER_PIC + " FROM "
               + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
       Cursor c=db.rawQuery(selectQuery,null);
       while(c.moveToNext()){
           val=c.getInt(c.getColumnIndex(USER_PIC));
       }

       return val;
   }

   public String getUserEid(String name){
       String val="";
       SQLiteDatabase db=this.getReadableDatabase();
       String selectQuery = "SELECT " + USER_EID + " FROM "
               + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
       Cursor c=db.rawQuery(selectQuery,null);
       while(c.moveToNext()){
           val=c.getString(c.getColumnIndex(USER_EID));
       }

       return val;

   }

   public boolean updateBase(Integer newVal,String name){
       Log.d(TAG, "updateBase: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_baseStep,newVal);
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
   }

   public Double getWeight(String name){
       Double val=0.0;
       SQLiteDatabase db=this.getReadableDatabase();
       String selectQuery = "SELECT " + USER_WEIGHT + " FROM "
               + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
       Cursor c=db.rawQuery(selectQuery,null);
       while(c.moveToNext()){
           val=c.getDouble(c.getColumnIndex(USER_WEIGHT));
       }

       return val;

   }

   public Double getHeight(String name){
       Double val=0.0;
       SQLiteDatabase db=this.getReadableDatabase();
       String selectQuery = "SELECT " + USER_HEIGHT + " FROM "
               + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
       Cursor c=db.rawQuery(selectQuery,null);
       while(c.moveToNext()){
           val=c.getDouble(c.getColumnIndex(USER_HEIGHT));
       }

       return val;
   }

   public Double getBMI(String name){
       Double val=0.0;
       SQLiteDatabase db=this.getReadableDatabase();
       String selectQuery = "SELECT " + USER_BMI + " FROM "
               + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
       Cursor c=db.rawQuery(selectQuery,null);
       while(c.moveToNext()){
           val=c.getDouble(c.getColumnIndex(USER_BMI));
       }

       return val;
   }

   public boolean setProfilePic(String name,Integer choice){
       Log.d(TAG, "setProfilePic: called");
       SQLiteDatabase db=this.getWritableDatabase();
       ContentValues values=new ContentValues();
       values.put(USER_PIC,String.valueOf(choice));
       db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
       db.close();
       return true;
   }

    public boolean setBack(String name,Integer choice){
        Log.d(TAG, "setProfilePic: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_BACK,String.valueOf(choice));
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public Integer getBack(String name){
        Integer val=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery = "SELECT " + USER_BACK + " FROM "
                + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
        Cursor c=db.rawQuery(selectQuery,null);
        while(c.moveToNext()){
            val=c.getInt(c.getColumnIndex(USER_BACK));
        }

        return val;
    }

    public Integer getAge(String name){
        Integer val=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery = "SELECT " + USER_AGE + " FROM "
                + TABLE_NAME + " WHERE " + USER_NAME + " = '" + name + "'";
        Cursor c=db.rawQuery(selectQuery,null);
        while(c.moveToNext()){
            val=c.getInt(c.getColumnIndex(USER_AGE));
        }

        return val;
    }

    public boolean updateEid(String newVal,String name){
        Log.d(TAG, "updateBase: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_EID,newVal);
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public boolean updateAge(Integer newVal,String name){
        Log.d(TAG, "updateBase: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_AGE,newVal);
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public boolean updateWeight(Double newVal,String name){
        Log.d(TAG, "updateBase: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_WEIGHT,newVal);
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public boolean updateHeight(Integer newVal,String name){
        Log.d(TAG, "updateBase: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_HEIGHT,newVal);
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public boolean updateNom(Integer newVal,String name){
        Log.d(TAG, "updateBase: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_NOM,newVal);
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }

    public boolean updateWORK(Integer newVal,String name){
        Log.d(TAG, "updateBase: called");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(USER_WORKHOUR,newVal);
        db.update(TABLE_NAME,values,USER_NAME+" = '"+name+"'",null);
        db.close();
        return true;
    }



}
