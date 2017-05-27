package com.d24.android.pictogramapp.util;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.d24.android.pictogramapp.ui.EditingFragment;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {

	// Her oppbevarer, legger til og fjerner vi view som skal vises i ViewPager
	// Viewene er persistente og fjernes ikke selv om de kommer ut av syne
	private ArrayList<View> views = new ArrayList<>();

	public MyViewPagerAdapter() {
		super();
	}

	// Arvede metoder

	// Forteller ViewPager på hvilken side viewet (object) skal vises
	@Override
	public int getItemPosition(Object object) {
		int index = views.indexOf(object);
		if (index == -1) {
			return POSITION_NONE;
		} else {
			return index;
		}
	}

	// Kalles av ViewPager når det skal vise et view
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = views.get(position);
		container.addView(v);
		return v;
	}

	// Kalles av ViewPager når det skal fjerne et view
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	// Kalles av ViewPager
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	// Kalles av ViewPager
	@Override
	public int getCount() {
		return views.size();
	}

	// Brukerdefinerte metoder

	public View getView(int position) {
		return views.get(position);
	}

	public List getAllViews() {
		return views;
	}

	public int addView(View v) {
		// For å legge til view bak i listen
		return addView(v, views.size());
	}

	public int addView(View v, int position) {
		// For å legge til view i listen
		if (position == views.size()) {
			views.add(v);
		} else {
			views.add(position, v);
		}
		notifyDataSetChanged();
		return position;
	}

	public int removeView(ViewPager pager, View v) {
		// For å fjerne fragment fra listen
		return removeView(pager, views.indexOf(v));
	}

	public int removeView(ViewPager pager, int position) {
		// ViewPager doesn't have a delete method; the closest is to set the adapter
		// again.  When doing so, it deletes all its views.  Then we can delete the view
		// from from the adapter and finally set the adapter to the pager again.  Note
		// that we set the adapter to null before removing the view from "views" - that's
		// because while ViewPager deletes all its views, it will call destroyItem which
		// will in turn cause a null pointer ref.
		pager.setAdapter(null);
		views.remove(position);
		pager.setAdapter(this);
		notifyDataSetChanged();
		return position;
	}
}
