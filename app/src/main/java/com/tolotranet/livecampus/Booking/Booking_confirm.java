package com.tolotranet.livecampus.Booking;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.App.AppSelect_Parent;
import com.tolotranet.livecampus.R;

/**
 * Created by Tolotra Samuel on 10/10/2016.
 */

public class Booking_confirm extends Activity {
    String Breakfast, Lunch, Dinner, date, time;
String col1, col2, col3, col4, col5;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_confirm);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        Intent i = getIntent();

        col1 = i.getStringExtra("convos");
        col2 = i.getStringExtra("people");
        col3 = i.getStringExtra("location");
        col4 = i.getStringExtra("date");
        col5 = i.getStringExtra("time");

        TextView tv1 = (TextView) findViewById(R.id.convosValue);
        tv1.setText(col1);
        TextView tv2 = (TextView) findViewById(R.id.staffValue);
        tv2.setText(col2);
        TextView tv3 = (TextView) findViewById(R.id.venueValue);
        tv3.setText(col3);
        TextView tv4 = (TextView) findViewById(R.id.dateValue);
        tv4.setText(col4);
        TextView tv5 = (TextView) findViewById(R.id.timeValue);
        tv5.setText(col5);



        Button calcBtn = (Button) findViewById(R.id.backBtn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Booking_confirm.this, AppSelect_Parent.class);
                startActivity(i);
            }
        });
    }
}