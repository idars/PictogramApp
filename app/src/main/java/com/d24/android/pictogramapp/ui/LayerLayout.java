package com.d24.android.pictogramapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Custom layout for organizing layers of views. Main difference to a standard
 * FrameLayout is added support for rearranging, swapping and grouping of
 * child views.
 *
 * Also supports nested groups; groups are simply nested LayerLayouts with
 * their own view hierarchies.
 */
public class LayerLayout extends FrameLayout {

	public LayerLayout(Context context) {
		super(context);
	}

	public LayerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LayerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Indicates whether the specified view exists within this layout
	 */
	public boolean contains(View v) {
		return indexOfChild(v) != -1;
	}

	/**
	 * Indicates whether the specified view is the bottom layer
	 */
	public boolean isAtBottom(View v) {
		return indexOfChild(v) == 0;
	}

	/**
	 * Indicates whether the specified view is the top layer
	 */
	public boolean isAtTop(View v) {
		return indexOfChild(v) == getChildCount() - 1;
	}

	/**
	 * Swaps this view with the underlaying view, switching positions with each other
	 */
	public void moveDown(View v) {
		if (!contains(v) || isAtBottom(v)) {
			return;
		}

		int position = indexOfChild(v);
		View w = getChildAt(position - 1);
		swap(v, w);
	}

	/**
	 * Moves this view to the bottom of the stack, shifting underlaying views up
	 */
	public void moveToBottom(View v) {
		if (!contains(v) || isAtBottom(v)) {
			return;
		}

		int position = indexOfChild(v);
		removeViewAt(position);
		addView(v, 0);
	}

	/**
	 * Swaps this view with the overlaying view, switching positions with each other
	 */
	public void moveUp(View v) {
		if (!contains(v) || isAtTop(v)) {
			return;
		}

		int position = indexOfChild(v);
		View w = getChildAt(position + 1);
		swap(v, w);
	}

	/**
	 * Moves this view to the top of the stack, shifting overlaying views down
	 */
	public void moveToTop(View v) {
		if (!contains(v) || isAtTop(v)) {
			return;
		}

		int position = indexOfChild(v);
		removeViewAt(position);
		addView(v);
	}

	/**
	 * Swaps the two specified views with each other
	 */
	public void swap(View v, View w) {
		if (!contains(v) || !contains(w) || v == w) {
			return;
		}

		int positionV = indexOfChild(v);
		int positionW = indexOfChild(w);

		// Implementation notice: when keeping track of layer positions, it's
		// important to remove the uppermost layers first, letting positions
		// of layers that are yet to be removed remain unchanged. This also
		// applies to addition, where the lowermost layers should be added first.
		if (positionV < positionW) {
			removeViewAt(positionW);
			removeViewAt(positionV);
			addView(w, positionV);
			addView(v, positionW);
		} else {
			removeViewAt(positionV);
			removeViewAt(positionW);
			addView(v, positionW);
			addView(w, positionV);
		}
	}

	/**
	 * Groups the specified views into a child LayerLayout. The uppermost
	 * layer will stay uppermost, and the lowermost layer will stay lowermost.
	 * The group will replace the position of the uppermost layer.
	 */
	public LayerLayout group(View v, View w) {
		if (!contains(v) || !contains(w) || v == w) {
			return null;
		}

		LayerLayout group = new LayerLayout(getContext());
		int positionV = indexOfChild(v);
		int positionW = indexOfChild(w);

		if (positionV < positionW) {
			removeViewAt(positionW);
			removeViewAt(positionV);
			group.addView(v);
			group.addView(w);
			addView(group, positionW - 1);
		} else {
			removeViewAt(positionV);
			removeViewAt(positionW);
			group.addView(w);
			group.addView(v);
			addView(group, positionV - 1);
		}

		return group;
	}

	/**
	 * Groups the specified views into a child LayerLayout. The resulting
	 * LayerLayout group will have its children in the same order as they are
	 * in this parent LayerLayout. The group will replace the position of the
	 * uppermost layer.
	 */
	public LayerLayout group(View... views) {
		LayerLayout group = new LayerLayout(getContext());
		// We'll use a TreeSet to take care of both duplicates and order of layers
		TreeSet<Integer> positions = new TreeSet<>();

		for (View v : views) {
			if (!contains(v)) return null;
			positions.add(indexOfChild(v));
		}

		Iterator<Integer> iterator = positions.descendingIterator();
		while (iterator.hasNext()) {
			Integer position = iterator.next();
			removeViewAt(position);
			group.addView(views[position], 0);
		}

		// Need to consider that layers below have been removed, hence the subtraction
		addView(group, positions.last() - positions.size() + 1);

		return group;
	}

	/**
	 * Ungroups the specified group, putting its children in its place. The
	 * order of children remains intact. If the group contains LayerLayout
	 * children, they will remain as LayerLayout groups.
	 */
	public void ungroup(LayerLayout v) {
		if (!contains(v)) {
			return;
		}

		// Remove the original group
		int position = indexOfChild(v);
		removeViewAt(position);

		// Add the layers in the same order as in the group
		for (int i = 0; i < v.getChildCount(); ++i) {
			addView(v.getChildAt(i), position + i);
		}
	}
}
