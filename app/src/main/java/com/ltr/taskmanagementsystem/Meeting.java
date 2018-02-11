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

@Entity(tableName = "meeting"
//        , foreignKeys = {
//        @ForeignKey(
//                entity = Department.class,
//                parentColumns = "dept_id",
//                childColumns = "dept_conducted_for",
//                onDelete = ForeignKey.CASCADE
//        )}
        )
public class Meeting {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="meeting_id")
    private int mMeeting_id;

    @ColumnInfo(name="title")
    private String mTitle;

    @ColumnInfo(name="type")
    private String mType;

    @ColumnInfo(name="description")
    private String mDescription;

    @ColumnInfo(name="meeting_date")
    private String mMeeting_date;

    @ColumnInfo(name="meeting_time")
    private String mMeeting_time;

    @ColumnInfo(name="venue")
    private String mVenue;

//    @ColumnInfo(name="creator")
//    private String mCreator;

//    @ColumnInfo(name="dept_conducted_for")
//    private String mDept_conducted_for;

    @ColumnInfo(name="frequency")
    private String mFrequency;

    public Meeting(int meeting_id, String title, String type, String description, String meeting_date,
                   String meeting_time, String venue, String frequency) {
        this.mMeeting_id = meeting_id;
        this.mTitle = title;
        this.mType = type;
        this.mDescription = description;
        this.mMeeting_date = meeting_date;
        this.mMeeting_time = meeting_time;
        this.mVenue = venue;
//        this.mCreator = creator;
//        this.mDept_conducted_for = dept_conducted_for;
        this.mFrequency = frequency;
    }

    @NonNull
    public int getMeeting_id() {
        return mMeeting_id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getType() {
        return mType;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getMeeting_date() {
        return mMeeting_date;
    }

    public String getMeeting_time() {
        return mMeeting_time;
    }

    public String getVenue() {
        return mVenue;
    }

//    public String getCreator() { return mCreator; }

//    public String getDept_conducted_for() { return mDept_conducted_for; }

    public String getFrequency() {
        return mFrequency;
    }
}
