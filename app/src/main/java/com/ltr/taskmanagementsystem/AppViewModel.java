package com.ltr.taskmanagementsystem;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;

import java.util.List;

/**
 * Created by Akshat Sharma on 09-02-2018.
 */

public class AppViewModel extends AndroidViewModel {

    public AppRepository mRepository;
    private LiveData<List<Task>> mAllResponsibleTasks;
    private LiveData<List<Task>> mAllAccountableTasks;
    private LiveData<List<Task>> mAllCreatedTasks;
    private LiveData<List<Task>> mOngoingTasks;

    public AppViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllResponsibleTasks = mRepository.getAllResponsibleTasks();
        mAllAccountableTasks = mRepository.getAllAccountableTasks();
        mAllCreatedTasks = mRepository.getAllCreatedTasks();
        mOngoingTasks = mRepository.getOngoingTasks();
    }

    public void insertTask(Task task) {
        mRepository.insertTask(task);
    }

    public void updateTask(Task task) {
        mRepository.updateTask(task);
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
        return mOngoingTasks;
    }

    public void insertMeeting(Meeting meeting) {
        mRepository.insertMeeting(meeting);
    }
}
