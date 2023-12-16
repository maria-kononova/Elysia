package com.example.elysia.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elysia.MainActivity;
import com.example.elysia.entity.Event;
import com.example.elysia.adapter.EventAdapter;
import com.example.elysia.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    EventAdapter eventAdapter;
    String selectedDate;
    RecyclerView recyclerView;
    EventAdapter.OnEventClickListener eventClickListener;

    public CalendarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);
        CalendarView calendarView = view.findViewById(R.id.calendar);
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        selectedDate = formmat1.format(ldt);
        recyclerView = (RecyclerView) view.findViewById(R.id.event_list);
        //удаление события
        eventClickListener = new EventAdapter.OnEventClickListener() {
            @Override
            public void deleteEvent(Event event, int position) {
                ((MainActivity) getActivity()).eventList.remove(event);
                ((MainActivity) getActivity()).dataBase.eventDao().delete(event);
                eventAdapter.notifyDataSetChanged();
            }
            //вкл/выкл уведомлений события
            @Override
            public void changeNotification(Event event) {
                if (event.getNotification()) {
                    ((MainActivity) getActivity()).dataBase.eventDao().updateNotificationOff(event.id);
                    event.setNotification(false);
                } else {
                    ((MainActivity) getActivity()).dataBase.eventDao().updateNotificationOn(event.id);
                    event.setNotification(true);
                }
                eventAdapter.notifyDataSetChanged();
            }
            //обновление события
            @Override
            public void calendarUpdate(Event event) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_calendar, null);
                builder.setView(dialogView);


                final ImageButton goButton = (ImageButton) dialogView.findViewById(R.id.goButton);
                CalendarView calendarView = dialogView.findViewById(R.id.calendar);
                TimePicker timePicker = dialogView.findViewById(R.id.clock);
                //переход от календаря к часам и обратно
                goButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (goButton.getTag().equals("next")) {
                            goButton.setTag("back");
                            goButton.setImageResource(R.drawable.ic_back);
                            calendarView.setVisibility(View.GONE);
                            timePicker.setVisibility(View.VISIBLE);
                        } else {
                            goButton.setTag("next");
                            goButton.setImageResource(R.drawable.ic_next);
                            timePicker.setVisibility(View.GONE);
                            calendarView.setVisibility(View.VISIBLE);
                        }
                    }
                });
                final StringBuilder selectedDate1 = new StringBuilder();
                final StringBuilder selectedTime = new StringBuilder();
                //получение выбранной даты с календаря
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        selectedDate1.append(year + "-" + (month + 1) + "-");
                        if (dayOfMonth >= 0 && dayOfMonth < 10)
                            selectedDate1.append("0");
                        selectedDate1.append(dayOfMonth);
                    }
                });
                //сохранение изменений даты и времени события
                builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedDate1.toString().equals("")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            selectedDate1.append(sdf.format(new Date(calendarView.getDate())));
                        }
                        System.out.println(selectedDate);
                        if (timePicker.getCurrentHour() <= 9) {
                            selectedTime.append("0");
                        }
                        selectedTime.append(timePicker.getCurrentHour() + ":");
                        if (timePicker.getCurrentMinute() <= 9) {
                            selectedTime.append("0");
                        }
                        selectedTime.append(timePicker.getCurrentMinute());

                        ((MainActivity) getActivity()).dataBase.eventDao().updateDateTime(event.getId(), selectedDate1.toString(), selectedTime.toString());
                        //if (selectedDate.equals(selectedDate1.toString())) ((MainActivity)getActivity()).eventList.add(event);
                        eventAdapter.notifyDataSetChanged();
                        updateRecyclerView(selectedDate);
                    }
                });

                builder.setNegativeButton("Отмена", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        updateRecyclerView(selectedDate);

        //получение выбранной даты в календаре
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = year + "-" + (month + 1) + "-";
                if (dayOfMonth >= 0 && dayOfMonth < 10)
                    selectedDate += "0";
                selectedDate += dayOfMonth;
                updateRecyclerView(selectedDate);
            }
        });

        Button addButton = view.findViewById(R.id.buttonAddEvent);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventDialog();
            }
        });
        return view;
    }
    //обновление списка событий в зависимости от выбранной даты в календаре
    public void updateRecyclerView(String selectedDate) {
        System.out.println(selectedDate);
        ((MainActivity) getActivity()).eventList = (ArrayList<Event>) ((MainActivity) getActivity()).dataBase.eventDao().getAllOnDate(selectedDate);
        eventAdapter = new EventAdapter(((MainActivity) getActivity()).eventList, eventClickListener);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    //Вызов диалогового окна для добавления события
    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_event, null);
        builder.setView(dialogView);
        final EditText titleEditText = dialogView.findViewById(R.id.event_title_edit_text);
        final CheckBox checkBox = dialogView.findViewById(R.id.checkBoxEvent);

        builder.setPositiveButton("дальше", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_calendar, null);
                builder.setView(dialogView);


                final ImageButton goButton = (ImageButton) dialogView.findViewById(R.id.goButton);
                CalendarView calendarView = dialogView.findViewById(R.id.calendar);
                TimePicker timePicker = dialogView.findViewById(R.id.clock);

                goButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (goButton.getTag().equals("next")) {
                            goButton.setTag("back");
                            goButton.setImageResource(R.drawable.ic_back);
                            calendarView.setVisibility(View.GONE);
                            timePicker.setVisibility(View.VISIBLE);
                        } else {
                            goButton.setTag("next");
                            goButton.setImageResource(R.drawable.ic_next);
                            timePicker.setVisibility(View.GONE);
                            calendarView.setVisibility(View.VISIBLE);
                        }
                    }
                });
                final StringBuilder selectedDate1 = new StringBuilder();
                final StringBuilder selectedTime = new StringBuilder();
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        selectedDate1.append(year + "-" + (month + 1) + "-");
                        if (dayOfMonth >= 0 && dayOfMonth < 10)
                            selectedDate1.append("0");
                        selectedDate1.append(dayOfMonth);
                    }
                });

                builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedDate1.toString().equals("")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            selectedDate1.append(sdf.format(new Date(calendarView.getDate())));
                        }
                        System.out.println(selectedDate);
                        if (timePicker.getCurrentHour() <= 9) {
                            selectedTime.append("0");
                        }
                        selectedTime.append(timePicker.getCurrentHour() + ":");
                        if (timePicker.getCurrentMinute() <= 9) {
                            selectedTime.append("0");
                        }
                        selectedTime.append(timePicker.getCurrentMinute());

                        String content = titleEditText.getText().toString();
                        Boolean state = false;
                        if (checkBox.isChecked()) state = true;
                        Event event = new Event(content, selectedDate1.toString(), selectedTime.toString(), state);
                        ((MainActivity) getActivity()).dataBase.eventDao().insert(event);
                        if (selectedDate.equals(selectedDate1.toString()))
                            ((MainActivity) getActivity()).eventList.add(event);
                        eventAdapter.notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("Отмена", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        builder.setNegativeButton("Отмена", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
