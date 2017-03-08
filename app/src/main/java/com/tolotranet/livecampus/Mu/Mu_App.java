package com.tolotranet.livecampus.Mu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.App.AppSelect_Parent;
import com.tolotranet.livecampus.Booking.Booking_ItemObject;
import com.tolotranet.livecampus.R;

import java.util.ArrayList;


/**
 * Created by Tolotra Samuel on 18/08/2016.
 */

public class Mu_App extends AppCompatActivity {
    ListView toolList;
    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Booking_ItemObject> ContactItemArray;
    Mu_MyCustomBaseAdapter_Select myAdapter;

    public static String mu_category;
    public static int mu_category_imgID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mu_app);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, AppsItemArray  );

        toolList = (ListView) findViewById(R.id.toolList);
        // toolList.setAdapter(adapter);

        ContactItemArray = MakeArrayList();
        //Mainlistview = (ListView) findViewById(R.id.Contacts_list_view);


        myAdapter = new Mu_MyCustomBaseAdapter_Select(getApplicationContext(),
                ContactItemArray);
        toolList.setAdapter(myAdapter);

        toolList.setOnItemClickListener(new AllContactListViewClickListener());

    }

    private ArrayList<Booking_ItemObject> MakeArrayList() {
        ArrayList<Booking_ItemObject> TempItemArray = new ArrayList<Booking_ItemObject>();

//        ArrayList<String> AppsItemArray = new ArrayList<String>(
//                Arrays.asList("Food App", "SIS", "Zendesk Form", "Transport Schedule",
//                        "Upcoming Appss", "StreamLive ((Locked))", "Business Area((Locked))", "Student Life((Locked))",
//                        "UWallet ((Locked))", "Developer Area((Locked))", "Art&Photography((Locked))"));


        toolList = (ListView) findViewById(R.id.foodapp_listView);

        String[][] AppsItemArray = new String[][]{

                {"Taxi Cab", "A  well being"},
                {"Restaurant", " skills and explore how to achieve this"},
                {"Activities", "A cular event or activity"},
                {"Pharmacies", "A t that feels like a crisis"},
                {"Hospitals, Clinics and Ambulance", "A  to address these"},
                {"Other", "A  to do about it"},

        };
        Integer[] imgid = {
                R.drawable.mu_taix_logo,
                R.drawable.mu_restaurant_logo,
                R.drawable.mu_activities,
                R.drawable.mu_pharmacy,
                R.drawable.mu_hospital_logo,
                R.drawable.mu_more_logo,

        };

        for (int i = 0; i < AppsItemArray.length; i++) {
            Booking_ItemObject CIO = new Booking_ItemObject();
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
        return TempItemArray;
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(),
                AppSelect_Parent.class);
        startActivity(i);

    }
    public class AllContactListViewClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                            long arg3) {
        // TODO Auto-generated method stub
        int Index = ((Booking_ItemObject) arg0.getItemAtPosition(arg2))
                .getIndex();
        int ThisId = ((Booking_ItemObject) arg0.getItemAtPosition(arg2))
                .getUserId();
        int ThisImg = ((Booking_ItemObject) arg0.getItemAtPosition(arg2))
                .getImgId();
        String Menu = ((Booking_ItemObject) arg0.getItemAtPosition(arg2))
                .getName();
        String Availability = ((Booking_ItemObject) arg0.getItemAtPosition(arg2))
                .getBottomText();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Menu);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Availability);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



         mu_category = Menu;
         mu_category_imgID = ThisImg;

        Intent i = new Intent(Mu_App.this, Mu_SpreadSheetActivity.class);
        i.putExtra("category", mu_category);

        startActivity(i);

            Log.d("hello", "Position Clicked is " + arg2);
            Log.d("hello", "Item Clicked is " + Index);
            Log.d("hello", "User Clicked is " + ThisId);
            Log.d("hello", "Menu Clicked is " + Menu);
        }
    }
}
