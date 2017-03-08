package com.tolotranet.livecampus.App;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.Bubble.Bubble_SpreadSheetActivity;
import com.tolotranet.livecampus.Feed.Feed_MainActivity;
import com.tolotranet.livecampus.Head.Head_Controller_Activity;
import com.tolotranet.livecampus.Head.Head_Service_Activity;
import com.tolotranet.livecampus.Lead.Lead_SpreadSheetActivity;
import com.tolotranet.livecampus.Mu.Mu_Add_Idea;
import com.tolotranet.livecampus.Mu.Mu_App;
import com.tolotranet.livecampus.Mu.Mu_SpreadSheetActivity;
import com.tolotranet.livecampus.MyApplication;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Settings.Settings_mainActivity;
import com.tolotranet.livecampus.Sign.Sign_DatabaseHelper;
import com.tolotranet.livecampus.Sign_User_Object;
import com.tolotranet.livecampus.Sign.Sign_check_mail;
import com.tolotranet.livecampus.Sis.Sis_DetailListViewOwner;
import com.tolotranet.livecampus.Sis.Sis_GetDataAsyncTask;
import com.tolotranet.livecampus.Voice.Voice_Speech_Action;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tolotra Samuel on 15/08/2016.
 */
public class AppSelect_Parent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String origin = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;
    private Apps_PagerAdapter_Upper mSectionsPagerAdapter;
    private ViewPager mViewPager;
    protected static DrawerLayout drawer;
    private com.github.clans.fab.FloatingActionMenu fab_menu_red;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.sign_start_application);
        View background = (View) findViewById(R.id.content_main);
        background.setBackgroundColor(getResources().getColor(R.color.white));

        origin = "appselect"; //because origin is used by differents apps to set their onback pressed


        boolean hasSetServiceAutoLaunch = true;
        if(hasSetServiceAutoLaunch) {
            //start service on app launced
            startService(new Intent(getApplication(), Head_Service_Activity.class));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setting theme
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_alu));
        // getSupportActionBar().setIcon(R.drawable.ic_logo_minimini);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AppSelect_Parent.this); // because we have to set the navigation nav_view

        Menu menu = navigationView.getMenu();

        MenuItem me_residence = menu.findItem(R.id.me_residence);
        me_residence.setTitle(me_residence.getTitle().toString() + ": " + Sign_User_Object.Residence);
        me_residence.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        MenuItem me_room = menu.findItem(R.id.me_room);
        me_room.setTitle(me_room.getTitle().toString() + ": " + Sign_User_Object.RoomNumber);
        me_room.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        if (!Sign_User_Object.Apartment.toString().equals("N/A")) {
            me_room.setTitle(me_room.getTitle().toString() + ": " + Sign_User_Object.RoomNumber + " " + Sign_User_Object.Apartment);
        }

        MenuItem nav_settings = menu.findItem(R.id.nav_settings);
        MenuItem nav_faq = menu.findItem(R.id.nav_faq);
        MenuItem nav_logout = menu.findItem(R.id.nav_logout);

        MenuItem menuContribute = menu.findItem(R.id.menuContribute);
        MenuItem menuShop = menu.findItem(R.id.menuShop);
        MenuItem menuLeaderboard = menu.findItem(R.id.menuLeaderboard);

        MenuItem menuHeadBubble = menu.findItem(R.id.HeadBubble);


        MenuItem menuallapps = menu.findItem(R.id.allapps);
        MenuItem menualive = menu.findItem(R.id.alive);
        MenuItem menufavourite = menu.findItem(R.id.favourite);
        MenuItem menumyday = menu.findItem(R.id.myday);


        nav_settings.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        nav_faq.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        nav_logout.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        menuallapps.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menualive.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menufavourite.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menumyday.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        menuContribute.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menuShop.setOnMenuItemClickListener(new MyInfoMenuClickListener());
        menuLeaderboard.setOnMenuItemClickListener(new MyInfoMenuClickListener());

        menuHeadBubble.setOnMenuItemClickListener(new MyInfoMenuClickListener());

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
        if (Integer.parseInt(Sign_User_Object.Score) > 1) {
            subscoretv.setText("Points");
        }


        mFirebaseAnalytics.setUserId(username);//set user ID


        mSectionsPagerAdapter = new Apps_PagerAdapter_Upper(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        tabLayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.red_alu));

        tabLayout.setBackgroundColor(getResources().getColor(R.color.blue_alu));

        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setCustomView(R.layout.tab_color_selector);
        ((TextView) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().findViewById(R.id.TabTitle)).setText(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText());


        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                String a = tab.getText().toString();
                Log.d("hello select", a);
                View tab_color_selector = tab.getCustomView();
                Boolean hasBeenCustomed = false;
                if (tab_color_selector == null) {
                    hasBeenCustomed = true;
                    tab_color_selector = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_color_selector, null);
                }
                RelativeLayout containerLT = (RelativeLayout) tab_color_selector.findViewById(R.id.containerLT);

                TextView tab_label = (TextView) tab_color_selector.findViewById(R.id.TabTitle);
                tab_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_alu));
                tab_label.setText(a);
                tab_label.setHeight(tabLayout.getHeight());
                containerLT.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tab_label.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tab_color_selector.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));


                if (hasBeenCustomed) {
                    Log.d("hello", "new custom view select");
                    tab.setCustomView(tab_color_selector);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //    super.onTabUnselected(tab);

                String a = tab.getText().toString();
                Log.d("hello unselect", a);

                View tab_color_selector = tab.getCustomView();
                Boolean hasBeenCustomed = false;
                if (tab_color_selector == null) {
                    hasBeenCustomed = true;
                    tab_color_selector = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_color_selector, null);
                }
                TextView tab_label = (TextView) tab_color_selector.findViewById(R.id.TabTitle);
                tab_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                RelativeLayout containerLT = (RelativeLayout) tab_color_selector.findViewById(R.id.containerLT);
                containerLT.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_alu));
                tab_label.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_alu));
                tab_color_selector.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_alu));
                tab_label.setText(a);
                tab_label.setHeight(tabLayout.getHeight());

                if (hasBeenCustomed) {
                    Log.d("hello", "new custom view unselect");
                    tab.setCustomView(tab_color_selector);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        fab_menu_red = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu_red);

        final com.github.clans.fab.FloatingActionButton fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_refresh);
        final com.github.clans.fab.FloatingActionButton fab_action_speech = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_action_speech);
        final com.github.clans.fab.FloatingActionButton fab_add = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_add);
        final com.github.clans.fab.FloatingActionButton fab_ideas = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_ideas);
        final com.github.clans.fab.FloatingActionButton fab_feedback = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_feedback);


        fab_action_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: When is the next Bus");
                try {
                    startActivityForResult(i, 100);

                } catch (ActivityNotFoundException a) {
                    Toast.makeText(AppSelect_Parent.this, "Sorry, your device does not support speech language", Toast.LENGTH_SHORT);

                }
            }
        });
        fab_menu_red.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return fab_menu_red.isOpened();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (fab_menu_red.isOpened()) {
                        fab_menu_red.close(fab_menu_red.isAnimated());
                        return true;
                    }
                }

                return false;
            }
        });
        fab_menu_red.setOnMenuButtonLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: When is the next Bus");
                try {
                    startActivityForResult(i, 100);

                } catch (ActivityNotFoundException a) {
                    Toast.makeText(AppSelect_Parent.this, "Sorry, your device does not support speech language", Toast.LENGTH_SHORT);

                }

                return true;
            }
        });
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    fab_refresh.startAnimation(AnimationUtils.loadAnimation(AppSelect_Parent.this, R.anim.rotation));
                    Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    Sis_GetDataAsyncTask InitialisetData = new Sis_GetDataAsyncTask();
                    InitialisetData.Target = "apps";
                    InitialisetData.execute(AppSelect_Parent.this);

                }
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    Intent i = new Intent(AppSelect_Parent.this, App_Contribute.class);
                    startActivity(i);

                }
            }
        });
        fab_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    Intent i = new Intent(AppSelect_Parent.this, App_Feedback.class);
                    startActivity(i);

                }
            }
        });
        fab_ideas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    Intent i = new Intent(AppSelect_Parent.this, Mu_Add_Idea.class);
                    startActivity(i);

                }
            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static int getUserID() {
        Context c = MyApplication.getAppContext();
        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(c);
        int userID = helper.getUserId();
        //int userID = 1000005;
        return userID;
    }


    //this activity result give response of the startactivityforresult of voice recognition
    @Override
    public void onActivityResult(int request_code, int request_result, Intent i) {
        super.onActivityResult(request_code, request_result, i);
        switch (request_code) {
            case 100:
                if (request_result == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(AppSelect_Parent.this, result.get(0), Toast.LENGTH_SHORT).show();
                    Voice_Speech_Action voiceSpeech_action = new Voice_Speech_Action();
                    voiceSpeech_action.Speech_Actions(AppSelect_Parent.this, result.get(0).toLowerCase());
                    Intent iii = new Intent(AppSelect_Parent.this, App_Contribute.class);
                    //startActivityPrompt(iii);
                }
                break;
        } //end case request_code 100
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (fab_menu_red.isOpened()) {
            fab_menu_red.close(true);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsMain:
                App_Tools tools = new App_Tools();

                if (isNetworkAvailable()) {
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

    private class MyInfoMenuClickListener implements MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getTitle().equals("Settings")) {
                Intent i = new Intent(AppSelect_Parent.this, Settings_mainActivity.class);
                startActivity(i);
            } else if (item.getTitle().equals("Sticky Bubble")) {

                Intent i = new Intent(AppSelect_Parent.this, Head_Controller_Activity.class);
                startActivity(i);

            }else if (item.getTitle().equals("Help")) {
                Mu_App.mu_category = "FAQ";
                Intent i = new Intent(AppSelect_Parent.this, Mu_SpreadSheetActivity.class);
                startActivity(i);

            } else if (item.getTitle().equals("Logout")) {
                Sign_DatabaseHelper helper = new Sign_DatabaseHelper(AppSelect_Parent.this);
                helper.cleanTable();
                Intent i = new Intent(AppSelect_Parent.this, Sign_check_mail.class);
                startActivity(i);
            } else if (item.getTitle().equals("Leaderboard")) {
                Intent i = new Intent(AppSelect_Parent.this, Lead_SpreadSheetActivity.class);
                startActivity(i);
            } else if (item.getTitle().equals("Exchange Points")) {
                Intent i = new Intent(AppSelect_Parent.this, Bubble_SpreadSheetActivity.class);
                startActivity(i);
            } else if (item.getTitle().equals("Contribute and get points")) {
                Intent i = new Intent(AppSelect_Parent.this, App_Contribute.class);
                startActivity(i);

            } else if (item.getTitle().equals("ALU ALIVE")) {
                Intent i = new Intent(AppSelect_Parent.this, Feed_MainActivity.class);
                startActivity(i);

            } else {
                Intent i = new Intent(AppSelect_Parent.this, Sis_DetailListViewOwner.class);
                startActivity(i);

            }
            return false;
        }
    }
}
