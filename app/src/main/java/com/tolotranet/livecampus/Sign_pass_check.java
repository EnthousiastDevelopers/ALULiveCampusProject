package com.tolotranet.livecampus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * Created by Tolotra Samuel on 08/10/2016.
 */

public class Sign_pass_check extends Activity {
    int codeextra;
    String emailextra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_pass_check);

        TextView emailTV = (TextView) findViewById(R.id.email);
        emailextra = getIntent().getStringExtra("email");
        emailTV.setText(emailextra);

        codeextra = getIntent().getIntExtra("code", 0);
        Log.d("hello", String.valueOf(codeextra));
    }

    public void onSignPassCheck(View v) {

        if (v.getId() == R.id.verify) {

            EditText codeET = (EditText) findViewById(R.id.codeET);
            int code = 0;
            if (codeET.getText().toString().equals("") || codeET.getText().toString() == null) {
                code = 0;
            } else {
                code = new Integer(parseInt(codeET.getText().toString()));
            }
            Log.d("hello-check", String.valueOf(code));
            Log.d("hello-check", String.valueOf(codeextra));
            if (code == codeextra) {

                Intent i = new Intent(Sign_pass_check.this, Sign_Up_confirm.class);

                i.putExtra("email", emailextra);
                Log.d("email is:", emailextra);
                startActivity(i);
            } else {
                Toast pass = Toast.makeText(Sign_pass_check.this, "Code incorrect. Please check and try again", Toast.LENGTH_SHORT);
                pass.show();
            }
        }
        if (v.getId() == R.id.resendcode) {

            if (isNetworkAvailable()) {
                Toast pass = Toast.makeText(Sign_pass_check.this, "New code has been sent. Previous code will be invalid", Toast.LENGTH_SHORT);
                pass.show();


                try {

                    Random r = new Random();
                    int Low = 100000;
                    int High = 999999;
                    int random = r.nextInt(High - Low) + Low;
                    codeextra = random;
                    Sign_codesender_MailerClass_AsyncTask gmailMailer = new Sign_codesender_MailerClass_AsyncTask();

                    String subject = "Welcome to CampusLive!";
                    String body = "Your activation code is: <div style='font-size:50px'><b>" + String.valueOf(random) + "</b></div><br><br><br><br>***************************************************************************************\n" +
                            "<br>#CampusLive Team.";

                    gmailMailer.BODY = body;
                    gmailMailer.SUBJECT = subject;
                    gmailMailer.RECIPIENT = emailextra;
                    gmailMailer.execute();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            } else {

                Toast.makeText(getBaseContext(), " Cannot verify your identity offline. Please check connection", Toast.LENGTH_SHORT).show();
            }
        }
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
