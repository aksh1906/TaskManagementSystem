package com.ltr.taskmanagementsystem;

import android.app.Activity;
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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private SmoothActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView toolbarTitle, tvTaskTitle;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private AppViewModel mAppViewModel;
    private LiveData<List<Task>> ongoingTasks;
    Intent intent;
    private static final int CREATE_TASK_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createFAB();
        createToolbar();
        createNavigationDrawer();
        setupDrawerListener();
        createRecyclerView();
    }

    // setup the toolbar
    private void createToolbar() {
        toolbar = findViewById(R.id.tb_main_activity);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null); // remove the default title for the toolbar
            toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText("Home");
        }
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
    private void createNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItem(item.getItemId());
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // open navigation drawer when the hamburger button in the toolbar is clicked
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
    public boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    // close the navigation drawer
    protected void closeNavDrawer() {
        if(mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // set the DrawerListener
    private void setupDrawerListener() {
        mDrawerToggle = new SmoothActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        switch (position) {
            case R.id.nav_home:
                closeNavDrawer();
                break;

            case R.id.nav_view_tasks:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(MainActivity.this, ViewAllTasksActivity.class);
                        startActivity(intent);
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_create_task:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                        startActivity(intent);
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_view_meetings:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(MainActivity.this, ViewAllMeetingsActivity.class);
                        startActivity(intent);
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_create_meeting:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(MainActivity.this, CreateMeetingActivity.class);
                        startActivity(intent);
                    }
                });
                mDrawerLayout.closeDrawers();
                break;
        }
    }

    // create the floating action button used to create a new task or meeting
    private void createFAB() {
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
    public void createRecyclerView() {
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

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int task_id = Integer.parseInt(((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tvTaskId)).getText().toString());
                        intent = new Intent(MainActivity.this, ViewSingleTaskActivity.class);
                        intent.putExtra("task id", task_id);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //
                    }
                })
        );
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                           Toolbar toolbar, int openDrawerContentDescRes,
                                           int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }

    }
}
