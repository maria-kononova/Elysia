package com.example.elysia.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elysia.MainActivity;
import com.example.elysia.R;
import com.example.elysia.adapter.AchievementAdapter;
import com.example.elysia.entity.Achievement;
import com.example.elysia.entity.Task;
import com.example.elysia.adapter.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskFragment extends Fragment {
    FragmentManager fragmentManager;

    public TaskFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    TaskAdapter taskAdapter;
    TaskAdapter taskCompleteAdapter;
    AchievementAdapter achievementAdapter;
    RecyclerView taskRecyclerView;
    RecyclerView taskCompleteRecyclerView;
    RecyclerView achievementRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment, container, false);
        ImageButton taskButton = (ImageButton) view.findViewById(R.id.taskButton);
        ImageButton taskCompleteButton = (ImageButton) view.findViewById(R.id.taskCompleteButton);
        CardView achievementAddButton = view.findViewById(R.id.addAchButton);
        taskRecyclerView = view.findViewById(R.id.task_list);
        taskCompleteRecyclerView = view.findViewById(R.id.taskComplete_list);
        achievementRecyclerView = view.findViewById(R.id.achievementRV);
        achievementAddButton.setOnClickListener(new View.OnClickListener() {
            //диалог добавления цели
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_add_achievement, null);
                builder.setView(dialogView);

                final EditText titleEditText = dialogView.findViewById(R.id.ach_title_edit_text);

                builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Achievement achievement = new Achievement(titleEditText.getText().toString());
                        ((MainActivity) getActivity()).dataBase.achievementDao().insert(achievement);
                        ((MainActivity) getActivity()).achievements = ((MainActivity) getActivity()).dataBase.achievementDao().getAchievements();
                        reloadTaskFragment();
                    }
                });
                builder.setNegativeButton("Отмена", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //скрыть/показать список не выполненных задач
        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visableRV(taskRecyclerView);
                changeButton(taskButton);
            }
        });
        //скрыть/показать список выполненных задач
        taskCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visableRV(taskCompleteRecyclerView);
                changeButton(taskCompleteButton);
            }
        });

        TaskAdapter.OnTaskClickListener taskClickListener = new TaskAdapter.OnTaskClickListener() {
            //переход к подробностям задачи на другой фрагмент
            @Override
            public void onTaskClick(Task task, int position) {
                TaskDetailsFragment taskDetails = new TaskDetailsFragment(task, fragmentManager);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, taskDetails)
                        .commit();
            }

            //изменение статуса задачи
            @Override
            public void setComplete(Task task, int position, Boolean isChecked) {
                System.out.println(isChecked);
                if (isChecked) {
                    task.setDone(true);
                    System.out.println(task.getTitle());
                    ((MainActivity) getActivity()).dataBase.taskDao().setDoneTrue(task.id);
                    ((MainActivity) getActivity()).taskList.remove(task);
                    ((MainActivity) getActivity()).taskCompleteList.add(task);
                } else {
                    task.setDone(false);
                    System.out.println(task.getTitle());
                    ((MainActivity) getActivity()).dataBase.taskDao().setDoneFalse(task.id);
                    ((MainActivity) getActivity()).taskList.add(task);
                    ((MainActivity) getActivity()).taskCompleteList.remove(task);
                }
                taskAdapter.notifyDataSetChanged();
                taskCompleteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }

            //удаление задачи
            @Override
            public void deleteTask(Task task, int position) {
                if (task.isDone()) {
                    ((MainActivity) getActivity()).taskCompleteList.remove(task);
                    taskCompleteAdapter.notifyDataSetChanged();
                } else {
                    ((MainActivity) getActivity()).taskList.remove(task);
                    taskAdapter.notifyDataSetChanged();
                }
                ((MainActivity) getActivity()).dataBase.taskDao().delete(task);
            }
        };

        AchievementAdapter.OnAchievementClickListener achievementClickListener = new AchievementAdapter.OnAchievementClickListener() {
            //сортировка по выбранной цели
            @Override
            public void onAchievementClick(Achievement achievement) {
                if (((MainActivity) getActivity()).achievements.size() != 1) {
                    ((MainActivity) getActivity()).achievements.remove(achievement);
                    Achievement achievement1 = ((MainActivity) getActivity()).achievements.get(0);
                    ((MainActivity) getActivity()).achievements.set(0, achievement);
                    ((MainActivity) getActivity()).achievements.add(1, achievement1);
                    achievementAdapter.notifyDataSetChanged();
                    ((MainActivity) getActivity()).idAchievement = ((MainActivity) getActivity()).achievements.get(0).getId();
                    System.out.println(((MainActivity) getActivity()).idAchievement);
                } else {
                    ((MainActivity) getActivity()).idAchievement = 1;
                }
                reloadTaskLists();
                reloadTaskFragment();
            }

            //открытие диалога редактирования цели
            @Override
            public void onAchievementLongClick(Achievement achievement, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_add_achievement, null);
                builder.setView(dialogView);

                final EditText titleEditText = dialogView.findViewById(R.id.ach_title_edit_text);
                final TextView textView = dialogView.findViewById(R.id.textViewAch);
                textView.setText(R.string.editAch);
                titleEditText.setText(achievement.getTitle());

                if (achievement.getId() != 1) {

                    builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((MainActivity) getActivity()).achievements.get(position).setTitle(titleEditText.getText().toString());
                            ((MainActivity) getActivity()).dataBase.achievementDao().updateAchievement(achievement.getId(), achievement.getTitle());
                            reloadTaskFragment();
                        }
                    });
                    builder.setNegativeButton(
                            "Удалить цель",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (((MainActivity) getActivity()).achievements.size() == 2) {
                                        ((MainActivity) getActivity()).idAchievement = 1;
                                        reloadTaskLists();
                                    }
                                    ((MainActivity) getActivity()).achievements.remove(achievement);
                                    ((MainActivity) getActivity()).dataBase.achievementDao().delete(achievement);
                                    reloadTaskFragment();
                                }
                            });
                } else {
                    builder.setNegativeButton("Отмена", null);
                }
                AlertDialog dialog = builder.create();
                dialog.show();
                achievementAdapter.notifyDataSetChanged();
            }
        };
        //загрузки данных в списки целей, выполненных и не выполненных задач
        taskAdapter = new TaskAdapter(((MainActivity) getActivity()).taskList, taskClickListener);
        taskRecyclerView.setAdapter(taskAdapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskCompleteAdapter = new TaskAdapter(((MainActivity) getActivity()).taskCompleteList, taskClickListener);
        taskCompleteRecyclerView.setAdapter(taskCompleteAdapter);
        taskCompleteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        achievementAdapter = new AchievementAdapter(((MainActivity) getActivity()).achievements, achievementClickListener);
        achievementRecyclerView.setAdapter(achievementAdapter);
        achievementRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        FloatingActionButton addButton = view.findViewById(R.id.add_task_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

        return view;
    }

    //диалог добавления задачи
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        final EditText titleEditText = dialogView.findViewById(R.id.task_title_edit_text);
        final EditText descriptionEditText = dialogView.findViewById(R.id.task_description_edit_text);
        final Spinner spinner = dialogView.findViewById(R.id.spinner);
        final CalendarView calendarView = dialogView.findViewById(R.id.calendar);
        List<String> data = new ArrayList<>();
        for (Achievement achievement : ((MainActivity) getActivity()).achievements) {
            data.add(achievement.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
        final StringBuilder selectedDate = new StringBuilder();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.append(year + "-" + (month + 1) + "-");
                if (dayOfMonth >= 0 && dayOfMonth < 10)
                    selectedDate.append("0");
                selectedDate.append(dayOfMonth);
            }
        });

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateToday = sdf.format(new Date());
                System.out.println(dateToday);
                if (selectedDate.toString().equals("")) {
                    selectedDate.append(sdf.format(new Date(calendarView.getDate())));
                }
                System.out.println(((MainActivity) getActivity()).achievements.get((int) spinner.getSelectedItemId()).getTitle());

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                Task task = new Task(title, description, dateToday, selectedDate.toString(), ((MainActivity) getActivity()).achievements.get((int) spinner.getSelectedItemId()).getId());
                //((MainActivity) getActivity()).taskList.add(task);
                ((MainActivity) getActivity()).dataBase.taskDao().insert(task);
                ((MainActivity) getActivity()).taskList = (ArrayList<Task>) ((MainActivity) getActivity()).dataBase.taskDao().getAllNotDone(((MainActivity) getActivity()).idAchievement);
                reloadTaskFragment();
            }
        });
        builder.setNegativeButton("Отмена", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //обноление списков
    public void reloadTaskLists() {
        ((MainActivity) getActivity()).taskList = (ArrayList<Task>) ((MainActivity) getActivity()).dataBase.taskDao().getAllNotDone(((MainActivity) getActivity()).idAchievement);
        ((MainActivity) getActivity()).taskCompleteList = (ArrayList<Task>) ((MainActivity) getActivity()).dataBase.taskDao().getAllDone(((MainActivity) getActivity()).idAchievement);
    }

    //перезагрузка фрагмента
    public void reloadTaskFragment() {
        TaskFragment taskFragment = new TaskFragment(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, taskFragment)
                .commitAllowingStateLoss();
    }

    //изменение кнопки, которая скрывает списки задач (стрелка указывает вверх\вниз)
    public void changeButton(ImageButton imageButton) {
        System.out.println("ok");

        if (imageButton.getTag() == "true") {
            imageButton.setImageResource(R.drawable.ic_down);
            imageButton.setTag("false");
        } else {
            imageButton.setImageResource(R.drawable.ic_up);
            imageButton.setTag("true");
        }
    }

    //скрытие/показ списков задач
    public void visableRV(RecyclerView recyclerView) {
        if (recyclerView.getVisibility() == RecyclerView.VISIBLE) {
            recyclerView.setVisibility(RecyclerView.GONE);
        } else recyclerView.setVisibility(RecyclerView.VISIBLE);
    }
}
