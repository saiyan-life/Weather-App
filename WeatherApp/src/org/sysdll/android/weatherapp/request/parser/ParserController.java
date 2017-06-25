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

import org.json.JSONObject;
import org.sysdll.android.weatherapp.logger.Logger;
import org.sysdll.android.weatherapp.request.ActionTypes;
import org.sysdll.android.weatherapp.request.ResultType;


/**
 * This is main class that acts as the controller for the parsing, handling
 * which parser needs to be called in which situation.
 */
public class ParserController implements ActionTypes, ResultType {
	private final String TAG = getClass().getSimpleName();
	private static ParserController instance = null;
	private static Object mutex = new Object();

	private ParserController() {
	}

	/**
	 * Factory method to get singleton object of the class
	 */
	public static ParserController getInstance() {
		if (null == instance) {
			synchronized (mutex) {
				instance = new ParserController();
			}
		}
		return instance;
	}

	/**
	 * Main controller method which decides which parser to call depending on
	 * the action for which response was generated. For more actions add 'case'
	 * statements for checking which action was performed
	 * 
	 * @param data
	 *            Response in JSON object received from the server which needs
	 *            to be parsed
	 * @param aciton
	 *            action indicates which response was generated
	 * @return {@link ResultType}
	 */
	public int parseResonseFromJSON(JSONObject jsonResponse, int action) {

		Logger.printMessage(TAG,
				"Response  Message \n" + jsonResponse.toString(), Logger.DEBUG);
		Logger.printMessage(TAG, "parsing json for action: " + action,
				Logger.DEBUG);

		int outcome = ResultType.FAIL;

		switch (action) {
		case ActionTypes.GET_WEATHER: {
			GetWeatherParser parser = new GetWeatherParser();
			outcome = parser.parseResponse(jsonResponse);
			break;
		}
		case ActionTypes.GET_FORECAST: {
			GetForecastParser parser = new GetForecastParser();
			outcome = parser.parseResponse(jsonResponse);
			break;
		}
		default:
			break;
		}
		return outcome;
	}
}
