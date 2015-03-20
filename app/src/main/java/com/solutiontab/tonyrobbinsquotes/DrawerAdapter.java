package com.solutiontab.tonyrobbinsquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Abdurrahman Hijazi on 06-Mar-15.
 */
public class DrawerAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    String[] drawerText;
    int[] image_id;
    private Context mContext;

    public DrawerAdapter(Context c, String[] prgmNameList, int[] topicImages) {

        drawerText = prgmNameList;
        image_id = topicImages;

        mContext = c;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return drawerText.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.drawer_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.drawer_tv);
            ImageView imageView = (ImageView) grid.findViewById(R.id.drawer_iv);
            textView.setText(drawerText[position]);
            imageView.setImageResource(image_id[position]);

        } else {
            grid = (View) convertView;
        }
        return grid;

    }

    public class Holder {
        TextView tv;
        ImageView img;
        ImageView img_status;
    }


}
