package com.tolotranet.livecampus;


import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tolotra Samuel on 06/02/2017.
 */

public class Gift_Alarm_Service extends Service{

    private Timer timer;
    public int counter=0;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
         initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };
    }
        Gift_Alarm giftAlarm = new Gift_Alarm();
    @Override
        public void onCreate()
        {
            super.onCreate();
startTimer();
            Log.i("in timer", "in timer ++++  "+ (counter++));

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            giftAlarm.setAlarm(this);
            return START_STICKY;
        }

        @Override
        public void onStart(Intent intent, int startId)
        {
            giftAlarm.setAlarm(this);
        }

        @Override
        public IBinder onBind(Intent intent)
        {
            return null;
        }
    }

