package com.d24.android.pictogramapp.model;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.d24.android.pictogramapp.stickerview.StickerImageView;

import java.util.ArrayList;
import java.util.List;

public class Scene {

	public String background;
	public List<Figure> figures;

	public Scene(String background, List<Figure> figures) {
		this.background = background;
		this.figures = figures;
	}

	/**
	 * Populates this class from the given view. Upon calling this constructor, this instance
	 * will have a complete list of figures, provided that the view is correctly set up.
	 *
	 * @param view the view representing the figures in the scene
	 */
	public Scene(View view) {
		Drawable drawable = view.getBackground();
		if (drawable instanceof ColorDrawable) {
			int colorValue = ((ColorDrawable) drawable).getColor();
			background = "#" + Integer.toHexString(colorValue);
		} else {
			// TODO Determine drawable resource
		}

		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			figures = new ArrayList<>();
			for (int i = 0; i < group.getChildCount(); ++i) {
				StickerImageView sticker = (StickerImageView) group.getChildAt(i);
				figures.add(new Figure(sticker));
			}
		}
	}

}
