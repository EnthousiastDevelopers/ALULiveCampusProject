package com.tolotranet.livecampus;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Food_SpreadSheetActivity extends Activity {

	ProgressDialog mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_spread_sheet);

		mProgress = new ProgressDialog(this);
		mProgress.setMessage("Loading data ...");
		mProgress.show();

		Com_GetDataAsyncTask commentTaskBackGround = new Com_GetDataAsyncTask(); // because we cannot make it static, getData() is already inside it and cannot be called it is static
		commentTaskBackGround.synchronize(); // because we are loading the comment from the cloud into the system before showing the objects
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
	protected void onDestroy()
	{
		super.onDestroy();
		if (mProgress!=null && mProgress.isShowing()){
			mProgress.dismiss();
		}
	}

}
