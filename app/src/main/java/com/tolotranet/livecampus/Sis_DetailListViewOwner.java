package com.tolotranet.livecampus;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
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
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.FindReplaceRequest;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
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

public class Sis_DetailListViewOwner extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {

    ListView Mainlistview;
    TextView countryNameHeaderSection;
    TextView fullnameHeaderSection;
    Sis_DetailListViewAdapterOwner myPersonDetailListViewAdapter;
    ArrayList<Sis_DetailListItem> DetailList;
    TextView mOutputText;
    GoogleAccountCredential mCredential;
    int Index;
    int userID;
    int MyRow;
    private Button mCallApiButton;
    ProgressDialog mProgress;
    Boolean BoolHasUpdated = false;
    Map<String, String> HasMapLabelValueCoupleOld = new HashMap<String, String>();
    Map<String, String> HasmapUpdateCouple = new HashMap<String, String>();
    Sign_DatabaseHelper helper;
    String MyEmail;
    String spreadSheetAction;
    int ColumnID;
    String NewValue;
    Boolean IWantToComplicateMyLife = false;
    com.github.clans.fab.FloatingActionButton fab_refresh;

    private FirebaseAnalytics mFirebaseAnalytics;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Sheets API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};

    public com.google.api.services.sheets.v4.Sheets mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_detail_list_view);
        Log.d("hello", "owner edit mode has started");



        helper = new Sign_DatabaseHelper(this);

        fab_refresh = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_green);
        fab_refresh.setVisibility(View.VISIBLE);
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDeviceOnline()){
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else {
                    Snackbar.make(view, "Updating.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                if ((isDeviceOnline())) {
                    helper.AllUserObjectToDataBase();
                    Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask(); //because the user might have updated something, the local file needs to syncrhonise with the server
                    getDataTask.synchronize();
                }

                if (AppSelect.origin == null) { //because origin is where the user come from, because the user can come from sis or appselect or drawer
                 finish();
                } else {
                    if (AppSelect.origin.equals("appselect") || AppSelect.origin.equals("signup")) {
                        Intent i = new Intent(Sis_DetailListViewOwner.this, AppSelect.class);
                        startActivity(i);
                    } else {
                        Sis_DetailListViewOwner.super.onBackPressed();
                    }
                }


            }
        });


        MyEmail = helper.getUserEmail();
        int Myindex = Sis_XMLParserClass.q2.indexOf(MyEmail); //because the index of object where it contains the myemail from db is equal to data from server or local file
        MyRow = Integer.parseInt(Sis_XMLParserClass.q1.get(Myindex)); //because q1 is the row number
        Sign_User_Object.Id = MyRow; //
        Sign_User_Object.Rank = Sis_XMLParserClass.q17.get(Myindex); // because q17 is the rank number
        Sign_User_Object.Score = Sis_XMLParserClass.q16.get(Myindex); // because q16 is the score number
        Sign_User_Object.Name = Sis_XMLParserClass.q5.get(Myindex); // because q5 is the fullname

        Sign_User_Object.Apartment = Sis_XMLParserClass.q12.get(Myindex);
        Sign_User_Object.RoomNumber = Sis_XMLParserClass.q14.get(Myindex);
        Sign_User_Object.Residence = Sis_XMLParserClass.q13.get(Myindex);



        Index = Myindex; //because index has been used before even though myindex sounds better


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        Bundle params2 = new Bundle();
        params2.putString("sis ", String.valueOf(MyRow));
        params2.putString("origin ", AppSelect.origin);
        mFirebaseAnalytics.logEvent("sis", params2);
        Log.d("hello", "I am in detail sis owner and I fetched the data in getdataasynctask");


        //first section (profile pic, fullname, country)
        fullnameHeaderSection = (TextView) findViewById(R.id.fullName);
        countryNameHeaderSection = (TextView) findViewById(R.id.country);
        fullnameHeaderSection.setText(Sis_XMLParserClass.q5.get(Index));
        countryNameHeaderSection.setText(Sis_XMLParserClass.q7.get(Index));

        //second section (the rest available)
        DetailList = getPersonalDetails(Index);

        Mainlistview = (ListView) findViewById(R.id.person_details_lv);

        myPersonDetailListViewAdapter = new Sis_DetailListViewAdapterOwner(this,
                DetailList);
        Mainlistview.setAdapter(myPersonDetailListViewAdapter);
        Mainlistview.setOnItemClickListener(new PersonDetailListViewClickListener());

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

    }


    private ArrayList<Sis_DetailListItem> getPersonalDetails(int Index) {
        ArrayList<Sis_DetailListItem> DetailList = new ArrayList<Sis_DetailListItem>();


        Sis_DetailListItem Sis_DetailListItem_Instance = new Sis_DetailListItem();
//         tag = "EmailAddress"; //NO USER EMAIL ABILITY  TO EDIT
//         columnID = 2;
//        Sis_DetailListItem_Instance.setColumnValue(columnID);
//
//        Sis_DetailListItem_Instance.setDetailName(tag);
//        if (!(Sis_XMLParserClass.q2.get(Index).equals(""))) {
//            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q2.get(Index));
//        } else {
//            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
//        }
//        DetailList.add(Sis_DetailListItem_Instance);
//        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q2.get(Index));


        String tag = "Firstname";
        int columnID = 4;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q3.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q3.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q3.get(Index));


        tag = "Lastname";
        columnID = 5;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q4.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q4.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q4.get(Index));

        tag = "Apartment";
        columnID = 16;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q12.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q12.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q12.get(Index));

        tag = "Residence";
        columnID = 14;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q13.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q13.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q13.get(Index));


        tag = "Room";
        columnID = 15;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q14.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q14.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q14.get(Index));


        tag = "Birthday";
        columnID = 7;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q6.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q6.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q6.get(Index));


        tag = "Nationality";
        columnID = 9;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q7.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q7.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q7.get(Index));

        tag = "Major";
        columnID = 8;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q15.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q15.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q15.get(Index));

        tag = "PassportNumber";
        columnID = 10;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q8.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q8.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q8.get(Index));


        tag = "PhoneNumber1";
        columnID = 11;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q9.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q9.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q9.get(Index));

        tag = "PhoneNumber2";
        columnID = 12;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q10.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q10.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q10.get(Index));

        tag = "PhoneNumber3";
        columnID = 13;
        Sis_DetailListItem_Instance = new Sis_DetailListItem();
        Sis_DetailListItem_Instance.setColumnValue(columnID);

        Sis_DetailListItem_Instance.setDetailName(tag);
        if (!(Sis_XMLParserClass.q11.get(Index).equals(""))) {
            Sis_DetailListItem_Instance.setDetailValue(Sis_XMLParserClass.q11.get(Index));
        } else {
            Sis_DetailListItem_Instance.setDetailValue("please updatme me! :-( ");
        }
        DetailList.add(Sis_DetailListItem_Instance);
        HasMapLabelValueCoupleOld.put(tag, Sis_XMLParserClass.q11.get(Index));


        //test
        String x = DetailList.get(2).DetailName;
        int n = DetailList.get(2).getColumnValue();
        Log.d("helloget2", x+" and column id is: "+String.valueOf(n));
        return DetailList;
    }


    public class PersonDetailListViewClickListener implements
            AdapterView.OnItemClickListener {

        private Dialog dialog;

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub

            View parentView = null;
            parentView = Sis_DetailListViewOwner.getViewByPosition(arg2, Mainlistview); //getting the clicked value as how it is displayed on the UI, because the original values might have changed twice
            final TextView BottomTextTV = (TextView) parentView.findViewById(R.id.detail_value); //textview of the value as how it is onscreen at the click event
            final String ValueToModify = BottomTextTV.getText().toString(); //


            final EditText modifyET = new EditText(Sis_DetailListViewOwner.this); //create new edittext programatically
            final String Titlelabel = ((Sis_DetailListItem) arg0.getItemAtPosition(arg2)).getDetailName(); //because we need to put the label to put on the dialog
            ColumnID = ((Sis_DetailListItem) arg0.getItemAtPosition(arg2)).getColumnValue(); //because the spread require the column number to update

            modifyET.setText(ValueToModify);
            modifyET.setSelection(modifyET.getText().length()); // set the cursor at the end of the edittext immediately
            modifyET.setTextColor(getResources().getColor(R.color.black));
            new AlertDialog.Builder(Sis_DetailListViewOwner.this)
                    .setTitle(Titlelabel)
                    .setView(modifyET).setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // mProgress.show(); //start upload
                            NewValue = modifyET.getText().toString();
                            if(NewValue != ValueToModify) { //because if the user didn't change anything, the dialog will just close.

                                spreadSheetAction = "updateCell";
                               // spreadSheetAction = "test"; //because the test is used to just read data and print the result in logcat
                                BottomTextTV.setText(NewValue);
                                StartProcessSpreadSheetRequestAPI(); //because it verify and will pass through all the requiremet
                                if(Titlelabel.equals("Apartment")){
                                    Sign_User_Object.Apartment = NewValue;
                                }
                                if(Titlelabel.equals("Room")){
                                    Sign_User_Object.RoomNumber = NewValue;
                                }
                                if(Titlelabel.equals("Residence")){
                                    Sign_User_Object.Residence = NewValue;
                                }
                            }

                            //UpdateCell(MyRow, ColumnID, NewValue);
                            //end of if yes clicked on alert dialog
                        }
                        //show the alert dialog
                    }).create().show();


        }

    }

    private void UpdateCell(int myRow, int columnID, String newValue) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.person_detail_list_view, menu);
        return true;
    }

    public static View getViewByPosition(int pos, ListView listView) {
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

      if(!IWantToComplicateMyLife) { //because there is a second part in else that update the whole row based on find and replace, changed it to something simpler
          if ((isDeviceOnline())) {
              Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask(); //because the user might have updated something, the local file needs to syncrhonise with the server
              getDataTask.synchronize();
              helper.AllUserObjectToDataBase(); //because it cannot syncronise local database if the server is not updated.
          }

          if (AppSelect.origin == null) { //because origin is where the user come from, because the user can come from sis or appselect or drawer
              Sis_DetailListViewOwner.super.onBackPressed();
          } else {
              if (AppSelect.origin.equals("appselect")) {
                  Intent i = new Intent(this, AppSelect.class);
                  startActivity(i);
              } else {
                  Sis_DetailListViewOwner.super.onBackPressed();
              }
          }
      }else {
          Integer x = Mainlistview.getAdapter().getCount();
          Log.d("helloworld", x.toString());

          View parentView = null;

          for (int i = 0; i < Mainlistview.getAdapter().getCount(); i++) {

              parentView = getViewByPosition(i, Mainlistview);
              //View viewTelefone = Mainlistview.getChildAt(i);
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


                              mProgress = new ProgressDialog(Sis_DetailListViewOwner.this);
                              mProgress.setMessage("Finalising edit ...");
                              mProgress.show();
                              StartProcessSpreadSheetRequestAPI();
                              if (AppSelect.origin == null) {
                                  Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
                                  getDataTask.execute(Sis_DetailListViewOwner.this);
                              } else {
                                  Log.d("helloorigin", AppSelect.origin);
                                  if (AppSelect.origin.equals("appselect")) {
                                      Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
                                      AppSelect.origin = "appselect";
                                      getDataTask.execute(Sis_DetailListViewOwner.this);
//                Intent i = new Intent(this, AppSelect.class); this is set into Sis_getdataasynctask
//                startActivity(i);
                                  } else {
                                      Sis_GetDataAsyncTask getDataTask = new Sis_GetDataAsyncTask();
                                      getDataTask.execute(Sis_DetailListViewOwner.this);
                                  }
                              }
                              // mProgress.hide();
                              //end of if yes clicked on alert dialog
                          }
                          //show the alert dialog
                      }).create().show();
          } else {
              //is BoolHasUpdated == false
              if (AppSelect.origin == null) {
                  Sis_DetailListViewOwner.super.onBackPressed();
              } else {
                  if (AppSelect.origin.equals("appselect")) {
                      Intent i = new Intent(this, AppSelect.class);
                      startActivity(i);
                  } else {
                      Sis_DetailListViewOwner.super.onBackPressed();
                  }
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


    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     *
     * @return List of names and majors
     * @throws IOException
     */
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

        if (spreadSheetAction.equals("updateRow")) {
            //getting memory location
            Integer row = null;
            if (userID < 2000000 && userID > 999999) {  //because student reference IDs are between 1,000,000 and 2,000,000
                row = userID - 1000000;
            }

            for (Map.Entry<String, String> entry : HasmapUpdateCouple.entrySet()) {  //because HasmapUpdateCouple is the couple of new value/old value, setFindreplace will be used

                Log.d("hello HashMapUpdate", entry.getKey() + "/" + entry.getValue());        //check HasMapLabelValueCoupleOld iteration


                requests.add(new Request().setFindReplace(new FindReplaceRequest().setFind(entry.getKey())
                        .setMatchEntireCell(true)
                        .setMatchCase(true)
                        .setReplacement(entry.getValue())
                        .setRange(new GridRange()
                                .setSheetId(0)
                                .setStartRowIndex(row)
                                .setEndRowIndex(row + 1))));
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
                                            .setGreen(Float.valueOf(1)))));


            requests.add(new Request()
                    .setUpdateCells(new UpdateCellsRequest()
                    .setStart(new GridCoordinate()
                            .setSheetId(0)
                            .setRowIndex(MyRow-1) //because the row index start with 0
                            .setColumnIndex(ColumnID-1)) //because the column index start with 0

                    .setRows(Arrays.asList(new RowData().setValues((NewValueCellData))))
                    .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
        }
        Log.d("hello", "column: " + String.valueOf(ColumnID) + ", row: " + String.valueOf(MyRow));
        if (spreadSheetAction.equals("testedit")) {

            List<CellData> valuesL = new ArrayList<>();
            valuesL.add(new CellData()
                    .setUserEnteredValue(new ExtendedValue()
                            .setNumberValue(Double.valueOf(3)))
                    .setUserEnteredFormat(new CellFormat()
                            .setBackgroundColor(new Color()
                                    .setGreen(Float.valueOf(1)))));
            requests.add(new Request()
                    .setUpdateCells(new UpdateCellsRequest()
                            .setStart(new GridCoordinate()
                                    .setSheetId(0)
                                    .setRowIndex(3)
                                    .setColumnIndex(2))
                            .setRows(Arrays.asList(
                                    new RowData().setValues(valuesL)))
                            .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

        }

        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);

      //  String range = "student data!A2:V12";
       // ValueRange response = mService.spreadsheets().values().get(spreadsheetId, range).execute();

        mService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();

        return results;
    }


    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
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
                StartProcessSpreadSheetRequestAPI();
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

    protected void onDestroy() {
        super.onDestroy();
        if (mProgress != null && mProgress.isShowing()) {
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

