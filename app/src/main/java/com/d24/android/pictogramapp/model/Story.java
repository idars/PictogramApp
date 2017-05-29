package com.d24.android.pictogramapp.model;

import android.support.v4.view.ViewPager;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.ui.LayerLayout;

import java.util.ArrayList;
import java.util.List;

public class Story {

	public String title;
	public List<Scene> scenes;

	public Story(String title, List<Scene> scenes) {
		this.title = title;
		this.scenes = scenes;
	}

	/**
	 * Populates this class from the given ViewPager. Upon calling this constructor, this instance
	 * will have a complete list of scenes, provided that the view is correctly set up.
	 *
	 * @param title the name of the story
	 * @param view the view representing the scenes in the story
	 */
	public Story(String title, ViewPager view) {
		this.title = title;
		scenes = new ArrayList<>();
		for (int i = 0; i < view.getChildCount(); ++i) {
			LayerLayout layer = (LayerLayout) view.getChildAt(i).findViewById(R.id.layer_layout);
			scenes.add(new Scene(layer));
		}
	}

}
