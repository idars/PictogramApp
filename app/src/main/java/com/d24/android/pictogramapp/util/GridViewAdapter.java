package com.d24.android.pictogramapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d24.android.pictogramapp.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.drawable = (ImageView) row.findViewById(R.id.image);
            // TODO, NEW


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Drawable item = (Drawable) data.get(position);
		holder.drawable.setImageDrawable(item);

        /*Adjusting the size of items */
        if (!(position == getCount())) {
            /*ViewGroup.LayoutParams layoutParams = holder.drawable.getLayoutParams();
            layoutParams.height = 200;
            layoutParams.width = 200;
            holder.drawable.setLayoutParams(layoutParams);*/
        }


        if (item instanceof ColorDrawable) {
            holder.drawable.setColorFilter(((ColorDrawable) item).getColor());
        } else {
            holder.drawable.setImageDrawable(item);
        }

        return row;
    }

    static class ViewHolder {
        ImageView drawable;
    }
}