package com.d24.android.pictogramapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;




public class MainActivity extends AppCompatActivity {

	private static final int SHOW_INTRODUCTION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Starts SceneActivity using Intent
				gotoStagingActivity();	// eventuelt gotoTutorialActivity();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Check if the app is started for the first time
		SharedPreferences pref = getSharedPreferences(
				getString(R.string.preference_file_key), MODE_PRIVATE
		);

		if (pref.getString(getString(R.string.preference_value_username), null) == null) {
			Intent intent = new Intent(this, IntroActivity.class);
			startActivityForResult(intent, SHOW_INTRODUCTION);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SHOW_INTRODUCTION) {
			if (resultCode != RESULT_OK) {
				// Welcome screen aborted; close the app
				finish();
			}
		}
	}

	public void gotoStagingActivity() {
		Intent intent = new Intent(this, StagingActivity.class);
		startActivity(intent);
	}

	public void gotoIntroActivity() {
		Intent intent = new Intent(this, IntroActivity.class);
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
