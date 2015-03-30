package com.solutiontab.tonyrobbinsquotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.solutiontab.tonyrobbinsquotes.data.QuoteContract;

import java.util.Random;

/**
 * Created by Abdurrahman Hijazi on 04-Mar-15.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    TextView tv1;
    TextView tv2;
    TextView tv3;
    String id1;
    String id2 ="";
    String id3;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
//    public  DashboardFragment newInstance(int sectionNumber) {
//        DashboardFragment fragment = new DashboardFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle("Dashboard");

        tv1 = (TextView) rootView.findViewById(R.id.dbquote_tv1);
        tv2 = (TextView) rootView.findViewById(R.id.dbquote_tv2);
        tv3 = (TextView) rootView.findViewById(R.id.dbquote_tv3);
        LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.dblayout1);
        LinearLayout l2 = (LinearLayout) rootView.findViewById(R.id.dblayout2);
        LinearLayout l3 = (LinearLayout) rootView.findViewById(R.id.dblayout3);
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);


        Cursor c = getActivity().getContentResolver().query(QuoteContract.QuoteEntry.buildQuoteUri(), new String[]{QuoteContract.QuoteEntry._ID, QuoteContract.QuoteEntry.COLUMN_QUOTE_TEXT}, null, null, null);
        int range = c.getCount();

        Random r = new Random();

        int i2 = r.nextInt(range - 1) + 1;


        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_MULTI_PROCESS);
        int i1 = Integer.parseInt(prefs.getString("today_quote_id", "1")) - 1;
        c.moveToPosition(i1);
        tv1.setText(c.getString(1).replaceAll("\'", ""));
        id1 = c.getString(0);

        c.moveToPosition(i2);
        tv3.setText(c.getString(1).replaceAll("\'", ""));
        id3 = c.getString(0);
        c.close();

        Cursor  favourite_c = getActivity().getContentResolver().query(QuoteContract.QuoteEntry.buildQuoteUri(), new String[]{QuoteContract.QuoteEntry._ID, QuoteContract.QuoteEntry.COLUMN_QUOTE_TEXT}, QuoteContract.QuoteEntry.COLUMN_FAVOURITE + " = 1", null, null);
        range = favourite_c.getCount();
        if (range > 0) {
            int i3 = r.nextInt((range - 1) + 1) ;
            favourite_c.moveToPosition(i3);
            tv2.setText(favourite_c.getString(1).replaceAll("\'", ""));
            id2 = favourite_c.getString(0);
        } else {
            tv2.setText("Waiting for you to select your favourites!");
        }
        favourite_c.close();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(getActivity(), QuoteViewActivity.class);

        switch (v.getId()) {
            case R.id.dblayout1:
                i.putExtra("qID", id1);
                    startActivity(i);

                break;
            case R.id.dblayout2:
                i.putExtra("qID", id2);
                if (!id2.equals("")) {
                    startActivity(i);
                }
                break;
            case R.id.dblayout3:
                i.putExtra("qID", id3);
                    startActivity(i);
                break;
        }
    }
}
