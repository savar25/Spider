package com.example.workncardio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.example.workncardio.dummy.ColourItem;
import com.example.workncardio.dummy.ProfileItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Homescreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.NavigListener, cardio_cal_fragment.ButtonListener, workoutFragment.BacktoOriginal, calorie_foodFragment.modif, workoutFragment.IonBackPressed {


    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int IMAGE_PICK = 1002;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    FragmentManager manager;
    FragmentTransaction transaction;
    public static String USER_NAME;
    public static Integer setup;
    Double kcal;
    TextView prof, name, id;
    UserDets userDets;
    ImageView cam_image, colour;
    ImageButton camBtn;
    Uri image_uri;
    SharedPreferences picPref;
    SharedPreferences.Editor editor;
    Bitmap thumbnail;
    OutputStream outputStream;
    ConstraintLayout nav_layout;
    Integer[] integers = new Integer[]{
            R.drawable.ic_box1,
            R.drawable.ic_box2,
            R.drawable.ic_box3,
            R.drawable.ic_box4
    };

    String[] strings = new String[]{
            "Grey",
            "Blue",
            "Orange",
            "Green"
    };

    private static final String TAG = "Homescreen";
    private int request_code = 100;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference("Users");
    UserInfo baseInfo;
    Intent intent;
    String cho = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        setTitle("Home");


        userDets = new UserDets(this);
        if (workoutFragment.mediaPlayer != null) {
            workoutFragment.mediaPlayer.stop();
        }


        intent = getIntent();
        USER_NAME = intent.getStringExtra("Name1");
        Log.d(TAG, "onCreate: " + USER_NAME);


        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(Homescreen.this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, new ProfileFragment());
        transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
        transaction.commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, new stepFragment()).setCustomAnimations(R.anim.right_entry, R.anim.right_exit).commit();
        drawerLayout.closeDrawer(GravityCompat.START);


        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);

        prof = header.findViewById(R.id.prof_icon);
        name = header.findViewById(R.id.nav_name);
        id = header.findViewById(R.id.nav_id);
        cam_image = header.findViewById(R.id.cam_image);
        camBtn = header.findViewById(R.id.camButton);

        nav_layout = header.findViewById(R.id.nav_header_layout);
        colour = header.findViewById(R.id.backGround);




        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<ProfileItem> profileItems = new ArrayList<>();
                if (!baseInfo.isPic()) {
                    profileItems.add(new ProfileItem("From Camera", R.drawable.ic_menu_camera));
                    profileItems.add(new ProfileItem("From Gallery", R.drawable.ic_menu_gallery));
                } else {
                    profileItems.add(new ProfileItem("From Camera", R.drawable.ic_menu_camera));
                    profileItems.add(new ProfileItem("From Gallery", R.drawable.ic_menu_gallery));
                    profileItems.add(new ProfileItem("Remove Profile Picture", R.drawable.ic_cross));
                }


                ListAdapter adapter = new ArrayAdapter<ProfileItem>(Homescreen.this, android.R.layout.select_dialog_item, android.R.id.text1, profileItems) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        //Use super class to create the View
                        View v = super.getView(position, convertView, parent);
                        TextView tv = (TextView) v.findViewById(android.R.id.text1);

                        //Put the image on the TextView
                        tv.setCompoundDrawablesWithIntrinsicBounds(profileItems.get(position).getIcon(), 0, 0, 0);
                        tv.setText(profileItems.get(position).getText());
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                        //Add margin between image and text (support various screen densities)
                        int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                        tv.setCompoundDrawablePadding(dp5);

                        return v;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Homescreen.this);
                builder.setTitle("Set Profile Image");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent photoClick = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(photoClick, IMAGE_CAPTURE_CODE);
                                break;

                            case 1:
                                Intent photoPick = new Intent(Intent.ACTION_PICK);
                                photoPick.setType("image/*");
                                startActivityForResult(photoPick, IMAGE_PICK);
                                break;
                            case 2:
                                baseInfo.setPic(false);
                                reference.child(baseInfo.getID()).setValue(baseInfo);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                checkProfImage();
                        }
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<ProfileItem> profileItems = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    profileItems.add(new ProfileItem(strings[i], integers[i]));
                }


                ListAdapter adapter = new ArrayAdapter<ProfileItem>(Homescreen.this, android.R.layout.select_dialog_item, android.R.id.text1, profileItems) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        //Use super class to create the View
                        View v = super.getView(position, convertView, parent);
                        TextView tv = (TextView) v.findViewById(android.R.id.text1);

                        //Put the image on the TextView
                        tv.setCompoundDrawablesWithIntrinsicBounds(profileItems.get(position).getIcon(), 0, 0, 0);
                        tv.setText(profileItems.get(position).getText());
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                        //Add margin between image and text (support various screen densities)
                        int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                        tv.setCompoundDrawablePadding(dp5);

                        return v;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Homescreen.this);
                builder.setTitle("Set Profile Image");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                nav_layout.setBackgroundColor(Color.parseColor("#DDDDDD"));
                                break;
                            case 1:
                                nav_layout.setBackgroundColor(Color.parseColor("#8aebdd"));
                                break;
                            case 2:
                                nav_layout.setBackgroundColor(Color.parseColor("#DD973e"));
                                break;
                            case 3:
                                nav_layout.setBackgroundColor(Color.parseColor("#32dd24"));
                                break;
                        }
                        baseInfo.setBackground(i);
                        reference.child(baseInfo.getID()).setValue(baseInfo);
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (workoutFragment.mediaPlayer != null) {
            workoutFragment.mediaPlayer.stop();
            workoutFragment.cancelNotifications();
            workoutFragment.sendonchannel3(new View(this), "Workout Stopped", 2);
        }
        switch (item.getItemId()) {
            case R.id.drawer_profile:
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new ProfileFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, new stepFragment()).setCustomAnimations(R.anim.right_entry, R.anim.right_exit).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawer_calorie:
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new CalorieFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Calorie Charts");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawer_screen:
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new ScreenFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Manage Sleep");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.cardio:
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new CardioFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Cardio");
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().add(R.id.cardio_container, new cardio_cal_fragment()).setCustomAnimations(R.anim.right_entry, R.anim.right_exit).commit();
                break;

            case R.id.logout:
                Intent intent = new Intent(this, login.class);
                intent.putExtra("logVal", 0);
                Intent intent1 = new Intent(this, StepService.class);
                stopService(intent1);
                startActivity(intent);
        }


        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void onItemSelected(int ID) {
        switch (ID) {
            case R.id.steps:
                getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, new stepFragment()).setCustomAnimations(R.anim.left_entry, R.anim.right_exit).commit();
                break;
            case R.id.analysis:
                getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, new analysisFragment()).commit();
                break;
            case R.id.goal:
                getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, new goalChange()).setCustomAnimations(R.anim.right_entry, R.anim.right_exit).commit();
                break;

        }
    }


    @Override
    public void onPress(int choice, Double kcal) {
        getSupportFragmentManager().beginTransaction().replace(R.id.cardio_container, new workoutFragment(choice, kcal, 0)).setCustomAnimations(R.anim.right_entry, R.anim.right_exit).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri image_uri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(image_uri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    cam_image.setImageBitmap(selectedImage);
                    setImage();
                    checkProfImage();
                } catch (FileNotFoundException f) {
                    f.printStackTrace();
                }
            }
        } else if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == RESULT_OK) {
                onCaptureImageResult(data);
                Log.d(TAG, "onActivityResult: " + data.getData());
                checkProfImage();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    public void onCaptureImageResult(Intent intent) {
        thumbnail = (Bitmap) intent.getExtras().get("data");

        cam_image.setImageBitmap(thumbnail);
        setImage();
    }

    public void checkProfImage() {
        if (!baseInfo.isPic()) {
            prof.setVisibility(View.VISIBLE);
            cam_image.setVisibility(View.GONE);
        } else if (baseInfo.isPic()) {
            prof.setVisibility(View.GONE);
            cam_image.setVisibility(View.VISIBLE);
            setProfilePic();
        }
    }


    public void setImage() {
        Log.d(TAG, "setImage: called");
        BitmapDrawable drawable = (BitmapDrawable) cam_image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(getFilesDir(), "Profile");


        if (dir.exists() && dir.isDirectory()) {
            Log.d(TAG, "setImage: " + dir.exists());
            File imageFile = new File(dir, "profile.jpg");
            try {
                outputStream = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            baseInfo.setPic(true);
            reference.child(baseInfo.getID()).setValue(baseInfo);

            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void setProfilePic() {
        File file = new File(getFilesDir(), "Profile");
        File main = new File(file, "profile.jpg");

        try {
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(main));
            cam_image.setImageBitmap(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setBack() {
        switch (baseInfo.getBackground()) {
            case 0:
                nav_layout.setBackgroundColor(Color.parseColor("#DDDDDD"));
                break;
            case 1:
                nav_layout.setBackgroundColor(Color.parseColor("#8aebdd"));
                break;
            case 2:
                nav_layout.setBackgroundColor(Color.parseColor("#DD973e"));
                break;
            case 3:
                nav_layout.setBackgroundColor(Color.parseColor("#32dd24"));
                break;
        }
    }


    @Override
    public void goBack(Double kcal) {
        getSupportFragmentManager().beginTransaction().replace(R.id.cardio_container, new cardio_cal_fragment()).setCustomAnimations(R.anim.right_entry, R.anim.right_exit).commit();
    }

    public void setShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = (ShortcutManager) getSystemService(Context.SHORTCUT_SERVICE);

            final Intent intent1 = new Intent(this, Homescreen.class);
            intent1.putExtra("Name1", Homescreen.USER_NAME);
            intent1.setAction("step");

            final Intent intent2 = new Intent(this, Homescreen.class);
            intent2.putExtra("Name1", Homescreen.USER_NAME);
            intent2.setAction("cardio");

            final Intent intent3 = new Intent(this, Homescreen.class);
            intent3.putExtra("Name1", Homescreen.USER_NAME);
            intent3.setAction("analysis");


            ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "STEPS")
                    .setShortLabel("Get Steps")
                    .setLongLabel("Get Today's Steps")
                    .setIcon(Icon.createWithResource(this, R.drawable.step_shortcut_icon_blue))
                    .setIntent(intent1)
                    .build();

            ShortcutInfo shortcut2 = new ShortcutInfo.Builder(this, "ANALYSIS")
                    .setShortLabel("Get Analysis")
                    .setLongLabel("Get Net Analysis")
                    .setIcon(Icon.createWithResource(this, R.drawable.analysis_blue))
                    .setIntent(intent3)
                    .build();

            ShortcutInfo shortcut3 = new ShortcutInfo.Builder(this, "CARDIO")
                    .setShortLabel("Go Cardio")
                    .setLongLabel("Go Cardio")
                    .setIcon(Icon.createWithResource(this, R.drawable.heart1_blue))
                    .setIntent(intent2)
                    .build();

            List<ShortcutInfo> shortcutInfoList = new ArrayList<>();
            shortcutInfoList.add(shortcut1);
            shortcutInfoList.add(shortcut2);
            shortcutInfoList.add(shortcut3);


            shortcutManager.addDynamicShortcuts(shortcutInfoList);


        }
    }

    public void handleAction(String action) {
        switch (action) {
            case "step":
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new ProfileFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, new stepFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case "cardio":
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new CardioFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Cardio");
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().add(R.id.cardio_container, new cardio_cal_fragment()).commit();
                break;
            case "analysis":
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new ProfileFragment())
                        .setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, new analysisFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);


        }
    }


    @Override
    public void onModify() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CalorieFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.container, new version());
                setTitle("Version");
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                break;
            case R.id.update:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new modif_fragment()).commit();
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (intent.getStringExtra("fragment") != null) {
            cho = intent.getStringExtra("fragment");
        } else {
            cho = "";
        }
        kcal = intent.getDoubleExtra("kcal", 0.0);
        setup = intent.getIntExtra("setup", 0);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserInfo info = snapshot1.getValue(UserInfo.class);
                    if (info.getName().equals(USER_NAME)) {
                        baseInfo = info;
                        Log.d(TAG, "onCreate: " + baseInfo);
                        Log.d(TAG, "onDataChange: called");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setupMain();
                            }
                        }, 2000);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Intent intent = new Intent(this, StepService.class);
        stopService(intent);
        Log.d(TAG, "onCreate: intent");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

    }

    public void setupMain() {
        prof.setText(USER_NAME.substring(0, 1));
        name.setText(String.valueOf(USER_NAME));
        id.setText(String.valueOf(baseInfo.geteID()));

        checkProfImage();
        setBack();


        Log.d(TAG, "onCreate: " + USER_NAME);


        switch (cho) {
            case "workout":
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.add(R.id.container, new CardioFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Workout");
                getSupportFragmentManager().beginTransaction().add(R.id.cardio_container, new workoutFragment(setup, kcal, intent.getIntExtra("stepCount", 0))).commit();
                break;
            case "def":
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.add(R.id.container, new ProfileFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                getSupportFragmentManager().beginTransaction().add(R.id.profile_container, new stepFragment()).commit();
                break;
            case "Cardio":
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.add(R.id.container, new CardioFragment());
                transaction.setCustomAnimations(R.anim.right_entry, R.anim.right_exit);
                transaction.commit();
                setTitle("Cardio");
                getSupportFragmentManager().beginTransaction().add(R.id.cardio_container, new cardio_cal_fragment()).commit();
                break;

        }
        setShortcut();
        handleAction(intent.getAction());

    }


    @Override
    public boolean onBackPress() {
        Log.d(TAG, "onBackPress: called");
        getSupportFragmentManager().beginTransaction().replace(R.id.cardio_container, new cardio_cal_fragment()).commit();
        return true;
    }
}