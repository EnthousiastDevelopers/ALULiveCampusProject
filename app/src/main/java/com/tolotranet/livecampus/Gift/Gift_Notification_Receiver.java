package com.tolotranet.livecampus.Gift;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.tolotranet.livecampus.R;

/**
 * Created by Tolotra Samuel on 06/02/2017.
 */
public class Gift_Notification_Receiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent fromGiftNotificationIntent = new Intent(context, Gift_App.class);
        fromGiftNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, fromGiftNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.app_ic_gift)
                .setContentTitle("Your Gift is Ready!")
                .setContentText("You can get your point now!")
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000, 50, 2000} )
                .setLights(Color.RED, 3000, 3000)
                .setSound(alarmSound)
                ;
        notificationManager.notify(100, builder.build());


    }
}
