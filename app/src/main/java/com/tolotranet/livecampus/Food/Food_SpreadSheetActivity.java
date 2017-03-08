package com.tolotranet.livecampus.Food;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tolotranet.livecampus.Com.Com_GetDataAsyncTask;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sis.Sis_XMLParserClass;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

public class Food_SpreadSheetActivity extends Activity {

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_spread_sheet);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");
        mProgress.show();

        if (isNetworkAvailable()) {
            Com_GetDataAsyncTask commentTaskBackGround = new Com_GetDataAsyncTask(); // because we cannot make it static, getData() is already inside it and cannot be called it is static
            commentTaskBackGround.synchronize(); // because we are loading the comment from the cloud into the system before showing the objects
        } else {
            File Root = Environment.getExternalStorageDirectory();

            File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
            File myfile = new File(Dir, "SISList.txt");
            if (myfile.exists()) {
                Log.d("hello", "File Exists");
                //FileExists = true;
                try {
                    new Sis_XMLParserClass();

                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Food_startApplicationAsyncTask myTask = new Food_startApplicationAsyncTask();
        myTask.execute(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spread_sheet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsSpreadSheet:
                if (isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Updating.....",
                            Toast.LENGTH_SHORT).show();
                    Food_GetDataAsyncTask getDataTask = new Food_GetDataAsyncTask();
                    getDataTask.execute(this);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "check Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

}
