package com.tolotranet.livecampus.Feed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tolotranet.livecampus.R;

/**
 * Created by Tolotra Samuel on 25/02/2017.
 */

public class Feed_Create extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_create);

        //complete Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Feed_Create.super.onBackPressed();
            }
        });
        //complete Toolbar setup

        Button bt = new Button(this);
        bt.setText("POST");
        bt.setBackgroundColor(getResources().getColor(R.color.red_alu));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;

        bt.setLayoutParams(params);
        toolbar.addView(bt);
    }
}
