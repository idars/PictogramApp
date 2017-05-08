package com.d24.android.pictogramapp.ui;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.stickerview.StickerImageView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class EditingFragment extends Fragment {

    View view;
    FrameLayout canvas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_stage_editing, container,false);

        canvas = (FrameLayout) view.findViewById(R.id.canvasView);
        canvas.setClickable(true);
        canvas.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int count = canvas.getChildCount();
                StickerImageView currSticker;
                for (int i = 0; i < count; i++) {
                    View currView = canvas.getChildAt(i);
                    if (currView instanceof StickerImageView) {
                        currSticker = (StickerImageView) currView;
                        currSticker.setControlItemsHidden(true);
                    }
                }

                Log.d(TAG, "clicked on framelayout");
            }
        });

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