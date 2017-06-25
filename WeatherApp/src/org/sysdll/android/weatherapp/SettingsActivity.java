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

import org.sysdll.android.weatherapp.logger.Logger;
import org.sysdll.android.weatherapp.request.RequestConstants;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

public class SettingsActivity extends LocationBaseScreen implements
		OnSharedPreferenceChangeListener {

	protected SharedPreferences mPreferences = null;
	protected SharedPreferences.Editor mEditor = null;

	protected final static String KEY_PREFERENCES = "KEY_WEATHER";
	protected final static String KEY_TEMP_UNIT = "KEY_TEMP_UNIT";
	protected final static String KEY_TEMP_UNIT_SYM = "KEY_TEMP_UNIT_SYM";
	protected final static String KEY_CITY = "KEY_CITY";

	protected final static String TEMP_SYM_C = (char) 0x00B0 + "C";
	protected final static String TEMP_SYM_F = (char) 0x00B0 + "F";
	protected final static String TEMP_DEFAULT_SYM = TEMP_SYM_C;

	@SuppressLint("CommitPrefEdits")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = getApplicationContext().getSharedPreferences(
				KEY_PREFERENCES, Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Registers a listener whenever a key changes
		mPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Unregisters the listener set in onResume().
		// It's best practice to unregister listeners when your app isn't using
		// them to cut down on
		// unnecessary system overhead. You do this in onPause().
		mPreferences.unregisterOnSharedPreferenceChangeListener(this);

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// When the user changes the preferences selection,
	// onSharedPreferenceChanged() restarts the main activity as a new task.
	// the main activity should update its display based on the new setting.
	// The main activity queries the PreferenceManager to get the latest
	// settings.
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

	/**
	 * Clear all settings
	 */
	public void clearSettings() {
		Logger.printMessage(TAG, "Clear settings", Logger.DEBUG);
		mEditor.clear().apply();
	}

	/**
	 * Set the temperature unit
	 */
	public void setTempUnit(String unit) {
		Logger.printMessage(TAG, "set temperature unit " + unit, Logger.DEBUG);
		mEditor.putString(KEY_TEMP_UNIT, unit).apply();
	}

	/**
	 * Get the temperature unit
	 * 
	 * @return get the temperature unit
	 */
	public String getTempUnit() {
		String unit = mPreferences.getString(KEY_TEMP_UNIT,
				RequestConstants.PARAM_DEFAULT_TEMP_UNITS);
		Logger.printMessage(TAG, "get temperature unit " + unit, Logger.DEBUG);
		return unit;
	}

	/**
	 * Set the temperature unit symbol
	 */
	public void setTempUnitSymbol(String sym) {
		Logger.printMessage(TAG, "set temperature unit  sym" + sym,
				Logger.DEBUG);
		mEditor.putString(KEY_TEMP_UNIT_SYM, sym).apply();
	}

	/**
	 * Get the temperature unit symbol
	 * 
	 * @return get the temperature unit symbol
	 */
	public String getTempUnitSymbol() {
		String sym = mPreferences
				.getString(KEY_TEMP_UNIT_SYM, TEMP_DEFAULT_SYM);
		Logger.printMessage(TAG, "get temperature unit sym " + sym,
				Logger.DEBUG);
		return sym;
	}

	/**
	 * Set the city
	 * 
	 * @param city
	 *            - the city which its weather being searched.
	 */
	public void setCity(String city) {
		Logger.printMessage(TAG, "set city" + city, Logger.DEBUG);
		mEditor.putString(KEY_CITY, city).apply();
	}

	/**
	 * Get the city
	 * 
	 * @return get the city which its weather being searched.
	 */
	public String getCity() {
		String city = mPreferences.getString(KEY_CITY, null);
		Logger.printMessage(TAG, "get city " + city, Logger.DEBUG);
		return city;
	}
}
