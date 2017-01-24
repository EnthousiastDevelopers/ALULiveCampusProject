package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;


/**
 * Created by Tolotra Samuel on 18/08/2016.
 */

public class Booking_Location_People_Day extends Activity {
    ListView toolList;
    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Booking_ItemObject> ContactItemArray;
    String convos;
    Booking_MyCustomBaseAdapter_3 myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_people);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, AppsItemArray  );
        Intent i = getIntent();
        convos = i.getStringExtra("convos");

        toolList = (ListView) findViewById(R.id.toolList);
        // toolList.setAdapter(adapter);

        ContactItemArray = MakeArrayList();
        //lv = (ListView) findViewById(R.id.Contacts_list_view);


        myAdapter = new Booking_MyCustomBaseAdapter_3(getApplicationContext(),
                ContactItemArray);
        toolList.setAdapter(myAdapter);

        toolList.setOnItemClickListener(new Booking_Location_People_Day.AllContactListViewClickListener());

    }

    private ArrayList<Booking_ItemObject> MakeArrayList() {
        ArrayList<Booking_ItemObject> TempItemArray = new ArrayList<Booking_ItemObject>();

//        ArrayList<String> AppsItemArray = new ArrayList<String>(
//                Arrays.asList("Food App", "SIS", "Zendesk Form", "Transport Schedule",
//                        "Upcoming Appss", "StreamLive ((Locked))", "Business Area((Locked))", "Student Life((Locked))",
//                        "UWallet ((Locked))", "Developer Area((Locked))", "Art&Photography((Locked))"));


        toolList = (ListView) findViewById(R.id.foodapp_listView);

        String[][] AppsItemArray = new String[][]{

                {"Veda", "Friday 2pm-5pm","Paradise"},
                {"Mandisa", "Thurday 2pm-5pm","Paradise"},
                {"Lisl", "Wednesday 2pm-5pm","Paradise"},
                {"Jeremy", "Tuesday 2pm-5pm","Paradise"},
                {"Vreeti", "Monday 2pm-5pm","Paradise"},

                {"Mandisa", "Friday 2pm-5pm","Beau Plan"},
                {"Lohinee", "Thurday 2pm-5pm","Beau Plan"},
                {"Lisl", "Tuesday 2pm-5pm","Beau Plan"},
                {"Jeremy", "Wednesday 2pm-5pm","Beau Plan"},
        };
        Integer[] imgid = {
                R.drawable.veda,
                R.drawable.m,
                R.drawable.lisl,
                R.drawable.j,
                R.drawable.vreeti,
                R.drawable.m,
                R.drawable.lohinee,
                R.drawable.lisl,
                R.drawable.j,

        };

        for (int i = 0; i < AppsItemArray.length; i++) {
            Booking_ItemObject CIO = new Booking_ItemObject();
            if (!AppsItemArray[i][0].equals("")) {
                //Set Name text
                CIO.setName(AppsItemArray[i][0]);

                //Set Date
                if (AppsItemArray[i][1].equals("")) {
                    CIO.setBottomText("");
                } else {
                    CIO.setBottomText(AppsItemArray[i][1]);
                }
                //Set Location
                if (AppsItemArray[i][2].equals("")) {
                    CIO.setBottomText_2("");
                } else {
                    CIO.setBottomText_2(AppsItemArray[i][2]);
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

        String Location = ((Booking_ItemObject) arg0.getItemAtPosition(arg2))
                .getBottomText_2();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Menu);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Availability);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        String people = Menu;

        Intent i = new Intent(Booking_Location_People_Day.this, Booking_Calendar.class);
        i.putExtra("people", people);
        i.putExtra("convos", convos);
        i.putExtra("location", Location);

        Log.d("hello", "Position Clicked is " + arg2);
        Log.d("hello", "Item Clicked is " + Index);
        Log.d("hello", "User Clicked is " + ThisId);
        Log.d("hello", "Menu Clicked is " + Menu);
        Log.d("hello", "Location Clicked: " + Location);

     startActivity(i);

        }
    }
}
