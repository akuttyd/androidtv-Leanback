package com.studio.customtv;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomRowsFragment extends RowsFragment {

	private final int NUM_COLS = 15;

	private ArrayObjectAdapter rowsAdapter;
	private CardPresenter cardPresenter;

	// CustomHeadersFragment, scaled by 0.9 on a 1080p screen, is 600px wide.
	// This is the corresponding dip size.
	private static final int HEADERS_FRAGMENT_SCALE_SIZE = 300;
    private static final String TAG_FRAGMENT_CATEGORY_ID = "fragment_category_id";

    public static CustomRowsFragment newInstance(int categoryId){
        CustomRowsFragment f = new CustomRowsFragment();
        Bundle args = new Bundle();
        args.putInt(TAG_FRAGMENT_CATEGORY_ID , categoryId);
        f.setArguments(args);
        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);

		int marginOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEADERS_FRAGMENT_SCALE_SIZE, getResources().getDisplayMetrics());
		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		params.rightMargin -= marginOffset;
		v.setLayoutParams(params);

		//v.setBackgroundColor(getRandomColor());

        FrameLayout fm = (FrameLayout)v.getRootView();
        View viewChild = fm.getChildAt(0);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) viewChild.getLayoutParams();
        p.topMargin = Utils.dpToPx(65, getActivity());
        viewChild.setLayoutParams(p);

        TextView label = new TextView(getActivity());
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
        label.setPadding(Utils.dpToPx(50, getActivity()), 0, 0, 0);
        label.setText(MovieList.HEADER_CATEGORY[(int)getArguments().get(TAG_FRAGMENT_CATEGORY_ID)]);

        fm.addView(label);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadRows();

		setCustomPadding();
	}

	private void loadRows() {
		rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
		cardPresenter = new CardPresenter();

		List<Movie> list = MovieList.setupMovies();

		int i;
		for (i = 0; i < MovieList.MOVIE_CATEGORY.length; i++) {
			if (i != 0) {
				Collections.shuffle(list);
			}
			ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
			for (int j = 0; j < NUM_COLS; j++) {
				listRowAdapter.add(list.get(j % 5));
			}
			HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i], null);
			rowsAdapter.add(new ListRow(header, listRowAdapter));
		}

		setAdapter(rowsAdapter);
	}

	private void setCustomPadding() {
		getView().setPadding(Utils.dpToPx(-24, getActivity()), Utils.dpToPx(128, getActivity()), Utils.dpToPx(48, getActivity()), 0);
	}

	private int getRandomColor() {
		Random rnd = new Random();
		return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}

	public void refresh() {
		getView().setPadding(Utils.dpToPx(-24, getActivity()), Utils.dpToPx(128, getActivity()), Utils.dpToPx(300, getActivity()), 0);
	}
}
