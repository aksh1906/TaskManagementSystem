package com.ltr.taskmanagementsystem;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by Akshat Sharma on 08-02-2018.
 */

@Database(entities = {Employee.class, Department.class, Task.class, Meeting.class, Project.class, Attendee.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract EmployeeDao employeeDao();
    public abstract DepartmentDao departmentDao();
    public abstract TaskDao taskDao();
    public abstract MeetingDao meetingDao();
    public abstract AttendeeDao attendeeDao();
    public abstract ProjectDao projectDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
