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
package org.sysdll.android.weatherapp;

import java.util.Map;

import org.sysdll.android.weatherapp.utils.Constants;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class WeatherApplication extends FragmentActivity implements Constants {

	// Google Analytics Tracker Object
	private static EasyTracker mEasyTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		// May return null if EasyTracker has not been initialized with a
		// property ID.
		// Initialize a tracker
		mEasyTracker = EasyTracker.getInstance(this);
		// When dry run is set, hits will not be dispatched, but will still be
		// logged as
		// though they were dispatched.
		GoogleAnalytics.getInstance(this).setDryRun(DEBUG);
	}

	/**
	 * The class used to measure user timings and then send data the Google
	 * Analytics
	 * 
	 */
	public class TimingTracker {

		private long startTimeInMillis, endTimeInMillis;

		public TimingTracker() {
		}

		// Get the start time of event in milliseconds
		public void startTrack() {
			startTimeInMillis = System.currentTimeMillis();
		}

		// Send the measure timing data to Google Analytics
		public void endTrack() {
			endTimeInMillis = System.currentTimeMillis();
			long loadTime = endTimeInMillis - startTimeInMillis;
			Map<String, String> data = createTimingData(loadTime);
			if (mEasyTracker != null && data != null) {
				mEasyTracker.send(data);
			}
		}

		// Create the timing data
		public Map<String, String> createTimingData(long loadTime) {
			return null;
		};
	}
	
	/**
	 * Measure the search weather event
	 */
	public void trackWeatherSearch() {
		// MapBuilder.createEvent().build() returns a Map of event fields and
		// values that are set and sent with the hit.
		if (mEasyTracker != null) {
			mEasyTracker.send(MapBuilder.createEvent(
								"ui_action", // Event category (required)
								"button_press", // Event action (required)
								"search_weather", // Event label
								null) // Event value
						.build());
		}
	}
	
	/**
	 * Measure the refresh weather event
	 */
	public void trackRefresh() {
		// MapBuilder.createEvent().build() returns a Map of event fields and
		// values that are set and sent with the hit.
		if (mEasyTracker != null) {
			mEasyTracker.send(MapBuilder.createEvent(
								"ui_action", // Event category (required)
								"button_press", // Event action (required)
								"refresh_button", // Event label
								null) // Event value
						.build());
		}
	}
	
	/**
	 * Measure the change temperature unit event
	 */
	public void trackChangeUnit() {
		// MapBuilder.createEvent().build() returns a Map of event fields and
		// values that are set and sent with the hit.
		if (mEasyTracker != null) {
			mEasyTracker.send(MapBuilder.createEvent(
								"ui_action", // Event category (required)
								"button_press", // Event action (required)
								"change_unit_button", // Event label
								null) // Event value
						.build());
		}
	}

	/**
	 * Track the click on info button
	 */
	public void trackInfo() {
		// MapBuilder.createEvent().build() returns a Map of event fields and
		// values that are set and sent with the hit.
		if (mEasyTracker != null) {
			mEasyTracker.send(MapBuilder.createEvent(
								"ui_action", // Event category (required)
								"button_press", // Event action (required)
								"info_button", // Event label
								null) // Event value
						.build());
		}
	}
	
	/**
	 * Measure the user setting reminder
	 */
	public void trackRemind() {
		// MapBuilder.createEvent().build() returns a Map of event fields and
		// values that are set and sent with the hit.
		if (mEasyTracker != null) {
			mEasyTracker.send(MapBuilder.createEvent(
								"ui_action", // Event category (required)
								"dialog_positive_press", // Event action (required)
								"remind_me_button", // Event label
								null) // Event value
						.build());
		}
	}
}