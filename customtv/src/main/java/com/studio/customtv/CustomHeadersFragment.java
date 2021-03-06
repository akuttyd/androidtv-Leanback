package com.studio.customtv;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v17.leanback.app.HeadersFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.OnItemSelectedListener;
import android.support.v17.leanback.widget.Row;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;


public class CustomHeadersFragment extends HeadersFragment {

	private ArrayObjectAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		customSetBackground(R.color.fastlane_background);
		setOnItemSelectedListener(getDefaultItemSelectedListener());
		setHeaderAdapter();

		setCustomPadding();
	}

	private void setHeaderAdapter() {
		adapter = new ArrayObjectAdapter();

		LinkedHashMap<Integer, CustomRowsFragment> fragmentLinkedHashMap = ((CustomTvActivity) getActivity()).getFragmentLinkedHashMap();

		int id = 0;
		for (int i = 0; i < fragmentLinkedHashMap.size(); i++) {
			HeaderItem header = new HeaderItem(id, DummyDataList.HEADER_CATEGORY[i], null);
			ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
			innerAdapter.add(fragmentLinkedHashMap.get(i));
			adapter.add(id, new ListRow(header, innerAdapter));
			id++;
		}

		setAdapter(adapter);
	}

	private void setCustomPadding() {
		getView().setPadding(0, Utils.dpToPx(128, getActivity()), Utils.dpToPx(48, getActivity()), 0);
	}

	private OnItemSelectedListener getDefaultItemSelectedListener() {
		return new OnItemSelectedListener() {
			@Override
			public void onItemSelected(Object o, Row row) {
				Object obj = ((ListRow) row).getAdapter().get(0);
				getFragmentManager().beginTransaction().replace(R.id.rows_container, (Fragment) obj).commit();
				((CustomTvActivity) getActivity()).updateCurrentRowsFragment((CustomRowsFragment) obj);
			}
		};
	}

	/**
	 * Since the original setBackgroundColor is private, we need to
	 * access it via reflection
	 *
	 * @param colorResource The colour resource
	 */
	private void customSetBackground(int colorResource) {
		try {
			Class clazz = HeadersFragment.class;
			Method m = clazz.getDeclaredMethod("setBackgroundColor", Integer.TYPE);
			m.setAccessible(true);
			m.invoke(this, getResources().getColor(colorResource));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
