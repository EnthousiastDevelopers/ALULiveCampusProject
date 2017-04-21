package com.tolotranet.livecampus.Sis;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tolotranet.livecampus.App.AppSelect_Parent;
import com.tolotranet.livecampus.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

public class Sis_Switch_SpreadSheetActivity extends Activity {
	Activity myActivity;
	Context context;
	Boolean FileExists;
	Boolean InternetConnection;
	String target;
	ProgressDialog mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("Loading data ...");
		mProgress.show();

		//target is the next intent we want to go after loading the xml data
		target = getIntent().getStringExtra("target");
		setContentView(R.layout.sis_spread_sheet);
		Sis_startApplicationAsyncTaskLocal();
	}

	private void Sis_startApplicationAsyncTaskLocal() {
		if(makeSureInteractionListisLoadedFromFile()){
			StartTargetActivity(target);
		}else{
			FileExists = false;
			Log.d("hello", "File doesn't exist");

			if ((InternetConnection = isNetworkAvailable())) {
				Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask(){
					@Override
					protected void onPostExecute(Void v){
						StartTargetActivity(target);
					}
				};
				getDataTask.Target = "refreshonly";
				getDataTask.execute(myActivity);
			}
			Log.d("hello", "Network Connection " + isNetworkAvailable());
		}
	}

	private boolean makeSureInteractionListisLoadedFromFile() {
		myActivity = Sis_Switch_SpreadSheetActivity.this;
		InternetConnection = false;
		context = myActivity.getApplicationContext();
		File Root = Environment.getExternalStorageDirectory();
		File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
		File myfile = new File(Dir, "SISList.txt");

		if (myfile.exists()) {
			Log.d("hello", "File Exists");
			try {
				new Sis_XMLParserClass();
				return true;
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false; //meaning the file doesn't exists
	}

	private void StartTargetActivity(String target) {
		Intent i = new Intent(myActivity.getApplicationContext(), Sis_MainList.class);
		if (target.equals("card")) {
			i = new Intent(myActivity.getApplicationContext(), Sis_Interaction_MainList.class);
		}
		if (target.equals("me")) {
			i = new Intent(myActivity.getApplicationContext(), Sis_DetailListViewOwner.class);
		}
		if (target.equals("apps")) {
			i = new Intent(myActivity.getApplicationContext(), AppSelect_Parent.class);
		}
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		myActivity.startActivity(i);
		myActivity.overridePendingTransition(0, 0);
		myActivity.finish();
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
				Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
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
	protected void onDestroy()
	{
		super.onDestroy();
		if (mProgress!=null && mProgress.isShowing()){
			mProgress.dismiss();
		}
	}

}
