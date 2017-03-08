package com.tolotranet.livecampus.App;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.CalendarAPI;
import com.tolotranet.livecampus.Food.FoodCalendar;
import com.tolotranet.livecampus.R;


/**
 * Created by Tolotra Samuel on 18/08/2016.
 */

public class App_Contribute extends Activity {
    ListView toolList;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodapp);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        String[] apps = {
                "Suggest Taxi Cab",
                "Suggest Food",
                "Submit Idea for the app",
                "Operation Team Feedback",
                "Student Life Feedback",
                "Suggest Restaurant",
                "Suggest Activities",
                "Suggest Pharmacy",
                "Suggest Hospital, Clinics, Ambulance",
                "Other Contribution",

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, apps  );

        toolList = (ListView)findViewById(R.id.foodapp_listView);
        toolList.setAdapter(adapter);

        toolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " is selected", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(position));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, (String) parent.getItemAtPosition(position));
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, (String) parent.getItemAtPosition(position));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                if (parent.getItemAtPosition(position) == "Preorder food") {

                    Intent i = new Intent(App_Contribute.this, FoodCalendar.class);
                    startActivity(i);
                }

                if (parent.getItemAtPosition(position) == "CalendarAPI") {

                    Intent i = new Intent(App_Contribute.this, CalendarAPI.class);
                    startActivity(i);
                }
            }
        });
    }
}
