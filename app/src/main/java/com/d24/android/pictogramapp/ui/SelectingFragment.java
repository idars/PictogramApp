package com.d24.android.pictogramapp.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

    // Container Activity must implement this interface
    public interface PictogramSelectedListener {
        public void onItemSelected(long item_id);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_pictogram_select, container,false);
        gridView = (GridView)  view.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, getData());
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

    // Prepare array data for gridview
    private ArrayList<Drawable> getData() {
        final ArrayList<Drawable> imageItems = new ArrayList<>();
        Resources res = getResources();
        TypedArray image_ids = res.obtainTypedArray(R.array.image_ids);

        for (int i = 0; i < image_ids.length(); i++) {
            Drawable drawable = image_ids.getDrawable(i);
            imageItems.add(drawable);


        }
        return imageItems;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (PictogramSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}