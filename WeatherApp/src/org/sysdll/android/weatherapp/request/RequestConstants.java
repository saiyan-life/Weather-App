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

public interface RequestConstants {
	// Open Weather Map API Key
	static final String OPENWEATHER_APP_ID = "8c9040d2cf32d613e5d36d170d7235eb";

	// Constant corresponding to API
	static final String API_PROTOCOL = "http";
	static final String API_HOST = "api.openweathermap.org";
	static final String API_VERSION = "/2.5";
	static final String API_PATH = "/data" + API_VERSION;
	// Return format can be "json", "xml", "html"
	static final String PARAM_RETURN_FORMAT = "json";

	// Types of Metric system - metric(Celsius) OR imperial(Fahrenheit)
	static final String PARAM_DEFAULT_TEMP_UNITS = "metric";
	static final String PARAM_TEMP_UNIT_C = PARAM_DEFAULT_TEMP_UNITS;
	static final String PARAM_TEMP_UNIT_F = "imperial";

	// Limit the days of forecast, MAXIMUM is 15 (including today)
	static final int PARAM_NUM_OF_DAYS_FORECAST = 1 + 6; // e.g. get TODAY & FOLLOWING
													// 6 days weather forecast
	// Accuracy of the searching cities
	// "accurate" - the search only exactly match the input city
	// "like" - the search will match all cities which contain the input city as
	// part of the word
	static final String PARAM_SEARCH_ACCURACY = "accurate";

	static final String API_ACTION_WEATHER = "/weather";
	static final String API_ACTION_FORECAST = "/forecast";
	static final String API_ACTION_FORECAST_DAILY = API_ACTION_FORECAST
			+ "/daily";
	static final String API_ACTION_FIND = "/find";

	// Error code from Open Weather Map API
	static final int ERROR_NOT_FOUND_CITY = 404;
}
