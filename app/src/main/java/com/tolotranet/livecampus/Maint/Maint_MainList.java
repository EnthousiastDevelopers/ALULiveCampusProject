package com.tolotranet.livecampus.Maint;


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
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sign_User_Object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Maint_MainList extends AppCompatActivity {

	private FirebaseAnalytics mFirebaseAnalytics;
	ArrayList<Maint_ItemObject> ContactItemArray;
	Maint_MyCustomBaseAdapter myAdapter;
	com.github.clans.fab.FloatingActionButton fab_refresh;
	com.github.clans.fab.FloatingActionButton fab_add;
	EditText SearchET;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.maint_schedule);



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
					fab_refresh.startAnimation(AnimationUtils.loadAnimation(Maint_MainList.this, R.anim.rotation));
					Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
					Maint_GetDataAsyncTask getDataTask = new Maint_GetDataAsyncTask();
					getDataTask.execute(Maint_MainList.this);
				}
			}
		});




		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
		mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
		mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

		Log.d("hello", "Starting tolotra");
		ContactItemArray = MakeArrayList();
		lv = (ListView) findViewById(R.id.Contacts_list_view);
		SearchET = (EditText) findViewById(R.id.SearchET);

		myAdapter = new Maint_MyCustomBaseAdapter(getApplicationContext(),
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
			int Index = ((Maint_ItemObject) arg0.getItemAtPosition(arg2))
					.getIndex();
			int ThisId = ((Maint_ItemObject) arg0.getItemAtPosition(arg2))
					.getUserId();

			String name = ((Maint_ItemObject) arg0.getItemAtPosition(arg2))
					.getName();
			String btmtext = ((Maint_ItemObject) arg0.getItemAtPosition(arg2))
					.getBottomText();
			//open editor profile if the person clicked is the current user
			Bundle params2 = new Bundle();
			params2.putString("faqselected", name);
			params2.putString("bottomtext", btmtext);
			mFirebaseAnalytics.logEvent("faq", params2);


			if (Sign_User_Object.Id == (ThisId)){
				Intent iw = new Intent(getApplicationContext(),
						Maint_DetailListViewOwner.class);
				iw.putExtra("index", Index);

				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(iw);
			}else{
				Intent i = new Intent(getApplicationContext(),
						Maint_DetailListView.class);
				i.putExtra("index", Index);
				Log.d("hello", "Position Clicked is " + arg2);
				Log.d("hello", "Item Clicked is " + Index);
				Log.d("hello", "User Clicked is " + ThisId);
				startActivity(i);
			}
		}

	}

	private ArrayList<Maint_ItemObject> MakeArrayList() {
		ArrayList<Maint_ItemObject> TempItemArray = new ArrayList<Maint_ItemObject>();
		String nullTag = "Update your";

		for (int i = 0; i < Maint_XMLParserClass.q1.size(); i++) {
			Maint_ItemObject CIO = new Maint_ItemObject();
			if(Maint_XMLParserClass.q3.get(i).equals(Sign_User_Object.Email)){ //because q3 refers to the email address of the sender of this issue, we only want to show him his issue
			if(!Maint_XMLParserClass.q2.get(i).equals("")){

				CIO.setName(Maint_XMLParserClass.q9.get(i));
				CIO.setState(Maint_XMLParserClass.q12.get(i));
				if((Maint_XMLParserClass.q11.get(i).equals(""))){  //because q11 is the number of comments, and score is a righttxtview on the list
					CIO.setCommentsCount(0);
				}else{
					CIO.setCommentsCount((Integer.parseInt(Maint_XMLParserClass.q11.get(i)))); //because this file is inherited from leaderboard, thus, setCommentsCount is used to display the number of commentsin maintenance
				};
				CIO.setBottomText(Maint_XMLParserClass.q1.get(i));
				CIO.setIndex((i)); //because this index will be refered when the user click on one item, and redirected to the detailed listview

				//CIO.setState(Integer.parseInt(Maint_XMLParserClass.q1.get(i)));
				TempItemArray.add(CIO);
			}
			}
		}

//sorting
		Collections.sort(TempItemArray, new Comparator<Maint_ItemObject>() {
			@Override
			public int compare(Maint_ItemObject lhs, Maint_ItemObject rhs) {
				return ((Integer) rhs.getCommentsCount()).compareTo( lhs.getCommentsCount()); // compare scores
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
				Maint_GetDataAsyncTask getDataTask = new Maint_GetDataAsyncTask();
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
