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

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.sysdll.android.weatherapp.R;
import org.sysdll.android.weatherapp.logger.Logger;
import org.sysdll.android.weatherapp.model.DataManager;
import org.sysdll.android.weatherapp.utils.MyAlertDialog;


public class BaseScreen extends WeatherApplication {
	protected final String TAG = ((Object) this).getClass().getSimpleName();

	protected NetworkReceiver mNetworkReceiver;
	protected DataManager mDataManager;

	protected SharedPreferences mPromptPrefs = null;
	protected SharedPreferences.Editor mPromptEditor = null;
	protected final static String PREFS_PROMPT = "PREFS_PROMPT";
	protected final static String PREFS_STOP_PROMPT = "PREFS_STOP_PROMPT";
	protected final static String PREFS_PROMPT_DATE = "PREFS_PROMPT_DATE";
	protected final static int DAYS_UNITL_PROMPT = 7;

	public BaseScreen() {
		Logger.printMessage(TAG, "Initial Screen", Logger.DEBUG);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.printMessage(TAG, "onCreate()", Logger.DEBUG);

		// Create preferences for the prompt dialog
		mDataManager = DataManager.getInstance();
		mPromptPrefs = getApplicationContext().getSharedPreferences(
				PREFS_PROMPT, Context.MODE_PRIVATE);
		mPromptEditor = mPromptPrefs.edit();

		if (getPromptDateInMillis() == 0) {
			// First Launch - don't prompt rate me dialog
			// Set the first prompt date
			long promptDate = System.currentTimeMillis()
					+ DateUtils.DAY_IN_MILLIS * DAYS_UNITL_PROMPT;
			setPromptDateInMillis(promptDate);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.printMessage(TAG, "onResume()", Logger.DEBUG);

		if (System.currentTimeMillis() >= getPromptDateInMillis()
				&& !isStopPrompt()) {
			// Reset the prompt dialog
			long promptDate = System.currentTimeMillis()
					+ DateUtils.DAY_IN_MILLIS * DAYS_UNITL_PROMPT;
			setPromptDateInMillis(promptDate);

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// Prompt the rate me dialog
					MyAlertDialog dialog = new MyAlertDialog(BaseScreen.this,
							"Rate " + getApplicationName(),
							getString(R.string.rate_me_message), false);
					dialog.setPositiveButton(
							getString(R.string.dialog_rate_me),
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Don't prompt the rate me dialog again
									setStopPrompt(true);
									// Direct user to the app page in play store
									// for rating
									try {
										startActivity(new Intent(
												Intent.ACTION_VIEW,
												Uri.parse("market://details?id="
														+ getApplicationContext()
																.getPackageName())));
									} catch (ActivityNotFoundException e) {
										Toast.makeText(BaseScreen.this,
												"Could not launch Play Store!",
												Toast.LENGTH_SHORT).show();
									}
									dialog.dismiss();
								}
							});
					dialog.setNeutralButton(getString(R.string.dialog_later),
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					dialog.setNegativeButton(getString(R.string.dialog_no),
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Never prompt the rate me dialog
									setStopPrompt(true);
									dialog.dismiss();
								}
							});
					dialog.show();
				}
			});
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.printMessage(TAG, "onStart()", Logger.DEBUG);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.printMessage(TAG, "onPause()", Logger.DEBUG);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.printMessage(TAG, "onStop()", Logger.DEBUG);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.printMessage(TAG, "onDestroy()", Logger.DEBUG);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Logger.printMessage(TAG, "onWindowFocusChanged()", Logger.DEBUG);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Logger.printMessage(TAG, "onConfigurationChanged()", Logger.DEBUG);
	}

	/**
	 * Function to hide the soft keyboard
	 * 
	 * @param edittext
	 *            - The context of the window that is currently being focused
	 */
	protected void hideSoftKeyboard(EditText edittext) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
	}

	/**
	 * Function to show the soft keyboard
	 * 
	 * @param edittext
	 *            - The context of the window that is currently being focused
	 */
	protected void showSoftKeyboard(EditText edittext) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * The subclass of the BroadcastReeiver is used to detect the change of
	 * connectivity.
	 * 
	 * @author Cyclic Studio
	 * 
	 */
	public class NetworkReceiver extends BroadcastReceiver {

		public NetworkReceiver() {
			super();
		}

		public void onNoConnectivity() {
		};

		public void onNetworkChange() {
		};

		public void onConnect() {
		};

		public void onReconnect() {
		};

		public void toggleNetworkSourceSetting() {
			startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager conn = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activNetworkInfo = conn.getActiveNetworkInfo();

			String failMessage = intent
					.getStringExtra(ConnectivityManager.EXTRA_REASON);
			Logger.printMessage(getClass().getSimpleName(), failMessage,
					Logger.DEBUG);
			Boolean isNoConnectivity = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			Boolean isFailOver = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_IS_FAILOVER, false);
			Logger.printMessage(getClass().getSimpleName(), "is Failover: "
					+ isFailOver, Logger.DEBUG);
			Boolean isNetworkChanged = false;

			NetworkInfo otherNetworkInfo = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
			NetworkInfo networkInfo = (NetworkInfo) intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

			Logger.printMessage(getClass().getSimpleName(),
					"Current Network Info : " + networkInfo, Logger.DEBUG);
			Logger.printMessage(getClass().getSimpleName(),
					"Other Network Info : " + otherNetworkInfo, Logger.DEBUG);

			if (networkInfo != null && networkInfo.isConnected()) {
				if (isFailOver) {
					Logger.printMessage(getClass().getSimpleName(),
							"Network is re-connected and available now",
							Logger.DEBUG);
					onReconnect();
					return;
				} else {
					Logger.printMessage(getClass().getSimpleName(),
							"Network is available", Logger.DEBUG);
					onConnect();
					return;
				}

			} else if (networkInfo != null
					&& !networkInfo.isConnectedOrConnecting()) {
				// do application-specific task(s) based on the current network
				// state, such
				// as enabling queuing of HTTP requests when currentNetworkInfo
				// is connected etc.
				if (otherNetworkInfo != null
						&& otherNetworkInfo.isConnectedOrConnecting()) {
					isNetworkChanged = true;
				} else {
					Logger.printMessage(getClass().getSimpleName(),
							"No network is available", Logger.DEBUG);
					onNoConnectivity();
					return;
				}
			}

			// No network is active OR no network is available
			if (activNetworkInfo == null || isNoConnectivity) {
				if (isNetworkChanged) {
					Logger.printMessage(getClass().getSimpleName(),
							"Change network connectivity", Logger.DEBUG);
					onNetworkChange();
					return;
				} else {
					Logger.printMessage(getClass().getSimpleName(),
							"No network is available", Logger.DEBUG);
					onNoConnectivity();
					return;
				}
			}
		}
	}

	/**
	 * Get the screen size
	 * 
	 * @param screenSize
	 *            - an array of floats to hold the size of screen
	 */
	protected void getScreenSize(float[] screenSize) {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		// Convert the height of screen from pixels to dp
		float dpHeight = displaymetrics.heightPixels;
		// Convert the width of screen from pixels to dp
		float dpWidth = displaymetrics.widthPixels;

		screenSize[0] = dpWidth;
		screenSize[1] = dpHeight;
	}

	public void setPromptDateInMillis(long promptDateInMillis) {
		Logger.printMessage(TAG, "set promptDateInMillis" + promptDateInMillis,
				Logger.DEBUG);
		mPromptEditor.putLong(PREFS_PROMPT_DATE, promptDateInMillis).apply();
	}

	public long getPromptDateInMillis() {
		long promptDateInMillis = mPromptPrefs.getLong(PREFS_PROMPT_DATE, 0);
		Logger.printMessage(TAG,
				"get promptDateInMillis " + promptDateInMillis, Logger.DEBUG);
		return promptDateInMillis;
	}

	public void setStopPrompt(boolean isStopPrompt) {
		Logger.printMessage(TAG, "set isStopPrompt" + isStopPrompt,
				Logger.DEBUG);
		mPromptEditor.putBoolean(PREFS_STOP_PROMPT, isStopPrompt).apply();
	}

	public boolean isStopPrompt() {
		boolean isStopPrompt = mPromptPrefs
				.getBoolean(PREFS_STOP_PROMPT, false);
		Logger.printMessage(TAG, "get isStopPrompt " + isStopPrompt,
				Logger.DEBUG);
		return isStopPrompt;
	}

	/**
	 * @return the application name of the host activity
	 */
	private String getApplicationName() {
		final PackageManager pm = getApplicationContext().getPackageManager();
		ApplicationInfo ai;
		String appName;
		try {
			ai = pm.getApplicationInfo(getPackageName(), 0);
			appName = (String) pm.getApplicationLabel(ai);
		} catch (final NameNotFoundException e) {
			appName = "this app";
		}
		return appName;
	}
}