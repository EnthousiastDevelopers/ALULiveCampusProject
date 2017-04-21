package com.tolotranet.livecampus.Nfc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tolotranet.livecampus.R;

/**
 * Created by Tolotra Samuel on 19/03/2017.
 */

public class Nfc_Bank_MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private View childLayout;
    private ViewPager mViewPager;
    private Nfc_bank_page_adapter mSectionsPagerAdapter;
    private TabLayout tabLayout;

    protected void onCreate(Bundle savedInstanceState) {
        // setContentView(R.layout.tag_viewer);
        Log.d("hello", "on create");
        super.onCreate(savedInstanceState);

        setUpSharedDrawerLayout();
        includeLayoutContent();
        instantiateLayers();
        createAbstractElements();
        setUpLayoutListeners();
        setUpLayoutAdapters();

        setUpUiAlgorythm();

    }

    private void createAbstractElements() {
        mSectionsPagerAdapter = new Nfc_bank_page_adapter(getSupportFragmentManager(), this);
    }

    private void setUpUiAlgorythm() {
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.red_alu));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red_alu));
    }

    private void setUpLayoutAdapters() {

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void instantiateLayers() {
        tabLayout = (TabLayout) childLayout.findViewById(R.id.tabs);
        mViewPager = (ViewPager) childLayout.findViewById(R.id.container);
    }

    private void setUpLayoutListeners() {

    }

    private void setUpSharedDrawerLayout() {
        Log.d("hello", "setUpSharedDrawerLayout");

        setContentView(R.layout.nfc_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setting theme
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.red_alu));
        // getSupportActionBar().setIcon(R.drawable.ic_logo_minimini);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void includeLayoutContent() {
        Log.d("hello", "includeLayoutContent");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        childLayout = inflater.inflate(R.layout.nfc_main_content_bank_container, (ViewGroup) findViewById(R.id.nfc_main_content_bank_container));
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.nfc_include_relativeLT_container);
        parentLayout.addView(childLayout, 0);

    }
}
