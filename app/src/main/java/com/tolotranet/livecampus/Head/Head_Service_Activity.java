package com.tolotranet.livecampus.Head;
//mine

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tolotranet.livecampus.Event.CalendarList.Event_CalendarList_ItemObject;
import com.tolotranet.livecampus.Event.CalendarList.Event_CalendarList_Main;
import com.tolotranet.livecampus.Event.CalendarList.Event_CalendarList_MyCustomBaseAdapter;
import com.tolotranet.livecampus.Event.CalendarList.Event_CalendarList_XMLParserClass;
import com.tolotranet.livecampus.Event.Event_DetailListView;
import com.tolotranet.livecampus.Event.Event_ItemObject;
import com.tolotranet.livecampus.Event.Event_MainList;
import com.tolotranet.livecampus.Event.Event_MyCustomBaseAdapter;
import com.tolotranet.livecampus.Event.Event_SpreadSheetActivity;
import com.tolotranet.livecampus.HeaderListView;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Voice.Voice_DialogActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Head_Service_Activity extends android.app.Service implements SwipeRefreshLayout.OnRefreshListener {

    private WindowManager windowManagerHeadBubble, windowManagerContentMain;
    private WindowManager.LayoutParams paramsHeadSticky;
    private WindowManager.LayoutParams paramsContentMain;
    private RelativeLayout dummyLineLeft, dummyLineTop;
    private WindowManager wm = null;
    private View headBubbleStickyIconView = null;
    private View mainContentView = null;
    private Button popupBtn;
    private ImageView imgViewheadBubbleStickyIcon, headBubbleMainContentView;
    private boolean hasCreatedMainContent = false;
    private LayoutInflater rootInflater = null;

    private ArrayList<Event_CalendarList_ItemObject> ItemArrayCalendarList;

    private Event_CalendarList_MyCustomBaseAdapter myAdapterCalendarList;
    private Event_CalendarList_Main calendarList;
    private ArrayList<String> tempEvent_CalendarList_XMLParserClass_q6;
    private SwipeRefreshLayout mSwipeRefreshLayoutEvents;
    private ArrayList<Event_ItemObject> ContactSectionArray;
    private ListView Calendarlv;
    private SwipeRefreshLayout mSwipeRefreshLayoutCalendar;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("hello", "onstartcommand called service bubble");
        boolean hasNewData = false;
        boolean toggleBubbleView = false;

        if (intent != null) {
            Log.d("hello", "intent (extra) detected");
            hasNewData = intent.getBooleanExtra("hasNewData", false); //the intent extras is from the startActivity in event_spreadsheet activity
            toggleBubbleView = intent.getBooleanExtra("toggleBubbleView", false); // if the intent was called from notification
            String s = intent.getStringExtra("keytest");
            if(s!= null){
                Log.d("hello TEST", s);
            }
        }

        if (toggleBubbleView) {
            toggleheadBubbleStickyIconView();
            Log.d("hello", "toggled");
        }
        if (hasNewData) {
            Log.d("hello", "onstartcommand called service bubble and hasnewData true detected");
            buildMainList(rootInflater, mainContentView);

            //buble might come from the swipe refresh
            mSwipeRefreshLayoutEvents.setRefreshing(false);
            mSwipeRefreshLayoutCalendar.setRefreshing(false);
        } else {
            showNotification();
        }
        return START_STICKY;
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, Head_Service_Activity.class);
        notificationIntent.putExtra("toggleBubbleView", true);
        notificationIntent.putExtra("keytest", "test");
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_logo_new)
                .setContentTitle("CampusLive")
                .setContentText("Bubble is running")
                .setContentIntent(pendingIntent);

        startForeground(17, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Log.d("EXIT", "ondestroy!");

        if (imgViewheadBubbleStickyIcon != null) {
            windowManagerHeadBubble.removeView(headBubbleStickyIconView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        startService(new Intent(getApplication(), Head_Service_Activity.class));
        super.onCreate();

        rootInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        createPopupHeadBubbleSticker(rootInflater);

        //this code is for dragging the chat head
        imgViewheadBubbleStickyIcon.setOnTouchListener(new MyDragOnTouchListener());
        imgViewheadBubbleStickyIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("hello", "lonpressed bubble");
//                Intent dialogIntent = new Intent(Head_Service_Activity.this, Voice_DialogActivity.class);
//                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(dialogIntent);

                Intent i = new Intent();
                i.setClass(Head_Service_Activity.this, Voice_DialogActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(i);
                return true;
            }
        });


        //IMAGE HEAD STICKY ONLY ONCLICKLISTENER//
        imgViewheadBubbleStickyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello", "I can hear you Img outer");
                if (hasCreatedMainContent) {
                    headBubbleStickyIconView.setVisibility(View.GONE);
                    mainContentView.setVisibility(View.VISIBLE);


                } else {
                    Log.d("hello", "I can hear you Img outer");
                    hasCreatedMainContent = true; // to avoid the mainConentView to be added more than once to the rootInflater

                    //hide the bubble head icon just clicked first
                    headBubbleStickyIconView.setVisibility(View.GONE);
                    //show maincontent
                    createPopupMainContent(rootInflater);

                    //those 3 views make the maincontent view gone and the head popup view visible onClick
                    headBubbleMainContentView.setOnClickListener(new OnCloserMainContentClickListener());
                    dummyLineTop.setOnClickListener(new OnCloserMainContentClickListener());


                    mainContentView.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            Log.d("hello", "any key is pressed");
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                Log.d("hello", "back pressed");
                                headBubbleMainContentView.performClick();
                                return true;
                            }
                            return false;
                        }
                    });
                    Log.d("hello", "after startapplication");
                }//else if not hasCreatedMainContent
            }//end onclick

        });
        //IMAGE HEAD STICKY ONLY ONCLICKLISTENER//

    }//end of OnCreate


    private void createPopupHeadBubbleSticker(LayoutInflater inflater) {
        //this is to plot the popup on top right of the screen
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();


        windowManagerHeadBubble = (WindowManager) getSystemService(WINDOW_SERVICE); //the windowmanager of the draggable content head
        //parameters of the floating head bubble
        paramsHeadSticky = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsHeadSticky.gravity = Gravity.TOP | Gravity.LEFT;
        paramsHeadSticky.x = display.getWidth() - 50; //on top right
        paramsHeadSticky.y = 100;

        //layout of the headBubble
        headBubbleStickyIconView = inflater.inflate(R.layout.head_ic_head, null); //sticky icon head only
        //image inside the headBubbleLayout, use to drag and click
        imgViewheadBubbleStickyIcon = (ImageView) headBubbleStickyIconView.findViewById(R.id.imgViewheadBubbleStickyIcon);
        //setting up the headerbubble
        windowManagerHeadBubble.addView(headBubbleStickyIconView, paramsHeadSticky);

    }

    private void toggleheadBubbleStickyIconView(){
       if( imgViewheadBubbleStickyIcon.getVisibility() == View.VISIBLE) {
           imgViewheadBubbleStickyIcon.setVisibility(View.GONE);
       }else{
           imgViewheadBubbleStickyIcon.setVisibility(View.VISIBLE);
       }
    }
    private void createPopupMainContent(LayoutInflater inflater) {
        //the windowmanager of the maincontent, not visible by default
        windowManagerContentMain = (WindowManager) getSystemService(WINDOW_SERVICE);
        //parameters of the main content
        paramsContentMain = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        paramsContentMain.gravity = Gravity.TOP | Gravity.RIGHT;
        paramsContentMain.x = 10;
        paramsContentMain.y = 10;
        //paramsContentMain.width = 400;
        //paramsContentMain.height = 700;
        paramsContentMain.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE;

        mainContentView = inflater.inflate(R.layout.head_content_main, null);

        headBubbleMainContentView = (ImageView) mainContentView.findViewById(R.id.imgViewheadBubbleStickyIcon);
        dummyLineTop = (RelativeLayout) mainContentView.findViewById(R.id.dummyLT);

        imageBtnEvent = (ImageView) mainContentView.findViewById(R.id.image1);
        imageBtnCalendar = (ImageView) mainContentView.findViewById(R.id.image2);
        image3 = (ImageView) mainContentView.findViewById(R.id.image3);

        imageBtnEvent.setOnClickListener(new NavBottomOnClickListener());
        imageBtnCalendar.setOnClickListener(new NavBottomOnClickListener());

        formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");

        buildMainList(inflater, mainContentView);
        //setting up the maincontent
        windowManagerContentMain.addView(mainContentView, paramsContentMain);

    }

    private ArrayList<Event_ItemObject> ContactItemArray;
    ArrayList<ArrayList<Event_ItemObject>> ContactArrayGroup = new ArrayList<ArrayList<Event_ItemObject>>(2);
    private Event_MyCustomBaseAdapter myAdapter;
    private ProgressDialog mProgress;
    private EditText SearchET;
    private ImageView imageBtnEvent;
    private ImageView imageBtnCalendar;
    private ImageView image3;
    private com.github.clans.fab.FloatingActionButton fab_refresh;
    private com.github.clans.fab.FloatingActionButton fab_add;
    private HeaderListView lv;
    private SimpleDateFormat formatter1;
    private Event_MainList event_mainList;

    private void buildMainList(LayoutInflater inflater, View mainContentView) {


        Log.d("hello", "buildMainlist tolotra");

        mSwipeRefreshLayoutEvents = (SwipeRefreshLayout) mainContentView.findViewById(R.id.swipe_refresh_layout_events);
        mSwipeRefreshLayoutCalendar = (SwipeRefreshLayout) mainContentView.findViewById(R.id.swipe_refresh_layout_calendar);

        mSwipeRefreshLayoutCalendar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMainlist();
            }
        });
        mSwipeRefreshLayoutEvents.setOnRefreshListener(this);
        mSwipeRefreshLayoutEvents.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3,
                R.color.refresh_progress_4);

        event_mainList = new Event_MainList();
        ContactArrayGroup = event_mainList.MakeArrayItemList();

        ContactItemArray = ContactArrayGroup.get(0);
        ContactSectionArray = ContactArrayGroup.get(1);

        lv = (HeaderListView) mainContentView.findViewById(R.id.Contacts_list_view);
        Calendarlv = (ListView) mainContentView.findViewById(R.id.Calendar_list_view);

        lv.setOnScrollListener(new CorrectSwipeOnScrollListener());

        mSwipeRefreshLayoutEvents.setVisibility(View.VISIBLE);
        mSwipeRefreshLayoutCalendar.setVisibility(View.GONE);

        SearchET = (EditText) mainContentView.findViewById(R.id.SearchET);

        myAdapter = new Event_MyCustomBaseAdapter(getApplicationContext(),
                ContactItemArray, ContactSectionArray);
        lv.setAdapter(myAdapter);

        // mSwipeRefreshLayoutEvents.setRefreshing(false);

        MyTextWatcher mytextwatcher = new Head_Service_Activity.MyTextWatcher();
        SearchET.addTextChangedListener(mytextwatcher);
        //lv.setOnItemClickListener(new Head_Service_Activity.AllContactListViewClickListener());

    }

    @Override
    public void onRefresh() {
        refreshMainlist();
    }

    public class AllContactListViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            int Index = ((Event_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisId = ((Event_ItemObject) arg0.getItemAtPosition(arg2))
                    .getUserId();

            //open editor profile if the person clicked is the current user

            Intent i = new Intent(getApplicationContext(),
                    Event_DetailListView.class);
            i.putExtra("index", Index);
            Log.d("hello", "Position Clicked is " + arg2);
            Log.d("hello", "Item Clicked is " + Index);
            Log.d("hello", "User Clicked is " + ThisId);
            startActivity(i);
        }

    }

