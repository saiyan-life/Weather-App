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
package org.sysdll.android.weatherapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class AlarmReceiver extends BroadcastReceiver {

	public static final String KEY_EVENT_NAME = "KEY_EVENT_NAME";
	public static final String KEY_EVENT_DATE = "KEY_EVENT_DATE";
	public static final String KEY_ALARM_ID = "KEY_ALARM_ID";
	public static final String ACTION_ALARM = "ACTION_ALARM";

	@SuppressLint("Wakelock")
	@Override
	public void onReceive(Context context, Intent intent) {
		// Keep the device from sleeping or screen from dimming
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, getClass().getSimpleName());
		wl.acquire();
		Intent intentService = new Intent(context, AlarmService.class);
		intentService.putExtras(intent);
		context.startService(intentService);
		wl.release();
	}
}