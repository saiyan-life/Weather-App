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

public class DataWeather {

	private double lat = Double.NaN;
	private double lon = Double.NaN;
	private String country = "";
	private double utcSunrise = Double.NaN;
	private double utcSunset = Double.NaN;
	private int conditionId = -1;
	private String conditionMain = "";
	private String conditionDesc = "";
	private String conditionIcon = "";
	private double currentTemp = Double.NaN;
	private double minTemp = Double.NaN;
	private double maxTemp = Double.NaN;
	private double currentPressure = Double.NaN;
	private double seaLevel = Double.NaN;;
	private double groundLevel = Double.NaN;
	private int humidity = -1; // Humidity in %
	private double windSpeed = Double.NaN;
	private double windDegree = Double.NaN;
	private double rain3hInMM = Double.NaN; // Precipitation volume mm per 3
											// hours
	private int cloudiness = -1; // Cloudiness in %
	private long refreshUTCTime = -1;
	private int id = -1;
	private String city = "";
	private int responseCode = -1;
	private double dayTemp = Double.NaN;
	private double nightTemp = Double.NaN;
	private double morningTemp = Double.NaN;
	private double eveningTemp = Double.NaN;
	private long forecastUTCDate = -1;

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

	public double getUtcSunrise() {
		return utcSunrise;
	}

	public void setUtcSunrise(double utcSunrise) {
		this.utcSunrise = utcSunrise;
	}

	public double getUtcSunset() {
		return utcSunset;
	}

	public void setUtcSunset(double utcSunset) {
		this.utcSunset = utcSunset;
	}

	public int getConditionId() {
		return conditionId;
	}

	public void setConditionId(int conditionId) {
		this.conditionId = conditionId;
	}

	public String getConditionMain() {
		return conditionMain;
	}

	public void setConditionMain(String conditionMain) {
		this.conditionMain = conditionMain;
	}

	public String getConditionDesc() {
		return conditionDesc;
	}

	public void setConditionDesc(String conditionDesc) {
		this.conditionDesc = conditionDesc;
	}

	public String getConditionIcon() {
		return conditionIcon;
	}

	public void setConditionIcon(String conditionIcon) {
		this.conditionIcon = conditionIcon;
	}

	public double getCurrentTemp() {
		return currentTemp;
	}

	public void setCurrentTemp(double currentTemp) {
		this.currentTemp = currentTemp;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(double minTemp) {
		this.minTemp = minTemp;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public double getCurrentPressure() {
		return currentPressure;
	}

	public void setCurrentPressure(double currentPressure) {
		this.currentPressure = currentPressure;
	}

	public double getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(double seaLevel) {
		this.seaLevel = seaLevel;
	}

	public double getGroundLevel() {
		return groundLevel;
	}

	public void setGroundLevel(double groundLevel) {
		this.groundLevel = groundLevel;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getWindDegree() {
		return windDegree;
	}

	public void setWindDegree(double windDegree) {
		this.windDegree = windDegree;
	}

	public double getRain3hInMM() {
		return rain3hInMM;
	}

	public void setRain3hInMM(double rain3hInMM) {
		this.rain3hInMM = rain3hInMM;
	}

	public int getCloudiness() {
		return cloudiness;
	}

	public void setCloudiness(int cloudiness) {
		this.cloudiness = cloudiness;
	}

	public long getRefreshUTCTime() {
		return refreshUTCTime;
	}

	public void setRefreshUTCTime(long refreshUTCTime) {
		this.refreshUTCTime = refreshUTCTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public double getDayTemp() {
		return dayTemp;
	}

	public void setDayTemp(double dayTemp) {
		this.dayTemp = dayTemp;
	}

	public double getNightTemp() {
		return nightTemp;
	}

	public void setNightTemp(double nightTemp) {
		this.nightTemp = nightTemp;
	}

	public double getMorningTemp() {
		return morningTemp;
	}

	public void setMorningTemp(double morningTemp) {
		this.morningTemp = morningTemp;
	}

	public double getEveningTemp() {
		return eveningTemp;
	}

	public void setEveningTemp(double eveningTemp) {
		this.eveningTemp = eveningTemp;
	}

	public long getForecastUTCDate() {
		return forecastUTCDate;
	}

	public void setForecastUTCDate(long forecastUTCDate) {
		this.forecastUTCDate = forecastUTCDate;
	}

}