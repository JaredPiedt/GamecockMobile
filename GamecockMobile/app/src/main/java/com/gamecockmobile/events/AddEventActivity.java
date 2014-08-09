package com.gamecockmobile.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.gamecockmobile.ClassTime;
import com.gamecockmobile.Course;
import com.gamecockmobile.DatabaseHandler;
import com.gamecockmobile.R;
import com.gamecockmobile.util.LogUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
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

/**
 * 'AddEventActivity' class.
 * <p/>
 * This class is used to add a new 'Event'. The user inputs all of the event details and then the
 * event is added to the database.
 *
 * @author Jared W. Piedt
 */
@SuppressWarnings("deprecation")
public class AddEventActivity extends Activity implements OnClickListener {

    EditText mEventNameEditText;
    Spinner mSelectCourseSpinner;
    Spinner mSelectTypeSpinner;
    Button mDateButton;
    Button mStartTimeButton;
    Button mEndTimeButton;
    Spinner mRemindersSpinner;

    private String mTimeZone;
    private boolean mIsStartTime;
    private Time mStartTime;
    private Time mEndTime;
    private boolean mUpdateEvent;

    DatabaseHandler db;
    EventDatabaseHandler eDB;

    private Event mEvent;
    private ArrayList<String> mCourseNames = new ArrayList<String>();

