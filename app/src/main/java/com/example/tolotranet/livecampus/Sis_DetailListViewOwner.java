package com.example.tolotranet.livecampus;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.FindReplaceRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.tolotranet.livecampus.R.layout.sis_activity_detail_list_view;

public class Sis_DetailListViewOwner extends Activity
        implements EasyPermissions.PermissionCallbacks {

    ListView lv;
    TextView cy;
    TextView fn;
    Sis_DetailListViewAdapterOwner myPersonDetailListViewAdapter;
    ArrayList<Sis_DetailListItem> DetailList;
    TextView mOutputText;
    GoogleAccountCredential mCredential;
    int Index;
    int userID;
    String origin = "";
    private Button mCallApiButton;
    ProgressDialog mProgress;
    Boolean BoolHasUpdated = false;
    Map<String, String> HasMapLabelValueCoupleOld = new HashMap<String, String>();
    Map<String, String> HasmapUpdateCouple = new HashMap<String, String>();

    private FirebaseAnalytics mFirebaseAnalytics;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Sheets API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sis_activity_detail_list_view);
        Log.d("hello", "owner edit mode has started");

        Intent i = getIntent();
        Index = i.getIntExtra("index", 0);
        userID = i.getIntExtra("myId", 0);
        origin =  i.getStringExtra("origin");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        Bundle params2 = new Bundle();
        params2.putString("sis ", String.valueOf(userID));
        params2.putString("origin ", origin);
        mFirebaseAnalytics.logEvent("sis", params2);
        Log.d("hello", "I am in detail sis owner and I fetched the data in getdataasynctask");


        //first section (profile pic, fullname, country)
        fn = (TextView) findViewById(R.id.fullName);
        cy = (TextView) findViewById(R.id.country);
        fn.setText(Sis_XMLParserClass.q5.get(Index));
        cy.setText(Sis_XMLParserClass.q7.get(Index));

        //second section (the rest available)
        DetailList = getPersonalDetails(Index);

        lv = (ListView) findViewById(R.id.person_details_lv);

        myPersonDetailListViewAdapter = new Sis_DetailListViewAdapterOwner(this,
                DetailList);
        lv.setAdapter(myPersonDetailListViewAdapter);
        lv.setOnItemClickListener(new PersonDetailListViewClickListener());


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());


        for (int n = 0; n < lv.getAdapter().getCount(); n++) {
            View viewET = lv.getChildAt(n);
            if (viewET != null) {
                EditText ValueEditText = (EditText) viewET.findViewById(R.id.detail_value);
                if (ValueEditText.length() > 0) {
                    ValueEditText.getText().clear();
                }
                Log.d("hello", ":" + ValueEditText.getText().toString());
            }
        }
    }

    private ArrayList<Sis_DetailListItem> getPersonalDetails(int Index) {
        ArrayList<Sis_DetailListItem> DetailList = new ArrayList<Sis_DetailListItem>();


        Sis_DetailListItem sr = new Sis_DetailListItem();
        String tag = "EmailAddress";
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q2.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q2.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q2.get(Index));


        tag = "Firstname";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q3.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q3.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q3.get(Index));


        tag = "Lastname";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q4.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q4.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q4.get(Index));

        //just ignore fullname because spreadsheet will do that using first and last name
