package com.tolotranet.livecampus;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Tolotra Samuel on 15/08/2016.
 */
public class AppSelect_Child_Fragment extends Fragment {

    public static String origin = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;
    private Apps_PagerAdapter_Child mSectionsPagerAdapter;
    private ViewPager mViewPager;
    protected static DrawerLayout drawer;


    private final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.appselect, container, false);
        int position = getArguments().getInt(ARG_SECTION_NUMBER);

        mSectionsPagerAdapter = new Apps_PagerAdapter_Child(getChildFragmentManager(), getActivity());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return rootView;
    }


    public static int getUserID() {
        Context c = MyApplication.getAppContext();
        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(c);
        int userID = helper.getUserId();
        //int userID = 1000005;
        return userID;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsMain:
                App_Tools tools = new App_Tools();


                if (tools.isNetworkAvailable()) {
                    Toast.makeText(getActivity(), "Updating.....", Toast.LENGTH_LONG).show();
                    //   Event_GetDataAsyncTask getDataTask = new Event_GetDataAsyncTask();
                    //  getDataTask.execute(this);
                } else {
                    Toast.makeText(getActivity(), "check Internet Connection", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


