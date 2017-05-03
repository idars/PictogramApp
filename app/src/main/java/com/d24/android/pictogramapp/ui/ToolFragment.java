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
		View view = inflater.inflate(R.layout.fragment_tool, container, false);

		view.findViewById(R.id.button_add).setOnClickListener(this);
		view.findViewById(R.id.button_background).setOnClickListener(this);
		view.findViewById(R.id.button_undo).setOnClickListener(this);
		view.findViewById(R.id.button_redo).setOnClickListener(this);
		view.findViewById(R.id.button_info).setOnClickListener(this);

		return view;
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
				mListener.onUndoButtonSelected(view);
				break;
			case R.id.button_redo:
				mListener.onRedoButtonSelected(view);
				break;
			case R.id.button_info:
				mListener.onInfoButtonSelected(view);
				break;
			default:
				break;
		}
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
		void onAddButtonSelected(View v);
		void onBackgroundButtonSelected(View v);
		void onUndoButtonSelected(View v);
		void onRedoButtonSelected(View v);
		void onInfoButtonSelected(View v);
	}
}
