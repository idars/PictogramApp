package com.d24.android.pictogramapp.ui;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.fragment.EditingFragment;
import com.d24.android.pictogramapp.fragment.SelectingFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StagingActivity extends AppCompatActivity {

	private ImageView img;

	EditingFragment editingFragagment;
	SelectingFragment selectingFragagment;

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

		// TODO, Over here nich! Start transaction
		FrameLayout fr = (FrameLayout) findViewById(R.id.fragment_selection);
		fr.setVisibility(View.GONE);

		editingFragagment	=new EditingFragment();//create the fragment instance for the top fragment
		selectingFragagment =new SelectingFragment();//create the fragment instance for the bottom fragment
		String editingFragmentTag = "Editing_tag";
		String selectingFragmentTag = "Selecting_tag";

		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
		transaction.add(R.id.fragment_editing, editingFragagment).addToBackStack(editingFragmentTag);
		transaction.add(R.id.fragment_selection, selectingFragagment);//.addToBackStack(selectingFragmentTag);
		transaction.hide(selectingFragagment);

		transaction.commit();

	}


	public void selectClick(View view){

		FrameLayout fr = (FrameLayout) findViewById(R.id.fragment_selection);
		fr.setVisibility(View.VISIBLE);

		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
		//transaction.hide(editingFragagment);
		transaction.show(selectingFragagment);
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}