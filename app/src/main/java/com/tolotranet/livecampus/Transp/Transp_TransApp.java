package com.tolotranet.livecampus.Transp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import com.tolotranet.livecampus.App.App_Tools;
import com.tolotranet.livecampus.R;

import static com.tolotranet.livecampus.App.App_Tools.CheckIfFirstBeforeLastDates;

public class Transp_TransApp extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static String SpeechAction;
    public static String Origin;
    ArrayList<Transp_ItemObject> ContactItemArray;
    Transp_MyCustomBaseAdapter myAdapter;
    EditText SearchET;
    ListView lv;
    ProgressDialog mProgress;
    com.github.clans.fab.FloatingActionButton fab_refresh;
    com.github.clans.fab.FloatingActionButton fab_add;
    SimpleDateFormat formatter1;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");

        setContentView(R.layout.transp_schedule);
        formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");

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
                if (!isNetworkAvailable()) {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    fab_refresh.startAnimation(AnimationUtils.loadAnimation(Transp_TransApp.this, R.anim.rotation));
                    Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Transp_GetDataAsyncTask getDataTask = new Transp_GetDataAsyncTask(Transp_TransApp.this, "normal");
                    getDataTask.execute(Transp_TransApp.this);
                }
            }
        });

        MyTextWatcher mytextwatcher = new MyTextWatcher();
        SearchET.addTextChangedListener(mytextwatcher);
        lv.setOnItemClickListener(new AllContactListViewClickListener());



        String speech = SpeechAction;
            //this part is called only when the activity was called by the speech action
        if(speech != null && speech.equals("New Bus")) {
            AnswerVocalNextBus();
          } //end of speech



        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final EditText minutes = new EditText(getApplicationContext());
                minutes.setWidth(60);
                minutes.setHint("Eg: 5 minutes");
                final String TimeStamp = ((Transp_ItemObject) arg0.getItemAtPosition(arg2)).getTimeStamp();
                final String TripTxt = ((Transp_ItemObject) arg0.getItemAtPosition(arg2)).getName();
                Date alertDate = null;
                try {
                    alertDate = formatter1.parse(TimeStamp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                minutes.setInputType(InputType.TYPE_CLASS_NUMBER);
                final Date finalAlertDate = alertDate;
                new AlertDialog.Builder(Transp_TransApp.this)
                        .setTitle("Bus Reminder")
                        .setView(minutes)
                        .setMessage("Enter number of minutes before the departure:")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int add_min = 0;
                                if (minutes.getText().toString().equals("")) {
                                    add_min = 0;
                                } else {
                                    add_min = Integer.parseInt(minutes.getText().toString());
                                }
                                int add_sec = 0;
                                Log.d("hello", TimeStamp);

                                int UniqueRequestCode = App_Tools.randomInt(100, 100000); //pending intent has to have a unique request <code</code>

                                Intent intent = new Intent(getApplicationContext(), Transp_Notification_Receiver.class);

                                intent.putExtra("trip", TripTxt); //to show in the notification builder
                                intent.putExtra("timeleft", String.valueOf(add_min)); //to show in the notification builder

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), UniqueRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                                //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                                long finalTimeInMillis = finalAlertDate.getTime() - add_min * 1000 * 60 - add_sec * 1000;
                                alarmManager.set(AlarmManager.RTC_WAKEUP, finalTimeInMillis, pendingIntent);


                                Calendar c = Calendar.getInstance();
                                long secondsLeft = (finalTimeInMillis - c.getTimeInMillis()) / 1000;
                                String timeleftString = App_Tools.ConvertSecondToHHMMString((int) secondsLeft);

                                showToast("You will be reminded in " + timeleftString);
                            }
                        }).create().show();

                return true;
            }
        });

    }

    private void AnswerVocalNextBus() {
        String route = "";
        String day = "";
        String time = "";
        String timestamp = "";
        String cohort = "";
        Date newTimestamp = null;
        //getting the first but only the list having the index 0, because we asked when is the next bus
        timestamp = ((Transp_ItemObject) lv.getAdapter().getItem(0)).getTimeStamp();

        try {
            route = ((Transp_ItemObject) lv.getAdapter().getItem(0)).getName();
            cohort = ((Transp_ItemObject) lv.getAdapter().getItem(0)).getCohort();

            Log.d("hello tts", route);
        } catch (Exception e) {

        }

        try {
            newTimestamp = formatter1.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        Log.d("Hello", String.valueOf(newTimestamp.getDay()) + " day departure | day now" + c.getTime().getDay());

        //choosing which vocabulary the speech will say depending on the date when the next bus is leaving
        int diff = newTimestamp.getDay() - c.getTime().getDay();
        switch (diff) {
            case 0:
                day = "Today";
                break;
            case 1:
                day = "Tomorrow";
                break;
            case -1:
                day = "yesterday";
                break;
            default:
                day = "in " + String.valueOf(diff) + " days";
        }
        //formatting the time to speak
        SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm aa");
        time = formatter2.format(newTimestamp);

        //replacing all the acronym to hearable words
        route = route.replace("-", "to");
        cohort = cohort.replace("(", " ")
                .replace(")", " ")
                .replace("ENG", " Electrical Engineering ")
                .replace("BM", " Business Management ")
                .replace("CS", " Computer Science ")
                .replace("-", " and ")
                .replace("/", " and ");

        //prompt to speak
        final String finalRoute = "The next bus is leaving " + day +
                " at " + time +
                " from " + route +
                ". It is for the " + cohort+" trip";

        //final speech in action
        tts = new TextToSpeech(Transp_TransApp.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        tts.speak(finalRoute, TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
        SpeechAction = "";

    }


    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Log.d("hello", "tts init success");
        } else {
            Log.d("hello", "tts init failed");
        }
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

        String currentDate = formatter1.format(calendar1.getTime());
        String datadb;
        long now = calendar1.getTimeInMillis();

        ArrayList<Transp_ItemObject> TempItemArray = new ArrayList<Transp_ItemObject>();
        for (int i = 0; i < Transp_XMLParserClass.TimeStamp.size(); i++) {
            //remove outdated app_transp even offline
            datadb = Transp_XMLParserClass.TimeStamp.get(i);
            //\Log.d("hello", "time is datab: "+(datadb)+"current time"+currentDate +"size is "+String.valueOf(Transp_XMLParserClass.TimeStamp.size())+" i is: "+String.valueOf(i));

            if (CheckIfFirstBeforeLastDates(datadb, currentDate)) {
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
                }
                ;

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
                    Transp_GetDataAsyncTask getDataTask = new Transp_GetDataAsyncTask(Transp_TransApp.this, "normal");
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
