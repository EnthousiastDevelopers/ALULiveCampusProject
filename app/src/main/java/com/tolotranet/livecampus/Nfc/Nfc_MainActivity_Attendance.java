package com.tolotranet.livecampus.Nfc;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

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
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.Arrays;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import com.tolotranet.livecampus.App.App_Tools;
import com.tolotranet.livecampus.HttpRequestApp;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.RoundedImageView;
import com.tolotranet.livecampus.Sis.Sis_DetailListItem;
import com.tolotranet.livecampus.Sis.Sis_XMLParserClass;
import com.tolotranet.livecampus.Nfc.nfc_record.ParsedNdefRecord;

import org.xmlpull.v1.XmlPullParserException;

//////////

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device just discovered.
 */
public class Nfc_MainActivity_Attendance extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {

    private ArrayList<Sis_DetailListItem> DetailList;
    private GoogleAccountCredential mCredential;
    private int Index;
    private int userID;
    private int MyRow;
    private ProgressDialog mProgress;
    private String spreadSheetAction;
    private int ColumnID;
    private String NewValue;
    private com.github.clans.fab.FloatingActionButton fab_refresh;
    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};
    public com.google.api.services.sheets.v4.Sheets mService = null;
    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private LinearLayout linearLayoutContent;
    private volatile boolean hasFinishedNewIdRegistration = true;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private ArrayList<String> TempTrackList;
    App_Tools tools;
    private AlertDialog mDialog;
    private MediaPlayer mp_On = new MediaPlayer();
    private MediaPlayer mp_Off = new MediaPlayer();
    private List<Tag> mTags = new ArrayList<>();
    private String lastCourse;
    private TextView courseTV;
    private boolean hasShownCoursePopupThroughResolveIntent;
    private Vibrator vibe;
    private ArrayList<String> NameItemArrayString;
    private View childLayout;
    private DrawerLayout drawer;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setContentView(R.layout.tag_viewer);
        Log.d("hello", "on create");
        super.onCreate(savedInstanceState);

        setUpSharedDrawerLayout();
        includeThisLayoutContent();
       
        
        vibe = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        mp_On = MediaPlayer.create(Nfc_MainActivity_Attendance.this, R.raw.wav_success);
        mp_Off = MediaPlayer.create(Nfc_MainActivity_Attendance.this, R.raw.wav_alert);



        TempTrackList = new ArrayList<String>();

        linearLayoutContent = (LinearLayout)  childLayout.findViewById(R.id.list);
        resolveIntent(getIntent());

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            //finish();
            return;
        }
        mNfcAdapter.enableReaderMode(this,
                new NfcAdapter.ReaderCallback() {
                    @Override
                    public void onTagDiscovered(final Tag tag) {
                        // do something

                        //Attendance
                        final String cardID = dumpTagDataIdDec(tag);
                        Log.d("hello", "card ontagdiscovered ID in Dec is: " + cardID);
                        attendance(cardID);
                    }
                },
                NfcAdapter.FLAG_READER_NFC_A |
                        NfcAdapter.FLAG_READER_NFC_B |
                        NfcAdapter.FLAG_READER_NFC_F |
                        NfcAdapter.FLAG_READER_NFC_V |
                        NfcAdapter.FLAG_READER_NFC_BARCODE |
                        NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                null);



        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true)});


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        tools = new App_Tools();

        ImageView courseBtn = (ImageView) childLayout.findViewById(R.id.courseBtn);
        courseTV = (TextView) childLayout.findViewById(R.id.courseTV);
        lastCourse = courseTV.getText().toString();

        courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupCourserInput(""); //course popup change
            }
        });

        if (!hasShownCoursePopupThroughResolveIntent) {
            mp_Off.start();
            vibe.vibrate(100);
            showPopupCourserInput("");
        }
    }//end Oncreate

    private void setUpSharedDrawerLayout() {
        setContentView(R.layout.nfc_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setting theme
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.red_alu));
        // getSupportActionBar().setIcon(R.drawable.ic_logo_minimini);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void includeThisLayoutContent() {
        LayoutInflater inflater = (LayoutInflater)  this.getSystemService(LAYOUT_INFLATER_SERVICE);
        childLayout = inflater.inflate(R.layout.nfc_content_main_attendance, (ViewGroup) findViewById(R.id.content_nfc_main_content));
        RelativeLayout  parentLayout = (RelativeLayout) findViewById(R.id.nfc_include_relativeLT_container);
        parentLayout.addView(childLayout,0);

    }


    private void showPopupCourserInput(final String Cardid) {


        final AutoCompleteTextView modifyAT = new AutoCompleteTextView(Nfc_MainActivity_Attendance.this);
        //create new edittext programatically
        final ArrayAdapter<String> NamemyAutoCAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.nfc_simple_textview,
                getResources().getStringArray(R.array.course_suggestion_list));
        final String Titlelabel = "Course";//because we need to put the label to put on the dialog
        runOnUiThread(new Runnable() {
            public void run() {
                modifyAT.setAdapter(NamemyAutoCAdapter);
                modifyAT.setThreshold(1);
                modifyAT.setHint("Enter the course name");
                modifyAT.setSelection(modifyAT.getText().length()); // set the cursor at the end of the edittext immediately
                modifyAT.setTextColor(getResources().getColor(R.color.black));
                new AlertDialog.Builder(Nfc_MainActivity_Attendance.this)
                        .setTitle(Titlelabel)
                        .setView(modifyAT).setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // mProgress.show(); //start upload
                                        String newCourse = modifyAT.getText().toString();
                                        if (!newCourse.equals(lastCourse)) {
                                            courseTV.setText(newCourse);
                                            lastCourse = newCourse;


                                            //back up attendance before changing course
                                            String listString = "";

                                            for (String s : TempTrackList) {
                                                listString += s + "\n";
                                            }

                                            try {
                                                tools.StoreData("attendance-" + String.valueOf(tools.getTimeStamp()) + ".txt", listString);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            TempTrackList.clear(); //remove all previous attendance

                                            if (!(Cardid == "")) {
                                                attendance(Cardid);
                                            }
                                        }
                                        //recursion, we just made sure the card is regitered to one email address
                                    }//end onClisk yes

                                }//end on yes clicked           //end of if yes clicked on alert dialog
                        ).create().show();   //show the alert dialog
            }
        }); //end run on UI thread
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                modifyAT.showDropDown(); //show threshold 0
            }
        });
    }


    //
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_nfc__main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }


    private void UpdateCell(int row, int col, String val) {
        spreadSheetAction = "updateCell";
        MyRow = row;
        ColumnID = col;
        NewValue = val;
        if (!isGooglePlayServicesAvailable()) {
            Log.d("hello", "google play service not available");
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            Log.d("hello", "no account chosen");
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.d("hello", "no network connexion available");
        } else {
            //edit because all the requirements are met
            Log.d("hello", "edit because all the requirements are met");
            new MakeRequestTask(mCredential).execute(); //because MakeRequestTask contains mService that containts all the new information
        }
    }

    private void StartProcessSpreadSheetRequestAPI() {
        if (!isGooglePlayServicesAvailable()) {
            Log.d("hello", "google play service not available");
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            Log.d("hello", "no account chosen");
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.d("hello", "no network connexion available");
        } else {
            //edit because all the requirements are met
            Log.d("hello", "edit because all the requirements are met");
            new MakeRequestTask(mCredential).execute(); //because MakeRequestTask contains mService that containts all the new information
        }
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {

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
                return GetEditDataFromApi(); //because ALL request set are finished, create the request
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> GetEditDataFromApi() throws IOException {
            Log.d("hello", "starting get edit data from API");
            String spreadsheetId = "1En7qJ5aG2U9fEMSoh-wW1FXoqzNWQfMB4v6-vcN5pVg";
            List<Request> requests = new ArrayList<>();
            List<String> results = new ArrayList<String>();


            if (spreadSheetAction.equals("test")) {
                String range = "student data!A2:V12";

                ValueRange response = mService.spreadsheets().values().get(spreadsheetId, range).execute();

                List<List<Object>> values = response.getValues();
                if (values != null) {
                    results.add("Name, Major");
                    for (List row : values) {
                        Log.d("hello", row.get(0) + ", " + row.get(4));
                    }
                }
            }

            if (spreadSheetAction.equals("updateCell")) {


                Log.d("hello", "setting up Cells to update");
                List<CellData> NewValueCellData = new ArrayList<>();
                NewValueCellData.add(new CellData()
                        .setUserEnteredValue(new ExtendedValue()
                                .setStringValue(NewValue))
                        .setUserEnteredFormat(new CellFormat()
                                .setBackgroundColor(new Color()
                                        .setBlue(Float.valueOf(1)))));


                requests.add(new Request()
                        .setUpdateCells(new UpdateCellsRequest()
                                .setStart(new GridCoordinate()
                                        .setSheetId(0)
                                        .setRowIndex(MyRow - 1) //because the row index start with 0
                                        .setColumnIndex(ColumnID - 1)) //because the column index start with 0

                                .setRows(Arrays.asList(new RowData().setValues((NewValueCellData))))
                                .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
            }
            Log.d("hello", "column: " + String.valueOf(ColumnID) + ", row: " + String.valueOf(MyRow));


            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);

            //  String range = "student data!A2:V12";
            // ValueRange response = mService.spreadsheets().values().get(spreadsheetId, range).execute();

            mService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();

            hasFinishedNewIdRegistration = true;
            return results;
        }

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
                            Nfc_MainActivity_Attendance.REQUEST_AUTHORIZATION);
                } else {
                    Log.d("hello", "The following error occurred:\n"
                            + mLastError.getMessage());

                }
            } else {
                Log.d("hello", "Request cancelled.");
            }
        }
    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                Log.d("hello", "account chosen automatically");
                StartProcessSpreadSheetRequestAPI();
            } else {
                // Start a dialog from which the user can choose an account
                Log.d("hello", "account not yet chosen before picker");
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
                Log.d("hello", "account chosen after picker");
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access to Google.",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
        Log.d("hello", "account chosen end after spreadsheet activity");
    }


    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Nfc_MainActivity_Attendance.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

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
                    StartProcessSpreadSheetRequestAPI();
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
                        StartProcessSpreadSheetRequestAPI();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    StartProcessSpreadSheetRequestAPI();
                }
                break;
        }
    }

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


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();


        Log.d("hello", "on resume");
        if (mNfcAdapter != null) {
            if (!mNfcAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mNfcAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onPause() {
        Log.d("hello", "on pause");
        super.onPause();
        if (hasFinishedNewIdRegistration == true) {
            if (mNfcAdapter != null) {
                mNfcAdapter.disableForegroundDispatch(this);
                mNfcAdapter.disableForegroundNdefPush(this);
            }
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }


    //Resolve intent: read nfc; lookup nfc id with email and name; if not exists request email , find row, update nfc id column; send google form attendance
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                //dumpTagData is a stand alone function seen below
                byte[] payload = dumpTagData(tag).getBytes();

                //Attendance
                final String cardID = dumpTagDataIdDec(tag);
                Log.d("hello", "card ID in Dec is: " + cardID);
                //lookup cardid in sis_xml_parser class

                attendance(cardID);


                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
                mTags.add(tag);
            }
            // Setup the views
            //buildTagViews(msgs); //developer option

            //display the user on Screen


        }// end if NFC Adapter
    } //End resolve intent

    private void attendance(final String cardID) { //this method checks first if the card is linked to an email and then will send the http post

        //checking if course is valid
        if (lastCourse == null
                || lastCourse.isEmpty()
                || lastCourse.equals("Specify a course first")) {
            hasShownCoursePopupThroughResolveIntent = true;
            mp_Off.start();
            vibe.vibrate(100);
            showPopupCourserInput(cardID); //show alert dialog with course input
            Log.d("hello", "invalid course");
            return;
        }

        //checking internet connection
        if (!tools.isNetworkAvailable(Nfc_MainActivity_Attendance.this)) {
            Toast.makeText(Nfc_MainActivity_Attendance.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        makeSureXMLDataSisIsLoaded();


        int Myindex = Sis_XMLParserClass.q8.indexOf(cardID); //because the index of object where it contains the myemail from db is equal to data from server or local file
        if (Myindex == -1) {
            showEmailPopup(cardID);
        } //else if myindex != -1
        else {
            //if card already registered,

            String MyEmail = Sis_XMLParserClass.q2.get(Myindex);
            String MyFirstName = Sis_XMLParserClass.q3.get(Myindex);
            String MyLastName = Sis_XMLParserClass.q4.get(Myindex);
            String Residence = Sis_XMLParserClass.q13.get(Myindex);
            String Room = Sis_XMLParserClass.q14.get(Myindex);
            String Apartment = Sis_XMLParserClass.q12.get(Myindex);
            String Point = Sis_XMLParserClass.q16.get(Myindex);
            String Course = lastCourse;

            // check if not already signed the attendance, then send http post to google form,
            if (TempTrackList.indexOf(MyEmail) == -1) {
                //send http post
                HttpRequestApp.add_Course_Attendance(cardID, MyEmail, MyFirstName, MyLastName, Course);
                TempTrackList.add(MyEmail);

                Nfc_MainActivity_Attendance.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Nfc_MainActivity_Attendance.this, "Your attendance has been successfully recorded", Toast.LENGTH_SHORT).show();
                    }
                });
                vibe.vibrate(100);
                mp_On.start();

            } else {
                //show message already signed
                Nfc_MainActivity_Attendance.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Nfc_MainActivity_Attendance.this, "You already confirmed your attendance for this course", Toast.LENGTH_SHORT).show();
                    }
                });
                vibe.vibrate(100);
                mp_Off.start();
            }
            String userInfo = dumpUserCardOwner(MyEmail, MyFirstName, MyLastName, Course, Residence, Room, Apartment, Point);
            buildUserViews(userInfo);
        }

    }

    private void makeSureXMLDataSisIsLoaded() {
        if (Sis_XMLParserClass.q8 == null) { //if Sis_XMLParserClass is null, the intent was lunched and jumped the appselect ac
            try {
                new Sis_XMLParserClass();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showEmailPopup(final String cardID) {

        mp_Off.start();
        vibe.vibrate(100);
        Log.d("hello", "card not registered");

        final String Titlelabel = "Enter your email address";//because we need to put the label to put on the dialog

        NameItemArrayString = MakeArrayList();

        Nfc_MainActivity_Attendance.this.runOnUiThread(new Runnable() {
            public void run() {

                ArrayAdapter<String> NamemyAutoCAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.nfc_simple_textview,
                        NameItemArrayString);

                LayoutInflater inflater = Nfc_MainActivity_Attendance.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.nfc_autocompletetextview, null);
                final AutoCompleteTextView modifyET = (AutoCompleteTextView) dialogView.findViewById(R.id.myAutoCompleteTV);

                modifyET.setAdapter(NamemyAutoCAdapter);
                modifyET.setHint("studentXX@alustudent.com");
                modifyET.setSelection(modifyET.getText().length()); // set the cursor at the end of the edittext immediately
                modifyET.setTextColor(getResources().getColor(R.color.black));

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Nfc_MainActivity_Attendance.this)
                        .setTitle(Titlelabel)
                        .setView(dialogView).setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // mProgress.show(); //start upload
                                        String Email = modifyET.getText().toString();

                                        int EmailMyindex = Sis_XMLParserClass.q2.indexOf(Email);
                                        if (EmailMyindex == -1) {
                                            Toast.makeText(Nfc_MainActivity_Attendance.this, "Email not found", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        //if email found
                                        else {
                                            hasFinishedNewIdRegistration = false;
                                            int ColumnID = 10; //because the spread require the column number to update
                                            int RowID = Integer.parseInt(Sis_XMLParserClass.q1.get(EmailMyindex)); //because q1 is the row number
                                            UpdateCell(RowID, ColumnID, cardID);

                                            //setting new Card ID
                                            Sis_XMLParserClass.q8.set(EmailMyindex, cardID);

                                        }
                                        attendance(cardID); //recursion, we just made sure the card is regitered to one email address
                                    }//end onClisk yes

                                }//end on yes clicked           //end of if yes clicked on alert dialog
                        );

                dialogBuilder.create().show();   //show the alert dialog

            }//end run on UI thread
        });

        Log.d("hello", "card not registered have shown alert dialog");

    }

    //create arraylist for textview autocomplete adapter
    private ArrayList<String> MakeArrayList() {
        ArrayList<String> tempArrayList = new ArrayList<String>();
        for (int i = 0; i < Sis_XMLParserClass.q1.size(); i++) {
            //if card id is  null or card id is empty (removing people already having a card
            // if (Sis_XMLParserClass.q8.get(i) == null || Sis_XMLParserClass.q8.get(i).equals("")) {
            tempArrayList.add(Sis_XMLParserClass.q2.get(i));// add email
            // }
        }
        return tempArrayList;
    }

    private String dumpUserCardOwner(String myEmail,
                                     String myFirstName,
                                     String myLastName,
                                     String course,
                                     String residence,
                                     String room, String apartment, String point) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID Email: ").append(myEmail).append('\n');
        sb.append("ID First Name: ").append(myFirstName).append('\n');
        sb.append("ID Last Name: ").append(myLastName).append('\n');
        sb.append("ID Course: ").append(course).append('\n');
        sb.append("ID Residence: ").append(residence).append('\n');
        sb.append("ID Room: ").append(room).append('\n');
        sb.append("ID Apartment: ").append(apartment).append('\n');
        sb.append("ID Points: ").append(point).append('\n');
        return sb.toString();

    }

    void buildUserViews(String aboutUserOwner) {

        final LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout content = linearLayoutContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        Date now = new Date();
        Log.d("hello", "record UI building display...");
        final TextView timeView = new TextView(this);
        timeView.setText(TIME_FORMAT.format(now));
        timeView.setTextSize(20);
        timeView.setTextColor(getResources().getColor(R.color.blue));


        final RoundedImageView profile = new RoundedImageView(this);
        profile.setImageResource(R.drawable.sis_profil_neutral);


        final TextView text = (TextView) inflater.inflate(R.layout.nfc_tag_text, content, false);
        text.setText(aboutUserOwner);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                content.addView(timeView, 0);
                content.addView(profile, 1);
                content.addView(text, 2);
                content.addView(inflater.inflate(R.layout.nfc_tag_divider, content, false), 3);

            }
        });
    }


    private String dumpTagDataIdDec(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append(toDec(id));
        return sb.toString();
    }


    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";
                try {
                    MifareClassic mifareTag;
                    try {
                        mifareTag = MifareClassic.get(tag);
                    } catch (Exception e) {
                        // Fix for Sony Xperia Z3/Z5 phones
                        tag = cleanupTag(tag);
                        mifareTag = MifareClassic.get(tag);
                    }
                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private Tag cleanupTag(Tag oTag) {
        if (oTag == null)
            return null;

        String[] sTechList = oTag.getTechList();

        Parcel oParcel = Parcel.obtain();
        oTag.writeToParcel(oParcel, 0);
        oParcel.setDataPosition(0);

        int len = oParcel.readInt();
        byte[] id = null;
        if (len >= 0) {
            id = new byte[len];
            oParcel.readByteArray(id);
        }
        int[] oTechList = new int[oParcel.readInt()];
        oParcel.readIntArray(oTechList);
        Bundle[] oTechExtras = oParcel.createTypedArray(Bundle.CREATOR);
        int serviceHandle = oParcel.readInt();
        int isMock = oParcel.readInt();
        IBinder tagService;
        if (isMock == 0) {
            tagService = oParcel.readStrongBinder();
        } else {
            tagService = null;
        }
        oParcel.recycle();

        int nfca_idx = -1;
        int mc_idx = -1;
        short oSak = 0;
        short nSak = 0;

        for (int idx = 0; idx < sTechList.length; idx++) {
            if (sTechList[idx].equals(NfcA.class.getName())) {
                if (nfca_idx == -1) {
                    nfca_idx = idx;
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        oSak = oTechExtras[idx].getShort("sak");
                        nSak = oSak;
                    }
                } else {
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        nSak = (short) (nSak | oTechExtras[idx].getShort("sak"));
                    }
                }
            } else if (sTechList[idx].equals(MifareClassic.class.getName())) {
                mc_idx = idx;
            }
        }

        boolean modified = false;

        if (oSak != nSak) {
            oTechExtras[nfca_idx].putShort("sak", nSak);
            modified = true;
        }

        if (nfca_idx != -1 && mc_idx != -1 && oTechExtras[mc_idx] == null) {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx];
            modified = true;
        }

        if (!modified) {
            return oTag;
        }

        Parcel nParcel = Parcel.obtain();
        nParcel.writeInt(id.length);
        nParcel.writeByteArray(id);
        nParcel.writeInt(oTechList.length);
        nParcel.writeIntArray(oTechList);
        nParcel.writeTypedArray(oTechExtras, 0);
        nParcel.writeInt(serviceHandle);
        nParcel.writeInt(isMock);
        if (isMock == 0) {
            nParcel.writeStrongBinder(tagService);
        }
        nParcel.setDataPosition(0);

        Tag nTag = Tag.CREATOR.createFromParcel(nParcel);

        nParcel.recycle();

        return nTag;
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout content = linearLayoutContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        Date now = new Date();
        List<ParsedNdefRecord> records = Nfc_NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        Log.d("hello", "record size is: " + String.valueOf(size));
        for (int i = 0; i < size; i++) {
            TextView timeView = new TextView(this);
            timeView.setText(TIME_FORMAT.format(now));
            content.addView(timeView, 0);
            ParsedNdefRecord record = records.get(i);
            content.addView(record.getView(this, inflater, content, i), 1 + i);
            content.addView(inflater.inflate(R.layout.nfc_tag_divider, content, false), 2 + i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mTags.size() == 0) {
            Toast.makeText(this, R.string.nothing_scanned, Toast.LENGTH_LONG).show();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menu_main_clear:
                clearTags();
                return true;
            case R.id.menu_copy_hex:
                copyIds(getIdsHex());
                return true;
            case R.id.menu_copy_reversed_hex:
                copyIds(getIdsReversedHex());
                return true;
            case R.id.menu_copy_dec:
                copyIds(getIdsDec());
                return true;
            case R.id.menu_copy_reversed_dec:
                copyIds(getIdsReversedDec());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearTags() {
        mTags.clear();
        for (int i = linearLayoutContent.getChildCount() - 1; i >= 0; i--) {
            View view = linearLayoutContent.getChildAt(i);
            if (view.getId() != R.id.tag_viewer_text) {
                linearLayoutContent.removeViewAt(i);
            }
        }
    }

    private void copyIds(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("NFC IDs", text);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(this, mTags.size() + " IDs copied", Toast.LENGTH_SHORT).show();
    }

    private String getIdsHex() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toHex(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString().replace(" ", "");
    }

    private String getIdsReversedHex() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toReversedHex(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString().replace(" ", "");
    }

    private String getIdsDec() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toDec(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString();
    }

    private String getIdsReversedDec() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toReversedDec(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("hello", "on new intent");
        setIntent(intent);
        resolveIntent(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("hello", "on destroy");
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


}


///////

