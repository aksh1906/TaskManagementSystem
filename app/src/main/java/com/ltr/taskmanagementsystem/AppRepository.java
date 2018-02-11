package com.ltr.taskmanagementsystem;

import android.app.Application;
import android.os.AsyncTask;

/**
 * Created by Akshat Sharma on 09-02-2018.
 */

public class AppRepository {
    private TaskDao mTaskDao;
    private MeetingDao mMeetingDao;

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mMeetingDao = db.meetingDao();
    }

    public void insertTask(Task task) {
        new insertAsyncTaskTask(mTaskDao).execute(task);
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
