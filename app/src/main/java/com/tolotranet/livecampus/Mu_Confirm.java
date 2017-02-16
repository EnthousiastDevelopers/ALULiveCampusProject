package com.tolotranet.livecampus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tolotra Samuel on 10/10/2016.
 */

public class Mu_Confirm extends Activity {
    String cat, pho, des, tit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mu_add_confirm);


        cat = getIntent().getStringExtra("cat");
        pho = getIntent().getStringExtra("pho");
        des = getIntent().getStringExtra("des");
        tit = getIntent().getStringExtra("tit");

        TextView catTV = (TextView) findViewById(R.id.Category);
        TextView desTV = (TextView) findViewById(R.id.Description);
        TextView phoTV = (TextView) findViewById(R.id.Phone);
        TextView titTV = (TextView) findViewById(R.id.Title);


        catTV.setText(cat);
        phoTV .setText(pho);
        desTV.setText(des);
        titTV.setText(tit);


        Button calcBtn = (Button) findViewById(R.id.backBtn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isNetworkAvailable()){
                    Toast.makeText(getApplicationContext(), "No internet connection, it will be updated automatically later", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Mu_Confirm.this, Mu_SpreadSheetActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(), "Updating, please wait.....", Toast.LENGTH_LONG).show();
                    Mu_App.mu_category = cat;
                    Mu_GetDataAsyncTask getDataTask = new Mu_GetDataAsyncTask();
                    getDataTask.execute(Mu_Confirm.this);
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

}