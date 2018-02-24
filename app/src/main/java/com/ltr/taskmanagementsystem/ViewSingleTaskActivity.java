package com.ltr.taskmanagementsystem;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ViewSingleTaskActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private Intent intent;
    private FloatingActionButton fab;
    private TextView tvSubject, tvType, tvDescription, tvDateCreated, tvPriority;
    private TextView tvPlannedFinishDate, tvStatus, tvPercentComplete, etPlannedFinishDate;
    private TextView tvCreator, tvResponsible, tvAccountable, tvProject;
    private TextView tvDepartment, tvTeamName, tvRemarks, tvActualFinishDate;
    private EditText etRemarks;
    private Spinner spinnerStatus, spinnerPercentComplete;
    private LinearLayout llActualFinishDate, llButtons;
    private Button btnSubmit, btnCancel;
    private String subject, type, description, dateCreated, plannedFinishDate;
    private String creator, responsible, accountable, project, department;
    private String teamName, remarks, status, percentComplete, actualFinishDate;
    private String intentSubject;
    private AppViewModel mAppViewModel;
    private AppDatabase mDatabase;
    private final int EDIT_TASK_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_task);

        createToolbar();
        populateTaskDetails();
        createFAB();
    }

    private void createToolbar() {
        mToolbar = findViewById(R.id.tbViewSingleTaskActivity);
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

    // create the floating action button used to create a new task or meeting
    private void createFAB() {
        fab = findViewById(R.id.fabEditTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goIntoEditMode();
            }
        });
    }

    private void populateTaskDetails() {
        tvSubject = findViewById(R.id.tvTaskSubjectData);
        tvType = findViewById(R.id.tvTaskCategoryData);
        tvDescription = findViewById(R.id.tvTaskDescriptionData);
        tvPriority = findViewById(R.id.tvTaskPriorityData);
        tvPlannedFinishDate = findViewById(R.id.tvTaskPlannedFinishDateData);
        tvDateCreated = findViewById(R.id.tvTaskDateCreatedData);
        tvActualFinishDate = findViewById(R.id.tvActualFinishDate);
        tvStatus = findViewById(R.id.tvTaskStatusData);
        tvPercentComplete = findViewById(R.id.tvTaskPercentCompleteData);
        tvCreator = findViewById(R.id.tvTaskCreatorData);
        tvResponsible = findViewById(R.id.tvTaskResponsibleData);
        tvAccountable = findViewById(R.id.tvTaskAccountableData);
        tvProject = findViewById(R.id.tvTaskProjectData);
        tvDepartment = findViewById(R.id.tvTaskDepartmentData);
        tvTeamName = findViewById(R.id.tvTaskTeamNameData);
        tvRemarks = findViewById(R.id.tvTaskRemarksData);
        llActualFinishDate = findViewById(R.id.llActualFinishDate);

//        if(actualFinishDate != null) {
//            llActualFinishDate.setVisibility(View.VISIBLE);
//        }

        // MAKESHIFT SOLUTION FOR NOW. WILL BE CHANGED LATER TO USE THE VIEWMODEL
        // INSTEAD OF USING THE DAO DIRECTLY

//        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        intent = getIntent();
        intentSubject = intent.getStringExtra(MainActivity.MESSAGE);
        mDatabase = AppDatabase.getDatabase(getApplicationContext());
        mDatabase.taskDao().getSingleTaskData(intentSubject).observe(this, new Observer<Task>() {
            @Override
            public void onChanged(@Nullable Task task) {
                tvSubject.setText(task.getSubject());
                tvType.setText(task.getCategory());
                tvDescription.setText(task.getDescription());
                tvPriority.setText(task.getPriority());
                tvPlannedFinishDate.setText(task.getExp_finish_date());
                tvDateCreated.setText(task.getDate_created());
                if(task.getActual_finish_date() != null) {
                    llActualFinishDate.setVisibility(View.VISIBLE);
                    tvActualFinishDate.setText(task.getActual_finish_date());
                }
                tvStatus.setText(task.getStatus());
                tvPercentComplete.setText(task.getPercent_complete());
                tvCreator.setText(task.getCreator());
                tvResponsible.setText(task.getResponsible());
                tvAccountable.setText(task.getAccountable());
                tvProject.setText(task.getConcerned_project());
                tvDepartment.setText(task.getConcerned_dept());
                tvTeamName.setText(task.getTeam_name());
                tvRemarks.setText(task.getRemarks());
            }
        });
//        mAppViewModel.mRepository.subject = "test";
//        mAppViewModel.setSubject();
//        mAppViewModel.getSingleTask().observe(this, new Observer<Task>() {
//            @Override
//            public void onChanged(@Nullable Task task) {
//
//
//
//            }
//        });
    }

    public void goIntoEditMode() {

        String remarksText, plannedFinishDateText, statusText, percentCompleteText;

        fab.setVisibility(View.GONE);

        llButtons = findViewById(R.id.llButtons);
        llButtons.setVisibility(View.VISIBLE);

        tvRemarks = findViewById(R.id.tvTaskRemarksData);
        remarksText = tvRemarks.getText().toString();
        tvRemarks.setVisibility(View.GONE);
        etRemarks = findViewById(R.id.etTaskRemarksData);
        etRemarks.setVisibility(View.VISIBLE);
        etRemarks.setText(remarksText);

        tvPlannedFinishDate = findViewById(R.id.tvTaskPlannedFinishDateData);
        plannedFinishDateText = tvPlannedFinishDate.getText().toString();
        tvPlannedFinishDate.setVisibility(View.GONE);
        etPlannedFinishDate = findViewById(R.id.tvDatePicker);
        etPlannedFinishDate.setVisibility(View.VISIBLE);
        etPlannedFinishDate.setText(plannedFinishDateText);

        tvStatus = findViewById(R.id.tvTaskStatusData);
        tvStatus.setVisibility(View.GONE);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerStatus.setVisibility(View.VISIBLE);

        tvPercentComplete = findViewById(R.id.tvTaskPercentCompleteData);
        tvPercentComplete.setVisibility(View.GONE);
        spinnerPercentComplete = findViewById(R.id.spinnerPercentComplete);
        spinnerPercentComplete.setVisibility(View.VISIBLE);

        activateButtons();
    }

    public void showDatePickerDialog(View view) {
        CreateTaskActivity.DatePickerFragment fragment = new CreateTaskActivity.DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        etPlannedFinishDate.setText(dateFormat.format(calendar.getTime()));

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

    public void activateButtons() {

        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

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
        btnSubmit.setBackgroundResource(R.drawable.button_rounded_corners_enabled);
        btnSubmit.setEnabled(true);
    }

    public void saveTask() {

    }

    public void cancelTask() {
        //
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
