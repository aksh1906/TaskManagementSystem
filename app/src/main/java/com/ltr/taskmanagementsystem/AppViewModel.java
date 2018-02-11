package com.ltr.taskmanagementsystem;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

/**
 * Created by Akshat Sharma on 09-02-2018.
 */

public class AppViewModel extends AndroidViewModel {

    private AppRepository mRepository;

    public AppViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public void insertTask(Task task) {
        mRepository.insertTask(task);
    }

    public void insertMeeting(Meeting meeting) {
        mRepository.insertMeeting(meeting);
    }
}
