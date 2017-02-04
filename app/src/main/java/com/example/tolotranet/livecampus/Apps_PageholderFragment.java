package com.example.tolotranet.livecampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class Apps_PageholderFragment extends Fragment {

    private  final String ARG_SECTION_NUMBER = "section_number";
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final GridView toolList1;
        final GridView toolList2;
        final GridView toolList3;

        ArrayList<Apps_ItemObject> ContactItemArray1 ;
        ArrayList<Apps_ItemObject> ContactItemArray2 ;
        ArrayList<Apps_ItemObject> ContactItemArray3 ;
        AppSelect_MyCustomBaseAdapter myAdapter1 ;
        AppSelect_MyCustomBaseAdapter myAdapter2;
        AppSelect_MyCustomBaseAdapter myAdapter3 ;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        final View rootView = inflater.inflate(R.layout.apps_fragment_main, container, false);
        int position = getArguments().getInt(ARG_SECTION_NUMBER) ;

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, AppsItemArray  );


        // toolList.setAdapter(adapter);
        Log.d("hello", "postition is:"+ position);

            if(position == 1) {
                ContactItemArray1 = MakeArrayList("StudentLife");
                myAdapter1 = new AppSelect_MyCustomBaseAdapter(getActivity(), ContactItemArray1);
                toolList1 = (GridView) rootView.findViewById(R.id.toolList);
                toolList1.setAdapter(myAdapter1);
                toolList1.setOnItemClickListener(new AllContactListViewClickListener());

            }else if (position == 2){
                ContactItemArray2 = MakeArrayList("Operations");
                myAdapter2 = new AppSelect_MyCustomBaseAdapter(getActivity(), ContactItemArray2);
                toolList2 = (GridView) rootView.findViewById(R.id.toolList);
                toolList2.setAdapter(myAdapter2);
                toolList2.setOnItemClickListener(new AllContactListViewClickListener());

            }else if (position == 3){
                ContactItemArray3 = MakeArrayList("MoreApps");
                myAdapter3 = new AppSelect_MyCustomBaseAdapter(getActivity(), ContactItemArray3);
                toolList3 = (GridView) rootView.findViewById(R.id.toolList);
                toolList3.setAdapter(myAdapter3);
                toolList3.setOnItemClickListener(new AllContactListViewClickListener());

            }

        //lv = (ListView) findViewById(R.id.Contacts_list_view);
        //  toolList.setOnItemClickListener(new AllContactListViewClickListener());

        return rootView;
    }


   
    
    public static ArrayList<Apps_ItemObject> MakeArrayList(String section) {
        ArrayList<Apps_ItemObject> TempItemArray = new ArrayList<Apps_ItemObject>();
        String nullTag = "Update your";
        Integer[] imgid = null;
        String[][] AppsItemArray = null;
        Log.d("Makearraylist", section);
//        ArrayList<String> AppsItemArray = new ArrayList<String>(
//                Arrays.asList("Food App", "SIS", "Zendesk Form", "Transport Schedule",
//                        "Upcoming Appss", "StreamLive ((Locked))", "Business Area((Locked))", "Student Life((Locked))",
//                        "UWallet ((Locked))", "Developer Area((Locked))", "Art&Photography((Locked))"));

        String[][] MoreAppsItemArray = new String[][]{
                {"Mauritius", "All useful information to live in Mauritius"},
                {"FAQ", "Support and Q&A "},
                {"Me", "Edit your profil"},
                {"Settings", "Privacy, Security, Preference"},
        };

        String[][] StudentLifeAppsItemArray = new String[][]{
                {"SIS", "Know all the members of our community"},
                {"Events", "Upcoming Events"},
                {"Student Life Stream", "Messages from Student Life and Head of College"},
                {"Book a meeting", "Set an appointment with staff member"},

        };

        String[][] OpsAppsItemArray = new String[][]{
                {"Food App", "Order food easily"},
                {"Zendesk Form", "Report maintenance issue"},
                {"Transport Schedule", "Easiest way to get the app_transp schedule"},

        };

        Integer[] MoreImgid = {
                R.drawable.app_mu,
                R.drawable.app_faq,
                R.drawable.app_me,
                R.drawable.app_settings,
        };
        Integer[] Opsimgid = {
                R.drawable.app_food,
                R.drawable.app_maint,
                R.drawable.app_transp,
        };
        Integer[] SLimgid = {
                R.drawable.app_sis,
                R.drawable.app_event,
                R.drawable.app_stream,
                R.drawable.app_book,
        };
        if (section == "StudentLife" ) {
            imgid = SLimgid;
            AppsItemArray = StudentLifeAppsItemArray;
        }else
        if (section == "MoreApps")  {
            AppsItemArray = MoreAppsItemArray;
            imgid = MoreImgid;

        }else
        if (section == "Operations") {
            imgid = Opsimgid;
            AppsItemArray = OpsAppsItemArray;

        }

        for (int i = 0; i < AppsItemArray.length; i++) {
            Apps_ItemObject CIO = new Apps_ItemObject();
            if (!AppsItemArray[i][0].equals("")) {
                //Set Name text
                CIO.setName(AppsItemArray[i][0]);

                //Set Img
                if (AppsItemArray[i][1].equals("")) {
                    CIO.setBottomText("");
                } else {
                    CIO.setBottomText(AppsItemArray[i][1]);
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
                CIO.setUserId(0);
                TempItemArray.add(CIO);
            }
        }
//        Collections.sort(TempItemArray, new Comparator<Apps_ItemObject>() {
//            @Override
//            public int compare(Apps_ItemObject lhs, Apps_ItemObject rhs) {
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
            int Index = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisId = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getUserId();
            int ThisImg = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getImgId();
            String Menu = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getName();
            String Availability = ((Apps_ItemObject) arg0.getItemAtPosition(arg2))
                    .getBottomText();

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Index));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "locked menu");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Bundle params = new Bundle();
            params.putString("image_name", Menu);
            params.putString("full_text", "locked menu");
            mFirebaseAnalytics.logEvent("share_image", params);


            Log.d("hello", "Menu Clicked is " + Menu);
            //open editor profile if the person clicked is the current user
            if (Menu == "Book a meeting") {

                Intent i = new Intent(getActivity(), Booking_App.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Food App") {

                Intent i = new Intent(getActivity(), FoodApp.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Transport Schedule") {

                Intent i = new Intent(getActivity(), Transp_SpreadSheetActivity.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Zendesk Form") {

                Intent i = new Intent(getActivity(), MaintZendesk.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "SIS") {

                Intent i = new Intent(getActivity(), Sis_SpreadSheetActivity.class);
                startActivity(i);
                // i.putExtra("myId", userID);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Events") {

                Intent i = new Intent(getActivity(), Event_SpreadSheetActivity.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Settings") {

                Intent i = new Intent(getActivity(), Settings_mainActivity.class);
                startActivity(i);
                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Me") {

                mProgress = new ProgressDialog(getActivity());
                mProgress.setMessage("Loading data ...");
                mProgress.show();
                Sis_startApplicationAsyncTaskOwner myTask = new Sis_startApplicationAsyncTaskOwner();

                myTask.execute(getActivity());


                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "FAQ") {

                Intent i = new Intent(getActivity(), Faq_SpreadSheetActivity.class);
                startActivity(i);

                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }
            if (Menu == "Mauritius") {

                Intent i = new Intent(getActivity(), Mu_App.class);
                startActivity(i);

                Log.d("hello", "Position Clicked is " + arg2);
                Log.d("hello", "Item Clicked is " + Index);
                Log.d("hello", "User Clicked is " + ThisId);
                Log.d("hello", "Menu Clicked is " + Menu);
            }

            if (Availability == "(Locked)") {
                Toast.makeText(getActivity(), Menu + " is locked. Please contact the developer to unlocked it", Toast.LENGTH_SHORT).show();
                Log.d("hello", "Img Clicked is " + ThisImg);
                Bundle bundle2 = new Bundle();
                bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Index));
                bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, Menu);
                bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "locked menu");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);

                Bundle params2 = new Bundle();
                params2.putString("image_name", Menu);
                params2.putString("full_text", "locked menu");
                mFirebaseAnalytics.logEvent("share_image", params2);
            }


        }
    }
}