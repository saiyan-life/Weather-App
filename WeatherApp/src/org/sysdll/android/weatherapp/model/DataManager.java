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

import java.util.Hashtable;

import org.sysdll.android.weatherapp.logger.Logger;


/**
 * This class is used to store application level data as well as provides
 * mechanism for the different application layers to transfer data between them
 * by storing and retrieving data via key-value pairs.
 * 
 * This class acts as a in-memory cache.
 * 
 * As a guideline, only the temporary transferable data is stored in the hash
 * table and should be cleared once its life cycle is over. The module that
 * adds/caches data should be responsible also for clearing data from cache. <br>
 * For storing the more reused application data in memory, add/refer particular
 * objects to this class which will be accessed only via getters and setters. It
 * should be checked that only small amount of data is kept in this way to avoid
 * out of memory issues.
 * 
 */
public class DataManager {

	private final String TAG = getClass().getSimpleName();

	public static final String MODEL_KEY_WEATHER = "model_weather";
	public static final String MODEL_KEY_FORECAST = "model_forecast";

	/**
	 * Key/value pairs are stored in {@code Hashtable} for in-memory cache.
	 */
	private final Hashtable<String, Object> model;

	/**
	 * Used to provide synchronization
	 */
	private static Object mutex = new Object();

	/**
	 * Singleton object of the class
	 */
	private static DataManager instance;

	private DataManager() {
		model = new Hashtable<String, Object>();
	}

	/**
	 * Factory method to get singleton object of the class
	 * 
	 * @return DataManager instance
	 */
	public static DataManager getInstance() {

		if (instance != null) {
			return instance;
		}

		synchronized (mutex) {
			if (instance == null) {
				instance = new DataManager();
			}
		}
		return instance;
	}

	/**
	 * This method stores object in the data manager hash table
	 * 
	 * @param key
	 *            key for the object, that can be used to retrieve the object
	 *            later
	 * @param object
	 *            object to be stored
	 */
	public void storeObject(String key, Object object) {
		synchronized (mutex) {
			Logger.printMessage(TAG, "storing object " + object + " with key "
					+ key + " in data model ", Logger.INFO);

			model.put(key, object);
		}
	}

	/**
	 * Method to get data from the data manager hash table
	 * 
	 * @param key
	 *            key of the object user wants to get
	 * @return object previously stored with the given key or null if there was
	 *         no object stored with the given key
	 */
	public Object getObject(String key) {
		synchronized (mutex) {

			if (model == null) {
				return null;
			}
			Logger.printMessage(TAG, "reading object " + model.get(key)
					+ " with key " + key + " from data model ", Logger.INFO);
			return model.get(key);
		}
	}

	/**
	 * Method to remove object from the data model
	 * 
	 * @param key
	 *            key of the object to be removed from the data model
	 * @return true if the object was removed successfully or false
	 */
	public boolean removeObject(String key) {
		synchronized (mutex) {

			boolean outcome = false;

			Object object = model.remove(key);

			Logger.printMessage(TAG, "removing following from the data model. "
					+ "key " + key + " object " + object, Logger.DEBUG);

			if (object != null) {
				outcome = true;
			}
			return outcome;
		}
	}

	/**
	 * Method to remove all the data from the data model
	 * 
	 * @return true id the operation is successful, false otherwise
	 */
	public boolean resetDataModel() {
		Logger.printMessage(TAG, "resting data model", Logger.DEBUG);

		synchronized (mutex) {
			model.clear();
		}

		if (model.isEmpty())
			return true;
		else
			return false;
	}

}
