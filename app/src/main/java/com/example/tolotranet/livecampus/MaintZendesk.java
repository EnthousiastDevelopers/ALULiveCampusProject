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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by Tolotra Samuel on 18/08/2016.
 */



public class MaintZendesk extends Activity {

    Spinner maintZendeskCategory;
    ArrayAdapter<CharSequence> maintZendeskCategoryAdapter;

    Spinner maintZendeskLocation;
    ArrayAdapter<CharSequence> maintZendeskLocationAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    EditText maintZendeskDescription;
    Activity myActivity;
    String col1 ;
    String col2 ;
    String col3 ;

    final String myTag = "DocsUpload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintzendesk);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        final Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    EditText maintZendeskDescription = (EditText)findViewById(R.id.maintZendeskDescription);
                    String maintZendeskDescriptionStr = maintZendeskDescription.getText().toString();

                    col3 = maintZendeskDescriptionStr;

                    String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSc3thwTViHK1r7r3-tYwYIoN7-KgBJjq40TWjDpwzIQrTorFw/formResponse";
                    HttpRequest mReq = new HttpRequest();


                    String data = "entry.544598487=" + URLEncoder.encode(col1) + "&" +
                            "entry.691271547=" + URLEncoder.encode(col2)+ "&" +
                            "entry.1951449669=" + URLEncoder.encode(col3);
                    String response = mReq.sendPost(fullUrl, data);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


        Button calcBtn = (Button) findViewById(R.id.maintZendeskSubmit);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {

                    //submit to google form
                thread.start();

                //send copy to user email
                //send to copy to email of mailer
                Log.d("hello", "attempted copy user email");


                    Sign_DatabaseHelper user = new Sign_DatabaseHelper(MaintZendesk.this);
                    String  Uemail = user.getUserEmail();

                    Sign_codesender_MailerClass_AsyncTask gmailMailer = new Sign_codesender_MailerClass_AsyncTask();

                    String subject = "Maintenance ticket submitted via App #CampusLive!";
                    String body = "Your maintenance issue has been reported. A new ticket will be opened. Please check the following information: <div style='font-size:20px'><b>Your email address:</b>"+Uemail+"<ul><li><b>Category:</b>"+col1+"</li><li><b>Place:</b>"+col2+"</li><li><b>Description:</b>"+col3+"</li></ul><b></b></div><br><br><br><br>***************************************************************************************\n" +
                        "<br>#CampusLive Team.";

                    gmailMailer.BODY = body;
                    gmailMailer.SUBJECT = subject;
                    gmailMailer.RECIPIENT = Uemail;
                    gmailMailer.execute();

                Toast.makeText(getBaseContext(),  " Ticket Submitted MADE!!! ", Toast.LENGTH_SHORT).show();

                //Go to confirm

                Intent i = new Intent(MaintZendesk.this, Maint_Confirm.class);
                i.putExtra("cat", col1);
                i.putExtra("place", col2);
                i.putExtra("des", col3);

                startActivity(i);

            }else{
                    Toast.makeText(getBaseContext(),  " Cannot Submit ticket ticket while offline. Please try again later", Toast.LENGTH_SHORT).show();
                }}

        });




        maintZendeskCategory = (Spinner) findViewById(R.id.maintZendeskCategory);
        maintZendeskCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.maint_zendesk_category, R.layout.maintspinnerlayout);
        maintZendeskCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maintZendeskCategory.setAdapter(maintZendeskCategoryAdapter);
        maintZendeskCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                 col1 =   parent.getItemAtPosition(position).toString();

               // Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " is the category selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        maintZendeskLocation = (Spinner) findViewById(R.id.maintZendeskLocation);
        maintZendeskLocationAdapter = ArrayAdapter.createFromResource(this, R.array.maint_zendesk_place, R.layout.maintspinnerlayout);
        maintZendeskLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maintZendeskLocation.setAdapter(maintZendeskLocationAdapter);
        maintZendeskLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent2, View view2, int position2, long l2) {
                 col2 =   parent2.getItemAtPosition(position2).toString();
              //  Toast.makeText(getBaseContext(), parent2.getItemAtPosition(position2) + " is the place selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView2) {

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