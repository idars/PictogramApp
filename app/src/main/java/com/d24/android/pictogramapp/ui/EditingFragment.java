package com.d24.android.pictogramapp.ui;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.stickerview.StickerImageView;

import java.util.ArrayList;

public class EditingFragment extends Fragment {

    View view;
    FrameLayout canvas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_stage_editing, container,false);
        canvas = (FrameLayout) view.findViewById(R.id.canvasView);

        return view;

    }

    /* TODO, Nich; Newest edition */
    public void updateImageView(long item_id) {

        //final ArrayList<Drawable> imageItems = new ArrayList<>();
        Resources res = getResources();
        TypedArray image_ids = res.obtainTypedArray(R.array.image_ids);
        Integer intItemId = (int) item_id;

        Drawable drawable = image_ids.getDrawable(intItemId);

        StickerImageView newImg = new StickerImageView(getActivity());
        newImg.setImageDrawable(drawable);
        canvas.addView(newImg);

    }
}