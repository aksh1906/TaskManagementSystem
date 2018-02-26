package com.ltr.taskmanagementsystem;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.List;

public class ViewAllTasksActivity extends AppCompatActivity {

    private Spinner spinnerRole;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private SmoothActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private AppViewModel mAppViewModel;
    Intent intent;
    TaskAdapter adapter;
    private static final int CREATE_TASK_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tasks);

        spinnerRole = findViewById(R.id.spinner_responsible_accountable);
        ArrayAdapter<CharSequence> spinnerRoleAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_responsible_accountable, R.layout.spinner);
        spinnerRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerRoleAdapter);

        createFAB();
        createToolbar();
        createNavigationDrawer();
        setupDrawerListener();
        spinnerSelectedItemListener();
    }

    private void spinnerSelectedItemListener() {
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                if(selectedItem.equals("Responsible")) {
                    getTasksResponsibleFor();
                } else if(selectedItem.equals("Accountable")) {
                    getTasksAccountableFor();
                } else if(selectedItem.equals("Created")) {
                    getTasksCreated();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }
        });
    }

    public void getTasksResponsibleFor() {
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        try {
            mAppViewModel.getAllResponsibleTasks().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
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
                        intent = new Intent(ViewAllTasksActivity.this, ViewSingleTaskActivity.class);
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

    public void getTasksAccountableFor() {
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        try {
            mAppViewModel.getAllAccountableTasks().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
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
                        intent = new Intent(ViewAllTasksActivity.this, ViewSingleTaskActivity.class);
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

    public void getTasksCreated() {
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        try {
            mAppViewModel.getAllCreatedTasks().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
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
                        intent = new Intent(ViewAllTasksActivity.this, ViewSingleTaskActivity.class);
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

    // create the toolbar
    private void createToolbar() {
        mToolbar = findViewById(R.id.tb_main_activity);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(null); // remove the default title for the toolbar
            toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText("View All Tasks");
        }

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
        mDrawerToggle = new SmoothActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        switch (position) {
            case R.id.nav_home:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllTasksActivity.this, MainActivity.class);
                        startActivity(intent);
                        ViewAllTasksActivity.this.finish();
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_view_tasks:
                closeNavDrawer();
                break;

            case R.id.nav_create_task:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllTasksActivity.this, CreateTaskActivity.class);
                        startActivity(intent);
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_view_meetings:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllTasksActivity.this, ViewAllMeetingsActivity.class);
                        startActivity(intent);
                        ViewAllTasksActivity.this.finish();
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_create_meeting:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllTasksActivity.this, CreateMeetingActivity.class);
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
                Intent intent = new Intent(ViewAllTasksActivity.this, CreateTaskActivity.class);
                startActivityForResult(intent, CREATE_TASK_ACTIVITY_REQUEST_CODE);
            }
        });
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
