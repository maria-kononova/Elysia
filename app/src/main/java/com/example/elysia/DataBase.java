package com.example.elysia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.elysia.dao.AchievementDao;
import com.example.elysia.dao.EventDao;
import com.example.elysia.dao.NoteDao;
import com.example.elysia.dao.ResourceDao;
import com.example.elysia.dao.TaskDao;
import com.example.elysia.entity.Achievement;
import com.example.elysia.entity.Event;
import com.example.elysia.entity.Note;
import com.example.elysia.entity.Resource;
import com.example.elysia.entity.Task;

@Database(entities = {Task.class, Event.class, Note.class, Resource.class, Achievement.class}, version = 7, exportSchema = true)
public abstract class DataBase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract EventDao eventDao();
    public abstract NoteDao noteDao();
    public abstract ResourceDao resourceDao();
    public abstract AchievementDao achievementDao();
    private static DataBase INSTANCE;

    public static DataBase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, "MyDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

}
