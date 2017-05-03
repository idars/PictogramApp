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


}
