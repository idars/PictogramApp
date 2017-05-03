package com.d24.android.pictogramapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d24.android.pictogramapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ToolFragment.OnToolSelectedListener} interface
 * to handle interaction events.
 * Use the {@link ToolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolFragment extends Fragment implements View.OnClickListener {

	private OnToolSelectedListener mListener;
	private boolean redoAvailable;
	private boolean undoAvailable;
	private View contentView;

	public ToolFragment() {
		// Required empty public constructor
	}

	public static ToolFragment newInstance() {
		return new ToolFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		contentView = inflater.inflate(R.layout.fragment_tool, container, false);

		contentView.findViewById(R.id.button_add).setOnClickListener(this);
		contentView.findViewById(R.id.button_background).setOnClickListener(this);
		contentView.findViewById(R.id.button_undo).setOnClickListener(this);
		contentView.findViewById(R.id.button_redo).setOnClickListener(this);
		contentView.findViewById(R.id.button_info).setOnClickListener(this);

		setRedoAvailable(false);
		setUndoAvailable(false);

		return contentView;
	}

	@Override
	public void onClick(View view) {
		if (mListener == null) {
			return;
		}

		// Use the corresponding callback depending on which button is being pressed
		switch (view.getId()) {
			case R.id.button_add:
				mListener.onAddButtonSelected(view);
				break;
			case R.id.button_background:
				mListener.onBackgroundButtonSelected(view);
				break;
			case R.id.button_undo:
				if (undoAvailable) mListener.onUndoButtonSelected(view);
				break;
			case R.id.button_redo:
				if (redoAvailable) mListener.onRedoButtonSelected(view);
				break;
			case R.id.button_info:
				mListener.onInfoButtonSelected(view);
				break;
		}
	}

	/**
	 * Set whether the 'redo' button should be enabled. If the button is disabled,
	 * it will not trigger any callbacks.
	 *
	 * @param available true will enable the button, false will disable it
	 */
	public void setRedoAvailable(boolean available) {
		if (available) {
			// Enable button and make the color pop
			contentView.findViewById(R.id.button_redo).setAlpha(1f);
		} else {
			// Disable button and grey out button
			contentView.findViewById(R.id.button_redo).setAlpha(.5f);
		}

		redoAvailable = available;
	}

	/**
	 * Set whether the 'undo' button should be enabled. If the button is disabled,
	 * it will not trigger any callbacks.
	 *
	 * @param available true will enable the button, false will disable it
	 */
	public void setUndoAvailable(boolean available) {
		if (available) {
			// Enable button and make the color pop
			contentView.findViewById(R.id.button_undo).setAlpha(1f);
		} else {
			// Disable button and grey out button
			contentView.findViewById(R.id.button_undo).setAlpha(.5f);
		}

		undoAvailable = available;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnToolSelectedListener) {
			mListener = (OnToolSelectedListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnToolSelectedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnToolSelectedListener {
		/**
		 * Callback method for when a user clicks the 'add' button on the toolbar.
		 *
		 * @param v the view which is clicked on
		 */
		void onAddButtonSelected(View v);

		/**
		 * Callback method for when a user clicks the 'background' button on the toolbar.
		 *
		 * @param v the view which is clicked on
		 */
		void onBackgroundButtonSelected(View v);

		/**
		 * Callback method for when a user clicks the 'undo' button on the toolbar.
		 * This method will only be called when the button is enabled.
		 *
		 * @param v the view which is clicked on
		 */
		void onUndoButtonSelected(View v);

		/**
		 * Callback method for when a user clicks the 'redo' button on the toolbar.
		 * This method will only be called when the button is enabled.
		 *
		 * @param v the view which is clicked on
		 */
		void onRedoButtonSelected(View v);

		/**
		 * Callback method for when a user clicks the 'info' button on the toolbar.
		 *
		 * @param v the view which is clicked on
		 */
		void onInfoButtonSelected(View v);
	}
}