//                        if (isNetworkAvailable()) {
//                            Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_LONG).show();
//                            Event_GetDataAsyncTask getDataTask = new Event_GetDataAsyncTask();
//                            getDataTask.execute(this);
//                        } else {
//                            Toast.makeText(getApplicationContext(), "check Internet Connection", Toast.LENGTH_SHORT).show();
//                        }

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
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            myAdapter.getFilter().filter(arg0.toString());
        }

    }


    private class MyDragOnTouchListener implements View.OnTouchListener {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Log.d("hello", "Touched sticky header");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = paramsHeadSticky.x;
                    initialY = paramsHeadSticky.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return false;
                case MotionEvent.ACTION_UP:
                    return false;
                case MotionEvent.ACTION_MOVE:
                    paramsHeadSticky.x = initialX
                            + (int) (event.getRawX() - initialTouchX);
                    paramsHeadSticky.y = initialY
                            + (int) (event.getRawY() - initialTouchY);
                    windowManagerHeadBubble.updateViewLayout(headBubbleStickyIconView, paramsHeadSticky);
                    return false;
            } // while(event.getAction()
            return true;
        }//on touch
    }

    private class OnCloserMainContentClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            headBubbleStickyIconView.setVisibility(View.VISIBLE);
            mainContentView.setVisibility(View.GONE);

        }
    }

    private class NavBottomOnClickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            if (v == imageBtnCalendar) {
                copyCurrentCalendarXMLParserToTemp(); //because we wanna compare it with the one after change, if similar, do not call event_spreadsheet activity
                buildCalendarList(rootInflater, mainContentView);
            }
            if (v == imageBtnEvent) {

                if (hasEditedCalendarList()) {
                    mSwipeRefreshLayoutEvents.setRefreshing(true);
                    calendarList.saveCurrentCalendarList();
                    refreshMainlist();
                }
                buildMainList(rootInflater, mainContentView);
            }
        }
    }


    //this is an async task method,
    // call the event_spreadsheetactivity without setting content view, close it after and re call this service to pass sign that download finished
    private void refreshMainlist() {
        Event_SpreadSheetActivity refresher = new Event_SpreadSheetActivity();
        refresher.setContextData(getApplicationContext());
        refresher.setContentViewManually("bubble");
//        Intent i = new Intent(Head_Service_Activity.this, Event_SpreadSheetActivity.class);
//        i.putExtra("origin", "bubble");
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
    }

    private boolean hasEditedCalendarList() {
        //tempEvent_CalendarList_XMLParserClass_q6 is a copy we made when we swiped to the calendarlist, if we never swipe, this array should be null
        if (tempEvent_CalendarList_XMLParserClass_q6 == null) {
            return false;
        }
        //else if it's not null, check wich one has changed
        Log.d("hello", "size of temp cal list is :" + String.valueOf(tempEvent_CalendarList_XMLParserClass_q6.size()));
        for (int i = 0; i < tempEvent_CalendarList_XMLParserClass_q6.size(); i++) {
            Log.d("hello", "new is " + tempEvent_CalendarList_XMLParserClass_q6.get(i)
                    + " and previous was " + Event_CalendarList_XMLParserClass.q6.get(i) + " " + Event_CalendarList_XMLParserClass.q1.get(i));
            if (!tempEvent_CalendarList_XMLParserClass_q6.get(i).equals(Event_CalendarList_XMLParserClass.q6.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void copyCurrentCalendarXMLParserToTemp() {
        if (Event_CalendarList_XMLParserClass.q6 == null) {
            Log.d("hello", "missing Event_CalendarList_XMLParserClass in service for temp copy of it, but it's ok, it loaded it from file, I assumed the file exists");
            try {
                new Event_CalendarList_XMLParserClass();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //copying the current xml data to temp, equaling won't copy it, it will just create a shortcut, this method works
        tempEvent_CalendarList_XMLParserClass_q6 = new ArrayList<String>(Event_CalendarList_XMLParserClass.q6);
    }

    private void buildCalendarList(LayoutInflater rootInflater, View mainContentView) {
        mSwipeRefreshLayoutEvents.setVisibility(View.GONE);
        mSwipeRefreshLayoutCalendar.setVisibility(View.VISIBLE);

        calendarList = new Event_CalendarList_Main();
        ItemArrayCalendarList = calendarList.MakeArrayList(Head_Service_Activity.this);
        myAdapterCalendarList = new Event_CalendarList_MyCustomBaseAdapter(Head_Service_Activity.this,
                ItemArrayCalendarList);
        Calendarlv.setAdapter(myAdapterCalendarList);
    }


    private class CorrectSwipeOnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                mSwipeRefreshLayoutEvents.setEnabled(true);
            } else mSwipeRefreshLayoutEvents.setEnabled(false);
        }
    }
}
