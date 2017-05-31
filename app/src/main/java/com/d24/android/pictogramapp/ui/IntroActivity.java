package com.d24.android.pictogramapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d24.android.pictogramapp.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

public class IntroActivity extends AppIntro {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Use default slides for the first three pages, providing text, image and color for each
		addSlide(AppIntroFragment.newInstance(
				getString(R.string.welcome),
				getString(R.string.intro_about),
				R.drawable.intro_figure_1,
				ContextCompat.getColor(this, R.color.pink_500)
		));

		addSlide(AppIntroFragment.newInstance(
				getString(R.string.intro_adding_title),
				getString(R.string.intro_adding_description),
				R.drawable.intro_figure_2,
				ContextCompat.getColor(this, R.color.amber_500)
		));

		addSlide(AppIntroFragment.newInstance(
				getString(R.string.intro_editing_title),
				getString(R.string.intro_editing_description),
				R.drawable.intro_figure_3,
				ContextCompat.getColor(this, R.color.indigo_500)
		));

		// Use a custom slide fragment for the last page, to display multiple images
		addSlide(CreditsFragment.newInstance());

		setColorDoneText(Color.BLACK);
		showSkipButton(true);
		setProgressButtonEnabled(true);
	}

	@Override
	public void onSkipPressed(Fragment currentFragment) {
		// Do something when users tap on Skip button.
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onDonePressed(Fragment currentFragment) {
		// Do something when users tap on Done button.
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
		super.onSlideChanged(oldFragment, newFragment);
		// The last page has a light background, so we want to invert the HUD colors
		if (newFragment instanceof CreditsFragment) {
			setSeparatorColor(Color.BLACK);
			setIndicatorColor(Color.BLACK, Color.GRAY);
		} else {
			setSeparatorColor(Color.WHITE);
			setIndicatorColor(Color.WHITE, Color.GRAY);
		}
	}

	// Fragment representing the third page
	public static class CreditsFragment extends Fragment implements ISlideBackgroundColorHolder {

		private View mContent;

		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			mContent = inflater.inflate(R.layout.fragment_credits, container, false);
			return mContent;
		}

		public static CreditsFragment newInstance() {
			Bundle args = new Bundle();
			CreditsFragment fragment = new CreditsFragment();
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getDefaultBackgroundColor() {
			// Return the default background color of the slide.
			return Color.parseColor("#000000");
		}

		@Override
		public void setBackgroundColor(@ColorInt int backgroundColor) {
			// Set the background color of the view within your slide to which the transition should be applied.
			if (mContent != null) {
				mContent.setBackgroundColor(backgroundColor);
			}
		}
	}
}