package com.gamecockmobile.events;

import java.util.ArrayList;

import com.gamecockmobile.Course;
import com.gamecockmobile.DatabaseHandler;
import com.gamecockmobile.R;
import com.gamecockmobile.R.layout;
import com.gamecockmobile.R.menu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AddEventActivity extends Activity implements OnClickListener {

  Spinner mSelectCourseSpinner;
  Button mDateButton;
  Spinner mRemindersSpinner;

  DatabaseHandler db;
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

            finish();
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

    db = new DatabaseHandler(this);

    mCourses = db.getAllCourses();

    for (int i = 0; i < mCourses.size(); i++) {
      mCourseNames.add(mCourses.get(i).getCourseName());
      System.out.println(mCourses.get(i).getCourseName());
    }

    mSelectCourseSpinner = (Spinner) findViewById(R.id.selectCourse_spinner);
    mRemindersSpinner = (Spinner) findViewById(R.id.reminders_spinner);

    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<String> selectCourseAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, mCourseNames);
    selectCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSelectCourseSpinner.setAdapter(selectCourseAdapter);
    
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

}
