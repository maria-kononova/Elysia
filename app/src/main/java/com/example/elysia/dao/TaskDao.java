package com.example.elysia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.elysia.entity.Task;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("Select * from Task where isDone=0 and idAchievement=:idAch")
    List<Task> getAllNotDone(long idAch);
    @Query("Select * from Task where isDone=1 and idAchievement=:idAch")
    List<Task> getAllDone(long idAch);

    @Query("Select Count(*) from Task where isDone=1")
    int getCompleteTask();
    @Query("Select Count(*) from Task where isDone=0")
    int getNoCompleteTask();

    /*@Query("Select * from Task")
    List<Task> getAll();*/
    @Query("update Task set isDone=1 where id=:id")
    void setDoneTrue(long id);

    @Query("update Task set dateFinish=:date where id=:id")
    void updateDate(long id, String date);

    @Query("update Task set isDone=:state where id=:id")
    void setState(long id, int state);

    @Query("update Task set isDone=0 where id=:id")
    void setDoneFalse(long id);

    @Query("update Task set titleTask=:newTitle where id=:id")
    void updateTitle(long id, String newTitle);

    @Query("update Task set descriptionTask=:newDescription where id=:id")
    void updateDescription(long id, String newDescription);
    @Query("update Task set idAchievement=:idAch where id=:id")
    void updateAchievement(long id, long idAch);

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
