package com.example.workncardio;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class app extends Application {


    public static final String stepCover="channel1";
    public static final String stepGoal="channel2";
    public static final String workout="channel3";
    private static final String TAG = "app";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannnel();

        Log.d(TAG, "onCreate: "+isExternalStorageWritable());
        File dir=new File(getFilesDir(),"Profile");
        if(!dir.exists()){
            dir.mkdir();
            Log.d(TAG, "onCreateApp: directory made: " +dir);
            Log.d(TAG, "onCreateApp: "+dir.exists());

        }


    }

    public void createNotificationChannnel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel stepCheck=new NotificationChannel(stepCover,"Step Notifications", NotificationManager.IMPORTANCE_LOW);
            stepCheck.setDescription("Trial for channel");
            NotificationChannel stepGoal=new NotificationChannel(app.stepGoal,"Goal Notification",NotificationManager.IMPORTANCE_HIGH);
            stepGoal.setDescription("Goal Points");
            NotificationChannel workout=new NotificationChannel(app.workout,"Workout Notifications",NotificationManager.IMPORTANCE_HIGH);
            stepGoal.setDescription("Goal Points");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(stepCheck);
            manager.createNotificationChannel(stepGoal);
            manager.createNotificationChannel(workout);
        }
    }

    private boolean isExternalStorageWritable(){
        return Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED_READ_ONLY;
    }


}
