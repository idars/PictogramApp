package com.d24.android.pictogramapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d24.android.pictogramapp.util.GridViewAdapter;
import com.d24.android.pictogramapp.R;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Insert to sharedPreferences
				SharedPreferences pref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean(getString(R.string.preference_value_firstrun), false);
				editor.apply();

				setResult(RESULT_OK);
				finish();
			}
		});
    }

    public static class EditingFragment extends Fragment {

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

    public static class SelectingFragment extends Fragment {

        View view;
        GridView gridView;
        private GridViewAdapter gridAdapter;
        OnHeadlineSelectedListener mCallback;


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

                    String text = "row id of the item clicked: " + l;
                    Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if(mCallback == null){
                        Log.i("D-bug", "callback is null");
                    }
                    mCallback.onItemSelected(l); // TODO, GOOD shit
                    Log.i("D-bug", "SHIts done, " + l);

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


        // Container Activity must implement this interface
        public interface OnHeadlineSelectedListener {
            public void onItemSelected(long item_id);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (OnHeadlineSelectedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }

    }
}
