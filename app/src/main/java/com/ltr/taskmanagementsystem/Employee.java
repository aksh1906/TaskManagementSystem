package com.ltr.taskmanagementsystem;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Akshat Sharma on 08-02-2018.
 */

@Entity(tableName = "employee", foreignKeys = {
        @ForeignKey(
                entity = Department.class,
                parentColumns = "dept_id",
                childColumns = "dept_id",
                onDelete = ForeignKey.CASCADE
        )
})
public class Employee {

    @PrimaryKey
    @NonNull String emp_id;
    public String emp_first_name;
    public String emp_last_name;
    public String email_id;
    public String designation;
    public String dept_id;

    public Employee(String emp_id, String emp_first_name, String emp_last_name,
                    String email_id, String designation, String dept_id) {
        this.emp_id = emp_id;
        this.emp_first_name = emp_first_name;
        this.emp_last_name = emp_last_name;
        this.email_id = email_id;
        this.designation = designation;
        this.dept_id = dept_id;
    }
}
