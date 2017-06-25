/*
 * Copyright (C) 2017 sysdll.org 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.sysdll.android.weatherapp.utils.ui;

import org.sysdll.android.weatherapp.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class HorizontalViewGallery extends LinearLayout {

	@SuppressWarnings("unused")
	private Context mContext;

	public HorizontalViewGallery(Context context) {
		super(context);
		this.mContext = context;
	}

	public HorizontalViewGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public HorizontalViewGallery(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	/**
	 * Add child view to the gallery
	 * 
	 * @param view
	 *            - view to be added
	 */
	public void add(View view) {
		addView(view);
	}

	/**
	 * Evenly distribute all child views up to 3 views on the scrollable
	 * horizontal gallery view
	 * 
	 * @param width
	 *            - width of child view
	 * @param height
	 *            - height of child view
	 */
	public void setChildSize(float width, float height) {
		// Get the number of child item
		int numOfChild = getChildCount();
		LayoutParams lp = null;

		if (numOfChild == 0)
			return;
		else if (numOfChild == 1)
			lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		else if (numOfChild == 2)
			lp = new LayoutParams((int) width / 2, LayoutParams.MATCH_PARENT);
		else
			lp = new LayoutParams((int) width / 3, LayoutParams.MATCH_PARENT);

		for (int i = 0; i < getChildCount(); i++) {
			// Update the size of each child view
			getChildAt(i).setLayoutParams(lp);

			// Set the background color of child view
			if (i % 6 == 0)
				getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.forecast_grad_1));
			else if (i % 6 == 1)
				getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.forecast_grad_2));
			else if (i % 6 == 2)
				getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.forecast_grad_3));
			else if (i % 6 == 3)
				getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.forecast_grad_4));
			else if (i % 6 == 4)
				getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.forecast_grad_5));
			else if (i % 6 == 5)
				getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.forecast_grad_6));
		}

	}
}