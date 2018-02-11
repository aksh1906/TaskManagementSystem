package com.ltr.taskmanagementsystem;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Akshat Sharma on 08-02-2018.
 */

@Entity(tableName = "attendee", foreignKeys = {
        @ForeignKey(
                entity = Employee.class,
                parentColumns = "emp_id",
                childColumns = "employee_id",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Meeting.class,
                parentColumns = "meeting_id",
                childColumns = "meeting_id",
                onDelete = ForeignKey.CASCADE
        )
})
public class Attendee {

    @PrimaryKey(autoGenerate = true)
    public int attendee_id;
    public String employee_id;
    public String meeting_id;

    public Attendee(int attendee_id, String employee_id, String meeting_id) {
        this.attendee_id = attendee_id;
        this.employee_id = employee_id;
        this.meeting_id = meeting_id;
    }
}
