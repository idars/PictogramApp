package com.d24.android.pictogramapp.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CustomViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Log.i("patch_01_frag_order", "CustomViewPager()//");

    }
    /* HAVE TESTED
        public void addTouchables(ArrayList<View> views)
        public void addFocusables(ArrayList<View> views, int direction, int focusableMode)
        protected boolean onRequestFocusInDescendants(int direction,

      */

    @Override // Litt sammenheng onAttach
    protected boolean onRequestFocusInDescendants(int direction,
                                                  Rect previouslyFocusedRect) {

        // Log.i("patch_01_frag_order", "NEW onRequestFocusInDescendants()//");
        return super.onRequestFocusInDescendants(direction,previouslyFocusedRect);
    }





    @Override
    protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        Log.i("patch_01_swipe", "Swipe: " + b);
        this.isPagingEnabled = b
        ;
    }

}