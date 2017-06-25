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
package org.sysdll.android.weatherapp.logger;

import org.sysdll.android.weatherapp.utils.Constants;

import android.util.Log;

/**
 * This class would be used for logging. It use the android default logger for
 * logging. <br>
 * 
 * We can enable/disable the logging globally via {@code DEBUG} <br>
 * 
 * Usage: <br>
 * 
 * <code>
 * Logger.printMessage(TAG, "Print Message", Logger.DEBUG);
 * </code> <br>
 * 
 * TAG is generally a used as a static string in each class containing the class
 * name. This is to identify from which class the log was generated. <br>
 * 
 * {@code ALL, INFO, DEBUG, WARN, ERROR } are the different logging levels
 * provided by this class. Debug logs are compiled in but stripped at runtime.
 * Error, Warning and Info logs are always kept.
 * 
 */

public class Logger {
	/**
	 * Debug logs are compiled in but stripped at runtime. Error, warning and
	 * info logs are always kept.
	 */
	public static final int ALL = 1;
	public static final int INFO = 2;
	public static final int DEBUG = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	public static final int VERBOSE = 6;
	public static final int ASSERT = 7;
	private static final int CURRENT_LOGGING_LEVEL = ALL;

	/**
	 * Method used to print logs to DDMS Logcat
	 * 
	 * @param tag
	 *            String to identify the log. Recommended use of class name as
	 *            tag
	 * @param msg
	 *            Actual error message to print
	 * @param loggingLevel
	 *            Sets the logging level of log. Can contain values
	 *            {@code ALL, INFO, DEBUG, WARN, ERROR, VERBOSE, ASSERT }
	 */
	public static void printMessage(String tag, String msg, int loggingLevel) {
		try {
			// If debug is enabled
			if (Constants.DEBUG) {
				if (loggingLevel >= CURRENT_LOGGING_LEVEL) {
					Log.d("Delim", "-----------------------------");
					switch (loggingLevel) {
					case INFO:
						Log.i(tag, msg);
						break;
					case DEBUG:
						Log.d(tag, msg);
						break;
					case WARN:
						Log.w(tag, msg);
						break;
					case ERROR:
						Log.e(tag, msg);
						break;
					case VERBOSE:
						Log.v(tag, msg);
						break;
					}
				}
			}
		} catch (Exception e) {
			Log.e("Logger", e.getMessage());
		}
	}
}