package com.example.workncardio;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.example.workncardio.app.stepCover;
import static com.example.workncardio.app.stepGoal;


public class stepFragment extends Fragment {

    public static String temp;
    private static NotificationManagerCompat notificationManagerCompat;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    static RecyclerView stepList;
    static StepAdapter adapter;
    static StepDatabase internalDatabase;
    static Context context;
    static filler fil1;
    static ArrayList<DataStepsModel> stepVals = new ArrayList<>();
    static TextView goal, highest;
    static UserDets userDatabase;
    static boolean check11 = false, check12 = false, check13 = false;
    private static final String TAG = "stepFragment";
    static Activity activity;
    ImageButton share;
    ImageView Whatspp;
    static UserInfo baseInfo;
    static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    static DatabaseReference reference = firebaseDatabase.getReference("Users");
    static DatabaseReference step = firebaseDatabase.getReference("step");
    static DatabaseReference internal;
    static ProgressBar bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        notificationManagerCompat = NotificationManagerCompat.from(getContext());
        stepList = (RecyclerView) view.findViewById(R.id.stepInfo);
        context = getActivity();
        goal = view.findViewById(R.id.vgoal);
        highest = view.findViewById(R.id.Highest);
        fil1 = view.findViewById(R.id.CurrentSteps);
        share = view.findViewById(R.id.shareButton);
        Whatspp = view.findViewById(R.id.whatsapp);
        userDatabase = new UserDets(context);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        internal = firebaseDatabase.getReference("step").child(Homescreen.USER_NAME);
        activity = getActivity();
        bar = view.findViewById(R.id.progressBar2);
        bar.setVisibility(View.VISIBLE);
        stepList.setVisibility(View.INVISIBLE);


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String extra = "";
                Calendar calendar = Calendar.getInstance();
                String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(Calendar.MONTH)) + "/" + String.valueOf(calendar.get(Calendar.YEAR));
                for (DataStepsModel vls : stepVals) {
                    if (vls.date.equals(today)) {
                        extra = "Steps Covered: " + String.valueOf(vls.steps) + "\n Best Count: " + String.valueOf(preferences.getInt("high", 0));
                    }
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, extra);
                sendIntent.putExtra(Intent.EXTRA_TITLE, R.string.app_name);
                sendIntent.setType("text/plain");

                Intent shareI = Intent.createChooser(sendIntent, null);
                startActivity(shareI);
            }
        });

        Whatspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String extra = "";
                Calendar calendar = Calendar.getInstance();
                String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(Calendar.MONTH)) + "/" + String.valueOf(calendar.get(Calendar.YEAR));
                for (DataStepsModel vls : stepVals) {
                    if (vls.date.equals(today)) {
                        extra = "Steps Covered: " + String.valueOf(vls.steps) + "\n Best Count: " + String.valueOf(preferences.getInt("high", 0));
                    }
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, extra);
                sendIntent.putExtra(Intent.EXTRA_TITLE, R.string.app_name);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bar.setVisibility(View.GONE);
                stepList.setVisibility(View.VISIBLE);
                setup();
            }
        }, 1700);


        return view;
    }


   /* public static void sendOnChannel1(View v,String text) {

        Intent intent=new Intent(context,Homescreen.class);
        Log.d(TAG, "sendOnChannel1: "+temp);
        intent.putExtra("Name1",Homescreen.USER_NAME);
        intent.putExtra("fragment","def");
        intent.setAction("");
        PendingIntent contentIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(context,stepCover)
                .setSmallIcon(R.drawable.ic_notif_name)
                .setContentTitle("Goal Reached")
                .setContentText(text)
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

        notificationManagerCompat.notify(1,notification);
    }*/

    public static void sendOnchannel2(View view, String text) {

        Intent intent = new Intent(context, Homescreen.class);
        Log.d(TAG, "sendOnchannel2: " + temp);
        intent.putExtra("Name1", Homescreen.USER_NAME);
        intent.putExtra("fragment", "def");
        intent.setAction("");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification2 = new NotificationCompat.Builder(context, stepGoal)
                .setSmallIcon(R.drawable.ic_notif_name)
                .setContentTitle("Goal Reached")
                .setContentText(text)
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL)
                .setColorized(true)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setColor(Color.parseColor("#ff00ff"))
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(2, notification2);

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

                        return;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        internal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final @NonNull DataSnapshot snapshot) {
                stepVals.clear();
                final Calendar calendar = Calendar.getInstance();
                String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(calendar.get(Calendar.MONTH)) + "_" + String.valueOf(calendar.get(Calendar.YEAR));



                for (int i = 0; i < 6; i++) {

                    String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(calendar.get(Calendar.MONTH)) + "_" + String.valueOf(calendar.get(Calendar.YEAR));
                    Log.d(TAG, "onDataChangeDate: "+date);
                    if (snapshot.child(date).getValue(DataStepsModel.class) != null && !stepVals.contains(snapshot.child(date).getValue(DataStepsModel.class))) {

                        stepVals.add(snapshot.child(date).getValue(DataStepsModel.class));
                        Log.d(TAG, "onDataChange: " + stepVals);
                    }
                    calendar.setTimeInMillis(calendar.getTimeInMillis()-1000*24*60*60);
                }


                    DataStepsModel model = snapshot.child(today).getValue(DataStepsModel.class);
                    if (model != null) {
                        if (preferences.getInt("high", 0) == 0) {
                            editor = preferences.edit();
                            editor.putInt("high", model.steps);
                            editor.commit();
                        } else if (preferences.getInt("high", 0) < model.steps) {
                            editor = preferences.edit();
                            editor.putInt("high", model.steps);
                            editor.commit();
                        }

                        highest.setText(String.valueOf(preferences.getInt("high", 0)));
                        fil1.setPercentage(context, model.steps);

                        Integer base = baseInfo.getBaseStepGoal();
                        Double percent = (double) model.steps / base;
                        if (percent > 0.2 && percent < 0.3 && !check11) {
                            sendOnchannel2(new View(context), "25% steps covered");
                            check11 = !check11;
                        } else if (percent > 0.4 && percent < 0.6 && !check12) {
                            sendOnchannel2(new View(context), "50% steps covered");
                            check12 = !check12;
                        } else if (percent > 0.7 && percent < 0.8 && !check13) {
                            sendOnchannel2(new View(context), "75% steps covered");
                            check13 = !check13;
                        }

                        adapter = new StepAdapter(stepVals, context);
                        stepList.setAdapter(adapter);
                        stepList.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        Calendar calendar1 = Calendar.getInstance();
                        String today1 = String.valueOf(calendar1.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(calendar1.get(Calendar.MONTH)) + "_" + String.valueOf(calendar1.get(Calendar.YEAR));
                        internal.child(today).setValue(new DataStepsModel(calendar1.get(Calendar.DAY_OF_MONTH), String.valueOf(calendar1.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar1.get(Calendar.MONTH)) + "/" + String.valueOf(calendar1.get(Calendar.YEAR)), 0));
                    }
                }

                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }
            });

        }

        public void setup () {
            Log.d(TAG, "setup: " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            activity = getActivity();
            goal.setText(String.valueOf(baseInfo.getBaseStepGoal()));


        }


    }