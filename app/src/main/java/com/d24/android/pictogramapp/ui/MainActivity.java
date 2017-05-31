package com.d24.android.pictogramapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SaveDialogFragment.SaveDialogListener {

	private static final int SHOW_INTRODUCTION = 1;

	private ArrayAdapter<File> mAdapter;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.title_stories);
		setSupportActionBar(toolbar);

		File internalStorage = new File(getFilesDir() + File.separator + "stories");
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
						bundle.putString("filename", name);
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
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage(R.string.dialog_delete);
						builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								onConfirmationPositiveClick();
							}
						});
						builder.setNegativeButton(R.string.dialog_cancel, null);
						builder.create().show();
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
				intent.putExtra("filename", (text.getText().toString()));
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
	protected void onStart() {
		super.onStart();

		SharedPreferences pref = getSharedPreferences(
				getString(R.string.preference_file_key), MODE_PRIVATE
		);

		// Check if the app is started for the first time
		if (pref.getBoolean(getString(R.string.preference_value_firstrun), true)) {
			Intent intent = new Intent(this, IntroActivity.class);
			startActivityForResult(intent, SHOW_INTRODUCTION);
		}
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

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(String filename, boolean exitActivity) {
		// Get current file
		String directory = getFilesDir() + File.separator + "stories";
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
		File newFile = new File(directory, filename);
		if (!oldFile.renameTo(newFile)) {
			Snackbar.make(findViewById(R.id.main_layout),
					R.string.error_file_rename, Snackbar.LENGTH_SHORT).show();
		}

		// Replace old file in adapter with new file
		mAdapter.remove(oldFile);
		mAdapter.insert(newFile, mPosition);
	}

	public void onConfirmationPositiveClick() {
		File entry = mAdapter.getItem(mPosition);

		if (entry != null) {
			mAdapter.remove(entry);
			if (!entry.delete()) {
				Snackbar.make(findViewById(R.id.main_layout),
						R.string.error_file_delete, Snackbar.LENGTH_SHORT).show();
			}
		}

		mAdapter.notifyDataSetChanged();
	}
}
