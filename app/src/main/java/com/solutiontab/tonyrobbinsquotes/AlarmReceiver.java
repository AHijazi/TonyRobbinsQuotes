package com.solutiontab.tonyrobbinsquotes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.solutiontab.tonyrobbinsquotes.data.QuoteContract;

import java.util.Random;

/**
 * Created by Abdurrahman Hijazi on 18-Mar-15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Cursor c = context.getContentResolver().query(QuoteContract.QuoteEntry.buildQuoteUri(), null, null, null, null);
        Random r = new Random();
        int i1 = r.nextInt(c.getCount() - 1) + 1;
        c.moveToPosition(i1);


        String todaysQuote = c.getString(1).replaceAll("\'", "");
        String todaysQuoteId = c.getString(0);

        c.close();
        SharedPreferences prefs = context.getSharedPreferences("PREFS", Context.MODE_MULTI_PROCESS);
        prefs.edit().remove("today_quote_id");
        prefs.edit().putString("today_quote_id", todaysQuoteId).apply();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.message_box_title))
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.mlogo)
                .setContentText(todaysQuote);
        NotificationCompat.BigTextStyle inboxStyle =
                new NotificationCompat.BigTextStyle();

        inboxStyle.setBigContentTitle(context.getResources().getString(R.string.message_box_title));
        inboxStyle.bigText(todaysQuote);
        inboxStyle.setSummaryText("Tap to see more GREAT quotes!");
        mBuilder.setStyle(inboxStyle);


        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());


        //////////


    }
}