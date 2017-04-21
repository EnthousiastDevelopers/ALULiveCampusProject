package com.tolotranet.livecampus.Sis.Interact;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.App.App_Tools;
import com.tolotranet.livecampus.R;

import java.util.ArrayList;

public class Interact_MainFragment extends Fragment {

    private final String ARG_SECTION_NUMBER = "section_number";
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog mProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout1;
    private SwipeRefreshLayout mSwipeRefreshLayout2;
    private SwipeRefreshLayout mSwipeRefreshLayout3;

    private ListView toolList;
    // private ListView toolList;
    //private ListView toolList;
    private View rootView = null;

    private ArrayList<Interaction_ItemObject> ContactItemArray1;
    private ArrayList<Interaction_ItemObject> ContactItemArray2;
    private ArrayList<Interaction_ItemObject> ContactItemArray3;

    private Interact_List_CustomBaseAdapter myAdapter1;
    private Interact_List_CustomBaseAdapter myAdapter2;
    private Interact_List_CustomBaseAdapter myAdapter3;

    private Interaction_SendObject selectedObj = null;

    int position = 0; //viewpager position
    private int selectedid = 0; //listview selected position on this fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true); //this prevent the parent viewpager from recreating this fragment on swipe, in order to keep listview selected

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute


        rootView = inflater.inflate(R.layout.interact_fragment_main, null);
        position = getArguments().getInt(ARG_SECTION_NUMBER);
        Log.d("hello", "postition is:" + position);


        // try {

