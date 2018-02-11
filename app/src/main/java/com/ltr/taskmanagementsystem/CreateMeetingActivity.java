package com.ltr.taskmanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateMeetingActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public EditText etMeetingTitle, etMeetingDescription, etConductedFor, etMeetingVenue, etParticipants;
    public Spinner spinnerMeetingType, spinnerMeetingFrequency;
    public TextView tvDatePicker, tvTimePicker;
    public Button btnSubmit, btnCancel;
    public String meetingTitle, meetingDescription, conductedFor, meetingVenue;
    public String meetingType, meetingFrequency, meetingDate, meetingTime;
    private AppViewModel mAppViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        etMeetingTitle = findViewById(R.id.etMeetingTitle);
        etMeetingDescription = findViewById(R.id.etMeetingDescription);
        etConductedFor = findViewById(R.id.etConductedFor);
        etMeetingVenue = findViewById(R.id.etMeetingVenue);
        tvDatePicker = findViewById(R.id.tvDatePicker);
        tvTimePicker = findViewById(R.id.tvTimePicker);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeeting();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelMeeting();
            }
        });

        spinnerMeetingFrequency = findViewById(R.id.spinnerMeetingFrequency);
        ArrayAdapter<CharSequence> spinnerMeetingFrequencyAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_meeting_frequency, R.layout.spinner);
        spinnerMeetingFrequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeetingFrequency.setAdapter(spinnerMeetingFrequencyAdapter);

        spinnerMeetingType = findViewById(R.id.spinnerMeetingType);
        ArrayAdapter<CharSequence> spinnerMeetingTypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_meeting_types, R.layout.spinner);
        spinnerMeetingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeetingType.setAdapter(spinnerMeetingTypeAdapter);

        // get a new or existing ViewModel from the ViewModelProvider
        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.tvDatePicker)).setText(dateFormat.format(calendar.getTime()));

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }

    }

    public void showTimePickerDialog(View view) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.show(getSupportFragmentManager(), "time");
    }

    private void setTime(String time) {
        ((TextView) findViewById(R.id.tvTimePicker)).setText(time);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        String time = view.getHour() + ":" + view.getMinute();
        setTime(time);
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, true);
        }
    }

    public void saveMeeting() {
        int meetingId = 1;
        meetingTitle = etMeetingTitle.getText().toString();
        meetingDescription = etMeetingDescription.getText().toString();
        conductedFor = etConductedFor.getText().toString();
        meetingVenue = etMeetingVenue.getText().toString();
        meetingType = spinnerMeetingType.getItemAtPosition(0).toString();
        meetingFrequency = spinnerMeetingFrequency.getItemAtPosition(0).toString();
        meetingDate = tvDatePicker.getText().toString();
        meetingTime = tvTimePicker.getText().toString();

        Meeting meeting = new Meeting(meetingId, meetingTitle, meetingType, meetingDescription,
                meetingDate, meetingTime, meetingVenue, meetingFrequency);

        mAppViewModel.insertMeeting(meeting);
    }

    public void cancelMeeting() {

    }
}
