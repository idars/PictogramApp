package com.d24.android.pictogramapp.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SaveDialogFragment.SaveDialogListener {

	private static final int SHOW_INTRODUCTION = 1;

	private ArrayAdapter<File> mAdapter;
	private ListView mContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.title_stories);
		setSupportActionBar(toolbar);

		File internalStorage = getFilesDir();
		final File[] files = internalStorage.listFiles();

		mAdapter = new ArrayAdapter<File>(this, R.layout.list_item_file, R.id.text1, files) {
			@Override
			public View getView(int position, View convertView, @NonNull ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(R.id.text1);
				TextView text2 = (TextView) view.findViewById(R.id.text2);
				ImageView edit = (ImageView) view.findViewById(R.id.edit);
				ImageView delete = (ImageView) view.findViewById(R.id.delete);

				DateFormat formatter = DateFormat.getDateInstance();
				text1.setText(files[position].getName());
				text2.setText(formatter.format(new Date(files[position].lastModified())));
				edit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						SaveDialogFragment dialog = new SaveDialogFragment();
						// TODO set current filename as argument
						dialog.show(getFragmentManager(), "save");
					}
				});
				delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO show confirmation
					}
				});

				return view;
			}
		};

		mContainer = (ListView) findViewById(R.id.list_files);
		mContainer.setAdapter(mAdapter);
		mContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(MainActivity.this, StagingActivity.class);
				TextView text = (TextView) view.findViewById(R.id.text1);
				intent.putExtra("filename", (text.getText().toString()));
				startActivity(intent);
			}
		});

		View empty = findViewById(R.id.empty);
		mContainer.setEmptyView(empty);

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
	public void onDialogPositiveClick(DialogFragment dialog, String filename) {
		File internalStorage = getFilesDir();
	}

	public void onConfirmationPositiveClick(DialogFragment dialog, int index) {
		mAdapter.remove(mAdapter.getItem(index));
		mAdapter.notifyDataSetChanged();
	}
}
