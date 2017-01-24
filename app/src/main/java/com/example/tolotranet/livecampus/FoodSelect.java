package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.net.URLEncoder;

/**
 * Created by Tolotra Samuel on 18/08/2016.
 */


public class FoodSelect extends Activity {

    Spinner foodBreakfastSpinner;
    ArrayAdapter<CharSequence> foodBreakfastadapter;

    Spinner foodLunchSpinner;
    ArrayAdapter<CharSequence> foodLunchadapter;

    Spinner foodDinnerSpinner;
    ArrayAdapter<CharSequence> foodDinneradapter;
    Activity myActivity;
    String col1 = "Milk";
    String col2 = "Orange";
    String col3 = "Rice";
    String col4 = "date";
    final String myTag = "DocsUpload";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodselect);
        col4 = getIntent().getStringExtra("date");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String fullUrl = "https://docs.google.com/forms/u/0/d/1fxPFhTg2El1tnBmBtrvulF5RfjyYG7GJXOiJQOZ8rWU/formResponse";
                    HttpRequest mReq = new HttpRequest();


                    String data = "entry.917676895=" + URLEncoder.encode(col1) + "&" +
                            "entry.538081858=" + URLEncoder.encode(col2) + "&" +
                            "entry.981893535=" + URLEncoder.encode(col3) + "&" +
                            "entry.999999999=" + URLEncoder.encode(col4);
                    String response = mReq.sendPost(fullUrl, data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Button calcBtn = (Button) findViewById(R.id.foodSelectbutton);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "foodselect" );
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "foodselect");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "foodselect button click");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                if (isNetworkAvailable()) {


                    thread.start();

                    //send copy to user email
                    //send to copy to email of mailer
                    Log.d("hello", "attempted copy user email");


                    Sign_DatabaseHelper user = new Sign_DatabaseHelper(FoodSelect.this);
                    String Uemail = user.getUserEmail();

                    Sign_codesender_MailerClass_AsyncTask gmailMailer = new Sign_codesender_MailerClass_AsyncTask();

                    String subject = "Food menu ordered via App #CampusLive!";
                    String body = "Your food menu has been ordered. Please check the following information: <div style='font-size:20px'><b>Your email address:</b>" + Uemail + "<ul><li><b>Breakfast:</b>" + col1 + "</li><li><b>Lunch:</b>" + col2 + "</li><li><b>Dinner:</b>" + col3 + "</li></ul><b></b></div><br><br><br><br>***************************************************************************************\n" +
                            "<br>#CampusLive Team.";

                    gmailMailer.BODY = body;
                    gmailMailer.SUBJECT = subject;
                    gmailMailer.RECIPIENT = Uemail;
                    gmailMailer.execute();

                    Toast.makeText(getBaseContext(), " Ticket Submitted MADE!!! ", Toast.LENGTH_SHORT).show();
                    //Your code goes here

                    //Go to confirm

                    Intent i = new Intent(FoodSelect.this, Food_Confirm.class);
                    i.putExtra("Breakfast", col1);
                    i.putExtra("Lunch", col2);
                    i.putExtra("Dinner", col3);

                    startActivity(i);
                } else {
                    Toast.makeText(getBaseContext(), " Cannot order food while offline. Try again later ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        foodBreakfastSpinner = (Spinner) findViewById(R.id.foodBreakfast);
        foodBreakfastadapter = ArrayAdapter.createFromResource(this, R.array.food_menu_breakfast, R.layout.foodspinnerlayout);
        foodBreakfastadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodBreakfastSpinner.setAdapter(foodBreakfastadapter);
        foodBreakfastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                col1 = parent.getItemAtPosition(position).toString();

                //  Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " is the breakfast selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        foodLunchSpinner = (Spinner) findViewById(R.id.foodLunch);
        foodLunchadapter = ArrayAdapter.createFromResource(this, R.array.food_menu_lunch, R.layout.foodspinnerlayout);
        foodLunchadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodLunchSpinner.setAdapter(foodLunchadapter);
        foodLunchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent2, View view2, int position2, long l2) {
                col2 = parent2.getItemAtPosition(position2).toString();
                //   Toast.makeText(getBaseContext(), parent2.getItemAtPosition(position2) + " is the lunch selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView2) {

            }
        });

        foodDinnerSpinner = (Spinner) findViewById(R.id.foodDinner);
        foodDinneradapter = ArrayAdapter.createFromResource(this, R.array.food_menu_dinner, R.layout.foodspinnerlayout);
        foodDinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodDinnerSpinner.setAdapter(foodDinneradapter);
        foodDinnerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent3, View view3, int position3, long l3) {
                col3 = parent3.getItemAtPosition(position3).toString();
                // Toast.makeText(getBaseContext(), parent3.getItemAtPosition(position3) + " is the dinner selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView3) {

            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }


}