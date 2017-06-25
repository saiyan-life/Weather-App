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
package org.sysdll.android.weatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.sysdll.android.weatherapp.logger.Logger;

import android.text.format.DateUtils;


public class Utils implements Constants {

	/**
	 * Determine if the Android OS on device is equal or larger than the target
	 * api.
	 * 
	 * @param target_api
	 *            - target api version of the Android OS
	 * @return True if the current Android OS is equal or larger than target api
	 *         version, false otherwise
	 */
	public static boolean androidMinimum(int target_api) {
		if (android.os.Build.VERSION.SDK_INT >= target_api)
			return true;
		else
			return false;
	}

	/**
	 * Convert the UTC time in seconds to local time
	 * 
	 * @param timeInSeconds
	 *            - UTC time in seconds
	 * @param format
	 *            - output format of the time, e.g. "dd/MM/yyyy HH:mm:ss" =
	 *            15/01/2009 23:11:28
	 * @return - formatted local time
	 */
	public static String convertUTCToLocalDate(long timeInSeconds, String format) {
		// get the time in milliseconds
		long timeInMillis = timeInSeconds * DateUtils.SECOND_IN_MILLIS;
		// set the output time format
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		// set the local time-zone
		sdf.setTimeZone(TimeZone.getDefault());

		String localDate = null;

		if (timeInMillis < 0)
			// if the input time is invalid, output the current local time
			localDate = sdf.format(new Date());
		else
			// output the formatted local time
			localDate = sdf.format(new Date(timeInMillis));

		// print your time stamp
		Logger.printMessage("Current date", localDate, Logger.DEBUG);

		return localDate;
	}

	/**
	 * Get the day of the week
	 * 
	 * @param timeInSeconds
	 *            UTC time being parsed
	 * @return the day of the week
	 */
	public static String getDayOfWeek(long timeInSeconds) {
		// get the time in milliseconds
		long timeInMillis = timeInSeconds * DateUtils.SECOND_IN_MILLIS;

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMillis);
		c.setTimeZone(TimeZone.getDefault());

		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return "SUNDAY";
		case Calendar.MONDAY:
			return "MONDAY";
		case Calendar.TUESDAY:
			return "TUESDAY";
		case Calendar.WEDNESDAY:
			return "WEDNESDAY";
		case Calendar.THURSDAY:
			return "THURSDAY";
		case Calendar.FRIDAY:
			return "FRIDAY";
		case Calendar.SATURDAY:
			return "SATURDAY";
		default:
			return null;
		}
	}

	/**
	 * Convert Celsius to Fahrenheit
	 * 
	 * @param c
	 *            - temperature in Celsius
	 * @return temperature in Fahrenheit
	 */
	public static double celsiusToFahrenheit(double c) {
		if (c < 0)
			return c;

		return ((c * 9) / 5 + 32);
	}

	/**
	 * Convert Fahrenheit to Celsius
	 * 
	 * @param f
	 *            - temperature in Fahrenheit
	 * @return temperature in Celsius
	 */
	public static double fahrenheitToCelsius(double f) {
		if (f < 0)
			return f;

		return ((f - 32) * 5 / 9);
	}

	/**
	 * Convert speed in meters per second to kilometers per hour
	 * 
	 * @param mps
	 *            - speed in meters per second
	 * @return speed in kilometers per hour
	 */
	public static Double convertMpsToKmh(double mps) {
		return mps * 60 * 60 / 1000;
	}
}
