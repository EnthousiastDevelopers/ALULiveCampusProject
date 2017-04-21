package com.tolotranet.livecampus.Transp;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tolotranet.livecampus.R;

public class Transp_MainActivity extends Activity {

    ArrayList<Transp_ItemObject> ContactItemArray;
    Transp_MyCustomBaseAdapter myAdapter;
    EditText SearchET;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transp_schedule);

        ContactItemArray = MakeArrayList();
        lv = (ListView) findViewById(R.id.Contacts_list_view);
        SearchET = (EditText) findViewById(R.id.SearchET);

        myAdapter = new Transp_MyCustomBaseAdapter(getApplicationContext(),
                ContactItemArray);
        lv.setAdapter(myAdapter);

        MyTextWatcher mytextwatcher = new MyTextWatcher();
        SearchET.addTextChangedListener(mytextwatcher);
        lv.setOnItemClickListener(new AllContactListViewClickListener());

    }

    public class AllContactListViewClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            int Index = ((Transp_ItemObject) arg0.getItemAtPosition(arg2))
                    .getIndex();
            Intent i = new Intent(getApplicationContext(),
                    Transp_ElementDetailListView.class);
            i.putExtra("index", Index);
            Log.d("hello", "Position Clicked is " + arg2);
            Log.d("hello", "Item Clicked is " + Index);
            startActivity(i);
        }

    }

    private ArrayList<Transp_ItemObject> MakeArrayList() {

        ArrayList<Transp_ItemObject> TempItemArray = new ArrayList<Transp_ItemObject>();
        for (int i = 0; i < Transp_XMLParserClass.RouteArray.size(); i++) {
            Transp_ItemObject CIO = new Transp_ItemObject();
            CIO.setName(Transp_XMLParserClass.RouteArray.get(i));
            CIO.setBottomText(Transp_XMLParserClass.TimeArray.get(i));
            CIO.setIndex(i);
            TempItemArray.add(CIO);
        }
        return TempItemArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.UpdateContactsMain:

                if (isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Updating.....", Toast.LENGTH_SHORT).show();
                    Transp_GetDataAsyncTask getDataTask = new Transp_GetDataAsyncTask(Transp_MainActivity.this, "normal");
                    getDataTask.execute(this);
                } else {
                    Toast.makeText(getApplicationContext(), "check Internet Connection", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub
            myAdapter.getFilter().filter(arg0.toString());
        }

    }

}
