package com.example.elysia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.elysia.entity.Note;
import com.example.elysia.entity.Resource;

import java.util.List;

@Dao
public interface ResourceDao {
    @Query("Select * from Resource where idTask=:idTask")
    List<Resource> getResourcesByTaskId(long idTask);

    @Insert
    void insert(Resource resource);

    @Update
    void update(Resource resource);

    @Delete
    void delete(Resource resource);
}
