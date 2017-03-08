package com.tolotranet.livecampus.App;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.MyApplication;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sign.Sign_DatabaseHelper;

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

        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(getResources().getColor( R.color.blue_alu), getResources().getColor(R.color.red_alu));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red_alu));


        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            String a = tab.getText().toString();

            View tab_color_selector = tab.getCustomView();

                tab_color_selector = LayoutInflater.from(getContext()).inflate(R.layout.tab_color_selector, null);

            RelativeLayout containerLT = (RelativeLayout) tab_color_selector.findViewById(R.id.containerLT);

            TextView tab_label = (TextView) tab_color_selector.findViewById(R.id.TabTitle);
            TextView separator = (TextView) tab_color_selector.findViewById(R.id.rightSeparator);
            if(i!= tabLayout.getTabCount()-1) {
                separator.setVisibility(View.VISIBLE);
                separator.setTextColor(ContextCompat.getColor(getContext(),R.color.blue_alu));
            }
            tab_label.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_alu));
            tab_label.setText(a);
            tab_label.setTextSize(15);
            tab_label.setHeight(tabLayout.getHeight());
            containerLT.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            tab_label.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            tab_color_selector.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));


                tab.setCustomView(tab_color_selector);

        }




        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if(tab.getPosition() > 0) {
                    ((TextView) tabLayout.getTabAt(tab.getPosition() - 1).getCustomView().findViewById(R.id.rightSeparator)).setTextColor(ContextCompat.getColor(getContext(), R.color.red_alu));
                }
                ((TextView) tab.getCustomView().findViewById(R.id.TabTitle)).setTextColor(ContextCompat.getColor(getContext(), R.color.red_alu));
                ((TextView) tab.getCustomView().findViewById(R.id.rightSeparator)).setTextColor(ContextCompat.getColor(getContext(), R.color.red_alu));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() > 0) {
                    ((TextView) tabLayout.getTabAt(tab.getPosition() - 1).getCustomView().findViewById(R.id.rightSeparator)).setTextColor(ContextCompat.getColor(getContext(), R.color.blue_alu));
                }
                ((TextView) tab.getCustomView().findViewById(R.id.TabTitle)).setTextColor(ContextCompat.getColor(getContext(), R.color.blue_alu));
                ((TextView) tab.getCustomView().findViewById(R.id.rightSeparator)).setTextColor(ContextCompat.getColor(getContext(), R.color.blue_alu));
            }
            });

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


                if (tools.isNetworkAvailable(getContext())) {
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


