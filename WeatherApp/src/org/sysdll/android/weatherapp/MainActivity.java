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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Stack;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.sysdll.android.weatherapp.R;
import org.sysdll.android.weatherapp.logger.Logger;
import org.sysdll.android.weatherapp.model.DataForecast;
import org.sysdll.android.weatherapp.model.DataManager;
import org.sysdll.android.weatherapp.model.DataWeather;
import org.sysdll.android.weatherapp.request.APICall;
import org.sysdll.android.weatherapp.request.ActionTypes;
import org.sysdll.android.weatherapp.request.JSONCall;
import org.sysdll.android.weatherapp.request.Request;
import org.sysdll.android.weatherapp.request.RequestConstants;
import org.sysdll.android.weatherapp.request.RequestGenerator;
import org.sysdll.android.weatherapp.request.ResultType;
import org.sysdll.android.weatherapp.request.parser.ParserController;
import org.sysdll.android.weatherapp.utils.MyAlertDialog;
import org.sysdll.android.weatherapp.utils.Utils;
import org.sysdll.android.weatherapp.utils.ui.HorizontalViewGallery;
import org.sysdll.android.weatherapp.utils.ui.MyCustomEditText;
import org.sysdll.android.weatherapp.weather.IconFinder;

import com.google.ads.AdRequest;
//import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

