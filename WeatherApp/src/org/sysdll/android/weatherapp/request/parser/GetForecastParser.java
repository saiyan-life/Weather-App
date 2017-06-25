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
package org.sysdll.android.weatherapp.request.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sysdll.android.weatherapp.logger.Logger;
import org.sysdll.android.weatherapp.model.DataForecast;
import org.sysdll.android.weatherapp.model.DataManager;
import org.sysdll.android.weatherapp.model.DataWeather;


public class GetForecastParser implements BaseParser {

	private static final String TAG = GetForecastParser.class.getSimpleName();

	@Override
	public int parseResponse(JSONObject json) {
		int result = FAIL;

		if (json != null) {
			// Get the response code from the returned result
			String responseCode = json.optString("cod");
			if (responseCode.length() > 0 && responseCode.contains("200")) {
				result = SUCCESS;
				// Save weather result as a object
				DataForecast data = new DataForecast();

				// Get city information
				JSONObject jsonCity = json.optJSONObject("city");
				if (jsonCity != null) {
					data.setCityId(jsonCity.optInt("id", -1));
					// City name
					data.setCity(jsonCity.optString("name"));
					// Coordinate
					JSONObject jsonCoord = jsonCity.optJSONObject("coord");
					if (jsonCoord != null) {
						// Latitude
						data.setLat(jsonCoord.optDouble("lat"));
						// Longitude
						data.setLon(jsonCoord.optDouble("lon"));
					}
					// Country
					data.setCountry(jsonCity.optString("country"));
					// Population
					data.setPopulation(jsonCity.optInt("population", -1));
				}

				// Number of forecast results
				data.setNumberOfResult(json.optInt("cnt", -1));

				ArrayList<DataWeather> weatherArr = new ArrayList<DataWeather>();
				JSONArray forecastList = json.optJSONArray("list");
				if (forecastList != null) {
					for (int i = 0; i < forecastList.length(); i++) {
						DataWeather dataWeather = new DataWeather();
						JSONObject weatherObj = forecastList.optJSONObject(i);
						if (weatherObj != null) {
							// Get date
							dataWeather.setForecastUTCDate(weatherObj.optLong(
									"dt", -1));
							// Get Cloudiness
							dataWeather.setCloudiness(weatherObj.optInt(
									"clouds", -1));
							// Get wind speed
							dataWeather.setWindSpeed(weatherObj
									.optDouble("speed"));
							// Get wind degree
							dataWeather.setWindDegree(weatherObj
									.optDouble("deg"));
							// Get pressure
							dataWeather.setCurrentPressure(weatherObj
									.optDouble("pressure"));
							// Get humidity
							dataWeather.setHumidity(weatherObj.optInt(
									"humidity", -1));
							// Get the temperature details
							JSONObject jsonTemp = weatherObj
									.optJSONObject("temp");
							if (jsonTemp != null) {
								// Highest temperature
								dataWeather.setMaxTemp(jsonTemp
										.optDouble("max"));
								// Lowest temperature
								dataWeather.setMinTemp(jsonTemp
										.optDouble("min"));
								// Day temperature
								dataWeather.setDayTemp(jsonTemp
										.optDouble("day"));
								// Night temperature
								dataWeather.setNightTemp(jsonTemp
										.optDouble("night"));
								// Morning temperature
								dataWeather.setMorningTemp(jsonTemp
										.optDouble("morn"));
								// Evening temperature
								dataWeather.setEveningTemp(jsonTemp
										.optDouble("eve"));
							}

							// Get the weather condition
							JSONArray jsonCondArray = weatherObj
									.optJSONArray("weather");
							if (jsonCondArray != null) {
								JSONObject jsonCond = jsonCondArray
										.optJSONObject(0);
								if (jsonCond != null) {
									dataWeather.setConditionId(jsonCond.optInt(
											"id", -1));
									// Main condition
									dataWeather.setConditionMain(jsonCond
											.optString("main"));
									// Description
									dataWeather.setConditionDesc(jsonCond
											.optString("description"));
									// Default icon
									dataWeather.setConditionIcon(jsonCond
											.optString("icon"));
								}
							}
						}

						weatherArr.add(dataWeather);
					}
					data.setForecastList(weatherArr);
				}

				// Store forecast data in a model
				dataModel.storeObject(DataManager.MODEL_KEY_FORECAST, data);
			} else if (responseCode.length() > 0
					&& responseCode.contains("404")) {
				result = ERROR_NOT_FOUND_CITY;
			}
		}

		Logger.printMessage(TAG, Integer.toString(result), Logger.DEBUG);

		return result;
	}
}