package com.tolotranet.livecampus.Gift;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tolotranet.livecampus.App.App_Tools;
import com.tolotranet.livecampus.HttpRequest;
import com.tolotranet.livecampus.R;
import com.tolotranet.livecampus.Sign.Sign_DatabaseHelper;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Created by Tolotra Samuel on 18/08/2016.
 */


public class Gift_App extends AppCompatActivity {


    ArrayAdapter<CharSequence> maintZendeskLocationAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    String Uemail;
    String col1_s = "";//author
    String col2_s = "";//recipient
    String col3_s = "";//action
    String col4_s = "";//object
    String col5_s = "";//score
    String col6_s;  //currency
    final String myTag = "DocsUpload";
    Boolean hasWaitedForGift = true;
    SimpleDateFormat formatter;
    int finalN = 30;

    private Gift_AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_app);
        //alarm = new Gift_AlarmManagerBroadcastReceiver();

        final Button GetDailyGiftBtn = (Button) findViewById(R.id.GetDailyGiftBtn);
        Sign_DatabaseHelper helper = new Sign_DatabaseHelper(this);
        String nextGiftDate = helper.getNextGiftDate();
        Calendar calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy h:mm:ss");

        String currentDate = formatter.format(calendar.getTime());
//        String datadb;

        final Thread buttonTimerThread = new Thread() {

            @Override
            public void run() {

                try {


                    while (0 < finalN) {
                        //Thread.sleep(1000);
                        finalN--;
                        final int n = finalN;
                        final String timeLeft = App_Tools.ConvertSecondToHHMMString(n);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // update TextView here!

                                GetDailyGiftBtn.setText(timeLeft + " Time left before your next gift");

                                Log.d("hello", String.valueOf(n));
                                if (n == 0) {
                                    GetDailyGiftBtn.setText(" CLAIM MY GIFT POINTS");
                                    hasWaitedForGift = true;
                                }

                            } //run ui
                        }); //threadUi
                        Thread.sleep(1000);
                    }//while

                } catch (Exception e) {
                    e.printStackTrace();
                } //try catch

            } //main vote_thread run
        }; // vote_thread scope

        Log.d("hello", "current date is: " + currentDate);
        if (nextGiftDate != null) {
            Log.d("hello", "nextGiftDate date is: " + nextGiftDate);
            if (nextGiftDate.equals("") || nextGiftDate.equals("0")) {
                hasWaitedForGift = true;

            } else if (App_Tools.CompareDates(nextGiftDate, currentDate)) {
                Log.d("hello", "nextGiftDate date is before currentDate: can get gift");
                hasWaitedForGift = true;
            } else {
                hasWaitedForGift = false;

                long sec = App_Tools.DifferenceDates(nextGiftDate, currentDate, TimeUnit.SECONDS);//difference between nextgift date and current date
                finalN = (int) sec;

                Log.d("hello", "nextGiftDate date is after currentDate: cannot get gift yet, number of second left: " + String.valueOf(finalN));
                buttonTimerThread.start();
            }
        } else { //if nextGiftDate from DB is null
            Log.d("hello", "it's null :/ the next gift date");
            hasWaitedForGift = true;
        }


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());//get firebase analytics instance
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);//enable analytics
        mFirebaseAnalytics.setMinimumSessionDuration(3000);//minimum session is 1 minute

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpRequest mReq = new HttpRequest();
                    //Add score +1
                    String scoreUrl = "https://docs.google.com/forms/d/e/1FAIpQLScTo4QaRmnksgipbXf4OwUMBt2eofT2pah52KQLtq2K8TAK0w/formResponse";
                    String data_score =
                            "entry.683575632=" + URLEncoder.encode(col1_s) + "&" + //author
                                    "entry.2052192297=" + URLEncoder.encode(col2_s) + "&" + //recipient
                                    "entry.270042749=" + URLEncoder.encode(col3_s) + "&" + //action
                                    "entry.136521820=" + URLEncoder.encode(col4_s) + "&" + //object
                                    "entry.293560667=" + URLEncoder.encode(col5_s) + "&" + //score
                                    "entry.568849419=" + URLEncoder.encode(col6_s);  //currency
                    String response_score = mReq.sendPost(scoreUrl, data_score);
                    Log.d("response", response_score);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        GetDailyGiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasWaitedForGift) {
                    if (isNetworkAvailable()) {
                        hasWaitedForGift = false;
                        Calendar calendar = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        String currentDate = formatter.format(c.getTime());
                        Sign_DatabaseHelper user = new Sign_DatabaseHelper(Gift_App.this);
                        App_Tools tools = new App_Tools();

                        int seconds = c.get(Calendar.SECOND);
                        int minutes = c.get(Calendar.MINUTE);
                        int hours = c.get(Calendar.HOUR);


                        int add_hours;
                        int add_min = 5;
                        int add_sec = 0;

                        String nextGiftDate = formatter.format(c.getTimeInMillis() + add_min * 1000 * 60 + add_sec * 1000);

                        Uemail = user.getUserEmail();
                        user.updateNextGiftDate(nextGiftDate);

                        Log.d("newDate 1", String.valueOf(c.getTime()));
                        Log.d("newDate 2", String.valueOf(c));
                        //score data
                        col1_s = Uemail;
                        col2_s = Uemail;
                        col3_s = "Claimed the Daily Gift";
                        col4_s = tools.randomString();
                        ;
                        col5_s = String.valueOf(tools.randomInt(1, 10));
                        col6_s = "Normal";


//                    startService(new Intent(getApplication(), Gift_Alarm_Service.class));
//                    startRepeatingTimer();

                        Log.d("time h", String.valueOf(hours));
                        Log.d("time m", String.valueOf(minutes));
                        Log.d("time s", String.valueOf(seconds));

                        //calendar.set(Calendar.HOUR_OF_DAY,hours);
                        //calendar.set(Calendar.MINUTE,minutes+add_min);
                        // calendar.set(Calendar.SECOND,seconds+add_sec); //set the alarm in 30 seconds

                        Intent intent = new Intent(getApplicationContext(), Gift_Notification_Receiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + add_min * 1000 * 60 + add_sec * 1000, pendingIntent);

                        long sec = App_Tools.DifferenceDates(nextGiftDate, currentDate, TimeUnit.SECONDS);//difference between nextgift date and current date
                        finalN = (int) sec;


                        thread.interrupt(); //submit to google form
                        buttonTimerThread.interrupt(); //start timer

                        thread.start(); //submit to google form
                        buttonTimerThread.start(); //start timer


                        new AlertDialog.Builder(Gift_App.this)
                                .setTitle("Congratulations!!!")
                                .setMessage("You have earned " + col5_s + " points for free today. Come back tomorrow to get more gifts.")
                                .setPositiveButton("THANKS", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                    //show the alert dialog
                                }).create().show();


                    } else {
                        Toast.makeText(getBaseContext(), " Cannot Submit ticket ticket while offline. Please try again later", Toast.LENGTH_SHORT).show();
                    }//isnetwork available
                } else {//haswaited
                    Toast.makeText(getBaseContext(), " Yes, your gift is almost ready!", Toast.LENGTH_SHORT).show();
                }//haswaited
            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void startRepeatingTimer() {
        Context context = this.getApplicationContext();
        if (alarm != null) {
            alarm.SetAlarm(context);
            Toast.makeText(context, "Alarm is not null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelRepeatingTimer(View view) {
        Context context = this.getApplicationContext();
        if (alarm != null) {
            alarm.CancelAlarm(context);
        } else {
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onetimeTimer(View view) {
        Context context = this.getApplicationContext();
        if (alarm != null) {
            alarm.setOnetimeTimer(context);
        } else {
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }


}