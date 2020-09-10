package com.example.workncardio;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.workncardio.app.stepCover;

public class DateSetter extends Service {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference steps=firebaseDatabase.getReference("step").child(Homescreen.USER_NAME);


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendar=Calendar.getInstance();
        String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(calendar.get(Calendar.MONTH)) + "_" + String.valueOf(calendar.get(Calendar.YEAR));
        steps.child(String.valueOf(today)).setValue(new DataStepsModel(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), today.replace("_","/"), 0));

        Intent intent1=new Intent(getApplicationContext(),Homescreen.class);
        intent1.putExtra("Name1",Homescreen.USER_NAME);
        intent1.putExtra("fragment","def");
        intent1.setAction("");
        PendingIntent contentIntent= PendingIntent.getActivity(getApplicationContext(),0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification= new NotificationCompat.Builder(getApplicationContext(),stepCover)
                .setSmallIcon(R.drawable.ic_notif_name)
                .setContentTitle("New Day Initiated")
                .setContentText("Steps: "+ 0)
                .setLights(Color.RED,3000,3000)
                .setDefaults(Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_NO_CLEAR)
                .setOnlyAlertOnce(true)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .build();
        startForeground(8, notification);

        return Service.START_STICKY;
    }
}
