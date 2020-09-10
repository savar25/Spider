package com.example.workncardio;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.example.databases.UserInfo;
import com.example.workncardio.stepFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import static com.example.workncardio.app.stepCover;

public class StepService extends Service implements SensorEventListener {

    static boolean flag=false;
    SensorManager sensorManager1;
    Sensor sensor1;
    StepDatabase database;
    UserInfo baseInfo;
    DataStepsModel model;
    private static final String TAG = "StepService";
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");
    static DatabaseReference steps;
    Context context;
    Notification notification;
    String name;
    Intent intent1;
    PendingIntent contentIntent;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: sensor Called");
        sensorManager1=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager1.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null){
            sensor1=sensorManager1.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            sensorManager1.registerListener(this,sensor1,SensorManager.SENSOR_DELAY_NORMAL);



        }
        name=Homescreen.USER_NAME;
        Log.d(TAG, "onCreateService: "+name);
        context=getApplicationContext();
        Log.d(TAG, "onCreateSERVICE: "+context);
        steps=firebaseDatabase.getReference("step").child(name.toString());
        model=new DataStepsModel(1,"1/1/2020",0);
        flag=false;
        intent1=new Intent(getApplicationContext(),Homescreen.class);
        intent1.putExtra("Name1",Homescreen.USER_NAME);
        intent1.putExtra("fragment","def");
        intent1.setAction("");
        contentIntent=PendingIntent.getActivity(getApplicationContext(),0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        notification= new NotificationCompat.Builder(getApplicationContext(),stepCover)
                .setSmallIcon(R.drawable.ic_notif_name)
                .setContentTitle("Goal Reached")
                .setContentText("Steps: "+ model.getSteps())
                .setLights(Color.RED,3000,3000)
                .setDefaults(Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_NO_CLEAR)
                .setOngoing(true)
                .setColorized(true)
                .setOnlyAlertOnce(true)
                .setColor(Color.parseColor("#00ff00"))
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .build();
        startForeground(3, notification);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        steps=firebaseDatabase.getReference("step").child(name);
        flag=false;
        setVal();

    }


    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        startForeground(3,notification);
        Log.d(TAG, "onStartCommand: "+Homescreen.USER_NAME);
        steps=firebaseDatabase.getReference("step").child(name);
        final String name=intent.getStringExtra("Name1");
        setVal();
        intent1=new Intent(getApplicationContext(),Homescreen.class);
        intent1.putExtra("Name1",Homescreen.USER_NAME);
        intent1.putExtra("fragment","def");
        intent1.setAction("");
        contentIntent=PendingIntent.getActivity(getApplicationContext(),0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        notification= new NotificationCompat.Builder(getApplicationContext(),stepCover)
                .setSmallIcon(R.drawable.ic_notif_name)
                .setContentTitle("Goal Reached")
                .setContentText("Steps: "+ model.getSteps())
                .setLights(Color.RED,3000,3000)
                .setDefaults(Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_NO_CLEAR)
                .setOngoing(true)
                .setColorized(true)
                .setOnlyAlertOnce(true)
                .setColor(Color.parseColor("#00ff00"))
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .build();
        startForeground(3, notification);

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,56);

        Intent intent2=new Intent(getApplicationContext(),DateSetter.class);
        PendingIntent pendingIntent=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent=PendingIntent.getForegroundService(getApplicationContext(),002,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            pendingIntent=PendingIntent.getService(getApplicationContext(),002,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        AlarmManager manager=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        return Service.START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stopForeground(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setVal(){

        steps.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar calendar = Calendar.getInstance();
                String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(calendar.get(Calendar.MONTH)) + "_" + String.valueOf(calendar.get(Calendar.YEAR));
                DataStepsModel dataStepsModel = snapshot.child(today).getValue(DataStepsModel.class);
                if (dataStepsModel != null ) {
                    dataStepsModel.setSteps(dataStepsModel.getSteps() + 1);
                    model=dataStepsModel;
                    notification= new NotificationCompat.Builder(getApplicationContext(),stepCover)
                            .setSmallIcon(R.drawable.ic_notif_name)
                            .setContentTitle("Goal Reached")
                            .setContentText("Steps: "+ model.getSteps())
                            .setLights(Color.RED,3000,3000)
                            .setDefaults(Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_NO_CLEAR)
                            .setOngoing(true)
                            .setColorized(true)
                            .setOnlyAlertOnce(true)
                            .setColor(Color.parseColor("#00ff00"))
                            .setCategory(Notification.CATEGORY_MESSAGE)
                            .setPriority(NotificationCompat.PRIORITY_LOW)
                            .setContentIntent(contentIntent)
                            .build();
                    startForeground(3,notification);
                    steps.child(today).setValue(dataStepsModel);
                } else if(!flag){
                    flag=true;
                    steps.child(String.valueOf(today)).setValue(new DataStepsModel(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), today.replace("_","/"), 0));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}