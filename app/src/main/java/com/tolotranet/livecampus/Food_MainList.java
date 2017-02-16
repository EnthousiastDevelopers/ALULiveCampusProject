package com.tolotranet.livecampus;


import android.app.ActionBar;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Food_MainList extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Food_ItemObject> ContactItemArray;
    Food_MyCustomBaseAdapter myAdapter;
    EditText SearchET;
    ListView lv;
    String category;
    com.github.clans.fab.FloatingActionButton fab_refresh;
    com.github.clans.fab.FloatingActionButton fab_add;
    int MyId = 999999;
    int category_img_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_schedule);

        fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_refresh);

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    fab_refresh.startAnimation(AnimationUtils.loadAnimation(Food_MainList.this, R.anim.rotation));
                    Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Food_GetDataAsyncTask getDataTask = new Food_GetDataAsyncTask();
                    getDataTask.execute(Food_MainList.this);
                }
            }
        });
        fab_add = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    //	Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                }
                Intent i = new Intent(getApplicationContext(),
                        Food_Add.class);
                startActivity(i);
            }
        });

        ActionBar ab = getActionBar();
        category = Food_App.mu_category;
        category_img_id = Food_App.mu_category_imgID;
        category = "same";
//		ab.setSubtitle("Mauritius");
//		ab.setTitle(category);
        setTitle(category);
        //setSubtitle("Mauritius");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        Log.d("hello", "Starting tolotra");
        ContactItemArray = MakeArrayList();
        lv = (ListView) findViewById(R.id.Contacts_list_view);
        SearchET = (EditText) findViewById(R.id.SearchET);

        myAdapter = new Food_MyCustomBaseAdapter(getApplicationContext(),
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
            int Index = ((Food_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisId = ((Food_ItemObject) arg0.getItemAtPosition(arg2))
                    .getUserId();

            String name = ((Food_ItemObject) arg0.getItemAtPosition(arg2))
                    .getName();
            String btmtext = ((Food_ItemObject) arg0.getItemAtPosition(arg2))
                    .getBottomText();
            //open editor profile if the person clicked is the current user
            Bundle params2 = new Bundle();
            params2.putString("faq selected", name);
            params2.putString("bottom text ", btmtext);
            mFirebaseAnalytics.logEvent("faq", params2);


            if (MyId == (ThisId)) {
                Intent iw = new Intent(getApplicationContext(),
                        Food_DetailListViewOwner.class);
                iw.putExtra("index", Index);
                iw.putExtra("myId", MyId);

                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                startActivity(iw);
            } else {
                Intent i = new Intent(getApplicationContext(),
                        Food_DetailListView.class);
                i.putExtra("index", Index);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                startActivity(i);
            }
        }

    }

    private ArrayList<Food_ItemObject> MakeArrayList() {
        ArrayList<Food_ItemObject> TempItemArray = new ArrayList<Food_ItemObject>();
        String nullTag = "Update your";

        for (int i = 0; i < Food_XMLParserClass.q1.size(); i++) {
            Log.d("hello", "category is: " + category + " and category in db is: " + Food_XMLParserClass.q8.get(i));
            if (Food_XMLParserClass.q8.get(i).equals("same")) { //filter category, but from the same table
                Food_ItemObject CIO = new Food_ItemObject();

                if (!Food_XMLParserClass.q2.get(i).equals("")) {

                    CIO.setName(Food_XMLParserClass.q2.get(i));
                    if ((Food_XMLParserClass.q3.get(i).startsWith(nullTag))) {
                        CIO.setBottomText("");
                    } else {
                        CIO.setBottomText(Food_XMLParserClass.q3.get(i));
                    }
                    ;
                    if (Food_XMLParserClass.q7.get(i).equals("")) { //because the votes on the database or >0 or empty, we want to show values
                        CIO.setVotes(0);
                    }else{
                        CIO.setVotes(Integer.parseInt(Food_XMLParserClass.q7.get(i)));
                    }
                    if (Food_XMLParserClass.q6.get(i).equals("")) { //because the comments on the database or >0 or empty, we want to show values
                        CIO.setComments(0);
                    } else {
                        CIO.setComments(Integer.parseInt(Food_XMLParserClass.q6.get(i)));
                    }
                    CIO.setRightText(Food_XMLParserClass.q9.get(i)); //because q9 is the category of the food, breakfast, lunch or dinner
                    CIO.setIndex(i);
                    CIO.setImgID(this.getResources().getIdentifier("app_icon_food", "drawable", this.getPackageName()));
//				CIO.setUserId(Integer.parseInt(Food_XMLParserClass.q1.get(i)));
                    TempItemArray.add(CIO);
                }
            }
        }
//sorting
        Collections.sort(TempItemArray, new Comparator<Food_ItemObject>() {
            @Override
            public int compare(Food_ItemObject lhs, Food_ItemObject rhs) {
                return ((Integer) rhs.getIndex()).compareTo( lhs.getIndex()); // compare index because its the food and must be ordered by weekday
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
//commented because we don;t want to attach any event on back pressed
//    @Override
//    public void onBackPressed() {
//
//        Intent i = new Intent(getApplicationContext(),
//                Food_App.class);
//        startActivity(i);
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsMain:

                if (isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_LONG).show();
                    Food_GetDataAsyncTask getDataTask = new Food_GetDataAsyncTask();
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
