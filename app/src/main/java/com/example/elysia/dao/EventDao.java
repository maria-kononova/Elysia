package com.example.elysia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.elysia.entity.Event;
import com.example.elysia.entity.Task;

import java.util.List;

@Dao
public interface EventDao {

    @Query("Select * from Event where date=:date")
    List<Event> getAllOnDate(String date);

    @Query("update Event set notification=1 where id=:id")
    void updateNotificationOn(long id);

    @Query("update Event set notification=0 where id=:id")
    void updateNotificationOff(long id);

    @Query("update Event set date=:date, time=:time where id=:id")
    void updateDateTime(long id, String date, String time);

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);
}
