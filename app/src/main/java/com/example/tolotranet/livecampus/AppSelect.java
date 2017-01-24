package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

/**
 * Created by Tolotra Samuel on 15/08/2016.
 */
public class AppSelect extends Activity {

    private FirebaseAnalytics mFirebaseAnalytics;
    GridView toolList;
    ArrayList<Apps_ItemObject> ContactItemArray;
    AppSelect_MyCustomBaseAdapter myAdapter;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appselect);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        String username = getIntent().getStringExtra("Username");

        TextView tv = (TextView) findViewById(R.id.TVusername);

        tv.setText(username);
        mFirebaseAnalytics.setUserId(username);//set user ID


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, AppsItemArray  );

        toolList = (GridView) findViewById(R.id.toolList);
        // toolList.setAdapter(adapter);

        ContactItemArray = MakeArrayList();
        //lv = (ListView) findViewById(R.id.Contacts_list_view);


        myAdapter = new AppSelect_MyCustomBaseAdapter(getApplicationContext(),
                ContactItemArray);
        toolList.setAdapter(myAdapter);

        toolList.setOnItemClickListener(new AllContactListViewClickListener());

    }

    private ArrayList<Apps_ItemObject> MakeArrayList() {
        ArrayList<Apps_ItemObject> TempItemArray = new ArrayList<Apps_ItemObject>();
        String nullTag = "Update your";

//        ArrayList<String> AppsItemArray = new ArrayList<String>(
//                Arrays.asList("Food App", "SIS", "Zendesk Form", "Transport Schedule",
//                        "Upcoming Appss", "StreamLive ((Locked))", "Business Area((Locked))", "Student Life((Locked))",
//                        "UWallet ((Locked))", "Developer Area((Locked))", "Art&Photography((Locked))"));

        String[][] AppsItemArray = new String[][]{
                {"Food App", "Order food easily"},
                {"SIS", "Know all the members of our community"},
                {"Zendesk Form", "Report maintenance issue"},
                {"Transport Schedule", "Easiest way to get the app_transp schedule"},
                {"Events", "Upcoming Events"},
                {"Student Life Stream", "Messages from Student Life and Head of College"},
                {"Book a meeting", "Set an appointment with staff member"},
                {"Mauritius", "All useful information to live in Mauritius"},
                {"FAQ", "Support and Q&A "},
                {"Me", "Edit your profil"},
                {"Settings", "Privacy, Security, Preference"},
        };
        Integer[] imgid = {
                R.drawable.app_food,
                R.drawable.app_sis,
                R.drawable.app_maint,
                R.drawable.app_transp,
                R.drawable.app_event,
                R.drawable.app_stream,
                R.drawable.app_book,
                R.drawable.app_mu,
                R.drawable.app_faq,
                R.drawable.app_me,
                R.drawable.app_settings,
        };

        for (int i = 0; i < AppsItemArray.length; i++) {
            Apps_ItemObject CIO = new Apps_ItemObject();
            if (!AppsItemArray[i][0].equals("")) {
                //Set Name text
                CIO.setName(AppsItemArray[i][0]);

                //Set Img
                if (AppsItemArray[i][1].equals("")) {
                    CIO.setBottomText("");
                } else {
                    CIO.setBottomText(AppsItemArray[i][1]);
                }
                //Set Img id
                if (imgid[i].equals("")) {

                    CIO.setImgId(imgid[4]);
                } else {
                    CIO.setImgId(imgid[i]);
                    // CIO.setImgId(4);
                }
                ;
                CIO.setIndex(i);
                CIO.setUserId(0);
                TempItemArray.add(CIO);
            }
        }
//        Collections.sort(TempItemArray, new Comparator<Apps_ItemObject>() {
//            @Override
//            public int compare(Apps_ItemObject lhs, Apps_ItemObject rhs) {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
        return TempItemArray;
    }

    public class AllContactListViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            int Index = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisId = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getUserId();
            int ThisImg = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getImgId();
            String Menu = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getName();
            String Availability = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getBottomText();

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Index));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "locked menu");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Bundle params = new Bundle();
            params.putString("image_name", Menu);
            params.putString("full_text", "locked menu");
            mFirebaseAnalytics.logEvent("share_image", params);


            Log.d("hello", "Menu Clicked is " + Menu);
            //open editor profile if the person clicked is the current user
            if (Menu == "Book a meeting") {

                Intent i = new Intent(AppSelect.this, Booking_App.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Food App") {

                Intent i = new Intent(AppSelect.this, FoodApp.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Transport Schedule") {

                Intent i = new Intent(AppSelect.this, Transp_SpreadSheetActivity.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Zendesk Form") {

                Intent i = new Intent(AppSelect.this, MaintZendesk.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "SIS") {

                Intent i = new Intent(AppSelect.this, Sis_SpreadSheetActivity.class);
                startActivity(i);
               // i.putExtra("myId", userID);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Events") {

                Intent i = new Intent(AppSelect.this, Event_SpreadSheetActivity.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Settings") {

                Intent i = new Intent(AppSelect.this, Settings_mainActivity.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Me") {

                mProgress = new ProgressDialog(AppSelect.this);
                mProgress.setMessage("Loading data ...");
                mProgress.show();
                Sis_startApplicationAsyncTaskOwner myTask = new Sis_startApplicationAsyncTaskOwner();

                myTask.execute(AppSelect.this);


                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "FAQ") {

                Intent i = new Intent(AppSelect.this, Faq_SpreadSheetActivity.class);
                startActivity(i);

                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Mauritius") {

                Intent i = new Intent(AppSelect.this, Faq_SpreadSheetActivity.class);
                startActivity(i);

                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }

            if (Availability == "(Locked)") {
                Toast.makeText(getBaseContext(), Menu + " is locked. Please contact the developer to unlocked it", Toast.LENGTH_SHORT).show();
                Log.d("hello", "Img Clicked is " + ThisImg);
                Bundle bundle2 = new Bundle();
                bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Index));
                bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
                bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "locked menu");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);

                Bundle params2 = new Bundle();
                params2.putString("image_name", Menu);
                params2.putString("full_text", "locked menu");
                mFirebaseAnalytics.logEvent("share_image", params2);
            }


        }

    }


    public static  int getUserID(){
        Context c = MyApplication.getAppContext();
        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(c);
       int userID  = helper.getUserId();
       //int userID = 1000005;
        return userID;
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mProgress!=null && mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

}
