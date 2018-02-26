package com.ltr.taskmanagementsystem;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewSingleMeetingActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private Intent intent;
    private TextView tvTitle, tvType, tvDescription, tvConductedFor;
    private TextView tvFrequency, tvDate, tvTime, tvVenue;
    private String title, type, description, conductedFor, frequency, date, time, venue;
    private int intentMeetingId;
    private AppViewModel mAppViewModel;
    private AppDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_meeting);

        createToolbar();
        populateMeetingDetails();
    }

    private void createToolbar() {
        mToolbar = findViewById(R.id.tbViewSingleMeetingActivity);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(null); // remove the default title for the toolbar
            toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText("View Task Details");

            ActionBar ab = getSupportActionBar();
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Close the current activity when the back button
    // in the toolbar is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

    private void populateMeetingDetails() {
        tvTitle = findViewById(R.id.tvMeetingTitleData);
        tvType = findViewById(R.id.tvMeetingTypeData);
        tvDescription = findViewById(R.id.tvMeetingDescriptionData);
        tvConductedFor = findViewById(R.id.tvConductedForData);
        tvFrequency = findViewById(R.id.tvMeetingFrequencyData);
        tvDate = findViewById(R.id.tvMeetingDateData);
        tvTime = findViewById(R.id.tvMeetingTimeData);
        tvVenue = findViewById(R.id.tvMeetingVenueData);

        intent = getIntent();
        intentMeetingId = intent.getIntExtra("meeting id", 0);
        mDatabase = AppDatabase.getDatabase(getApplicationContext());
        mDatabase.meetingDao().getSingleMeetingData(intentMeetingId).observe(this, new Observer<Meeting>() {
            @Override
            public void onChanged(@Nullable Meeting meeting) {
                tvTitle.setText(meeting.getTitle());
                tvType.setText(meeting.getType());
                tvDescription.setText(meeting.getDescription());
                tvConductedFor.setText(meeting.getDept_conducted_for());
                tvFrequency.setText(meeting.getFrequency());
                tvDate.setText(meeting.getMeeting_date());
                tvTime.setText(meeting.getMeeting_time());
                tvVenue.setText(meeting.getVenue());
            }
        });
    }
}
