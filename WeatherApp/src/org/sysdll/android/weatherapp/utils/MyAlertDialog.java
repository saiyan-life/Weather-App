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

/**
 *  This class to customize a alert dialog.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class MyAlertDialog {

	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;

	/**
	 * Set the constructor
	 * 
	 * @param context
	 *            - context of the activity
	 */
	public MyAlertDialog(Context context) {
		builder = new AlertDialog.Builder(context);
	}

	/**
	 * Set the instance from the constructor
	 * 
	 * @param context
	 *            - context of the activity
	 * @param title
	 *            - title of the dialog
	 * @param message
	 *            - message shown in the dialog
	 * @param isCancelable
	 *            - set if the dialog is cancelable
	 */
	public MyAlertDialog(Context context, String title, String message,
			boolean isCancelable) {
		builder = new AlertDialog.Builder(context);
		if (title != null && title.length() > 0)
			setTitle(title);
		if (message != null && message.length() > 0)
			setMessage(message);

		setCancelable(isCancelable);
	}

	/**
	 * Set the title of the dialog
	 * 
	 * @param title
	 *            - title of the dialog
	 */
	public void setTitle(String title) {
		if (builder != null)
			builder.setTitle(title);
	}

	/**
	 * Set the message to display
	 * 
	 * @param message
	 *            - message to be displayed
	 */
	public void setMessage(String message) {
		if (builder != null)
			builder.setMessage(message);
	}

	/**
	 * Set the dialog if it is cancelable
	 * 
	 * @param isCancelable
	 *            - true if the dialog is cancelable, false otherwise.
	 */
	public void setCancelable(boolean isCancelable) {
		if (builder != null)
			builder.setCancelable(isCancelable);
	}

	/**
	 * Add a custom view to be the content of the dialog
	 * 
	 * @param view
	 *            - customized view
	 */
	public void addView(View view) {
		if (builder != null)
			builder.setView(view);
	}

	/**
	 * Set a listener to be invoked when the positive button of the dialog is
	 * pressed.
	 * 
	 * @param text
	 *            - The text to display in the positive button
	 * @param listener
	 *            - The DialogInterface.OnClickListener to use.
	 */
	public void setPositiveButton(String text,
			DialogInterface.OnClickListener listener) {
		builder.setPositiveButton(text, listener);
	}

	/**
	 * Set a listener to be invoked when the negative button of the dialog is
	 * pressed.
	 * 
	 * @param text
	 *            - The text to display in the negative button
	 * @param listener
	 *            - The DialogInterface.OnClickListener to use.
	 */
	public void setNegativeButton(String text,
			DialogInterface.OnClickListener listener) {
		builder.setNegativeButton(text, listener);
	}

	/**
	 * Set a listener to be invoked when the neutral button of the dialog is
	 * pressed.
	 * 
	 * @param text
	 *            - The text to display in the neutral button
	 * @param listener
	 *            - The DialogInterface.OnClickListener to use.
	 */
	public void setNeutralButton(String text,
			DialogInterface.OnClickListener listener) {
		builder.setNeutralButton(text, listener);
	}

	/**
	 * Set a list of items to be displayed as the content in dialog and listener
	 * to be invoked when an item has been selected
	 * 
	 * @param choices
	 *            - text of the items to be displayed on the list.
	 * @param checkedItem
	 *            - Specific a item which is checked. If -1 no items are
	 *            checked.
	 * @param listener
	 *            - The DialogInterface.OnClickListener to use.
	 */
	public void setSingleChoiceItems(String[] choices, int checkedItem,
			DialogInterface.OnClickListener listener) {
		builder.setSingleChoiceItems(choices, checkedItem, listener);
	}

	/**
	 * Set a listener to be invoked if the dialog is canceled.
	 * 
	 * @param text
	 *            - The text to display in the neutral button
	 * @param listener
	 *            - The DialogInterface.OnClickListener to use.
	 */
	public void dismiss(DialogInterface.OnCancelListener listener) {
		builder.setOnCancelListener(listener);
	}

	/**
	 * Create a Dialog with the arguments supplied to this builder. And show
	 * display the dialog on the screen.
	 */
	public void show() {
		if (builder != null) {
			alertDialog = builder.create();
			alertDialog.show();
		}
	}

	/**
	 * Check if the dialog is showing
	 * 
	 * @return true if the dialog is showing, false otherwise
	 */
	public boolean isShowing() {
		if (alertDialog != null)
			return alertDialog.isShowing();
		return false;
	}

	/**
	 * Dismiss the dialog, remove it from the screen
	 */
	public void dismiss() {
		alertDialog.dismiss();
	}
}