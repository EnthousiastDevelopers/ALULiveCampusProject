package com.tolotranet.livecampus.Event;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tolotranet.livecampus.Event.CalendarList.Event_CalendarList_Main;
import com.tolotranet.livecampus.HeaderListView;
import com.tolotranet.livecampus.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import static com.tolotranet.livecampus.App.App_Tools.CheckIfFirstBeforeLastDates;

public class Event_MainList extends AppCompatActivity {
    ArrayList<ArrayList<Event_ItemObject>> ContactArrayGroup = new ArrayList<ArrayList<Event_ItemObject>>(2);
    ArrayList<Event_ItemObject> ContactItemArray;
    ArrayList<Event_ItemObject> ContactSectionArray;
    Event_MyCustomBaseAdapter myAdapter;
    ProgressDialog mProgress;
    EditText SearchET;
    com.github.clans.fab.FloatingActionButton fab_refresh;
    com.github.clans.fab.FloatingActionButton fab_add;
    HeaderListView lv;
    int MyId = 9999999; //is 99999 because this is event activity and event don't need the user to change it
    private SimpleDateFormat formatter1, formatterDateOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_schedule);

        Log.d("hello", "Starting tolotra");
        formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");
        formatterDateOnly = new SimpleDateFormat("dd/MM/yy");

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");

        ContactArrayGroup = MakeArrayItemList();

        ContactItemArray = ContactArrayGroup.get(0);
        ContactSectionArray = ContactArrayGroup.get(1);
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.mysrl);

        lv = (HeaderListView) findViewById(R.id.Contacts_list_view);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    srl.setEnabled(true);
                } else srl.setEnabled(false);
            }
        });


        SearchET = (EditText) findViewById(R.id.SearchET);


        myAdapter = new Event_MyCustomBaseAdapter(getApplicationContext(), ContactItemArray, ContactSectionArray);
        lv.setAdapter(myAdapter);
        fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_refresh);
        fab_add = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Event_MainList.this, Event_CalendarList_Main.class);
                startActivity(i);
            }
        });
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    fab_refresh.startAnimation(AnimationUtils.loadAnimation(Event_MainList.this, R.anim.rotation));
                    Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Event_GetDataAsyncTask getDataTask = new Event_GetDataAsyncTask();
                    getDataTask.execute(Event_MainList.this);
                }
            }
        });

        MyTextWatcher mytextwatcher = new MyTextWatcher();
        SearchET.addTextChangedListener(mytextwatcher);
        //lv.setOnItemClickListener(new AllContactListViewClickListener());

    }


    public class AllContactListViewClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            int Index = ((Event_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisId = ((Event_ItemObject) arg0.getItemAtPosition(arg2))
                    .getUserId();

            //open editor profile if the person clicked is the current user

            if (MyId == (ThisId)) {
                Intent iw = new Intent(getApplicationContext(),
                        Event_DetailListViewOwner.class);
                iw.putExtra("index", Index);
                iw.putExtra("myId", MyId);

                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                startActivity(iw);
            } else {
                Intent i = new Intent(getApplicationContext(),
                        Event_DetailListView.class);
                i.putExtra("index", Index);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                startActivity(i);
            }
        }

    }

    public ArrayList<ArrayList<Event_ItemObject>> MakeArrayItemList() {
        ArrayList<ArrayList<Event_ItemObject>> group = new ArrayList<ArrayList<Event_ItemObject>>(2);

        ArrayList<Event_ItemObject> TempItemArray = new ArrayList<Event_ItemObject>();
        String nullTag = "Update your";
        Calendar calendar1 = Calendar.getInstance();
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");
        final SimpleDateFormat formatterDateOnly = new SimpleDateFormat("dd/MM/yy");

        String currentDate = formatter1.format(calendar1.getTime());
        String datadb;
        long now = calendar1.getTimeInMillis();


        if (Event_XMLParserClass.q1 == null) {
            try {
                new Event_XMLParserClass();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for (int i = 0; i < Event_XMLParserClass.q1.size(); i++) {
            if (Event_XMLParserClass.q12.get(i) != null && !Event_XMLParserClass.q12.get(i).equals("")) {
                //ideally, choose end time as limit view on calendar
                datadb = Event_XMLParserClass.q12.get(i);
                Log.d("hellos", "end time for "+ String.valueOf(i));
            } else {
                //but if the end time is not specified, go for start time
                datadb = Event_XMLParserClass.q10.get(i);
            }

            //Log.d("hello", "time is datab: "+(datadb)+"current time"+currentDate +"size is "+String.valueOf(Event_XMLParserClass.q10.size())+" i is: "+String.valueOf(i));
            Log.d("hello", "date time q4 is " + String.valueOf(Event_XMLParserClass.q4.get(i)) + " i is: " + String.valueOf(i));

            //filtering out date that are passed the current Date ideally using the endtimestamp
            if (CheckIfFirstBeforeLastDates(datadb, currentDate)) {

                //Log.d("hello", "add this");
                Event_ItemObject CIO = new Event_ItemObject();
                if (!Event_XMLParserClass.q2.get(i).equals("")) {

                    CIO.setName(Event_XMLParserClass.q2.get(i));
                    CIO.setMiniText(Event_XMLParserClass.q6.get(i));
                    if ((Event_XMLParserClass.q4.get(i).startsWith(nullTag))) {
                        CIO.setBottomText("");
                    } else {
                        CIO.setBottomText(Event_XMLParserClass.q4.get(i));
                    }
                    CIO.setIndex(i);
                    CIO.setTimeStamp(datadb);
                    CIO.setUserId(0);
                    CIO.setBgColor(Event_XMLParserClass.q11.get(i));
                    CIO.setObjectID((Event_XMLParserClass.q1.get(i)));

                    TempItemArray.add(CIO);
                }
            }
        }

        Collections.sort(TempItemArray, new Comparator<Event_ItemObject>() {
            @Override
            public int compare(Event_ItemObject lhs, Event_ItemObject rhs) {
                try {
                    return formatter1.parse(lhs.getTimeStamp()).compareTo(formatter1.parse(rhs.getTimeStamp())); // compare scores
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        }); //end of section sort

        //CREATING GROUP SECTION ARRAY
        //GOAL : section number start with 0
        //GOAL : section count start with 1
        ArrayList<Event_ItemObject> TempSectionArray = new ArrayList<Event_ItemObject>();
        SimpleDateFormat dayOfWeekString = new SimpleDateFormat("EEE", Locale.US);
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd", Locale.US);

        int lastDay = -1;
        int childcount = 1;
        int allChild = 0;

        for (int i = 0; i < TempItemArray.size(); i++) {
            String datedb = TempItemArray.get(i).getTimeStamp();
            Date Date_datedb = null;
            try {
                Date_datedb = formatter1.parse(datedb);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dayofMonth = (dayOfMonthFormat.format(Date_datedb));
            int newDay = Integer.parseInt(dayofMonth);

            //if i = 0 meaning first item and first to be added to TempSectionArray,
            // put section number equal to 0 because, TempSectionArray.size(0 would return null
            int sectionNumber = 0;
            if (i != 0) { //if this is not the first item, set section to the size of the array, which minimum is 1
                sectionNumber = TempSectionArray.size();
            }

            if (lastDay != newDay || i == 0) {
                //if the day has changed to the next or if it is the first item
                //if it is not the first item but the day has changed
                if (i != 0) {
                    //setting the previous index childcount
                    TempSectionArray.get(TempSectionArray.size() - 1).setChildCount(childcount);
                    allChild += childcount;
                }

                //create new object CIO to add to TempSection Array
                Event_ItemObject CIO = new Event_ItemObject();
                CIO.setName(dayOfMonthFormat.format(Date_datedb));
                CIO.setTimeStamp(formatterDateOnly.format(Date_datedb));
                CIO.setBottomText(dayOfWeekString.format(Date_datedb));
                CIO.setType(sectionNumber);
                //adding CIO to TEmp section array
                TempSectionArray.add(CIO);
                lastDay = newDay;
                childcount = 1;
            } else {
                childcount++;
            }

            if (i == TempItemArray.size() - 1) {
                //since the last child count can either be a new day or the same as the previous section, the two else if above
                // must be run before setting the last index childcount
                TempSectionArray.get(TempSectionArray.size() - 1).setChildCount(childcount);
                allChild += childcount;
            }
            TempItemArray.get(i).setType(sectionNumber); //NOTE this modify the previously created arraylist for the section number
            Log.d("hellomarli", dayofMonth + " " +
                    String.valueOf(TempItemArray.get(i).getType()) + " " +
                    String.valueOf(TempItemArray.get(i).getBottomText()) + " " +
                    String.valueOf(childcount));
        }//end for loop on TempItemArray

        Log.d("hellomarlix I", String.valueOf(TempItemArray.size()));
        Log.d("hellomarlix S", String.valueOf(TempSectionArray.size()));
        Log.d("hellomarlix A", String.valueOf(allChild));


        for (int i = 0; i < TempSectionArray.size(); i++) {
            Log.d("hellomarlis", String.valueOf(TempSectionArray.get(i).getType()) +
                    " " + String.valueOf(TempSectionArray.get(i).getName()) +
                    " " + String.valueOf(TempSectionArray.get(i).getChildCount()));

        }
        group.add(TempItemArray);
        group.add(TempSectionArray);

        return (group);
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
                    Event_GetDataAsyncTask getDataTask = new Event_GetDataAsyncTask();
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
