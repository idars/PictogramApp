package com.d24.android.pictogramapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.d24.android.pictogramapp.R;

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
				SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putBoolean(getString(R.string.preference_value_firstrun), false);
				editor.apply();

				setResult(RESULT_OK);
				finish();
			}
		});
    }

}
