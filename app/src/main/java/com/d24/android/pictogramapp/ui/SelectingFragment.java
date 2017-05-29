package com.d24.android.pictogramapp.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.util.GridViewAdapter;

import java.util.ArrayList;

public class SelectingFragment extends Fragment {

    View view;
    GridView gridView;
    private GridViewAdapter gridAdapter;
    PictogramSelectedListener mCallback;

    public static SelectingFragment newInstance() {
        return new SelectingFragment();
    }

    // Container Activity must implement this interface
    public interface PictogramSelectedListener {
        public void onItemSelected(long item_id);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_pictogram_select, container,false);
        gridView = (GridView)  view.findViewById(R.id.gridView);

        final ArrayList<Drawable> imageItems = new ArrayList<>();
        final ArrayList<Integer> colorItems = new ArrayList<>();
        Resources res = getResources();
        TypedArray image_colors = res.obtainTypedArray(R.array.image_colors);
        TypedArray image_ids = res.obtainTypedArray(R.array.image_ids);

        for (int i = 0; i < image_ids.length(); i++) {
            Drawable drawable = image_ids.getDrawable(i);
            Integer color = image_colors.getInt(i,0);
            //Log.i("bbb", "f."+color);
            imageItems.add(drawable);
            colorItems.add(color);
        }



        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, imageItems, colorItems);
        gridView.setAdapter(gridAdapter);

        // Configure what happens when an item in the list is clicked
        gridView.setClickable(true);

        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                        mCallback.onItemSelected(l);
                        //Resources res = getResources();
                        //TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
                        //Drawable drawable = image_ids.getDrawable(l);
                        //(ImageView) flyttbarPiktogram.drawable.setImageDrawable(drawable);

                    }
                });
        return view;
    }

    /*
    // Prepare array data for gridview
    private ArrayList<Drawable> getData() {
        final ArrayList<Drawable> imageItems = new ArrayList<>();
        final ArrayList<Integer> colorItems = new ArrayList<>();
        Resources res = getResources();
        TypedArray image_colors = res.obtainTypedArray(R.array.image_colors);
        TypedArray image_ids = res.obtainTypedArray(R.array.image_ids);

        for (int i = 0; i < image_ids.length(); i++) {
            Drawable drawable = image_ids.getDrawable(i);
            Integer color = image_colors.getInt(i,0);
            Log.i("bbb", "f."+color);
            imageItems.add(drawable);
            colorItems.add(color);
        }

        return imageItems;
    }
*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (PictogramSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}