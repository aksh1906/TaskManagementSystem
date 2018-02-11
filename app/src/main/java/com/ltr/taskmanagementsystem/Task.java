package com.ltr.taskmanagementsystem;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Akshat Sharma on 08-02-2018.
 */

@Entity(tableName = "task"
//        ,foreignKeys = {
//        @ForeignKey(
//                entity = Department.class,
//                parentColumns = "dept_id",
//                childColumns = "concerned_dept",
//                onDelete = ForeignKey.CASCADE
//        ),
//        @ForeignKey(
//                entity = Project.class,
//                parentColumns = "project_name",
//                childColumns = "concerned_project",
//                onDelete = ForeignKey.CASCADE
//        )}
)
public class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="task_id")
    private int mTask_id;
    
    @ColumnInfo(name="subject")
    private String mSubject;

    @ColumnInfo(name="priority")
    private String mPriority;

    @ColumnInfo(name="date_created")
    private String mDate_created;

//    @ColumnInfo(name="exp_finish_date")
//    private String mExp_finish_date;
//
//    @ColumnInfo(name="actual_finish_date")
//    private String mActual_finish_date;

    @ColumnInfo(name="status")
    private String mStatus;

    @ColumnInfo(name="description")
    private String mDescription;

    @ColumnInfo(name="category")
    private String mCategory;

//    @ColumnInfo(name="creator")
//    private String mCreator;

    @ColumnInfo(name="responsible")
    private String mResponsible;

    @ColumnInfo(name="accountable")
    private String mAccountable;

    @ColumnInfo(name="concerned_dept")
    private String mConcerned_dept;

    @ColumnInfo(name="concerned_project")
    private String mConcerned_project;

//    @ColumnInfo(name="meeting_linked_with")
//    private String mMeeting_linked_with;

    public Task(int task_id, String subject, String priority, String date_created,
                String status, String description, String category,
                String responsible, String accountable, String concerned_dept,
                String concerned_project) {
        this.mTask_id = task_id;
        this.mSubject = subject;
        this.mPriority = priority;
        this.mDate_created = date_created;
        this.mStatus = status;
        this.mDescription = description;
        this.mCategory = category;
        this.mResponsible = responsible;
        this.mAccountable = accountable;
        this.mConcerned_dept = concerned_dept;
        this.mConcerned_project = concerned_project;
    }

    @NonNull
    public int getTask_id() { return mTask_id; }

    public String getSubject() { return mSubject; }

    public String getPriority() { return mPriority; }

    public String getDate_created() { return mDate_created; }

//    public String getExp_finish_date() { return mExp_finish_date; }
//
//    public String getActual_finish_date() { return mActual_finish_date; }

    public String getStatus() { return mStatus; }

    public String getDescription() { return mDescription; }

    public String getCategory() { return mCategory; }

//    public String getCreator() { return mCreator; }

    public String getResponsible() { return mResponsible; }

    public String getAccountable() { return mAccountable; }

    public String getConcerned_dept() { return mConcerned_dept; }

    public String getConcerned_project() { return mConcerned_project; }

//    public String getMeeting_linked_with() { return mMeeting_linked_with; }
}
