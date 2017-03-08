package com.tolotranet.livecampus.App;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Apps_PagerAdapter_Upper extends FragmentPagerAdapter {
    private  final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    public Apps_PagerAdapter_Upper(FragmentManager fm, Context current) {
        super(fm);
        this.context = current;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new AppSelect_MainFragment_Upper();
                break;
            case 1:
                fragment = new AppSelect_Child_Fragment();
                break;
        }
        args.putInt(ARG_SECTION_NUMBER, position+1);

        fragment.setArguments(args);
        return  fragment;
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MY FAVOURITES";
//                return Sign_User_Object.Firstname+"'S FAVORITE";
            case 1:
                return "ALL APPS";
        }
        return null;
    }
}
