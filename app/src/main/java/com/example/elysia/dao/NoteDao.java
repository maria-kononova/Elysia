package com.example.elysia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.elysia.entity.Note;
import com.example.elysia.entity.Task;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("Select * from Note where idTask=:id")
    Note getNoteByIdTask(long id);
    @Query("update Note set content=:content, dateLastChange=:dateLastChange where id=:id")
    void updateNote(long id, String content, String dateLastChange);
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
