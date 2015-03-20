package com.solutiontab.tonyrobbinsquotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TopicsFragment.Callback, AllFragment.Callback {


    boolean mTwoFrag;
    LinearLayout.LayoutParams p;
    LinearLayout fl;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("PREFS", MODE_PRIVATE);

        if (!prefs.getBoolean("first_time", false)) {
            handleNotification();
            prefs.edit().putBoolean("first_time", true).apply();
        }


        if (findViewById(R.id.quote_view_container) != null) {

            mTwoFrag = true;

//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.quote_view_container, new QuoteViewFragment(), "qvtag")
//                        .commit();
//            }
        } else {
            mTwoFrag = false;
        }

        fl = (LinearLayout) findViewById(R.id.quote_view_container);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments


        FragmentManager fragmentManager = getSupportFragmentManager();
        AllFragment all_frag;
        Bundle bundle;

        switch (position) {
            case 0:
                removingAnotherFrag();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new DashboardFragment())
                        .commit();
                break;
            case 1:
                if (mTwoFrag) {
                    p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                    p.weight = 2;
                    fl.setLayoutParams(p);
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.container, new TopicsFragment())
                        .commit();

                break;
            case 2:
                all_frag = new AllFragment();
                bundle = new Bundle();
                bundle.putString("topicSelection", "0");
                all_frag.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, all_frag)
                        .commit();


                break;
            case 3:
                all_frag = new AllFragment();
                bundle = new Bundle();
                bundle.putString("topicSelection", "11");
                all_frag.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, all_frag)
                        .commit();


                break;


        }


    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(String topicSelection) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        AllFragment all_frag = new AllFragment();
        Bundle bundle = new Bundle();
        bundle.putString("topicSelection", topicSelection);
        all_frag.setArguments(bundle);

        if (mTwoFrag) {
            if (mTwoFrag) {
                p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                p.weight = 2;
                fl.setLayoutParams(p);
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.quote_view_container, all_frag)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, all_frag)
                    .addToBackStack("tag")
                    .commit();
        }

    }

    @Override
    public void removingAnotherFrag() {
        if (mTwoFrag) {
            p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            p.weight = 0;
            fl.setLayoutParams(p);
        }
    }

    @Override
    public void settingAnotherFrag(String topicSelection) {


        if (mTwoFrag) {
            p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            if (Integer.parseInt(topicSelection) > 10) {
                p.weight = 0;
            } else if (Integer.parseInt(topicSelection) > 0) {
                p.weight = 2;
            } else {
                p.weight = 0;
            }
            fl.setLayoutParams(p);


        }


    }

    private void handleNotification() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
