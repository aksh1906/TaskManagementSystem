package com.ltr.taskmanagementsystem;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private AppViewModel mAppViewModel;
    private LiveData<List<Task>> ongoingTasks;
    private static final int CREATE_TASK_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFAB();
        setupToolbar();
        initNavigationDrawer();
        setupRecyclerView();
    }

    // setup the toolbar
    private void setupToolbar() {
        toolbar = findViewById(R.id.tb_main_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null); // remove the default title for the toolbar
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Home");

        // add the navigation drawer button to the toolbar
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


    }

    // create the navigation drawer
    private void initNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);

        if(mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
    }

    // populate the navigation drawer
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId())  {
                            //
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // open navigation drawer when the hamnburger button in the toolbar is clicked
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // check if the back button is pressed
    // and close the navigation drawer if it is
    public void onBackPressed() {
        if(isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    // check if the navigation drawer is open
    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    // close the navigation drawer
    protected void closeNavDrawer() {
        if(mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    // create the floating action button used to create a new task or meeting
    private void setupFAB() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivityForResult(intent, CREATE_TASK_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    // setup the recyclerview used to show the cards
    // containing brief info about the user's currently ongoing tasks
    private void setupRecyclerView() {
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
