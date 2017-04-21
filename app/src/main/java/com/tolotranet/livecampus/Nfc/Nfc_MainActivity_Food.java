package com.tolotranet.livecampus.Nfc;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.tolotranet.livecampus.App.App_Tools;
import com.tolotranet.livecampus.HttpRequestApp;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sis.Sis_DetailListItem;
import com.tolotranet.livecampus.Sis.Sis_XMLParserClass;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//////////

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device just discovered.
 */
public class Nfc_MainActivity_Food extends AppCompatActivity
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
    private String foodLabel;
    private boolean hasShownCoursePopupThroughResolveIntent;
    private Vibrator vibe;
    private ArrayList<String> NameItemArrayString;
    private View childLayout;
    private DrawerLayout drawer;
    private ViewPager mViewPager;
    private Nfc_food_page_adapter mSectionsPagerAdapter;
    private int ViewPagerOnScreenLimitNumber = 2;
    private ListView recordList;
    private ArrayList<Nfc_Record_ItemObject> ContactItemArray;
    private Nfc_Record_Custom_BaseAdapter mAdapter;
    private String optionSelected;
    private TextView choiceTV;
    private TextView recordTV;
    private String foodCategorySelected;
    private Spinner categorySP;
    private TabLayout tabLayout;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setContentView(R.layout.tag_viewer);
        Log.d("hello", "on create");
        super.onCreate(savedInstanceState);

        setUpSharedDrawerLayout();
        includeChildLayoutContent();
        setUpLayoutListeners();
        createAbstractElements();

        resolveIntent(getIntent());

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
                        recordThisTag(cardID);
                    }
                },
                NfcAdapter.FLAG_READER_NFC_A |
                        NfcAdapter.FLAG_READER_NFC_B |
                        NfcAdapter.FLAG_READER_NFC_F |
                        NfcAdapter.FLAG_READER_NFC_V |
                        NfcAdapter.FLAG_READER_NFC_BARCODE |
                        NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                null);



        if (!hasShownCoursePopupThroughResolveIntent) {
            mp_Off.start();
            vibe.vibrate(100);
            // showPopupCourserInput("");
        }
        Log.d("hello", "end on create");
    }//end Oncreate



    private void createAbstractElements() {
        vibe = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        mp_On = MediaPlayer.create(Nfc_MainActivity_Food.this, R.raw.wav_success);
        mp_Off = MediaPlayer.create(Nfc_MainActivity_Food.this, R.raw.wav_alert);
        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();
        TempTrackList = new ArrayList<String>();

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true)});


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        tools = new App_Tools();
    }

    private void setUpSharedDrawerLayout() {
        Log.d("hello", "setUpSharedDrawerLayout");

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

    private void includeChildLayoutContent() {
        Log.d("hello", "includeChildLayoutContent");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        childLayout = inflater.inflate(R.layout.nfc_content_main_food, (ViewGroup) findViewById(R.id.content_nfc_main_content));
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.nfc_include_relativeLT_container);
        parentLayout.addView(childLayout, 0);

    }

    private void setUpLayoutListeners() {

        //Parents
        linearLayoutContent = (LinearLayout) findViewById(R.id.list);

        //Childs
        categorySP = (Spinner) childLayout.findViewById(R.id.categorySP);
        choiceTV = (TextView) childLayout.findViewById(R.id.choiceTV);
        recordTV = (TextView) childLayout.findViewById(R.id.recordTV);
        recordTV.setOnClickListener(new OnClickListenerSwapViews());
        choiceTV.setOnClickListener(new OnClickListenerSwapViews());

        mViewPager = (ViewPager) childLayout.findViewById(R.id.container);
        mSectionsPagerAdapter = new Nfc_food_page_adapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(ViewPagerOnScreenLimitNumber);

        optionSelected = (String) mViewPager.getAdapter().getPageTitle(0); //setting default
        mViewPager.addOnPageChangeListener(new MyViewPagerOnPageChangeListener());
        categorySP.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

        tabLayout = (TabLayout) childLayout.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        recordList = (ListView) childLayout.findViewById(R.id.recordLV);
        Log.d("hello", "end includeChildLayoutContent");
    }


    //Resolve intent: read nfc; lookup nfc id with email and name; if not exists request email , find row, update nfc id column; send google form recordThisTag
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

                //Attendance
                final String cardID = dumpTagDataIdDec(tag);
                Log.d("hello", "card ID in Dec is: " + cardID);
                //lookup cardid in sis_xml_parser class

                recordThisTag(cardID);
                mTags.add(tag);
            }

        }// end if NFC Adapter
    } //End resolve intent

    private void recordThisTag(final String cardID) { //this method checks first if the card is linked to an email and then will send the http post

        //checking if course is valid
//        if (foodLabel == null
//                || foodLabel.isEmpty()
//                || foodLabel.equals("Specify a course first")) {
//            hasShownCoursePopupThroughResolveIntent = true;
//            mp_Off.start();
//            vibe.vibrate(100);
//            showPopupCourserInput(cardID); //show alert dialog with course input
//            Log.d("hello", "invalid course");
//            return;
//        }

        //checking internet connection
        if (!isDeviceOnline()) {
            Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
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
            String optionLabel = foodLabel;
            String option = optionSelected;
            String foodCategory = foodCategorySelected;

            // check if not already signed the recordThisTag, then send http post to google form,
            if (TempTrackList.indexOf(MyEmail) == -1) {
                //send http post
                HttpRequestApp.add_Food_Attendance(cardID, MyEmail, MyFirstName, MyLastName, Residence, optionLabel, option, foodCategory);
                TempTrackList.add(MyEmail);

                Nfc_MainActivity_Food.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Nfc_MainActivity_Food.this, "Recorded successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                vibe.vibrate(100);
                mp_On.start();

                Date now = new Date();
                Log.d("hello", "record UI building display...");
                String nowStr = (TIME_FORMAT.format(now));
                Nfc_Record_ItemObject userInfoObject = dumpUserCardOwner(
                        MyEmail, MyFirstName, MyLastName, Residence, Room, Apartment, Point, nowStr, optionLabel, option, foodCategory);

                addToListViewRecord(userInfoObject);

            } else {
                //user already signed, show message already signed only
                Nfc_MainActivity_Food.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Nfc_MainActivity_Food.this, "This person has already eaten", Toast.LENGTH_SHORT).show();
                    }
                });
                vibe.vibrate(100);
                mp_Off.start();
            }

        }

    }


    public static ArrayList<Nfc_Record_ItemObject> MakeArrayList(String section) {
        ArrayList<Nfc_Record_ItemObject> TempItemArray = new ArrayList<Nfc_Record_ItemObject>();
        Log.d("Makearraylist", section);
        return TempItemArray;
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

        Nfc_MainActivity_Food.this.runOnUiThread(new Runnable() {
            public void run() {

                ArrayAdapter<String> NamemyAutoCAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.nfc_simple_textview,
                        NameItemArrayString);

                LayoutInflater inflater = Nfc_MainActivity_Food.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.nfc_autocompletetextview, null);
                final AutoCompleteTextView modifyET = (AutoCompleteTextView) dialogView.findViewById(R.id.myAutoCompleteTV);


                modifyET.setOnItemClickListener(new AdapterView.OnItemClickListener() { // to not allow keyboard hiding the dropdown list
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

                    }

                });
                modifyET.setAdapter(NamemyAutoCAdapter);
                modifyET.setHint("studentXX@alustudent.com");
                modifyET.setSelection(modifyET.getText().length()); // set the cursor at the end of the edittext immediately
                modifyET.setTextColor(getResources().getColor(R.color.black));

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Nfc_MainActivity_Food.this)
                        .setTitle(Titlelabel)
                        .setView(dialogView).setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // mProgress.show(); //start upload
                                        String Email = modifyET.getText().toString();

                                        int EmailMyindex = Sis_XMLParserClass.q2.indexOf(Email);
                                        if (EmailMyindex == -1) {
                                            Toast.makeText(Nfc_MainActivity_Food.this, "Email not found", Toast.LENGTH_SHORT).show();
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
                                        recordThisTag(cardID); //recursion, we just made sure the card is regitered to one email address
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

    private Nfc_Record_ItemObject dumpUserCardOwner(
            String myEmail,
            String myFirstName,
            String myLastName,
            String residence,
            String room,
            String apartment, String point, String nowStr, String optionLabel, String option, String foodCategory) {

        Nfc_Record_ItemObject CIO = new Nfc_Record_ItemObject();

        CIO.setName(myFirstName + " " + myLastName + " \n" + myEmail);
        CIO.setBottomText(residence);
        CIO.setBottomText_2(foodCategory + " - " + option + ": " + optionLabel + " \n" + nowStr);
        CIO.setImgId(R.drawable.sis_profil_neutral);
        CIO.setIndex(2);
        CIO.setUserId(0);
        return CIO;

    }

    void addToListViewRecord(final Nfc_Record_ItemObject aboutUserOwner) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ContactItemArray == null || mAdapter == null) {
                    ContactItemArray = new ArrayList<Nfc_Record_ItemObject>();
                    ContactItemArray.add(aboutUserOwner);
                    mAdapter = new Nfc_Record_Custom_BaseAdapter(getApplicationContext(), ContactItemArray);
                    recordList.setAdapter(mAdapter);
                } else {
                    ContactItemArray.add(0, aboutUserOwner);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private String dumpTagDataIdDec(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append(toDec(id));
        return sb.toString();
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
        StartProcessSpreadSheetRequestAPI();
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
            startCardRegistration();
        }
    }

    private void startCardRegistration() {
        Nfc_registerCard_Sis mSpreadSheetNfcRegister = new Nfc_registerCard_Sis(mCredential, spreadSheetAction, MyRow, ColumnID, NewValue) {
            private Exception mLastError = null;

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
                                Nfc_MainActivity_Food.REQUEST_AUTHORIZATION);
                    } else {
                        Log.d("hello", "The following error occurred:\n"
                                + mLastError.getMessage());
                    }
                } else {
                    Log.d("hello", "Request cancelled.");
                }
            }
        };
        mSpreadSheetNfcRegister.execute(); //because MakeRequestTask contains mService that containts all the new information

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
                Nfc_MainActivity_Food.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
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


    public void setFoodLabel(String lastFood) {
        Log.d("hello", "set foodlabel called " + lastFood);
        this.foodLabel = lastFood;
    }

    private class OnClickListenerSwapViews implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mViewPager.getVisibility() == View.VISIBLE) {
                mViewPager.setVisibility(View.GONE);
            } else {
                mViewPager.setVisibility(View.VISIBLE);
            }
            if (v == null) {
                mViewPager.setVisibility(View.VISIBLE);
            }
        }
    }//end onClicklistener

    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            foodCategorySelected = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class MyViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("hello", "page selected'!!!" + String.valueOf(position));
            optionSelected = (String) mViewPager.getAdapter().getPageTitle(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        ;
    }
}


///////

