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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.net.URLEncoder;

/**
 * Created by Tolotra Samuel on 18/08/2016.
 */


public class Booking_Select extends Activity {

    Spinner foodBreakfastSpinner;
    ArrayAdapter<CharSequence> foodBreakfastadapter;

    Spinner foodLunchSpinner;
    ArrayAdapter<CharSequence> foodLunchadapter;

    Spinner bookTimeSpinner;
    ArrayAdapter<CharSequence> foodDinneradapter;
    Activity myActivity;
    String col1 = "Milk";
    String col2 = "Orange";
    String col3 = "Rice";
    String col4 = "date";
    String col5 = "5pm";
    final String myTag = "DocsUpload";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_select);
        Intent i = getIntent();

        col1 = i.getStringExtra("convos");
        col2 = i.getStringExtra("people");
        col3 = i.getStringExtra("location");
        col4 = i.getStringExtra("date");

        Log.d("hello", col3);
        TextView tv1 = (TextView) findViewById(R.id.convosValue);
        tv1.setText(col1);
        TextView tv2 = (TextView) findViewById(R.id.staffValue);
        tv2.setText(col2);
        TextView tv3 = (TextView) findViewById(R.id.venueValue);
        tv3.setText(col3);
        TextView tv4 = (TextView) findViewById(R.id.dateValue);
        tv4.setText(col4);

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

                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "book_select" );
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "book_select");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "book_select button click");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                if (isNetworkAvailable()) {


                    thread.start();

                    //send copy to user email
                    //send to copy to email of mailer
                    Log.d("hello", "attempted copy user email");


                    Sign_DatabaseHelper user = new Sign_DatabaseHelper(Booking_Select.this);
                    String Uemail = user.getUserEmail();

                    Sign_codesender_MailerClass_AsyncTask gmailMailer = new Sign_codesender_MailerClass_AsyncTask();

                    String subject = "Student life Appointment booking via App #CampusLive!";
                    String body = "Your appointment has been booked successfully. Please check the following information: <div style='font-size:20px'><b>Your email address: </b>" + Uemail + "<ul><li><b>Theme: </b>" + col1 + "</li><li><b>Staff: </b>" + col2 + "</li><li><b>Location: </b>" + col3 + "</li><li><b>Date: </b>" + col4 + "</li><li><b>Slot: </b>" + col5 + "</li></ul><br><b>An email from the Staff will be sent to you for confirmation</b></div><br><br><br><br>***************************************************************************************\n" +
                            "<br>Thanks for using the #CampusLive.  <br>TheTeam.";

                    gmailMailer.BODY = body;
                    gmailMailer.SUBJECT = subject;
                    gmailMailer.RECIPIENT = Uemail;
                    gmailMailer.execute();

                    Toast.makeText(getBaseContext(), " Appointment Submitted! ", Toast.LENGTH_SHORT).show();
                    //Your code goes here

                    //Go to confirm

                    Intent i = new Intent(Booking_Select.this, Booking_confirm.class);
                    i.putExtra("convos", col1);
                    i.putExtra("people", col2);
                    i.putExtra("location", col3);
                    i.putExtra("date", col4);
                    i.putExtra("time", col5);
                    startActivity(i);
                } else {
                    Toast.makeText(getBaseContext(), " Cannot order food while offline. Try again later ", Toast.LENGTH_SHORT).show();
                }
            }
        });



        bookTimeSpinner = (Spinner) findViewById(R.id.bookTimeSpinner);
        foodDinneradapter = ArrayAdapter.createFromResource(this, R.array.book_slot_test, R.layout.book_spinnerlayout);
        foodDinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookTimeSpinner.setAdapter(foodDinneradapter);
        bookTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent5, View view5, int position5, long l5) {
                col5 = parent5.getItemAtPosition(position5).toString();
                // Toast.makeText(getBaseContext(), parent5.getItemAtPosition(position5) + " is the dinner selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView5) {

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