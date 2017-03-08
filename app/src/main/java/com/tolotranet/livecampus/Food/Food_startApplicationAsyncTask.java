package com.tolotranet.livecampus.Food;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tolotranet.livecampus.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

public class Food_startApplicationAsyncTask extends AsyncTask<Activity, Void, Void> {

	Activity myActivity;
	Boolean FileExists;
	Boolean InternetConnection;

	@Override
	protected Void doInBackground(Activity... arg0) {
		// TODO Auto-generated method stub
		myActivity = arg0[0];
		FileExists = false;
		InternetConnection = false;

		File Root = Environment.getExternalStorageDirectory();
		File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
		File myfile = new File(Dir, "Food.txt");

		if (myfile.exists()) {
			Log.d("hello", "File Exists");
			FileExists = true;
			try {
				new Food_XMLParserClass();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			FileExists = false;
			Log.d("hello", "File doesn't exist");

			if ((InternetConnection = isNetworkAvailable())) {
				Food_GetDataAsyncTask getDataTask = new Food_GetDataAsyncTask();
				getDataTask.execute(myActivity);
			}

			Log.d("hello", "Network Connection " + isNetworkAvailable());
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		if (FileExists == true) {

			Intent i = new Intent();
			i.setClass(myActivity.getApplicationContext(), Food_MainList.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			myActivity.getApplicationContext().startActivity(i);
			myActivity.overridePendingTransition(0, 0);
			myActivity.finish();

		} else {
			if (!InternetConnection) {
				Toast.makeText(myActivity.getApplicationContext(),
						"Please Connect to app_maint", Toast.LENGTH_LONG).show();
				TextView mytv = (TextView) myActivity
						.findViewById(R.id.StartTextView);
				mytv.setText("Please Connect to Internet");
			}
		}
		super.onPostExecute(result);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) myActivity
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

}