        //  } catch (Exception e) {
        //    Toast.makeText(getActivity().getApplicationContext(), e+" impossible error, if xml interaction data not yet loaded", Toast.LENGTH_SHORT).show();
        //    Log.d("hello", e.getMessage());
        //}
        setUpAllSwipeRefreshViewAndRecycle(position); //setting up SwipeRefreshLayout data inside
        setUpAllListViewAndRecycle(position); //setting up listviews data inside
        return rootView;
    }

    private void setUpAllSwipeRefreshViewAndRecycle(int pos) {
        if (pos == 1) {
            mSwipeRefreshLayout1 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_interactlist);
            mSwipeRefreshLayout1.setOnRefreshListener(new MySwipeRefreshLayoutOnRefreshListener(mSwipeRefreshLayout1));

        } else if (pos == 2) {

            mSwipeRefreshLayout2 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_interactlist);
            mSwipeRefreshLayout2.setOnRefreshListener(new MySwipeRefreshLayoutOnRefreshListener(mSwipeRefreshLayout2));

        } else if (pos == 3) {
            mSwipeRefreshLayout3 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_interactlist);
            mSwipeRefreshLayout3.setOnRefreshListener(new MySwipeRefreshLayoutOnRefreshListener(mSwipeRefreshLayout3));

        }
    }

    private void setUpAllListViewAndRecycle(int pos) {
        if (pos == 1) {
            ContactItemArray1 = MakeArrayList("Professional");
            myAdapter1 = new Interact_List_CustomBaseAdapter(getActivity(), ContactItemArray1, "child");
            toolList = (ListView) rootView.findViewById(R.id.toolList);
            toolList.setAdapter(myAdapter1);
            toolList.setOnItemClickListener(new AllContactListViewClickListener());

        } else if (pos == 2) {
            ContactItemArray2 = MakeArrayList("Friendship");
            myAdapter2 = new Interact_List_CustomBaseAdapter(getActivity(), ContactItemArray2, "child");
            toolList = (ListView) rootView.findViewById(R.id.toolList);
            toolList.setAdapter(myAdapter2);
            toolList.setOnItemClickListener(new AllContactListViewClickListener());

        } else if (pos == 3) {
            ContactItemArray3 = MakeArrayList("Relationship");
            myAdapter3 = new Interact_List_CustomBaseAdapter(getActivity(), ContactItemArray3, "child");
            toolList = (ListView) rootView.findViewById(R.id.toolList);
            toolList.setAdapter(myAdapter3);
            toolList.setOnItemClickListener(new AllContactListViewClickListener());
        }
    }


    public static ArrayList<Interaction_ItemObject> MakeArrayList(String section) {
        ArrayList<Interaction_ItemObject> TempItemArray = new ArrayList<Interaction_ItemObject>();
        Log.d("Makearraylist", section);

        Integer[][] SLimgid = {
                {R.drawable.app_sis, 1},
                {R.drawable.app_event, 2},
                {R.drawable.app_stream, 3},
                {R.drawable.app_book, 4},
                {R.drawable.app_book, 41}
        };
        Integer[][] Opsimgid = {
                {R.drawable.food_ic_chef, 6},
                {R.drawable.app_ic_menu_food, 7},
                {R.drawable.app_food_feedback, 8},
                {R.drawable.app_maint, 9},
                {R.drawable.app_housing_history, 10},
                {R.drawable.app_transp, 11},
        };

        Integer[][] imgid = {
                // { R.drawable.app_mu,12},
                {R.drawable.app_taxi_cab_ic, 13},
                {R.drawable.app_food, 14},
                {R.drawable.app_activity_bowling, 15},
                {R.drawable.app_pharmacy_icon, 16},
                {R.drawable.app_hostpital_building, 17},
                {R.drawable.app_more, 24}

        };

        if (Interaction_XMLParserClass.q1 == null) {
            Log.d("hello", "Too fast, impossible error");
            return null;
        }
        for (int i = 0; i < Interaction_XMLParserClass.q1.size(); i++) {
            if (Interaction_XMLParserClass.q3.get(i).toLowerCase().equals(section.toLowerCase())) {
                Interaction_ItemObject CIO = new Interaction_ItemObject();
                CIO.setName(Interaction_XMLParserClass.q2.get(i));
                CIO.setCategory(Interaction_XMLParserClass.q3.get(i));
                CIO.setId(Integer.parseInt(Interaction_XMLParserClass.q1.get(i)));

                CIO.setImgId(imgid[4][0]);            //Set Img
                CIO.setIndex(i);
                TempItemArray.add(CIO);
            }
        }
//        Collections.sort(TempItemArray, new Comparator<Interaction_ItemObject>() {
//            @Override
//            public int compare(Interaction_ItemObject lhs, Interaction_ItemObject rhs) {
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
            int Index = ((Interaction_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            int ThisId = ((Interaction_ItemObject) arg0.getItemAtPosition(arg2))
                    .getId();

            String ThisName = ((Interaction_ItemObject) arg0.getItemAtPosition(arg2))
                    .getName();
            String Category = ((Interaction_ItemObject) arg0.getItemAtPosition(arg2))
                    .getCategory();

            int ThisImg = ((Interaction_ItemObject) arg0.getItemAtPosition(arg2))
                    .getImgId();
            int ThisMenuId = ((Interaction_ItemObject) arg0.getItemAtPosition(arg2))
                    .getMenuId();

            Interaction_SendObject CIO = new Interaction_SendObject();
            CIO.setId(ThisId);
            CIO.setName(ThisName);
            CIO.setCategory(Category);


            ((Interact_MainActivity) getActivity()).setInteractionTitle(CIO);
            selectedObj = CIO;
            selectedid = Index;


            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Index));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ThisName);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "locked menu");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Bundle params = new Bundle();
            params.putString("image_name", ThisName);
            params.putString("full_text", "locked menu");
            mFirebaseAnalytics.logEvent("share_image", params);


            Log.d("hello", "Position Clicked is " + arg2);
            Log.d("hello", "Item Clicked is " + Index);
            Log.d("hello", "User Clicked is " + ThisId);
            Log.d("hello", "Menu Clicked is " + ThisName);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    private static boolean m_iAmVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

        if (m_iAmVisible) {
            Log.d("hello", "this fragment is now visible" + String.valueOf(position));
            ((Interact_MainActivity) getActivity()).setInteractionTitle(this.selectedObj);
        } else {
            Log.d("hello", "this fragment is now invisible" + String.valueOf(position));
        }
    }

    private class MySwipeRefreshLayoutOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        private SwipeRefreshLayout mSwipeRefreshLayout;

        public MySwipeRefreshLayoutOnRefreshListener(SwipeRefreshLayout mSwipeRefreshLayout) {
            this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        }

        @Override
        public void onRefresh() {
            App_Tools tool = new App_Tools();
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3,
                    R.color.refresh_progress_4);

            if ((tool.isNetworkAvailable(getActivity()))) {
                Interaction_GetDataAsyncTask getDataTask = new Interaction_GetDataAsyncTask("refreshonly") {
                    @Override
                    protected void onPostExecute(Void result) {
                        // TODO Auto-generated method stub
                        setUpAllListViewAndRecycle(position);
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.d("hello", "list " + String.valueOf(position) + "updated! on swipe");
                    }

                };
                getDataTask.execute(getActivity());
            }

        }

    }
}