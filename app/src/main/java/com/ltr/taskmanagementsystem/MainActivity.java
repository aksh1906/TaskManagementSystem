package com.ltr.taskmanagementsystem;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton fab;
    public RecyclerView recyclerView;
    private AppViewModel mAppViewModel;
    public LiveData<List<Task>> ongoingTasks;
    public static final int CREATE_TASK_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivityForResult(intent, CREATE_TASK_ACTIVITY_REQUEST_CODE);
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        final TaskAdapter adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        try {
            mAppViewModel.getOngoingTasks().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable final List<Task> tasks) {
                    adapter.setTasks(tasks);
                }
            });
        } catch (Exception e) {
            //
        }

    }
}
