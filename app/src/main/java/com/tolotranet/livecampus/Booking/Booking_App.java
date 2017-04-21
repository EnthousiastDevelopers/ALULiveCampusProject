package com.tolotranet.livecampus.Booking;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.R;

import java.util.ArrayList;


/**
 * Created by Tolotra Samuel on 18/08/2016.
 */

public class Booking_App extends AppCompatActivity {
    ListView toolList;
    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Booking_ItemObject> ContactItemArray;
    Booking_MyCustomBaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_app);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, AppsItemArray  );

        toolList = (ListView) findViewById(R.id.toolList);
        // toolList.setAdapter(adapter);

        ContactItemArray = MakeArrayList();
        //Mainlistview = (ListView) findViewById(R.id.Contacts_list_view);


        myAdapter = new Booking_MyCustomBaseAdapter(Booking_App.this,
                ContactItemArray);
        toolList.setAdapter(myAdapter);

        toolList.setOnItemClickListener(new Booking_App.AllContactListViewClickListener());

    }

    private ArrayList<Booking_ItemObject> MakeArrayList() {
        ArrayList<Booking_ItemObject> TempItemArray = new ArrayList<Booking_ItemObject>();

//        ArrayList<String> AppsItemArray = new ArrayList<String>(
//                Arrays.asList("Food App", "SIS", "Zendesk Form", "Transport Schedule",
//                        "Upcoming Appss", "StreamLive ((Locked))", "Business Area((Locked))", "Student Life((Locked))",
//                        "UWallet ((Locked))", "Developer Area((Locked))", "Art&Photography((Locked))"));


        toolList = (ListView) findViewById(R.id.foodapp_listView);

        String[][] AppsItemArray = new String[][]{

                {"Health Convos", "A conversation to address matters relating to your body and physical well being"},
                {"Growth Convos", "Description A conversation to identify areas in which you would like to develop yourself or your skills and explore how to achieve this"},
                {"Planning Convos", "A conversation to help you make a difficult decision or plot the way forward with a particular event or activity"},
                {"Counselling Convos", "A conversation about an ongoing problem which makes it difficult for you to function at your best or a recent event that feels like a crisis"},
                {"Leadership Convos", "A conversation about your goals and challenges as a leader and how to address these"},
                {"Help! Convos", "A conversation to clarify what is troubling you when you know there is something wrong but you don’t know what, and decide what to do about it"},

        };
        Integer[] imgid = {
                R.drawable.book_health,
                R.drawable.book_growth,
                R.drawable.book_planning,
                R.drawable.book_counselling,
                R.drawable.book_leadership,
                R.drawable.book_help,

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



        String convos = Menu;

        Intent i = new Intent(Booking_App.this, Booking_Location_People_Day.class);
        i.putExtra("convos", convos);
        startActivity(i);

            Log.d("hello", "Position Clicked is " + arg2);
            Log.d("hello", "Item Clicked is " + Index);
            Log.d("hello", "User Clicked is " + ThisId);
            Log.d("hello", "Menu Clicked is " + Menu);
        }
    }
}
