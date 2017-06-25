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
package org.sysdll.android.weatherapp.model;

import java.util.ArrayList;

public class DataForecast {

	private int responseCode = -1;
	private int cityId = -1;
	private String city = "";
	private double lat = Double.NaN;
	private double lon = Double.NaN;
	private String country = "";
	private int population = -1;
	private int numberOfResult = -1;
	private ArrayList<DataWeather> forecastList = new ArrayList<DataWeather>();

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getNumberOfResult() {
		return numberOfResult;
	}

	public void setNumberOfResult(int numberOfResult) {
		this.numberOfResult = numberOfResult;
	}

	public ArrayList<DataWeather> getForecastList() {
		return forecastList;
	}

	public void setForecastList(ArrayList<DataWeather> forecastList) {
		this.forecastList = forecastList;
	}

}