    private static final String TAG = LogUtils.makeLogTag(AddEventActivity.class);
    private static final String EVENT_ID = "Event ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        final LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.action_bar_add_course, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    //
                    public void onClick(View v) {
                        // "Done"
                        Intent intent = new Intent();
                        int name = mSelectCourseSpinner.getSelectedItemPosition();

                        mEvent.setName(mEventNameEditText.getText().toString());
                        mEvent.setCourse(mCourseNames.get(name));
                        mEvent.setType(mSelectTypeSpinner.getSelectedItemPosition());
                        mEvent.addNotification(mRemindersSpinner.getSelectedItemPosition());

                        if(mUpdateEvent){
                            eDB.updateEvent(mEvent, getApplicationContext());
                            LogUtils.LOGD(TAG, "Updating event " + mEvent.getId());
                        } else {
                            eDB.addEvent(mEvent);
                            LogUtils.LOGD(TAG, "Adding event.");
                        }

                        setResult(1, intent);
                        finish();
                    }
                }
        );
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Cancel"
                        setResult(2);
                        finish();
                    }
                }
        );

        // create a custom layout for the action bar so that it has a save and a cancel button
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // initialize the databases
        db = new DatabaseHandler(this);
        eDB = new EventDatabaseHandler(this);

        mEvent = new Event();

        ArrayList<Course> mCourses = db.getAllCourses();
        mTimeZone = Time.getCurrentTimezone();
        mStartTime = new Time(mTimeZone);
        mEndTime = new Time(mTimeZone);

        // add course name to the 'ArrayList' of 'Courses' in order to create the 'Spinner' of 'Course'
        // names
        for (Course mCourse : mCourses) {
            mCourseNames.add(mCourse.getCourseName());
            System.out.println(mCourse.getCourseName());
        }

        // initialize all of the 'Buttons' and 'Spinners'
        mEventNameEditText = (EditText) findViewById(R.id.eventName_editText);
        mSelectCourseSpinner = (Spinner) findViewById(R.id.selectCourse_spinner);
        mSelectTypeSpinner = (Spinner) findViewById(R.id.selectType_spinner);
        mDateButton = (Button) findViewById(R.id.date_button);
        mStartTimeButton = (Button) findViewById(R.id.event_startTime_button);
        mEndTimeButton = (Button) findViewById(R.id.event_endTime_button);
        mRemindersSpinner = (Spinner) findViewById(R.id.reminders_spinner);

        // Create an ArrayAdapter for the 'Spinner' used to select the 'Course' using the string array
        // and a default spinner layout
        ArrayAdapter<String> selectCourseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mCourseNames);
        selectCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectCourseSpinner.setAdapter(selectCourseAdapter);

        // Create an ArrayAdapter for the 'Spinner' used to select the type using the string array and a
        // default spinner layout
        ArrayAdapter<CharSequence> selectTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        selectTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectTypeSpinner.setAdapter(selectTypeAdapter);

        // Create an ArrayAdapter for the 'Spinner' used to select reminder times using the string array
        // and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRemindersSpinner.setAdapter(adapter);

        Bundle mBundle = getIntent().getExtras();
        if(mBundle != null){
            int id = mBundle.getInt(EVENT_ID);
            mEvent = eDB.getEvent(id);
            if(mEvent == null){
                mUpdateEvent = false;
            } else {
                mUpdateEvent = true;
                LogUtils.LOGD(TAG, "Editing an event.");
                setUpLayout();
            }
        } else {
            mUpdateEvent = false;

            LogUtils.LOGD(TAG, "Creating a new event.");


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_event, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    /**
     * This class is used to set up the 'DatePicker'.
     */
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

        /**
         * Method used to display the date when it is selected.
         */
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Time date = new Time(mTimeZone);
            long millis;
            date.year = year;
            date.month = month;
            date.monthDay = day;

            millis = date.normalize(true);
            mEvent.setDate(millis);

            // set up the flags and initialize the formatted date string
            int flags = DateUtils.FORMAT_SHOW_DATE;
            flags |= DateUtils.FORMAT_SHOW_YEAR;
            String timeString = DateUtils.formatDateTime(getApplicationContext(), millis, flags);

            mDateButton.setText(timeString);
            System.out.println("Date was set");

            autoDisplayTimes(millis);
        }

    }

    /**
     * Method used to display the 'DialogFragment' that contains the 'DatePicker'.
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * This class is used to create a TimePickerDialog when the start and end time buttons are pressed
     */

    @SuppressLint("ValidFragment")
    private class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if ((mIsStartTime) && (mStartTimeButton.getText() != "")) {
                return new TimePickerDialog(getActivity(), this, mStartTime.hour, mStartTime.minute,
                        DateFormat.is24HourFormat(getActivity()));
            } else if ((!mIsStartTime) && (mEndTimeButton.getText() != "")) {
                // set the end time button picker an hour and a half after the start time

                return new TimePickerDialog(getActivity(), this, mEndTime.hour, mEndTime.minute,
                        DateFormat.is24HourFormat(getActivity()));
            } else {
                System.out.println(mStartTimeButton.getText().toString().trim().length());
                if (mStartTimeButton.getText().toString().trim().length() > 0) {
                    int hr = mStartTime.hour;
                    int min = mStartTime.minute;

                    if (min >= 45) {
                        hr += 2;
                    } else {
                        hr += 1;
                    }

                    min = ((min + 75) % 60);

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
            Time startTime = mStartTime;
            Time endTime = mEndTime;

            long startMillis;
            long endMillis;

            if (mIsStartTime) {
                startTime.hour = hourOfDay;
                startTime.minute = minute;
                startMillis = startTime.normalize(true);
                mEvent.setStartTime(startMillis);
                setTime(mStartTimeButton, startMillis);
            } else if (!mIsStartTime) {
                endTime.hour = hourOfDay;
                endTime.minute = minute;
                endMillis = endTime.normalize(true);
                mEvent.setEndTime(endMillis);
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

        mIsStartTime = v.getId() == R.id.event_startTime_button;

        newFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Method that is used like a toString for the time.
     */
    public void setTime(Button button, long millis) {

        int flags = DateUtils.FORMAT_SHOW_TIME;
        flags |= DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
        String timeString = DateUtils.formatDateTime(getApplicationContext(), millis, flags);

        button.setText(timeString);
    }

    /**
     * Method used to auto display start and end time.
     * <p/>
     * This method is used to speed up the process of adding a new event. It takes the day of the
     * week for the selected date and then, using the selected course, checks to see if that
     * course has a class time on that day. If it does, it gets the start and end time for that day
     * and then sets the start and end time button accordingly.
     *
     * @param date the selected date in milliseconds
     */
    private void autoDisplayTimes(long date) {
        int which = mSelectCourseSpinner.getSelectedItemPosition();
        String name = mCourseNames.get(which);
        Course course = db.getCourseByName(name);
        ArrayList<ClassTime> classTimes = course.getClassTimes();

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date);
        String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);

        for (ClassTime ct : classTimes) {
            ArrayList<CharSequence> days = ct.getDays();

            for (CharSequence cs : days) {
                if (dayOfWeek.equals(cs.toString())) {
                    setTime(mStartTimeButton, ct.getStartTime());
                    setTime(mEndTimeButton, ct.getEndTime());
                }
            }
        }
    }

    private void setUpLayout(){
        mEventNameEditText.setText(mEvent.getName());
        mSelectCourseSpinner.setSelection(mCourseNames.indexOf(mEvent.getCourse()));
        mSelectTypeSpinner.setSelection(mEvent.getType());

        // set up the flags and initialize the formatted date string
        int flags = DateUtils.FORMAT_SHOW_DATE;
        flags |= DateUtils.FORMAT_SHOW_YEAR;
        String timeString = DateUtils.formatDateTime(getApplicationContext(), mEvent.getDate(), flags);

        mDateButton.setText(timeString);

        Time startTime = new Time();
        startTime.set(mEvent.getStartTime());
        mStartTime = startTime;
        mStartTimeButton.setText(mEvent.getStartTimeAsString(this));

        Time endTime = new Time();
        endTime.set(mEvent.getEndTime());
        mEndTime = endTime;
        mEndTimeButton.setText(mEvent.getEndTimeAsString(this));

        mRemindersSpinner.setSelection(mEvent.getNotifications().get(0));

    }
}
