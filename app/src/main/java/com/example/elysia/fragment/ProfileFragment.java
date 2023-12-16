package com.example.elysia.fragment;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.elysia.MainActivity;
import com.example.elysia.R;
import com.example.elysia.entity.Achievement;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    Boolean stateEditFragment = true;
    PieChart pieChart;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        TextView userName = view.findViewById(R.id.userNameTextView);
        TextView clickTo = (TextView) view.findViewById(R.id.clickTo);
        TextView completeTaskTextView = view.findViewById(R.id.completeTaskTextView);
        TextView noCompleteTaskTextView = view.findViewById(R.id.noCompleteTaskTextView);
        completeTaskTextView.setText(String.valueOf(((MainActivity) getActivity()).dataBase.taskDao().getCompleteTask()));
        noCompleteTaskTextView.setText(String.valueOf(((MainActivity) getActivity()).dataBase.taskDao().getNoCompleteTask()));
        View fragmentForEdit = view.findViewById(R.id.editProfile_fragment);
        pieChart = view.findViewById(R.id.piechart);

        //загрузка данных пользователя, если он зарегистрирован
        if (((MainActivity) getActivity()).stateAuth) {
            clickTo.setText(R.string.clToUpdate);
            userName.setText(((MainActivity) getActivity()).user.getName());
            FragmentManager fragmentManager1 = ((MainActivity) getActivity()).getSupportFragmentManager();
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            fragmentManager1.beginTransaction()
                    .replace(R.id.editProfile_fragment, editProfileFragment)
                    .commit();
        }

        CardView profileCardView = (CardView) view.findViewById(R.id.profileCardView);
        //открытие и скрытие фрагмента с изменением данных пользователя
        profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((MainActivity) getActivity()).stateAuth) {
                    if (stateEditFragment) {
                        fragmentForEdit.setVisibility(View.VISIBLE);
                        stateEditFragment = false;
                    } else {
                        fragmentForEdit.setVisibility(View.GONE);
                        stateEditFragment = true;
                    }
                } else {
                    AuthenticationFragment authenticationFragment = new AuthenticationFragment();
                    ((MainActivity) getActivity()).fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, authenticationFragment)
                            .commit();
                }
            }
        });

        Spinner spinner = view.findViewById(R.id.spinner1);
        List<String> data = new ArrayList<>();
        int idSelectedAchievement = 0;
        for (Achievement achievement : ((MainActivity) getActivity()).achievements) {
            data.add(achievement.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, data);
        spinner.setAdapter(adapter);
        spinner.setSelection(idSelectedAchievement);
        //обработка события выбора элемента из списка спиннера
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Achievement newAch = ((MainActivity) getActivity()).achievements.get(position);
                PieChart pieChart = view.findViewById(R.id.piechart);
                ArrayList NoOftask = new ArrayList();
                NoOftask.add(new Entry(((MainActivity) getActivity()).dataBase.taskDao().getAllDone(newAch.getId()).size(), 0));
                NoOftask.add(new Entry(((MainActivity) getActivity()).dataBase.taskDao().getAllNotDone(newAch.getId()).size(), 1));
                PieDataSet dataSet = new PieDataSet(NoOftask, "");
                ArrayList labels = new ArrayList();
                labels.add("Сделано");
                labels.add("Осталось");
                PieData dataPie = new PieData(labels, dataSet);
                pieChart.setData(dataPie);
                //установка цветов диаграммы в зависимости от темы приложения
                switch (((MainActivity) getActivity()).themeName) {
                    case ("Yellow"): {
                        dataSet.setColors(new int[]{R.color.yellow_dark, R.color.grey_yellow_light}, getContext());
                        break;
                    }
                    case ("Green"): {
                        dataSet.setColors(new int[]{R.color.green_dark, R.color.grey_green_light}, getContext());
                        break;
                    }
                    case ("Blue"): {
                        dataSet.setColors(new int[]{R.color.blue_dark, R.color.grey_blue_light}, getContext());
                        break;
                    }
                    case ("Red"): {
                        dataSet.setColors(new int[]{R.color.red_dark, R.color.grey_red_light}, getContext());
                        break;
                    }
                    case ("Grey"): {
                        dataSet.setColors(new int[]{R.color.grey_dark, R.color.grey_light}, getContext());
                        break;
                    }
                    case ("Mint"): {
                        dataSet.setColors(new int[]{R.color.mint_dark, R.color.mint_light}, getContext());
                        break;
                    }
                    case ("Orange"): {
                        dataSet.setColors(new int[]{R.color.orange_dark, R.color.orange_light}, getContext());
                        break;
                    }
                    case ("LightBlue"): {
                        dataSet.setColors(new int[]{R.color.lblue_dark, R.color.lblue_light}, getContext());
                        break;
                    }
                    case ("Purple"): {
                        dataSet.setColors(new int[]{R.color.purple_dark, R.color.grey_purple_light}, getContext());
                        break;
                    }
                }
                pieChart.setUsePercentValues(true);
                pieChart.getLegend().setTextSize(14);
                pieChart.animateXY(500, 500);
                pieChart.setDescription("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }
}
