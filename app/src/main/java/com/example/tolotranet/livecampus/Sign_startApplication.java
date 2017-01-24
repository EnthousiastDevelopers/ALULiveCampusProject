package com.example.tolotranet.livecampus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Tolotra Samuel on 08/10/2016.
 */

public class Sign_startApplication extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("passcheck", "L1");

        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(this);
        Log.d("passcheck", "L11");

        Log.d("passcheck", "L111");

        boolean log_exist = helper.check_log_exist();
        if (log_exist) {
            boolean passcheck = helper.check_pass_requirement();
            if (passcheck) {
                Log.d("passcheck", "L2");
                Intent i = new Intent(Sign_startApplication.this, Sign_in.class);
                // i.putExtra("Username", str);
                startActivity(i);
            }
            if (!passcheck) {
                Log.d("passcheck", "L3");
                Intent i = new Intent(Sign_startApplication.this, AppSelect.class);
                //i.putExtra("Username", str);
                startActivity(i);
            }
        } else {
            Log.d("passcheck", "L4");
            Intent i = new Intent(Sign_startApplication.this, Sign_check_mail.class);
            startActivity(i);
        }
        Log.d("passcheck", "L5");
    }


}