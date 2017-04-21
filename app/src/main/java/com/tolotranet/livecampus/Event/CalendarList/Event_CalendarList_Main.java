package com.tolotranet.livecampus.Event.CalendarList;

/**
 * Created by Tolotra Samuel on 16/02/2017.
 */


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.tolotranet.livecampus.Event.Event_SpreadSheetActivity;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sign.Sign_DatabaseHelper;
import com.tolotranet.livecampus.Sign_User_Object;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Event_CalendarList_Main extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mCallApiButton;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};


    //View variables
    ArrayList<Event_CalendarList_ItemObject> ContactItemArray;
    Event_CalendarList_MyCustomBaseAdapter myAdapter;
    EditText SearchET;
    com.github.clans.fab.FloatingActionButton fab_refresh;
    com.github.clans.fab.FloatingActionButton fab_add;
    ListView lv;

    /**
     * Create the main activity.
     *
     * @param savedInstanceState previously saved instance data.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_calendar_list);


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");

        //Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        //getting the list of the calendars of the user
        getResultsFromApi();


        //setting up tool bar back and post button
        setupToolBarElements();


    }

    private class MySwitchOnItemClickLisBlablablaIknowit implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.saveTB:

                saveCurrentCalendarList();
                Intent i = new Intent(Event_CalendarList_Main.this, Event_SpreadSheetActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupToolBarElements() {
        // setting up tool bar back and post button

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Event_CalendarList_Main.super.onBackPressed();
            }
        });

    }

    private void setUpListView() {
        lv = (ListView) findViewById(R.id.Contacts_list_view);
        SearchET = (EditText) findViewById(R.id.SearchET);

        ContactItemArray = MakeArrayList(Event_CalendarList_Main.this);
        Log.d("hello calendar", String.valueOf(ContactItemArray.size()));
        myAdapter = new Event_CalendarList_MyCustomBaseAdapter(Event_CalendarList_Main.this,
                ContactItemArray);
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new MySwitchOnItemClickLisBlablablaIknowit());
    }

    public ArrayList<Event_CalendarList_ItemObject> MakeArrayList(Context context) {
        ArrayList<Event_CalendarList_ItemObject> TempItemArray = new ArrayList<>();
        ArrayList<Event_CalendarList_ItemObject> TempItemArrayCalendar = MakeArrayList_CreateSection_CALENDAR(context);
        return TempItemArrayCalendar;
    }


    private ArrayList<Event_CalendarList_ItemObject> MakeArrayList_CreateSection_CALENDAR(Context context) {
        ArrayList<Event_CalendarList_ItemObject> TempItemArray = new ArrayList<>();

        Event_CalendarList_ItemObject CIO = new Event_CalendarList_ItemObject();

        if (Event_CalendarList_XMLParserClass.q1 == null) {
            Log.d("hello", "missing Event_CalendarList_XMLParserClass jump accessed, without refreshing the Sis from cloud, but it's ok");
            try {
                new Event_CalendarList_XMLParserClass();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int size = Event_CalendarList_XMLParserClass.q1.size();
        //End of head SECTION
        Log.d("hello", "total of this calendar in makearraylisy is " + String.valueOf(size));

        for (int i = 0; i < Event_CalendarList_XMLParserClass.q1.size(); i++) {

            Log.d("hello", "add this calendar in makearraylist " + String.valueOf(i));
            CIO = new Event_CalendarList_ItemObject();

            CIO.setName(Event_CalendarList_XMLParserClass.q1.get(i));
            CIO.setObjectID(Event_CalendarList_XMLParserClass.q2.get(i));
            CIO.setBottomText(Event_CalendarList_XMLParserClass.q3.get(i)); //description
            CIO.setMiniText(Event_CalendarList_XMLParserClass.q4.get(i)); //access control
            CIO.setBackGroundColor(Event_CalendarList_XMLParserClass.q5.get(i));//q5 is the background color
            CIO.setState(Boolean.valueOf(Event_CalendarList_XMLParserClass.q6.get(i)));
            CIO.setIndex(i);
            CIO.setType(Integer.parseInt(Event_CalendarList_XMLParserClass.q8.get(i)));

            TempItemArray.add(CIO);

            //if i has passed 0, 1, 2, add the section header for calendar category mine (0 is the first header seciton)
            if (i == 2) {
                CIO = new Event_CalendarList_ItemObject();

                //Start of head SECTION
                if (Sign_User_Object.Email == null) {
                    Log.d("hello", "missing Sign_User_Object jump accessed, without refreshing the Sis from cloud, but it's ok");
                    Sign_DatabaseHelper helper = new Sign_DatabaseHelper(context);
                    helper.AllUserDataBaseToObject();
                }
                CIO.setName(Sign_User_Object.Email);
                CIO.setType(0);
                TempItemArray.add(CIO);

            }
        }//end for xml loop
        return TempItemArray;
    }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.d("hello", "No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.d("hello",
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    public void saveCurrentCalendarList() {
        try {
            String xmlString = Event_CalendarList_XMLCreator.CreateSpreadSheetToXML();
            Event_CalendarList_FileOperations.StoreData(xmlString);
//			Log.d("hello",xmlString);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Event_CalendarList_Main.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }


        @Override
        protected List<String> doInBackground(Void... params) {
            return getDataFromApi();

        }


        private List<String> getDataFromApi()   {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();


            //GETTING THE LIST OF THE CALENDARS
            String pageToken = null;

            CalendarList calendarList = null;
            try {
                calendarList = mService.calendarList().list().setPageToken(pageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<CalendarListEntry> items_Calendar_list = calendarList.getItems();

            InitialiseCalendarListVariables(items_Calendar_list);
            //GETTING THE LIST OF THE CALENDARS
            saveCurrentCalendarList();
            return eventStrings;
        }

        ArrayList<String> q1 = new ArrayList<String>();
        ArrayList<String> q2 = new ArrayList<String>();
        ArrayList<String> q3 = new ArrayList<String>();
        ArrayList<String> q4 = new ArrayList<String>();
        ArrayList<String> q5 = new ArrayList<String>();
        ArrayList<String> q6 = new ArrayList<String>();
        ArrayList<String> q7 = new ArrayList<String>();
        ArrayList<String> q8 = new ArrayList<String>();

        private void InitialiseCalendarListVariables(List<CalendarListEntry> items_calendar_list) {

            int i = 0;
            addALUCalendarScopeOnTop();
            for (CalendarListEntry calendarListEntry : items_calendar_list) {

                Log.d("hello", "add calendar number: " + String.valueOf(i));
                i++;
                q1.add(calendarListEntry.getSummary());
                q2.add(calendarListEntry.getId());
                if (calendarListEntry.getDescription() == null) {
                    q3.add("");
                } else {
                    q3.add(calendarListEntry.getDescription());
                }
                q4.add(calendarListEntry.getAccessRole());
                q5.add(calendarListEntry.getBackgroundColor());
                Log.d("hello", "bg color is " + calendarListEntry.getBackgroundColor());
                //q6 should follow the online isSelected, online if the it's the first time, or the file calendar.txt doesn't exist yet
                if (Event_CalendarList_XMLParserClass.q6 == null) {
                    q6.add(String.valueOf(calendarListEntry.getSelected()));
                } else {
                    int index = Event_CalendarList_XMLParserClass.q2.indexOf(calendarListEntry.getId());
                    if(index!=-1) { //if the calendar is  saved in cache, meaning the calendar is not new
                        q6.add(Event_CalendarList_XMLParserClass.q6.get(index));
                    }else{//if the calendar is  not yet saved in cache, meaning the calendar is  new
                        q6.add(String.valueOf(calendarListEntry.getSelected()));
                    }
                }
                q7.add("mine"); //category (ALU or my others calendar)
                q8.add("1"); //type (Header (0) or Item (1))
            }

            Event_CalendarList_XMLParserClass.q1 = q1;
            Event_CalendarList_XMLParserClass.q2 = q2;
            Event_CalendarList_XMLParserClass.q3 = q3;
            Event_CalendarList_XMLParserClass.q4 = q4;
            Event_CalendarList_XMLParserClass.q5 = q5;
            Event_CalendarList_XMLParserClass.q6 = q6;
            Event_CalendarList_XMLParserClass.q7 = q7;
            Event_CalendarList_XMLParserClass.q8 = q8;
        }

        private void addALUCalendarScopeOnTop() {
            q1.add("ALU Operation Team");
            q2.add("OPERATIONS_2017"); //id
            q3.add("Stay well informed about operations"); //description
            q4.add("Reader"); //access control
            q5.add("#ff4040");//q5 is the background color
            q6.add(setDynamycSelected("alu", "OPERATIONS_2017"));
            q7.add("alu"); //cat
            q8.add("0");  //type

            q1.add("Transport Schedule");
            q2.add("TRANSPORT_ALU");
            q3.add("Transport @ALU"); //description
            q4.add("Reader"); //access control
            q5.add("#ff4040");//q5 is the background color
            q6.add(setDynamycSelected("alu", "TRANSPORT_ALU"));
            q7.add("alu");
            q8.add("1");

            q1.add("Food Menu");
            q2.add("FOODMENU");
            q3.add("Food menu"); //description
            q4.add("Reader"); //access control
            q5.add("#404dff");//q5 is the background color
            q6.add(setDynamycSelected("alu", "FOODMENU"));
            q7.add("alu");
            q8.add("1");

        }

        private String setDynamycSelected(String category, String id) {
            if (category.equals("alu")) {
                if (Event_CalendarList_XMLParserClass.q6 == null) {
                } else {
                    int index = Event_CalendarList_XMLParserClass.q2.indexOf(id);
                    if (index != -1) {
                        return (Event_CalendarList_XMLParserClass.q6.get(index));
                    }
                }
            }
            return (("true"));//default
        }

        @Override
        protected void onPreExecute() {
            Log.d("hello", "");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();

            setUpListView();
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Event_CalendarList_Main.REQUEST_AUTHORIZATION);
                } else {
                    Log.d("hello", "The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.d("hello", "Request cancelled.");
            }
        }
    }
}