@SuppressWarnings("deprecation")
public class MainActivity extends SettingsActivity implements ActionTypes,
		ResultType {

	private TextView tvLocation, tvHTemp, tvLTemp, tvCurrentTemp, tvTimestamp,
			tvDate, tvWindSpeed, tvPressure;
	private ImageView ivCurrentWeather, ivRefresh, ivUnit, ivSearch, ivRemind;
	private HorizontalViewGallery forecastGallery;
	// Search field
	private MyCustomEditText etSearch;

	// holding different weather icons presenting weather condition
	// alternatively.
	private IconFinder mIconFinder;

	private ApiCallQueue requestsQueue = new ApiCallQueue();

	private void setupUI(View view) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// Hide the search field if the user is touching anywhere
					// except the search field
					if (etSearch.isShown()) {
						etSearch.startDeflation();
						return true;
					}
					return false;
				}
			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupUI(innerView);
			}
		}
	}

	/**
	 * Initial the UI of current screen Initial variables;
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void initialActivity() {
		setupUI(findViewById(R.id.parent));
		// display the name of location
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		// Set the location icon
		tvLocation.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.icon_marker), null, null, null);
		// display today highest temperature
		tvHTemp = (TextView) findViewById(R.id.tvHTemp);
		// Set the highest temperature icon
		tvHTemp.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.icon_highest), null, null, null);
		// display today lowest temperature
		tvLTemp = (TextView) findViewById(R.id.tvLTemp);
		// Set the lowest temperature icon
		tvLTemp.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.icon_lowest), null, null, null);
		// display the current temperature
		tvCurrentTemp = (TextView) findViewById(R.id.tvCurrentTemp);
		// display the update time stamp
		tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
		// display the date of today
		tvDate = (TextView) findViewById(R.id.tvDate);
		// display the wind speed
		tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeed);
		// Set wind speed icon
		tvWindSpeed.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.icon_wind), null, null, null);
		// display the pressure
		tvPressure = (TextView) findViewById(R.id.tvPressure);
		// Set wind speed icon
		tvPressure.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.icon_pressure), null, null, null);
		// visualize the current weather condition
		ivCurrentWeather = (ImageView) findViewById(R.id.ivCurrentWeather);
		// Scrollable forecast
		forecastGallery = (HorizontalViewGallery) findViewById(R.id.gForcast);
		// Search city button
		ivSearch = (ImageView) findViewById(R.id.ivSearch);
		// Setting button
		ivRemind = (ImageView) findViewById(R.id.ivRemind);
		// Temp unit setting Button
		ivUnit = (ImageView) findViewById(R.id.ivUnit);
		if (getTempUnit().equals(PARAM_TEMP_UNIT_C))
			ivUnit.setImageDrawable(getResources().getDrawable(
					R.drawable.button_unit_f));
		else if (getTempUnit().equals(PARAM_TEMP_UNIT_F))
			ivUnit.setImageDrawable(getResources().getDrawable(
					R.drawable.button_unit_c));
		// Refresh button
		ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
		// Search field
		etSearch = (MyCustomEditText) findViewById(R.id.etSearch);
		// set the animation of search field
		// Animation Duration in milliseconds;
		int duration = 500;
		// Inflate animation
		final AnimationSet inflate = new AnimationSet(true);
		ScaleAnimation scaleIn = new ScaleAnimation(0f, 1.0f, 1.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleIn.setDuration(duration);
		inflate.addAnimation(scaleIn);
		inflate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
				etSearch.setVisibility(View.VISIBLE);
				etSearch.requestFocus();
				showSoftKeyboard(etSearch);
			}
		});
		// Deflate animation
		final AnimationSet deflate = new AnimationSet(true);
		ScaleAnimation scaleDe = new ScaleAnimation(1.0f, 0f, 1.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleDe.setDuration(duration);
		deflate.addAnimation(scaleDe);
		deflate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				etSearch.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
				hideSoftKeyboard(etSearch);
			}
		});

		etSearch.setInflation(inflate);
		etSearch.setDeflation(deflate);

		// Running the change of digital clock on separate UI thread
		// to avoid any delay of other action on UI.
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!Utils.androidMinimum(API_JELLY_BEAN_MR1)) {
					// Using the widget class {@code DigitalClock} if the
					// android api is less than 17
					DigitalClock dcClock = (DigitalClock) findViewById(R.id.dcClock);
					dcClock.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
							// Removed seconds
							if (s.length() >= 5) {
								if (s.charAt(4) == ':') {
									s.delete(4, s.length());
								} else if (s.length() >= 6
										&& s.charAt(5) == ':') {
									s.delete(5, s.length());
								}
							}
						}
					});
				} else {
					// Using the widget class {@code TextClock} if the android
					// api is greater than or equal to 17
					TextClock dcClock = (TextClock) findViewById(R.id.dcClock);
					dcClock.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
							// Removed seconds
							if (s.length() >= 5) {
								if (s.charAt(4) == ':') {
									s.delete(4, s.length());
								} else if (s.length() >= 6
										&& s.charAt(5) == ':') {
									s.delete(5, s.length());
								}
							}
						}
					});
				}
			}
		});

		mIconFinder = new IconFinder(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(this, this, this);

		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

		setContentView(R.layout.activity_main);
//
//		AdView adView = (AdView) this.findViewById(R.id.adView);
//		// Look up the AdView as a resource and load a request.
//		adView.loadAd(new AdRequest());
//		adView.setVisibility(View.VISIBLE);

		initialActivity();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		// Connect the client.
		mLocationClient.connect();

		// Registers BroadcastReceiver to track network connection changes.
		mNetworkReceiver = new NetworkReceiver() {
			private MyAlertDialog enableNetworkDialog = null;

			@Override
			public void onNoConnectivity() {
				Toast.makeText(MainActivity.this,
						getString(R.string.network_disabled), Toast.LENGTH_LONG)
						.show();

				// If the dialog already prompted, do nothing
				if (enableNetworkDialog != null
						&& enableNetworkDialog.isShowing())
					return;

				// Prompt a dialog for user to open the network settings screen
				enableNetworkDialog = new MyAlertDialog(MainActivity.this,
						null, getString(R.string.network_disabled), false);
				enableNetworkDialog.setPositiveButton("Enable",
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								toggleNetworkSourceSetting();
							}
						});
				enableNetworkDialog.setNegativeButton("Cancel",
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				enableNetworkDialog.show();
			}

			@Override
			public void onConnect() {
				if (enableNetworkDialog != null
						&& enableNetworkDialog.isShowing())
					enableNetworkDialog.dismiss();
			}

			@Override
			public void onNetworkChange() {
			}

			@Override
			public void onReconnect() {
			};
		};
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(mNetworkReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		LocalBroadcastManager.getInstance(this).registerReceiver(
				mWeatherAlarmReceiver,
				new IntentFilter(AlarmReceiver.ACTION_ALARM));

		DataWeather cacheWeather = (DataWeather) mDataManager
				.getObject(DataManager.MODEL_KEY_WEATHER);
		if (cacheWeather != null) {
			// Set the refresh time stamp
			tvTimestamp.setText(getRefreshTimestamp(
					cacheWeather.getRefreshUTCTime(), "dd/MM HH:mm"));
			// Get the city
			tvLocation.setText(cacheWeather.getCity());
			// Current temperature rounded to 1 decimal place
			tvCurrentTemp.setText(String.format("%.1f%s",
					cacheWeather.getCurrentTemp(), getTempUnitSymbol()));
			// Highest temperature rounded to 1 decimal place
			tvHTemp.setText(String.format("%.1f%s", cacheWeather.getMaxTemp(),
					getTempUnitSymbol()));
			// Lowest temperature rounded to 1 decimal place
			tvLTemp.setText(String.format("%.1f%s", cacheWeather.getMinTemp(),
					getTempUnitSymbol()));
			ivCurrentWeather.setImageDrawable(mIconFinder.getIcon(
					cacheWeather.getConditionId(), IconFinder.TYPE_LARGE));

			// Set the date of the main weather information
			tvDate.setText(String.format(
					"%s, %s",
					Utils.convertUTCToLocalDate(
							cacheWeather.getRefreshUTCTime(), "dd/MM/yyyy"),
					Utils.getDayOfWeek(cacheWeather.getRefreshUTCTime())));

			// Set the wind speed in km/h
			tvWindSpeed
					.setText(String.format("%.1f %s",
							Utils.convertMpsToKmh(cacheWeather.getWindSpeed()),
							"km/h"));

			// Set atmospheric pressure in hPa
			tvPressure.setText(Double.toString(cacheWeather
					.getCurrentPressure()));
		}

		DataForecast cacheForecast = (DataForecast) mDataManager
				.getObject(DataManager.MODEL_KEY_FORECAST);
		if (cacheForecast != null) {
			ArrayList<DataWeather> weatherArr = cacheForecast.getForecastList();
			if (weatherArr.size() > 0) {
				// Reset the scrollable forecast
				forecastGallery.removeAllViews();
			}

			for (int i = 0; i < weatherArr.size(); i++) {
				// Disregard the first weather forecast details. It is the
				// current weather information
				if (i == 0)
					continue;

				DataWeather weatherObj = weatherArr.get(i);
				// Create forecast view for each day
				LinearLayout forecastItem = (LinearLayout) getLayoutInflater()
						.inflate(R.layout.view_forecast_item, null);
				// Show the day of week
				((TextView) forecastItem.findViewById(R.id.tvDate))
						.setText(Utils.getDayOfWeek(weatherObj
								.getForecastUTCDate()));
				// Show the weather condition icon
				((ImageView) forecastItem.findViewById(R.id.ivWeather))
						.setImageDrawable(mIconFinder.getIcon(
								weatherObj.getConditionId(),
								IconFinder.TYPE_SMALL));
				// Show the minimum temperature and the maximum temperature of
				// the day
				String forecastTemp = String.format("%.0f%s/%.0f%s",
						weatherObj.getMinTemp(), getTempUnitSymbol(),
						weatherObj.getMaxTemp(), getTempUnitSymbol());
				((TextView) forecastItem.findViewById(R.id.tvTemp))
						.setText(forecastTemp);

				forecastGallery.add(forecastItem);
			}

			float[] screenSize = new float[2];
			getScreenSize(screenSize);
			forecastGallery.setChildSize(screenSize[0], screenSize[1]);
		}
	}

	@Override
	public void onPause() {
		// If the client is connected
		if (mLocationClient.isConnected()) {
			/*
			 * Remove location updates for a listener. The current Activity is
			 * the listener, so the argument is "this".
			 */
			mLocationClient.removeLocationUpdates(this);
		}
		/*
		 * After disconnect() is called, the client is considered "dead".
		 */
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();

		// Unregisters BroadcastReceiver when app is destroyed.
		if (mNetworkReceiver != null)
			unregisterReceiver(mNetworkReceiver);

		if (mWeatherAlarmReceiver != null)
			LocalBroadcastManager.getInstance(this).unregisterReceiver(
					mWeatherAlarmReceiver);
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Create the weather alarm receiver This block is called when a weather
	 * alarm is received while the application is active
	 */
	private BroadcastReceiver mWeatherAlarmReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(AlarmReceiver.ACTION_ALARM)) {
				Bundle b = intent.getExtras();
				String eventName = b.getString(AlarmReceiver.KEY_EVENT_NAME);
				String eventDate = b.getString(AlarmReceiver.KEY_EVENT_DATE);
				MyAlertDialog dialog = new MyAlertDialog(MainActivity.this,
						getString(R.string.remind_title), getString(
								R.string.remind_message, eventName, eventDate),
						false);
				dialog.setNeutralButton(getString(R.string.dialog_ok),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startWeatherFetch(getCity());
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		}
	};

	@Override
	public void onConnected(Bundle connectionHint) {
		super.onConnected(connectionHint);
		Logger.printMessage("Location Services", "Connected", Logger.DEBUG);
		// Get the current location
		Location lastLocation = mLocationClient.getLastLocation();
		mCurrentLocation = (lastLocation != null && lastLocation.getTime() < LOCATION_TIME_ACCEPTACNE) ? lastLocation
				: null;

		if (mCurrentLocation == null) {
			// Start the location update if the current location is out of date
			Logger.printMessage("Location Services",
					"Request location updates", Logger.DEBUG);
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		} else {
			// Stop location update if the current location is up to date
			Logger.printMessage("Location Services", "Location is up-to-date",
					Logger.DEBUG);
			mLocationClient.removeLocationUpdates(this);

			if (Geocoder.isPresent()) {
				GetAddressTask t = new GetAddressTask(this) {
					@Override
					protected void onPostExecute(String address) {
						super.onPostExecute(address);
						// If no city has been searched before, then take the
						// currently detected location
						setCity(getCity() == null ? address : getCity());
						getCurrentWeather(getCity());
						getForecast(getCity());
					}
				};
				// Get the address name of current location
				t.execute(mCurrentLocation);
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		Logger.printMessage("Location Services", "Location changed",
				Logger.DEBUG);
		mCurrentLocation = (location != null && location.getTime() < System
				.currentTimeMillis() + LOCATION_TIME_ACCEPTACNE) ? location
				: null;

		if (mCurrentLocation == null) {
			// Start the location update if the current location is out of date
			Logger.printMessage("Location Services",
					"Request location updates", Logger.DEBUG);
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		} else {
			// Stop location update if the current location is up to date
			Logger.printMessage("Location Services", "Location is up-to-date",
					Logger.DEBUG);
			mLocationClient.removeLocationUpdates(this);

			if (Geocoder.isPresent()) {
				GetAddressTask t = new GetAddressTask(this) {
					@Override
					protected void onPostExecute(String address) {
						super.onPostExecute(address);

						// If no city has been searched before, then take the
						// currently detected location
						setCity(getCity() == null ? address : getCity());

						if (!APICall.isOnline(MainActivity.this)) {
							Toast.makeText(MainActivity.this,
									getString(R.string.network_disabled),
									Toast.LENGTH_LONG).show();
						} else {
							JSONCall getWeatherTh = getCurrentWeather(getCity());

							if (getWeatherTh != null) {
								// This api call may be added before. So there
								// may be some old tasks in the queue. We need
								// to discard them.
								synchronized (requestsQueue.requests) {
									requestsQueue.clean(getWeatherTh);
									requestsQueue.requests.push(getWeatherTh);
								}
							}

							JSONCall getForecastTh = getForecast(getCity());

							if (getForecastTh != null) {
								// This api call may be added before. So there
								// may be some old tasks in the queue. We need
								// to discard them.
								synchronized (requestsQueue.requests) {
									requestsQueue.clean(getForecastTh);
									requestsQueue.requests.push(getForecastTh);
								}
							}

							for (int i = 0; i < requestsQueue.size(); i++) {
								synchronized (requestsQueue.requests) {
									JSONCall request = requestsQueue.requests
											.get(i);
									// Start api request if it's not started yet
									if (request.getStatus() != AsyncTask.Status.RUNNING
											&& request.getStatus() != AsyncTask.Status.FINISHED)
										request.execute();
								}
							}
						}
					}
				};
				// Get the address name of current location
				t.execute(location);
			}
		}
	}

	/**
	 * Perform a weather api call to retrieve the current weather information
	 * 
	 * @param address
	 *            - address of current location
	 */
	private JSONCall getCurrentWeather(String address) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("q", address));
		// Set the unit of degree
		postParameters.add(new BasicNameValuePair("units", getTempUnit()));
		RequestGenerator rqGenerator = new RequestGenerator(postParameters);
		Request request = rqGenerator
				.setWeatherRequest(RequestConstants.API_ACTION_WEATHER);

		JSONCall getWeatherTh = new JSONCall(this, request) {

			private RelativeLayout mainThrobber;
			// get weather request time tracker
			private TimingTracker tracker;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mainThrobber = (RelativeLayout) findViewById(R.id.mainThrobber);
				mainThrobber.setVisibility(View.VISIBLE);
				startWeatherLoading();

				tracker = new TimingTracker() {
					@Override
					public Map<String, String> createTimingData(long loadTime) {
						return MapBuilder.createTiming("api_request", loadTime,
								"get_weather", null).build();
					};
				};
				tracker.startTrack();
			}

			@Override
			protected void onPostExecute(InputStream content) {
				super.onPostExecute(content);
				// Submit the loading time to Google Analytics
				tracker.endTrack();

				mainThrobber.setVisibility(View.GONE);

				// Check if all the api requests finished
				synchronized (requestsQueue.requests) {
					requestsQueue.clean(this);

					if (requestsQueue.size() == 0) {
						Logger.printMessage("JSONCall", "finish", Logger.DEBUG);
						endWeatherLoading();
						requestsQueue.clear();
					}
				}
			}

			@Override
			public void onComplete(JSONObject json) {
				Logger.printMessage("Get current weather",
						"result: " + json.toString(), Logger.DEBUG);

				ParserController parser = ParserController.getInstance();
				int result = parser.parseResonseFromJSON(json, GET_WEATHER);
				if (result == SUCCESS) {
					DataWeather data = (DataWeather) mDataManager
							.getObject(DataManager.MODEL_KEY_WEATHER);
					if (data != null) {
						// Set the refresh time stamp
						tvTimestamp.setText(getRefreshTimestamp(
								data.getRefreshUTCTime(), "dd/MM HH:mm"));
						// Get the city
						// If city isn't supplied, then get the country name
						String city = data.getCity().length() > 0 ? data
								.getCity() : data.getCountry();
						tvLocation.setText(city);
						// Save the search city
						setCity(city);
						// Current temperature rounded to 1 decimal place
						tvCurrentTemp.setText(String.format("%.1f%s",
								data.getCurrentTemp(), getTempUnitSymbol()));
						// Highest temperature rounded to 1 decimal place
						tvHTemp.setText(String.format("%.1f%s",
								data.getMaxTemp(), getTempUnitSymbol()));
						// Lowest temperature rounded to 1 decimal place
						tvLTemp.setText(String.format("%.1f%s",
								data.getMinTemp(), getTempUnitSymbol()));
						ivCurrentWeather.setImageDrawable(mIconFinder.getIcon(
								data.getConditionId(), IconFinder.TYPE_LARGE));
						// Set the date of the main weather information
						tvDate.setText(String.format(
								"%s, %s",
								Utils.convertUTCToLocalDate(
										data.getRefreshUTCTime(), "dd/MM/yyyy"),
								Utils.getDayOfWeek(data.getRefreshUTCTime())));
						// Set the wind speed in km/h
						tvWindSpeed.setText(String.format("%.1f %s",
								Utils.convertMpsToKmh(data.getWindSpeed()),
								"km/h"));

						// Set atmospheric pressure in hPa
						tvPressure.setText(Double.toString(data
								.getCurrentPressure()));
					} else {
						// Fail to get the current weather details
						Toast.makeText(MainActivity.this,
								getString(R.string.error_get_weather),
								Toast.LENGTH_LONG).show();
					}
				} else if (result == ERROR_NOT_FOUND_CITY) {
					// Invalid city name
					Toast.makeText(MainActivity.this,
							getString(R.string.error_invalid_city),
							Toast.LENGTH_LONG).show();
				} else {
					// Fail to get the current weather details
					Toast.makeText(MainActivity.this,
							getString(R.string.error_get_weather),
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onJSONError(String response) {
				Logger.printMessage("Get current weather", "json error: "
						+ response, Logger.DEBUG);
				Toast.makeText(MainActivity.this,
						getString(R.string.error_get_weather),
						Toast.LENGTH_LONG).show();
			}
		};

		return getWeatherTh;
	}

	/**
	 * Perform a forecast api call to retrieve the forecast information
	 * 
	 * @param address
	 *            - address of current location
	 */
	@SuppressLint("DefaultLocale")
	private JSONCall getForecast(String address) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("q", address));
		// Set the unit of degree
		postParameters.add(new BasicNameValuePair("units", getTempUnit()));
		// Limit the days of forecast
		postParameters.add(new BasicNameValuePair("cnt", Integer
				.toString(RequestConstants.PARAM_NUM_OF_DAYS_FORECAST)));
		RequestGenerator rqGenerator = new RequestGenerator(postParameters);
		Request request = rqGenerator
				.setWeatherRequest(RequestConstants.API_ACTION_FORECAST_DAILY);

		JSONCall getForecastTh = new JSONCall(this, request) {

			private RelativeLayout forecastThrobber;
			// get forecast request time tracker
			private TimingTracker tracker;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				forecastThrobber = (RelativeLayout) findViewById(R.id.forecastThrobber);
				forecastThrobber.setVisibility(View.VISIBLE);
				startWeatherLoading();

				tracker = new TimingTracker() {
					@Override
					public Map<String, String> createTimingData(long loadTime) {
						return MapBuilder.createTiming("api_request", loadTime,
								"get_forecast", null).build();
					};
				};
				tracker.startTrack();
			}

			@Override
			protected void onPostExecute(InputStream content) {
				super.onPostExecute(content);
				forecastThrobber.setVisibility(View.GONE);

				// Check if all the api requests finished
				synchronized (requestsQueue.requests) {
					requestsQueue.clean(this);

					if (requestsQueue.size() == 0) {
						Logger.printMessage("JSONCall", "finish", Logger.DEBUG);
						endWeatherLoading();
						requestsQueue.clear();
					}
				}
			}

			@Override
			public void onComplete(JSONObject json) {
				// Submit the loading time to Google Analytics
				tracker.endTrack();

				Logger.printMessage("Get forecast",
						"result: " + json.toString(), Logger.DEBUG);

				ParserController parser = ParserController.getInstance();
				int result = parser.parseResonseFromJSON(json, GET_FORECAST);
				if (result == SUCCESS) {
					DataForecast data = (DataForecast) mDataManager
							.getObject(DataManager.MODEL_KEY_FORECAST);
					if (data != null) {
						ArrayList<DataWeather> weatherArr = data
								.getForecastList();
						if (weatherArr.size() > 0) {
							// Reset the scrollable forecast
							forecastGallery.removeAllViews();
						}

						for (int i = 0; i < weatherArr.size(); i++) {
							// Disregard the first weather forecast details. It
							// is the current weather information
							if (i == 0)
								continue;

							DataWeather weatherObj = weatherArr.get(i);
							// Create forecast view for each day
							LinearLayout forecastItem = (LinearLayout) getLayoutInflater()
									.inflate(R.layout.view_forecast_item, null);
							// Show the day of week
							((TextView) forecastItem.findViewById(R.id.tvDate))
									.setText(Utils.getDayOfWeek(weatherObj
											.getForecastUTCDate()));
							// Show the weather condition icon
							((ImageView) forecastItem
									.findViewById(R.id.ivWeather))
									.setImageDrawable(mIconFinder.getIcon(
											weatherObj.getConditionId(),
											IconFinder.TYPE_SMALL));
							// Show the minimum temperature and the maximum
							// temperature of the day
							String forecastTemp = String.format(
									"%.0f%s/%.0f%s", weatherObj.getMinTemp(),
									getTempUnitSymbol(),
									weatherObj.getMaxTemp(),
									getTempUnitSymbol());
							((TextView) forecastItem.findViewById(R.id.tvTemp))
									.setText(forecastTemp);

							forecastGallery.add(forecastItem);
						}

						float[] screenSize = new float[2];
						getScreenSize(screenSize);
						forecastGallery.setChildSize(screenSize[0],
								screenSize[1]);
					} else {
						// Fail to get the forecast details
						Toast.makeText(MainActivity.this,
								getString(R.string.error_get_forecast),
								Toast.LENGTH_LONG).show();
					}
				} else if (result == ERROR_NOT_FOUND_CITY) {
					// Invalid city name
					Toast.makeText(MainActivity.this,
							getString(R.string.error_invalid_city),
							Toast.LENGTH_LONG).show();
				} else {
					// Fail to get the forecast details
					Toast.makeText(MainActivity.this,
							getString(R.string.error_get_forecast),
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onJSONError(String response) {
				Logger.printMessage("Get forecast", "json error: " + response,
						Logger.DEBUG);
				Toast.makeText(MainActivity.this,
						getString(R.string.error_get_forecast),
						Toast.LENGTH_LONG).show();
			}
		};

		return getForecastTh;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		super.onSharedPreferenceChanged(sharedPreferences, key);

		// Updated the temperature precisely by api call when the user change
		// the temperature unit
		if (key.equals(KEY_TEMP_UNIT)) {
			// Update the temperature unit symbol
			if (getTempUnit().equals(RequestConstants.PARAM_TEMP_UNIT_C)) {
				setTempUnitSymbol(TEMP_SYM_C);
				ivUnit.setImageDrawable(getResources().getDrawable(
						R.drawable.button_unit_f));
			}

			if (getTempUnit().equals(RequestConstants.PARAM_TEMP_UNIT_F)) {
				setTempUnitSymbol(TEMP_SYM_F);
				ivUnit.setImageDrawable(getResources().getDrawable(
						R.drawable.button_unit_c));
			}

			startWeatherFetch(getCity());
		}
	}

	/*
	 * Display or hide the search field
	 */
	public void searchOnClick(View view) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (etSearch.isShown())
					// Hide search field
					etSearch.startDeflation();
				else
					// Show search field
					etSearch.startInflation();
			}
		});

		etSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// Listener the search button on soft keyboard
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (etSearch.getText().toString().length() > 0) {
						startWeatherFetch(etSearch.getText().toString());
						etSearch.startDeflation();
					} else {
						Toast.makeText(MainActivity.this,
								getString(R.string.error_empty_city),
								Toast.LENGTH_LONG).show();
					}
					trackWeatherSearch();
					return true;
				}
				return false;
			}
		});
	}

	/*
	 * Handling the action of pressing changing temperature unit button
	 */
	public void unitOnChange(View view) {
		if (getTempUnit().equals(RequestConstants.PARAM_TEMP_UNIT_C))
			setTempUnit(RequestConstants.PARAM_TEMP_UNIT_F);
		else if (getTempUnit().equals(RequestConstants.PARAM_TEMP_UNIT_F))
			setTempUnit(RequestConstants.PARAM_TEMP_UNIT_C);

		trackChangeUnit();
	}

	/*
	 * Handling the action of pressing refresh button
	 */
	public void refreshOnClick(View view) {
		startWeatherFetch(getCity());

		trackRefresh();
	}

	/*
	 * Handling the action of pressing remind button
	 */
	public void remindOnClick(View view) {
		MyRemindDialog dialog = new MyRemindDialog(this, this,
				RequestConstants.PARAM_NUM_OF_DAYS_FORECAST - 1) {
			@Override
			public void setReminder(String eventName, String eventDate,
					long remindDateInMillis) {

				// Set the alarm
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(remindDateInMillis);
				calendar.setTimeZone(TimeZone.getDefault());

				// Set the alarm unique id;
				int alarmId = (int) (calendar.getTimeInMillis() / 1000);

				// Pass extra data to the alarm
				Intent intent = new Intent(MainActivity.this,
						AlarmReceiver.class);
				intent.putExtra(AlarmReceiver.KEY_EVENT_NAME, eventName);
				intent.putExtra(AlarmReceiver.KEY_EVENT_DATE, eventDate);
				intent.putExtra(AlarmReceiver.KEY_ALARM_ID, alarmId);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(
						MainActivity.this, alarmId, intent,
						PendingIntent.FLAG_ONE_SHOT);

				// Create the alarm
				AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmMgr.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), alarmIntent);

				trackRemind();
			};
		};
		dialog.showDialog();
	}

	/*
	 * Handling the action of pressing info button
	 */
	public void infoOnClick(View view) {
		MyAlertDialog dialog = new MyAlertDialog(this,
				getString(R.string.feedback_title), null, true);

		// Set the message with linkable email address
		TextView tvMsg = new TextView(this);
		tvMsg.setText(getString(R.string.feedback_message));
//		tvMsg.setText(getString(R.string.developedBy));
//		tvMsg.setText(getString(R.string.copyright));
		Linkify.addLinks(tvMsg, Linkify.EMAIL_ADDRESSES);
		tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
		tvMsg.setPadding(
				(int) getResources().getDimension(R.dimen.base_margin),
				(int) getResources().getDimension(R.dimen.base_margin),
				(int) getResources().getDimension(R.dimen.base_margin),
				(int) getResources().getDimension(R.dimen.base_margin));
		dialog.addView(tvMsg);

		dialog.setNeutralButton(getString(R.string.dialog_ok),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialog.show();

		trackInfo();
	}

	/**
	 * Request to get weather & forecast information
	 * 
	 * @param city
	 *            the city which its weather being searched
	 */
	private void startWeatherFetch(String city) {
		if (!APICall.isOnline(MainActivity.this)) {
			Toast.makeText(MainActivity.this,
					getString(R.string.network_disabled), Toast.LENGTH_LONG)
					.show();
		} else {
			city = city == null ? getCity() : city;

			JSONCall getWeatherTh = getCurrentWeather(city);

			if (getWeatherTh != null) {
				// This api call may be added before. So there may be some old
				// tasks in the queue. We need to discard them.
				synchronized (requestsQueue.requests) {
					requestsQueue.clean(getWeatherTh);
					requestsQueue.requests.push(getWeatherTh);
				}
			}

			JSONCall getForecastTh = getForecast(city);

			if (getForecastTh != null) {
				// This api call may be added before. So there may be some old
				// tasks in the queue. We need to discard them.
				synchronized (requestsQueue.requests) {
					requestsQueue.clean(getForecastTh);
					requestsQueue.requests.push(getForecastTh);
				}
			}

			for (int i = 0; i < requestsQueue.size(); i++) {
				synchronized (requestsQueue.requests) {
					JSONCall request = requestsQueue.requests.get(i);
					// Start api request if it's not started yet
					if (request.getStatus() != AsyncTask.Status.RUNNING
							&& request.getStatus() != AsyncTask.Status.FINISHED)
						request.execute();
				}
			}
		}
	}

	/**
	 * Start to get weather & forecast information
	 */
	private void startWeatherLoading() {
		ivRefresh.setEnabled(false);
		ivSearch.setEnabled(false);
		ivUnit.setEnabled(false);
		ivRemind.setEnabled(false);
	}

	/**
	 * Complete to get weather & forecast information
	 */
	private void endWeatherLoading() {
		ivRefresh.setEnabled(true);
		ivSearch.setEnabled(true);
		ivUnit.setEnabled(true);
		ivRemind.setEnabled(true);
	}

	/**
	 * Stores a list of api call
	 */
	private class ApiCallQueue {
		private Stack<JSONCall> requests = new Stack<JSONCall>();

		// removes all instances of this api request
		public void clean(JSONCall request) {
			int index = requests.indexOf(request);
			if (index >= 0)
				requests.remove(index);
		}

		// Check if any api request is running
		public boolean isRunning() {
			for (int i = 0; i < requests.size(); i++) {
				JSONCall request = requests.get(i);
				if (request.getStatus() == AsyncTask.Status.RUNNING) {
					Logger.printMessage("Request Status", request.getStatus()
							+ "", Logger.DEBUG);
					return true;
				}
			}

			return false;
		}

		// Get the size of the stack of api calls
		public int size() {
			return requests.size();
		}

		// Clear all the api requests
		public void clear() {
			if (!isRunning())
				requests.clear();
			Logger.printMessage("Queue size", "Size: " + requests.size(),
					Logger.DEBUG);
		}
	}

	/**
	 * Get the timestamp in formatted string
	 * 
	 * @param timeInSeconds
	 *            - UTC time in seconds
	 * @param format
	 *            - output format of the time, e.g. "dd/MM/yyyy HH:mm:ss" =
	 *            "15/01/2009 23:11:28"
	 * @return - formatted timestamp
	 */
	private String getRefreshTimestamp(long timeInSeconds, String format) {
		String formattedLocalTime = Utils.convertUTCToLocalDate(timeInSeconds,
				format);
		return getString(R.string.timestamp_prefix, formattedLocalTime);
	}
}