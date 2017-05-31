package com.d24.android.pictogramapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.d24.android.pictogramapp.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SaveDialogFragment.SaveDialogListener {

	private static final int SHOW_INTRODUCTION = 1;

	private String DIR_STORIES;
	private String DIR_DELETED;

	private ArrayAdapter<File> mAdapter;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Check for first run
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences pref = getSharedPreferences(
						getString(R.string.preference_file_key), MODE_PRIVATE
				);

				// Check if the app is started for the first time
				String key = getString(R.string.preference_value_firstrun);
				if (pref.getBoolean(key, true)) {
					Intent intent = new Intent(MainActivity.this, IntroActivity.class);
					startActivityForResult(intent, SHOW_INTRODUCTION);
				}

				// Indicate that the welcome screen no longer requires to be shown
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean(getString(R.string.preference_value_firstrun), false);
				editor.apply();
			}
		});
		thread.start();

		DIR_STORIES = getFilesDir() + File.separator + "stories";
		DIR_DELETED = getFilesDir() + File.separator + "deleted";

		// Delete files in "deleted" subfolder
		File deletedStorage = new File(DIR_DELETED);
		if (!deletedStorage.exists()) deletedStorage.mkdir();
		try {
			FileUtils.cleanDirectory(deletedStorage);
		} catch (IOException e) {
			Log.i(getLocalClassName(), "Deleted stories remain in internal storage");
		}

		// Sort and show files in "stories" subfolder
		File internalStorage = new File(DIR_STORIES);
		if (!internalStorage.exists()) internalStorage.mkdir();
		final ArrayList<File> files = new ArrayList<>(Arrays.asList(internalStorage.listFiles()));
		Collections.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				// For reverse order (most recently modified first), we need to swap the positions
				return Long.compare(f2.lastModified(), f1.lastModified());
			}
		});

		mAdapter = new ArrayAdapter<File>(this, R.layout.list_item_file, R.id.text1, files) {
			@NonNull
			@Override
			public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(R.id.text1);
				TextView text2 = (TextView) view.findViewById(R.id.text2);
				ImageView edit = (ImageView) view.findViewById(R.id.edit);
				ImageView delete = (ImageView) view.findViewById(R.id.delete);

				final String name = files.get(position).getName();
				text1.setText(name);
				text2.setText(DateUtils.getRelativeTimeSpanString(
						files.get(position).lastModified(),
						new Date().getTime(),
						0)
				);
				edit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Bundle bundle = new Bundle();
						bundle.putString(SaveDialogFragment.PARAM_NAME, name);
						mPosition = position;

						SaveDialogFragment dialog = new SaveDialogFragment();
						dialog.setArguments(bundle);
						dialog.show(getFragmentManager(), "save");
					}
				});
				delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mPosition = position;
						onDeleteClick();
					}
				});

				return view;
			}
		};

		ListView container = (ListView) findViewById(R.id.list_files);
		container.setAdapter(mAdapter);
		container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(MainActivity.this, StagingActivity.class);
				TextView text = (TextView) view.findViewById(R.id.text1);
				intent.putExtra(getString(R.string.key_file), (text.getText().toString()));
				startActivity(intent);
			}
		});

		View empty = findViewById(R.id.empty);
		container.setEmptyView(empty);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, StagingActivity.class);
				startActivity(intent);
			}
		});
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

		if (id == R.id.action_show_intro) {
			Intent intent = new Intent(MainActivity.this, IntroActivity.class);
			startActivityForResult(intent, SHOW_INTRODUCTION);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(String filename, boolean exitActivity) {
		// Get current file
		File oldFile = mAdapter.getItem(mPosition);
		if (oldFile == null) {
			Snackbar.make(findViewById(R.id.main_layout),
					R.string.error_file_rename, Snackbar.LENGTH_SHORT).show();
			return;
		}

		// Check if it's necessary to rename
		String oldName = oldFile.getName();
		if (oldName.equals(filename)) {
			return;
		}

		// Rename old file to new file
		File newFile = new File(DIR_STORIES, filename);
		if (!oldFile.renameTo(newFile)) {
			Snackbar.make(findViewById(R.id.main_layout),
					R.string.error_file_rename, Snackbar.LENGTH_SHORT).show();
		}

		// Replace old file in adapter with new file
		mAdapter.remove(oldFile);
		mAdapter.insert(newFile, mPosition);
	}

	public void onDeleteClick() {
		final File source = mAdapter.getItem(mPosition);

		if (source == null) {
			return;
		}

		final File destination = new File(DIR_DELETED, source.getName());
		mAdapter.remove(source);

		try {
			FileUtils.copyFile(source, destination); // Can also throw exception
			if (!source.delete()) throw new IOException();
		} catch (IOException e) {
			Snackbar.make(findViewById(R.id.main_layout),
					R.string.error_file_delete, Snackbar.LENGTH_SHORT).show();
		}

		mAdapter.notifyDataSetChanged();

		// The destination file will be deleted once the activity is created again,
		// unless the user wants to restore it through the following Snackbar

		Snackbar.make(findViewById(R.id.main_layout),
				R.string.success_story_delete, Snackbar.LENGTH_LONG)
				.setAction(R.string.button_undo, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Restore file
				try {
					FileUtils.copyFile(destination, source);
					if (!destination.delete()) throw new IOException();
				} catch (IOException e) {
					Snackbar.make(findViewById(R.id.main_layout),
							R.string.error_file_restore, Snackbar.LENGTH_SHORT).show();
				}

				// Insert file to view where it was located previously
				mAdapter.insert(source, mPosition);
				mAdapter.notifyDataSetChanged();
			}
		}).show();
	}
}
