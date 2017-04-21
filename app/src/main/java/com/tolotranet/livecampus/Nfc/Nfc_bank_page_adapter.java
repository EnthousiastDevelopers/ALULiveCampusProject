package com.tolotranet.livecampus.Nfc;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Nfc_bank_page_adapter extends FragmentPagerAdapter {
    private final String ARG_SECTION_NUMBER = "section_number";
    private final String ARG_SECTION_TITLE= "section_title";

    private Context context;

    public Nfc_bank_page_adapter(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = new Nfc_bank_fragment();
        args.putInt(ARG_SECTION_NUMBER, position );
        args.putString(ARG_SECTION_TITLE, (String) getPageTitle(position));

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
                return "SEND MONEY";
            case 1:
                return "REQUEST MONEY";
        }
        return null;
    }
}
