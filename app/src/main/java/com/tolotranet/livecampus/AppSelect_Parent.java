package com.tolotranet.livecampus;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Tolotra Samuel on 15/08/2016.
 */
public class AppSelect_Parent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String origin = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;
    private Apps_PagerAdapter_Upper mSectionsPagerAdapter;
    private ViewPager mViewPager;
    protected static DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.sign_start_application);


        origin = "appselect"; //because origin is used by differents apps to set their onback pressed

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setIcon(R.drawable.ic_logo_minimini);




         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AppSelect_Parent.this); // because we have to set the navigation nav_view

        Menu menu = navigationView.getMenu();

        MenuItem me_residence = menu.findItem(R.id.me_residence);
        me_residence.setTitle(me_residence.getTitle().toString()+": "+Sign_User_Object.Residence);
        me_residence.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        MenuItem me_room = menu.findItem(R.id.me_room);
        me_room.setTitle(me_room.getTitle().toString()+": "+Sign_User_Object.RoomNumber);
        me_room.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        if(!Sign_User_Object.Apartment.toString().equals("N/A")) {
            me_room.setTitle(me_room.getTitle().toString()+": "+Sign_User_Object.RoomNumber+" "+Sign_User_Object.Apartment);
        }

        MenuItem nav_settings = menu.findItem(R.id.nav_settings);
        MenuItem nav_faq = menu.findItem(R.id.nav_faq);
        MenuItem nav_logout = menu.findItem(R.id.nav_logout);

        MenuItem menuContribute = menu.findItem(R.id.menuContribute);
        MenuItem menuShop = menu.findItem(R.id.menuShop);
        MenuItem menuLeaderboard = menu.findItem(R.id.menuLeaderboard);


        MenuItem menuallapps = menu.findItem(R.id.allapps);
        MenuItem menualive = menu.findItem(R.id.alive);
        MenuItem menufavourite = menu.findItem(R.id.favourite);
        MenuItem menumyday = menu.findItem(R.id.myday);



        nav_settings.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        nav_faq.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        nav_logout.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        menuallapps.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menualive.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menufavourite.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menumyday.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        menuContribute.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menuShop.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menuLeaderboard.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        View header = navigationView.getHeaderView(0); //because we want to edit the textview view in the navigation header

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        //SETTING UP DRAWERHEADER
        String username = getIntent().getStringExtra("Username");
        TextView scoretv = (TextView) header.findViewById(R.id.headerDrawerScore);
        TextView emailtv = (TextView) header.findViewById(R.id.headerDrawerEmail);
        TextView nametv = (TextView) header.findViewById(R.id.headerDrawerName);
        TextView subscoretv = (TextView) header.findViewById(R.id.headerDrawerSubScore);


        scoretv.setText(Sign_User_Object.Score);
        emailtv.setText(Sign_User_Object.Email);
        nametv.setText(Sign_User_Object.Name);
        if(Integer.parseInt(Sign_User_Object.Score) > 1 ) {
            subscoretv.setText("Points");
        }


        mFirebaseAnalytics.setUserId(username);//set user ID


        mSectionsPagerAdapter = new Apps_PagerAdapter_Upper(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        final com.github.clans.fab.FloatingActionButton fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_refresh);
        final com.github.clans.fab.FloatingActionButton fab_add = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_add);
        final com.github.clans.fab.FloatingActionButton fab_ideas = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_ideas);
        final com.github.clans.fab.FloatingActionButton fab_feedback = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_feedback);

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable()){
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else {
                    fab_refresh.startAnimation(AnimationUtils.loadAnimation(AppSelect_Parent.this, R.anim.rotation));
                    Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    Sis_GetDataAsyncTask InitialisetData = new Sis_GetDataAsyncTask();
                    InitialisetData.Target = "apps";
                    InitialisetData.execute(AppSelect_Parent.this);

                }
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable()){
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else {
                    Intent i = new Intent(AppSelect_Parent.this, App_Contribute.class);
                    startActivity(i);

                }
            }
        });
        fab_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable()){
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else {
                    Intent i = new Intent(AppSelect_Parent.this, App_Feedback.class);
                    startActivity(i);

                }
            }
        });
        fab_ideas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable()){
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else {
                    Intent i = new Intent(AppSelect_Parent.this, Mu_Add_Idea.class);
                    startActivity(i);

                }
            }
        });


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static int getUserID() {
        Context c = MyApplication.getAppContext();
        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(c);
        int userID = helper.getUserId();
        //int userID = 1000005;
        return userID;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsMain:
                App_Tools tools = new App_Tools();


                if (isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_LONG).show();
                    //   Event_GetDataAsyncTask getDataTask = new Event_GetDataAsyncTask();
                    //  getDataTask.execute(this);
                } else {
                    Toast.makeText(getApplicationContext(), "check Internet Connection", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    private class MyInfoMenuClickListener implements MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(item.getTitle().equals("Settings")) {
                Intent i = new Intent(AppSelect_Parent.this, Settings_mainActivity.class);
                startActivity(i);
            }else if(item.getTitle().equals("Help")){
                Mu_App.mu_category = "FAQ";
                Intent i = new Intent(AppSelect_Parent.this, Mu_SpreadSheetActivity.class);
                startActivity(i);

            }

            else if(item.getTitle().equals("Logout")){
                Sign_DatabaseHelper helper = new Sign_DatabaseHelper(AppSelect_Parent.this);
                helper.cleanTable();
                Intent i = new Intent(AppSelect_Parent.this, Sign_check_mail.class);
                startActivity(i);
            }

            else if(item.getTitle().equals("Leaderboard")){
                Intent i = new Intent(AppSelect_Parent.this, Lead_SpreadSheetActivity.class);
                startActivity(i);
            }

            else if(item.getTitle().equals("Exchange Points")) {
                Intent i = new Intent(AppSelect_Parent.this, Bubble_SpreadSheetActivity.class);
                startActivity(i);
            }
              else if(item.getTitle().equals("Contribute and get points")){
                Intent i = new Intent(AppSelect_Parent.this, App_Contribute.class);
                startActivity(i);

            } else if(item.getTitle().equals("ALU ALIVE")){
                Intent i = new Intent(AppSelect_Parent.this, Feed_MainActivity.class);
                startActivity(i);

            }else {
                Intent i = new Intent(AppSelect_Parent.this, Sis_DetailListViewOwner.class);
                startActivity(i);

            }
            return false;
        }
    }
}
