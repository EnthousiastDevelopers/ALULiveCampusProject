package com.tolotranet.livecampus;


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

import java.util.Random;

/**
 * Created by Tolotra Samuel on 08/10/2016.
 */

public class Sign_check_mail extends Activity {

    Random rand;
    Spinner SpinnerDomain;
    public int random;
    ArrayAdapter<CharSequence> SpinnerDomainAdapter;
    String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_check_email);

        Button Blogin = (Button) findViewById(R.id.Blogin);

        SpinnerDomain = (Spinner) findViewById(R.id.spinnerDomain);
        SpinnerDomainAdapter= ArrayAdapter.createFromResource(this, R.array.sign_domain_array, R.layout.signdomainspinnerlayout);
        SpinnerDomainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerDomain.setAdapter(SpinnerDomainAdapter);
        SpinnerDomain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent2, View view2, int position2, long l2) {
                domain =   parent2.getItemAtPosition(position2).toString();
                //  Toast.makeText(getBaseContext(), parent2.getItemAtPosition(position2) + " is the place selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView2) {

            }
        });


        Blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.mailusername);
//                TextView domainTV = (TextView) findViewById(R.id.domain);
                String Uemail = username.getText().toString() + domain;


                Intent i = new Intent(Sign_check_mail.this, Sign_pass_check.class);

                if (username.getText().toString().equals("") || username.getText().toString() == null) {

                    Toast.makeText(getBaseContext(), "Please insert your ALU username", Toast.LENGTH_SHORT).show();
                } else {

                    Log.d("hello", "clicked");
                    if (isNetworkAvailable()) {


                        try {

                            Random r = new Random();
                            int Low = 100000;
                            int High = 999999;
                            random = r.nextInt(High - Low) + Low;
                            Sign_codesender_MailerClass_AsyncTask gmailMailer = new Sign_codesender_MailerClass_AsyncTask();

                            String subject = "Welcome to CampusLive!";
                            String body = "Your activation code is: <div style='font-size:50px'><b>" + String.valueOf(random) + "</b></div><br><br><br><br>***************************************************************************************\n" +
                                    "<br>#CampusLive Team.";

                            gmailMailer.BODY = body;
                            gmailMailer.SUBJECT = subject;
                            gmailMailer.RECIPIENT = Uemail;
                            gmailMailer.execute();

                            Log.d("HelloSendMail", Uemail);
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                        i.putExtra("code", random);
                        i.putExtra("email", Uemail);
                        Log.d("email is:", Uemail);
                        Log.d("sign up code is", String.valueOf(random));
                        startActivity(i);
                    } else {
                        Toast.makeText(getBaseContext(), " Cannot verify your identity offline. Please try check connection", Toast.LENGTH_SHORT).show();
                    }
                }
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
