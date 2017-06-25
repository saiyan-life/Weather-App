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

import java.io.InputStream;

import org.json.JSONObject;
import org.sysdll.android.weatherapp.logger.Logger;


import android.content.Context;

/**
 * The class is used for handling HTTP request and return the result in JSON.
 * The method @category AsyncTask would be used to handle the HTTP request on a
 * separate thread.
 */
public class JSONCall extends APICall {

	private final static String TAG = JSONCall.class.getSimpleName();

	public JSONCall(Request request) {
		super(request);
	}

	public JSONCall(Context context, Request request) {
		super(context, request);
	}

	public void onComplete(JSONObject responseJson) {
	}

	public void onJSONError(String response) {
	}

	@Override
	protected void onPostExecute(InputStream content) {
		String response = convertStreamToString(content);
		JSONObject json = toJSON(response);

		if (json != null) {
			Logger.printMessage(TAG,
					"API Result in json(success): " + response, Logger.DEBUG);
			onComplete(json);
		} else {
			Logger.printMessage(TAG, "API Result in json(fail): " + response,
					Logger.DEBUG);
			onJSONError(response);
		}
	}

	/*
	 * Convert the returned result from string to json object.
	 */
	private JSONObject toJSON(String json) {
		try {
			return new JSONObject(json);
		} catch (Exception e) {
			Logger.printMessage(TAG,
					"toJSON fail: " + json + ": " + e.toString(), Logger.DEBUG);
			e.printStackTrace();
			return null;
		}
	}
}