package com.example.elysia;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.elysia.entity.Achievement;
import com.example.elysia.entity.Event;
import com.example.elysia.entity.Resource;
import com.example.elysia.entity.Task;
import com.example.elysia.entity.User;
import com.example.elysia.fragment.CalendarFragment;
import com.example.elysia.fragment.ProfileFragment;
import com.example.elysia.fragment.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity{
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private static final int LOCATION_REQUEST = 222;
    public static final String APP_PREFERENCES  = "my_settings";
    public static final String APP_PREFERENCES_AUTH = "auth";
    public static final String APP_PREFERENCES_EMAIL = "email";
    public static final String APP_PREFERENCES_NAME = "user";
    public static final String APP_PREFERENCES_THEME = "theme";
    //public static final String APP_PREFERENCES_PASSWORD = "password";
    public ArrayList<Task> taskList = new ArrayList<>();
    public ArrayList<Task> taskCompleteList = new ArrayList<>();
    public ArrayList<Event> eventList = new ArrayList<>();
    public List<Achievement> achievements = new ArrayList<>();
    public List<Resource> resources = new ArrayList<>();
    DialogFragment loadingScreen;
    public FirebaseAuth mAuth;
    public FirebaseFirestore db;
    public User user;
    public Boolean stateAuth = false;
    public SharedPreferences mSettings;
    public DataBase dataBase;
    public BottomNavigationView bottomNavigationView;
    public String themeName;
    public long idAchievement = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        themeName = mSettings.getString(APP_PREFERENCES_THEME, "Purple");
        switch (themeName){
            case ("Yellow"): setTheme(R.style.Theme_Elysia_Yellow); break;
            case ("Green"): setTheme(R.style.Theme_Elysia_Green); break;
            case ("Blue"): setTheme(R.style.Theme_Elysia_Blue); break;
            case ("Red"): setTheme(R.style.Theme_Elysia_Red); break;
            case ("Grey"): setTheme(R.style.Theme_Elysia_Grey); break;
            case ("Mint"): setTheme(R.style.Theme_Elysia_Mint); break;
            case ("Orange"): setTheme(R.style.Theme_Elysia_Orange); break;
            case ("LightBlue"): setTheme(R.style.Theme_Elysia_LigntBlue); break;
            case ("Purple"): setTheme(R.style.Theme_Elysia_Purple); break;
        }
        setContentView(R.layout.activity_main);
        checkLocationRequest();
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, 1);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //БД и аутентификация
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dataBase = DataBase.getInstance(this.getApplicationContext());
        achievements = dataBase.achievementDao().getAchievements();
        if(achievements.size()==0){
            Achievement achievement = new Achievement("Общее");
            dataBase.achievementDao().insert(achievement);
        }
        achievements = dataBase.achievementDao().getAchievements();


        //чтение данных из SharedPreference
        if (mSettings.getBoolean(APP_PREFERENCES_AUTH, false)) {
            stateAuth = true;
            String email = mSettings.getString(APP_PREFERENCES_EMAIL, "email");
            String nameUser = mSettings.getString(APP_PREFERENCES_NAME, "user");
            user = new User(email, nameUser);
        }


        loadingScreen = Loading.getInstance();
        loadingScreen.show(getSupportFragmentManager(), "loading screen");
        loadDataFromServer();

        //Навигация меню
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calendar_menu:
                        CalendarFragment calendarFragment = new CalendarFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, calendarFragment)
                                .commitAllowingStateLoss();
                        break;
                    case R.id.task_menu: {
                        TaskFragment taskFragment = new TaskFragment(fragmentManager);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, taskFragment)
                                .commitAllowingStateLoss();
                        break;
                    }
                    case R.id.profile_menu:
                        ProfileFragment profileFragment = new ProfileFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, profileFragment)
                                .commitAllowingStateLoss();
                        break;
                }
                return false;
            }
        });
    }


   /* private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // Нельзя закрыть ProgressDialog кнопкой "назад"
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }*/


    private void loadDataFromServer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                taskList = (ArrayList<Task>) dataBase.taskDao().getAllNotDone(idAchievement);
                taskCompleteList = (ArrayList<Task>) dataBase.taskDao().getAllDone(idAchievement);
                TaskFragment taskFragment = new TaskFragment(fragmentManager);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, taskFragment)
                        .commitAllowingStateLoss();
                loadingScreen.dismiss();
            }
        }, 500);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(LOCATION_REQUEST)
    private void checkLocationRequest() {
        String[] perms = {android.Manifest.permission.ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,"Please grant permission",
                    LOCATION_REQUEST, perms);
        }
    }


}