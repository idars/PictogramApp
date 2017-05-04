package com.d24.android.pictogramapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d24.android.pictogramapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnToolClickedListener} interface
 * to handle interaction events.
 * Use the {@link ToolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolFragment extends Fragment implements View.OnClickListener {

	private OnToolClickedListener mListener;
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
				mListener.onAddButtonClicked(view);
				break;
			case R.id.button_background:
				mListener.onBackgroundButtonClicked(view);
				break;
			case R.id.button_undo:
				if (undoAvailable) mListener.onUndoButtonClicked(view);
				break;
			case R.id.button_redo:
				if (redoAvailable) mListener.onRedoButtonClicked(view);
				break;
			case R.id.button_info:
				mListener.onInfoButtonClicked(view);
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
			contentView.findViewById(R.id.button_redo).setClickable(true);
			contentView.findViewById(R.id.button_redo).setFocusable(true);
		} else {
			// Disable button and grey out button
			contentView.findViewById(R.id.button_redo).setAlpha(.5f);
			contentView.findViewById(R.id.button_redo).setClickable(false);
			contentView.findViewById(R.id.button_redo).setFocusable(false);
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
			contentView.findViewById(R.id.button_undo).setClickable(true);
			contentView.findViewById(R.id.button_undo).setFocusable(true);
		} else {
			// Disable button and grey out button
			contentView.findViewById(R.id.button_undo).setAlpha(.5f);
			contentView.findViewById(R.id.button_undo).setClickable(false);
			contentView.findViewById(R.id.button_undo).setFocusable(false);
		}

		undoAvailable = available;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnToolClickedListener) {
			mListener = (OnToolClickedListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnToolClickedListener");
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
	public interface OnToolClickedListener {
		/**
		 * Callback method for when a user clicks the 'add' button on the toolbar.
		 *
		 * @param v the view which is clicked on
		 */
		void onAddButtonClicked(View v);

		/**
		 * Callback method for when a user clicks the 'background' button on the toolbar.
		 *
		 * @param v the view which is clicked on
		 */
		void onBackgroundButtonClicked(View v);

		/**
		 * Callback method for when a user clicks the 'undo' button on the toolbar.
		 * This method will only be called when the button is enabled.
		 *
		 * @param v the view which is clicked on
		 */
		void onUndoButtonClicked(View v);

		/**
		 * Callback method for when a user clicks the 'redo' button on the toolbar.
		 * This method will only be called when the button is enabled.
		 *
		 * @param v the view which is clicked on
		 */
		void onRedoButtonClicked(View v);

		/**
		 * Callback method for when a user clicks the 'info' button on the toolbar.
		 *
		 * @param v the view which is clicked on
		 */
		void onInfoButtonClicked(View v);
	}
}
