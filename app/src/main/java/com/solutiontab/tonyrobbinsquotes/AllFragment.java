package com.solutiontab.tonyrobbinsquotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.solutiontab.tonyrobbinsquotes.data.QuoteContract;

/**
 * Created by Abdurrahman Hijazi on 04-Mar-15.
 */
public class AllFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final int ALL_LOADER = 0;
    GridView gv;
    Context context;
    String topicSelection;
    private AllAdapter mAllAdapter;
    public AllFragment() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((Callback) getActivity()).removingAnotherFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        topicSelection = getArguments().getString("topicSelection");
        mAllAdapter = new AllAdapter(getActivity(), null, 0);


        if (Integer.parseInt(topicSelection) > 10) {
            ((MainActivity) getActivity())
                    .setActionBarTitle("Favourite Quotes");


        } else if (Integer.parseInt(topicSelection) > 0) {
            Cursor c = getActivity().getContentResolver().query(QuoteContract.TopicEntry.buildTopicUri(), null, QuoteContract.TopicEntry._ID + " = " + topicSelection, null, null);
            c.moveToFirst();

            ((MainActivity) getActivity())
                    .setActionBarTitle(c.getString(1).replaceAll("\'", "") + " Quotes");
            ;
            c.close();
        } else {
            ((MainActivity) getActivity())
                    .setActionBarTitle("All Quotes");
        }
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);


        ((Callback) getActivity()).settingAnotherFrag(topicSelection);


        gv = (GridView) rootView.findViewById(R.id.all_gv);
        gv.setAdapter(mAllAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                Cursor c = mAllAdapter.getCursor();
                c.moveToPosition(position);
                String q_id = c.getString(0);
                Intent i = new Intent(getActivity(), QuoteViewActivity.class);
                i.putExtra("qID", q_id);
                startActivity(i);

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ALL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (Integer.parseInt(topicSelection) > 10) {
            return new CursorLoader(getActivity(), QuoteContract.QuoteEntry.buildQuoteUri(), null, QuoteContract.QuoteEntry.COLUMN_FAVOURITE + " = 1", null, null);
        } else if (Integer.parseInt(topicSelection) > 0) {
            return new CursorLoader(getActivity(), QuoteContract.QuoteEntry.buildQuoteUri(), null, QuoteContract.QuoteEntry.COLUMN_QUOTE_CATEGORY + " = " + topicSelection, null, null);
        } else {
            return new CursorLoader(getActivity(), QuoteContract.QuoteEntry.buildQuoteUri(), null, null, null, null);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAllAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAllAdapter.swapCursor(null);
    }

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


    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void settingAnotherFrag(String topicSelection);

        public void removingAnotherFrag();
    }
}
