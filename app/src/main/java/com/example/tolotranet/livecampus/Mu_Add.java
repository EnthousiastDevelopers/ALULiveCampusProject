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

/**
 * Created by Tolotra Samuel on 18/08/2016.
 */



public class Mu_Add extends Activity {

    Spinner maintZendeskCategory;
    ArrayAdapter<CharSequence> maintZendeskCategoryAdapter;

    Spinner maintZendeskLocation;
    ArrayAdapter<CharSequence> maintZendeskLocationAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    EditText titleET;
    EditText descrptionET;
    EditText phone1ET;
    EditText phone2ET;
    EditText phone3ET;

    Activity myActivity;
    String col1 = "" ;
    String col2 = "" ;
    String col3 = "" ;
    String col4 = "" ;
    String col5 = "" ;
    String col6 = "" ;
    String col7 = "" ;
    String col8 = "" ;

    String Uemail ;

    final String myTag = "DocsUpload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mu_add);


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


                    String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSd90CEcTqfDwwrfmFnINY9VpeaTO2F_BAy9eF3pUbB_l2x98w/formResponse";
                    HttpRequest mReq = new HttpRequest();


                    String data = "entry.1883075296=" + URLEncoder.encode(col1) + "&" +
                            "entry.1951449669=" + URLEncoder.encode(col2)+ "&" +
                            "entry.733850032=" + URLEncoder.encode(col3)+ "&" +
                            "entry.583468840=" + URLEncoder.encode(col4)+ "&" +
                            "entry.700251506=" + URLEncoder.encode(col5)+ "&" +
                            "entry.544598487=" + URLEncoder.encode(col6)+ "&" +
                            "entry.691271547=" + URLEncoder.encode(col8)+ "&" +
                            "entry.133876350=" + URLEncoder.encode(col7);
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
                    Sign_DatabaseHelper user = new Sign_DatabaseHelper(Mu_Add.this);
                    Uemail = user.getUserEmail();

                    titleET = (EditText)findViewById(R.id.titleET);
                    col1 = titleET.getText().toString();

                    descrptionET = (EditText)findViewById(R.id.descrptionET);
                    col2 = descrptionET.getText().toString();

                    phone1ET = (EditText)findViewById(R.id.phone1ET);
                    col3 = phone1ET.getText().toString();


                    col4 = Uemail;

                    col5 = "Full name";
                    //submit to google form
                    thread.start();

                    Log.d("hello", "attempted send copy to user email");




                    Sign_codesender_MailerClass_AsyncTask gmailMailer = new Sign_codesender_MailerClass_AsyncTask();

                    String subject = "Congratulation!! You've earned 1 point(s) by contributing to the App #CampusLive!";
                    String body = "Your contribution has been received. Everyone can now see it. " +
                            "Please check the following information: <div style='font-size:20px'><b>Your email address:</b>"+Uemail+"" +
                            "<ul><li><b>Title:</b>"+col1+"</li>" +
                            "<li><b>Category:</b>"+col6+"</li>" +
                            "<li><b>Description:</b>"+col2+"</li>" +
                            "<li><b>Phone:</b>"+col3+"</li>" +
                            "</ul>You also have got 1 point(s) through this action. Congratulation! See the leaderboard to check your friends' point.<b></b></div><br><br><br><br>***************************************************************************************\n" +
                        "<br>#CampusLive Team.";

                    gmailMailer.BODY = body;
                    gmailMailer.SUBJECT = subject;
                    gmailMailer.RECIPIENT = Uemail;
                    gmailMailer.execute();

                Toast.makeText(getBaseContext(),  " Ticket Submitted MADE!!! ", Toast.LENGTH_SHORT).show();

                //Go to confirm

                Intent i = new Intent(Mu_Add.this, Mu_Confirm.class);
                i.putExtra("cat", col6);
                i.putExtra("pho", col3);
                i.putExtra("des", col2);
                i.putExtra("tit", col1);

                startActivity(i);

            }else{
                    Toast.makeText(getBaseContext(),  " Cannot Submit ticket ticket while offline. Please try again later", Toast.LENGTH_SHORT).show();
                }}

        });




        maintZendeskCategory = (Spinner) findViewById(R.id.categorySP);
        maintZendeskCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.mu_category, R.layout.mu_add_spinnerlayout);
        maintZendeskCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maintZendeskCategory.setAdapter(maintZendeskCategoryAdapter);

        String category = Mu_App.mu_category;
        Log.d("hello","Index of value in spinner is: "+ String.valueOf(getIndex(maintZendeskCategory, category)));
        maintZendeskCategory.setSelection(getIndex(maintZendeskCategory, category)); //setting spinner position automatically

        maintZendeskCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                 col6 =   parent.getItemAtPosition(position).toString();

               // Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " is the category selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

}