package com.tolotranet.livecampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Tolotra Samuel on 24/02/2017.
 */

public class Feed_MainActivity extends AppCompatActivity {
    ListView lv;
    TextView post;
    private Feed_MyCustomBaseAdapter myAdapter;
    private ArrayList<Feed_ItemObject> FeedItemArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_main_activity);
        FeedItemArray = MakeArrayList();
        lv = (ListView) findViewById(R.id.feed_main_lv);

        myAdapter = new Feed_MyCustomBaseAdapter(getApplicationContext(), FeedItemArray);
        lv.setAdapter(myAdapter);
lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position ==0){
            post = (TextView) parent.findViewById(R.id.post);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent( Feed_MainActivity.this, Feed_Create.class);
                    startActivity(i);
                }
            });

        }
    }
});

    }


    private ArrayList<Feed_ItemObject> MakeArrayList() {
        ArrayList<Feed_ItemObject> TempItemArray = new ArrayList<Feed_ItemObject>();
        String nullTag = "Update your";

        for (int i = 0; i < 15; i++) {
            Feed_ItemObject CIO = new Feed_ItemObject();
            CIO.setName("");
            TempItemArray.add(CIO);
        }
//        Collections.sort(TempItemArray, new Comparator<Feed_ItemObject>() {
        //          @Override
        //        public int compare(Feed_ItemObject lhs, Feed_ItemObject rhs) {
        //          return ((Integer) rhs.getIndex()).compareTo( lhs.getIndex()); // compare index because its the Feed and must be ordered by weekday
        //    }
//        });
        return TempItemArray;
    }

}
