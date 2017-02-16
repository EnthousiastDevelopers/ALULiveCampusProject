package com.tolotranet.livecampus;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Tolotra Samuel on 15/08/2016.
 */
public class AppSelect extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String origin = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;
    private Apps_SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    protected static DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.sign_start_application);

        origin = "appselect";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_minimini);

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AppSelect.this); // because we have to set the navigation nav_view

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


        mSectionsPagerAdapter = new Apps_SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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


                if (tools.isNetworkAvailable()) {
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
}
