package com.gamecockmobile;

import java.util.ArrayList;
import java.util.Scanner;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.LoaderManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CourseListActivity extends Activity implements OnClickListener, OnLongClickListener, OnNavigationListener {

  DatabaseHandler db;

  private ArrayList<String> mCourses = new ArrayList<String>();

  public static final String FILE_NAME = "courses";
  private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_list);

    // getApplicationContext().deleteDatabase("CoursesManager.db");
    
    String[] strings = getResources().getStringArray(R.array.schedule_action_list);
    
    ArrayAdapter<String> aAdapt = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, android.R.id.text1, strings);
    
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);    
    actionBar.setListNavigationCallbacks(aAdapt, this);

    db = new DatabaseHandler(this);
    Log.d("db", Integer.toString(db.getCoursesCount()));
    ArrayList<Course> courses = db.getAllCourses();

    for (Course c : courses) {
      Log.d("Insert", c.toString());

      mCourses.add(c.getCourseName());

      LinearLayout layout = (LinearLayout) findViewById(R.id.courseList_Layout);

      // initialize the TextView that the class day and time will be displayed in, set the text,
      // and
      // add it to the layout
      TextView textView = new TextView(this);
      textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
          LayoutParams.WRAP_CONTENT));
      textView.setTextSize(18);
      textView.setText(c.getCourseName());
      // textView.append("\n" + tempClassTime.getStartTimeAsString(getApplicationContext()) + " - "
      // + tempClassTime.getEndTimeAsString(getApplicationContext()));
      textView.setPadding(10, 10, 10, 10);
      textView.setLineSpacing(8, 1);
      textView.setBackgroundColor(getResources().getColor(android.R.color.white));
      layout.addView(textView);

      textView.setId(c.getID());
      textView.setOnClickListener(this);
      textView.setLongClickable(true);
      textView.setOnLongClickListener(this);

      // initialize the divider, set it's properties, and add it to the layout
      View divider = new View(this);
      divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 2));
      divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
      layout.addView(divider);
    }

  }
  
  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    // Restore the previously serialized current dropdown position.
    if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
      getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    // Serialize the current dropdown position.
    outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
        .getSelectedNavigationIndex());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.course_list, menu);
    return true;
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    int id = v.getId();
    Intent intent = new Intent(CourseListActivity.this, AddCourseActivity.class);
    Log.d("Click", id + " was clicked");

    Course tempCourse = db.getCourse(id);
    intent.putExtra("course", tempCourse);
    startActivityForResult(intent, 1);

  }

  @Override
  public boolean onLongClick(View v) {
    final View view = v;
    final Course course = db.getCourse(v.getId());
    final CharSequence[] mDialogList = { "Delete", "Edit" };

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(course.getCourseName());
    builder.setItems(mDialogList, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        if (which == 0) {
          db.deleteCourse(course);

          LinearLayout layout = (LinearLayout) findViewById(R.id.courseList_Layout);

          layout.removeView(view);

        } else if (which == 1) {
          Intent intent = new Intent(CourseListActivity.this, AddCourseActivity.class);
          intent.putExtra("course", course);
          startActivityForResult(intent, 1);
        }
      }
    });
    AlertDialog dialog = builder.create();

    dialog.show();

    return true;

  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // check if the request code is same as what is passed here it is 2 and check that the Intent is
    // not equal to null so app doesn't crash when back button is pressed
    if (requestCode == 1 && data != null) {

      Course tempCourse = (Course) data.getParcelableExtra("course");

      Log.d("Adding", "course added: " + tempCourse.getCourseName());

      if (findViewById(tempCourse.getID()) != null) {
        db.updateCourse(tempCourse, getApplicationContext());

        TextView tv = (TextView) findViewById(tempCourse.getID());
        tv.setText(tempCourse.getCourseName());
      } else {
        Log.d("Insert:", "Inserting...");
        db.addCourse(tempCourse, getApplicationContext());

        ArrayList<Course> courses = db.getAllCourses();

        for (int i = 0; i < courses.size(); i++) {
          Course course = courses.get(i);

          System.out.println(tempCourse.getCourseName());
          System.out.println(course.getCourseName());

          if (tempCourse.getCourseName().equals(course.getCourseName())) {
            tempCourse = course;

            System.out.println(tempCourse.getID());
          }
        }

        // initialize the layout that the new text view will be added to
        LinearLayout layout = (LinearLayout) findViewById(R.id.courseList_Layout);

        // initialize the TextView that the class day and time will be displayed in, set the text,
        // and
        // add it to the layout
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT));
        textView.setTextSize(18);
        textView.setText(tempCourse.getCourseName());
        // textView.append("\n" + tempClassTime.getStartTimeAsString(getApplicationContext()) +
        // " - "
        // + tempClassTime.getEndTimeAsString(getApplicationContext()));
        textView.setPadding(10, 10, 10, 10);
        textView.setLineSpacing(8, 1);
        textView.setBackgroundColor(getResources().getColor(android.R.color.white));
        layout.addView(textView);

        // set the id equal to the courses count because it was the last one added in the db
        textView.setId(tempCourse.getID());

        textView.setOnClickListener(this);
        textView.setOnLongClickListener(this);

        // initialize the divider, set it's properties, and add it to the layout
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 2));
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        layout.addView(divider);

      }

    }

  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.action_new:
      Intent intent = new Intent(CourseListActivity.this, AddCourseActivity.class);
      startActivityForResult(intent, 1);
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  public void displayCourses(String content) {
    // initialize the layout that the new text view will be added to
    ListView lv = (ListView) findViewById(R.id.courseList_Layout);

    // initialize the TextView that the class day and time will be displayed in, set the text, and
    // add it to the layout
    TextView textView = new TextView(this);
    textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT));
    textView.setBackgroundColor(getResources().getColor(android.R.color.white));
    textView.setTextSize(18);
    textView.setText(content);

    textView.setPadding(10, 10, 10, 10);
    textView.setLineSpacing(8, 1);
    lv.addView(textView);

  }

  @Override
  public boolean onNavigationItemSelected(int position, long id) {
    // TODO Auto-generated method stub
    return false;
  }

}
