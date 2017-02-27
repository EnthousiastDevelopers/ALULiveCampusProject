package com.tolotranet.livecampus;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

import static android.os.SystemClock.sleep;

/**
 * Created by Tolotra Samuel on 08/10/2016.
 */

public class Sign_startApplication extends Activity {
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_start_application);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Welcome Back");
        mProgressDialog.show();
        Log.d("passcheck", "L1");

        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(this);
        Log.d("passcheck", "L11");

        Log.d("passcheck", "L111");

        boolean log_exist = helper.check_log_exist();
        if (log_exist) {
            helper.AllUserDataBaseToObject();

            boolean passcheck = helper.check_pass_requirement();
            if (passcheck) {
                Log.d("passcheck", "L2");
                Intent i = new Intent(Sign_startApplication.this, Sign_in.class);
                // i.putExtra("Username", str);
                startActivity(i);
            }
            if (!passcheck) {
                Log.d("passcheck", "L3");
                if (isNetworkAvailable()) {
                    Sis_GetDataAsyncTask InitialisetData = new Sis_GetDataAsyncTask();
                    InitialisetData.Target = "apps";
                    InitialisetData.execute(Sign_startApplication.this);

                    // Intent i = new Intent(Sign_startApplication.this, AppSelect_Parent.class);
                    //i.putExtra("Username", str);
                    // startActivity(i);
                }else {
                    File Root = Environment.getExternalStorageDirectory();
                    File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
                    File myfile = new File(Dir, "SISList.txt");
                    if (myfile.exists()) {
                        Log.d("hello", "File Exists");
                        //FileExists = true;
                        try {
                            new Sis_XMLParserClass();
                            Intent i = new Intent(Sign_startApplication.this, AppSelect_Parent.class);
                            //i.putExtra("Username", str);
                            startActivity(i);
                        } catch (XmlPullParserException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                }else {
                        Toast.makeText(Sign_startApplication.this, "Check your memory card or your internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            Log.d("passcheck", "L4");
            Intent i = new Intent(Sign_startApplication.this, Sign_check_mail.class);
            startActivity(i);
        }
        Log.d("passcheck", "L5");
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Sign_startApplication.this
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }
}