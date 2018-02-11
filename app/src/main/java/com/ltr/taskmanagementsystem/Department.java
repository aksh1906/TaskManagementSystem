package com.ltr.taskmanagementsystem;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Akshat Sharma on 08-02-2018.
 */

@Entity(tableName = "department", foreignKeys = {
        @ForeignKey(
                entity = Project.class,
                parentColumns = "project_name",
                childColumns = "project_name",
                onDelete = ForeignKey.CASCADE
        )
})
public class Department {

    @PrimaryKey
    @NonNull String dept_id;
    public String dept_name;
    public String dept_head;
    public String project_name;

    public Department(String dept_id, String dept_name, String dept_head, String project_name) {
        this.dept_id = dept_id;
        this.dept_name = dept_name;
        this.dept_head = dept_head;
        this.project_name = project_name;
    }
}
