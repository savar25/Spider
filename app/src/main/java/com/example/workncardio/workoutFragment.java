package com.example.workncardio;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.workncardio.app.stepCover;
import static com.example.workncardio.app.stepGoal;
import static com.example.workncardio.app.workout;


public class workoutFragment extends Fragment implements SensorEventListener {


    public static int SONG_SELECT = 1004;
    IonBackPressed backPressed;
    static Integer stepCount;
    static Integer setup;
    static Double kcal;
    SensorManager manager1, mangaer2;
    SensorEventListener listener;
    private static final String TAG = "workoutFragment";
    TextView steps, accel, cal, goal, songName;
    Double acc;
    ImageView play, pause, stop, heart;
    static ImageView fire1, fire2, fire3, ice;
    Spinner musicBundle;
    public static MediaPlayer mediaPlayer;
    static Integer pausePoint;
    static NotificationManagerCompat notificationManagerCompat,notificationManagerCompat1;
    ArrayList<Integer> imageList = new ArrayList<>();
    static Context context;
    static Activity activity;
    BacktoOriginal backtoOriginal;
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> musicUris = new ArrayList<>();
    Notification notification;
    ImageButton nPlay, nPause, nStop;
    View view;

    public workoutFragment(Integer setup, Double kcal, Integer stepCount) {
        this.setup = setup;
        this.kcal = kcal;
        workoutFragment.stepCount = stepCount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workout, container, false);
        play = view.findViewById(R.id.playbtn);
        pause = view.findViewById(R.id.pausebtn);
        stop = view.findViewById(R.id.stopbtn);
        fire1 = view.findViewById(R.id.fire1);
        fire2 = view.findViewById(R.id.fire2);
        fire3 = view.findViewById(R.id.fire3);
        ice = view.findViewById(R.id.cooldown);
        musicBundle = view.findViewById(R.id.bundle);
        heart = view.findViewById(R.id.heart);

