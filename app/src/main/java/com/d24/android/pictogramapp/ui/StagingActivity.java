package com.d24.android.pictogramapp.ui;

import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.util.StoryXmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StagingActivity extends AppCompatActivity
		implements SelectingFragment.PictogramSelectedListener,
		BackgroundPickerFragment.OnBackgroundSelectedListener,
		EditingFragment.OnCanvasTouchedListener,
		SaveDialogFragment.SaveDialogListener {

	private ImageView img;
	private static final String BACKGROUND_FRAGMENT_TAG = "BACKGROUND_TAG";
	private static final String SELECTING_FRAGMENT_TAG = "SELECTING_TAG";
	private static final String EDITING_FRAGMENT_TAG = "EDITING_TAG";
	//TODO, private static final String PREVIEW_FRAGMENT_TAG = "PREVIEW_TAG";

	private List<EditingFragment> fragmentList;
	private List<EditingFragment> fragmentList2;
	SelectingFragment selectingFragment;
	BackgroundPickerFragment backgroundPickerFragment;
	//TODO, PreviewFragment previewFragment;

	Menu menu;


	private ViewPager mPager;
	private static final int NUM_OF_SCENES = 5; // Initial nr of scenes to start with
	private PagerAdapter mPagerAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_staging);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);		// Option to navigate back
			actionBar.setDisplayShowTitleEnabled(false);	// Remove title from bar
			actionBar.getNavigationMode();
		}

		// TEST, Get size
		Log.i("Test_22.1", "FragList2");
		int testVal = 5;
		fragmentList2 = new ArrayList<EditingFragment>();
		EditingFragment[] frags = new EditingFragment[5];
		for(int i = 0; i< testVal; i++)
			frags[i] = EditingFragment.newInstance();

		for(int i = 0; i < testVal; i++) {
			fragmentList2.add(frags[i]);
			int size = fragmentList2.size();
			Log.i("Test_22.1", "Frag count, " + size);
		}

		// TEST, Get index
		for(int i = 0; i < 5; i++) {
			int index = fragmentList2.indexOf(frags[i]);
			Log.i("Test_22.1", "Frag index, " + index);
		}

		// Remove one index
		fragmentList2.remove(3);

		// TEST, Get index
		for(int i = 0; i < 5; i++) {
			int index = fragmentList2.indexOf(frags[i]);
			Log.i("Test_22.1", "\tFrag index2, " + index);
		}










		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction


		// TODO, Scene Initalization
		// Instantiate a ViewPager and a PagerAdapter.
		fragmentList = new ArrayList<EditingFragment>();
		for(int i = 0; i < NUM_OF_SCENES; i++) {
			createNewScene(false);
		}


		mPager = (ViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new ViewPagerAdapter(manager);
		mPager.setAdapter(mPagerAdapter);
//		mViewPager.setOnPageChangeListener((ViewPager.OnPageChangeListener) this);

		selectingFragment = SelectingFragment.newInstance();
		backgroundPickerFragment = BackgroundPickerFragment.newInstance();
		// TODO, previewFragment = PreviewFragment.newInstance();

		transaction.add(R.id.frame_layout, selectingFragment, SELECTING_FRAGMENT_TAG);
		transaction.add(R.id.frame_layout, backgroundPickerFragment, BACKGROUND_FRAGMENT_TAG);
		// TODO, transaction.add(R.id.frame_layout, previewFragment);

		transaction.hide(selectingFragment);
		transaction.hide(backgroundPickerFragment);

		transaction.commit();
	}


	@Override
	public void onResume(){
		super.onResume();
		EditingFragment f = fragmentList.get(1);
		if(f == null) {
		} else {
			//Context c = this;
			//Resources r = getResources();
			//f.updateImageView(2, r, c);
		}
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

			transaction.hide(selectingFragment);
			transaction.hide(backgroundPickerFragment);
			transaction.commit();
		}

		// TODO, new shit
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the system to handle the
			// Back button. This calls finish() on this activity and pops the back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	public void focusEditingFragment(){
		if (selectingFragment.isVisible() || backgroundPickerFragment.isVisible()) {
			FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
			FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
			transaction.hide(selectingFragment);
			transaction.hide(backgroundPickerFragment);
			transaction.commit();
		}

	}


	/*TODO, DENNE VIL BLI ET PROBLEM SENERE */
	/*TODO, verdien på oppdateres når scener slettes */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		if (id == R.id.action_pictogram) {
			// This ID represents the Add Pictogram button.

			onAddButtonClicked();
			return true;
		}
		if (id == R.id.action_background) {
			// This ID represents the Home or Up button.
			onBackgroundButtonClicked();
			return true;
		}
		if (id == R.id.action_undo) {
			// This ID represents the Undo button.
			onUndoButtonClicked();
			return true;
		}
		if (id == R.id.action_redo) {
			// This ID represents the Redo button.
			onRedoButtonClicked();
			return true;
		}
		if (id == R.id.action_save) {
			// This ID represents the Redo button.
			onSaveButtonClicked();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}



	public void onAddButtonClicked() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.show(selectingFragment);
		transaction.commit();
	}

	public void onBackgroundButtonClicked() {
		// TODO Display list of backgrounds below toolbar
		BackgroundPickerFragment fragment = BackgroundPickerFragment.newInstance();

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.show(backgroundPickerFragment);
		//transaction.add(R.id.frame_layout, fragment, BACKGROUND_FRAGMENT_TAG);
		//transaction.addToBackStack(null);
		transaction.commit();
	}

	public void onUndoButtonClicked() {
		boolean buttonIsActive = true;
		//createNewScene(true); //Todo, remove
		if (buttonIsActive) {
			int color_white = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		} else {
			int color_grey = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		}

        //createNewScene(true); // TODO, Temporary use for testing: Adding new Scene
        deleteCurrentScene(); // TODO, Temporary use for testing: Deleting Scene


        // If undo is successful:
		// toolFragment.setRedoAvailable(true);

		// If there are no more changes to undo:
		// toolFragment.setUndoAvailable(false);
	}

	public void onRedoButtonClicked() {
		boolean buttonIsActive = true;
		deleteCurrentScene(); //Todo, remove
		if (buttonIsActive) {
			int color_white = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_redo).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		} else {
			int color_grey = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_redo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		}
		// If redo is successful:
		// toolFragment.setUndoAvailable(true);

		// If there are no more changes to redo:
		// toolFragment.setRedoAvailable(false);
	}


	// Not implemented, Created for PreviewFragment. Navigation & Management of Scenes
	private void navigateScene(boolean positiveIndexChange) {
		// TODO <Get visible index>
		// TODO <Get wanted new direction (or index)>
		int index = 0;
		if (positiveIndexChange){
			index = 1;
		}
	}

	// Not fully implemented, Created for PreviewFragment. Navigation & Management of Scenes
	private void createNewScene(boolean notifyChange) {
		Log.i("Testing_15", "ADDING NEW FRAGMENT");
		//TEST_22.3EditingFragment frag = EditingFragment.newInstance();
		EditingFragment frag = new EditingFragment();
		fragmentList.add(frag);
		if(notifyChange) {
			mPagerAdapter.notifyDataSetChanged();
		}
	}


	// Not fully implemented, Created for PreviewFragment. Navigation & Management of Scenes
	private void deleteCurrentScene() {

		int currentIndex = mPager.getCurrentItem();
		Log.i("Testing_15", "DELETING FRAGMENT at " + currentIndex);

		// TODO, NAVIGATION, mPager.setCurrentItem(2);
		//mPager.removeView((View) fragmentList.get(currentIndex).getView());
		//mPager.setCurrentItem(currentIndex+1);
		//alternative, mPager.removeViewAt(2);

		fragmentList.remove(currentIndex);
		Log.i("Test_22.size", "Size after " + fragmentList.size());

		mPagerAdapter.notifyDataSetChanged();
		//mPager.setAdapter(mPagerAdapter);
	}


	// Not implemented, Created for PreviewFragment. Navigation & Management of Scenes
	public void onSaveButtonClicked() {
		// TODO Create new thread
		SaveDialogFragment dialog = new SaveDialogFragment();
		dialog.show(getFragmentManager(), "save");
    }

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String filename) {
		try {
			File file = new File(getFilesDir(), filename);
			FileOutputStream outputStream = new FileOutputStream(file);
			StoryXmlSerializer serializer = new StoryXmlSerializer();
			serializer.write(outputStream, (ViewPager) findViewById(R.id.viewPager), filename);

			// Debug code; to display output code
			FileInputStream inputStream = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();

			Log.d(getLocalClassName(), "File output: " + filename + "\n" + sb.toString());

			Snackbar.make(findViewById(R.id.frame_layout),
					R.string.success_file_save, Snackbar.LENGTH_SHORT).show();
		} catch (IOException e) {
			// Display error message
			Snackbar.make(findViewById(R.id.frame_layout),
					R.string.error_file_save, Snackbar.LENGTH_LONG)
					.setAction(R.string.button_retry, new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							onSaveButtonClicked();
						}
					}).show();
		}
	}

	@Override
	public void onSolidColorSelected(AdapterView<?> adapterView, View view, int i, long l) {
		TypedArray colors = getResources().obtainTypedArray(R.array.background_colors);
		int color = colors.getColor(i, -1);

		int index = 0;
		index = mPager.getCurrentItem();
		EditingFragment frag = fragmentList.get(index);

		if(frag != null) {
			frag.getView().setBackgroundColor(color);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_staging, menu);
		this.menu = menu;

		int color_white = getResources().getColor(R.color.white);
		int color_grey = getResources().getColor(R.color.grey_500);

		menu.findItem(R.id.action_pictogram).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_background).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_redo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_save).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		return true;
	}

	// TODO, item select.
	public void onItemSelected(long item_id)
	{
		int index;
		index = mPager.getCurrentItem();
		EditingFragment frag = fragmentList.get(index);

		// TODO, alternative implementation
		//EditingFragment page = (EditingFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + mPager.getCurrentItem());
		/*if (mPager.getCurrentItem() == 0 && page != null) {
			((EditingFragment)page).updateImageView(item_id);
		}*/

		if(frag != null){
			try {
					frag.updateImageView(item_id);
			} catch (Exception e) {
				Log.i("Test_22.3", "Error at updateImg: " + e.toString());
			}

		}
		else {
			Log.d("StagingActivity","EditingFragment from fragmentList.get(" + item_id + ") is null");
		}
	}

	@Override
	public void onCanvasPressed() {
		focusEditingFragment();
	}



























	public class ViewPagerAdapter extends FragmentStatePagerAdapter {

		public boolean newlyDestroyed = false;
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}


		//Destroying Item at (position / getCount())
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (object instanceof EditingFragment) {
				EditingFragment fr = (EditingFragment) object;
				int index = -1;
				int index1 = fragmentList.indexOf(fr);
				for(int i = 0; i < getCount(); i++) {
					EditingFragment item = (EditingFragment) fragmentList.get(i);
					if(item.equals(fr)) {
						// item still exists in dataset; return position
						index = i;
						break;
					}
				}
				Log.i("Test_22.3", "Hiding Frag:\t" + (index1 + 1));


				if(index == -1) {
					Log.i("Test_22.3", "\tDestroying fragment at:\t" + (position+1));
					super.destroyItem(container, position, object);
					newlyDestroyed = true;
				}
			}
		}


		/* METODE blir kalt fra mPagerAdapter.notifyDataSetChanged()
		 Dette er når fragmentList får nye elementer eller det fjernes et element
		 POSITION_NONE, om objekt er fjernet fra listen
		 */
		@Override
		public int getItemPosition(Object object)
		{
			//			Log.i("Testing_15", "1.0\tgetItemPosition(), return " + 			super.getItemPosition(object));
			//Log.i("Testing_15", "Checking, how much have this object moved?, Position (" + 			super.getItemPosition(object) + ")*?");
			//Log.i("Testing_15", "(Check visible fragment) (" + index + "/" + (getCount()-1) + ")");
			//Log.i("Test_22.2", "(-/" + (getCount()-1) + ") Check visible fragments");
			EditingFragment fr = (EditingFragment) object;
			int index = fragmentList.indexOf(fr);

			Log.i("Test_22.3", "gtItemPos. frag.index,\t" + index);

			//Log.i("Testing_15", "" + object.);

			// TODO, could test a boolean variable if fragment found
			if ((object instanceof  EditingFragment) && (index != -1)) {
				Log.i("Test_22.3", "\t\tFragment found");
				EditingFragment f = (EditingFragment) object;
				return index;
			} else {
				((EditingFragment) object).copyObject(fragmentList.get(0));
				Log.i("Test_22.3", "\t\tFragment NOT found, delete");
				return POSITION_NONE;
			}
		}


		/*Create the page for the given position.*/
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			Fragment fragment = getItem(position);
			if(fragment.getId() == 0){
				Log.i("Test_22.3", "GOOD, GOOD at " + position);
				return super.instantiateItem(container, position);
			} else {
				Log.i("Test_22.3", "BAD, BAD at " + position);
				return fragment;

				/*Log.i("Test_22.3", "BAD, BAD at " + position);
				try{
					return super.instantiateItem(container, position);
				} catch (Exception e) {
					Log.i("Test_22.3", "Error at instantiate: " + e.toString());
					return fragment;
				}*/
			}


			/* Trying out some new things
			Log.i("Test_22.3", "Showing fragment at:\t" + (position+1));
			if(newlyDestroyed){
				Log.i("Test_22.3", "Trixy:\t" + (position+1));
				newlyDestroyed = false;
				//return getItem(position);
			}

			// Test_22 Problem here??
			Object obj = null;
			try{
				obj = super.instantiateItem(container, position);
			} catch (Exception e){
				Log.i("Test_22.3", "\t Error at instansiate: " + e.toString());
				EditingFragment f = (EditingFragment) getItem(position);
				return getItem(position);
			}
			return obj;*/


		}











		/*Return the Fragment associated with a specified position.*/
		@Override
		public Fragment getItem(int position) {
			EditingFragment fragment = null;
			fragment = fragmentList.get(position);
			if(fragment.getId() == 0) {
				return fragment;
			}
			Log.i("Test_22.3", "BAD");
			return fragment;
		}

		// Setting new primary fragment at(" + position + ")
		@Override
		public void 	setPrimaryItem(ViewGroup container, int position, Object object){
			super.setPrimaryItem(container, position, object);
			//Log.i("Test_22.3", (position+1) + " ID: "+((EditingFragment) object).getId());

		}


		//Called when the a change in the shown pages has been completed.
		//Transaction completed
		@Override
		public void finishUpdate(ViewGroup container){
			try {
				super.finishUpdate(container);
			} catch (Exception e){
				Log.i("Test_22.3", "Error at finish: " + e.toString());
			}
			int start = mPager.getCurrentItem();
			//Log.i("Test_22.3", "Now viewing fragment: " + ++start);
		}


	}


}

