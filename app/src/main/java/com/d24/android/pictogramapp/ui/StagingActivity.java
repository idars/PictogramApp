package com.d24.android.pictogramapp.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.stickerview.StickerImageView;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StagingActivity extends AppCompatActivity
		implements SelectingFragment.PictogramSelectedListener,
		           ToolFragment.OnToolClickedListener,
		           BackgroundPickerFragment.OnBackgroundSelectedListener,
					EditingFragment.OnCanvasTouched{

	private ImageView img;
	private static final String BACKGROUND_FRAGMENT_TAG = "BACKGROUND_TAG";
	private static final String SELECTING_FRAGMENT_TAG = "SELECTING_TAG";
	private static final String EDITING_FRAGMENT_TAG = "EDITING_TAG";

	EditingFragment editingFragment;
	SelectingFragment selectingFragment;
	ToolFragment toolFragment;
	BackgroundPickerFragment backgroundPickerFragment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_staging);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			// actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
		}

		/* TODO, MOVE TO FRAGMENT */

		// TODO, Over here nich! Start transaction
		toolFragment = ToolFragment.newInstance();
		editingFragment = new EditingFragment(); // TODO use newInstance()
		selectingFragment = new SelectingFragment();
		backgroundPickerFragment = new BackgroundPickerFragment();

		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
		transaction.add(R.id.frame_layout, editingFragment, EDITING_FRAGMENT_TAG);
		transaction.add(R.id.frame_layout, toolFragment);
		transaction.add(R.id.frame_layout, selectingFragment, SELECTING_FRAGMENT_TAG).addToBackStack(null);
		transaction.add(R.id.frame_layout, backgroundPickerFragment, BACKGROUND_FRAGMENT_TAG).addToBackStack(null);

		transaction.hide(selectingFragment);
		transaction.hide(backgroundPickerFragment);

		transaction.commit();

	}

    @Override
    public void onBackPressed() {
		if (!selectingFragment.isVisible() && !backgroundPickerFragment.isVisible()) {

			String text = "Press the top-left button to complete scene";

			View view = findViewById(R.id.frame_layout);
			Snackbar.make(view, text, Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
		}

		else {
			FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
			FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
			transaction.show(editingFragment);
			transaction.show(toolFragment);
			transaction.hide(selectingFragment);
			transaction.hide(backgroundPickerFragment);
			transaction.commit();
		}


    }

    public void focusEditingFragment(){
		if (selectingFragment.isVisible() || backgroundPickerFragment.isVisible()) {
			FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
			FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
			transaction.show(editingFragment);
			transaction.show(toolFragment);
			transaction.hide(selectingFragment);
			transaction.hide(backgroundPickerFragment);
			transaction.commit();
		}

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

	public void onItemSelected(long item_id)
	{
		// The user selected the headline of an article from the HeadlinesFragment
		// Do something here to display that article

		editingFragment = (EditingFragment)
				getSupportFragmentManager().findFragmentByTag(EDITING_FRAGMENT_TAG);

		if (editingFragment != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			editingFragment.updateImageView(item_id); // TODO
		} else {
			// Otherwise, we're in the one-pane layout and must swap frags...

			// Create fragment and give it an argument for the selected article
			editingFragment = new EditingFragment();
			Bundle args = new Bundle();
			//args.putInt(EditingFragment.ARG_POSITION, item_id);
			editingFragment.setArguments(args);

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment_container view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.add(R.id.frame_layout, editingFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}
	}

	@Override
	public void onAddButtonClicked(View v) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.show(selectingFragment);
		transaction.commit();
	}

	@Override
	public void onBackgroundButtonClicked(View v) {
		// TODO Display list of backgrounds below toolbar
		BackgroundPickerFragment fragment = BackgroundPickerFragment.newInstance();

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.show(backgroundPickerFragment);
		//transaction.add(R.id.frame_layout, fragment, BACKGROUND_FRAGMENT_TAG);
		//transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onUndoButtonClicked(View v) {
		// TODO Undo last change

		// If undo is successful:
		// toolFragment.setRedoAvailable(true);

		// If there are no more changes to undo:
		// toolFragment.setUndoAvailable(false);
	}

	@Override
	public void onRedoButtonClicked(View v) {
		// TODO Redo previous change

		// If redo is successful:
		// toolFragment.setUndoAvailable(true);

		// If there are no more changes to redo:
		// toolFragment.setRedoAvailable(false);
	}

	@Override
	public void onInfoButtonClicked(View v) {
		// TODO Display information about the scene/story
	}

	@Override
	public void onSolidColorSelected(AdapterView<?> adapterView, View view, int i, long l) {
		TypedArray colors = getResources().obtainTypedArray(R.array.background_colors);
		int color = colors.getColor(i + 1, -1);
		findViewById(R.id.frame_layout).setBackgroundColor(color);
	}

	@Override
	public void onCanvasPressed() {
		focusEditingFragment();
	}
}
