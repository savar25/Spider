package com.example.workncardio;

import android.icu.lang.UScript;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.databases.FoodDatabase;
import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.example.databases.food_analysisDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScreenFragment extends Fragment {
        TimePicker start,stop;
        Date date_start,data_stop;
        TextView total_hours;
        ProgressBar progressBar;
        UserDets userDets;
        UserInfo baseInfo;
        food_analysisDatabase food_analysisDatabaseD;
        long difference;
        TextView dinner,sleep,BMR;
    SimpleDateFormat format=new SimpleDateFormat("HH:mm");
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");
    DatabaseReference foodAnalysisD=firebaseDatabase.getReference("Food Analysis Dinner").child(Homescreen.USER_NAME);
        public ScreenFragment() {
        }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_screen, container, false);
        start=view.findViewById(R.id.startTime);
        stop=view.findViewById(R.id.awake_time);
        start.setIs24HourView(true);
        stop.setIs24HourView(true);



        total_hours=view.findViewById(R.id.time_left);
        progressBar=view.findViewById(R.id.progress);
        dinner=view.findViewById(R.id.eat_time);
        BMR=view.findViewById(R.id.cal_loss);
        sleep=view.findViewById(R.id.expected_sleep_time);





        start.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String start_time=String.valueOf(timePicker.getHour())+":"+String.valueOf(timePicker.getMinute());

                try {
                    date_start = format.parse(start_time);
                }catch (ParseException p){
                    p.printStackTrace();
                }
                setTime(date_start,data_stop);
            }
        });

        stop.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String start_time=String.valueOf(timePicker.getHour())+":"+String.valueOf(timePicker.getMinute());
                try {
                    data_stop= format.parse(start_time);
                }catch (ParseException p){
                    p.printStackTrace();
                }
                setTime(date_start,data_stop);

            }
        });

        return view;
    }

    public void setTime(Date one,Date two){
            difference = two.getTime() - one.getTime();
        if(difference<0)
        {
            try {
                Date dateMax = format.parse("24:00");
                Date dateMin = format.parse("00:00");
                difference = (dateMax.getTime() - one.getTime()) + (two.getTime() - dateMin.getTime());
            }catch (ParseException p){
                p.printStackTrace();
            }
        }
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        Log.i("log_tag","Hours: "+hours+", Mins: "+min);
        String timeVal="Sleep Time: \n "+String.valueOf(hours)+":"+String.valueOf(min);
        total_hours.setText(timeVal);
        setProgress(difference);
        setDinner();
    }

    public void setProgress(long time){
            long REQUIREMENT=8*60*60*1000;
            long percent=time*100/REQUIREMENT;
            long setupPercent=percent*67/100;
            if(setupPercent>67){
                setupPercent=67;
            }
            if(Build.VERSION.SDK_INT>=24) {
                progressBar.setProgress((int) setupPercent, true);
            }else{
                progressBar.setProgress((int) setupPercent);
            }
    }

    public void setDinner(){
            Double BMI=baseInfo.getBMI();
            String timeVal="";


            Integer hr=start.getHour();
        if (hr == 0) {
            hr=24;
        }
            if(BMI<26.0){
                if(start.getMinute()>30) {
                    timeVal = String.valueOf(hr - 1) + ":" + String.valueOf(start.getMinute() - 15);
                    sleep.setText(String.valueOf(start.getHour()) + ":" + String.valueOf(start.getMinute()));
                    dinner.setText(timeVal);
                }else{
                    timeVal = String.valueOf(hr - 2) + ":" + String.valueOf(start.getMinute() +30);
                    sleep.setText(String.valueOf(start.getHour()) + ":" + String.valueOf(start.getMinute()));
                    dinner.setText(timeVal);
                }

            }else {
                if(start.getMinute()>30) {
                    timeVal = String.valueOf(hr - 2) + ":" + String.valueOf(start.getMinute() - 15);
                    dinner.setText(timeVal);
                }else{
                    timeVal = String.valueOf(hr - 3) + ":" + String.valueOf(start.getMinute() +30);
                    dinner.setText(timeVal);
                }

                if(hr==24){
                    sleep.setText(String.valueOf(1)+";"+String.valueOf(start.getMinute()));
                }else {
                    sleep.setText(String.valueOf(start.getHour() +1) + ";" + String.valueOf(start.getMinute()));
                }
            }
    }


    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserInfo info = snapshot1.getValue(UserInfo.class);
                    if (info.getName().equals(Homescreen.USER_NAME)) {
                        baseInfo = info;
                        show();

                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void show(){
        BMR.setText(String.valueOf(66.47 + (baseInfo.getWeight() * 2.204) + (baseInfo.getHeight() / 2.54) - 6.755 * baseInfo.getAge()));
        try {
            date_start = format.parse("22:00");
            data_stop = format.parse("07:00");
            start.setHour(date_start.getHours());
            stop.setHour(data_stop.getHours());
            start.setMinute(date_start.getMinutes());
            stop.setMinute(data_stop.getMinutes());
            setTime(date_start, data_stop);
        } catch (ParseException p) {
            p.printStackTrace();
        }
    }
}