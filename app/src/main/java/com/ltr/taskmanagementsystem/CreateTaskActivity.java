package com.ltr.taskmanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModel;
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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener  {

    public EditText etTaskSubject, etTaskDescription, etResponsible, etAccountable;
    public TextView tvDatePicker;
    public EditText etProject, etDepartment, etTeamName, etRemarks;
    public Spinner spinnerTaskCategory, spinnerPriority, spinnerReminderRequired, spinnerTaskStatus, spinnerPercentComplete;
    public Button btnCancel, btnSubmit;
    public String taskSubject, taskDescription, responsible, accountable;
    public String date;
    public String project, department, teamName, remarks;
    public String taskCategory, priority, reminderRequired, taskStatus, percentComplete;
    private AppViewModel mAppViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        etTaskSubject = findViewById(R.id.etTaskSubject);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        etResponsible = findViewById(R.id.etResponsible);
        etAccountable = findViewById(R.id.etAccountable);
        tvDatePicker = findViewById(R.id.tvDatePicker);
        etProject = findViewById(R.id.etProject);
        etDepartment = findViewById(R.id.etDepartment);
        etTeamName = findViewById(R.id.etTeamName);
        etRemarks = findViewById(R.id.etRemarks);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveTask();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancelTask();
            }
        });

        spinnerTaskCategory = findViewById(R.id.spinnerTaskCategory);
        ArrayAdapter<CharSequence> spinnerTaskCategoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_task_categories, R.layout.spinner);
        spinnerTaskCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskCategory.setAdapter(spinnerTaskCategoryAdapter);

        spinnerPriority = findViewById(R.id.spinnerPriority);
        ArrayAdapter<CharSequence> spinnerPriorityAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_priority, R.layout.spinner);
        spinnerPriorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriorityAdapter);

        spinnerReminderRequired = findViewById(R.id.spinnerReminderRequired);
        ArrayAdapter<CharSequence> spinnerReminderRequiredAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_reminder_required, R.layout.spinner);
        spinnerReminderRequiredAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReminderRequired.setAdapter(spinnerReminderRequiredAdapter);

        spinnerTaskStatus = findViewById(R.id.spinnerTaskStatus);
        ArrayAdapter<CharSequence> spinnerTaskStatusAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_task_status, R.layout.spinner);
        spinnerTaskStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskStatus.setAdapter(spinnerTaskStatusAdapter);

        spinnerPercentComplete = findViewById(R.id.spinnerPercentComplete);
        ArrayAdapter<CharSequence> spinnerPercentCompleteAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_percent_complete, R.layout.spinner);
        spinnerPercentCompleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPercentComplete.setAdapter(spinnerPercentCompleteAdapter);

        // get a new or existing ViewModel from the ViewModelProvider
        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        tvDatePicker.setText(dateFormat.format(calendar.getTime()));

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

    public void saveTask() {
        int taskId = 1;
        taskSubject = etTaskSubject.getText().toString();
        taskDescription = etTaskDescription.getText().toString();
        responsible = etResponsible.getText().toString();
        accountable = etAccountable.getText().toString();
        date = tvDatePicker.getText().toString();
        project = etProject.getText().toString();
        department = etDepartment.getText().toString();
        teamName = etTeamName.getText().toString();
        remarks = etRemarks.getText().toString();
        taskCategory = spinnerTaskCategory.getItemAtPosition(0).toString();
        priority = spinnerPriority.getItemAtPosition(0).toString();
        reminderRequired = spinnerReminderRequired.getItemAtPosition(0).toString();
        taskStatus = spinnerTaskStatus.getItemAtPosition(0).toString();
        percentComplete = spinnerPercentComplete.getItemAtPosition(0).toString();

        Task task = new Task(taskId, taskSubject, priority, date, taskStatus, taskDescription,
                taskCategory, responsible, accountable, department, project);
        mAppViewModel.insertTask(task);

    }

    public void cancelTask() {

    }


}