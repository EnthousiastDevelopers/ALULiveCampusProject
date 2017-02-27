package com.tolotranet.livecampus;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Lead_MainList extends AppCompatActivity {

	private FirebaseAnalytics mFirebaseAnalytics;
	ArrayList<Lead_ItemObject> ContactItemArray;
	Lead_MyCustomBaseAdapter myAdapter;
	com.github.clans.fab.FloatingActionButton fab_refresh;
	com.github.clans.fab.FloatingActionButton fab_add;
	EditText SearchET;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lead_schedule);



		//setTitle("LEADERBOARD");
//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);


		fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_refresh);

		fab_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isNetworkAvailable()){
					Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
				}else {
					fab_refresh.startAnimation(AnimationUtils.loadAnimation(Lead_MainList.this, R.anim.rotation));
					Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
					Lead_GetDataAsyncTask getDataTask = new Lead_GetDataAsyncTask();
					getDataTask.execute(Lead_MainList.this);
				}
			}
		});

		TextView scoretv = (TextView) findViewById(R.id.scoreTV);
		TextView ranktv = (TextView) findViewById(R.id.rankTV);

		int Myindex = Lead_XMLParserClass.q2.indexOf(Sign_User_Object.Email); //because q2 is the author which is the email of the balance ownder, the index of object where it contains the myemail from db is equal to data from server or local file
		Sign_User_Object.Rank = Lead_XMLParserClass.q8.get(Myindex); // because q8 is the rank number
		Sign_User_Object.Score = Lead_XMLParserClass.q6.get(Myindex); // because q6 is the score number



		scoretv.setText(Sign_User_Object.Score);
		ranktv.setText(Sign_User_Object.Rank);


		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
		mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
		mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

		Log.d("hello", "Starting tolotra");
		ContactItemArray = MakeArrayList();
		lv = (ListView) findViewById(R.id.Contacts_list_view);
		SearchET = (EditText) findViewById(R.id.SearchET);

		myAdapter = new Lead_MyCustomBaseAdapter(getApplicationContext(),
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
			int Index = ((Lead_ItemObject) arg0.getItemAtPosition(arg2))
					.getIndex();
			int ThisId = ((Lead_ItemObject) arg0.getItemAtPosition(arg2))
					.getUserId();

			String name = ((Lead_ItemObject) arg0.getItemAtPosition(arg2))
					.getName();
			String btmtext = ((Lead_ItemObject) arg0.getItemAtPosition(arg2))
					.getBottomText();
			//open editor profile if the person clicked is the current user
			Bundle params2 = new Bundle();
			params2.putString("faq selected", name);
			params2.putString("bottom text ", btmtext);
			mFirebaseAnalytics.logEvent("faq", params2);


			if (Sign_User_Object.Id == (ThisId)){
				Intent iw = new Intent(getApplicationContext(),
						Lead_DetailListView.class);
				iw.putExtra("index", Index);

				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(iw);
			}else{
				Intent i = new Intent(getApplicationContext(),
						Lead_DetailListView.class);
				i.putExtra("index", Index);
				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(i);
			}
		}

	}

	private ArrayList<Lead_ItemObject> MakeArrayList() {
		ArrayList<Lead_ItemObject> TempItemArray = new ArrayList<Lead_ItemObject>();
		String nullTag = "Update your";

		for (int i = 0; i < Lead_XMLParserClass.q1.size(); i++) {
			Lead_ItemObject CIO = new Lead_ItemObject();
			if(!Lead_XMLParserClass.q2.get(i).equals("")){

				CIO.setName(Lead_XMLParserClass.q4.get(i));
				if((Lead_XMLParserClass.q5.get(i).startsWith(nullTag))){
					CIO.setScore(0);
				}else{
					CIO.setScore(Integer.parseInt(Lead_XMLParserClass.q6.get(i)));
				};
				CIO.setBottomText(Lead_XMLParserClass.q5.get(i));
				CIO.setUserId(Integer.parseInt(Lead_XMLParserClass.q1.get(i)));
				TempItemArray.add(CIO);
			}
		}

//sorting
		Collections.sort(TempItemArray, new Comparator<Lead_ItemObject>() {
			@Override
			public int compare(Lead_ItemObject lhs, Lead_ItemObject rhs) {
				return ((Integer) rhs.getScore()).compareTo( lhs.getScore()); // compare scores
			}
		});
		//adding rank
		for (int i = 0; i < TempItemArray.size(); i++) {
			TempItemArray.get(i).setIndex(i+1);
		}

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
				Lead_GetDataAsyncTask getDataTask = new Lead_GetDataAsyncTask();
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
