package com.d24.android.pictogramapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d24.android.pictogramapp.R;

public class EditingFragment extends Fragment {

    View view;
    private ViewGroup rootLayout;
    ImageView imageView;
    private int _xDelta;
    private int _yDelta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_stage_editing, container,false);
        rootLayout = (ViewGroup) view.findViewById(R.id.view_root);
        imageView = (ImageView)  view.findViewById(R.id.fullscreen_content);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        imageView.setLayoutParams(layoutParams);

        imageView.setOnTouchListener(new ChoiceTouchListener());

        return view;

    }

    public void updateImageView(long item_id) {
        if(imageView == null){
            imageView = (ImageView)  view.findViewById(R.id.fullscreen_content);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnTouchListener(new ChoiceTouchListener());
        }


    }

    private final class ChoiceTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    //layoutParams.rightMargin = -250;
                    //layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }
}