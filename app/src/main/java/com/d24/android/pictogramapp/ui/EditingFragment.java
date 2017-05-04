package com.d24.android.pictogramapp.ui;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d24.android.pictogramapp.R;

import java.util.ArrayList;

public class EditingFragment extends Fragment {

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    private  Bitmap bmap;
    View view;
    private ViewGroup rootLayout;
    private ImageView currView;
    ImageView imageView;
    private int _xDelta;
    private int _yDelta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_stage_editing, container,false);
        rootLayout = (ViewGroup) view.findViewById(R.id.view_root);
        //imageView = (ImageView)  view.findViewById(R.id.fullscreen_content);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        //imageView.setLayoutParams(layoutParams);
        //imageView.setOnTouchListener(new ChoiceTouchListener());

       // rootLayout.addView(imageView); //TODO, trying to implement several ImageViews

        return view;

    }

    /* TODO, Nich; Newest edition */
    public void updateImageView(long item_id) {

        final ArrayList<Drawable> imageItems = new ArrayList<>();
        Resources res = getResources();
        TypedArray image_ids = res.obtainTypedArray(R.array.image_ids);
        Integer intItemId = (int) item_id;

        Drawable drawable = image_ids.getDrawable(intItemId);

        ImageView newImg = new ImageView(getActivity());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        newImg.setLayoutParams(layoutParams);
        newImg.setScaleType(ImageView.ScaleType.MATRIX);
        newImg.setOnTouchListener(new ChoiceTouchListener());
        newImg.setImageDrawable(drawable);
        rootLayout.addView(newImg);

    }



    private final class ChoiceTouchListener implements View.OnTouchListener {

        public boolean onTouch(View v, MotionEvent event) {
            // handle touch events here
            currView = (ImageView) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    d = rotation(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        matrix.postTranslate(dx, dy);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = (newDist / oldDist);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                        if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
                            newRot = rotation(event);
                            float r = newRot - d;
                            float[] values = new float[9];
                            matrix.getValues(values);
                            float tx = values[2];
                            float ty = values[5];
                            float sx = values[0];
                            float xc = (currView.getWidth() / 2) * sx;
                            float yc = (currView.getHeight() / 2) * sx;
                            matrix.postRotate(r, tx + xc, ty + yc);
                        }
                    }
                    break;
            }

            currView.setImageMatrix(matrix);

            bmap= Bitmap.createBitmap(rootLayout.getWidth(), rootLayout.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bmap);
            rootLayout.draw(canvas);

            return true;
        }
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float s=x * x + y * y;
        return (float)Math.sqrt(s);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}