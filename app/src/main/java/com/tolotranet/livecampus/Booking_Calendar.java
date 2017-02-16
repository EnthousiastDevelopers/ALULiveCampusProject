package com.tolotranet.livecampus;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
//import android.support.annotation.RequiresApi;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by Tolotra Samuel on 18/08/2016.
 */
public class Booking_Calendar extends Activity {

    CalendarView calendar;
    Calendar c;
    String weekday;
    String convos;
    String Location;
    String people;
    int n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_calendar);


        Intent i = getIntent();
        convos = i.getStringExtra("convos");
        Location = i.getStringExtra("location");
        people = i.getStringExtra("people");

        c =   Calendar.getInstance();
        n = 1;
        weekday = "Sunday";



        calendar = (CalendarView) findViewById(R.id.foodcalendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           // @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //DO STUFF with the data. here it is toasting

                c.set(year, month, dayOfMonth);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == n) {
                    Intent i = new Intent(Booking_Calendar.this, Booking_Select.class);
                    i.putExtra("date", dayOfMonth + "/" + month + "/" + year);
                    i.putExtra("people", people);
                    i.putExtra("convos", convos);
                    i.putExtra("location", Location);

                    startActivity(i);
                }else{
//                    Toast.makeText(get+ApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "This person is not available on this day. Choose "+ weekday, Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}