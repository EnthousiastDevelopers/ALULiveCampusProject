package com.tolotranet.livecampus.Event;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.tolotranet.livecampus.Event.CalendarList.Event_CalendarList_Main;
import com.tolotranet.livecampus.Event.CalendarList.Event_CalendarList_XMLParserClass;
import com.tolotranet.livecampus.Food.Food_GetDataAsyncTask;
import com.tolotranet.livecampus.Food.Food_XMLParserClass;
import com.tolotranet.livecampus.Head.Head_Service_Activity;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Transp.Transp_GetDataAsyncTask;
import com.tolotranet.livecampus.Transp.Transp_XMLParserClass;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Event_SpreadSheetActivity extends Activity implements EasyPermissions.PermissionCallbacks {

    private GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mCallApiButton;
    private ProgressDialog mProgress;
    private SimpleDateFormat formatter1;
    private Context context;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
    private boolean wasCalledFromService = false;

    public void setContextData(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");
        mProgress.show();
        setContextData(Event_SpreadSheetActivity.this);
        setContentViewManually("bubble-stop");

    } //end of onCreate

    public void setContentViewManually(String target) {
        if (target.equals("bubble")) { //if this activity was called from service
            wasCalledFromService = true;
        } else {
            if (getIntent().getStringExtra("origin") != null && getIntent().getStringExtra("origin").equals("bubble")) {
                Log.d("hello", "origin buble");
                wasCalledFromService = true;
            } else {
                setContentView(R.layout.event_spread_sheet);
                Log.d("hello", "origin not bubble, origin normal");
            }
        }
        Log.d("hello", "finished loading 1");
        if (Event_CalendarList_XMLParserClass.q2 == null) {
            Log.d("hello", "finished loading 2");
            loadCalendarListIfnotYetDone();
            Log.d("hello", "finished loading calendar");
        }
        Log.d("hello", "finished loading 5");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                context.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        //prioritize online data download if is device connected

        if (isNetworkAvailable()) {
            getResultsFromApi(mCredential, context);
        } else {
            Event_startApplicationAsyncTask myTask = new Event_startApplicationAsyncTask();
            myTask.execute(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spread_sheet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsSpreadSheet:
                if (isNetworkAvailable()) {
                    Toast.makeText(context.getApplicationContext(), "Updating.....",
                            Toast.LENGTH_SHORT).show();
                    Event_GetDataAsyncTask getDataTask = new Event_GetDataAsyncTask();
                    getDataTask.execute(this);
                } else {
                    Toast.makeText(context.getApplicationContext(),
                            "check Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void onDestroy() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
        super.onDestroy();
    }


    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */


//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.calendar_events);
//
//            getResultsFromApi();
//
//            mCallApiButton = new Button(this);
//            mCallApiButton.setText(BUTTON_TEXT);
//            mCallApiButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mCallApiButton.setEnabled(false);
//                    Log.d("hello","");
//                    getResultsFromApi();
//                    mCallApiButton.setEnabled(true);
//                }
//            });
//
//            mProgress = new ProgressDialog(this);
//            mProgress.setMessage("Calling Google Calendar API ...");
//
//
//            // Initialize credentials and service object.
//            mCredential = GoogleAccountCredential.usingOAuth2(
//                    context.getApplicationContext(), Arrays.asList(SCOPES))
//                    .setBackOff(new ExponentialBackOff());
//        }
//


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     *
     * @param mCredential
     */
    public void getResultsFromApi(GoogleAccountCredential mCredential, Context context) {
        if (!isGooglePlayServicesAvailable(context)) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount(context, mCredential);
        } else if (!isNetworkAvailable()) {
            Log.d("hello", "No network connection available.");
        } else {
            new Event_MakeRequest_AsyncTask(mCredential, context).execute();
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
    private void chooseAccount(Context context, GoogleAccountCredential mCredential) {
        if (context == null) {
            Log.d("hello", "yooo dudue context is null");
        } else if (mCredential == null) {
            Log.d("hello", "yooo dudue mCredential is null");

        }
        int REQUEST_ACCOUNT_PICKER = 1000;
        int REQUEST_AUTHORIZATION = 1001;
        int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
        int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
        String PREF_ACCOUNT_NAME = "accountName";
        SharedPreferences mPrefs;
        if (EasyPermissions.hasPermissions(
                context, Manifest.permission.GET_ACCOUNTS)) {
            mPrefs = context.getSharedPreferences("AccountAPIGoogle", Context.MODE_PRIVATE);
            //mPrefs = Event_SpreadSheetActivity.this.getSharedPreferences("", Context.MODE_PRIVATE);
            // mPrefs = getPreferences(Context.MODE_PRIVATE);
            // String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            String accountName = mPrefs.getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi(mCredential, context);
            } else {
                //if this function wass called from service, open activity and restart the whole process again cause we need account and startactivitty for result wont work in service
                if (wasCalledFromService) {
                    Intent i = new Intent(context, Event_SpreadSheetActivity.class);
                    // i.putExtra("origin", "bubble");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(i);
                    Toast.makeText(context, "No shared preference found", Toast.LENGTH_SHORT).show();
                } else {
                    // Start a dialog from which the user can choose an account
                    ((Activity) context).startActivityForResult(
                            mCredential.newChooseAccountIntent(),
                            REQUEST_ACCOUNT_PICKER);
                }
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    context,
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
                    getResultsFromApi(mCredential, Event_SpreadSheetActivity.this);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getSharedPreferences("AccountAPIGoogle", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi(mCredential, Event_SpreadSheetActivity.this);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi(mCredential, Event_SpreadSheetActivity.this);
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
     * /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
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
                Event_SpreadSheetActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */

    /**
     * Created by Tolotra Samuel on 11/03/2017.
     */
    private void loadCalendarListIfnotYetDone() {
        Log.d("hello", "finished loading 3");

        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
        File myfile = new File(Dir, "Calendar.txt");
        Log.d("hello", "xmlparserclass calendar not yet loaded, i am going to do it now");
        Log.d("hello", "finished loading 4");

        if (myfile.exists()) {
            Log.d("hello", "File Exists");
            try {
                new Event_CalendarList_XMLParserClass();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Log.d("hello", "File Doesn't Exists");
            Intent i = new Intent(context.getApplicationContext(), Event_CalendarList_Main.class);
           context.startActivity(i);
            finish();

        }
        Log.d("hello", "finished loading 4.1");

    }


    private class Event_MakeRequest_AsyncTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;
        private SimpleDateFormat formatter1;
        private Context context;
        private ProgressDialog mProgress;
        static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;


        Event_MakeRequest_AsyncTask(GoogleAccountCredential credential, Context c) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            this.context = c;
            Log.d("hello", "ok, start calendar request");
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            getDataFromApi();

            try {
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }


            //Use the current XMP_parser Class variables to create a long string to sink into fileoperation on memory
            try {
                String xmlString = Event_XMLCreator.CreateSpreadSheetToXML();
                Event_FileOperations.StoreData(xmlString);
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
            return null;
        }


        //this function is get called only after the data has been downloaded , this function is used before saving the file using XML_Creator


        /**
         * Fetch a list of the next 10 events from the primary calendar.
         *
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        ArrayList<String> q1 = new ArrayList<String>();
        ArrayList<String> q2 = new ArrayList<String>();
        ArrayList<String> q3 = new ArrayList<String>();
        ArrayList<String> q4 = new ArrayList<String>();
        ArrayList<String> q5 = new ArrayList<String>();
        ArrayList<String> q6 = new ArrayList<String>();
        ArrayList<String> q7 = new ArrayList<String>();
        ArrayList<String> q8 = new ArrayList<String>();
        ArrayList<String> q9 = new ArrayList<String>();
        ArrayList<String> q10 = new ArrayList<String>();
        ArrayList<String> q11 = new ArrayList<String>();
        ArrayList<String> q12 = new ArrayList<String>();
        ArrayList<String> q13 = new ArrayList<String>();
        ArrayList<String> q14 = new ArrayList<String>();
        ArrayList<String> Tag_Array = new ArrayList<String>();


        private List<String> getDataFromApi() {
            Log.d("hello", "ok, start getDataFromAPI request");

            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            List<Event> items = null;
            List<String> color = new ArrayList<String>();
            int i = 0;


            for (String CalendarID : Event_CalendarList_XMLParserClass.q2) {
                //if calendar has set display true
                if (Event_CalendarList_XMLParserClass.q6.get(i).equals("true")) {
                    if (Event_CalendarList_XMLParserClass.q7.get(i).equals("mine")) {
                        Events events1 = null;
                        try {
                            events1 = mService.events().list(CalendarID)
                                    .setMaxResults(30)
                                    .setTimeMin(now)
                                    .setOrderBy("startTime")
                                    .setSingleEvents(true)
                                    .execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<Event> item1 = events1.getItems();
                        //mergin the two lists

                        if (items == null) {
                            items = item1;
                        } else {
                            items.addAll(item1);
                        }

                        //adding color
                        for (int x = 0; x < item1.size(); x++) {
                            color.add(Event_CalendarList_XMLParserClass.q5.get(i));
                        }
                    } else if (Event_CalendarList_XMLParserClass.q7.get(i).equals("alu")) {
                        addALUScopeEvents(i);
                    } else {
                        Log.d("hello", "Error 4948796, category not found");
                    }
                } //end if display is true in xml calendar q6
                i++;
            }//end loop for each

            //END GETTING THE PRIMARY CALENDAR EVENTS
            if (items != null) {
                //because if items is null, it means that the user didn't selected anything on its calendar
                updateXMLparserDataFromCurrent(items, color);
            }
            return eventStrings;
        }

        private void addALUScopeEvents(int i) {
            if (Event_CalendarList_XMLParserClass.q1.get(i).equals("Transport Schedule")) {
                addTransportScheduleToEventXMLdata(i);
            } else if (Event_CalendarList_XMLParserClass.q1.get(i).equals("Food Menu")) {
                addFoodMenuToEventXMLdata(i);
            }
        }

        private void addFoodMenuToEventXMLdata(int t) {
            formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");
            if (Food_XMLParserClass.q2 == null) {
                new Food_GetDataAsyncTask(context, "refreshonly"); //this is not asynctask, download and save
                //now read
            }
            if (Food_XMLParserClass.q2 == null) {
                try {
                    Log.d("hello", "Transport XML data is missing, but dont worry, i did it ");
                    new Food_XMLParserClass();

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Calendar c = Calendar.getInstance();
            boolean hasBeenAlignedToDayOfWeek = false;

            for (int w = 0; w < 3; w++) {
                for (int i = 0; i < Food_XMLParserClass.q2.size(); ) { //loop over the whole food menu

                    int todayNofweek = c.getTime().getDay(); //min 0, max 6, from sunday to monday

                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    if (todayNofweek == 0) {
                        //if today week number is 0 (sunday), change it to 6 because q1 know sunday as 7 ( 6 + 1 below)
                        todayNofweek = 7;
                    }
                    Log.d("hello dayofWek", String.valueOf(todayNofweek));
                    Log.d("hello dayofWekq1", String.valueOf(Food_XMLParserClass.q1.get(i)));
                    //if today week number is not the same as day of week one q1 which is the order start mon at 1
                    if (todayNofweek == Integer.parseInt(Food_XMLParserClass.q1.get(i))
                            || hasBeenAlignedToDayOfWeek) {
                        hasBeenAlignedToDayOfWeek = true;

                        //continue here is we go the same week day
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                        String dateString = sdf.format(c.getTime());

                        for (int n = 0; n < 5; n++) { // we eat n times a day ( n max = 5) // all the same day

                            //Default Values
                            String startTimeTxt = "07:30 AM";
                            String endTimeTxt = "09:30 AM";
                            String StartTimeStampStr = formatTimeAndDate(startTimeTxt, dateString);
                            String EndTimeStampStr = formatTimeAndDate(endTimeTxt, dateString);
                            String FoodName = Food_XMLParserClass.q9.get(i); // breakfast;
                            String category = "Breakfast";
                            switch (n) {
                                case 0:
                                    //breakfast
                                    startTimeTxt = "07:30 AM";
                                    endTimeTxt = "09:30 AM";
                                    StartTimeStampStr = formatTimeAndDate(startTimeTxt, dateString);
                                    FoodName = Food_XMLParserClass.q9.get(i); // breakfast;
                                    category = "Breakfast";

                                    break;
                                case 1:
                                    //snack
                                    startTimeTxt = "09:30 AM";
                                    endTimeTxt = "12:00 AM";
                                    StartTimeStampStr = formatTimeAndDate(startTimeTxt, dateString);
                                    FoodName = Food_XMLParserClass.q10.get(i); // breakfast;
                                    category = "Snack";

                                    break;
                                case 2:
                                    //lunch
                                    startTimeTxt = "12:00 PM";
                                    endTimeTxt = "02:00 PM";
                                    StartTimeStampStr = formatTimeAndDate(startTimeTxt, dateString);
                                    FoodName = Food_XMLParserClass.q11.get(i); // breakfast;
                                    category = "Lunch";

                                    break;
                                case 3:
                                    //lunch
                                    startTimeTxt = "02:00 PM";
                                    endTimeTxt = "06:00 PM";
                                    StartTimeStampStr = formatTimeAndDate(startTimeTxt, dateString);
                                    FoodName = Food_XMLParserClass.q12.get(t); // breakfast;
                                    category = "Afternoon Snack";
                                    break;
                                case 4:
                                    //lunch
                                    startTimeTxt = "07:00 PM";
                                    endTimeTxt = "09:00 PM";
                                    StartTimeStampStr = formatTimeAndDate(startTimeTxt, dateString);
                                    FoodName = Food_XMLParserClass.q13.get(i); // breakfast;
                                    category = "Dinner";
                                    break;
                            }

                            EndTimeStampStr = formatTimeAndDate(endTimeTxt, dateString);
                            //variables
                            q1.add(Food_XMLParserClass.q1.get(i) + "_" + Food_XMLParserClass.q2.get(i));//id (=order + day name)
                            q2.add(FoodName);//name
                            q4.add(category);//name
                            q6.add(startTimeTxt); //startime
                            q7.add(endTimeTxt); //endtime
                            //timestamp
                            q10.add(StartTimeStampStr); //start timestamp
                            q12.add(EndTimeStampStr); //end TimeStamp

                            //Constants
                            q3.add("");//location
                            q5.add(""); //description
                            q8.add(""); //no organiser
                            q9.add("catering@alueducation.com"); // contact
                            q11.add(Event_CalendarList_XMLParserClass.q5.get(t)); //color
                            q13.add(""); //none
                            q14.add(""); //none

                        }//end n food 5 times
                        c.add(Calendar.DATE, +1);  // number of days to add only if the hasbeen aligned to day of week
                    } else {
                        //else if todayNofweek + 1 is not equal to Integer.parseInt(Food_XMLParserClass.q1.get(i)
                        //meaning that the day of week is not yet aligned
                        //and to not increment 1 to i;
                        //Do nothing, just moving one in life
                    }
                    i++; //alwayas increment even if not aligned with today of week
                }//event transp loop over xml data
            }
        }

        private String formatTimeAndDate(String startTimeTxt, String dateString) {

            SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat formatter3 = new SimpleDateFormat("kk'h'mm");
            Date time = null;
            try {
                time = formatter2.parse(startTimeTxt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String timeString = formatter3.format(time);
            String TimeStampStr = dateString + " " + timeString;
            return TimeStampStr;
        }


        private void addTransportScheduleToEventXMLdata(int i) {
            formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");
            if (Transp_XMLParserClass.DateArray == null) {
                new Transp_GetDataAsyncTask(context, "refreshonly"); //this is not asynctask, download and save
                //now read
            }
            if (Transp_XMLParserClass.TimeStamp == null) {
                try {
                    Log.d("hello", "Transport XML data is missing, but dont worry, i did it ");
                    new Transp_XMLParserClass();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (int t = 0; t < Transp_XMLParserClass.TimeStamp.size(); t++) {
                Date TimeStamp = null;
                Log.d("hello", "checkpoint 1 for " + String.valueOf(t));
                Log.d("hello", "checkpoint 2 for " + String.valueOf(Transp_XMLParserClass.TimeStamp.size()) + " " + Transp_XMLParserClass.TimeStamp.get(t));
                try {
                    TimeStamp = formatter1.parse(Transp_XMLParserClass.TimeStamp.get(t));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm aa");

                q1.add(Transp_XMLParserClass.TimeStamp.get(t));
                q2.add(Transp_XMLParserClass.RouteArray.get(t));
                q3.add(Transp_XMLParserClass.RouteArray.get(t));
                q4.add(Transp_XMLParserClass.Day_Array.get(t));
                q5.add(Transp_XMLParserClass.Cohort_Array.get(t));
                String startTimeTxt = formatter2.format(TimeStamp);
                q6.add(startTimeTxt);
                q7.add(""); //no end time
                q8.add(""); //no organiser
                q9.add("transport@alueducation.com"); // contact
                //timestamp
                q10.add(formatter1.format(TimeStamp));
                q11.add(Event_CalendarList_XMLParserClass.q5.get(i));//color
                q12.add("");
                q13.add("");
                q14.add("");
            }//event transp loop over xml data
        }


        private void updateXMLparserDataFromCurrent(List<Event> items, List<String> color) {
            formatter1 = new SimpleDateFormat("dd/MM/yy kk'h'mm");

            int i = 0;
            for (Event event : items) {
                Log.d("hello", "events added number:" + String.valueOf(i));

                DateTime startTime = event.getStart().getDateTime();
                //startime
                if (startTime == null) {
                    // All-day events don't have startTime times, so just use  the startTime date.
                    startTime = event.getStart().getDate();
                }
                Date startTimes = new Date(startTime.getValue());
                DateTime endTime = event.getEnd().getDateTime();
                if (endTime == null) {
                    // All-day events don't have endTime times, so just use the endTime date
                    endTime = event.getEnd().getDate();
                }
                Date endTimes = new Date(endTime.getValue());

                //id
                q1.add(event.getId());
                //name
                q2.add(event.getSummary());
                //location
                if (event.getLocation() == null) {
                    q3.add("");
                } else {
                    q3.add(event.getLocation());
                }

                Log.d("hello", "check point q3 sucessfull");
                //readable date
                String StartdateTxt = DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM, DateFormat.SHORT).format(startTimes);
                q4.add(StartdateTxt);

                Log.d("hello", "check point q4 sucessfull");
                //description
                if (event.getDescription() == null) {
                    q5.add("");
                } else {
                    q5.add(event.getDescription());
                }

                //startTime time txt
                SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm aa");
                String startTimeTxt = formatter2.format(startTimes);
                q6.add(startTimeTxt);

                Log.d("hello", "check point q6 sucessfull");

                //end time txt
                String endTimeTxt = formatter2.format(endTimes);
                q7.add(endTimeTxt);

                //organiser
                if (event.getCreator().getDisplayName() == null) {
                    q8.add("");
                } else {
                    q8.add(event.getCreator().getDisplayName());
                }

                //organiser contact
                Log.d("hello", "check point q8 sucessfull");
                q9.add(event.getCreator().getEmail());

                //timestamp
                q10.add(formatter1.format(startTimes)); //start timestamp
                q11.add(color.get(i));
                q12.add(formatter1.format(endTimes)); //end timestamp
                q13.add("");
                q14.add("");
                i++;
            }//end of loop over all events from the api results

            Event_XMLParserClass.q1 = q1;
            Event_XMLParserClass.q2 = q2;
            Event_XMLParserClass.q3 = q3;
            Event_XMLParserClass.q4 = q4;
            Event_XMLParserClass.q5 = q5;
            Event_XMLParserClass.q6 = q6;
            Event_XMLParserClass.q7 = q7;
            Event_XMLParserClass.q8 = q8;
            Event_XMLParserClass.q9 = q9;
            Event_XMLParserClass.q10 = q10;
            Event_XMLParserClass.q11 = q11;
            Event_XMLParserClass.q12 = q12;
            Event_XMLParserClass.q13 = q13;
            Event_XMLParserClass.q14 = q14;
            Event_XMLParserClass.Tag_Array = Tag_Array;

        }


        @Override
        protected void onPreExecute() {
            mProgress = new ProgressDialog(context);
            mProgress.setMessage("Loading data ...");
            if (!wasCalledFromService) {
                mProgress.show();
            }
        }

        //onPost Execute is called last in the asynctask process
        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            Log.d("hello", "Data retrieved using the Google Calendar API");

            //Start mainlist after getting data from the cloud

            if (wasCalledFromService) {
                Log.d("hello", "Speadsheet event was called from service");
                wasCalledFromService = false;
                Intent si = new Intent(context, Head_Service_Activity.class);
                si.putExtra("hasNewData", true);
                context.startService(si);
//                finishAndRemoveTask();
//                android.os.Process.killProcess(android.os.Process.myPid());
                // System.exit(0);
            } else {
                Intent i = new Intent(context, Event_MainList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        }//end onPostexecute

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    ((Activity) context).startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    Log.d("hello", "The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.d("hello", "Request cancelled.");
            }
        }

        void showGooglePlayServicesAvailabilityErrorDialog(
                final int connectionStatusCode) {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            Dialog dialog = apiAvailability.getErrorDialog(
                    (Activity) context,
                    connectionStatusCode,
                    REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        }
    }
}
