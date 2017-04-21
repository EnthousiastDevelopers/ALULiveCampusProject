package com.tolotranet.livecampus.Nfc;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tolotranet.livecampus.App.AppSelect_MainFragment_Child;

public class Nfc_food_page_adapter extends FragmentPagerAdapter {
    private final String ARG_SECTION_NUMBER = "section_number";
    private Context context;

    public Nfc_food_page_adapter(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = new Nfc_Food_Fragment();
        args.putInt(ARG_SECTION_NUMBER, position + 1);
        args.putString(ARG_SECTION_NUMBER, (String) getPageTitle(position));

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Option A";
            case 1:
                return "Option B";
            case 2:
                return "Option C";
            case 3:
                return "Option D";
            case 4:
                return "Option E";
            case 5:
                return "Option F";
        }
        return null;
    }
}
