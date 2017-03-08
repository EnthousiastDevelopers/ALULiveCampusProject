package com.tolotranet.livecampus.Settings;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sign.Sign_DatabaseHelper;
import com.tolotranet.livecampus.Sign.Sign_check_mail;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by Tolotra Samuel on 15/08/2016.
 */
public class Settings_mainActivity extends Activity {

    private FirebaseAnalytics mFirebaseAnalytics;

    ListView toolList;
    ArrayList<Settings_ItemObject> ContactItemArray;
    Settings_MyCustomBaseAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_select);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

//        String username = getIntent().getStringExtra("Username");
//
//        TextView tv = (TextView) findViewById(R.id.TVusername);
//
//        tv.setText(username);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, AppsItemArray  );

        toolList = (ListView) findViewById(R.id.toolList);
        // toolList.setAdapter(adapter);

        ContactItemArray = MakeArrayList();
        //Mainlistview = (ListView) findViewById(R.id.Contacts_list_view);


        myAdapter = new Settings_MyCustomBaseAdapter(getApplicationContext(),
                ContactItemArray);
        toolList.setAdapter(myAdapter);

        toolList.setOnItemClickListener(new AllContactListViewClickListener());

    }

    private ArrayList<Settings_ItemObject> MakeArrayList() {
        ArrayList<Settings_ItemObject> TempItemArray = new ArrayList<Settings_ItemObject>();
        String nullTag = "Update your";

//        ArrayList<String> AppsItemArray = new ArrayList<String>(
//                Arrays.asList("Food App", "SIS", "Zendesk Form", "Transport Schedule",
//                        "Upcoming Appss", "StreamLive ((Locked))", "Business Area((Locked))", "Student Life((Locked))",
//                        "UWallet ((Locked))", "Developer Area((Locked))", "Art&Photography((Locked))"));

        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(Settings_mainActivity.this);

        Boolean boolKey =helper.check_pass_requirement();
        String ui = "";
        if(!boolKey){
             ui = "No Password check in on launch";
        }else{
             ui = "Always ask";
        }

        String[][] AppsItemArray = new String[][]{
                {"Login Password", ui, "5"},
                {"Logout", "End session", "2"},

        };
        Integer[] imgid = {
                R.drawable.app_icon_lock,
                R.drawable.app_icon_logout,
        };

        for (int i = 0; i < AppsItemArray.length; i++) {
            Settings_ItemObject CIO = new Settings_ItemObject();
            if (!AppsItemArray[i][0].equals("")) {
                //Set Name text
                CIO.setName(AppsItemArray[i][0]);

                //Set Bottom text
                if (AppsItemArray[i][1].equals("")) {
                    CIO.setBottomText("");

                } else {
                    CIO.setBottomText(AppsItemArray[i][1]);
                }
                //set option effect
                if (AppsItemArray[i][2].equals("")) {
                    CIO.setOptionId(0);
                } else {
                    CIO.setOptionId(parseInt(AppsItemArray[i][2]));
                }

                //Set Img id
                if (imgid[i].equals("")) {

                    CIO.setImgId(imgid[4]);
                } else {
                    CIO.setImgId(imgid[i]);
                    // CIO.setImgId(4);
                }

                ;
                CIO.setIndex(i);
              //  CIO.setOptionId(0);
                TempItemArray.add(CIO);
            }
        }
//        Collections.sort(TempItemArray, new Comparator<Settings_ItemObject>() {
//            @Override
//            public int compare(Settings_ItemObject lhs, Settings_ItemObject rhs) {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
        return TempItemArray;
    }

    public class AllContactListViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            int Index = ((Settings_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisOptionId = ((Settings_ItemObject) arg0.getItemAtPosition(arg2))
                    .getOptionId();
            int ThisImg = ((Settings_ItemObject) arg0.getItemAtPosition(arg2))
                    .getImgId();
            String Menu = ((Settings_ItemObject) arg0.getItemAtPosition(arg2))
                    .getName();
            String Availability = ((Settings_ItemObject) arg0.getItemAtPosition(arg2))
                    .getBottomText();

            Bundle bundle2 = new Bundle();
            bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Index));
            bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
            bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "locked menu");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);


            Log.d("hello", "Menu Clicked is " + Menu);
            //open editor profile if the person clicked is the current user
            if (Menu == "Logout") {

//                Intent i = new Intent(Settings_mainActivity.this, Booking_App.class);
//                startActivityPrompt(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "Optionid Clicked is " + ThisOptionId);
                Log.d("hello", "Menu Clicked is " + Menu);
                Sign_DatabaseHelper helper = new Sign_DatabaseHelper(Settings_mainActivity.this);

                helper.cleanTable();

                Intent i = new Intent(Settings_mainActivity.this, Sign_check_mail.class);
                startActivity(i);
            }
            if (Menu == "Login Password") {
                Sign_DatabaseHelper helper = new Sign_DatabaseHelper(Settings_mainActivity.this);
                helper.switch_sign_check();
               boolean x =  helper.check_pass_requirement();
                String ui= "";
                if (!x) {
                    ui = "No Password check in on launch";
                }else{
                    ui = "Always ask";
                }
                View parentView = null;

                parentView = toolList.getChildAt(arg2);
                //View viewTelefone = Mainlistview.getChildAt(i);

                TextView BottomTV = (TextView) parentView.findViewById( R.id.Contact_bottom_tv);

                BottomTV.setText(ui);
               // Intent i = new Intent(Settings_mainActivity.this, Transp_SpreadSheetActivity.class);
               // startActivityPrompt(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "Optionid Clicked is " + ThisOptionId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Availability == "(Locked)") {
                Toast.makeText(getBaseContext(), Menu + " is locked. Please contact the developer to unlocked it", Toast.LENGTH_SHORT).show();
                Log.d("hello", "Img Clicked is " + ThisImg);
            }
        }
    }
    public static int getUserID(){
       int userID = 1000005;
        return userID;
    }

}
