package com.tolotranet.livecampus.App;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Apps_PagerAdapter_Child extends FragmentPagerAdapter {
    private  final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    public Apps_PagerAdapter_Child(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = new AppSelect_MainFragment_Child();
        args.putInt(ARG_SECTION_NUMBER, position+1);

        fragment.setArguments(args);
        return  fragment;
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "STUDENT LIFE";
            case 1:
                return "OPERATIONS";
            case 2:
                return "MAURITIUS";
            case 3:
                return "MORE APPS";
        }
        return null;
    }
}
