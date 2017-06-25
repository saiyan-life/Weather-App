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

import java.util.List;

import org.sysdll.android.weatherapp.R;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

public class AlarmService extends IntentService{

	public AlarmService() {
		super("AlarmService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		if (isApplicationRunning(this)) {
			Intent msgIntent = new Intent(AlarmReceiver.ACTION_ALARM);
			msgIntent.putExtras(intent.getExtras());
			LocalBroadcastManager.getInstance(this)
					.sendBroadcast(msgIntent);
		} else {
			sendNotification(this, intent.getExtras());
		}
	}

	/**
	 * Put a Alarm into a notification and post it
	 * 
	 * @param b
	 *            Bundle of parameters to include (requires 'event name', 'event
	 *            date' and 'alarm id')
	 */
	private void sendNotification(Context context, Bundle bundle) {
		// Create notification service
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Link the notification tray to the main activity of the application
		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String eventName = bundle.getString(AlarmReceiver.KEY_EVENT_NAME);
		String eventDate = bundle.getString(AlarmReceiver.KEY_EVENT_DATE);
		int alarmId = bundle.getInt(AlarmReceiver.KEY_ALARM_ID);

		PendingIntent contentIntent = PendingIntent.getActivity(context,
				alarmId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

		// Build the notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_stat_notify).setContentTitle(
				context.getString(R.string.remind_title))
				.setContentText(
						context.getString(R.string.remind_message, eventName,
								eventDate));
		mBuilder.setAutoCancel(true);
		mBuilder.setContentIntent(contentIntent);
		// Post the notification
		mNotificationManager.notify(alarmId, mBuilder.build());
	}
	
	/**
	 * Determine if the application is currently running
	 * 
	 * @param context
	 *            - current application context
	 * @return - true if the application is currently running ,false otherwise
	 */
	private boolean isApplicationRunning(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals(
					context.getApplicationContext().getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
