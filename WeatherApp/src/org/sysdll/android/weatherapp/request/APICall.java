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
package org.sysdll.android.weatherapp.request;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.sysdll.android.weatherapp.logger.Logger;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

/**
 * The class is used for handling HTTP request. The method @category AsyncTask
 * would be used to handle the HTTP request on a separate thread.
 */
public class APICall extends AsyncTask<HttpRequestBase, Integer, InputStream>
		implements RequestConstants {
	private final String TAG = getClass().getSimpleName();

	protected Context mContext = null;
	protected Request request;

	protected HttpPost httppost = null;
	protected HttpGet httpget = null;

	public APICall(Request request) {
		super();
		this.request = request;
	}

	public APICall(Context context, Request request) {
		super();
		this.mContext = context;
		this.request = request;
	}

	public void onComplete(InputStream response) {
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onCancelled() {
		// About the HTTP request if the api request is being cancelled
		if (httpget != null)
			httpget.abort();
		if (httppost != null)
			httppost.abort();
	}

	@Override
	protected InputStream doInBackground(final HttpRequestBase... requests) {
		return query(mContext, requests[0]);
	}

	protected void onProgressUpdate(Integer... progress) {
	}

	@Override
	protected void onPostExecute(InputStream content) {
		onComplete(content);
	}

	/*
	 * Create the HTTP request.
	 */
	public void execute() {
		String method = request.getMethod();
		if (method.equals(HttpPost.METHOD_NAME)) {
			try {
				httppost = new HttpPost(new URI(request.getUrl().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(
						request.getParams(), HTTP.UTF_8));
				httppost.setHeaders(request.getHeaders());

				Logger.printMessage(TAG, "API Request: " + httppost.getURI()
						+ request.getParams().toString(), Logger.DEBUG);
				execute(httppost);
			} catch (Exception e) {
				Logger.printMessage("HTTP post", e.getMessage(), Logger.DEBUG);
				e.printStackTrace();
			}
		} else if (method.equals(HttpGet.METHOD_NAME)) {
			try {
				String query = (request.getParams() == null) ? ""
						: URLEncodedUtils.format(request.getParams(),
								HTTP.UTF_8);
				HttpGet httpget = new HttpGet(new URI(request.getUrl()
						.toString().concat("?").concat(query)));
				httpget.setHeaders(request.getHeaders());

				Logger.printMessage(TAG, "API Request: " + httpget.getURI()
						+ httpget.getParams(), Logger.DEBUG);
				execute(httpget);
			} catch (Exception e) {
				Logger.printMessage("HTTP get", e.getMessage(), Logger.DEBUG);
				e.printStackTrace();
			}
		} else {
			Logger.printMessage(
					"HTTP request",
					"Fail to execute the api call: Missing http request method",
					Logger.DEBUG);
		}
	}

	/*
	 * Execute HTTP query
	 */
	protected InputStream query(Context context, HttpRequestBase http) {
		InputStream result = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpResponse response;

			// Executes the query
			response = httpclient.execute(http);

			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
					response.getEntity());
			result = bufHttpEntity.getContent();
		} catch (ClientProtocolException e) {
			Logger.printMessage(TAG, e.getMessage() + "", Logger.DEBUG);
			e.printStackTrace();
		} catch (IOException e) {
			Logger.printMessage(TAG, e.getMessage() + "", Logger.DEBUG);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Convert the input stream to string
	 * 
	 * @param is
	 *            - input stream being decoded to string
	 * @return decoded string
	 */
	protected String convertStreamToString(InputStream is) {
		if (is == null) {
			return null;
		}

		Scanner s = new Scanner(is).useDelimiter("\\A");
		try {
			// Check if content is in the input stream
			if (s.hasNext()) {
				String content = s.next();
				return content;
			} else {
				return "";
			}
		} catch (IllegalStateException e1) {
			Logger.printMessage("Convert Stream to String", e1.getMessage(),
					Logger.DEBUG);
			e1.printStackTrace();
			return "";
		} catch (NoSuchElementException e2) {
			Logger.printMessage("Convert Stream to String", e2.getMessage(),
					Logger.DEBUG);
			e2.printStackTrace();
			return "";
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				Logger.printMessage("Input stream",
						"Fail to close the input stream", Logger.DEBUG);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Check the network connection Determine if any network is active and
	 * connected
	 * 
	 * @return boolean true if any network type is available and connected
	 */
	public static Boolean isOnline(Context context) {
		Boolean isAvailable = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = cm.getAllNetworkInfo();
		for (int i = 0; i < info.length; i++) {
			if (info[i].isConnected()) {
				isAvailable = true;
				Logger.printMessage("Network Connection", info[i].getTypeName()
						+ " is available", Logger.DEBUG);
			}
		}
		if (!isAvailable)
			Logger.printMessage("Network Connection",
					"Network connectivity doesn't exist", Logger.DEBUG);
		return isAvailable;
	}
}