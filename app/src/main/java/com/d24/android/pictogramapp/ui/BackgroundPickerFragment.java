package com.d24.android.pictogramapp.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.util.GridViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnBackgroundSelectedListener} interface
 * to handle interaction events.
 * Use the {@link BackgroundPickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackgroundPickerFragment extends Fragment {

	private OnBackgroundSelectedListener mListener;
	private View mContentView;
	private GridView mGridView;
	private GridViewAdapter mGridAdapter;

	public BackgroundPickerFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment BackgroundPickerFragment.
	 */
	public static BackgroundPickerFragment newInstance() {
		return new BackgroundPickerFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mContentView = inflater.inflate(R.layout.fragment_background_picker, container, false);

		mGridView = (GridView) mContentView.findViewById(R.id.grid_background);
		mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, getData());
		mGridView.setAdapter(mGridAdapter);
		mGridView.setClickable(true);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				mListener.onSolidColorSelected(adapterView, view, i, l);
			}
		});

		return mContentView;
	}

	// Prepare array data for GridView to show
	private ArrayList<ColorDrawable> getData() {
		final ArrayList<ColorDrawable> imageItems = new ArrayList<>();
		Resources res = getResources();
		TypedArray colors = res.obtainTypedArray(R.array.background_colors);

		for (int i = 0; i < colors.length(); i++) {
			// TODO, confirm if functioning correctly
			int defValue =  -1;
			int color = colors.getColor(i, defValue);
			ColorDrawable drawable = new ColorDrawable(color);
			imageItems.add(drawable);

		}

		return imageItems;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnBackgroundSelectedListener) {
			mListener = (OnBackgroundSelectedListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnBackgroundSelectedListener");
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
	public interface OnBackgroundSelectedListener {

		void onSolidColorSelected(AdapterView<?> adapterView, View view, int i, long l);
		// void onTwinColourSelected(AdapterView<?> adapterView, View view, int i, long l);
		// void onBackgroundSelected(AdapterView<?> adapterView, View view, int i, long l);
	}
}
