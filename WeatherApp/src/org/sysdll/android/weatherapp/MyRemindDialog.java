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

/**
 *  This class to customize a weather reminder dialog.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.sysdll.android.weatherapp.R;
import org.sysdll.android.weatherapp.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MyRemindDialog extends AlertDialog.Builder {

	private Context mContext;
	private Activity mActivity;
	private String eventName = "", eventDate = "";
	private long eventDateInMillis = System.currentTimeMillis()
			+ DateUtils.DAY_IN_MILLIS;
	private int remindDay = 0;
	private int numOfForecastAvailable = 1;
	private final static int MAX_FORECAST_AVAILABLE = 14;
	private Spinner dateSpinner, daysSpinner;

	/**
	 * Set the constructor
	 * 
	 * @param context
	 *            - context of the activity
	 */
	public MyRemindDialog(Context context, Activity activity,
			int numOfForecastAvailable) {
		super(context);
		this.mContext = context;
		this.mActivity = activity;

		this.numOfForecastAvailable = (numOfForecastAvailable < 1) ? 1
				: numOfForecastAvailable;

		// Pre-set the dialog title
		setTitle(mContext.getString(R.string.dialog_reminder_title));
		// Disallow user to cancel the dialog
		setCancelable(false);
		// Default action of the cancel button
		setNegativeButton(mContext.getString(R.string.dialog_cancel),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		// Default action of the "remind me" button
		setPositiveButton(mContext.getString(R.string.dialog_remind),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						setReminder(getEventName(), getEventDate(),
								getEventDateInMillis()
										- DateUtils.DAY_IN_MILLIS
										* getRemindDay());
					}
				});
		setEventReminderView();
	}

	/**
	 * Set a custom view to be the content of the dialog
	 * 
	 * @param view
	 *            - customized view
	 */
	public void setEventReminderView() {
		View vEvent = null;
		vEvent = mActivity.getLayoutInflater().inflate(R.layout.view_event,
				null);
		// Event name input field
		final EditText etEventName = (EditText) vEvent
				.findViewById(R.id.etEventName);
		etEventName
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							setEventName(etEventName.getText().toString()
									.trim());
							return true;
						}
						return false;
					}
				});

		etEventName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// Save the event name
				setEventName(etEventName.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		dateSpinner = (Spinner) vEvent.findViewById(R.id.dateSpinner);
		setDatePicker();
		daysSpinner = (Spinner) vEvent.findViewById(R.id.daysSpinner);
		setRemindDayPicker();

		setView(vEvent);
	}

	private void setDatePicker() {
		// Create date picker spinner
		final ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(
				mContext, android.R.layout.simple_spinner_item,
				android.R.id.text1);
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dateSpinner.setAdapter(dateAdapter);

		// Set tomorrow as the default date
		final Calendar defaultTime = Calendar.getInstance();
		defaultTime.setTimeInMillis(System.currentTimeMillis()
				+ DateUtils.DAY_IN_MILLIS);
		defaultTime.setTimeZone(TimeZone.getDefault());
		String formattedDate = setDisplayDate(defaultTime);
		dateAdapter.add(formattedDate);
		dateAdapter.notifyDataSetChanged();
		setEventDateInMillis(defaultTime.getTimeInMillis());
		setEventDate(setDisplayDate(defaultTime));

		dateSpinner.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// Display the default date on date picker
					final Calendar time = Calendar.getInstance();
					time.setTimeInMillis(getEventDateInMillis());
					time.setTimeZone(TimeZone.getDefault());

					final int mDayOfMonth = time.get(Calendar.DAY_OF_MONTH);
					final int mMonth = time.get(Calendar.MONTH);
					final int mYear = time.get(Calendar.YEAR);
					// Prompt date picker dialog
					DatePickerDialog mDatePickerDialog;
					mDatePickerDialog = new DatePickerDialog(mContext,
							new DatePickerDialog.OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									Calendar pickedDate = Calendar
											.getInstance();
									pickedDate.set(year, monthOfYear,
											dayOfMonth, 0,
											0, 0);
									pickedDate.setTimeZone(TimeZone
											.getDefault());
									// Save the event time
									setEventDateInMillis(pickedDate
											.getTimeInMillis());
									// Save the formatted event time
									setEventDate(setDisplayDate(pickedDate));

									// Display the event time on the spinner
									dateAdapter.clear();
									dateAdapter.add(setDisplayDate(pickedDate));
									dateAdapter.notifyDataSetChanged();

									setRemindDayPicker();
								}
							}, mYear, mMonth, mDayOfMonth) {
						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// Force the date to be set to tomorrow or after
							if (year < mYear)
								view.updateDate(mYear, mMonth, mDayOfMonth);

							if (monthOfYear < mMonth && year == mYear)
								view.updateDate(mYear, mMonth, mDayOfMonth);

							if (dayOfMonth < mDayOfMonth && year == mYear
									&& monthOfYear == mMonth)
								view.updateDate(mYear, mMonth, mDayOfMonth);
						}
					};
					mDatePickerDialog.setTitle(mContext
							.getString(R.string.dialog_datepicker_title));
					mDatePickerDialog.show();
				}
				return true;
			}
		});
	}

	private void setRemindDayPicker() {
		// Create string array for remind-day
		ArrayList<String> remindMeText = new ArrayList<String>();
		for (int i = 0; i < numOfForecastAvailable; i++) {
			if (i == 0)
				remindMeText.add("on same day");
			else if (i == 1
					&& daysDifference(System.currentTimeMillis(),
							getEventDateInMillis()) >= 1)
				remindMeText.add("1 day before");
			else if (i > 1 && i < MAX_FORECAST_AVAILABLE) {
				if (daysDifference(System.currentTimeMillis(),
						getEventDateInMillis()) >= i)
					remindMeText.add(String.format("%s %s", i, "days before"));
			}
		}

		ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, remindMeText);
		dayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daysSpinner.setAdapter(dayAdapter);

		daysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				setRemindDay(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void showDialog() {
		AlertDialog dialog = create();
		if (dialog != null)
			show();
	}

	private void setEventName(String eventName) {
		this.eventName = eventName;
	}

	private String getEventName() {
		return this.eventName;
	}

	private void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	private String getEventDate() {
		return this.eventDate;
	}

	private void setEventDateInMillis(long eventDateInMillis) {
		this.eventDateInMillis = eventDateInMillis;
	}

	private long getEventDateInMillis() {
		return this.eventDateInMillis;
	}

	private void setRemindDay(int remindDay) {
		this.remindDay = remindDay;
	}

	private int getRemindDay() {
		return this.remindDay;
	}

	private String getDayOfWeekInString(int day) {
		switch (day) {
		case 1:
			return "SUN";
		case 2:
			return "MON";
		case 3:
			return "TUE";
		case 4:
			return "WED";
		case 5:
			return "THU";
		case 6:
			return "FRI";
		case 7:
			return "SAT";
		default:
			return "";
		}
	}

	private String setDisplayDate(Calendar time) {
		long timeInSecond = time.getTimeInMillis() / DateUtils.SECOND_IN_MILLIS;

		String formattedDate = Utils.convertUTCToLocalDate(timeInSecond,
				"dd/MM/yyyy");
		formattedDate = String.format("%s %s",
				getDayOfWeekInString(time.get(Calendar.DAY_OF_WEEK)),
				formattedDate);
		return formattedDate;
	}

	public void setReminder(String eventName, String eventDate,
			long remindDateInMillis) {
	};

	private int daysDifference(long start, long end) {
		return (int) ((end - start) / DateUtils.DAY_IN_MILLIS);
	}
}