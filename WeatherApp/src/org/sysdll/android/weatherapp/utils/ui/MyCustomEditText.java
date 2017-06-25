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
package org.sysdll.android.weatherapp.utils.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnimationSet;
import android.widget.EditText;

/**
 * This class to custom the EditText
 */
public class MyCustomEditText extends EditText {

	private AnimationSet inflateAnim, deflateAnim;

	public MyCustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setInflation(AnimationSet anim) {
		this.inflateAnim = anim;
	}

	public void setDeflation(AnimationSet anim) {
		this.deflateAnim = anim;
	}

	public void startInflation() {
		if (getAnimation() == null || getAnimation().hasEnded())
			startAnimation(inflateAnim);
	}

	public void startDeflation() {
		if (getAnimation() == null || getAnimation().hasEnded())
			startAnimation(deflateAnim);
	}
}