package com.d24.android.pictogramapp.ui;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
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
import com.d24.android.pictogramapp.util.ViewPagerToggleSwipe;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StagingActivity extends AppCompatActivity
		implements SelectingFragment.PictogramSelectedListener,
		BackgroundPickerFragment.OnBackgroundSelectedListener,
		SaveDialogFragment.SaveDialogListener,
		SaveAndExitDialogFragment.SaveAndExitDialogListener {

	private static final String BACKGROUND_FRAGMENT_TAG = "BACKGROUND_TAG";
	private static final String SELECTING_FRAGMENT_TAG = "SELECTING_TAG";

	SelectingFragment selectingFragment;
	BackgroundPickerFragment backgroundPickerFragment;

	Menu menu;

    private ViewPagerToggleSwipe mPager;
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


        mPager = (ViewPagerToggleSwipe) findViewById(R.id.viewPager);
        mPagerAdapter = new MyViewPagerAdapter();
		mPager.setAdapter(mPagerAdapter);

		// Check if this activity has been initiated with a pre-made story
		String filename = getIntent().getStringExtra("filename");
		if (filename != null) {
			// Read from the provided file
			try {
				File file = new File(getFilesDir() + File.separator + "stories", filename);
				FileInputStream inputStream = new FileInputStream(file);
				StoryXmlParser parser = new StoryXmlParser();
				populateFrom(parser.parse(inputStream));
			} catch (XmlPullParserException | IOException e) {
				e.printStackTrace();
				Snackbar.make(findViewById(R.id.frame_layout),
						R.string.error_file_load, Snackbar.LENGTH_LONG).show();
			}
		} else {
			// Instantiate ViewPager with an initial blank view
			LayoutInflater inflater = getLayoutInflater();
			FrameLayout v0 = (FrameLayout) inflater.inflate(R.layout.layout_layer, null);
			mPagerAdapter.addView(v0, 0);
		}

		selectingFragment = SelectingFragment.newInstance();
		backgroundPickerFragment = BackgroundPickerFragment.newInstance();

		transaction.add(R.id.frame_layout, selectingFragment, SELECTING_FRAGMENT_TAG);
		transaction.add(R.id.frame_layout, backgroundPickerFragment, BACKGROUND_FRAGMENT_TAG);
		transaction.hide(selectingFragment);
		transaction.hide(backgroundPickerFragment);

		transaction.commit();
	}

	private void populateFrom(Story story) {
		for (Scene scene : story.scenes) {
			LayoutInflater inflater = getLayoutInflater();
			FrameLayout view = (FrameLayout) inflater.inflate(R.layout.layout_layer, null);
			mPagerAdapter.addView(view);

			LayerLayout layer = (LayerLayout) view.findViewById(R.id.layer_layout);
			layer.setBackgroundColor(Color.parseColor(scene.background));

			for (Figure figure : scene.figures) {
				if (figure == null) continue;
				StickerImageView sticker = createSticker(figure.id);
				ImageView image = (ImageView) sticker.getMainView();

				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(figure.size, figure.size);
				params.leftMargin = (int) figure.x;
				params.topMargin = (int) figure.y;

				image.setColorFilter(Color.parseColor(figure.color));
				sticker.setTag(R.integer.tag_color, figure.color);
				sticker.setRotation(figure.rotation);
				if (figure.mirrored) image.setRotationY(-180f);

				sticker.setControlItemsHidden(true);
				layer.addView(sticker, params);
				sticker.requestLayout();
			}
		}

		mPager.setCurrentItem(0);
	}

	public StickerImageView createSticker(long id) {
		StickerImageView newImg = new StickerImageView(this);
		Integer intItemId = (int) id;
		newImg.setTag(R.integer.tag_id, intItemId);

		TypedArray image_ids = getResources().obtainTypedArray(R.array.image_ids);
		Drawable drawable = image_ids.getDrawable(intItemId);
		newImg.setImageDrawable(drawable);

		ImageView figure = (ImageView) newImg.getMainView();
		figure.setColorFilter(Color.BLACK);
		newImg.setTag(R.integer.tag_color, "#" + Integer.toHexString(Color.BLACK));

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

	public void movePageBackward() {
		int position = mPager.getCurrentItem();
		if (position != 0) {
			View v = mPagerAdapter.getView(position);
			View w = mPagerAdapter.getView(position - 1);
			mPagerAdapter.removeView(mPager, v);
			mPagerAdapter.removeView(mPager, w);
			mPagerAdapter.addView(v, position - 1);
			mPagerAdapter.addView(w, position);
			mPager.setCurrentItem(position - 1);
		}
	}

	public void movePageForward() {
		int position = mPager.getCurrentItem();
		if (position != mPagerAdapter.getCount()) {
			View v = mPagerAdapter.getView(position);
			View w = mPagerAdapter.getView(position + 1);
			mPagerAdapter.removeView(mPager, w);
			mPagerAdapter.removeView(mPager, v);
			mPagerAdapter.addView(w, position);
			mPagerAdapter.addView(v, position + 1);
			mPager.setCurrentItem(position + 1);
		}
	}

	@Override
	public void onBackPressed() {
		if (selectingFragment.isVisible() || backgroundPickerFragment.isVisible()) {
			FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
			FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction

			transaction.hide(selectingFragment);
			transaction.hide(backgroundPickerFragment);
			transaction.commit();
			mPager.setPagingEnabled(true);
		} else if (mPager.getCurrentItem() > 0) {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		} else if (mPager.getCurrentItem() == 0){
			SaveAndExitDialogFragment dialog = new SaveAndExitDialogFragment();
			dialog.show(getFragmentManager(), "save_and_exit");

		}
	}

	public void focusEditingFragment(boolean swipeEnable){
		if (selectingFragment.isVisible() || backgroundPickerFragment.isVisible()) {
			FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
			FragmentTransaction transaction=manager.beginTransaction();	//create an instance of Fragment-transaction
			transaction.hide(selectingFragment);
			transaction.hide(backgroundPickerFragment);
			transaction.commit();
		}
		mPager.setPagingEnabled(swipeEnable);
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

		focusEditingFragment(true);

		Log.d(TAG, "clicked on layerlayout");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_staging, menu);
		this.menu = menu;

		int color_white = getResources().getColor(R.color.white);

		menu.findItem(R.id.action_add_figure).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_color).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);
		menu.findItem(R.id.action_save).getIcon().setColorFilter(color_white, PorterDuff.Mode.SRC_IN);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mPagerAdapter.getCount() > 1) {
			menu.findItem(R.id.action_remove_page).setEnabled(true);
		} else {
			menu.findItem(R.id.action_remove_page).setEnabled(false);
		}

		if (mPager.getCurrentItem() != 0) {
			menu.findItem(R.id.action_move_back).setEnabled(true);
		} else {
			menu.findItem(R.id.action_move_back).setEnabled(false);
		}

		if (mPager.getCurrentItem() != mPagerAdapter.getCount() - 1) {
			menu.findItem(R.id.action_move_forward).setEnabled(true);
		} else {
			menu.findItem(R.id.action_move_forward).setEnabled(false);
		}

		return super.onPrepareOptionsMenu(menu);
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
		} else if (id == R.id.action_save) {
			// This ID represents the Redo button.
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
		} else if (id == R.id.action_move_back) {
			movePageBackward();
			return true;
		} else if (id == R.id.action_move_forward) {
			movePageForward();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onAddButtonClicked() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if (selectingFragment.isVisible()) {
			transaction.hide(selectingFragment);
		} else {
			transaction.show(selectingFragment);
			transaction.hide(backgroundPickerFragment);
		}
		transaction.commit();
	}


	public void onColorButtonClicked() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if (backgroundPickerFragment.isVisible()) {
			transaction.hide(backgroundPickerFragment);
		} else {
			transaction.show(backgroundPickerFragment);
			transaction.hide(selectingFragment);
		}
		transaction.commit();
	}
	@Override
	public void onDialogConfirmExit(boolean saveAndExit) {
		if(saveAndExit){
			NavUtils.navigateUpFromSameTask(this);
		} else {
			Snackbar.make(findViewById(R.id.frame_layout),
					R.string.dialog_cancel, Snackbar.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDialogPositiveClick(String filename) {
		try {
			// Set up streams and serializer
			File file = new File(getFilesDir() + File.separator + "stories", filename);
			FileOutputStream outputStream = new FileOutputStream(file);
			StoryXmlSerializer serializer = new StoryXmlSerializer();

			// Attempt initializing model class instances and serialize the resulting Story
			serializer.write(outputStream, new Story(filename, (ViewPagerToggleSwipe) findViewById(R.id.viewPager)));

			// If successful, show message
			Snackbar.make(findViewById(R.id.frame_layout),
					R.string.success_file_save, Snackbar.LENGTH_SHORT).show();
		} catch (IOException e) {
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

	public void onSaveButtonClicked() {
		focusEditingFragment(true);
		SaveDialogFragment dialog = new SaveDialogFragment();
		dialog.show(getFragmentManager(), "save");
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
					sticker.setTag(R.integer.tag_color, "#" + Integer.toHexString(color));
					stickerPainted = true;
				}
			}
		}

		// If not, paint the background only
		if (!stickerPainted) {
			focusedView.setBackgroundColor(color);
		}
	}

	// When a pictogram has been selected
	public void onItemSelected(long item_id)
	{
		LayerLayout focusedView = (LayerLayout) getCurrentPage().findViewById(R.id.layer_layout);
		StickerImageView sticker = createSticker(item_id);

		focusedView.addView(sticker);
	}

}

