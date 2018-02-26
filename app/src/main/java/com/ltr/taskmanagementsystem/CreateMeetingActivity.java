package com.ltr.taskmanagementsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateMeetingActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public EditText etMeetingTitle, etMeetingDescription, etConductedFor, etMeetingVenue, etParticipants;
    public Spinner spinnerMeetingType, spinnerMeetingFrequency;
    public TextView tvDatePicker, tvTimePicker;
    public MultiAutoCompleteTextView actvParticipants;
    public Button btnSubmit, btnCancel;
    public String meetingTitle, meetingDescription, conductedFor, meetingVenue;
    public String meetingType, meetingFrequency, meetingDate, meetingTime, meetingParticipants;
    private AppViewModel mAppViewModel;
    private Toolbar mToolbar;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        etMeetingTitle = findViewById(R.id.etMeetingTitle);
        etMeetingTitle.addTextChangedListener(titleWatcher);

        etMeetingDescription = findViewById(R.id.etMeetingDescription);

        etConductedFor = findViewById(R.id.etConductedFor);
        etConductedFor.addTextChangedListener(conductedForWatcher);

        etMeetingVenue = findViewById(R.id.etMeetingVenue);
        etMeetingVenue.addTextChangedListener(venueWatcher);

        tvDatePicker = findViewById(R.id.tvDatePicker);
        tvDatePicker.addTextChangedListener(dateWatcher);

        tvTimePicker = findViewById(R.id.tvTimePicker);
        tvTimePicker.addTextChangedListener(timeWatcher);

        actvParticipants = findViewById(R.id.actvParticipants);
        actvParticipants.addTextChangedListener(participantsWatcher);

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

        String[] placeholder = getResources().getStringArray(R.array.placeholder_participants);
        ArrayAdapter<String> actvParticipantsAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, placeholder);
        actvParticipants.setAdapter(actvParticipantsAdapter);
        actvParticipants.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // get a new or existing ViewModel from the ViewModelProvider
        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);


        createToolbar();
        checkAllFieldsForEmptyValues();
    }

    private void createToolbar() {
        mToolbar = findViewById(R.id.tbCreateNewMeeting);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(null); // remove the default title for the toolbar
            tvToolbarTitle = findViewById(R.id.toolbar_title);
            tvToolbarTitle.setText("Create New Meeting");
        }

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
        String creator = "Akshat"; // placeholder

        Meeting meeting = new Meeting(meetingId, meetingTitle, meetingType, meetingDescription,
                meetingDate, meetingTime, meetingVenue, creator, conductedFor, meetingFrequency);

        mAppViewModel.insertMeeting(meeting);

        Intent intent = new Intent(CreateMeetingActivity.this, MainActivity.class);
        startActivity(intent);
        CreateMeetingActivity.this.finish();
        Toast toast = Toast.makeText(getApplicationContext(), "Meeting Created", Toast.LENGTH_LONG);
        toast.show();
    }

    public void cancelMeeting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle("Cancel")
                .setMessage("Are you sure you want to cancel Meeting Creation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CreateMeetingActivity.this.finish();
                        Toast toast = Toast.makeText(getApplicationContext(), "Meeting not created", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // TextWatchers for all fields
    private TextWatcher titleWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkTitleIsEmpty();
        }
    };

    private TextWatcher conductedForWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkConductedForIsEmpty();
        }
    };

    private TextWatcher dateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkDateIsEmpty();
        }
    };

    private TextWatcher timeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkTimeIsEmpty();
        }
    };

    private TextWatcher venueWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkVenueIsEmpty();
        }
    };

    private TextWatcher participantsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkParticipantsIsEmpty();
        }
    };

    // Methods for checking if individual required fields are empty

    private void checkTitleIsEmpty() {
        meetingTitle = etMeetingTitle.getText().toString();
        if(meetingTitle.isEmpty()) {
            etMeetingTitle.setError("Field cannot be empty!");
        }

        checkAllFieldsForEmptyValues();
    }

    private void checkConductedForIsEmpty() {
        conductedFor = etConductedFor.getText().toString();
        if(conductedFor.isEmpty()) {
            etConductedFor.setError("Field cannot be empty!");
        }

        checkAllFieldsForEmptyValues();
    }

    private void checkDateIsEmpty() {
        meetingDate = tvDatePicker.getText().toString();
        if(meetingDate.isEmpty()) {
            tvDatePicker.setError("Field cannot be empty!");
        }

        checkAllFieldsForEmptyValues();
    }

    private void checkTimeIsEmpty() {
        meetingTime = tvTimePicker.getText().toString();
        if(meetingTime.isEmpty()) {
            tvTimePicker.setError("Field cannot be empty!");
        }

        checkAllFieldsForEmptyValues();
    }

    private void checkVenueIsEmpty() {
        meetingVenue = etMeetingVenue.getText().toString();
        if(meetingVenue.isEmpty()) {
            etMeetingVenue.setError("Field cannot be empty!");
        }

        checkAllFieldsForEmptyValues();
    }

    private void checkParticipantsIsEmpty() {
        meetingParticipants = actvParticipants.getText().toString();
        if(meetingParticipants.isEmpty()) {
            actvParticipants.setError("Field cannot be empty!");
        }

        checkAllFieldsForEmptyValues();
    }

    // check all fields for if they are empty, if not, enable the button
    private void checkAllFieldsForEmptyValues() {
        btnSubmit = findViewById(R.id.btnSubmit);

        meetingTitle = etMeetingTitle.getText().toString();
        conductedFor = etConductedFor.getText().toString();
        meetingDate = tvDatePicker.getText().toString();
        meetingTime = tvTimePicker.getText().toString();
        meetingVenue = etMeetingVenue.getText().toString();
        meetingParticipants = actvParticipants.getText().toString();

        if(meetingTitle.equals("") || conductedFor.equals("") || meetingDate.equals("") ||
                meetingTime.equals("") || meetingVenue.equals("") || meetingParticipants.equals("")) {
            btnSubmit.setBackgroundResource(R.drawable.button_rounded_corners_disabled);
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setBackgroundResource(R.drawable.button_rounded_corners_enabled);
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        // used to clear focus from an EditText when the user clicks outside of it

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
