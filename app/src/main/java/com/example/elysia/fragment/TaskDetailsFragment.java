package com.example.elysia.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elysia.FileDialog;
import com.example.elysia.MainActivity;
import com.example.elysia.R;
import com.example.elysia.adapter.ResourseAdapter;
import com.example.elysia.adapter.TaskAdapter;
import com.example.elysia.entity.Achievement;
import com.example.elysia.entity.Note;
import com.example.elysia.entity.Resource;
import com.example.elysia.entity.Task;
import com.example.elysia.fragment.TaskFragment;
import com.example.elysia.themes.LinedEditText;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskDetailsFragment extends Fragment {
    private static final int SELECT_PICTURE = 200;
    Task task;
    FragmentManager fragmentManager;
    Note note;
    LinedEditText linedEditText;
    CheckBox checkBox;
    boolean state = true;
    ImageView iVPreviewImage;
    ImageButton deleteNote;
    LinearLayout calendarButton;
    Boolean save = true;
    Boolean saveNote = false;
    EditText titleTextView;
    EditText descriptionTextView;
    TextView dateNoteLastChangeTextView;
    Spinner spinner;
    RecyclerView recyclerView;
    ResourseAdapter resourseAdapter;
    private static final int REQUEST_CODE = 1;
    File folderData =  new File("/data/data/com.example.elysia/data_resourse/");

    public TaskDetailsFragment(Task task, FragmentManager fragmentManager) {
        this.task = task;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task, container, false);

        if (!folderData.exists()) {
            folderData.mkdirs();
        }
        titleTextView = view.findViewById(R.id.titleTask_textView);
        descriptionTextView = view.findViewById(R.id.descriptionTask_textView);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.backButton);
        ImageButton saveButton = (ImageButton) view.findViewById(R.id.saveButton);
        ImageButton addNote = (ImageButton) view.findViewById(R.id.addNote);
        ImageButton fileButton = (ImageButton) view.findViewById(R.id.fileButton);
        ImageView deleteNote = view.findViewById(R.id.deleteNote);
        calendarButton = view.findViewById(R.id.calendarButton);
        checkBox = (CheckBox) view.findViewById(R.id.checkBoxDeteils);
        dateNoteLastChangeTextView = view.findViewById(R.id.dateNoteLastChangeTextView);
        linedEditText = (LinedEditText) view.findViewById(R.id.edittxt_multilines);
        spinner = view.findViewById(R.id.spinner);
        TextView dateCreateTextView = view.findViewById(R.id.dateCreateTextView);
        TextView dateFinishTextView = view.findViewById(R.id.dateFinishTextView);

        ((MainActivity) getActivity()).resources = ((MainActivity) getActivity()).dataBase.resourceDao().getResourcesByTaskId(task.getId());

        List<String> data = new ArrayList<>();
        Achievement selectedAchievement = ((MainActivity) getActivity()).dataBase.achievementDao().getAchievementById(task.getIdAchievement());
        int idSelectedAchievement = 0;
        for (Achievement achievement : ((MainActivity) getActivity()).achievements) {
            data.add(achievement.getTitle());
            if (selectedAchievement.getId() == achievement.getId())
                idSelectedAchievement = ((MainActivity) getActivity()).achievements.indexOf(achievement);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, data);
        spinner.setAdapter(adapter);
        spinner.setSelection(idSelectedAchievement);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Achievement newAch = ((MainActivity) getActivity()).achievements.get(position);
                task.setIdAchievement(newAch.getId());
                ((MainActivity) getActivity()).dataBase.taskDao().updateAchievement(task.getId(), newAch.getId());
                updateTaskLists();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        ResourseAdapter.OnResourseClickListener resourseClickListener = new ResourseAdapter.OnResourseClickListener() {
            @Override
            public void openFile(Resource resource) {
                openFileInApp(Uri.parse(resource.getPathResource()));
            }

            @Override
            public void deleteFile(Resource resource) {
                ((MainActivity) getActivity()).resources.remove(resource);
                ((MainActivity)getActivity()).dataBase.resourceDao().delete(resource);
                resourseAdapter.notifyDataSetChanged();
                updateTaskLists();
            }
        };
        recyclerView = view.findViewById(R.id.resourse_list);
        resourseAdapter = new ResourseAdapter(((MainActivity) getActivity()).resources, resourseClickListener);
        recyclerView.setAdapter(resourseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    task.setDone(true);
                    ((MainActivity) getActivity()).dataBase.taskDao().setState(task.getId(), 1);
                    ((MainActivity) getActivity()).taskList.remove(task);
                    ((MainActivity) getActivity()).taskCompleteList.add(task);

                } else {
                    task.setDone(false);
                    ((MainActivity) getActivity()).dataBase.taskDao().setState(task.getId(), 0);
                    ((MainActivity) getActivity()).taskList.add(task);
                    ((MainActivity) getActivity()).taskCompleteList.remove(task);
                }
                setCheckBoxState();
                updateTaskLists();
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_calendar, null);
                builder.setView(dialogView);
                TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
                titleTextView.setText(R.string.changeDate);

                final ImageButton goButton = (ImageButton) dialogView.findViewById(R.id.goButton);
                CalendarView calendarView = dialogView.findViewById(R.id.calendar);
                TimePicker timePicker = dialogView.findViewById(R.id.clock);
                timePicker.setVisibility(View.GONE);
                goButton.setVisibility(View.GONE);
                final StringBuilder selectedDate = new StringBuilder();
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        selectedDate.setLength(0);
                        selectedDate.append(year + "-" + (month+1) + "-");
                        if (dayOfMonth >= 0 && dayOfMonth < 10)
                            selectedDate.append("0");
                        selectedDate.append(dayOfMonth);
                    }
                });

                builder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedDate.toString().equals("")){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            selectedDate.append(sdf.format(new Date(calendarView.getDate())));
                        }
                        System.out.println(selectedDate.toString());
                        ((MainActivity)getActivity()).dataBase.taskDao().updateDate(task.getId(), selectedDate.toString());
                        updateTaskLists();
                        dateFinishTextView.setText(selectedDate.toString());
                    }
                });

                builder.setNegativeButton("Отмена", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        titleTextView.setText(task.getTitle());
        descriptionTextView.setText(task.getDescription());
        setCheckBoxState();
        dateCreateTextView.setText(task.getDateCreate());
        dateFinishTextView.setText(task.getDateFinish());
        note = ((MainActivity) getActivity()).dataBase.noteDao().getNoteByIdTask(task.getId());
        if (note != null)
        {
            deleteNote.setTag("on");
            dateNoteLastChangeTextView.setText(note.getDateLastChange());
            linedEditText.setText(note.getContent());
            state = false;
        }

        /*Resource resource = ((MainActivity) getActivity()).dataBase.resourceDao().getResourceByTaskId(task.getId());
        if (resource !=null)
        {
            //getContext().grantUriPermission("com.example.elysia", Uri.parse(resource.getPathResource()), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            iVPreviewImage.setImageURI(Uri.parse(resource.getPathResource()));
        }*/

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteNote.getTag().equals("on")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage(R.string.deleteNote);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Удалить",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ((MainActivity) getActivity()).dataBase.noteDao().delete(note);
                                    dateNoteLastChangeTextView.setText("");
                                    linedEditText.setText("");
                                    deleteNote.setTag("off");
                                }
                            });

                    builder1.setNegativeButton("Отмена", null);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
                checkChange();
                if (save){
                    exit();
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage(R.string.changeNotSave);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Сохранить",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    saveData();
                                }
                            });

                    builder1.setNegativeButton(
                            "Отменить изменения",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    exit();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addNote.getTag().equals("off")) {
                    addNote.setImageResource(R.drawable.ic_minus);
                    linedEditText.setVisibility(LinedEditText.VISIBLE);
                    addNote.setTag("on");
                } else {
                    addNote.setImageResource(R.drawable.ic_add);
                    linedEditText.setVisibility(LinedEditText.GONE);
                    addNote.setTag("off");
                }
            }
        });
        return view;
    }

    public void setCheckBoxState() {
        if (task.isDone()) {
            checkBox.setChecked(true);
            checkBox.setText(R.string.complete);
        } else {
            checkBox.setChecked(false);
            checkBox.setText(R.string.noComplete);
        }
    }

    public void saveNote() {
        System.out.println(state);
        if (!linedEditText.getText().toString().equals("")){
            //note = ((MainActivity) getActivity()).dataBase.noteDao().getNoteByIdTask(task.getId());
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            String date = formmat1.format(ldt);
            if(state) {
                System.out.println("сохранение");
                System.out.println(task.getId());
                note = new Note(task.getId(), linedEditText.getText().toString(), formmat1.format(ldt));
                ((MainActivity) getActivity()).dataBase.noteDao().insert(note);
                //deleteNote.setTag("on");
            }
            else {
                System.out.println("обновление");
                System.out.println(task.getId());
                ((MainActivity) getActivity()).dataBase.noteDao().updateNote(note.getId(), linedEditText.getText().toString(), date);
                note.setContent(linedEditText.getText().toString());
                note.setDateLastChange(date);
                note = ((MainActivity) getActivity()).dataBase.noteDao().getNoteByIdTask(task.getId());
            }
        }
        saveNote = true;
        updateTaskLists();
    }

    public void checkChange(){
        if(!task.getTitle().equals(titleTextView.getText().toString())) save = false;
        if(!task.getDescription().equals(descriptionTextView.getText().toString())) save = false;
        System.out.println(saveNote);
        //if(!linedEditText.getText().toString().equals("")||saveNote) save = false;
        //note = ((MainActivity) getActivity()).dataBase.noteDao().getNoteByIdTask(task.getId());
        /*if(note!=null) {
            System.out.println(note.getContent());
            System.out.println(linedEditText.getText().toString());
            if(!note.getContent().equals(linedEditText.getText().toString())) save = false;
        }*/
    }

    public void saveData(){
        if(!task.getTitle().equals(titleTextView.getText().toString())){
            task.setTitle(titleTextView.getText().toString());
            ((MainActivity)getActivity()).dataBase.taskDao().updateTitle(task.getId(), titleTextView.getText().toString());
        }
        if(!task.getDescription().equals(descriptionTextView.getText().toString())){
            task.setDescription(descriptionTextView.getText().toString());
            ((MainActivity)getActivity()).dataBase.taskDao().updateDescription(task.getId(), descriptionTextView.getText().toString());
        }
        saveNote();
        updateTaskLists();
        save = true;
    }

    public void exit(){
        TaskFragment taskDetails = new TaskFragment(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, taskDetails)
                .commit();
    }

    public void updateTaskLists(){
        ((MainActivity)getActivity()).taskList = (ArrayList<Task>) ((MainActivity)getActivity()).dataBase.taskDao().getAllNotDone(((MainActivity) getActivity()).idAchievement);
        ((MainActivity)getActivity()).taskCompleteList = (ArrayList<Task>) ((MainActivity)getActivity()).dataBase.taskDao().getAllDone(((MainActivity) getActivity()).idAchievement);
    }

    private void openFilePicker() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose file");
        startActivityForResult(chooseFile, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Resource resource = new Resource(task.getId(), uri.toString(), "");//file.getAbsolutePath(), file.getAbsolutePath().split(".")[1]);
            ((MainActivity)getActivity()).dataBase.resourceDao().insert(resource);
            ((MainActivity)getActivity()).resources.add(resource);
            resourseAdapter.notifyDataSetChanged();
            //openFileInApp(uri);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((MainActivity)getActivity()).getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }


    private void openFileInApp(Uri uri) {

        try {
            //Uri uri = Uri.fromFile(url);
            File file = new File(uri.getPath());
            String url = file.getAbsolutePath();

            System.out.println(uri.getPath());


            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")){
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }
}

