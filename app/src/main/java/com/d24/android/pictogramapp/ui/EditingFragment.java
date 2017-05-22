package com.d24.android.pictogramapp.ui;

import android.content.Context;
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
    private OnCanvasTouchedListener mListener;


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
                mListener.onCanvasPressed();

                Log.d(TAG, "clicked on framelayout");
            }
        });

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCanvasTouchedListener) {
            mListener = (OnCanvasTouchedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBackgroundSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateImageView(long item_id) {

        //final ArrayList<Drawable> imageItems = new ArrayList<>();
        if(isAdded()) {
            Resources res = getResources();

            TypedArray image_ids = res.obtainTypedArray(R.array.image_ids);

            Integer intItemId = (int) item_id;

            Drawable drawable = image_ids.getDrawable(intItemId);
            StickerImageView newImg = new StickerImageView(getActivity());
            newImg.setImageDrawable(drawable);
            newImg.setTag(R.integer.tag_resource, intItemId);

            canvas.addView(newImg);
        } else {
            Log.i("Test_22.3", "Fragment not attached");
        }
    }

    public View getView(){
        return view;
    }

    public static EditingFragment newInstance() {
        return new EditingFragment();
    }

    public void copyObject(EditingFragment editingFragment) {
        canvas = editingFragment.canvas;
    }

    public interface OnCanvasTouchedListener {

        void onCanvasPressed();
    }
}