package com.tolotranet.livecampus.Sis.Interact;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tolotranet.livecampus.App.App_Tools;
import com.tolotranet.livecampus.HttpRequestApp;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sign.Sign_DatabaseHelper;
import com.tolotranet.livecampus.Sign_User_Object;
import com.tolotranet.livecampus.Sis.Sis_SpreadSheetActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;


/**
 * Created by Tolotra Samuel on 18/08/2016.
 */


public class Interact_MainActivity extends AppCompatActivity {

    private TextView privacyTV;
    private TextView interactionTitleTV;
    private TabLayout tabLayout;
    private RadioGroup privacyRG;
    private ViewPager viewPager;
    private Button relationshipBtn, friendshipBtn, professionalBtn;
    private Interact_PagerAdapter_Main mSectionsPagerAdapter;
    private ProgressBar loadingPanel;
    private String interaction_message;
    private String interaction_email;
    private FloatingActionButton sendInteraction;
    private int ViewPagerOnScreenLimitNumber = 3;
    private Interaction_SendObject interactionObj;
    private ProgressDialog mProgress;
    private String PrivacyID = "anonymous";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interact_main);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Sending...");

        this.interaction_email = getIntent().getStringExtra("recipient");

        interactionTitleTV = (TextView) findViewById(R.id.interactionTV);
        sendInteraction = (FloatingActionButton) findViewById(R.id.fab_sendInteraction);

        privacyTV = (TextView) findViewById(R.id.privacyTV);
        privacyRG = (RadioGroup) findViewById(R.id.privacyRG);
        privacyRG.setOnCheckedChangeListener(new MyOnCheckedRadioGroupChangeListener());

        viewPager = (ViewPager) findViewById(R.id.ViewPagerSelectInteraction);
        viewPager.setOffscreenPageLimit(ViewPagerOnScreenLimitNumber);

        relationshipBtn = (Button) findViewById(R.id.relationshipBtn);
        professionalBtn = (Button) findViewById(R.id.professionalBtn);
        friendshipBtn = (Button) findViewById(R.id.friendshipBtn);

        relationshipBtn.setOnClickListener(new onInteractCategoryBtnClick());
        professionalBtn.setOnClickListener(new onInteractCategoryBtnClick());
        friendshipBtn.setOnClickListener(new onInteractCategoryBtnClick());

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        loadingPanel = (ProgressBar) findViewById(R.id.loadingPanel);

        mSectionsPagerAdapter = new Interact_PagerAdapter_Main(getSupportFragmentManager(), this);

        //when the user click on the privacy TV, toggle visibility of privacy Radio Group
        privacyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privacyRG.getVisibility() == View.VISIBLE) {
                    privacyRG.setVisibility(View.GONE);
                } else {
                    privacyRG.setVisibility(View.VISIBLE);
                }
            }
        });
        sendInteraction.setOnClickListener(new OnSendFabClickListener());
        setupToolBarElements();

    }

    private boolean makeSureInteractionListisLoadedFromFile() {
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
        File myfile = new File(Dir, "Interaction.txt");
        if (myfile.exists()) {
            Log.d("hello", "File Exists");
            try {
                new Interaction_XMLParserClass();
                return true;
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setInteractionTitle(Interaction_SendObject obj) {
        if (obj == null || obj.getName().equals("")) {
            this.interaction_message = "";
            interactionTitleTV.setText("Choose an interaction");
        } else {
            this.interaction_message = obj.getName();
            this.interactionObj = obj;
            interactionTitleTV.setText("I want to " + interaction_message.toLowerCase());
        }
    }

    private class onInteractCategoryBtnClick implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            privacyRG.setVisibility(View.GONE); //hide privacy section
            loadingPanel.setVisibility(View.VISIBLE); //show loading panel

            if (Interaction_XMLParserClass.q1 == null) {
                if (makeSureInteractionListisLoadedFromFile()) { //if file exists and correctly loaded the the data
                    displayViewpagerInteraction(v);
                } else {
                    Log.d("hello", "File doesn't exist");
                    App_Tools tool = new App_Tools();
                    if ((tool.isNetworkAvailable(Interact_MainActivity.this))) {
                        Interaction_GetDataAsyncTask getDataTask = new Interaction_GetDataAsyncTask("refreshonly") {
                            @Override
                            protected void onPostExecute(Void method) {
                                displayViewpagerInteraction(v);
                            }
                        };
                        getDataTask.execute(Interact_MainActivity.this);
                    }else{
                        Toast.makeText(Interact_MainActivity.this, "Network Connection is not available", Toast.LENGTH_SHORT).show();
                    }
                }//end else if file doesnt exist
            }else {
                displayViewpagerInteraction(v);
            }//end if xml data null
        }//on onclick

        //this will setup and display the viewpagers
        private void displayViewpagerInteraction(View v) {
            loadingPanel.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);

            //only when the user click on one of the 3 interact type button, asign viewpager is fragment adapter, to save memory
            if (viewPager.getAdapter() == null) {
                viewPager.setAdapter(mSectionsPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
            switch (v.getId()) {
                case R.id.professionalBtn:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.friendshipBtn:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.relationshipBtn:
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    }

    private void setupToolBarElements() {
        // setting up tool bar back and post button

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Interact_MainActivity.super.onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.saveTB:

//                Intent i = new Intent(Event_CalendarList_Main.this, Event_SpreadSheetActivity.class);
                // startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class OnSendFabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (interaction_message.equals("")) {
                Toast.makeText(Interact_MainActivity.this, "No interaction selected", Toast.LENGTH_SHORT).show();
            } else {
                mProgress.show();
                HttpRequestApp tolotraHttpRequestApp = new HttpRequestApp("addinteraction") {
                    @Override
                    protected void onPostExecute(Void postE) {
                        mProgress.hide();
                        finish();
                    }
                };
                if (Sign_User_Object.Apartment == null) {
                    Log.d("hello", "missing Sign_User_Object jump accessed, without refreshing the Sis from cloud, but it's ok");
                    Sign_DatabaseHelper helper = new Sign_DatabaseHelper(Interact_MainActivity.this);
                    helper.AllUserDataBaseToObject();
                }

                interactionObj.setRecipient(interaction_email);
                interactionObj.setPrivacy(PrivacyID);
                interactionObj.setSender(Sign_User_Object.Email);

                tolotraHttpRequestApp.setInteractionObject(interactionObj);
                tolotraHttpRequestApp.execute(Interact_MainActivity.this);


            }
        }
    }

    private class MyOnCheckedRadioGroupChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            switch (checkedId) {
                case R.id.anonymousRadio:
                    // do operations specific to this selection
                    PrivacyID = "anonymous";
                    break;
                case R.id.showRadio:
                    // do operations specific to this selection
                    PrivacyID = "visible";
                    break;
                case R.id.hybridRadio:
                    // do operations specific to this selection
                    PrivacyID = "mixte";
                    break;
            }
        }
    }
}