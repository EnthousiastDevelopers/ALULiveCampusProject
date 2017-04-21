package com.tolotranet.livecampus.App;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.Booking.Booking_App;
import com.tolotranet.livecampus.Bubble.Bubble_SpreadSheetActivity;
import com.tolotranet.livecampus.Event.Event_SpreadSheetActivity;
import com.tolotranet.livecampus.Food.Food_App;
import com.tolotranet.livecampus.Food.Food_SpreadSheetActivity;
import com.tolotranet.livecampus.Gift.Gift_App;
import com.tolotranet.livecampus.HttpRequestApp;
import com.tolotranet.livecampus.Lead.Lead_SpreadSheetActivity;
import com.tolotranet.livecampus.Live.Live_App;
import com.tolotranet.livecampus.Maint.Maint_Add;
import com.tolotranet.livecampus.Maint.Maint_SpreadSheetActivity;
import com.tolotranet.livecampus.Mu.Mu_Add_Food;
import com.tolotranet.livecampus.Mu.Mu_App;
import com.tolotranet.livecampus.Mu.Mu_SpreadSheetActivity;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Settings.Settings_mainActivity;
import com.tolotranet.livecampus.Sign_User_Object;
import com.tolotranet.livecampus.Sis.Sis_SpreadSheetActivity;
import com.tolotranet.livecampus.Sis.Sis_XMLParserClass;
import com.tolotranet.livecampus.Sis.Sis_startApplicationAsyncTaskOwner;
import com.tolotranet.livecampus.Transp.Transp_SpreadSheetActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AppSelect_MainFragment_Upper extends Fragment {

    private final String ARG_SECTION_NUMBER = "section_number";
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;
    private static TextView welcomeTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final GridView toolList;

        ArrayList<Apps_ItemObject> ContactItemArray;
        AppSelect_List_CustomBaseAdapter myAdapter;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        final View rootView = inflater.inflate(R.layout.apps_fragment_upper, container, false);
        int position = getArguments().getInt(ARG_SECTION_NUMBER);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, AppsItemArray  );

        welcomeTV = (TextView) rootView.findViewById(R.id.Welcome);


        //Setting theme

        welcomeTV.setTextColor(getResources().getColor(R.color.blue_alu));

        // toolList.setAdapter(adapter);
        Log.d("hello", "postition is:" + position);
            ContactItemArray = MakeArrayList("MoreApps");
            myAdapter = new AppSelect_List_CustomBaseAdapter(getActivity(), ContactItemArray,"parent");
            toolList = (GridView) rootView.findViewById(R.id.toolList);
            toolList.setAdapter(myAdapter);
            toolList.setOnItemClickListener(new AllContactListViewClickListener());

        return rootView;
    }


    public static ArrayList<Apps_ItemObject> MakeArrayList(String section) {
        ArrayList<Apps_ItemObject> TempItemArray = new ArrayList<Apps_ItemObject>();
        String nullTag = "Update your";
        Log.d("Makearraylist", section);


        String[][] AppsItemArray = new String[][]{
                {"Student Profiles", "Know all the members of our community"},
                {"Events", "Upcoming Events"},
                {"Student Life Stream", "Messages from Student Life and Head of College"},
                {"Book a meeting", "Set an appointment with staff member"},
                {"Food App", "Order food easily"},
                {"Suggest Food", "Suggest a new menu and get feedback"},
                {"Get the menu of the week and give feedback", "Info about the menu of the week"},
                {"Get the list suggested food and vote", "Food"},
                {"Zendesk Form", "Report maintenance issue"},
                {"My Housing Queries", "Report maintenance issue"},
                {"Transport Schedule", "Easiest way to get the app_transp schedule"},
                //{"Mauritius", "All useful information to live in Mauritius"},
                {"Taxi Cab", "Mauritius"},
                {"Restaurant", "Mauritius"},
                {"Activities", "Mauritius"},
                {"Pharmacies", "Mauritius"},
                {"Hospitals, Clinics and Ambulance", "Mauritius"},
                {"Other", "Mauritius"},
                {"Gift", "Get free points, convert points into gifts"},
                {"Leaderboard", "Benchmark your score by Checking your friend's score"},
                {"BubbleMarket", "Benchmark your score by Checking your friend's score"},
                {"FAQ", "FAQ"},
                {"Me", "Edit your profil"},
                {"Submit ideas to improve the app", "Idea"},
        };


        Integer[][] imgid = {
                {R.drawable.app_sis,1},
                {R.drawable.app_event,2},
                { R.drawable.app_stream,3},
                { R.drawable.app_book,4},
                { R.drawable.app_food_dinner,5},
                { R.drawable.food_ic_chef,6},
                { R.drawable.app_ic_menu_food,7},
                { R.drawable.app_food_feedback,8},
                { R.drawable.app_maint,9},
                { R.drawable.app_housing_history,10},
                { R.drawable.app_transp,11},
               // { R.drawable.app_mu,12},
                { R.drawable.app_taxi_cab_ic,13},
                { R.drawable.app_food,14},
                { R.drawable.app_activity_bowling,15},
                { R.drawable.app_pharmacy_icon,16},
                { R.drawable.app_hostpital_building,17},
                { R.drawable.app_more,24},
                { R.drawable.app_ic_gift,18},
                { R.drawable.app_leaderboard_blank,19},
                { R.drawable.app_bubble_white,20},
                { R.drawable.app_faq,21},
                { R.drawable.app_me,22},
                { R.drawable.app_submit_idea,23},

        };


        if(Sis_XMLParserClass.q2 == null) {
            Log.d("hello", "Sis missing, jump accessed, without refreshing the Sis from cloud, but from file but it's ok");
            try {
                new Sis_XMLParserClass();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int Myindex = Sis_XMLParserClass.q2.indexOf(Sign_User_Object.Email); //because the index of object where it contains the myemail from db is equal to data from server or local file
        int MyRow = Integer.parseInt(Sis_XMLParserClass.q1.get(Myindex)); //because q1 is the row number

        Log.d("hello", "myIndex in xml parser class is: "+ String.valueOf(Myindex)+
               " and myRow in student data is: "+ String.valueOf(MyRow));

        Sign_User_Object.Id = MyRow; //

        String myAppUsageJson =  Sis_XMLParserClass.q18.get(Myindex);
        Log.d("hello",myAppUsageJson);

        HashMap ResultAppUsageIdScore = new HashMap();;

        try { //because is the json parsing might be incorrect

            JSONArray arrayJson = new JSONArray(myAppUsageJson); //because the json received start with [ ena with ], its an array of objects with propriety

            for (int n = 0; n < arrayJson.length(); n++) { //because the length is the number of unique user who voted this
                JSONObject theobj = arrayJson.getJSONObject(n); //because it


                int scoreGiven = theobj.getInt("score");
                int appId = theobj.getInt("author");

                ResultAppUsageIdScore.put(appId, scoreGiven) ; //because voteState is the switch we need to define wether the user has voted up, voted down or unvoted
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < AppsItemArray.length; i++) {
            Apps_ItemObject CIO = new Apps_ItemObject();
             if(ResultAppUsageIdScore.get(imgid[i][1])!=null) {
                 int score = (int) ResultAppUsageIdScore.get(imgid[i][1]);
                //Set Name text
                CIO.setName(AppsItemArray[i][0]);//Set Img
                if (AppsItemArray[i][1].equals("")) {
                    CIO.setBottomText("");
                } else {
                    CIO.setBottomText(AppsItemArray[i][1]);
                }
                //Set Img id
                if (imgid[i].equals("")) {
                    CIO.setImgId(imgid[4][0]);
                } else {
                    CIO.setImgId(imgid[i][0]);
                    // CIO.setImgId(4);
                }
                ;
                CIO.setIndex(i);
                CIO.setId(0);
                CIO.setMenuId(imgid[i][1]);

                CIO.setScore(score);
                TempItemArray.add(CIO);
            }
        }
        Collections.sort(TempItemArray, new Comparator<Apps_ItemObject>() {
            @Override
            public int compare(Apps_ItemObject lhs, Apps_ItemObject rhs) {
                return ((Integer) rhs.getScore()).compareTo(lhs.getScore());
            }
        });
        ArrayList<Apps_ItemObject> TempTempArray = new ArrayList<Apps_ItemObject>();
        if(TempItemArray.size()>3) {
            for (int i = 0; i < 4; i++) {
                TempTempArray.add(TempItemArray.get(i));
            }
            TempItemArray = TempTempArray;
        }
        else if (TempItemArray.size() == 0) {
            welcomeTV.setVisibility(View.VISIBLE);
        }
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
                    .getId();
            int ThisImg = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getImgId();
            int ThisMenuId = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getMenuId();

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

            HttpRequestApp.add_App_UsageTrack(Sign_User_Object.Email, String.valueOf(ThisMenuId), "Clicked","1", Menu);

            Log.d("hello", "Position Clicked is " + arg2);
            Log.d("hello", "Item Clicked is " + Index);
            Log.d("hello", "User Clicked is " + ThisId);
            Log.d("hello", "Menu Clicked is " + Menu);

            //open editor profile if the person clicked is the current user
            if (Menu == "Book a meeting") {
                Intent i = new Intent(getActivity(), Booking_App.class);
                startActivity(i);

            }
            if (Menu == "Food App") {
                Intent i = new Intent(getActivity(), App_Contribute.class);
                startActivity(i);

            }
            if (Menu == "Transport Schedule") {
                Intent i = new Intent(getActivity(), Transp_SpreadSheetActivity.class);
                startActivity(i);

            }
            if (Menu == "Student Life Stream") {
                Intent i = new Intent(getActivity(), Live_App.class);
                startActivity(i);

            }
            if (Menu == "Zendesk Form") {
                Intent i = new Intent(getActivity(), Maint_Add.class);
                startActivity(i);

            }
            if (Menu == "Student Profiles") {
                Intent i = new Intent(getActivity(), Sis_SpreadSheetActivity.class);
                startActivity(i);
                // i.putExtra("myId", userID);

            }
            if (Menu == "Events") {
                Intent i = new Intent(getActivity(), Event_SpreadSheetActivity.class);
                startActivity(i);

            }
            if (Menu == "Settings") {
                Intent i = new Intent(getActivity(), Settings_mainActivity.class);
                startActivity(i);

            }
            if (Menu == "Gift") {
                Intent i = new Intent(getActivity(), Gift_App.class);
                startActivity(i);

            }
            if (Menu == "Me") {
                mProgress = new ProgressDialog(getActivity());
                mProgress.setMessage("Loading data ...");
                mProgress.show();
                Sis_startApplicationAsyncTaskOwner myTask = new Sis_startApplicationAsyncTaskOwner();
                myTask.execute(getActivity());

            }
            if (Menu == "BubbleMarket") {
                Intent i = new Intent(getActivity(), Bubble_SpreadSheetActivity.class);
                startActivity(i);
            }
            if (Menu == "Leaderboard") {
                Intent i = new Intent(getActivity(), Lead_SpreadSheetActivity.class);
                startActivity(i);
            }
            if (Menu == "My Housing Queries") {
                Intent i = new Intent(getActivity(), Maint_SpreadSheetActivity.class);
                startActivity(i);
            }
            if (Menu == "Mauritius") {
                Intent i = new Intent(getActivity(), Mu_App.class);
                startActivity(i);
            }
//            if (Menu == "Submit ideas to improve the app") {
//                Intent i = new Intent(getActivity(), Mu_Add_Idea.class);
//                startActivityPrompt(i);
//            }
            if (Menu == "Get the menu of the week and give feedback") {
                Intent i = new Intent(getActivity(), Food_SpreadSheetActivity.class);
                startActivity(i);
                Food_App.mu_category_imgID = ThisImg;

            }

            if (Menu == "Suggest Food") {
                Intent i = new Intent(getActivity(), Mu_Add_Food.class);
                Mu_App.mu_category = "Food";
                startActivity(i);
            }

            //Mauritius Apps, because those apps are all about mauritius, they should go somewhere else on another seciton maybe

            //end of Mauritius Apps

            if (Availability == "(Locked)") {
                Toast.makeText(getActivity(), Menu + " is locked. Please contact the developer to unlocked it", Toast.LENGTH_SHORT).show();
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
            if (Availability == "Mauritius" ||
                    Availability == "FAQ" ||
                    Availability == "Idea" ||
                    Availability == "Food") { //because those apps with this attribut opens the exact same class, only one variable differentiate them        Bundle bundle2 = new Bundle();
                Bundle bundle2 = new Bundle();
                bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Index));
                bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
                bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "locked menu");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);
                Mu_App.mu_category = Menu;
                Mu_App.mu_category_imgID = ThisImg;
                if (Availability == "Food" || Availability == "Idea") { //because the name menu is not the category in the database but the availability
                    Mu_App.mu_category = Availability;
                }
                Intent i = new Intent(getActivity(), Mu_SpreadSheetActivity.class);
                startActivity(i);

            }


        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

}