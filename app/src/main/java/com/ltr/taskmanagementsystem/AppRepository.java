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
    private LiveData<List<Task>> mAllResponsibleTasks;
    private LiveData<List<Task>> mAllAccountableTasks;
    private LiveData<List<Task>> mAllCreatedTasks;
    private LiveData<List<Task>> mTaskStatus;
    private LiveData<List<Meeting>> mAllCreatedMeetings;
    private MeetingDao mMeetingDao;
    private final String name = "Akshat"; // placeholder

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mAllResponsibleTasks = mTaskDao.getAllResponsibleTasks(name);
        mAllAccountableTasks = mTaskDao.getAllAccountableTasks(name);
        mAllCreatedTasks = mTaskDao.getAllCreatedTasks(name);
        mTaskStatus = mTaskDao.getOngoingTasks();
        mMeetingDao = db.meetingDao();
        mAllCreatedMeetings = mMeetingDao.getAllCreatedMeetings(name);
    }

    public void insertTask(Task task) {
        new insertAsyncTaskTask(mTaskDao).execute(task);
    }

    public void updateTask(Task task) {
        new updateAsyncTask(mTaskDao).execute(task);
    }

    LiveData<List<Task>> getAllResponsibleTasks() {
        return mAllResponsibleTasks;
    }

    LiveData<List<Task>> getAllAccountableTasks() {
        return mAllAccountableTasks;
    }

    LiveData<List<Task>> getAllCreatedTasks() {
        return mAllCreatedTasks;
    }

    LiveData<List<Task>> getOngoingTasks() {
        return mTaskStatus;
    }

    LiveData<List<Meeting>> getAllCreatedMeetings() {
        return mAllCreatedMeetings;
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

    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskUpdateDao;

        updateAsyncTask(TaskDao dao) {
            mAsyncTaskUpdateDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskUpdateDao.updateTask(params[0]);
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
