package com.ltr.taskmanagementsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener  {

    private EditText etTaskSubject, etTaskDescription, etResponsible, etAccountable;
    private TextView tvDatePicker, tvToolbarTitle;
    private EditText etProject, etDepartment, etTeamName, etRemarks;
    private Spinner spinnerTaskCategory, spinnerPriority;
    private Button btnCancel, btnSubmit;
    private Toolbar mToolbar;
    private String taskSubject, taskDescription, responsible, accountable;
    private String dateCreated, dateExpected, dateActual, meetingLinkedWith;
    private String project, department, teamName, remarks, creator;
    private String taskCategory, priority, reminderRequired, taskStatus, percentComplete;
    private AppViewModel mAppViewModel;

    // public static final String URL_SAVE_NAME = "http://192.168.2.6:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        etTaskSubject = findViewById(R.id.etTaskSubject);
        etTaskSubject.addTextChangedListener(subjectWatcher);

        etTaskDescription = findViewById(R.id.etTaskDescription);

        etResponsible = findViewById(R.id.etResponsible);
        etResponsible.addTextChangedListener(responsibleWatcher);

        etAccountable = findViewById(R.id.etAccountable);
        etAccountable.addTextChangedListener(accountableWatcher);

        tvDatePicker = findViewById(R.id.tvDatePicker);
        tvDatePicker.addTextChangedListener(plannedFinishDateWatcher);

        etProject = findViewById(R.id.etProject);
        etProject.addTextChangedListener(projectWatcher);

        etDepartment = findViewById(R.id.etDepartment);
        etDepartment.addTextChangedListener(departmentWatcher);

        etTeamName = findViewById(R.id.etTeamName);
        etTeamName.addTextChangedListener(teamNameWatcher);

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

        createToolbar();

        // check if all required fields are empty
        // used here to disable the button on activity creation
        checkAllRequiredFieldsForEmptyValues();


        // get a new or existing ViewModel from the ViewModelProvider
        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    private void createToolbar() {
        mToolbar = findViewById(R.id.tbCreateNewTask);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(null); // remove the default title for the toolbar
            tvToolbarTitle = findViewById(R.id.toolbar_title);
            tvToolbarTitle.setText("Create New Activity");
        }

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
        taskSubject = etTaskSubject.getText().toString();
        taskDescription = etTaskDescription.getText().toString();
        responsible = etResponsible.getText().toString();
        accountable = etAccountable.getText().toString();
        dateExpected = tvDatePicker.getText().toString();
        project = etProject.getText().toString();
        department = etDepartment.getText().toString();
        teamName = etTeamName.getText().toString();
        remarks = etRemarks.getText().toString();
        priority = spinnerPriority.getSelectedItem().toString();
        dateCreated = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        if(taskDescription.equals("")) {
            taskDescription = null;
        }

        if(remarks.equals("")) {
            remarks = null;
        }

        percentComplete = "0";
        taskStatus = "To be started";
        reminderRequired = "No";
        creator = "Akshat"; // placeholder
        taskCategory = "cat1"; // placeholder

        Task task = new Task(taskSubject, priority, dateCreated, dateExpected, taskStatus, taskDescription,
                taskCategory, responsible, accountable, department, project, teamName, remarks, dateActual, creator,
                meetingLinkedWith, reminderRequired, percentComplete);
        mAppViewModel.insertTask(task);

        Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
        startActivity(intent);
        CreateTaskActivity.this.finish();
        Toast toast = Toast.makeText(getApplicationContext(), "Task Created", Toast.LENGTH_LONG);
        toast.show();
    }

    public void cancelTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle("Cancel")
                .setMessage("Are you sure you want to cancel Task Creation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CreateTaskActivity.this.finish();
                        Toast toast = Toast.makeText(getApplicationContext(), "Task not created", Toast.LENGTH_SHORT);
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
    private TextWatcher subjectWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkSubjectIsEmpty();
        }
    };

    private TextWatcher plannedFinishDateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkDateIsEmpty();
        }
    };

    private TextWatcher responsibleWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkResponsibleIsEmpty();
        }
    };

    private TextWatcher accountableWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkAccountableIsEmpty();
        }
    };

    private TextWatcher projectWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkProjectIsEmpty();
        }
    };

    private TextWatcher departmentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkDepartmentIsEmpty();
        }
    };

    private TextWatcher teamNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkTeamNameIsEmpty();
        }
    };


    // Methods for checking if individual required fields are empty

    private void checkSubjectIsEmpty() {
        taskSubject = etTaskSubject.getText().toString();
        if(taskSubject.isEmpty()) {
            etTaskSubject.setError("Field cannot be empty!");
        }

        checkAllRequiredFieldsForEmptyValues();
    }

    private void checkDateIsEmpty() {
        dateExpected = tvDatePicker.getText().toString();
        if(tvDatePicker.getText() == "") {
            tvDatePicker.setError("Field cannot be empty!");
        }
        checkAllRequiredFieldsForEmptyValues();
    }

    private void checkResponsibleIsEmpty() {
        responsible = etResponsible.getText().toString();
        if(responsible.isEmpty()) {
            etResponsible.setError("Field cannot be empty!");
        }
        checkAllRequiredFieldsForEmptyValues();
    }

    private void checkAccountableIsEmpty() {
        accountable = etAccountable.getText().toString();
        if(accountable.isEmpty()) {
            etAccountable.setError("Field cannot be empty!");
        }
        checkAllRequiredFieldsForEmptyValues();
    }

    private void checkProjectIsEmpty() {
        project = etProject.getText().toString();
        if(project.isEmpty()) {
            etProject.setError("Field cannot be empty!");
        }
        checkAllRequiredFieldsForEmptyValues();
    }

    private void checkDepartmentIsEmpty() {
        department = etDepartment.getText().toString();
        if(department.isEmpty()) {
            etDepartment.setError("Field cannot be empty!");
        }
        checkAllRequiredFieldsForEmptyValues();
    }

    private void checkTeamNameIsEmpty() {
        teamName = etTeamName.getText().toString();
        if(teamName.isEmpty()) {
            etTeamName.setError("Field cannot be empty!");
        }
        checkAllRequiredFieldsForEmptyValues();
    }


    // check all fields if they are empty, if not, enable the button
    private void checkAllRequiredFieldsForEmptyValues() {
        btnSubmit = findViewById(R.id.btnSubmit);

        taskSubject = etTaskSubject.getText().toString();
        dateExpected = tvDatePicker.getText().toString();
        responsible = etResponsible.getText().toString();
        accountable = etAccountable.getText().toString();
        project = etProject.getText().toString();
        department = etDepartment.getText().toString();
        teamName = etTeamName.getText().toString();

        if (taskSubject.equals("") || dateExpected.equals("") || responsible.equals("") || accountable.equals("")
                || project.equals("") || department.equals("") || teamName.equals("")) {
            btnSubmit.setBackgroundResource(R.drawable.button_rounded_corners_disabled);
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setBackgroundResource(R.drawable.button_rounded_corners_enabled); // change the color of the button to show the user that it's enabled
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