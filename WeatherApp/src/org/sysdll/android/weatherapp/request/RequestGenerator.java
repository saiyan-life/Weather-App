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

import java.net.URL;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.sysdll.android.weatherapp.logger.Logger;


/**
 * This class to generator the HTTP request Set request related details like
 * methodType(GET/POST), data to be sent as payload of POST request (postData),
 * request header, request parameters, request URL, and the action for which
 * request is being performed.
 */
public class RequestGenerator implements RequestConstants {
	private final String TAG = getClass().getSimpleName();

	private ArrayList<NameValuePair> nameValuePair = null;
	private Header[] headers = null;

	public RequestGenerator(ArrayList<NameValuePair> nameValuePair) {
		this.nameValuePair = nameValuePair;
	}

	public RequestGenerator(ArrayList<NameValuePair> nameValuePair,
			Header[] headers) {
		this.nameValuePair = nameValuePair;
		this.headers = headers;
	}

	private void setWeatherRequestDefaultParams(ArrayList<NameValuePair> params) {
		if (params == null)
			params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("APPID", OPENWEATHER_APP_ID));
		params.add(new BasicNameValuePair("mode", PARAM_RETURN_FORMAT));
		params.add(new BasicNameValuePair("type", PARAM_SEARCH_ACCURACY));
	}

	public Request setWeatherRequest(String action) {
		Request request = new Request();
		request.setAction(action);
		request.setMethod(HttpGet.METHOD_NAME);
		request.setParams(nameValuePair);
		setWeatherRequestDefaultParams(nameValuePair);
		request.setHeaders(headers);
		try {
			URL url = new URL(API_PROTOCOL, API_HOST, API_PATH
					+ request.getAction());
			request.setUrl(url);
		} catch (Exception e) {
			Logger.printMessage(TAG, e.getMessage(), Logger.DEBUG);
			e.printStackTrace();
		}
		return request;
	}

}