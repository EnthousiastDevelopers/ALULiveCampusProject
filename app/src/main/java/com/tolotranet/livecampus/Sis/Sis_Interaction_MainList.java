package com.tolotranet.livecampus.Sis;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.App.AppSelect_Parent;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sis.Interact.Interact_MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Sis_Interaction_MainList extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Sis_ItemObject> ContactItemArray;
    Sis_Interact_MySwipeDeck_BaseAdapter myAdapter;
    EditText SearchET;
    ListView lv;
    AppSelect_Parent AppHelper = new AppSelect_Parent();
    private SwipeDeck cardStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_interact_main_swipedeck);

        setupFirebaseAnalytics();

//		Intent i = getIntent();
//		MyId = i.getIntExtra("myId", 0);

        Log.d("hello", "Starting tolotra");
        ContactItemArray = MakeArrayList();
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        myAdapter = new Sis_Interact_MySwipeDeck_BaseAdapter(getApplicationContext(),
                ContactItemArray);
        cardStack.setAdapter(myAdapter);

        setupCardStackSupport();

    }

    private void setupCardStackSupport() {

        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + positionInAdapter);

            }

            @Override
            public void cardSwipedRight(long positionInAdapter) {

                String recipientEmail = ((Sis_ItemObject) myAdapter.getItem((int) positionInAdapter)).getEmail();
                Log.i("MainActivity", "card was swiped right, position in adapter: " + positionInAdapter);
                Intent i = new Intent(Sis_Interaction_MainList.this, Interact_MainActivity.class);
                i.putExtra("recipient", recipientEmail);
                startActivity(i);
            }
        });
        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);
            }
        });
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(180);
            }
        });

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testData.add("a sample string.");
                //adapter.notifyDataSetChanged();
            }
        });

    } //end setup cardstack support

    private void setupFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

    }

    private ArrayList<Sis_ItemObject> MakeArrayList() {
        ArrayList<Sis_ItemObject> TempItemArray = new ArrayList<Sis_ItemObject>();
        String nullTag = "Update your";

        for (int i = 1; i < Sis_XMLParserClass.q1.size(); i++) {
            Sis_ItemObject CIO = new Sis_ItemObject();
            if (!Sis_XMLParserClass.q5.get(i).equals("")) {

                CIO.setName(Sis_XMLParserClass.q5.get(i));
                CIO.setEmail(Sis_XMLParserClass.q2.get(i));
                if ((Sis_XMLParserClass.q7.get(i).startsWith(nullTag))) {
                    CIO.setBottomText("");
                } else {
                    CIO.setBottomText(Sis_XMLParserClass.q7.get(i));
                }
                ;
                CIO.setIndex(i);
                CIO.setUserId(Integer.parseInt(Sis_XMLParserClass.q1.get(i)));
                TempItemArray.add(CIO);
            }
        }

        //shuffling arraylist
        long seed = System.nanoTime();
        Collections.shuffle(TempItemArray, new Random(seed));

//		Collections.sort(TempItemArray, new Comparator<Sis_ItemObject>() {
//			@Override
//			public int compare(Sis_ItemObject lhs, Sis_ItemObject rhs) {
//				return lhs.getName().compareTo(rhs.getName());
//			}
//		});
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
                    Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
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
