package com.tolotranet.livecampus.Food;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.tolotranet.livecampus.R;


/**
 * Created by Tolotra Samuel on 18/08/2016.
 */
public class FoodCalendar extends Activity {

    CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodcalendar);



        calendar = (CalendarView) findViewById(R.id.foodcalendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //DO STUFF with the data. here it is toasting
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

                Intent i = new Intent(FoodCalendar.this, FoodSelect.class);
                i.putExtra("date", dayOfMonth + "/" + month + "/" + year);
                startActivity(i);
            }
        });

    }
}