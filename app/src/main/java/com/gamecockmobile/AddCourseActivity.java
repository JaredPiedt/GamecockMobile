package com.gamecockmobile;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gamecockmobile.provider.ScheduleDatabase;

public class AddCourseActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

  Spinner mSpinnerDepartments;
  Spinner mSpinnerCourseNumbers;
  Spinner mSpinnerSections;
  TextView mTextViewInstructor;
  TextView mTextViewLocation;
  Toolbar mToolbar;

  Bundle mBundle;
  ScheduleDatabase mDB;

  private int mDeptID;
  private int mCourseID;
  private int mSectionID;

  private Course mCourse;
  private int counter = 1;

  public static final String FILE_NAME = "courses";
  boolean mUpdateCourse;

  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_course);

    mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_add_course);
    mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_ab_close));
    mToolbar.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        System.out.println("Clicked!!!!");
      }
    });

    if(mToolbar != null) {
      setSupportActionBar(mToolbar);
    }

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("New course");

    mDB = new ScheduleDatabase(this);
    mSpinnerDepartments = (Spinner) findViewById(R.id.spinner_departments);
    mSpinnerCourseNumbers = (Spinner) findViewById(R.id.spinner_course_numbers);
    mSpinnerSections = (Spinner) findViewById(R.id.spinner_sections);

//    mSpinnerDepartments.setOnItemSelectedListener(this);
//    mSpinnerCourseNumbers.setOnItemSelectedListener(this);
//    mSpinnerSections.setOnItemSelectedListener(this);

    mTextViewInstructor = (TextView) findViewById(R.id.textView_instructor);
    mTextViewLocation = (TextView) findViewById(R.id.textView_location);

    mDeptID = -1;
    mCourseID = -1;
    mSectionID = -1;

    //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_large, mDB.getDepartmentList());
    SimpleCursorAdapter myAdapter = mDB.getDepartmentList();
    mSpinnerDepartments.setAdapter(myAdapter);



    mSpinnerDepartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final long department = id;
        mDeptID = (int)department;
        System.out.println(department);
        SimpleCursorAdapter adapter = mDB.getCoursesForDepartment(department);
        adapter.notifyDataSetChanged();
        mSpinnerCourseNumbers.setAdapter(adapter);

        mSpinnerCourseNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            long course = id;
            mCourseID = (int)course;
            SimpleCursorAdapter adapter = mDB.getSectionForDepartment(department, course);
            adapter.notifyDataSetChanged();
            mSpinnerSections.setAdapter(adapter);

            mSpinnerSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("***** Section id = " + id);
                mSectionID = (int)id;
                mTextViewInstructor.setText(mDB.getInstructorForSection(id));
                mTextViewLocation.setText(mDB.getLocationForSection(id));
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
            });
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
        });
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.add_course, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar actions click
    switch (item.getItemId()) {
      case android.R.id.home:
        System.out.println("Here");
        Intent i = new Intent();
        setResult(-1, i);
        finish();
        return true;
      case R.id.action_save:
        System.out.println("Save button pressed");
        mDB.insertMyCourse(mDeptID, mCourseID, mSectionID);
        i = new Intent();
        setResult(1, i);
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch(view.getId()) {
      case R.id.spinner_departments:
        System.out.println("Hereeeeee");
        mDeptID = (int)id;
        System.out.println(mDeptID);
        SimpleCursorAdapter adapter = mDB.getCoursesForDepartment(mDeptID);
        //adapter.notifyDataSetChanged();
        mSpinnerCourseNumbers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        break;
      case R.id.spinner_course_numbers:
        mCourseID = (int)id;
        SimpleCursorAdapter courseAdapter = mDB.getSectionForDepartment(mDeptID, mCourseID);
        courseAdapter.notifyDataSetChanged();
        mSpinnerSections.setAdapter(courseAdapter);
        break;
      case R.id.spinner_sections:
        mSectionID = (int)id;
        mTextViewInstructor.setText(mDB.getInstructorForSection(mSectionID));
        mTextViewLocation.setText(mDB.getLocationForSection(mSectionID));
        break;
      default:
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }
}
