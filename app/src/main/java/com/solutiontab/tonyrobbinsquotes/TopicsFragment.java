package com.solutiontab.tonyrobbinsquotes;

import android.app.Activity;
import android.content.Context;
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
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.solutiontab.tonyrobbinsquotes.data.QuoteContract;

import java.lang.reflect.Field;

/**
 * Created by Abdurrahman Hijazi on 04-Mar-15.
 */
public class TopicsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final int TOPIC_LOADER = 1;
    private MyListAdapter mAdapter;

    public TopicsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter = new MyListAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_topics, container, false);

        ((Callback) getActivity()).settingAnotherFrag("9");

        ListView lv = (ListView) rootView.findViewById(R.id.topics_list);
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ((Callback) getActivity()).onItemSelected(String.valueOf(position + 1));


            }
        });
        ((MainActivity) getActivity())
                .setActionBarTitle("Quote Topics");

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TOPIC_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), QuoteContract.TopicEntry.buildTopicUri(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
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
        public void onItemSelected(String topicSelection);

        public void settingAnotherFrag(String topicSelection);

    }

    public class MyListAdapter extends CursorAdapter {


        public MyListAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {


            View view = LayoutInflater.from(context).inflate(R.layout.topic_list_item, parent, false);
            THolder vHolder = new THolder(view);
            view.setTag(vHolder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            THolder vHolder = (THolder) view.getTag();

            String topicText = cursor.getString(1);
            if (topicText.contains("\'")) {
                topicText = topicText.replaceAll("\'", "");
            }
            vHolder.mTitle.setText(topicText);

            String topicId = cursor.getString(0);
            String topicRes = "topic_" + topicId;
            int resourceId = getId(topicRes, R.drawable.class);
            vHolder.mIcon.setImageResource(resourceId);


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

        private class THolder {
            ImageView mIcon;
            TextView mTitle;

            public THolder(View view) {
                mIcon = (ImageView) view.findViewById(R.id.topic_list_item_iv);
                mTitle = (TextView) view.findViewById(R.id.topic_list_item_tv);
            }
        }


    }

}

