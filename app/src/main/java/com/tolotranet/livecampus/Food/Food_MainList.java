package com.tolotranet.livecampus.Food;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Food_MainList extends AppCompatActivity {

    public static String SpeechAction;
    public static String Origin;
    public static String When;
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
    TextToSpeech tts;

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
                    Food_GetDataAsyncTask getDataTask = new Food_GetDataAsyncTask(Food_MainList.this, "normal");
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
        category = "Food Menu";
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

        myAdapter = new Food_MyCustomBaseAdapter(getApplicationContext(), ContactItemArray);
        lv.setAdapter(myAdapter);

        MyTextWatcher mytextwatcher = new MyTextWatcher();
        SearchET.addTextChangedListener(mytextwatcher);
        lv.setOnItemClickListener(new AllContactListViewClickListener());

        //SPEECH

        String speech = SpeechAction;

        if (Origin != null && Origin.equals("speech")) {

            int i = 0;
            if(When.equals("")){
                i = 0;
            }
            if(When.equals("tomorrow")){
                i = 1;
            }
            if(When.equals("after tomorrow")){
                i = 2;
            }
            if(When.startsWith("on")){

            }

            String day = "";
            String breakfast = "";
            String snack = "";
            String lunch = "";
            String snack2 = "";
            String dinner = "";
            Date newTimestamp = null;

            day = ((Food_ItemObject) lv.getAdapter().getItem(i)).getName();

            breakfast = ((Food_ItemObject) lv.getAdapter().getItem(i)).getBottomText1();
            snack = ((Food_ItemObject) lv.getAdapter().getItem(i)).getBottomText2();
            lunch = ((Food_ItemObject) lv.getAdapter().getItem(i)).getBottomText3();
            snack2 = ((Food_ItemObject) lv.getAdapter().getItem(0)).getBottomText4();
            dinner = ((Food_ItemObject) lv.getAdapter().getItem(0)).getBottomText5();

            switch (i) {
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
                    day = day;
            }

             String  sentenceToSpeech= "";
            if(SpeechAction.equals("all")) {
                sentenceToSpeech = day + "'s breakfast is "+breakfast+
                        ". And the snack is "+snack+
                        ". And the lunch is "+lunch+
                        ". And the afternoon snack is "+snack2+
                        ". And the dinner snack is "+dinner;
            }
            else if(SpeechAction.equals("dinner")){
                sentenceToSpeech = day + "'s dinner is "+dinner;
            }
             else if(SpeechAction.equals("lunch")){
                sentenceToSpeech = day + "'s lunch is "+lunch;
            }
             else if(SpeechAction.equals("snack")){
                sentenceToSpeech = day + "'s snack is "+snack+
                        ". And the afternoon snack is "+snack2;

            }
            else if(SpeechAction.equals("breakfast")){
                sentenceToSpeech = day + "'s breakfast is "+breakfast;
            }

            final String finalSentenceToSpeech = sentenceToSpeech;
            tts = new TextToSpeech(Food_MainList.this, new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    // TODO Auto-generated method stub
                    if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(Locale.US);
                        if (result == TextToSpeech.LANG_MISSING_DATA ||
                                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("error", "This Language is not supported");
                        } else {
                            tts.speak(finalSentenceToSpeech, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    } else
                        Log.e("error", "Initilization Failed!");
                }
            });
            SpeechAction = "";
        } //end speech


        //SPEECH
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
                    .getBottomText1();
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

        for (int i = 0; i < Food_XMLParserClass.q1.size(); i++) {
            Log.d("hello", "category is: " + category + " and category in db is: " + Food_XMLParserClass.q8.get(i));
            if (Food_XMLParserClass.q8.get(i).equals("Food Menu")) { //filter category, but from the same table
                Food_ItemObject CIO = new Food_ItemObject();

                if (!Food_XMLParserClass.q2.get(i).equals("")) {

                    CIO.setName(Food_XMLParserClass.q2.get(i)); //day
                    CIO.setBottomText1(Food_XMLParserClass.q9.get(i)); //breakfast
                    CIO.setBottomText2(Food_XMLParserClass.q10.get(i)); //snack
                    CIO.setBottomText3(Food_XMLParserClass.q11.get(i)); //lunch
                    CIO.setBottomText4(Food_XMLParserClass.q12.get(i)); //snack2
                    CIO.setBottomText5(Food_XMLParserClass.q13.get(i)); //dinner

                    if (Food_XMLParserClass.q7.get(i).equals("")) {
                        CIO.setVotes(0);
                    } else {
                        CIO.setVotes(Integer.parseInt(Food_XMLParserClass.q7.get(i)));
                    }

                    if (Food_XMLParserClass.q6.get(i).equals("")) {
                        CIO.setComments(0);
                    } else {
                        CIO.setComments(Integer.parseInt(Food_XMLParserClass.q6.get(i)));
                    }

                    CIO.setRightText(Food_XMLParserClass.q9.get(i)); //because q9 is the category of the food, breakfast, lunch or dinner

                    CIO.setIndex(i);
                    CIO.setImgID(this.getResources().getIdentifier("app_icon_food", "drawable", this.getPackageName()));
//				CIO.setState(Integer.parseInt(Food_XMLParserClass.q1.get(i)));
                    TempItemArray.add(CIO);
                }
            }
        }
//sorting
//        Collections.sort(TempItemArray, new Comparator<Food_ItemObject>() {
//            @Override
//            public int compare(Food_ItemObject lhs, Food_ItemObject rhs) {
//                return ((Integer) rhs.getIndex()).compareTo(lhs.getIndex()); // compare index because its the food and must be ordered by weekday
//            }
//        });
        Calendar c = Calendar.getInstance();
        int CurrentWeekDay = c.get(Calendar.DAY_OF_WEEK) - 1; //minus one because day of week 1 start with Sunday
        if (CurrentWeekDay == 0) {
            CurrentWeekDay = 7;
        }

        ArrayList<Food_ItemObject> TempItemArrayOrdered = new ArrayList<Food_ItemObject>();

        Log.d("hello", "Food_menu size is: " + String.valueOf(Food_XMLParserClass.q1.size()));
        for (int i = 0; i < Food_XMLParserClass.q1.size(); i++) {

            if (CurrentWeekDay > 7) {
                CurrentWeekDay = 1;
            }
            TempItemArrayOrdered.add(TempItemArray.get(CurrentWeekDay - 1));
            CurrentWeekDay++;
        }
        TempItemArray = TempItemArrayOrdered;
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
//        startActivityPrompt(i);
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsMain:

                if (isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_LONG).show();
                    Food_GetDataAsyncTask getDataTask = new Food_GetDataAsyncTask(Food_MainList.this, "normal");
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
