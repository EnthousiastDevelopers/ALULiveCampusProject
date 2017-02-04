package com.example.tolotranet.livecampus;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Apps_SectionsPagerAdapter extends FragmentPagerAdapter {
    private  final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    public Apps_SectionsPagerAdapter(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = new Apps_PageholderFragment();
        args.putInt(ARG_SECTION_NUMBER, position+1);

        fragment.setArguments(args);
        return  fragment;
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "STUDENT LIFE";
            case 1:
                return "OPERATIONS";
            case 2:
                return "MORE APPS";
        }
        return null;
    }
}
