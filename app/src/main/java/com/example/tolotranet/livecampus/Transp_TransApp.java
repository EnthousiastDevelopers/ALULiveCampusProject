package com.example.tolotranet.livecampus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Transp_TransApp extends Activity {

	ArrayList<Transp_ItemObject> ContactItemArray;
	Transp_MyCustomBaseAdapter myAdapter;
	EditText SearchET;
	ListView lv;
	ProgressDialog mProgress;

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

		MyTextWatcher mytextwatcher = new MyTextWatcher();
		SearchET.addTextChangedListener(mytextwatcher);
		lv.setOnItemClickListener(new AllContactListViewClickListener());

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
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");
		String currentDate = formatter1.format(calendar1.getTime());
		String datadb;

		ArrayList<Transp_ItemObject> TempItemArray = new ArrayList<Transp_ItemObject>();
		for (int i = 0; i < Transp_XMLParserClass.RouteArray.size(); i++) {
			//remove outdated app_transp even offline
			datadb = Transp_XMLParserClass.Res2_Array.get(i);
			//Log.d("hello", "time is datab: "+(datadb)+"current time"+currentDate );

			if(CheckDates(datadb, currentDate)) {
				Transp_ItemObject CIO = new Transp_ItemObject();
				CIO.setName(Transp_XMLParserClass.RouteArray.get(i));
				CIO.setBottomText(Transp_XMLParserClass.TimeArray.get(i));
				CIO.setDayText(Transp_XMLParserClass.Day_Array.get(i));
				CIO.setIndex(i);
				TempItemArray.add(CIO);
			}
		}
		return TempItemArray;
	}
	public static boolean CheckDates(String startDate, String endDate) {

		SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yy h'h'mm");
		SimpleDateFormat dfDate2 = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");

		Date strDate = null;
		try {
			strDate = dfDate.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		boolean b = false;
		if (System.currentTimeMillis() > strDate.getTime()) {
		//Log.d("hello","future");
		}

		try {
			if (dfDate.parse(startDate).before(dfDate2.parse(endDate))) {
				//Log.d("hello", "time is datab: "+(startDate)+"is before current time"+endDate );
				b = false;  // If start date is before end date.
			} else if (dfDate.parse(startDate).equals(dfDate2.parse(endDate))) {
				b = true;  // If two dates are equal.
			} else {
				b = true; // If start date is after the end date.
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return b;
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
