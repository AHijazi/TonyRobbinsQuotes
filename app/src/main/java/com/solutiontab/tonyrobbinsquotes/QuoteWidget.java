package com.solutiontab.tonyrobbinsquotes;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.solutiontab.tonyrobbinsquotes.data.QuoteContract;

import java.util.Random;

/**
 * Created by Abdurrahman Hijazi on 18-Mar-15.
 */

public class QuoteWidget extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                QuoteWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        for (int widgetId : allWidgetIds) {
            // provide data
            Cursor c = context.getContentResolver().query(QuoteContract.QuoteEntry.buildQuoteUri(), null, null, null, null);

            Random r = new Random();
            int i1 = r.nextInt(c.getCount() - 1) + 1;
            c.moveToPosition(i1);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            // Log.w("WidgetExample", c.getString(1).replaceAll("\'",""));
            // Set the text
            remoteViews.setTextViewText(R.id.quote_tv, c.getString(1).replaceAll("\'", ""));
            // remoteViews.setImageViewResource(R.id.topic_iv, );

            // Register an onClickListener
            Intent intent = new Intent(context, QuoteViewActivity.class);
            intent.putExtra("qID", c.getString(0));
            c.close();

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.widget_lay, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}