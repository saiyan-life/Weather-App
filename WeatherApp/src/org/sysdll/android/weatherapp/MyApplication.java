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

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import org.sysdll.android.weatherapp.R;

import android.app.Application;

@ReportsCrashes(formKey = "", // will not be used
				mailTo = "saiyan.libre@gmail.com", 
				mode = ReportingInteractionMode.DIALOG
                )
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		ACRA.getConfig().setResToastText(R.string.crash_toast_text);
		ACRA.getConfig().setResDialogText(R.string.crash_dialog_text);
		ACRA.getConfig().setResDialogIcon(android.R.drawable.ic_dialog_info);
		ACRA.getConfig().setResDialogTitle(R.string.crash_dialog_title);
		ACRA.getConfig().setResDialogCommentPrompt(R.string.crash_dialog_title);
		ACRA.getConfig().setResDialogOkToast(R.string.crash_dialog_ok_toast);
	}
}