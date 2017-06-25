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
package org.sysdll.android.weatherapp.weather;

import org.sysdll.android.weatherapp.R;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * The class is used to find out the corresponding weather icon according to the
 * weather type id
 * 
 * The icons also separated into two different type individually. The larger one
 * is used for the current weather The smaller one is used for all forecast
 * weather
 */
public class IconFinder {

	public final static int TYPE_LARGE = 0;
	public final static int TYPE_SMALL = 1;

	private Context mContext;

	public IconFinder(Context context) {
		this.mContext = context;
	}

	public Drawable getIcon(int type, int holder) {
		switch (type) {
		case 200:
		case 201:
		case 202:
		case 210:
		case 211:
		case 212:
		case 221:
		case 230:
		case 231:
		case 232:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(
						R.drawable.thunderstorm);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(
						R.drawable.thunderstorm_l);
			}
		case 300:
		case 301:
		case 302:
		case 310:
		case 311:
		case 312:
		case 313:
		case 314:
		case 321:
		case 520:
		case 521:
		case 522:
		case 531:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(R.drawable.shower);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(R.drawable.shower_l);
			}
		case 500:
		case 501:
		case 502:
		case 503:
		case 504:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(R.drawable.rain);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(R.drawable.rain_l);
			}
		case 511:
		case 600:
		case 601:
		case 602:
		case 611:
		case 612:
		case 615:
		case 616:
		case 620:
		case 621:
		case 622:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(R.drawable.snow);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(R.drawable.snow_l);
			}
		case 701:
		case 711:
		case 721:
		case 731:
		case 741:
		case 751:
		case 761:
		case 762:
		case 771:
		case 781:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(R.drawable.mist);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(R.drawable.mist_l);
			}
		case 800:
			if (holder == TYPE_SMALL) {
				return mContext.getResources()
						.getDrawable(R.drawable.clear_sky);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(
						R.drawable.clear_sky_l);
			}
		case 801:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(
						R.drawable.few_clouds);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(
						R.drawable.few_clouds_l);
			}
		case 802:
		case 803:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(
						R.drawable.scattered_clouds);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(
						R.drawable.scattered_clouds_l);
			}
		case 804:
			if (holder == TYPE_SMALL) {
				return mContext.getResources().getDrawable(
						R.drawable.broken_clouds);
			} else if (holder == TYPE_LARGE) {
				return mContext.getResources().getDrawable(
						R.drawable.broken_clouds_l);
			}
		default:
			return null;
		}
	}

}