package com.d24.android.pictogramapp.ui;

import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.model.Figure;
import com.d24.android.pictogramapp.model.Scene;
import com.d24.android.pictogramapp.model.Story;
import com.d24.android.pictogramapp.stickerview.StickerImageView;
import com.d24.android.pictogramapp.util.MyViewPagerAdapter;
import com.d24.android.pictogramapp.util.StoryXmlParser;
import com.d24.android.pictogramapp.util.StoryXmlSerializer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StagingActivity extends AppCompatActivity
		implements SelectingFragment.PictogramSelectedListener,
		BackgroundPickerFragment.OnBackgroundSelectedListener,
		SaveDialogFragment.SaveDialogListener {

	private ImageView img;
	private static final String BACKGROUND_FRAGMENT_TAG = "BACKGROUND_TAG";
	private static final String SELECTING_FRAGMENT_TAG = "SELECTING_TAG";
	private static final String EDITING_FRAGMENT_TAG = "EDITING_TAG";
	//TODO, private static final String PREVIEW_FRAGMENT_TAG = "PREVIEW_TAG";

	private List<EditingFragment> fragmentList;
	SelectingFragment selectingFragment;
	BackgroundPickerFragment backgroundPickerFragment;
	//TODO, PreviewFragment previewFragment;

	Menu menu;
	private ViewPager mPager;
	private MyViewPagerAdapter mPagerAdapter;


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

		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
		FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction


		// TODO, Scene Initalization
		// Instantiate a ViewPager and a PagerAdapter.
		// fragmentList = new ArrayList<EditingFragment>();
		// for(int i = 0; i < NUM_OF_SCENES; i++) {
			// createNewScene(false);
		// }


		mPager = (ViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new MyViewPagerAdapter();
		mPager.setAdapter(mPagerAdapter);
//		mViewPager.setOnPageChangeListener((ViewPager.OnPageChangeListener) this);

		// Instantiate ViewPager with an initial blank view
		LayoutInflater inflater = getLayoutInflater();
		FrameLayout v0 = (FrameLayout) inflater.inflate(R.layout.layout_layer, null);
		mPagerAdapter.addView(v0, 0);

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
	protected void onStart() {
		super.onStart();

		// Check if this activity has been initiated with a pre-made story
		String filename = getIntent().getStringExtra("filename");
		if (filename != null) {
			// Read from the provided file
			try {
				File file = new File(getFilesDir(), filename);
				FileInputStream inputStream = new FileInputStream(file);
				StoryXmlParser parser = new StoryXmlParser();
				populateFrom(parser.parse(inputStream));
			} catch (XmlPullParserException | IOException e) {
				e.printStackTrace();
				Snackbar.make(findViewById(R.id.frame_layout),
						R.string.error_file_load, Snackbar.LENGTH_LONG).show();
			}
		}
	}

	private void populateFrom(Story story) {
		for (Scene scene : story.scenes) {
			LayoutInflater inflater = getLayoutInflater();
			FrameLayout view = (FrameLayout) inflater.inflate(R.layout.layout_layer, null);
			mPagerAdapter.addView(view);

			LayerLayout layer = (LayerLayout) view.findViewById(R.id.layer_layout);
			layer.setBackgroundColor(Color.parseColor(scene.background));

			for (Figure figure : scene.figures) {
				StickerImageView sticker = createSticker(figure.id);

				sticker.setX(figure.x);
				sticker.setY(figure.y);
				sticker.getLayoutParams().height = figure.size;
				sticker.getLayoutParams().width = figure.size;
				sticker.setRotation(figure.rotation);
				if (figure.mirrored) sticker.setRotationY(-180f);

				layer.addView(sticker);
				sticker.requestLayout();
			}
		}

		// Remove initial fragment created in onCreate
		mPagerAdapter.removeView(mPager, 0);
		mPager.setCurrentItem(0);
	}

	public StickerImageView createSticker(long id) {
		TypedArray image_ids = getResources().obtainTypedArray(R.array.image_ids);
		Integer intItemId = (int) id;
		Drawable drawable = image_ids.getDrawable(intItemId);
		StickerImageView newImg = new StickerImageView(this);
		newImg.setImageDrawable(drawable);
		newImg.setTag(R.integer.tag_id, intItemId);
		return newImg;
	}

	// Methods for altering the pages (scenes) shown through the ViewPager

	public void addPage(View v) {
		int position = mPager.getCurrentItem();
		mPagerAdapter.addView(v, position + 1);
		mPager.setCurrentItem(position + 1);
	}

	public void removePage(View v) {
		int position = mPagerAdapter.removeView(mPager, v);
		if (position == mPagerAdapter.getCount()) position--;
		mPager.setCurrentItem(position);
	}

	public View getCurrentPage() {
		int position = mPager.getCurrentItem();
		return mPagerAdapter.getView(position);
	}

	public void setCurrentPage(View v) {
		int position = mPagerAdapter.getItemPosition(v);
		mPager.setCurrentItem(position, true);
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

	public void onCanvasPressed(View v) {
		if (v instanceof LayerLayout) {
			LayerLayout canvas = (LayerLayout) v;
			int count = canvas.getChildCount();
			StickerImageView currSticker;
			for (int i = 0; i < count; i++) {
				View currView = canvas.getChildAt(i);
				if (currView instanceof StickerImageView) {
					currSticker = (StickerImageView) currView;
					currSticker.setControlItemsHidden(true);
				}
			}
		}

		focusEditingFragment();

		Log.d(TAG, "clicked on layerlayout");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_staging, menu);
		this.menu = menu;

		int color_white = getResources().getColor(R.color.white);
		int color_grey = getResources().getColor(R.color.grey_500);

		menu.findItem(R.id.action_add_figure).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_color).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_redo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_save).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		return true;
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
		} else if (id == R.id.action_add_figure) {
			// This ID represents the pictogram button.
			onAddButtonClicked();
			return true;
		} else if (id == R.id.action_color) {
			// This ID represents the color button.
			onColorButtonClicked();
			return true;
		} else if (id == R.id.action_undo) {
			// This ID represents the undo button.
			onUndoButtonClicked();
			return true;
		} else if (id == R.id.action_redo) {
			// This ID represents the redo button.
			onRedoButtonClicked();
			return true;
		} else if (id == R.id.action_save) {
			// This ID represents the save button.
			onSaveButtonClicked();
			return true;
		} else if (id == R.id.action_add_page) {
			// This ID represents the add page button.
			LayoutInflater inflater = getLayoutInflater();
			addPage(inflater.inflate(R.layout.layout_layer, null));
			return true;
		} else if (id == R.id.action_remove_page) {
			removePage(getCurrentPage());
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

	// When a pictogram has been selected
	public void onItemSelected(long item_id)
	{
		LayerLayout focusedView = (LayerLayout) getCurrentPage().findViewById(R.id.layer_layout);
		StickerImageView sticker = createSticker(item_id);
		focusedView.addView(sticker);
	}

	public void onColorButtonClicked() {
		BackgroundPickerFragment fragment = BackgroundPickerFragment.newInstance();

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.show(backgroundPickerFragment);
		//transaction.add(R.id.frame_layout, fragment, BACKGROUND_FRAGMENT_TAG);
		//transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onSolidColorSelected(AdapterView<?> adapterView, View view, int i, long l) {
		TypedArray colors = getResources().obtainTypedArray(R.array.background_colors);
		int color = colors.getColor(i, -1);
		boolean stickerPainted = false;

		// Determine whether any stickers are focused and paint them respectively
		LayerLayout focusedView = (LayerLayout) getCurrentPage().findViewById(R.id.layer_layout);
		for (int j = 0; j < focusedView.getChildCount(); ++j) {
			View child = focusedView.getChildAt(j);
			if (child instanceof StickerImageView) {
				StickerImageView sticker = (StickerImageView) child;
				if (!sticker.areControlItemsHidden()) {
					ImageView figure = (ImageView) sticker.getMainView();
					figure.setColorFilter(color);
					sticker.setTag(R.integer.tag_color, color);
					stickerPainted = true;
				}
			}
		}

		// If not, paint the background only
		if (!stickerPainted) {
			focusedView.setBackgroundColor(color);
		}
	}

	// TODO to be implemented
	public void onUndoButtonClicked() {
		boolean buttonIsActive = true;

		if (buttonIsActive) {
			int color_white = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		} else {
			int color_grey = getResources().getColor(R.color.white);
			menu.findItem(R.id.action_undo).getIcon().setColorFilter(color_grey, PorterDuff.Mode.SRC_IN);
		}


		// If undo is successful:
		// toolFragment.setRedoAvailable(true);

		// If there are no more changes to undo:
		// toolFragment.setUndoAvailable(false);
	}

	// TODO to be implemented
	public void onRedoButtonClicked() {
		boolean buttonIsActive = true;

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

	public void onSaveButtonClicked() {
		SaveDialogFragment dialog = new SaveDialogFragment();
		dialog.show(getFragmentManager(), "save");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String filename) {
		// TODO move work outside main thread
		try {
			File file = new File(getFilesDir(), filename);
			FileOutputStream outputStream = new FileOutputStream(file);
			StoryXmlSerializer serializer = new StoryXmlSerializer();
			serializer.write(outputStream, new Story(filename, (ViewPager) findViewById(R.id.viewPager)));

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

	// Obsolete methods and classes

	/*
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
	*/

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
	private EditingFragment createNewScene(boolean notifyChange) {
		Log.i("Testing_15", "ADDING NEW FRAGMENT");
		EditingFragment frag = EditingFragment.newInstance();
		fragmentList.add(frag);
		if(notifyChange) {
			mPagerAdapter.notifyDataSetChanged();
		}
		return frag;
	}

	// Not fully implemented, Created for PreviewFragment. Navigation & Management of Scenes
	private void deleteCurrentScene() {

		int currentIndex = mPager.getCurrentItem();
		Log.i("Testing_15", "DELETING FRAGMENT at " + currentIndex);

		// TODO, NAVIGATION, mPager.setCurrentItem(2);
		mPager.removeView((View) fragmentList.get(currentIndex).getView());
		//mPager.setCurrentItem(currentIndex+1);
		//alternative, mPager.removeViewAt(2);
		fragmentList.remove(currentIndex);
		mPagerAdapter.notifyDataSetChanged();
		//mPager.setAdapter(mPagerAdapter);
	}

	/*
	@Override
	public void onCanvasPressed() {
		focusEditingFragment();
	}
	*/

	public class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
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
			Log.i("Testing_15", "(-/" + (getCount()-1) + ") Check visible fragments");

			EditingFragment fr = (EditingFragment) object;
			int index = -1;

			for(int i = 0; i < getCount(); i++) {
				EditingFragment item = (EditingFragment) fragmentList.get(i);
				if(item.equals(fr)) {
					// item still exists in dataset; return position
					index = i;
					break;
				}
			}
			//Log.i("Testing_15", "" + object.);

			// TODO, could test a boolean variable if fragment found
			if ((object instanceof  EditingFragment) && (index != -1)) {
				Log.i("Testing_15", "(" + index + "/(" + (getCount()-1) + ") Fragment found");
				EditingFragment f = (EditingFragment) object;

				Log.i("Testing_15", "\t return "+ POSITION_UNCHANGED);

				return POSITION_UNCHANGED;
			} else {
//				Log.i("Testing_15", "UNEXPECTED, Object.class: " + object.getClass());
				Log.i("Testing_15", "(-/(" + (getCount()-1) + ") Fragment NOT found, delete()");
				Log.i("Testing_15", "\t return "+ POSITION_NONE);
				return POSITION_NONE;
			}

			/*TODO, New implementation
			 EditingFragment f = (EditingFragment) object;

			for(int i = 0; i < getCount(); i++) {

				Fragment item = (Fragment) getItem(i);
				if(item.equals(f)) {
					// item still exists in dataset; return position
					return i;
				}
			}

			// if we arrive here, the data-item for which the Fragment was created
			// does not exist anymore.

			// Also, cleanup: remove reference to Fragment from mItems
			for(Map.Entry<Long, MainListFragment> entry : mItems.entrySet()) {
				if(entry.getValue().equals(f)) {
					mItems.remove(entry.getKey());
					break;
				}
			}

			// Let ViewPager remove the Fragment by returning POSITION_NONE.
			return POSITION_NONE;
			 */
		}



		/*TODO, Called when a change in the shown pages is going to start being made.*/
		@Override
		public void 	startUpdate(ViewGroup container){
			//Log.i("Testing_15", "1.\tstartUpdate()");
			//Log.i("Testing_15", "Starting Transaction");
			Log.i("Testing_15", "(-/" + (getCount()-1) + ") Start");
			//Log.i("Testing_15", "\t\t end");
			super.startUpdate(container);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);
			//Log.i("Testing_15", "\t2.0\tdestroyItem(" + position + ")");
			//Log.i("Testing_15", "\tDestroying Item at (" + position + "/" + getCount() + ") [disabled]");
			Log.i("Testing_15", "\t(" + position + "/" + (getCount()-1) + ") [attempt] [Destroying Frag.]");

			// TODO, possibility to NOT have to go through for-loop
			if (object instanceof EditingFragment) {
				EditingFragment fr = (EditingFragment) object;
				int index = -1;

				for(int i = 0; i < getCount(); i++) {
					EditingFragment item = (EditingFragment) fragmentList.get(i);
					if(item.equals(fr)) {
						// item still exists in dataset; return position
						index = i;
						break;
					}
				}
				if(index == -1) {
					Log.i("Testing_15", "\t(" + position + "/" + (getCount()-1) + ") [super] [Destroying Frag.]");

					//instantiateItem(container, (position-1));

				}
			}

			/*TODO, consider deleting
			EditingFragment f = fragmentList.get(0);
			if((f == null)){
				Log.i("Testing_15", "destroy is null");
			}
			Log.i("Testing_15", "IF-IF of DESTROY");
			if(f != null){
				Log.i("Testing_15", "destroying");
				fragmentList.remove(0);
				Log.i("Testing_15", "destroyingSuper");
				Log.i("Testing_15", "destroy end");
			}
*/
		}

		/*TODO, Return a unique identifier for the item at the given position.*/
		@Override
		public long 	getItemId(int position){
			// Basically returning position back as long-variable... nothing extra implemented
			Log.i("Testing_15", "\t(" + position + "/" + (getCount()-1) + ") [Find ID]");
			return super.getItemId(position);
		}

		/*Return the Fragment associated with a specified position.*/
		@Override
		public Fragment getItem(int position) {
			EditingFragment fragment = null;
			fragment = fragmentList.get(position);
			//Log.i("Testing_15", "\t(" + position + "/" + (getCount()-1) + ") [getFragment]");
			//Log.i("Testing_15", "\t\tReturning fragment-object at (" + position + ")");

			/*TODO, consider deleting
			if(fragmentList.size() > 2){
				fragment = EditingFragment.newInstance();
				//Log.i("Testing_15", "\t2.2.\tgetItem(" + position + "), \t return (new) Fragment");
				Log.i("Testing_15", "\tCreating fragment at (" + position + "), (new) Fragment-instnace");
			}
			else{
				//Log.i("Testing_15", "\t2.2\tgetItem(" + position + "), \t return fragment");
				Log.i("Testing_15", "\tCreating fragment(" + position + "), fragmentList(pos)");

				fragment = fragmentList.get(position);
			}*/

			return fragment;
		}

		/*Create the page for the given position.*/
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO, might be uncessary to override
			//Log.i("Testing_15", "\tInstansiate at (" + position + ")");
			Log.i("Testing_15", "\t(" + position + "/" + (getCount()-1) + ") [Load fragment]");
			return super.instantiateItem(container, position);


			/*TODO, consider deleting
			EditingFragment fragment = (EditingFragment) super.instantiateItem(container, position);
			//alt,1 fragmentList.add(position, fragment)
			//Log.i("Testing_15", "\t2.2.2 instansiate(" + position + ")");

			if(fragmentList.get(position) == null){
				Log.i("Testing_15", "INSTANSIATING, UNEXPECTED CALL, FRAGMENT WAS NULL");
				fragmentList.add(position, fragment);
			} else {
				fragmentList.remove(position);
				fragmentList.add(position, fragment);
			}
			return fragment;
			*/
		}




		/*TODO, Called to inform the adapter of which item is currently considered to be the "primary", that is the one show to the user as the current page.*/
		@Override
		public void 	setPrimaryItem(ViewGroup container, int position, Object object){
			//Log.i("Testing_15", "\t2.3\tsetPrimaryItem(" + position + ")");
			//Log.i("Testing_15", "\tSetting new primary fragment at(" + position + ")");
			Log.i("Testing_15", "\t(" + position + "/" + (getCount()-1) + ") ** New primary");
			super.setPrimaryItem(container, position, object);
		}

		/*TODO, Called when the a change in the shown pages has been completed.*/
		@Override
		public void finishUpdate(ViewGroup container){
			super.finishUpdate(container);
			//Log.i("Testing_15", "\t\t3. \tfinishUpdate()\n");
			//Log.i("Testing_15", "Transaction completed\n");
			Log.i("Testing_15", "\t");
		}



		/*TODO, Determines whether a page View is associated with a specific key object as returned by instantiateItem(ViewGroup, int).*/
		@Override
		public boolean 	isViewFromObject(View view, Object object){
			//Log.i("Testing_15", "\t\t\t4.\tisViewFromObject(), return" + super.isViewFromObject(view, object));
			return super.isViewFromObject(view,object);
		}

		/*TODO, Restore any instance state associated with this adapter and its pages that was previously saved by saveState().*/
		@Override
		public void 	restoreState(Parcelable state, ClassLoader loader){
			Log.i("Testing_15", "UNEXPECTED METHOD 3 - PageAdapter.restoreState()");
			super.restoreState(state,loader);
		}

		/*TODO, Save any instance state associated with this adapter and its pages that should be restored if the current UI state needs to be reconstructed.*/
		@Override
		public Parcelable saveState(){
			Log.i("Testing_15", "UNEXPECTED METHOD, 4 - PageAdapter.saveState()");
			//Log.i("Testing_15", "\t\t end");
			return super.saveState();
		}
	}


}

