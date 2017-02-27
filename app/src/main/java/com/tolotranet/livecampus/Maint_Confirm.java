package com.tolotranet.livecampus;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tolotra Samuel on 10/10/2016.
 */

public class Maint_Confirm extends Activity {
    String cat, place, des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maint_confirm);


        cat = getIntent().getStringExtra("cat");
        place = getIntent().getStringExtra("place");
        des = getIntent().getStringExtra("des");

        TextView catTV = (TextView) findViewById(R.id.maintCategory);
        TextView desTV = (TextView) findViewById(R.id.maintDescription);
        TextView placeTV = (TextView) findViewById(R.id.maintPlace);


        catTV.setText(cat);
        placeTV.setText(place);
        desTV.setText(des);


        Button calcBtn = (Button) findViewById(R.id.backBtn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Maint_Confirm.this, AppSelect_Parent.class);
                startActivity(i);
            }
        });
    }
}