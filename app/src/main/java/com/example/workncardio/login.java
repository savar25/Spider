package com.example.workncardio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class login extends AppCompatActivity {

    UserDets userDets;
    SharedPreferences sPName, spId,spLog;
    SharedPreferences.Editor editor;
    EditText name, EmailID;
    CheckBox save,log;
    Button logBtn;
    private int CAMERA = 1;
    private int STORAGE = 2;
    private int PHYSICAL = 3;
    private static final String TAG = "login";
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");
    ArrayList<UserInfo> userInfos=new ArrayList<>();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balck);
        checkPermission();

        sPName = this.getPreferences(Context.MODE_PRIVATE);
        spId = this.getPreferences(Context.MODE_PRIVATE);
        spLog=this.getPreferences(Context.MODE_PRIVATE);
        if(spLog.getInt("RFogd",2)==2 || getIntent().getIntExtra("logVal",2)==0) {
            editor = spLog.edit();
            editor.putInt("RFogd", 0);
            editor.commit();
        }

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               if(progressBar.getVisibility()==View.VISIBLE){
                   Toast.makeText(login.this, "Erratic Internet Connection", Toast.LENGTH_SHORT).show();
               }
           }
       },12000);

        Log.d(TAG, "onCreate: " + sPName.getString("Name", "") + spId.getString("ID", ""));



    }

    public void checkPermission() {
        if(Build.VERSION.SDK_INT<=28) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) +
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission Needed")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(login.this, new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,}, CAMERA);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                    System.exit(0);
                                }
                            }).create().show();
                } else {
                    ActivityCompat.requestPermissions(login.this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,}, CAMERA);
                }
            }
        }else{
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) +
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                    ContextCompat.checkSelfPermission(this,Manifest.permission.ACTIVITY_RECOGNITION)== PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Permissions Needed")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(login.this, new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.ACTIVITY_RECOGNITION}, CAMERA);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                    System.exit(0);
                                }
                            }).create().show();
                } else {
                    ActivityCompat.requestPermissions(login.this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACTIVITY_RECOGNITION}, CAMERA);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(Build.VERSION.SDK_INT<=28) {
            if (requestCode == CAMERA) {
                if (grantResults.length > 0 && grantResults[0] + grantResults[1] + grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }else {
            if (requestCode == CAMERA) {
                if (grantResults.length > 0 && grantResults[0] + grantResults[1] + grantResults[2] + grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }

    public boolean existence(String name,String eid){
        for(UserInfo info:userInfos){
            if(info.getName().equals(name) && info.geteID().equals(eid)){
                return  true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String id2=snapshot1.getKey();
                    UserInfo info=snapshot1.getValue(UserInfo.class);
                    userInfos.add(info);
                    Log.d(TAG, "onDataChange: "+userInfos.size());
                }
                set();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void set(){


        progressBar.setVisibility(View.GONE);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: "+userInfos);

        name = findViewById(R.id.Uname);
        EmailID = findViewById(R.id.UEmID);
        save = findViewById(R.id.checkBox);
        log=findViewById(R.id.login);
        logBtn = findViewById(R.id.LOGIN);
        if (sPName.getString("Name", "").equals("") && spId.getString("ID", "").equals("") && spLog.getInt("RFogd",2)==0) {


            logBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (save.isChecked()) {
                        editor = sPName.edit();
                        editor.putString("Name", name.getText().toString());
                        Log.d(TAG, "onClick: " + name.getText().toString());
                        editor.commit();
                        editor = spId.edit();
                        editor.putString("ID", EmailID.getText().toString());
                        Log.d(TAG, "onClick: " + EmailID.getText().toString());
                        editor.commit();
                        Log.d(TAG, "onClick: " + sPName.getString("Name", "") + spId.getString("ID", ""));
                        if (existence(name.getText().toString(), EmailID.getText().toString())) {
                            String nameAhead = name.getText().toString();
                            if(log.isChecked()) {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 1);
                                editor.commit();
                            }else {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 0);
                                editor.commit();
                            }
                            Intent intent = new Intent(login.this, Homescreen.class).setAction("");
                            intent.putExtra("Name1", nameAhead);
                            intent.putExtra("fragment", "def");
                            startActivity(intent);
                        } else {
                            AlertDialog builder = new AlertDialog.Builder(login.this)
                                    .setTitle("User Absent")
                                    .setMessage("Do you want to create a new account?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(login.this, initValues.class).setAction("");
                                            if(log.isChecked()) {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 1);
                                                editor.commit();
                                            }else {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 0);
                                                editor.commit();
                                            }
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editor.clear();
                                            editor.apply();
                                            finish();
                                            System.exit(0);
                                        }
                                    }).create();

                            builder.show();

                        }

                    } else {
                        if (existence(name.getText().toString(), EmailID.getText().toString())) {
                            String nameAhead = name.getText().toString();
                            if(log.isChecked()) {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 1);
                                editor.commit();
                            }else {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 0);
                                editor.commit();
                            }
                            Intent intent = new Intent(login.this, Homescreen.class).setAction("");
                            intent.putExtra("Name1", nameAhead);
                            intent.putExtra("fragment", "def");
                            startActivity(intent);
                        } else {
                            AlertDialog builder = new AlertDialog.Builder(login.this)
                                    .setTitle("User Absent")
                                    .setMessage("Do you want to create a new account?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(login.this, initValues.class).setAction("");
                                            if(log.isChecked()) {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 1);
                                                editor.commit();
                                            }else {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 0);
                                                editor.commit();
                                            }
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            System.exit(0);
                                        }
                                    }).create();

                            builder.show();


                        }
                        Toast.makeText(login.this, "Saving data mandatory for all features", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if(spLog.getInt("RFogd",2)==1) {
            name.setText(sPName.getString("Name", "1"));
            EmailID.setText(spId.getString("ID", "2"));
            if (existence(name.getText().toString(), EmailID.getText().toString())) {
                String nameAhead = sPName.getString("Name", "");
                Intent intent = new Intent(login.this, Homescreen.class).setAction("");
                intent.putExtra("Name1", nameAhead);
                intent.putExtra("fragment", "def");
                Log.d(TAG, "onClick: " + nameAhead);
                startActivity(intent);
            }
        } else{
            name.setText(sPName.getString("Name", "1"));
            EmailID.setText(spId.getString("ID", "2"));



            logBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (save.isChecked()) {
                        editor = sPName.edit();
                        editor.putString("Name", name.getText().toString());
                        editor.commit();
                        editor = spId.edit();
                        editor.putString("ID", EmailID.getText().toString());
                        editor.commit();
                        if (existence(name.getText().toString(), EmailID.getText().toString())) {
                            String nameAhead = sPName.getString("Name", "");
                            if(log.isChecked()) {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 1);
                                editor.commit();
                            }else {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 0);
                                editor.commit();
                            }
                            Intent intent = new Intent(login.this, Homescreen.class).setAction("");
                            intent.putExtra("Name1", nameAhead);
                            intent.putExtra("fragment", "def");
                            Log.d(TAG, "onClick: " + nameAhead);
                            startActivity(intent);
                        } else {

                            AlertDialog builder = new AlertDialog.Builder(login.this)
                                    .setTitle("User Absent")
                                    .setMessage("Do you want to create a new account?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(login.this, initValues.class).setAction("");
                                            if(log.isChecked()) {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 1);
                                                editor.commit();
                                            }else {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 0);
                                                editor.commit();
                                            }
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editor.clear();
                                            editor.apply();
                                            finish();
                                            System.exit(0);
                                        }
                                    }).create();

                            builder.show();

                        }
                    } else {
                        if (existence(name.getText().toString(), EmailID.getText().toString())) {
                            String nameAhead = name.getText().toString();
                            if(log.isChecked()) {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 1);
                                editor.commit();
                            }else {
                                editor = spLog.edit();
                                editor.putInt("RFogd", 0);
                                editor.commit();
                            }
                            Intent intent = new Intent(login.this, Homescreen.class).setAction("");
                            intent.putExtra("Name1", nameAhead);
                            intent.putExtra("fragment", "def");
                            startActivity(intent);
                        } else {
                            AlertDialog builder = new AlertDialog.Builder(login.this)
                                    .setTitle("User Absent")
                                    .setMessage("Do you want to create a new account?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(login.this, initValues.class).setAction("");
                                            if(log.isChecked()) {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 1);
                                                editor.commit();
                                            }else {
                                                editor = spLog.edit();
                                                editor.putInt("RFogd", 0);
                                                editor.commit();
                                            }
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editor.clear();
                                            editor.apply();
                                            finish();
                                            System.exit(0);
                                        }
                                    }).create();

                            builder.show();


                        }
                        Toast.makeText(login.this, "Saving data mandatory for all features", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}