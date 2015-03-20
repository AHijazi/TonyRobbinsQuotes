package com.solutiontab.tonyrobbinsquotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Abdurrahman Hijazi on 06-Mar-15.
 */
public class AllAdapter extends CursorAdapter {

    public AllAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_grid_item, parent, false);
        ViewHolder vHolder = new ViewHolder(view);
        view.setTag(vHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder vHolder = (ViewHolder) view.getTag();
        String quoteString = cursor.getString(1);

        if (quoteString.contains("\'")) {
            quoteString = quoteString.replaceAll("\'", "");
        }
        vHolder.mQuote_tv.setText(quoteString);


        String topic_id = cursor.getString(3);
        String topicRes = "ct_" + topic_id;


        int resourceId = getId(topicRes, R.drawable.class);

        vHolder.mTopic_iv.setImageResource(resourceId);


        int picStatus = cursor.getInt(4);

        if (picStatus == 1) {
            vHolder.mStatus_iv.setVisibility(View.VISIBLE);
        } else {

        }


    }

    public static class ViewHolder {
        public final ImageView mTopic_iv;
        public final ImageView mStatus_iv;
        public final TextView mQuote_tv;

        public ViewHolder(View view) {
            mTopic_iv = (ImageView) view.findViewById(R.id.topic_iv);
            mStatus_iv = (ImageView) view.findViewById(R.id.status_iv);
            mQuote_tv = (TextView) view.findViewById(R.id.quote_tv);
        }


    }

}
