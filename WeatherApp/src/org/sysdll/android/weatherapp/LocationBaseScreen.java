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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.widget.Toast;

import org.sysdll.android.weatherapp.R;
import org.sysdll.android.weatherapp.logger.Logger;
import org.sysdll.android.weatherapp.utils.MyAlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationBaseScreen extends BaseScreen implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	// Global constants
	// Variable to hold the current location
	protected Location mCurrentLocation = null;
	// Location client for connection Google Location Services
	protected LocationClient mLocationClient;
	// Milliseconds per second
	private static final long MILLISECONDS_PER_SECOND = DateUtils.SECOND_IN_MILLIS;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	protected static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	protected static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	// Define an object that holds accuracy and frequency parameters
	protected LocationRequest mLocationRequest;

	// Used for provider detection only
	protected LocationManager mLocationManager;

	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// Acceptance of time out-of-date location in milliseconds
	protected final static long LOCATION_TIME_ACCEPTACNE = DateUtils.MINUTE_IN_MILLIS * 5;

	/*
	 * Define a request code to show the location source settings
	 */
	private final static int TOGGLEGPS_REQUEST_CODE = 1000;

	private MyAlertDialog enableGPSDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void onResume() {
		super.onResume();
		servicesConnected();
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Decide what to do based on the original request code
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Logger.printMessage("Location Updates",
					"Google Play services is available.", Logger.DEBUG);
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				// Get the error dialog from Google Play services
				Dialog errorDialog = GooglePlayServicesUtil
						.getErrorDialog(resultCode, this,
								CONNECTION_FAILURE_RESOLUTION_REQUEST);

				// If Google Play services can provide an error dialog
				if (errorDialog != null) {
					// Create a new DialogFragment for the error dialog
					ErrorDialogFragment errorFragment = new ErrorDialogFragment();
					// Set the dialog in the DialogFragment
					errorFragment.setDialog(errorDialog);
					// Show the error dialog in the DialogFragment
					errorFragment.show(getSupportFragmentManager(),
							"Location Updates");
				}
			}
			return false;
		}
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				Logger.printMessage("Location Services", e.getMessage(),
						Logger.DEBUG);
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			Logger.printMessage("Location Services", "Connection failed: "
					+ connectionResult.getErrorCode(), Logger.DEBUG);
			Toast.makeText(
					getApplicationContext(),
					"We're unable to get your location. Please re-start the application",
					Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		Logger.printMessage("Location Services", "Connected", Logger.DEBUG);
		if (!isGPSEnabled() && !isGoogleLocationEnabled()) {
			Logger.printMessage("Location Services",
					"All location services are disabled", Logger.DEBUG);
			// If the dialog already prompted, do nothing
			if (enableGPSDialog != null & enableGPSDialog.isShowing())
				return;

			// Prompt a dialog for user to open the location source settings
			// screen
			enableGPSDialog = new MyAlertDialog(this, null,
					getString(R.string.location_service_disabled), false);
			enableGPSDialog.setPositiveButton("Enable", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					toggleLocationSourceSettings();
				}
			});
			enableGPSDialog.setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			enableGPSDialog.show();
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		Logger.printMessage("Location Services",
				"Disconnected. Please re-connect.", Logger.DEBUG);
	}

	/*
	 * Define the callback method that receives location updates(non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.location.LocationListener#onLocationChanged(android
	 * .location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		String msg = "Updated Location: "
				+ Double.toString(location.getLatitude()) + ","
				+ Double.toString(location.getLongitude());
		Logger.printMessage("Updated Location: ", msg, Logger.DEBUG);
	}

	/**
	 * A subclass of AsyncTask that calls getFromLocation() in the background.
	 * The class definition has these generic types: Location - A Location
	 * object containing the current location. Void - indicates that progress
	 * units are not used String - An address passed to onPostExecute()
	 */
	class GetAddressTask extends AsyncTask<Location, Void, String> {

		private Context mContext;

		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}

		/**
		 * Get a Geocoder instance, get the latitude and longitude look up the
		 * address, and return it
		 * 
		 * @params params One or more Location objects
		 * @return A string containing the address of the current location, or
		 *         an empty string if no address can be found, or an error
		 *         message
		 */
		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			Location loc = params[0];
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e1) {
				Logger.printMessage(TAG, "IO Exception in getFromLocation(): "
						+ e1.getMessage(), Logger.DEBUG);
				e1.printStackTrace();
				return null;
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(loc.getLatitude()) + " , "
						+ Double.toString(loc.getLongitude())
						+ " passed to address service";
				Logger.printMessage(TAG, errorString, Logger.DEBUG);
				e2.printStackTrace();
				return null;
			}

			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				String city = address.getLocality();
				return city;
			} else {
				return null;
			}
		}

		/**
		 * A method that's called once doInBackground() completes. Turn off the
		 * indeterminate activity indicator and set the text of the UI element
		 * that shows the address. If the lookup failed, display the error
		 * message.
		 */
		@Override
		protected void onPostExecute(String address) {
			if (address != null)
				Logger.printMessage("Location Address", address, Logger.DEBUG);
		}
	}

	/**
	 * Check if the system GPS specifically is enabled or not
	 * 
	 * @return
	 */
	public boolean isGPSEnabled() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Check if the "Google Location Services" (network) is enabled
	 * 
	 * @return
	 */
	public boolean isGoogleLocationEnabled() {
		return mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * Direct the user to the location sources control screen in their device
	 * settings
	 */
	protected void toggleLocationSourceSettings() {
		startActivityForResult(new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
				TOGGLEGPS_REQUEST_CODE);
	}
}