package com.gamecockmobile.events;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.widget.TextView;

import com.gamecockmobile.R;
import com.gamecockmobile.util.UIUtils;


public class EventDetailsActivity extends Activity {

    Bundle mBundle;
    EventDatabaseHandler eDB;
    Event mEvent;
    TextView mEventNameTextView;
    TextView mEventCourseTextView;
    TextView mEventTimeTextView;

    private static final String EVENT_ID = "Event ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eDB = new EventDatabaseHandler(this);

        mEventNameTextView = (TextView) findViewById(R.id.event_name_textView);
        mEventCourseTextView = (TextView) findViewById(R.id.event_course_textView);
        mEventTimeTextView = (TextView) findViewById(R.id.event_time_textView);

        mBundle = getIntent().getExtras();

        if(mBundle != null){
            int id = mBundle.getInt(EVENT_ID);
            mEvent = eDB.getEvent(id);

            if(mEvent != null){
                setUpEventDetails();
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
        int id = item.getItemId();
        switch(id) {
            case R.id.action_discard_event_details:
                eDB.deleteEvent(mEvent);
                setResult(1);
                finish();
                overridePendingTransition(R.animator.hold, R.animator.slide_right_out);
                return true;
            case R.id.action_edit_event_details:
                // TODO create an activity to edit an event
                Intent intent = new Intent(this, AddEventActivity.class);
                intent.putExtra(EVENT_ID, mEvent.getId());
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                overridePendingTransition(R.animator.hold, R.animator.slide_right_out);
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

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setUpEventDetails(){
        TypedArray colors = getResources().obtainTypedArray(R.array.event_details_drawables);

        ActionBar actionBar = this.getActionBar();
        actionBar.setBackgroundDrawable(colors.getDrawable(mEvent.getType()));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mEventNameTextView.setText(mEvent.getName());
        if(UIUtils.hasJellybean()) {
            mEventNameTextView.setBackground(colors.getDrawable(mEvent.getType()));
        } else {
            mEventNameTextView.setBackgroundDrawable(colors.getDrawable(mEvent.getType()));
        }
        mEventCourseTextView.setText(mEvent.getCourse());
        mEventTimeTextView.setText(mEvent.getStartTimeAsString(this) + " - " + mEvent.getEndTimeAsString(this));
    }
}
