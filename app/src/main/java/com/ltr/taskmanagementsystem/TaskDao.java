package com.ltr.taskmanagementsystem;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Akshat Sharma on 08-02-2018.
 */

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task task);

    @Query("SELECT * FROM task WHERE task_id = :task_id")
    LiveData<Task> getSingleTaskData(int task_id);

    @Query("SELECT * FROM task WHERE responsible = :name")
    LiveData<List<Task>> getAllResponsibleTasks(String name);

    @Query("SELECT * FROM task WHERE accountable = :name")
    LiveData<List<Task>> getAllAccountableTasks(String name);

    @Query("SELECT * FROM task WHERE creator = :name")
    LiveData<List<Task>> getAllCreatedTasks(String name);

    @Query("SELECT * FROM task WHERE status='Ongoing'")
    LiveData<List<Task>> getOngoingTasks();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);
}
