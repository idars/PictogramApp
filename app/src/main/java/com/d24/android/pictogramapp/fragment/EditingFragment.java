package com.d24.android.pictogramapp.fragment;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.d24.android.pictogramapp.GridViewAdapter;
import com.d24.android.pictogramapp.R;

import java.util.ArrayList;

public class EditingFragment extends Fragment {

    View view;
    GridView gridView;
    private GridViewAdapter gridAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_pictogram_select, container,false);
        gridView = (GridView)  view.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        // Configure what happens when an item in the list is clicked
        gridView.setClickable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                String text = "row id of the item clicked: " + 55;
                Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                /*
                        Resources res = getResources();
                        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
                        Drawable drawable = image_ids.getDrawable(l);
                        (ImageView) flyttbarPiktogram.drawable.setImageDrawable(drawable);
                 */
            }
        });

        return view;

    }

    // Prepare array data for gridview
    private ArrayList<Drawable> getData() {
        final ArrayList<Drawable> imageItems = new ArrayList<>();
        Resources res = getResources();
        TypedArray image_ids = res.obtainTypedArray(R.array.image_ids);

        for (int i = 0; i < image_ids.length() ||i < 10; i++) {
            Drawable drawable = image_ids.getDrawable(i);
            imageItems.add(drawable);
        }
        return imageItems;
    }
}
