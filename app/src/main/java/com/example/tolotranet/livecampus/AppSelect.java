package com.example.tolotranet.livecampus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Tolotra Samuel on 15/08/2016.
 */
public class AppSelect extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;
    private Apps_SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appselect);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        String username = getIntent().getStringExtra("Username");
        TextView tv = (TextView) findViewById(R.id.TVusername);
       // tv.setText(username);
        mFirebaseAnalytics.setUserId(username);//set user ID




        mSectionsPagerAdapter = new Apps_SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }





    public static  int getUserID(){
        Context c = MyApplication.getAppContext();
        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(c);
       int userID  = helper.getUserId();
       //int userID = 1000005;
        return userID;
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mProgress!=null && mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

}
