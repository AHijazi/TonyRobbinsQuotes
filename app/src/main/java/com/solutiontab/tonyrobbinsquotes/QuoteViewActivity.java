package com.solutiontab.tonyrobbinsquotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Abdurrahman Hijazi on 14-Mar-15.
 */
public class QuoteViewActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_view);

        Intent intent = getIntent();
        String sqid = intent.getStringExtra("qID");
        //int qID = Integer.parseInt();

        if (savedInstanceState == null) {

            QuoteViewFragment qvf = new QuoteViewFragment();
            Bundle b = new Bundle();
            b.putString("qID", sqid);
            qvf.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.quote_view_container, qvf, "qvtag")
                    .commit();
        }
    }
}
