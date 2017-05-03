package com.d24.android.pictogramapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d24.android.pictogramapp.fragment.EditingFragment;
import com.d24.android.pictogramapp.fragment.SelectingFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StagingActivity extends AppCompatActivity {

	private ImageView img;
	private ViewGroup rootLayout;
	private int _xDelta;
	private int _yDelta;
	EditingFragment editingFragagment;
	SelectingFragment selectingFragagment;

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */

	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * Some older devices needs a small delay between UI widget updates
	 * and a change of the status and navigation bar.
	 */
	private static final int UI_ANIMATION_DELAY = 300;
	private final Handler mHideHandler = new Handler();
	private View mContentView;
	private final Runnable mHidePart2Runnable = new Runnable() {
		@SuppressLint("InlinedApi")
		@Override
		public void run() {
			// Delayed removal of status and navigation bar

			// Note that some of these constants are new as of API 16 (Jelly Bean)
			// and API 19 (KitKat). It is safe to use them, as they are inlined
			// at compile-time and do nothing on earlier devices.
			mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
	};
	private View mControlsView;
	private final Runnable mShowPart2Runnable = new Runnable() {
		@Override
		public void run() {
			// Delayed display of UI elements
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.show();
			}
			mControlsView.setVisibility(View.VISIBLE);
		}
	};
	private boolean mVisible;
	private final Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			hide();
		}
	};
	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_staging);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
		}

		/* TODO, MOVE TO FRAGMENT */
		/*mVisible = true;
		mControlsView = findViewById(R.id.fullscreen_content_controls);
		mContentView = findViewById(R.id.fullscreen_content);


		// Set up the user interaction to manually show or hide the system UI.
		mContentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				toggle();
			}
		});

		rootLayout = (ViewGroup) findViewById(R.id.view_root);
		img = (ImageView) rootLayout.findViewById(R.id.fullscreen_content);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
		img.setLayoutParams(layoutParams);
		img.setOnTouchListener(new ChoiceTouchListener());

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
*/
		// TODO, Over here nich! Start transaction
		editingFragagment	=new EditingFragment();//create the fragment instance for the top fragment
		selectingFragagment =new SelectingFragment();//create the fragment instance for the bottom fragment
		String editingFragmentTag = "Editing_tag";
		String selectingFragmentTag = "Selecting_tag";

		// TODO, keep FragmentManager as class variable?, it is used in selectClick
		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction

		transaction.add(R.id.fragment_editing, editingFragagment).addToBackStack(editingFragmentTag);
		transaction.add(R.id.fragment_selection, selectingFragagment).addToBackStack(selectingFragmentTag);

		transaction.hide(selectingFragagment);
		transaction.commit();




	}


	public void selectClick(View view){

		int idTop = R.id.fragment_editing;
		int idMid = R.id.fragment_selection;

		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
		transaction.hide(editingFragagment);
		transaction.show(selectingFragagment);
		transaction.commit();
	}





	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
        Log.i("D-bug", "onPostCreate");

    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("D-bug", "onOptionsItemSelected");
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toggle() {
        Log.i("D-bug", "toggle");

        if (mVisible) {
			hide();
		} else {
			show();
		}
	}

	private void hide() {
        Log.i("D-bug", "hide");
		//TODO, Hide peeps
		/*
        // Hide UI first
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
		mControlsView.setVisibility(View.GONE);
		mVisible = false;

		// Schedule a runnable to remove the status and navigation bar after a delay
		mHideHandler.removeCallbacks(mShowPart2Runnable);
		mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);*/
	}

	@SuppressLint("InlinedApi")
	private void show() {
		Log.i("D-bug", "show");

		// Show the system bar
		mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		mVisible = true;

		// Schedule a runnable to display UI elements after a delay
		mHideHandler.removeCallbacks(mHidePart2Runnable);
		mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
	}

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		Log.i("D-bug", "delayedHide");
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
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
			Log.i("D-bug", "invalidate");

			rootLayout.invalidate();
			return true;
		}
	}
}
