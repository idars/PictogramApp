package com.d24.android.pictogramapp.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.d24.android.pictogramapp.ui.EditingFragment;

import java.util.ArrayList;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

	// Her oppbevarer, legger til og fjerner vi fragmenter som skal vises i ViewPager
	// Fragmentene er persistente og fjernes ikke selv om de kommer ut av syne
	private ArrayList<EditingFragment> fragments = new ArrayList<>();

	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	// Arvede metoder

	@Override
	public int getItemPosition(Object object) {
		// Forteller ViewPager på hvilken side fragmentet (object) skal vises
		int index = fragments.indexOf(object);
		if (index == -1) {
			return POSITION_NONE;
		} else {
			return index;
		}
	}

	/*
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// Kalles av ViewPager når det skal vise et fragment
		EditingFragment v = fragments.get(position);
		container.addView(v.getView());
		return v;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Kalles av ViewPager når det skal fjerne et fragment
		container.removeView(fragments.get(position).getView());
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// Kalles av ViewPager
		EditingFragment v = (EditingFragment) object;
		return view == v.getView();
	}
	*/

	@Override
	public Fragment getItem(int position) {
		// Kalles av ViewPager, kan kalles av app
		return fragments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public int getCount() {
		// Kalles av ViewPager
		return fragments.size();
	}

	// Brukerdefinerte metoder

	public int addFragment(EditingFragment e) {
		// For å legge til fragment bak i listen
		return addFragment(e, fragments.size());
	}

	public int addFragment(EditingFragment e, int position) {
		// For å legge til fragment i listen
		if (position == fragments.size()) {
			fragments.add(e);
		} else {
			fragments.add(position, e);
		}
		notifyDataSetChanged();
		return position;
	}

	public int removeFragment(ViewPager pager, EditingFragment e) {
		// For å fjerne fragment fra listen
		return removeFragment(pager, fragments.indexOf(e));
	}

	public int removeFragment(ViewPager pager, int position) {
		// ViewPager doesn't have a delete method; the closest is to set the adapter
		// again.  When doing so, it deletes all its views.  Then we can delete the view
		// from from the adapter and finally set the adapter to the pager again.  Note
		// that we set the adapter to null before removing the view from "views" - that's
		// because while ViewPager deletes all its views, it will call destroyItem which
		// will in turn cause a null pointer ref.
		pager.setAdapter(null);
		fragments.remove(position);
		pager.setAdapter(this);
		notifyDataSetChanged();
		return position;
	}
}
