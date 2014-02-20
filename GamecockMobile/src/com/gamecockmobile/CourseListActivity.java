package com.gamecockmobile;

import java.util.ArrayList;
import java.util.Scanner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseListActivity extends Activity implements OnClickListener {

  DatabaseHandler db;
  private ArrayList<Course> mCourses = new ArrayList<Course>();

  public static final String FILE_NAME = "courses";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_list);

    // getApplicationContext().deleteDatabase("CoursesManager.db");

    db = new DatabaseHandler(this);
    Log.d("db", Integer.toString(db.getCoursesCount()));
    ArrayList<Course> courses = db.getAllCourses();

    for (Course c : courses) {

      // Log.d("Insert", c.toString());

      // initialize the layout that the new text view will be added to
      LinearLayout layout = (LinearLayout) findViewById(R.id.courseList_Layout);

      // initialize the TextView that the class day and time will be displayed in, set the text, and
      // add it to the layout
      TextView textView = new TextView(this);
      textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
          LayoutParams.WRAP_CONTENT));
      textView.setTextSize(18);
      textView.setText(c.getID() + " " + c.getCourseName());
      // textView.append("\n" + tempClassTime.getStartTimeAsString(getApplicationContext()) + " - "
      // + tempClassTime.getEndTimeAsString(getApplicationContext()));
      textView.setPadding(10, 10, 10, 10);
      textView.setLineSpacing(8, 1);
      textView.setBackgroundColor(getResources().getColor(android.R.color.white));
      layout.addView(textView);

      textView.setId(c.getID());
      textView.setOnClickListener(this);

      // initialize the divider, set it's properties, and add it to the layout
      View divider = new View(this);
      divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 2));
      divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
      layout.addView(divider);

    }

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

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // check if the request code is same as what is passed here it is 2 and check that the Intent is
    // not equal to null so app doesn't crash when back button is pressed
    if (requestCode == 1 && data != null) {
      Course tempCourse = (Course) data.getParcelableExtra("course");

      Log.d("Adding", "course added: " + tempCourse.getCourseName());

      // initialize the layout that the new text view will be added to
      LinearLayout layout = (LinearLayout) findViewById(R.id.courseList_Layout);

      // initialize the TextView that the class day and time will be displayed in, set the text, and
      // add it to the layout
      TextView textView = new TextView(this);
      textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      textView.setTextSize(18);
      textView.setText(tempCourse.getCourseName());
      // textView.append("\n" + tempClassTime.getStartTimeAsString(getApplicationContext()) + " - "
      // + tempClassTime.getEndTimeAsString(getApplicationContext()));
      textView.setPadding(0, 10, 0, 10);
      textView.setLineSpacing(8, 1);
      layout.addView(textView);

      textView.setId(tempCourse.getID());

      // initialize the divider, set it's properties, and add it to the layout
      View divider = new View(this);
      divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 2));
      divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
      layout.addView(divider);

      DatabaseHandler db = new DatabaseHandler(this);

      Log.d("Insert:", "Inserting...");

      db.addCourse(tempCourse, getApplicationContext());

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
    LinearLayout layout = (LinearLayout) findViewById(R.id.courseList_Layout);

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
    layout.addView(textView);

  }

}
