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
public interface MeetingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeeting(Meeting meeting);

    @Query("SELECT * FROM meeting WHERE creator = :name")
    LiveData<List<Meeting>> getAllCreatedMeetings(String name);

    @Query("SELECT * FROM meeting WHERE meeting_id = :meeting_id")
    LiveData<Meeting> getSingleMeetingData(int meeting_id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMeeting(Meeting meeting);
}
