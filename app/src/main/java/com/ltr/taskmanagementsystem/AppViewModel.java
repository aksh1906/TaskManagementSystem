package com.ltr.taskmanagementsystem;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by Akshat Sharma on 09-02-2018.
 */

public class AppViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    private LiveData<List<Task>> mAllResponsibleTasks;
    private LiveData<List<Task>> mAllAccountableTasks;
    private LiveData<List<Task>> mOngoingTasks;

    public AppViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllResponsibleTasks = mRepository.getAllResponsibleTasks();
        mAllAccountableTasks = mRepository.getAllAccountableTasks();
        mOngoingTasks = mRepository.getOngoingTasks();
    }

    public void insertTask(Task task) {
        mRepository.insertTask(task);
    }

    LiveData<List<Task>> getAllResponsibleTasks() {
        return mAllResponsibleTasks;
    }

    LiveData<List<Task>> getAllAccountableTasks() {
        return mAllAccountableTasks;
    }

    LiveData<List<Task>> getOngoingTasks() {
        return mOngoingTasks;
    }

    public void insertMeeting(Meeting meeting) {
        mRepository.insertMeeting(meeting);
    }
}
