package com.gamecockmobile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gamecockmobile.provider.ScheduleDatabase;


public class CourseDetailsActivity extends ActionBarActivity {
    Toolbar mToolbar;
    ScheduleDatabase mDB;

    TextView mTextViewCourse;
    TextView mTextViewCourseTitle;
    TextView mTextViewSectionDetails;
    TextView mTextViewTimeDetails;
    TextView mTextViewInstructorDetails;
    TextView mTextViewLocationDetails;

    int mPosition;

    private Bundle mBundle;

    private int mCourseID;
    private Course mCourse;
    private static final String MYCOURSE_ID = "course_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        mPosition = getIntent().getIntExtra("position", 0);

        System.out.println(mPosition + " was received from intent");

        mToolbar = (Toolbar) findViewById(R.id.toolbar_course_details);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_ab_close));

        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mTextViewCourse = (TextView) findViewById(R.id.textView_course);
        mTextViewCourseTitle = (TextView) findViewById(R.id.textView_courseTitle);
        mTextViewSectionDetails = (TextView) findViewById(R.id.textView_sectionDetails);
        mTextViewTimeDetails = (TextView) findViewById(R.id.textView_timeDetails);
        mTextViewInstructorDetails = (TextView) findViewById(R.id.textView_instructorDetails);
        mTextViewLocationDetails = (TextView) findViewById(R.id.textView_locationDetails);

        mDB = new ScheduleDatabase(this);

        mBundle = getIntent().getExtras();
        if(mBundle != null) {
            mCourseID = mBundle.getInt(MYCOURSE_ID);
            mCourse = mDB.getMyCourse(mCourseID);
        }

        System.out.println(mCourse.getDept());

        mTextViewCourse.setText(mCourse.getDept() + " " + mCourse.getNumber());
        mTextViewCourseTitle.setText(mCourse.getName());
        mTextViewSectionDetails.setText("Section " + mCourse.getSection());
        mTextViewTimeDetails.setText(mCourse.getTime());
        mTextViewInstructorDetails.setText(mCourse.getInstructor());
        mTextViewLocationDetails.setText(mCourse.getLocation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
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
                mDB.deleteMyCourse(mCourse);
                i = new Intent();
                i.putExtra("position", mPosition);
                System.out.println(mPosition + " was deleted");
                setResult(2, i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
