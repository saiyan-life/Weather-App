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

import org.json.JSONArray;
import org.json.JSONObject;
import org.sysdll.android.weatherapp.logger.Logger;
import org.sysdll.android.weatherapp.model.DataManager;
import org.sysdll.android.weatherapp.model.DataWeather;


public class GetWeatherParser implements BaseParser {

	private static final String TAG = GetWeatherParser.class.getSimpleName();

	@Override
	public int parseResponse(JSONObject json) {
		int result = FAIL;

		if (json != null) {
			// Get the response code from the returned result
			String responseCode = json.optString("cod");
			if (responseCode.length() > 0 && responseCode.contains("200")) {
				result = SUCCESS;
				// Save weather result as a object
				DataWeather data = new DataWeather();

				data.setRefreshUTCTime(json.optLong("dt", -1));
				data.setId(json.optInt("id", -1));
				data.setCity(json.optString("name"));

				// Get the main details
				JSONObject jsonMain = json.optJSONObject("main");
				if (jsonMain != null) {
					// Current temperature
					data.setCurrentTemp(jsonMain.optDouble("temp"));
					// Highest temperature
					data.setMaxTemp(jsonMain.optDouble("temp_max"));
					// Lowest temperature
					data.setMinTemp(jsonMain.optDouble("temp_min"));
					// Pressure
					data.setCurrentPressure(jsonMain.optDouble("pressure"));
					// Sea level
					data.setSeaLevel(jsonMain.optDouble("sea_level"));
					// Ground level
					data.setGroundLevel(jsonMain.optDouble("grnd_level"));
					// Humidity
					data.setHumidity(jsonMain.optInt("humidity", -1));
				}

				// Get wind details
				JSONObject jsonWind = json.optJSONObject("wind");
				if (jsonWind != null) {
					// Wind speed
					data.setWindSpeed(jsonWind.optDouble("speed"));
					// Wind degree
					data.setWindDegree(jsonWind.optDouble("deg"));
				}

				// Get the weather condition
				JSONArray jsonCondArray = json.optJSONArray("weather");
				if (jsonCondArray != null) {
					JSONObject jsonCond = jsonCondArray.optJSONObject(0);
					if (jsonCond != null) {
						data.setConditionId(jsonCond.optInt("id", -1));
						// Main condition
						data.setConditionMain(jsonCond.optString("main"));
						// Description
						data.setConditionDesc(jsonCond.optString("description"));
						// Default icon
						data.setConditionIcon(jsonCond.optString("icon"));
					}
				}

				// Get rain details
				JSONObject jsonRain = json.optJSONObject("rain");
				if (jsonRain != null) {
					// Rain precipitation
					data.setRain3hInMM(jsonRain.optDouble("3h"));
				}

				// Get clouds details
				JSONObject jsonClouds = json.optJSONObject("clouds");
				if (jsonClouds != null) {
					data.setCloudiness(jsonClouds.optInt("all", -1));
				}

				// Get location details
				JSONObject jsonCoord = json.optJSONObject("coord");
				if (jsonCoord != null) {
					// Latitude
					data.setLat(jsonCoord.optDouble("lat"));
					// Longitude
					data.setLon(jsonCoord.optDouble("lon"));
				}

				// Get other information
				JSONObject jsonOther = json.optJSONObject("sys");
				if (jsonOther != null) {
					// Country
					data.setCountry(jsonOther.optString("country"));
					// Sunrise
					data.setUtcSunrise(jsonOther.optDouble("sunrise"));
					// Sunset
					data.setUtcSunset(jsonOther.optDouble("sunset"));
				}

				// Store weather data in a model
				dataModel.storeObject(DataManager.MODEL_KEY_WEATHER, data);
			} else if (responseCode.length() > 0
					&& responseCode.contains("404")) {
				result = ERROR_NOT_FOUND_CITY;
			}
		}

		Logger.printMessage(TAG, Integer.toString(result), Logger.DEBUG);

		return result;
	}
}