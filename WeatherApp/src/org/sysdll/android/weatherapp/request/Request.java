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

/**
 * Store request related details like methodType(GET/POST), data to be sent as
 * payload of POST request (postData), request header, request parameters,
 * request URL, and the action for which request is being performed.
 * 
 */
public class Request {
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private URL url = null;
	private Header[] headers = new Header[] {};
	private String action = null;
	private String method = null;

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public ArrayList<NameValuePair> getParams() {
		return nameValuePairs;
	}

	public void setParams(ArrayList<NameValuePair> nameValuePair) {
		this.nameValuePairs = nameValuePair;
	}

	public Header[] getHeaders() {
		return this.headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
