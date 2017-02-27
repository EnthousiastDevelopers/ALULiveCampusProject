package com.tolotranet.livecampus;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Bubble_MainList extends AppCompatActivity {

	private FirebaseAnalytics mFirebaseAnalytics;
	ArrayList<Bubble_ItemObject> ContactItemArray;
	Bubble_MyCustomBaseAdapter myAdapter;
	EditText SearchET;
	ListView lv;
	int MyId = 999999;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq_schedule);

		setTitle("FAQ");

//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
		mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
		mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

		Log.d("hello", "Starting tolotra");
		ContactItemArray = MakeArrayList();
		lv = (ListView) findViewById(R.id.Contacts_list_view);
		SearchET = (EditText) findViewById(R.id.SearchET);

		myAdapter = new Bubble_MyCustomBaseAdapter(getApplicationContext(),
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
			int Index = ((Bubble_ItemObject) arg0.getItemAtPosition(arg2))
					.getIndex();
			int ThisId = ((Bubble_ItemObject) arg0.getItemAtPosition(arg2))
					.getUserId();

			String name = ((Bubble_ItemObject) arg0.getItemAtPosition(arg2))
					.getName();
			String btmtext = ((Bubble_ItemObject) arg0.getItemAtPosition(arg2))
					.getBottomText();
			//open editor profile if the person clicked is the current user
			Bundle params2 = new Bundle();
			params2.putString("faq selected", name);
			params2.putString("bottom text ", btmtext);
			mFirebaseAnalytics.logEvent("faq", params2);


			if (MyId == (ThisId)){
				Intent iw = new Intent(getApplicationContext(),
						Bubble_DetailListViewOwner.class);
				iw.putExtra("index", Index);
				iw.putExtra("myId", MyId);

				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(iw);
			}else{
				Intent i = new Intent(getApplicationContext(),
						Bubble_DetailListView.class);
				i.putExtra("index", Index);
				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(i);
			}
		}

	}

	private ArrayList<Bubble_ItemObject> MakeArrayList() {
		ArrayList<Bubble_ItemObject> TempItemArray = new ArrayList<Bubble_ItemObject>();
		String nullTag = "Update your";

		for (int i = 0; i < Bubble_XMLParserClass.q1.size(); i++) {
			Bubble_ItemObject CIO = new Bubble_ItemObject();

			if (!Bubble_XMLParserClass.q2.get(i).equals("")) {

				CIO.setName(Bubble_XMLParserClass.q2.get(i));
				if ((Bubble_XMLParserClass.q3.get(i).startsWith(nullTag))) {
					CIO.setBottomText("");
				} else {
					CIO.setBottomText(Bubble_XMLParserClass.q3.get(i));
				}
				;
				if (Bubble_XMLParserClass.q7.get(i).equals("")) { //because the votes on the database or >0 or empty, we want to show values
					CIO.setVotes(0);
				}else{
					CIO.setVotes(Double.parseDouble(Bubble_XMLParserClass.q7.get(i)));
				}
				if (Bubble_XMLParserClass.q6.get(i).equals("")) { //because the comments on the database or >0 or empty, we want to show values
					CIO.setComments(0);
				} else {
					CIO.setComments(Integer.parseInt(Bubble_XMLParserClass.q6.get(i)));
				}
				CIO.setRightText(Bubble_XMLParserClass.q5.get(i)); //because q9 is the category of the Faq, breakfast, lunch or dinner
				CIO.setIndex(i);
				CIO.setImgID(this.getResources().getIdentifier("app_bubblemarket", "drawable", this.getPackageName()));
//				CIO.setUserId(Integer.parseInt(Bubble_XMLParserClass.q1.get(i)));
				TempItemArray.add(CIO);
			}
		}

//sorting
		Collections.sort(TempItemArray, new Comparator<Bubble_ItemObject>() {
			@Override
			public int compare(Bubble_ItemObject lhs, Bubble_ItemObject rhs) {
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
				Bubble_GetDataAsyncTask getDataTask = new Bubble_GetDataAsyncTask();
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
