package com.example.elysia.fragment;

import static android.content.ContentValues.TAG;
import static com.example.elysia.MainActivity.APP_PREFERENCES_AUTH;
import static com.example.elysia.MainActivity.APP_PREFERENCES_NAME;
import static com.example.elysia.MainActivity.APP_PREFERENCES_THEME;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dolatkia.animatedThemeManager.AppTheme;
import com.example.elysia.MainActivity;
import com.example.elysia.R;
import com.example.elysia.themes.PurpleTheme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment  extends Fragment {

    //SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
    public EditProfileFragment() {
    }
    EditText passwordOld;
    EditText passwordNew;
    EditText nameEditText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editprofile_fragment, container, false);
        EditText emailEditText = view.findViewById(R.id.email_textView);
        nameEditText = view.findViewById(R.id.name_textView);
        passwordOld = view.findViewById(R.id.passwordOld_textView);
        passwordNew = view.findViewById(R.id.passwordNew_textView);
        emailEditText.setText(((MainActivity)getActivity()).user.getEmail());
        nameEditText.setText(((MainActivity)getActivity()).user.getName());
        View v_purple = view.findViewById(R.id.viewPurple);
        Button exitBtn = (Button) view.findViewById(R.id.exit_btn);
        Button editBtn = (Button) view.findViewById(R.id.save_btn);
        v_purple.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Purple");
                editor.apply();
                updateActivity();
            }
        });
        View v_yellow = view.findViewById(R.id.viewYellow);
        v_yellow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Yellow");
                editor.apply();
                updateActivity();
            }
        });
        View v_blue = view.findViewById(R.id.viewBlue);
        v_blue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Blue");
                editor.apply();
                updateActivity();
            }
        });
        View v_green = view.findViewById(R.id.viewGreen);
        v_green.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Green");
                editor.apply();
                updateActivity();
            }
        });
        View v_red = view.findViewById(R.id.viewRed);
        v_red.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Red");
                editor.apply();
                updateActivity();
            }
        });
        View v_grey = view.findViewById(R.id.viewGrey);
        v_grey.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Grey");
                editor.apply();
                updateActivity();
            }
        });
        View v_mint = view.findViewById(R.id.viewMint);
        v_mint.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Mint");
                editor.apply();
                updateActivity();
            }
        });
        View v_orange = view.findViewById(R.id.viewOrange);
        v_orange.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Orange");
                editor.apply();
                updateActivity();
            }
        });
        View v_lblue = view.findViewById(R.id.viewLightBlue);
        v_lblue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "LightBlue");
                editor.apply();
                updateActivity();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).mAuth.signOut();
                ((MainActivity)getActivity()).user = null;
                /*editor.putBoolean(APP_PREFERENCES_AUTH, false);
                editor.apply();*/
                ((MainActivity)getActivity()).stateAuth = false;
                Toast.makeText(getActivity(),R.string.exitFromAcc,Toast.LENGTH_SHORT).show();
                refreshProfile();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!nameEditText.getText().toString().equals(((MainActivity)getActivity()).user.getName())){
                    ((MainActivity)getActivity()).user.setName(nameEditText.getText().toString());
                    updateNameUser();
                }
                if (!passwordOld.getText().toString().equals("") && !passwordNew.getText().toString().equals("")){
                    System.out.println("НЕ сюда");
                    if (passwordOld.getText().toString().length()>6 && passwordNew.getText().toString().length()>6){
                        System.out.println("НЕ сюда");
                        if (!passwordOld.getText().toString().equals(passwordNew.getText().toString())){
                            updatePassword();
                        }
                        else Toast.makeText(getActivity(),R.string.passwordsMatch,Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getActivity(),R.string.lengthPassNotValid,Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }

    public void updateNameUser(){
        // Получение ссылки на документ в коллекции
        DocumentReference docRef = ((MainActivity)getActivity()).db.collection("User").document(((MainActivity)getActivity()).user.getId());

        // Обновление данных в документе
        docRef.update("name", ((MainActivity)getActivity()).user.getName())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Данные успешно обновлены");
                        sucsses();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),R.string.notSucssesUpdateName,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sucsses(){
        SharedPreferences.Editor editor = ((MainActivity)getActivity()).mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME, nameEditText.getText().toString());
        editor.apply();
        Toast.makeText(getActivity(),R.string.nameChangeSuc,Toast.LENGTH_SHORT).show();
        refreshProfile();
    }

    public void updatePassword(){
        FirebaseUser user = ((MainActivity)getActivity()).mAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(((MainActivity)getActivity()).user.getEmail(), passwordOld.getText().toString());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(passwordNew.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(),R.string.changePassSucsses,Toast.LENGTH_SHORT).show();
                                        refreshProfile();
                                    } else {
                                        Toast.makeText(getActivity(),R.string.changePassNoSucsses,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(),R.string.oldPassNotValid,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void updateActivity(){
        Intent i = getContext().getPackageManager().getLaunchIntentForPackage( getContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        Toast.makeText(getActivity(),R.string.changeTheme,Toast.LENGTH_SHORT).show();
    }

    public void refreshProfile(){
        ProfileFragment profileFragment = new ProfileFragment();
        ((MainActivity)getActivity()).fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .commitAllowingStateLoss();
    }
}
