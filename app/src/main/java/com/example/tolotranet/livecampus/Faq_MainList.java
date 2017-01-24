package com.example.tolotranet.livecampus;

import android.app.Activity;
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

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Faq_MainList extends Activity {

	private FirebaseAnalytics mFirebaseAnalytics;
	ArrayList<Faq_ItemObject> ContactItemArray;
	Faq_MyCustomBaseAdapter myAdapter;
	EditText SearchET;
	ListView lv;
	int MyId = 999999;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq_schedule);


		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
		mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
		mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

		Log.d("hello", "Starting tolotra");
		ContactItemArray = MakeArrayList();
		lv = (ListView) findViewById(R.id.Contacts_list_view);
		SearchET = (EditText) findViewById(R.id.SearchET);

		myAdapter = new Faq_MyCustomBaseAdapter(getApplicationContext(),
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
			int Index = ((Faq_ItemObject) arg0.getItemAtPosition(arg2))
					.getIndex();
			int ThisId = ((Faq_ItemObject) arg0.getItemAtPosition(arg2))
					.getUserId();

			String name = ((Faq_ItemObject) arg0.getItemAtPosition(arg2))
					.getName();
			String btmtext = ((Faq_ItemObject) arg0.getItemAtPosition(arg2))
					.getBottomText();
			//open editor profile if the person clicked is the current user
			Bundle params2 = new Bundle();
			params2.putString("faq selected", name);
			params2.putString("bottom text ", btmtext);
			mFirebaseAnalytics.logEvent("faq", params2);


			if (MyId == (ThisId)){
				Intent iw = new Intent(getApplicationContext(),
						Faq_DetailListViewOwner.class);
				iw.putExtra("index", Index);
				iw.putExtra("myId", MyId);

				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(iw);
			}else{
				Intent i = new Intent(getApplicationContext(),
						Faq_DetailListView.class);
				i.putExtra("index", Index);
				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(i);
			}
		}

	}

	private ArrayList<Faq_ItemObject> MakeArrayList() {
		ArrayList<Faq_ItemObject> TempItemArray = new ArrayList<Faq_ItemObject>();
		String nullTag = "Update your";

		for (int i = 0; i < Faq_XMLParserClass.q1.size(); i++) {
			Faq_ItemObject CIO = new Faq_ItemObject();
			if(!Faq_XMLParserClass.q2.get(i).equals("")){

				CIO.setName(Faq_XMLParserClass.q2.get(i));
				if((Faq_XMLParserClass.q5.get(i).startsWith(nullTag))){
					CIO.setBottomText("");
				}else{
					CIO.setBottomText(Faq_XMLParserClass.q5.get(i));
				};
				CIO.setIndex(i);
				CIO.setUserId(Integer.parseInt(Faq_XMLParserClass.q1.get(i)));
				TempItemArray.add(CIO);
			}
		}

//sorting
		Collections.sort(TempItemArray, new Comparator<Faq_ItemObject>() {
			@Override
			public int compare(Faq_ItemObject lhs, Faq_ItemObject rhs) {
				return lhs.getName().compareTo(rhs.getName());
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
				Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_LONG).show();
				Faq_GetDataAsyncTask getDataTask = new Faq_GetDataAsyncTask();
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
