package com.tolotranet.livecampus.Food;


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

public class Food_Confirm extends Activity {
    String Breakfast, Lunch, Dinner;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_confirm);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        Breakfast = getIntent().getStringExtra("Breakfast");
        Lunch = getIntent().getStringExtra("Lunch");
        Dinner = getIntent().getStringExtra("Dinner");

        TextView BreakfastTV = (TextView) findViewById(R.id.foodBreakfast);
        TextView DinnerTV = (TextView) findViewById(R.id.foodDinner);
        TextView LunchTV = (TextView) findViewById(R.id.foodLunch);


        BreakfastTV.setText(Breakfast);
        LunchTV.setText(Lunch);
        DinnerTV.setText(Dinner);


        Button calcBtn = (Button) findViewById(R.id.backBtn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Food_Confirm.this, AppSelect_Parent.class);
                startActivity(i);
            }
        });
    }
}