package com.example.elysia.fragment;

import static com.example.elysia.MainActivity.APP_PREFERENCES_AUTH;
import static com.example.elysia.MainActivity.APP_PREFERENCES_EMAIL;
import static com.example.elysia.MainActivity.APP_PREFERENCES_ID;
import static com.example.elysia.MainActivity.APP_PREFERENCES_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.elysia.Loading;
import com.example.elysia.MainActivity;
import com.example.elysia.R;
import com.example.elysia.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationFragment extends Fragment {
    boolean state = true;
    EditText emailTextView;
    EditText nameTextView;
    EditText passwordTextView;
    EditText passwordTextView2;
    CheckBox checkBox;

    public AuthenticationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authentication_fragment, container, false);
        Button registrationBtn = view.findViewById(R.id.registration_btn);
        Button enterBtn = view.findViewById(R.id.enter_btn);
        emailTextView = view.findViewById(R.id.email_textView);
        nameTextView = view.findViewById(R.id.name_textView);
        passwordTextView = view.findViewById(R.id.password_textView);
        passwordTextView2 = view.findViewById(R.id.password2_textView);
        checkBox = view.findViewById(R.id.checkBox);
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(view);
            }
        });
        enterBtn.setOnClickListener(new View.OnClickListener() {
            String emailPattern = "[a-z0-9]+@[a-z0-9]+\\.+[a-z]{1,3}+";

            @Override
            public void onClick(View v) {
                //авторизация
                if (state) {
                    if (!emailTextView.getText().toString().equals("") && emailTextView.getText().toString().matches(emailPattern)) {
                        if (passwordTextView.getText().toString().length() >= 6) {
                            signIn();
                        } else
                            Toast.makeText(getActivity(), R.string.notValidPassword, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), R.string.notValidEmail, Toast.LENGTH_SHORT).show();
                    //регистрация
                } else {
                    if (!emailTextView.getText().toString().equals("") && emailTextView.getText().toString().matches(emailPattern)) {
                        if (passwordTextView.getText().toString().length() >= 6) {
                            if (passwordTextView.getText().toString().equals(passwordTextView2.getText().toString())) {
                                if (!nameTextView.getText().toString().equals("")) {
                                    createAccount();
                                } else
                                    Toast.makeText(getActivity(), R.string.notValidogin, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity(), R.string.notMatchPassword, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), R.string.notValidPassword, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), R.string.notValidEmail, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    //изменение интерфейса фрагмента при нажатии на кнопку (авторизация/регистрация)
    public void registration(View view) {
        LinearLayout registrationLinerLayout = view.findViewById(R.id.registration_linerLayout);
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        Button enterBtn = view.findViewById(R.id.enter_btn);
        Button registrationBtn = view.findViewById(R.id.registration_btn);
        if (state) {
            this.state = false;
            registrationLinerLayout.setVisibility(LinearLayout.VISIBLE);
            titleTextView.setText(getResources().getString(R.string.registration));
            enterBtn.setText(getResources().getString(R.string.singUp));
            registrationBtn.setText(getResources().getString(R.string.authorization));

        } else {
            this.state = true;
            registrationLinerLayout.setVisibility(LinearLayout.GONE);
            titleTextView.setText(getResources().getString(R.string.authorization));
            enterBtn.setText(getResources().getString(R.string.singIn));
            registrationBtn.setText(getResources().getString(R.string.registration));
        }
    }

    //авторизация пользователя
    public void signIn() {
        loadingScreen();
        // [START sign_in_with_email]
        ((MainActivity) getActivity()).mAuth.signInWithEmailAndPassword(emailTextView.getText().toString(), passwordTextView.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithEmail:success");
                            FirebaseUser user = ((MainActivity) getActivity()).mAuth.getCurrentUser();
                            updateUI(user);
                            readDate();

                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println(task.getException());
                            Toast.makeText(getActivity().getApplicationContext(), "Авторизация не успешна",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            ((MainActivity) getActivity()).loadingScreen.dismiss();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    //чтение данных пользователя из fireBase при авторизации
    public void readDate() {
        ((MainActivity) getActivity()).db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                if (emailTextView.getText().toString().equals(document.getData().get("email"))) {
                                    ((MainActivity) getActivity()).user = new User(document.getId(), document.getData().get("email").toString(), document.getData().get("name").toString());
                                    sucsess();
                                    ((MainActivity) getActivity()).stateAuth = true;
                                    break;
                                }
                            }
                        } else {
                            System.out.println(task.getException());
                        }
                    }
                });
    }

    //создание аккаунта при регистрации
    private void createAccount() {
        loadingScreen();
        // [START create_user_with_email]
        ((MainActivity) getActivity()).mAuth.createUserWithEmailAndPassword(emailTextView.getText().toString(), passwordTextView.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("createUserWithEmail:success");
                            FirebaseUser user = ((MainActivity) getActivity()).mAuth.getCurrentUser();
                            //state = true;
                            updateUI(user);
                            saveUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println(task.getException());
                            System.out.println("Регистрация не успешна. Проверьте данные");
                            updateUI(null);
                            ((MainActivity) getActivity()).loadingScreen.dismiss();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    //сохранение нового пользователя при регистрации
    public void saveUser() {
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", emailTextView.getText().toString());
        user.put("name", nameTextView.getText().toString());

        // Add a new document with a generated ID
        ((MainActivity) getActivity()).db.collection("User")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                        readDate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });
    }

    //функция вызова экрана загрузки
    public void loadingScreen() {
        ((MainActivity) getActivity()).loadingScreen = Loading.getInstance();
        ((MainActivity) getActivity()).loadingScreen.show(((MainActivity) getActivity()).getSupportFragmentManager(), "loading screen");
    }

    private void updateUI(FirebaseUser user) {

    }

    private void reload() {
    }

    //обработка успешного ответа firebase
    public void sucsess() {
        System.out.println("Запись в настройки");
        if (checkBox.isChecked()) {
            SharedPreferences.Editor editor = ((MainActivity) getActivity()).mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_AUTH, true);
            editor.apply();
            editor.putString(APP_PREFERENCES_EMAIL, ((MainActivity) getActivity()).user.getEmail());
            editor.apply();
            editor.putString(APP_PREFERENCES_NAME, ((MainActivity) getActivity()).user.getName());
            editor.apply();
            editor.putString(APP_PREFERENCES_ID, ((MainActivity) getActivity()).user.getId());
            editor.apply();
        }
        ProfileFragment profileFragment = new ProfileFragment();
        ((MainActivity) getActivity()).fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .commitAllowingStateLoss();
        ((MainActivity) getActivity()).loadingScreen.dismiss();
    }
}


