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

public interface Constants {
	/*
	 * Set this to true to enable log output. Remember to turn this back off
	 * before releasing.
	 */
	static final boolean DEBUG = false;
	//true

	/* Constants related to Api version */
	static final int API_GINGERBREAD = android.os.Build.VERSION_CODES.GINGERBREAD; // 9

	static final int API_JELLY_BEAN_MR1 = android.os.Build.VERSION_CODES.JELLY_BEAN_MR1; // 17

}
