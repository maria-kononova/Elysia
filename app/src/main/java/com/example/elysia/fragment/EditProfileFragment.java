package com.example.elysia.fragment;

import static android.content.ContentValues.TAG;
import static com.example.elysia.MainActivity.APP_PREFERENCES_AUTH;
import static com.example.elysia.MainActivity.APP_PREFERENCES_NAME;
import static com.example.elysia.MainActivity.APP_PREFERENCES_THEME;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.elysia.MainActivity;
import com.example.elysia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public class EditProfileFragment extends Fragment {
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
        emailEditText.setText(((MainActivity) getActivity()).user.getEmail());
        nameEditText.setText(((MainActivity) getActivity()).user.getName());
        Button exitBtn = (Button) view.findViewById(R.id.exit_btn);
        Button editBtn = (Button) view.findViewById(R.id.save_btn);
        //views для изменения темы
        //фиолетовая
        View v_purple = view.findViewById(R.id.viewPurple);
        v_purple.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Purple");
                editor.apply();
                updateActivity();
            }
        });
        //жёлтая
        View v_yellow = view.findViewById(R.id.viewYellow);
        v_yellow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Yellow");
                editor.apply();
                updateActivity();
            }
        });
        //синяя
        View v_blue = view.findViewById(R.id.viewBlue);
        v_blue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Blue");
                editor.apply();
                updateActivity();
            }
        });
        //тёмно зелёная
        View v_green = view.findViewById(R.id.viewGreen);
        v_green.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Green");
                editor.apply();
                updateActivity();
            }
        });
        //красная
        View v_red = view.findViewById(R.id.viewRed);
        v_red.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Red");
                editor.apply();
                updateActivity();
            }
        });
        //серая
        View v_grey = view.findViewById(R.id.viewGrey);
        v_grey.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Grey");
                editor.apply();
                updateActivity();
            }
        });
        //мятная
        View v_mint = view.findViewById(R.id.viewMint);
        v_mint.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Mint");
                editor.apply();
                updateActivity();
            }
        });
        //оранжевая
        View v_orange = view.findViewById(R.id.viewOrange);
        v_orange.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "Orange");
                editor.apply();
                updateActivity();
            }
        });
        //голубая
        View v_lblue = view.findViewById(R.id.viewLightBlue);
        v_lblue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                editor.putString(APP_PREFERENCES_THEME, "LightBlue");
                editor.apply();
                updateActivity();
            }
        });
        //кнопка для выхода из аккаунта
        exitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
                ((MainActivity) getActivity()).mAuth.signOut();
                ((MainActivity) getActivity()).user = null;
                editor.putBoolean(APP_PREFERENCES_AUTH, false);
                editor.apply();
                ((MainActivity) getActivity()).stateAuth = false;
                Toast.makeText(getActivity(), R.string.exitFromAcc, Toast.LENGTH_SHORT).show();
                refreshProfile();
            }
        });
        //кнопка для сохранения изменений в даннных пользователя

        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!nameEditText.getText().toString().equals(((MainActivity) getActivity()).user.getName())) {
                    ((MainActivity) getActivity()).user.setName(nameEditText.getText().toString());
                    updateNameUser();
                }
                if (!passwordOld.getText().toString().equals("") && !passwordNew.getText().toString().equals("")) {
                    System.out.println("НЕ сюда");
                    if (passwordOld.getText().toString().length() > 6 && passwordNew.getText().toString().length() > 6) {
                        System.out.println("НЕ сюда");
                        if (!passwordOld.getText().toString().equals(passwordNew.getText().toString())) {
                            updatePassword();
                        } else
                            Toast.makeText(getActivity(), R.string.passwordsMatch, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), R.string.lengthPassNotValid, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    //обновление имени пользователя на сервере
    public void updateNameUser() {
        // Получение ссылки на документ в коллекции
        DocumentReference docRef = ((MainActivity) getActivity()).db.collection("User").document(((MainActivity) getActivity()).user.getId());

        // Обновление данных в документе
        docRef.update("name", ((MainActivity) getActivity()).user.getName())
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
                        Toast.makeText(getActivity(), R.string.notSucssesUpdateName, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //обратботка успешного ответа от сервера при обновлении имени
    public void sucsses() {
        SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME, nameEditText.getText().toString());
        editor.apply();
        Toast.makeText(getActivity(), R.string.nameChangeSuc, Toast.LENGTH_SHORT).show();
        refreshProfile();
    }
    //функция обновления пароля пользователя
    public void updatePassword() {
        FirebaseUser user = ((MainActivity) getActivity()).mAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(((MainActivity) getActivity()).user.getEmail(), passwordOld.getText().toString());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(passwordNew.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), R.string.changePassSucsses, Toast.LENGTH_SHORT).show();
                                        refreshProfile();
                                    } else {
                                        Toast.makeText(getActivity(), R.string.changePassNoSucsses, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), R.string.oldPassNotValid, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    //перезапуск приложения для изменения его темы
    public void updateActivity() {
        Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        Toast.makeText(getActivity(), R.string.changeTheme, Toast.LENGTH_SHORT).show();
    }
    //обновление фрагмента профиля пользователя
    public void refreshProfile() {
        ProfileFragment profileFragment = new ProfileFragment();
        ((MainActivity) getActivity()).fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .commitAllowingStateLoss();
    }
}
