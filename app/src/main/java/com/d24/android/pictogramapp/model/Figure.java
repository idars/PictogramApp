package com.d24.android.pictogramapp.model;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.stickerview.StickerImageView;

public class Figure {

	public int id;
	public int color;
	public float x;
	public float y;
	public int size;
	public float rotation;
	public boolean mirrored;

	public Figure(int id, int color, float x, float y, int size, float rotation, boolean mirrored) {
		this.id = id;
		this.color = color;
		this.x = x;
		this.y = y;
		this.size = size;
		this.rotation = rotation;
		this.mirrored = mirrored;
	}

	/**
	 * Populates this class from the given view. Upon calling this constructor, this instance
	 * will have its settings complete, provided that the view is correctly set up.
	 *
	 * @param view the view representing this figure
	 */
	public Figure(StickerImageView view) {
		this.id = (int) view.getTag(R.integer.tag_id);
		this.color = (int) view.getTag(R.integer.tag_color);
		this.x = view.getX();
		this.y = view.getY();
		this.size = view.getWidth();
		this.rotation = view.getRotation();
		this.mirrored = view.isFlip();
	}

}
