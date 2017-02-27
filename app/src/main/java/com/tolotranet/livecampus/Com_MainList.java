package com.tolotranet.livecampus;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Com_MainList extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Com_ItemObject> ContactItemArray;
    Com_MyCustomBaseAdapter myAdapter;
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
        setContentView(R.layout.com_schedule);

        fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_refresh);

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    fab_refresh.startAnimation(AnimationUtils.loadAnimation(Com_MainList.this, R.anim.rotation));
                    Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Com_GetDataAsyncTask getDataTask = new Com_GetDataAsyncTask();
                    getDataTask.execute(Com_MainList.this);
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
                        Com_Add.class);
                startActivity(i);
            }
        });

        ActionBar ab = getActionBar();
        category = Com_App.mu_category;
        category_img_id = Com_App.mu_category_imgID;
//		ab.setSubtitle("Mauritius");
//		ab.setTitle(category);
        setTitle(category);
        //setSubtitle("Mauritius");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        Log.d("hello", "Starting tolotra");
        // ContactItemArray = MakeArrayList(); //commented because the activity will never be called, but the functions will be used somewhere else
        lv = (ListView) findViewById(R.id.Contacts_list_view);
        SearchET = (EditText) findViewById(R.id.SearchET);

        myAdapter = new Com_MyCustomBaseAdapter(getApplicationContext(),
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
            int Index = ((Com_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisId = ((Com_ItemObject) arg0.getItemAtPosition(arg2))
                    .getUserId();

            String name = ((Com_ItemObject) arg0.getItemAtPosition(arg2))
                    .getName();
            String btmtext = ((Com_ItemObject) arg0.getItemAtPosition(arg2))
                    .getBottomText();
            //open editor profile if the person clicked is the current user
            Bundle params2 = new Bundle();
            params2.putString("faq selected", name);
            params2.putString("bottom text ", btmtext);
            mFirebaseAnalytics.logEvent("faq", params2);


            if (MyId == (ThisId)) {
                Intent iw = new Intent(getApplicationContext(),
                        Com_DetailListViewOwner.class);
                iw.putExtra("index", Index);
                iw.putExtra("myId", MyId);

                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                startActivity(iw);
            } else {
                Intent i = new Intent(getApplicationContext(),
                        Com_DetailListView.class);
                i.putExtra("index", Index);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                startActivity(i);
            }
        }

    }

    public ArrayList<Com_ItemObject> MakeArrayList(String ObjectID) {
        ArrayList<Com_ItemObject> TempItemArray = new ArrayList<Com_ItemObject>();
        String nullTag = "Update your";


        if(Com_XMLParserClass.q1 == null) {

            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
            File myfile = new File(Dir, "Com.txt");
            if (myfile.exists()) {
                Log.d("hello", "File Exists");
                //FileExists = true;
                try {
                    new Sis_XMLParserClass();

                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }else {

                Toast.makeText( getApplicationContext(),"Check your memory card or your internet", Toast.LENGTH_LONG).show();
                Com_ItemObject CIO = new Com_ItemObject();
                CIO.setBottomText("");
                CIO.setIndex(0);
                CIO.setAuthor("");
                CIO.setComments(0);
                CIO.setImgID(0);
                CIO.setName("");
                CIO.setObject("");
                CIO.setParent("");
                CIO.setUserId(0);
                CIO.setVotes(0);
                TempItemArray.add(CIO);
                return TempItemArray;
            }
        }
        for (int i = 0; i < Com_XMLParserClass.q1.size(); i++) {
            Log.d("hello", "ObjectID is: " + ObjectID + " and category in db is: " + Com_XMLParserClass.q2.get(i));
            if (Com_XMLParserClass.q2.get(i).equals(ObjectID)) { //filter category, but from the same table
                Com_ItemObject CIO = new Com_ItemObject();

                if (!Com_XMLParserClass.q2.get(i).equals("")) {

                    CIO.setName(Com_XMLParserClass.q13.get(i));
                    if ((Com_XMLParserClass.q5.get(i).startsWith(nullTag))) {
                        CIO.setBottomText("");
                    } else {
                        CIO.setBottomText(Com_XMLParserClass.q5.get(i));
                    }
                    ;
                    if (Com_XMLParserClass.q7.get(i).equals("")) { //because the votes on the database or >0 or empty, we want to show values
                        CIO.setVotes(0);
                    } else {
                        CIO.setVotes(Integer.parseInt(Com_XMLParserClass.q7.get(i)));
                    }
                    if (Com_XMLParserClass.q6.get(i).equals("")) { //because the comments on the database or >0 or empty, we want to show values
                        CIO.setComments(0);
                    } else {
                        CIO.setComments(Integer.parseInt(Com_XMLParserClass.q6.get(i)));
                    }
                    //VoterList: we are  going to find out where this comment has been voted by the user
                    CIO.setVotes(0); //because if the json parsing failed, the vote will be set to zero, else, it will be erased by either 1 or -1
                    try { //because is the json parsing might be incorrect

                        JSONArray arrayJson = new JSONArray(Com_XMLParserClass.q14.get(i)); //because the json received start with "[" end with "]", its an array of objects with propriety

                        outerloop:
                        for (int n = 0; n < arrayJson.length(); n++) { //because the length is the number of unique user who voted this
                            JSONObject theobj = arrayJson.getJSONObject(n); //because it
                            if (theobj.getString("author").equals(Sign_User_Object.Email)) {
                                int scoreGiven = theobj.getInt("score");
                                CIO.setVotes(scoreGiven); //because voteState is the switch we need to define wether the user has voted up, voted down or unvoted
                                Log.d("hello", String.valueOf(scoreGiven) + Com_XMLParserClass.q14.get(i));
                                break outerloop; //because we found the user and its score given
                            }
                            Log.d("hello", "your email not found in json");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //end VoterList
                    CIO.setIndex(i);
                    CIO.setAuthor(Com_XMLParserClass.q13.get(i)); //because it is needed to send the score to
                    CIO.setObject(Com_XMLParserClass.q12.get(i)); // because it is needed to send the vote to
                    CIO.setImgID(1);
//				CIO.setUserId(Integer.parseInt(Com_XMLParserClass.q1.get(i)));
                    TempItemArray.add(CIO);
                }
            }
        }
//sorting
        Collections.sort(TempItemArray, new Comparator<Com_ItemObject>() {
            @Override
            public int compare(Com_ItemObject lhs, Com_ItemObject rhs) {
                return ((Integer) rhs.getVotes()).compareTo(lhs.getVotes()); // compare scores
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
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(),
                Com_App.class);
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsMain:

                if (isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_LONG).show();
                    Com_GetDataAsyncTask getDataTask = new Com_GetDataAsyncTask();
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
