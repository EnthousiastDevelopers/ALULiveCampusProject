package com.tolotranet.livecampus.Maint;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.App.App_Tools;
import com.tolotranet.livecampus.HttpRequest;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sign.Sign_DatabaseHelper;
import com.tolotranet.livecampus.Sign_User_Object;
import com.tolotranet.livecampus.Sign.Sign_codesender_MailerClass_AsyncTask;

import java.net.URLEncoder;

/**
 * Created by Tolotra Samuel on 18/08/2016.
 */



public class Maint_Add extends AppCompatActivity {

    Spinner maintZendeskCategory;
    ArrayAdapter<CharSequence> maintZendeskCategoryAdapter;

    Spinner maintZendeskLocation;
    ArrayAdapter<CharSequence> maintZendeskLocationAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    EditText maintZendeskDescription;
    Activity myActivity;
    String category;
    String place;
    String description, name,email,object, room, residence, apartment;

    final String myTag = "DocsUpload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintzendesk);
        name = Sign_User_Object.Name;
        email = Sign_User_Object.Email;
        room = Sign_User_Object.RoomNumber;
        residence = Sign_User_Object.Residence;
        apartment = Sign_User_Object.Apartment;

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

                    description = maintZendeskDescriptionStr;

                    String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSc3thwTViHK1r7r3-tYwYIoN7-KgBJjq40TWjDpwzIQrTorFw/formResponse";
                    HttpRequest mReq = new HttpRequest();


                    String data = "entry.544598487=" + URLEncoder.encode(category) + "&" +
                            "entry.691271547=" + URLEncoder.encode(place)+ "&" +
                            "entry.700251506=" + URLEncoder.encode(name)+ "&" +
                            "entry.583468840=" + URLEncoder.encode(email)+ "&" +
                            "entry.733850032=" + URLEncoder.encode(residence)+ "&" +
                            "entry.1883075296=" + URLEncoder.encode(room)+ "&" +
                            "entry.133876350=" + URLEncoder.encode(apartment)+ "&" +
                            "entry.34413419=" + URLEncoder.encode(object)+ "&" +
                            "entry.1951449669=" + URLEncoder.encode(description);
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

                    App_Tools tools = new App_Tools();
                    object = tools.randomString(); //because we need to give the item an unique object id to allow it to have features like votes, status, rank or comments
                    //submit to google form
                thread.start();

                //send copy to user email
                //send to copy to email of mailer
                Log.d("hello", "attempted copy user email");


                    Sign_DatabaseHelper user = new Sign_DatabaseHelper(Maint_Add.this);
                    String  Uemail = user.getUserEmail();

                    Sign_codesender_MailerClass_AsyncTask gmailMailer = new Sign_codesender_MailerClass_AsyncTask();

                    String subject = "Maintenance ticket submitted via App #CampusLive!";
                    String body = "Your maintenance issue has been reported. A new ticket will be opened. Please check the following information: <div style='font-size:20px'><b>Your email address:</b>"+Uemail+"<ul><li><b>Category:</b>"+ category +"</li><li><b>Place:</b>"+ place +"</li><li><b>Description:</b>"+ description +"</li></ul><b></b></div><br><br><br><br>***************************************************************************************\n" +
                        "<br>#CampusLive Team.";

                    gmailMailer.BODY = body;
                    gmailMailer.SUBJECT = subject;
                    gmailMailer.RECIPIENT = Uemail;
                    gmailMailer.execute();

                Toast.makeText(getBaseContext(),  " Ticket Submitted MADE!!! ", Toast.LENGTH_SHORT).show();

                //Go to confirm

                Intent i = new Intent(Maint_Add.this, Maint_Confirm.class);
                i.putExtra("cat", category);
                i.putExtra("place", place);
                i.putExtra("des", description);

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
                 category =   parent.getItemAtPosition(position).toString();

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
                 place =   parent2.getItemAtPosition(position2).toString();
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