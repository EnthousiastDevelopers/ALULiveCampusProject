package com.example.tolotranet.livecampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Sign_in extends AppCompatActivity {

    Sign_DatabaseHelper helper = new Sign_DatabaseHelper(this);
    TextView emailTV;
    String emailDB;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        setContentView(R.layout.sign_in_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("hello", "before getUseremail");
        emailDB = helper.getUserEmail();
        Log.d("hello", "after getUseremail");
        emailTV = (TextView) findViewById(R.id.emailTV);
        emailTV.setText(emailDB);

        mFirebaseAnalytics.setUserId(emailDB);//set user ID

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onButtonClick(View v) {

        if (v.getId() == R.id.Blogin) {


            TextView a = (TextView) findViewById(R.id.emailTV);
            String str = a.getText().toString();

            EditText b = (EditText) findViewById(R.id.TFpassword);
            String pass = b.getText().toString();

            String password = helper.searchPass(str);

            Bundle params2 = new Bundle();
            params2.putString("passowrd", pass);
            params2.putString("pasword true", password);
            mFirebaseAnalytics.logEvent("passwrods", params2);


            if (pass.equals(password))

            {
                Intent i = new Intent(Sign_in.this, AppSelect.class);
                i.putExtra("Username", str);
                startActivity(i);
            } else {
                Toast temp = Toast.makeText(Sign_in.this, "Incorrect password!", Toast.LENGTH_SHORT);
                temp.show();
            }


        }
        if (v.getId() == R.id.Breset) {

            Sign_DatabaseHelper helper = new Sign_DatabaseHelper(Sign_in.this);

            helper.cleanTable();
            Bundle bundle2 = new Bundle();

            bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
            bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, "reseted password");
            bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "reseted pass");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);


            Intent i = new Intent(Sign_in.this, Sign_check_mail.class);
            startActivity(i);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
