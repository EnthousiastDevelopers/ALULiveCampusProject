package com.tolotranet.livecampus;


/**
 * Created by Tolotra Samuel on 06/02/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Gift_AutoStart extends BroadcastReceiver
{
    Gift_Alarm giftAlarm = new Gift_Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            giftAlarm.setAlarm(context);
        }
    }
}