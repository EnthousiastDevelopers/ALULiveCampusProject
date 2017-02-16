package com.tolotranet.livecampus;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static com.tolotranet.livecampus.App_Tools.CheckIfFirstBeforeLastDates;

public class Transp_TransApp extends AppCompatActivity {

	ArrayList<Transp_ItemObject> ContactItemArray;
	Transp_MyCustomBaseAdapter myAdapter;
	EditText SearchET;
	ListView lv;
	ProgressDialog mProgress;
	com.github.clans.fab.FloatingActionButton fab_refresh;
	com.github.clans.fab.FloatingActionButton fab_add;
	SimpleDateFormat formatter1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("Loading data ...");

		setContentView(R.layout.transp_schedule);

		ContactItemArray = MakeArrayList();
		lv = (ListView) findViewById(R.id.Contacts_list_view);
		SearchET = (EditText) findViewById(R.id.SearchET);

		myAdapter = new Transp_MyCustomBaseAdapter(getApplicationContext(),
				ContactItemArray);
		lv.setAdapter(myAdapter);

		fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_refresh);

		fab_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isNetworkAvailable()){
					Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
				}else {
					fab_refresh.startAnimation(AnimationUtils.loadAnimation(Transp_TransApp.this, R.anim.rotation));
					Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
					Transp_GetDataAsyncTask getDataTask = new Transp_GetDataAsyncTask();
					getDataTask.execute(Transp_TransApp.this);
				}
			}
		});

		MyTextWatcher mytextwatcher = new MyTextWatcher();
		SearchET.addTextChangedListener(mytextwatcher);
		lv.setOnItemClickListener(new AllContactListViewClickListener());


		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final EditText minutes = new EditText(getApplicationContext());
				final String TimeStamp = ((Transp_ItemObject) arg0.getItemAtPosition(arg2)).getTimeStamp();
				Date alertDate = null;
				try {
					 alertDate = formatter1.parse(TimeStamp);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				minutes.setInputType(InputType.TYPE_CLASS_NUMBER);
				final Date finalAlertDate = alertDate;
				new AlertDialog.Builder(Transp_TransApp.this)
						.setTitle("#TheBusIsLeaving Reminder")
						.setView(minutes)
						.setMessage("How many minutes before this bus will leave do you want to be notified?")
						.setNegativeButton(android.R.string.no, null)
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								int add_min= 0;
								if(minutes.getText().toString().equals("")){
									add_min = 0;
								}else {
									 add_min = Integer.parseInt(minutes.getText().toString());
								}
								int add_sec = 0;
								Log.d("hello", TimeStamp);

								Calendar c = Calendar.getInstance();
								Intent intent = new Intent(getApplicationContext(), Transp_Notification_Receiver.class);
								int UniqueRequestCode = App_Tools.randomInt(100, 100000);
								PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), UniqueRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
								AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
								//  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
								long finalTimeInMillis = finalAlertDate.getTime() - add_min * 1000 * 60 - add_sec * 1000;
								alarmManager.set(AlarmManager.RTC_WAKEUP, finalTimeInMillis, pendingIntent);

								long secondsLeft = (finalTimeInMillis - c.getTimeInMillis())/1000;
								String timeleftString =  App_Tools.ConvertSecondToHHMMString((int) secondsLeft);

								showToast("You will be reminded in "+ timeleftString);
							}
						}).create().show();

				return true;
			}
		});

	}
	 void showToast(String s){
		 Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
	public class AllContactListViewClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			// TODO Auto-generated method stub
			int Index = ((Transp_ItemObject) arg0.getItemAtPosition(arg2))
					.getIndex();
			Intent i = new Intent(getApplicationContext(),
					Transp_ElementDetailListView.class);
			i.putExtra("index", Index);
			Log.d("hello", "Position Clicked is " + arg2);
			Log.d("hello", "Item Clicked is " + Index);
			startActivity(i);
		}

	}

	private ArrayList<Transp_ItemObject> MakeArrayList() {

		Calendar calendar1 = Calendar.getInstance();
		 formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");
		String currentDate = formatter1.format(calendar1.getTime());
		String datadb;
		long now = calendar1.getTimeInMillis();

		ArrayList<Transp_ItemObject> TempItemArray = new ArrayList<Transp_ItemObject>();
		for (int i = 0; i < Transp_XMLParserClass.Res2_Array.size(); i++) {
			//remove outdated app_transp even offline
			datadb = Transp_XMLParserClass.Res2_Array.get(i);
			//\Log.d("hello", "time is datab: "+(datadb)+"current time"+currentDate +"size is "+String.valueOf(Transp_XMLParserClass.Res2_Array.size())+" i is: "+String.valueOf(i));

			if(CheckIfFirstBeforeLastDates(datadb, currentDate)) {
			//	Log.d("hello", "add this");
				Transp_ItemObject CIO = new Transp_ItemObject();
				CIO.setName(Transp_XMLParserClass.RouteArray.get(i));
				CIO.setBottomText(Transp_XMLParserClass.TimeArray.get(i));
				CIO.setDayText(Transp_XMLParserClass.Day_Array.get(i));
				CIO.setTimeStamp(datadb);
				CIO.setCohort(Transp_XMLParserClass.Cohort_Array.get(i));
				CIO.setIndex(i);
				TempItemArray.add(CIO);
			}
		}

		Collections.sort(TempItemArray, new Comparator<Transp_ItemObject>() {
			@Override
			public int compare(Transp_ItemObject lhs, Transp_ItemObject rhs) {
				try {
					return formatter1.parse(lhs.getTimeStamp()).compareTo(formatter1.parse(rhs.getTimeStamp())); // compare scores
				} catch (ParseException e) {
					e.printStackTrace();
				};

				return 0;
			}
		});
		return TempItemArray;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.UpdateContactsMain:

				if (isNetworkAvailable()) {
					Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_SHORT).show();
					Transp_GetDataAsyncTask getDataTask = new Transp_GetDataAsyncTask();
					getDataTask.execute(this);
				} else {
					Toast.makeText(getApplicationContext(), "check Internet Connection", Toast.LENGTH_SHORT).show();
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

	public class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
								  int arg3) {
			// TODO Auto-generated method stub
			myAdapter.getFilter().filter(arg0.toString());
		}

	}


}
