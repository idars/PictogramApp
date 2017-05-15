package com.d24.android.pictogramapp.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d24.android.pictogramapp.R;

public class PreviewFragment extends Fragment{

	// TODO, not yet, private OnPreviewClickedListener mListener;

	private View contentView;

	public PreviewFragment() {
		// Required empty public constructor
	}

	public static PreviewFragment newInstance() {
		return new PreviewFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		contentView = inflater.inflate(R.layout.fragment_preview, container, false);

		ImageView img = new ImageView(getContext());
		Drawable draw = ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_circle_black_24dp);
		img.setImageDrawable(draw);
		img.setColorFilter(666);


		return contentView;
	}


}
