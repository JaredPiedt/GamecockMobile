package com.gamecockmobile.events;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gamecockmobile.R;
import com.gamecockmobile.provider.ScheduleDatabase;
import com.gamecockmobile.util.UIUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class EventDetailsActivity extends ActionBarActivity {

    Bundle mBundle;
    ScheduleDatabase mDB;
    Event mEvent;

    FloatingActionButton mFAB;
    TextView mTextViewEventTitle;
    TextView mTextViewEventCourse;
    TextView mTextViewEventDate;
    TextView mTextViewEventTime;
    TextView mTextViewEventNotification;

    Toolbar mToolbar;

    private static final String EVENT_ID = "Event ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        mDB = new ScheduleDatabase(this);

        mTextViewEventTitle = (TextView) findViewById(R.id.textView_eventTitle);
        mTextViewEventCourse = (TextView) findViewById(R.id.textView_eventCourse);
        mTextViewEventDate = (TextView) findViewById(R.id.textView_eventDate);
        mTextViewEventTime = (TextView) findViewById(R.id.textView_eventTime);
        mTextViewEventNotification = (TextView) findViewById(R.id.textView_eventNotification);

        mFAB = (FloatingActionButton) findViewById(R.id.fab_edit_event);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_event_details);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_ab_close));

        mBundle = getIntent().getExtras();

        if(mBundle != null){
            int id = mBundle.getInt(EVENT_ID);
            mEvent = mDB.getEvent(id);

            if(mEvent != null){
                TypedArray notifs = getResources().obtainTypedArray(R.array.reminders_array);
                TypedArray colors = getResources().obtainTypedArray(R.array.event_colors);
                int color = colors.getColor(mEvent.getType(), 0);
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);
                hsv[2] *= 0.8f;
                int darkColor = Color.HSVToColor(hsv);

                if(UIUtils.hasLollipop()) {
                    Window window = this.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(darkColor);
                }

                mToolbar.setBackgroundColor(color);
                if(mToolbar != null) {
                    setSupportActionBar(mToolbar);
                }
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("");

                mTextViewEventTitle.setText(mEvent.getName());
                mTextViewEventTitle.setBackgroundColor(color);
                mTextViewEventCourse.setText(mEvent.getCourse());
                // set up the flags and initialize the formatted date string
                int flags = DateUtils.FORMAT_SHOW_DATE;
                flags |= DateUtils.FORMAT_SHOW_YEAR;
                String timeString = DateUtils.formatDateTime(getApplicationContext(), mEvent.getDate(), flags);
                mTextViewEventDate.setText(timeString);
                mTextViewEventTime.setText(mEvent.getStartTimeAsString(this) + " - " + mEvent.getEndTimeAsString(this));
                mTextViewEventNotification.setText(notifs.getText(mEvent.getNotifications().get(0)));
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent();
                setResult(-1, i);
                finish();
                return true;
            case R.id.action_delete:
                System.out.println("Save button pressed");
                mDB.deleteEvent(mEvent);
                i = new Intent();
                setResult(1, i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            setResult(1);
            finish();
        }
    }
}
