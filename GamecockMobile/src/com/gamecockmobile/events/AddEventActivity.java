package com.gamecockmobile.events;

import java.util.ArrayList;
import java.util.Calendar;

import com.gamecockmobile.Course;
import com.gamecockmobile.DatabaseHandler;
import com.gamecockmobile.R;
import com.gamecockmobile.R.layout;
import com.gamecockmobile.R.menu;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AddEventActivity extends Activity implements OnClickListener {

  EditText mEventNameEditText;
  Spinner mSelectCourseSpinner;
  Spinner mSelectTypeSpinner;
  Button mDateButton;
  Spinner mRemindersSpinner;

  private String mTimeZone;

  DatabaseHandler db;
  EventDatabaseHandler eDB;
  
  private Event mEvent;
  private ArrayList<Course> mCourses = new ArrayList<Course>();
  private ArrayList<String> mCourseNames = new ArrayList<String>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_event);

    final LayoutInflater inflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View customActionBarView = inflater.inflate(R.layout.action_bar_add_course, null);
    customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // "Done"
            Intent intent = new Intent();
            int name = mSelectCourseSpinner.getSelectedItemPosition();
            mEvent.setCourse(mEventNameEditText.getText().toString());
            mEvent.setName(mCourseNames.get(name));
            mEvent.setType(mSelectCourseSpinner.getSelectedItemPosition());
            mEvent.addNotification(mRemindersSpinner.getSelectedItemPosition());
            eDB.addEvent(mEvent);
            System.out.println("event add successfully");

            setResult(1, intent);
            finish();
          }
        });
    customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // "Cancel"
            setResult(2);
            finish();
          }
        });

    // create a custom layout for the action bar so that it has a save and a cancel button
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
        | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    db = new DatabaseHandler(this);
    eDB = new EventDatabaseHandler(this);

    mEvent = new Event();
    mCourses = db.getAllCourses();
    mTimeZone = Time.getCurrentTimezone();

    for (int i = 0; i < mCourses.size(); i++) {
      mCourseNames.add(mCourses.get(i).getCourseName());
      System.out.println(mCourses.get(i).getCourseName());
    }

    mEventNameEditText = (EditText) findViewById(R.id.eventName_editText);
    mSelectCourseSpinner = (Spinner) findViewById(R.id.selectCourse_spinner);
    mSelectTypeSpinner = (Spinner) findViewById(R.id.selectType_spinner);
    mDateButton = (Button) findViewById(R.id.date_button);
    mRemindersSpinner = (Spinner) findViewById(R.id.reminders_spinner);

    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<String> selectCourseAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, mCourseNames);
    selectCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSelectCourseSpinner.setAdapter(selectCourseAdapter);

    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> selectTypeAdapter = ArrayAdapter.createFromResource(this,
        R.array.types_array, android.R.layout.simple_spinner_item);
    selectTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSelectTypeSpinner.setAdapter(selectTypeAdapter);

    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.reminders_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    mRemindersSpinner.setAdapter(adapter);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.add_event, menu);
    return true;
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    switch (v.getId()) {
    default:
      break;
    }
  }

  @SuppressLint("ValidFragment")
  private class DatePickerFragment extends DialogFragment implements
      DatePickerDialog.OnDateSetListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the current date as the default date in the picker
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);

      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
      // TODO Auto-generated method stub
      Time date = new Time(mTimeZone);
      date.year = year;
      date.month = month;
      date.monthDay = day;
      long millis;
      millis = date.normalize(true);
      mEvent.setDate(millis);

      int flags = DateUtils.FORMAT_SHOW_DATE;
      flags |= DateUtils.FORMAT_SHOW_YEAR;
      String timeString = DateUtils.formatDateTime(getApplicationContext(), millis, flags);

      // timeString += ", ";
      // timeString += year;
      mDateButton.setText(timeString);
    }

  }

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getFragmentManager(), "datePicker");
  }
}
