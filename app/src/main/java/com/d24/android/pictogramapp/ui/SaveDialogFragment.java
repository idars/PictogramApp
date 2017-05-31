package com.d24.android.pictogramapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.d24.android.pictogramapp.R;

public class SaveDialogFragment extends DialogFragment {

	private static final String PARAM_FILENAME = "filename";

	private String mFilename;

	private boolean exitActivity = false;

	public interface SaveDialogListener {
		void onDialogPositiveClick(String filename, boolean exitActivity);
	}

	SaveDialogListener mListener;
	Context mContext;
	EditText mField;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		mContext = context;
		Activity activity = getActivity();

		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			mListener = (SaveDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement SaveDialogListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mFilename = getArguments().getString(PARAM_FILENAME);
		}
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT
		);
		int margin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
		params.setMargins(margin, 0, margin, 0);

		mField = new EditText(mContext);
		mField.setHint(R.string.field_name);
		mField.setLayoutParams(params);

		if (mFilename != null) {
			mField.setText(mFilename);
			mField.setSelection(mField.getText().length());
		}

		FrameLayout frame = new FrameLayout(mContext);
		frame.addView(mField);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_save)
				.setView(frame)
				.setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String filename = mField.getText().toString();
						if (!filename.isEmpty()) {
							mListener.onDialogPositiveClick(
									mField.getText().toString(), exitActivity
							);
						}
					}
				})
				.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						SaveDialogFragment.this.getDialog().cancel();
						exitActivity = false;
					}
				});

		return builder.create();
	}

	public void setExitActivity(boolean exitActivity) {
		this.exitActivity = exitActivity;
	}



}
