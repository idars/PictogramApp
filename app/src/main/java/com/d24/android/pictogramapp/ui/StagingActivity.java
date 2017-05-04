package com.d24.android.pictogramapp.ui;

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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.d24.android.pictogramapp.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StagingActivity extends AppCompatActivity
		implements SelectingFragment.PictogramSelectedListener, ToolFragment.OnToolClickedListener {

	private ImageView img;

	EditingFragment editingFragagment;
	SelectingFragment selectingFragagment;
	ToolFragment toolFragment;

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
		FrameLayout fr = (FrameLayout) findViewById(R.id.container_selection);
		fr.setVisibility(View.GONE);

		toolFragment = ToolFragment.newInstance();
		editingFragagment	=new EditingFragment();//create the fragment instance for the top fragment
		selectingFragagment =new SelectingFragment();//create the fragment instance for the bottom fragment
		String editingFragmentTag = "Editing_tag";
		String selectingFragmentTag = "Selecting_tag";

		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
		transaction.add(R.id.container_tool, toolFragment);
		transaction.add(R.id.container_editing, editingFragagment).addToBackStack(editingFragmentTag);
		transaction.add(R.id.container_selection, selectingFragagment).addToBackStack(selectingFragmentTag);
		transaction.hide(selectingFragagment);

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

	@Override
	public void onBackPressed() {
		Log.i("D-bug", "onBackPressed Called");
		/*Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);*/
		String text = "sooooooome... BODY once told me the world is gonna roll me. I ain't the sharpest tool...";
		//String text2 = "You sure you want to exit?";

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);

		Snackbar.make(frameLayout, text, Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();



	}

	public void onItemSelected(long item_id)
	{
		// The user selected the headline of an article from the HeadlinesFragment
		// Do something here to display that article

		editingFragagment= (EditingFragment)
				getSupportFragmentManager().findFragmentById(R.id.container_editing);

		if (editingFragagment != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			editingFragagment.updateImageView(item_id); // TODO
		} else {
			// Otherwise, we're in the one-pane layout and must swap frags...

			// Create fragment and give it an argument for the selected article
			editingFragagment = new EditingFragment();
			Bundle args = new Bundle();
			//args.putInt(EditingFragment.ARG_POSITION, item_id);
			editingFragagment.setArguments(args);

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment_container view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.add(R.id.container_editing, editingFragagment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}
	}

	@Override
	public void onAddButtonClicked(View v) {
		// TODO Display list of pictograms below toolbar
		FrameLayout fr = (FrameLayout) findViewById(R.id.container_selection);
		fr.setVisibility(View.VISIBLE);

		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
		//transaction.hide(editingFragagment);
		transaction.show(selectingFragagment);
		transaction.commit();
	}

	@Override
	public void onBackgroundButtonClicked(View v) {
		// TODO Display list of backgrounds below toolbar
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
}