//        tag = "fullname";
//        sr = new Sis_DetailListItem();
//        sr.setDetailName("fullname");
//        if (!(Sis_XMLParserClass.q5.get(Index).equals(""))) {
//            sr.setDetailValue(Sis_XMLParserClass.q5.get(Index));
//        } else {
//            sr.setDetailValue("please updatme me! :-( ");
//        }
//        DetailList.add(sr);
//        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q5.get(Index));


        tag = "Birthday";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q6.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q6.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q6.get(Index));


        tag = "Nationality";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q7.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q7.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q7.get(Index));

        tag = "Major";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q15.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q15.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q15.get(Index));

        tag = "PassportNumber";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q8.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q8.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q8.get(Index));


        tag = "PhoneNumber1";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q9.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q9.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q9.get(Index));

        tag = "PhoneNumber2";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q10.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q10.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q10.get(Index));

        tag = "PhoneNumber3";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q11.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q11.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q11.get(Index));


        tag = "Apartment";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q12.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q12.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q12.get(Index));

        tag = "Residence";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q13.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q13.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q13.get(Index));


        tag = "Room";
        sr = new Sis_DetailListItem();
        sr.setDetailName(tag);
        if (!(Sis_XMLParserClass.q14.get(Index).equals(""))) {
            sr.setDetailValue(Sis_XMLParserClass.q14.get(Index));
        } else {
            sr.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(sr);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q14.get(Index));


        //test
        String x = DetailList.get(2).DetailName;
        Log.d("helloget2", x);
        return DetailList;
    }


    public class PersonDetailListViewClickListener implements
            OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            String TempDetailName = (String) ((Sis_DetailListItem) arg0
                    .getItemAtPosition(arg2)).getDetailName();
            if (TempDetailName.equals("Mobile")
                    || TempDetailName.equals("Residence")
                    || TempDetailName.equals("Office")) {
                String DetailValue = (String) ((Sis_DetailListItem) arg0
                        .getItemAtPosition(arg2)).getDetailValue();
                if (!DetailValue.equals("")) {
                    Intent callintent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:"
                                    + DetailValue.replaceAll("[A-Za-z()\\s]+",
                                    "").trim()));
                    startActivity(callintent);
                }
            }

            if (TempDetailName.equals("Email")) {
                String ToEmailId = (String) ((Sis_DetailListItem) arg0
                        .getItemAtPosition(arg2)).getDetailValue();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                Log.d("hello", ToEmailId);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{ToEmailId.trim()});
                // emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent,
                        "Send mail using..."));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.person_detail_list_view, menu);
        return true;
    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    @Override
    public void onBackPressed() {

        //Sis_UpdateMe updateMe = new Sis_UpdateMe();
        //updateMe.execute(this);

        Integer x = lv.getAdapter().getCount();
        Log.d("helloworld", x.toString());

        View parentView = null;

        for (int i = 0; i < lv.getAdapter().getCount(); i++) {

            parentView = getViewByPosition(i, lv);
            //View viewTelefone = lv.getChildAt(i);
            //this next condition mean, get the index of the view visible on screen(on back pressed)
//            if (viewTelefone != null) {

            TextView NameEditText = (TextView) parentView.findViewById(R.id.detail_name);
            EditText ValueEditText = (EditText) parentView.findViewById(R.id.detail_value);
            Log.d("hello", "Anything detected onscreen Label and value:" + NameEditText.getText().toString() + "--/--" + ValueEditText.getText().toString());

            String oldDataStr = HasMapLabelValueCoupleOld.get(NameEditText.getText().toString());
            Log.d("hello", "Old data looked up:" + NameEditText.getText().toString() + "--/--" + oldDataStr);
            Log.d("hellocompr", "Onscreen before and now:" + oldDataStr + "--/--" + ValueEditText.getText().toString());
            if (!oldDataStr.equals(ValueEditText.getText().toString())) {
                Log.d("hellocomparaison", "Different -> need update");

                //HasmapUpdateCouple is the findreplace couple to send to the sheet updater. oldDataStr is the find, ValueEdittext is the replace

                if (ValueEditText.getText().toString().equals("")) {
                    HasmapUpdateCouple.put(oldDataStr, "Update your " + NameEditText.getText().toString());

                    //exception 1 apartement to avoid interference with room number
                } else if ((NameEditText.getText().toString().equals("apartment"))) {
                    if (ValueEditText.getText().toString().startsWith("Apartment")) {
                        HasmapUpdateCouple.put(oldDataStr, ValueEditText.getText().toString());
                    } else {
                        HasmapUpdateCouple.put(oldDataStr, "Apartment " + ValueEditText.getText().toString());
                    }
                    //**end of exception
                } else {
                    HasmapUpdateCouple.put(oldDataStr, ValueEditText.getText().toString());
                }
                BoolHasUpdated = true;
            } else {
                Log.d("hellocomparaison", "Similar -> no need to update");
            }

        }

        //just testing if back pressed
        Log.d("helloevent test", "backpressed");

        if (BoolHasUpdated == true) {
            new AlertDialog.Builder(this)
                    .setTitle("Any Change made will be updated?")
                    .setMessage("Are you sure you want you enter the correct data? if you didn't change anything, you will just exit")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            //if yes clicked on alert dialog
                           // mProgress.show(); //start upload

                            getResultsFromApi();
                            // mProgress.hide();
                            //end of if yes clicked on alert dialog
                        }
                        //show the alert dialog
                    }).create().show();
        } else {
            //is BoolHasUpdated == false
          if(origin == null){
              Sis_DetailListViewOwner.super.onBackPressed();
          }else {
              if (origin.equals("appselect")) {
                  Intent i = new Intent(this, AppSelect.class);
                  startActivity(i);
              } else {
                  Sis_DetailListViewOwner.super.onBackPressed();
              }
          }
        }

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
            Log.d("hello", "google play service not available");
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            Log.d("hello", "no account chosen");
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.d("hello", "no network connexion available");
        } else {
            //edit
            mProgress = new ProgressDialog(Sis_DetailListViewOwner.this);
            mProgress.setMessage("Finalising edit ...");
            mProgress.show();

            new MakeRequestTask(mCredential).execute();


            if(origin == null){
                Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
                getDataTask.execute(this);
            }else{
                Log.d("helloorigin", origin);
            if(origin.equals("appselect")){
                Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
                getDataTask.target = "appselect";
                getDataTask.execute(this);
//                Intent i = new Intent(this, AppSelect.class); this is set into Sis_getdataasynctask
//                startActivity(i);
            }else {
                Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
                getDataTask.execute(this);
            }
        }}
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         *
         * @return List of names and majors
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            String spreadsheetId = "1En7qJ5aG2U9fEMSoh-wW1FXoqzNWQfMB4v6-vcN5pVg";
            String range = "student data!A2:V12";
            List<String> results = new ArrayList<String>();
            List<Request> requests = new ArrayList<>();

            ValueRange response = this.mService.spreadsheets().values().get(spreadsheetId, range).execute();
