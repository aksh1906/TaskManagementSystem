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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class ViewAllMeetingsActivity extends AppCompatActivity {

    private Spinner spinnerRole;
    private RecyclerView recyclerView;
    private MeetingAdapter adapter;
    private AppViewModel mAppViewModel;
    private Intent intent;
    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SmoothActionBarDrawerToggle mDrawerToggle;
    private FloatingActionButton fab;
    private static final int CREATE_MEETING_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_meetings);

        spinnerRole = findViewById(R.id.spinner_attendee_creator);
        ArrayAdapter<CharSequence> spinnerRoleAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_creator_attendee, R.layout.spinner);
        spinnerRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerRoleAdapter);

        createToolbar();
        createFAB();
        createNavigationDrawer();
        setupDrawerListener();
        spinnerSelectedItemListener();
    }

    private void spinnerSelectedItemListener() {
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long l) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                if(selectedItem.equals("Attended")) {
                    getMeetingsAttended();
                } else if(selectedItem.equals("Created")) {
                    getMeetingsCreated();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });
    }

    public void getMeetingsAttended() {
        recyclerView = findViewById(R.id.recyclerviewMeeting);
        adapter = new MeetingAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        try {
            //
        } catch (Exception e) {
            //
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int meeting_id = Integer.parseInt(((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tvMeetingId)).getText().toString());
                        intent = new Intent(ViewAllMeetingsActivity.this, ViewSingleMeetingActivity.class);
                        intent.putExtra("meeting id", meeting_id);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //
                    }
                })
        );
    }

    public void getMeetingsCreated() {
        recyclerView = findViewById(R.id.recyclerviewMeeting);
        adapter = new MeetingAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        try {
            mAppViewModel.getAllCreatedMeetings().observe(this, new Observer<List<Meeting>>() {
                @Override
                public void onChanged(@Nullable List<Meeting> meetings) {
                    adapter.setMeetings(meetings);
                }
            });
        } catch (Exception e) {
            //
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int meeting_id = Integer.parseInt(((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tvMeetingId)).getText().toString());
                        intent = new Intent(ViewAllMeetingsActivity.this, ViewSingleMeetingActivity.class);
                        intent.putExtra("meeting id", meeting_id);
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
        mToolbar = findViewById(R.id.tb_view_all_meetings);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(null); // remove the default title for the toolbar
            toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText("View All Meetings");
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
        mDrawerToggle = new ViewAllMeetingsActivity.SmoothActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    private void selectItem(int position) {
        switch (position) {
            case R.id.nav_home:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllMeetingsActivity.this, MainActivity.class);
                        startActivity(intent);
                        ViewAllMeetingsActivity.this.finish();
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_view_tasks:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllMeetingsActivity.this, ViewAllTasksActivity.class);
                        startActivity(intent);
                        ViewAllMeetingsActivity.this.finish();
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_create_task:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllMeetingsActivity.this, CreateTaskActivity.class);
                        startActivity(intent);
                        ViewAllMeetingsActivity.this.finish();
                    }
                });
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_view_meetings:
                closeNavDrawer();
                break;

            case R.id.nav_create_meeting:
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(ViewAllMeetingsActivity.this, CreateMeetingActivity.class);
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
                Intent intent = new Intent(ViewAllMeetingsActivity.this, CreateMeetingActivity.class);
                startActivityForResult(intent, CREATE_MEETING_ACTIVITY_REQUEST_CODE);
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
