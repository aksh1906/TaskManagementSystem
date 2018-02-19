package com.ltr.taskmanagementsystem;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Akshat Sharma on 09-02-2018.
 */

public class AppRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;
    private LiveData<List<Task>> mTaskStatus;
    private MeetingDao mMeetingDao;

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mAllTasks = mTaskDao.getAllTasks();
        mTaskStatus = mTaskDao.getOngoingTasks();
        mMeetingDao = db.meetingDao();
    }

    public void insertTask(Task task) {
        new insertAsyncTaskTask(mTaskDao).execute(task);
    }

    LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    LiveData<List<Task>> getOngoingTasks() {
        return mTaskStatus;
    }

    public void insertMeeting(Meeting meeting) {
        new insertAsyncTaskMeeting(mMeetingDao).execute(meeting);
    }

    private static class insertAsyncTaskTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskTaskDao;

        insertAsyncTaskTask(TaskDao dao) {
            mAsyncTaskTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskTaskDao.insertTask(params[0]);
            return null;
        }
    }

    private static class insertAsyncTaskMeeting extends AsyncTask<Meeting, Void, Void> {
        private MeetingDao mAsyncTaskMeetingDao;

        insertAsyncTaskMeeting(MeetingDao dao) {
            mAsyncTaskMeetingDao = dao;
        }

        @Override
        protected Void doInBackground(final Meeting... params) {
            mAsyncTaskMeetingDao.insertMeeting(params[0]);
            return null;
        }
    }
}