//            List<List<Object>> values = response.getValues();
//            if (values != null) {
//                results.add("Fullname, Nationality");
//                for (List row : values) {
//                    results.add(row.get(0) + ", " + row.get(4));
//                }
//            }

            //getting memory location
            Integer row = null;
            if (userID < 2000000 && userID > 999999) {
                //student reference IDs are between 1,000,000 and 2,000,000
                row = userID - 1000000;
            }


            for (Map.Entry<String, String> entry : HasmapUpdateCouple.entrySet()) {
                //check HasMapLabelValueCoupleOld iteration
                Log.d("hello HashMapUpdate", entry.getKey() + "/" + entry.getValue());


                requests.add(new Request().setFindReplace(new FindReplaceRequest().setFind(entry.getKey())
                        .setMatchEntireCell(true)
                        .setMatchCase(true)
                        .setReplacement(entry.getValue())
                        .setRange(new GridRange()
                                .setSheetId(0)
                                .setStartRowIndex(row)
                                .setEndRowIndex(row + 1))));
            }


            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(requests);

            try {
                mService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

//        private void updaterows() {
//
//        }


        @Override
        protected void onPreExecute() {
            Log.d("hello", ". onPreExecute");
            //mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            //mProgress.hide();
            if (output == null || output.size() == 0) {
                Log.d("hello", "No result returned");
            } else {
                output.add(0, "Data retrieved using the Google Sheets API:");
                Log.d("hello", TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            //mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Sis_DetailListViewOwner.REQUEST_AUTHORIZATION);
                } else {
                    Log.d("hello", "The following error occurred:\n"
                            + mLastError.getMessage());

                }
            } else {
                Log.d("hello", "Request cancelled.");
            }
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
                    "This app needs to access to Google.",
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
                    Log.d("hello", "This app requires Google Play Services. Please install " +
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
                Sis_DetailListViewOwner.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
    protected void onDestroy()
    {
        super.onDestroy();
        if (mProgress!=null && mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

}
//            List<CellData> valuesL = new ArrayList<>();


//            String cellReq = (new FindReplaceRequest().setFind("setfind!!! test")).getFind();
//            Log.d("findings", cellReq);

//This is a test (green color)

//			valuesL.add(new CellData()
//					.setUserEnteredValue(new ExtendedValue()
//							.setNumberValue(Double.valueOf(3)))
//					.setUserEnteredFormat(new CellFormat()
//							.setBackgroundColor(new Color()
//									.setGreen(Float.valueOf(1)))));
//			requests.add(new Request()
//					.setUpdateCells(new UpdateCellsRequest()
//							.setStart(new GridCoordinate()
//									.setSheetId(0)
//									.setRowIndex(3)
//									.setColumnIndex(2))
//							.setRows(Arrays.asList(
//									new RowData().setValues(valuesL)))
//							.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

