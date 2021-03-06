package com.gamecockmobile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

public class AddDayAndTimeActivity extends Activity implements OnClickListener {

  Button mSelectDaysButton;
  Button mStartTimeButton;
  Button mEndTimeButton;
  private String mTimeZone;
  private Time mStartTime;
  private Time mEndTime;

  boolean mIsStartTime;
  boolean mIsStartDate;

  private CharSequence[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
  private ArrayList<CharSequence> selectedDays = new ArrayList<CharSequence>();
  private ClassTime mClassTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_day_and_time);

    final LayoutInflater inflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View customActionBarView = inflater.inflate(R.layout.action_bar_add_course, null);
    customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // "Done"
            System.out.println("****" + mSelectDaysButton.getText().toString() + "****");
            if ((mSelectDaysButton.getText().toString().startsWith("-")) || (mSelectDaysButton.getText().toString().trim() == "")) {

              System.out.println("No days selected");

              String message = "Please select the class day(s).";
              showAlertDialog(message);
            } else if (mStartTimeButton.getText().toString().trim().length() == 0) {
              String message = "Please select the start time.";
              showAlertDialog(message);
            } else if (mEndTimeButton.getText().toString().trim().length() == 0) {
              String message = "Please select the end time.";
              showAlertDialog(message);
            } else {
              Intent intent = new Intent();

              mClassTime.setDays(selectedDays);
              // classTime.setStartTime();
              // classTime.setEndTime(mEndTimeButton.getText().toString());
              intent.putExtra("classTime", mClassTime);

              // intent.putExtra("Days", mSelectDaysButton.getText());
              // intent.putExtra("StartTime", mStartTimeButton.getText());
              // intent.putExtra("EndTime", mEndTimeButton.getText());
              setResult(2, intent);
              finish();
            }
          }
        });
    customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // "Cancel"
            finish();
          }
        });

    // create a custom layout for the action bar so that it has a save and a cancel button
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
        | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    mClassTime = new ClassTime();
    // initialize all of the buttons
    mSelectDaysButton = (Button) findViewById(R.id.selectDays_button);
    mStartTimeButton = (Button) findViewById(R.id.startTime_button);
    mEndTimeButton = (Button) findViewById(R.id.endTime_button);
    mSelectDaysButton.setOnClickListener(this);

    // set up the timezone and time
    mTimeZone = Time.getCurrentTimezone();
    mStartTime = new Time(mTimeZone);
    mEndTime = new Time(mTimeZone);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.add_day_and_time, menu);
    return true;
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    switch (v.getId()) {
    case R.id.selectDays_button:
      showSelectDaysDialog();
      break;
    default:
      break;
    }
  }

  /**
   * This class is used to create a TimePickerDialog when the start and end time buttons are pressed
   */

  @SuppressLint("ValidFragment")
  private class TimePickerFragment extends DialogFragment implements
      TimePickerDialog.OnTimeSetListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar c = Calendar.getInstance();
      int hour = c.get(Calendar.HOUR_OF_DAY);
      int minute = c.get(Calendar.MINUTE);

      if ((mIsStartTime == true) && (mStartTimeButton.getText() != "")) {
        return new TimePickerDialog(getActivity(), this, mStartTime.hour, mStartTime.minute,
            DateFormat.is24HourFormat(getActivity()));
      } else if ((mIsStartTime == false) && (mEndTimeButton.getText() != "")) {
        // set the end time button picker an hour and a half after the start time

        return new TimePickerDialog(getActivity(), this, mEndTime.hour, mEndTime.minute,
            DateFormat.is24HourFormat(getActivity()));
      } else {
        System.out.println(mStartTimeButton.getText().toString().trim().length());
        if (mStartTimeButton.getText().toString().trim().length() > 0) {
          int hr = mStartTime.hour;

          System.out.println(hr);

          int min = mStartTime.minute;

          System.out.println(min);

          if (min >= 45) {
            hr += 2;
          } else {
            hr += 1;
          }

          System.out.println(hr);

          min = ((min + 75) % 60);

          System.out.println(min);

          return new TimePickerDialog(getActivity(), this, hr, min,
              DateFormat.is24HourFormat(getActivity()));
        } else {
          // Create a new instance of TimePickerDialog and return it
          return new TimePickerDialog(getActivity(), this, 8, 30,
              DateFormat.is24HourFormat(getActivity()));
        }
      }
    }

    /**
     * Displays the text on the button when the time is set
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
      // TODO Auto-generated method stub
      Time startTime = mStartTime;
      Time endTime = mEndTime;

      long startMillis;
      long endMillis;

      if (mIsStartTime == true) {
        startTime.hour = hourOfDay;
        startTime.minute = minute;
        startMillis = startTime.normalize(true);
        mClassTime.setStartTime(startMillis);
        setTime(mStartTimeButton, startMillis);
      } else if (mIsStartTime == false) {
        endTime.hour = hourOfDay;
        endTime.minute = minute;
        endMillis = endTime.normalize(true);
        mClassTime.setEndTime(endMillis);
        setTime(mEndTimeButton, endMillis);
      }

    }

  }

  /**
   * Show the time picker dialog and also listen to see which one is pressed so that you know which
   * button to display the time on
   */
  public void showTimePickerDialog(View v) {
    DialogFragment newFragment = new TimePickerFragment();
    if (v.getId() == R.id.startTime_button) {
      mIsStartTime = true;
    } else // if (v.getId() == R.id.endTime_button)
    {
      mIsStartTime = false;
    }

    newFragment.show(getFragmentManager(), "timePicker");
  }

  /**
   * This method is used to show the days dialog so that the user can choose which days the class
   * time are on
   */
  protected void showSelectDaysDialog() {
    boolean[] checkedDays = new boolean[days.length];
    int count = days.length;

    for (int i = 0; i < count; i++) {
      checkedDays[i] = selectedDays.contains(days[i]);
    }

    DialogInterface.OnMultiChoiceClickListener daysDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

      public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
          selectedDays.add(days[which]);
        } else {
          selectedDays.remove(days[which]);
        }
      }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Select Days");
    builder.setMultiChoiceItems(days, checkedDays, daysDialogListener);
    builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        onChangeSelectedDays();
      }
    });
    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {

      }
    });

    AlertDialog dialog = builder.create();
    dialog.show();

  }

  /**
   * Method to display the text on the selected days button
   */
  protected void onChangeSelectedDays() {
    StringBuilder stringBuilder = new StringBuilder();
    int last = selectedDays.size() - 1;
    int counter = 0;

    if (last >= 0) {
      for (CharSequence day : selectedDays) {
        if (counter == last) {
          stringBuilder.append(day);
        } else {
          stringBuilder.append(day + ", ");
          counter++;
        }
      }
    }

    mSelectDaysButton.setText(stringBuilder.toString());
  }

  /**
   * Method that is used like a toString for the time.
   */
  public void setTime(Button button, long millis) {

    int flags = DateUtils.FORMAT_SHOW_TIME;
    flags |= DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
    String timeString = DateUtils.formatDateTime(getApplicationContext(), millis, flags);

    button.setText(timeString);

    // make sure number 0-9 have a leading zero
    // String minute = new DecimalFormat("00").format(time.minute);
    //
    // if (time.hour == 12) {
    // s += "12";
    // s += ":";
    // s += minute;
    // s += " PM";
    // } else if ((time.hour >= 12) && (time.hour <= 24)) {
    // s += time.hour % 12;
    // s += ":";
    // s += minute;
    // s += " PM";
    // } else if ((time.hour == 24)) {
    // s += "24";
    // s += ":";
    // s += minute;
    // s += " AM";
    // } else {
    // s += time.hour;
    // s += ":";
    // s += minute;
    // s += " AM";
    // }
    // return s;
  }

  private void showAlertDialog(String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message);
    builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

}
