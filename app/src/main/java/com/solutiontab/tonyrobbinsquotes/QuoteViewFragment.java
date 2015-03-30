package com.solutiontab.tonyrobbinsquotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.solutiontab.tonyrobbinsquotes.data.QuoteContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Abdurrahman Hijazi on 14-Mar-15.
 */
public class QuoteViewFragment extends Fragment implements View.OnClickListener {
    private final int QUERY_LOADER = 1;
    private final int READ_UPDATE_LOADER = 2;
    private final int FAVOURITE_UPDATE_LOADER = 3;
    int favourite_state;
    String sqID;
    Uri ScreenShotUri;
    ImageView favouriteB;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_quote_view, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.quote_tv);
        ImageView topic_iv = (ImageView) rootView.findViewById(R.id.topic_iv);
        favouriteB = (ImageView) rootView.findViewById(R.id.favouriteButton);
        ImageView shareB = (ImageView) rootView.findViewById(R.id.shareButton);
        shareB.setOnClickListener(this);


        sqID = getArguments().getString("qID", null);

        Cursor c = getActivity().getContentResolver().query(QuoteContract.QuoteEntry.buildQuoteUri(), null, QuoteContract.QuoteEntry._ID + "=" + sqID, null, null);
        c.moveToFirst();

        String topicId = c.getString(3);
        String topicRes = "ct_" + topicId;
        int resourceId = getId(topicRes, R.drawable.class);
        topic_iv.setImageResource(resourceId);


        favourite_state = c.getInt(5);
        if (favourite_state == 0) {
            favouriteB.setImageResource(R.drawable.favourite_b);
        } else {
            favouriteB.setImageResource(R.drawable.unfavourite_b);
        }
        favouriteB.setOnClickListener(this);

        String quoteString = c.getString(1);
        if (quoteString.contains("\'")) {
            quoteString = quoteString.replaceAll("\'", "");
        }
        tv.setText(quoteString);

        if (c.getInt(4) == 0) {

            ContentValues cv = new ContentValues();
            cv.put(QuoteContract.QuoteEntry.COLUMN_READ, "1");
            getActivity().getContentResolver().update(QuoteContract.QuoteEntry.buildQuoteUri(), cv, QuoteContract.QuoteEntry._ID + "=" + sqID, null);
        }

        c.close();



        return rootView;
    }

    private void getScreenShot() {
        File imageFile;
        // image naming and path  to include sd card  appending name you choose for file
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "quotesapp/";
        LinearLayout vg = (LinearLayout) rootView.findViewById(R.id.quote_layout);
//        vg.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        vg.layout(0, 0, vg.getMeasuredWidth(), vg.getMeasuredHeight());
        vg.buildDrawingCache(true);
        vg.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(vg.getDrawingCache());
        vg.setDrawingCacheEnabled(false);


        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }

        ScreenShotUri = Uri.fromFile(new File(String.valueOf(imagePath)));


    }

    public int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favouriteButton:

                if (favourite_state == 0) {
                    favourite_state = 1;
                    favouriteB.setImageResource(R.drawable.unfavourite_b);
                } else {
                    favouriteB.setImageResource(R.drawable.favourite_b);
                    favourite_state = 0;
                }
                ContentValues cv = new ContentValues();
                cv.put(QuoteContract.QuoteEntry.COLUMN_FAVOURITE, String.valueOf(favourite_state));
                getActivity().getContentResolver().update(QuoteContract.QuoteEntry.buildQuoteUri(), cv, QuoteContract.QuoteEntry._ID + "=" + sqID, null);
                break;
            case R.id.shareButton:

                getScreenShot();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, ScreenShotUri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_quote)));


                break;
        }

    }
}
