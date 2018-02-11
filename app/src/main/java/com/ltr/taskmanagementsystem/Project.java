package com.ltr.taskmanagementsystem;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Akshat Sharma on 08-02-2018.
 */

@Entity(tableName = "project")
public class Project {

    @PrimaryKey
    @NonNull String project_name;
    public String project_head;

    public Project(String project_name, String project_head) {
        this.project_name = project_name;
        this.project_head = project_head;
    }
}
