package com.d24.android.pictogramapp.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.util.CustomFragmentPagerAdapter;
import com.d24.android.pictogramapp.util.CustomViewPager;
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

	private static final String BACKGROUND_FRAGMENT_TAG = "BACKGROUND_TAG";
	private static final String SELECTING_FRAGMENT_TAG = "SELECTING_TAG";

	private List<EditingFragment> fragmentList;
	SelectingFragment selectingFragment;
	BackgroundPickerFragment backgroundPickerFragment;
	//TODO, patch_x_preview
	// PreviewFragment previewFragment;
	// private static final String PREVIEW_FRAGMENT_TAG = "PREVIEW_TAG";

	Menu menu;
	public CustomViewPager mPager;
	private static final int NUM_OF_SCENES = 5; // Initial nr of scenes to start with
	private ViewPagerAdapter mPagerAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i("patch_01_frag_order", "Oncreate() START");


		setContentView(R.layout.activity_staging);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);		// Option to navigate back
			actionBar.setDisplayShowTitleEnabled(false);	// Remove title from bar
			// actionBar.getNavigationMode();
		}

		FragmentManager manager=getSupportFragmentManager(); // create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	// create an instance of Fragment-transaction

		// Instantiate a ViewPager and a PagerAdapter.
		fragmentList = new ArrayList<EditingFragment>();

		mPager = (CustomViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new ViewPagerAdapter(manager);

		mPager.setPagingEnabled(false);
		mPager.setAdapter(mPagerAdapter);

		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				Log.i("Test_05", "Selected");
				Log.i("Test_05", "cur.item, " + mPager.getCurrentItem());
				Object obj = fragmentList.get(mPager.getCurrentItem());
				Log.i("Test_05", "obj.pos, " + mPagerAdapter.getItemPosition(obj));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				//Log.i("Test_05", "\tchange2");
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				//Log.i("Test_05", "\t\tchange3");

			}
		});

		for(int i = 0; i < NUM_OF_SCENES; i++) {
			createNewScene(false);
		}
		mPagerAdapter.notifyDataSetChanged();


		selectingFragment = SelectingFragment.newInstance();
		backgroundPickerFragment = BackgroundPickerFragment.newInstance();
		// TODO, previewFragment = PreviewFragment.newInstance();

		transaction.add(R.id.frame_layout, selectingFragment, SELECTING_FRAGMENT_TAG);
		transaction.add(R.id.frame_layout, backgroundPickerFragment, BACKGROUND_FRAGMENT_TAG);
		// TODO, transaction.add(R.id.frame_layout, previewFragment);

		transaction.hide(selectingFragment);
		transaction.hide(backgroundPickerFragment);
		mPager.refreshDrawableState();
		transaction.commit();

		//Log.i("patch_01_frag_order", "Oncreate() END");
		Log.i("patch_01_frag", "\tEND of onCreate");
	}

	// TODO,
	@Override // IKKE brukt i sammenheng
	public View onCreateView (View parent,
					   String name,
					   Context context,
					   AttributeSet attrs){
		// Log.i("patch_01_frag", "\tonCreateView()");
		return super.onCreateView(parent, name,context, attrs);
	}



	// TODO, implement patch_x_backstack
	@Override
	public void onBackPressed() {
		if (selectingFragment.isVisible() || backgroundPickerFragment.isVisible()) {
			FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
			FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
			transaction.hide(selectingFragment);
			transaction.hide(backgroundPickerFragment);
			// TODO, push patch_01_swipe
			mPager.setPagingEnabled(true);
			transaction.commit();
		}

		// TODO, implement patch_x_backstack
		else if (mPager.getCurrentItem() <= 0) {
			// If the user is currently looking at the first step, allow the system to handle the
			// Back button. This calls finish() on this activity and pops the back stack.
			String text = "Do you wish to exit? Promt save & exit, give option for Cancel";
			helpfulNotification(text);
			//super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			Log.i("patch_x_backstack", "BackButton pushed, going to fragment." + (mPager.getCurrentItem() - 1));
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


	// TODO, confirm patch_01_misc
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
		transaction.hide(backgroundPickerFragment);
		transaction.commit();
	}

	public void onBackgroundButtonClicked() {


		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.show(backgroundPickerFragment);
		transaction.hide(selectingFragment);
		// TODO, patch_x_backstack
		//transaction.add(R.id.frame_layout, fragment, BACKGROUND_FRAGMENT_TAG);
		//transaction.addToBackStack(null);
		transaction.commit();
	}

	// TODO, patch_01_frag
	public void onUndoButtonClicked() {
		boolean buttonIsActive = true;
		if (buttonIsActive) {
			int color_white = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		} else {
			int color_grey = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		}

        deleteCurrentScene(); // TODO, Temporary use for testing: Deleting Scene


        // If undo is successful:
		// toolFragment.setRedoAvailable(true);

		// If there are no more changes to undo:
		// toolFragment.setUndoAvailable(false);
	}

	// TODO, patch_01_frag
	public void onRedoButtonClicked() {
		boolean buttonIsActive = true;
		createNewScene(true);
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


	// TODO create patch_x_preview
	// Not implemented, Created for PreviewFragment. Navigation & Management of Scenes
	private void navigateScene(boolean positiveIndexChange) {
		int index = 0;
		if (positiveIndexChange){
			index = 1;
		}
	}

	// TODO, remove patch_01_frag (frag.name)
	// Not fully implemented, Created for PreviewFragment. Navigation & Management of Scenes
	private void createNewScene(boolean notifyChange) {

		EditingFragment frag = new EditingFragment();
		//Bundle args = new Bundle();
		//args.putInt("someInt", 5);
		//frag.setArguments(args);

		fragmentList.add(frag);
		int size = fragmentList.size()-1;
		if(notifyChange) {
			mPagerAdapter.notifyDataSetChanged();
			//mPager.setCurrentItem(size);
		}


		/*EditingFragment newFrag = new EditingFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", 5);
		newFrag.setArguments(args);

		fragmentList.add(newFrag);
		Log.i("patch_01_frag_02", "fragmentList.size." + fragmentList.size());
		EditingFragment f = fragmentList.get(fragmentList.size()-1);
		Log.i("patch_01_frag_02", "\tfrag.null." + (f == null));
		if(f != null){
			Log.i("patch_01_frag_02", "\tfrag.name." + (f.name));
		}
		mPagerAdapter.attachFragment(mPager,newFrag, fragmentList.size()-1);


		mPagerAdapter.notifyDataSetChanged();*/

	}


	// TODO, remove/create patch_01_frag, update mPager.currentItem
	// Not fully implemented, Created for PreviewFragment. Navigation & Management of Scenes
	private void deleteCurrentScene() {

		int currentIndex = mPager.getCurrentItem();
		//Log.i("patch_01_frag_action", "Delete fragment." + currentIndex);

		/*
		mPager.setCurrentItem(2);
		mPager.removeView((View) fragmentList.get(currentIndex).getView());
		mPager.setCurrentItem(currentIndex+1);
		alternative, mPager.removeViewAt(2);
		*/

		fragmentList.remove(currentIndex);
		// Log.i("patch_01_frag_stat", "REMOVED");
		//mPager.setAdapter(mPagerAdapter);
		//Log.i("patch_01_frag", "\tNEW adapter SET");
		mPagerAdapter.notifyDataSetChanged();
		//mPager.setCurrentItem(0);
		//Log.i("patch_01_frag", "Current." + mPager.getCurrentItem());

		//mPagerAdapter.notifyDataSetChanged();
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
			reader.close();
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}

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


	// TODO, fix patch_01_frag, scene not properly loaded
	// TODO, patch_x_misc, adapterView, view and l is unused.
	@Override
	public void onSolidColorSelected(AdapterView<?> adapterView, View view, int i, long l) {

		TypedArray colors = getResources().obtainTypedArray(R.array.background_colors);
		int color = colors.getColor(i, -1);

		int index;
		index = mPager.getCurrentItem();
		EditingFragment frag = fragmentList.get(index);

		if(frag != null) {
			View fragView = frag.getView();
			if (fragView != null) {
				frag.getView().setBackgroundColor(color);
			} else {
				helpfulNotification("ERROR at onSolidColorSelected");
				Log.i("patch_01_frag_error", "frag.view.null: " + (frag.getView() == null));

				try {
					// INSTANTIATING view


				} catch (Exception e) {
					Log.i("patch_01_frag_error", "Exception at try-catch: " + e.toString());
				}
			}
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
				Log.i("Test_22.3", "pre update");
				frag.updateImageView(item_id);
			} catch (Exception e) {
				helpfulNotification("Please swipe for a bit first");
				Log.i("Test_22.3", "Error at updateImg: " + e.toString());
			}

		}
		else {
			Log.i("Test_22.3", "error update");
			Log.d("StagingActivity","EditingFragment from fragmentList.get(" + item_id + ") is null");
		}
	}

	@Override
	public void onCanvasPressed() {
		focusEditingFragment();

		// TODO, patch_01_swipe
		mPager.setPagingEnabled(true);
	}

	public void helpfulNotification (String text) {
		View view = findViewById(R.id.frame_layout);
		Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();

	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		Log.i("patch_01_frag", "onAttachFr");
		EditingFragment f = (EditingFragment) fragment;
		Log.i("patch_01_frag", "f.name."+f.name);
	}

















	private class ViewPagerAdapter extends CustomFragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm,mPager);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}




		/*Create the page for the given position.*/
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			Fragment fragment = getItem(position);
			// Test_22.3, Log.i("Test_22.3", "inst, name: " + ((EditingFragment) fragment).name);
			if(fragment.getId() == 0){
				// Log.i("patch_01_frag", "Super: instantiateItem");
				Object obj = super.instantiateItem(container, position);
				// Log.i("patch_01_frag", "Container: id."+container.getId());
				// Log.i("patch_01_frag", "Object: id."+((EditingFragment)obj).getId());
				//Log.i("patch_01_frag", "Object: tag."+((EditingFragment)obj).getTag());
				return obj;
			} else {
				// Todo, IF FRAGMENT IS RETURNED... it better be attached
				// Log.i("patch_01_frag", "Super: instantiateItem, NOT");
				return fragment;

			}

		}

		// patch_01_frag
    /* METODE blir kalt fra mPagerAdapter.notifyDataSetChanged()
         Dette er når fragmentList får nye elementer eller det fjernes et element
         POSITION_NONE, om objekt er fjernet fra listen
         */
		@Override
		public int getItemPosition(Object object)
		{
			EditingFragment fr = (EditingFragment) object;
			int index = fragmentList.indexOf(fr);
			//int index = fragmentList.indexOf(fr);

			//Log.i("patch_01_frag_m", "getItemPos obj.index." + index);

			if ((object instanceof  EditingFragment) && (index != -1)) {
				//Log.i("patch_01_frag_m", "\t\tFragment found");
				EditingFragment f = (EditingFragment) object;
				return index;
			} else {
				mPager.removeView(((EditingFragment) object).getView());
				//Log.i("patch_01_frag_m", "\t\tFragment not in list, delete it");
				return POSITION_NONE;
			}
		}



		//Destroying Item at (position / getCount())
		@Override // IKKE BRUKT (før onAttach)
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (object instanceof EditingFragment) {
				EditingFragment fr = (EditingFragment) object;
				int index = -1;
				int index1 = fragmentList.indexOf(fr);
				for(int i = 0; i < getCount(); i++) {
					EditingFragment item = fragmentList.get(i);
					if(item.equals(fr)) {
						// item still exists in dataset; return position
						index = i;
						break;
					}
				}

				// Log.i("patch_01_frag", "Hiding Frag:\t" + (index1 + 1));

				if(index == -1) {
					// Log.i("patch_01_frag", "Destroying fragment at:\t" + (position));
					// Log.i("patch_01_frag", "Super: Destroy." + position);
					super.destroyItem(container, position, object);
					// Log.i("patch_01_frag", "\t\t(This should trigger, mFragment.set(position,object))");
				}
			}
		}



		// TODO, fix patch_01_frag, call super?
		/*Return the Fragment associated with a specified position.*/
		@Override
		public Fragment getItem(int position) {
			Log.i("patch_01_frag", "getItem("+position+")");

			EditingFragment item = fragmentList.get(position);
			if(item.isDetached()){
				 Log.i("patch_01_frag", "\tERROR: isDetached().true. EXPECTED isDetached().false");
			}
			else if(item != null){
				View itemView = item.getView();
				if(item.getId() != 0) {
					if(itemView == null){
						 Log.i("patch_01_frag_getItem", "\tERROR: EXPECTED view.null @ID.null, pos." + position);
					} else {
						// Log.i("patch_01_frag_getItem", "\tEverything good?");
						return item;
					}
				} else {
					Log.i("patch_01_frag_getItem", "\tERROR: instantiating");
					instantiateItem(mPager,position);
				}
			} else {
				 Log.i("patch_01_frag", "\tERROR: EXPECTED item.!null, @pos." + position);
				item = EditingFragment.newInstance();
				return item;
			}

			Log.i("patch_01_frag", "ERROR: unhandled." + position);
			return item;
		}
	}
}