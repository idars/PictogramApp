package com.d24.android.pictogramapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.d24.android.pictogramapp.R;

public class MainActivity extends AppCompatActivity {

	private static final int SHOW_INTRODUCTION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

	}

	@Override
	protected void onStart() {
		super.onStart();

		SharedPreferences pref = getSharedPreferences(
				getString(R.string.preference_file_key), MODE_PRIVATE
		);

		// Check if the app is started for the first time
		if (pref.getBoolean(getString(R.string.preference_value_firstrun), true)) {
			Intent intent = new Intent(this, IntroActivity.class);
			startActivityForResult(intent, SHOW_INTRODUCTION);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SHOW_INTRODUCTION) {
			// Close the app if the user has aborted the introduction sequence
			if (resultCode != RESULT_OK) {
				finish();
			}
		}
	}

	public void stageClick(View view) {
		Log.i("D-bug", "stageClick");
		Intent intent = new Intent(this, StagingActivity.class);
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