        backPressed=(IonBackPressed)context;

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.i(TAG, "keyCode: " + i);
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    Log.i(TAG, "onKey Back listener is working!!!");
                    manager1.unregisterListener(workoutFragment.this);
                    mangaer2.unregisterListener(workoutFragment.this);
                    mediaPlayer.stop();
                    setCancel();
                    backPressed=(IonBackPressed)context;
                    backPressed.onBackPress();
                    return true;
                }
                return false;
            }
        });

        context = getContext();
        activity = getActivity();
        final String[] songNames;


        musicBundle.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        notificationManagerCompat = NotificationManagerCompat.from(getContext());
        notificationManagerCompat1= NotificationManagerCompat.from(getContext());

        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));


        steps = view.findViewById(R.id.step_cardio);
        steps.setText(String.valueOf(stepCount));
        accel = view.findViewById(R.id.acceleration_cardio);
        cal = view.findViewById(R.id.cal_lost_cardio);
        goal = view.findViewById(R.id.goal_cal_cardio);
        songName = view.findViewById(R.id.song_name);
        songName.setSelected(true);
        songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        songName.setSingleLine(true);
        songName.setText("Trial");


        imageList.add(R.drawable.heart);
        imageList.add(R.drawable.heart1);
        imageList.add(R.drawable.heart2);
        imageList.add(R.drawable.heart3);


        goal.setText(String.valueOf(kcal));
        manager1 = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mangaer2 = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (manager1.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) == null) {
            Sensor sensor1 = manager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            manager1.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Sensor sensor1 = manager1.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            manager1.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Sensor sensor2 = mangaer2.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mangaer2.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    Toast.makeText(getContext(), "Music Playing", Toast.LENGTH_SHORT).show();
                } else if (mediaPlayer == null) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.seekTo(pausePoint);
                    mediaPlayer.start();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    pausePoint = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();

                } else {
                    Toast.makeText(getContext(), "No music playing", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                } else {
                    Toast.makeText(getContext(), "No music playing", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*final ArrayList<File> songs=readSongs(Environment.getExternalStorageDirectory());
        songNames=new String[songs.size()];
        for(int i=0;i<songs.size();i++){
            songNames[i]=songs.get(i).getName().replace(".mp3","");
        }*/


        nameList.add("BollyMix 1");
        nameList.add("BollyMix 2");
        setMusicList();
       /* for(int i=0;i<songs.size();i++){
            nameList.add(songNames[i]);
        }*/


        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.music_spinner_layout, nameList);
        adapter.setDropDownViewResource(R.layout.music_dropdown);
        musicBundle.setAdapter(adapter);

        musicBundle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (adapterView.getSelectedItem().toString()) {

                    case "BollyMix 1":
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();

                        }
                        mediaPlayer = MediaPlayer.create(getContext(), R.raw.bolly1);
                        songName.setText("BollyMix(JukeBox 13 Songs)|Hindi|Contemporary/Recent");
                        songName.setSelected(true);

                        break;
                    case "BollyMix 2":
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();

                        }
                        mediaPlayer = MediaPlayer.create(getContext(), R.raw.bolly1);


                        songName.setText("BollyMix 2(JukeBox 8 Songs)|Hindi|Gym Freaks");
                        songName.setSelected(true);

                        break;
                    default:
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();


                        }

                       /* for(int j=0;j<songs.size();j++){
                            if(songNames[j].equals(adapterView.getSelectedItem().toString())){
                                mediaPlayer=MediaPlayer.create(getContext(), Uri.parse(songs.get(j).toString())) ;
                                songName.setText(songNames[j]);
                                songName.setSelected(true);
                            }
                        }*/

                        play(getContext(), Uri.parse(musicUris.get(adapterView.getSelectedItemPosition() - 2)), nameList.get(adapterView.getSelectedItemPosition()));


                }

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sendonchannel3(new View(getContext()), "Workout Starting...", 2);


        return view;
    }

    public void setCancel() {
        cancelNotifications();
        Notification.MediaStyle style=new Notification.MediaStyle();
        Notification notification7 = new NotificationCompat.Builder(context, workout)
                .setSmallIcon(R.drawable.ic_notif_name)
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(Notification.FLAG_SHOW_LIGHTS)
                .setContentTitle("Workout Cancelled")
                .setContentText("Start next workout soon")
                .setStyle(new androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
                .setColorized(true)
                .setOnlyAlertOnce(true)
                .setColor(Color.parseColor("#ff0000"))
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManagerCompat1.notify(8, notification7);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        fire1 = view.findViewById(R.id.fire1);
        fire2 = view.findViewById(R.id.fire2);
        fire3 = view.findViewById(R.id.fire3);
        ice = view.findViewById(R.id.cooldown);
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER || sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Double cube = Math.pow(sensorEvent.values[0], 3) + Math.pow(sensorEvent.values[1], 3) + Math.pow(sensorEvent.values[2], 3);
            acc = Math.sqrt(cube);
            accel.setText(String.format("%.2f", acc));
            switch (setup) {
                case 0:
                    if (acc > 2.19 && acc < 2.47) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    } else if (acc >= 2.5 && acc < 3.4) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    } else if (acc >= 3.4) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                    } else {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    }
                    break;
                case 2:
                    if (acc > 3.07 && acc < 3.47) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    } else if (acc >= 3.47 && acc < 4.79) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    } else if (acc >= 4.79) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                    } else {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    }
                    break;

                case 1:
                    if (acc > 2.84 && acc < 3.26) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    } else if (acc >= 3.26 && acc < 4.1) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    } else if (acc >= 4.1) {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_on));
                    } else {
                        fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                        fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    }
                    break;

                default:
                    fire1.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    fire2.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
                    fire3.setImageDrawable(getActivity().getDrawable(R.drawable.fire_off));
            }
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            steps.setText(String.valueOf(stepCount));
            Double calLost = 0.4 * stepCount * (setup + 1);
            cal.setText(String.format("%.2f", calLost));

            if (calLost > 90 * kcal / 100) {
                ice.setImageDrawable(getActivity().getDrawable(R.drawable.ice_on));
                Snackbar.make(new View(getContext()), "Start CoolDown", Snackbar.LENGTH_LONG).show();
            }

            if (calLost >= kcal) {
                cancelNotifications();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                Snackbar.make(new View(getContext()), "Workout Completed", Snackbar.LENGTH_LONG).show();
                setCompletion("Workout Completed");
                backtoOriginal.goBack(calLost);
            }

            sendonchannel3(new View(getContext()), "Steps: " + stepCount + "\nCalories Burnt: " + calLost, 1);
            switch (stepCount % 4) {
                case 0:
                    heart.setImageDrawable(getActivity().getResources().getDrawable(imageList.get(0), null));
                    break;
                case 1:
                    heart.setImageDrawable(getActivity().getResources().getDrawable(imageList.get(1), null));
                    break;
                case 2:
                    heart.setImageDrawable(getActivity().getResources().getDrawable(imageList.get(2), null));
                    break;
                case 3:
                    heart.setImageDrawable(getActivity().getResources().getDrawable(imageList.get(3), null));
                    break;
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public static void sendonchannel3(View view, String text, Integer choice) {
        if (choice == 1) {
            Intent intent = new Intent(context, Homescreen.class);
            intent.putExtra("Name1", Homescreen.USER_NAME);
            intent.putExtra("fragment", "workout");
            intent.putExtra("setup", setup);
            intent.putExtra("kcal", kcal);
            intent.putExtra("stepCount", stepCount);
            intent.setAction("");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent pauseIntent = new Intent(context, PauseReciever.class);
            PendingIntent cPauseIntent = PendingIntent.getBroadcast(context, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent playIntent = new Intent(context, PlayReciever.class);
            PendingIntent cPlayIntent = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent stopIntent = new Intent(context, PauseReciever.class);
            PendingIntent cstopIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            RemoteViews remoteViews_collpsed = new RemoteViews(activity.getPackageName(), R.layout.workout_notification_small);
            RemoteViews remoteViews_enlarged = new RemoteViews(activity.getPackageName(), R.layout.workout_naotification_large);


            Double calLost = 0.4 * stepCount * (setup + 1);

            remoteViews_collpsed.setTextViewText(R.id.infoText, "Workout: Started");
            remoteViews_enlarged.setTextViewText(R.id.note_steps, String.valueOf(stepCount));
            remoteViews_enlarged.setTextViewText(R.id.note_cals, String.format("%.2f", calLost));
            remoteViews_enlarged.setImageViewResource(R.id.note_pause, R.drawable.ic_npause);
            remoteViews_enlarged.setImageViewResource(R.id.note_play, R.drawable.ic_npaly);
            remoteViews_enlarged.setImageViewResource(R.id.note_stop, R.drawable.ic_nstop);
            remoteViews_enlarged.setOnClickPendingIntent(R.id.note_pause, cPauseIntent);
            remoteViews_enlarged.setOnClickPendingIntent(R.id.note_play, cPlayIntent);
            remoteViews_enlarged.setOnClickPendingIntent(R.id.note_stop, cstopIntent);


            Notification notification3 = new NotificationCompat.Builder(context, workout)
                    .setSmallIcon(R.drawable.ic_notif_name)
                    .setLights(Color.RED, 3000, 3000)
                    .setDefaults(Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_NO_CLEAR)
                    .setCustomContentView(remoteViews_collpsed)
                    .setOngoing(true)
                    .setCustomBigContentView(remoteViews_enlarged)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setColorized(true)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(contentIntent)
                    .setColor(Color.parseColor("#00ff00"))
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
            notificationManagerCompat.notify(4, notification3);
        } else if (choice == 2) {

            Intent intent = new Intent(context, Homescreen.class);
            intent.putExtra("Name1", Homescreen.USER_NAME);
            intent.putExtra("fragment", "Cardio");
            intent.putExtra("setup", setup);
            intent.putExtra("kcal", kcal);
            intent.putExtra("stepCount", stepCount);
            intent.setAction("");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context, workout)
                    .setSmallIcon(R.drawable.ic_notif_name)
                    .setLights(Color.RED, 3000, 3000)
                    .setDefaults(Notification.FLAG_SHOW_LIGHTS)
                    .setContentTitle("Workout Status")
                    .setContentText(text)
                    .setContentInfo(text)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setColorized(true)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(contentIntent)
                    .setColor(Color.GREEN)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManagerCompat1.notify(8, notification);
        }


    }

    public static void cancelNotifications() {
        notificationManagerCompat.cancel(4);
    }

    private ArrayList<File> readSongs(File root) {
        ArrayList<File> fileArrayList = new ArrayList<>();
        File files[] = root.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                fileArrayList.addAll(readSongs(file));
            } else {
                if (file.getName().endsWith(".mp3")) {
                    fileArrayList.add(file);
                }
            }
        }
        return fileArrayList;
    }


    public void setCompletion(String text) {
        Intent intent = new Intent(getContext(), Homescreen.class);
        intent.putExtra("Name1", Homescreen.USER_NAME);
        intent.putExtra("fragment", "Cardio");
        intent.setAction("");
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification2 = new NotificationCompat.Builder(getContext(), workout)
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

        notificationManagerCompat.notify(4, notification2);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BacktoOriginal) {
            backtoOriginal = (BacktoOriginal) context;
        } else {
            throw new ClassCastException("Error");

        }

        if (context instanceof IonBackPressed) {
            backPressed = (IonBackPressed) context;
        }else {
            throw new ClassCastException("Error");
        }
    }





    public interface BacktoOriginal {
        public void goBack(Double kcal);
    }

    public void play(Context context, Uri uri,String name) {

        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        songName.setText(String.format("%-50s",name));
        Log.d(TAG, "play: "+String.format("%22s",name));
        songName.setSelected(true);
    }

    public void setMusicList() {
        String[] proj = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA};// Can include more data for more details and check it.

        Cursor audioCursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    if (audioCursor.getString(audioIndex).endsWith(".mp3")) {
                        nameList.add(audioCursor.getString(audioIndex).replace(".mp3", ""));
                        Log.d(TAG, "setMusicList: "+audioCursor.getString(2));
                        musicUris.add(audioCursor.getString(2));
                    }

                } while (audioCursor.moveToNext());
            }
        }
        audioCursor.close();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mediaPlayer!=null){
            mediaPlayer.start();

        }
    }
    public interface IonBackPressed {
        public boolean onBackPress();
    }



}