package com.gamecockmobile;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddCourseActivity extends Activity implements OnClickListener, OnLongClickListener {

  TextView mAddDayAndTime;
  EditText mCourseNameEditText;

  Bundle mBundle;
  DatabaseHandler db;

  private Course mCourse;
  private int counter = 1;

  public static final String FILE_NAME = "courses";
  boolean mUpdateCourse;

  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_course);

    overridePendingTransition(R.animator.slide_left_in, R.animator.hold);

    db = new DatabaseHandler(this);
    mUpdateCourse = false;

    final LayoutInflater inflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View customActionBarView = inflater.inflate(R.layout.action_bar_add_course, null);
    customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // "Done"
            System.out.println("*****EDIT TEXT*****" + mCourseNameEditText.getText().toString());

            if (mCourseNameEditText.getText().toString().trim().length() == 0) {
              System.out.println("Edit text was blank");
              createBlankNameAlertDialog();
            } else {
              Intent intent = new Intent();
              mCourse.setCourseName(mCourseNameEditText.getText().toString());
              // mCourse.setClassTimes(mClassTimes);

              System.out.println(mUpdateCourse);

              if (mUpdateCourse == true) {
                db.updateCourse(mCourse, getApplicationContext());
                System.out.println("Course was updated: " + mCourse.getCourseName());
              } else {
                db.addCourse(mCourse, getApplicationContext());
                System.out.println("Course added to database: " + mCourse.getCourseName());
              }

              intent.putExtra("course", mCourse);

              setResult(1, intent);

              Log.d("Result", "result was set");
              // writeCourseToFile();
              finish();
              overridePendingTransition(R.animator.hold, R.animator.slide_right_out);
            }
          }
        });
    customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // "Cancel"
            finish();
            overridePendingTransition(R.animator.hold, R.animator.slide_right_out);

          }
        });

    // create a custom layout for the action bar so that it has a save and a cancel button
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
        | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    // initialize all views
    mAddDayAndTime = (TextView) findViewById(R.id.addNewTime_button);
    mCourseNameEditText = (EditText) findViewById(R.id.courseName_editText);

    // initialize variables
    mCourse = new Course();
    mBundle = getIntent().getExtras();

    // check to make sure if the bundle contains something which means the course has already been
    // added to the database and the user wants to edit it
    if (mBundle != null) {
      mUpdateCourse = true;
      Course tempCourse = mBundle.getParcelable("course");
      mCourse.setID(tempCourse.getID());
      mCourse.setCourseName(tempCourse.getCourseName());
      mCourse.setClassTimes(tempCourse.getClassTimes());
      
      // increment counter to make sure ids don't overlap
      counter += tempCourse.getClassTimes().size();
      
      System.out.println("Bundle: " + mCourse.getCourseName());

      mCourseNameEditText.setText(tempCourse.getCourseName());

      for (ClassTime ct : mCourse.getClassTimes()) {
        // initialize the layout that the new text view will be added to
        LinearLayout layout = (LinearLayout) findViewById(R.id.addCourse_layout);

        // initialize the TextView that the class day and time will be displayed in, set the text,
        // and add it to the layout
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT));
        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.course_list_item_selector));
        textView.setTextSize(18);

        // set up the display string for days of the week
        String dayStr = "";
        ArrayList<CharSequence> days = ct.getDays();
        for (int i = 0; i < days.size(); i++) {
          if (i == days.size() - 1) {
            dayStr += days.get(i);
          } else {
            dayStr += days.get(i) + ", ";
          }
        }
        textView.setText(dayStr);
        textView.append("\n" + ct.getStartTimeAsString(getApplicationContext()) + " - "
            + ct.getEndTimeAsString(getApplicationContext()));
        textView.setPadding(0, 10, 0, 10);
        textView.setLineSpacing(8, 1);
        textView.setId(ct.getID());
        System.out.println(ct.getID());
        textView.setOnLongClickListener(this);

        layout.addView(textView);

        // initialize the divider, set it's properties, and add it to the layout
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 2));
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        layout.addView(divider);
      }
    }

    mAddDayAndTime.setOnClickListener(this);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.add_course, menu);
    return true;
  }

  /**
   * 
   */
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.addNewTime_button:
      Intent intent = new Intent(this, AddDayAndTimeActivity.class);
      startActivityForResult(intent, 2);
      break;

    default:
      break;
    }
  }

  /**
   * This method does the work for adding a new class time to the 'AddCourse' activity
   */
  @SuppressWarnings("deprecation")
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // check if the request code is same as what is passed here it is 2 and check that the Intent is
    // not equal to null so app doesn't crash when back button is pressed
    if (requestCode == 2 && data != null) {

      ClassTime tempClassTime = (ClassTime) data.getParcelableExtra("classTime");
      mCourse.addClassTime(tempClassTime);

      // initialize the layout that the new text view will be added to
      LinearLayout layout = (LinearLayout) findViewById(R.id.addCourse_layout);

      // initialize the TextView that the class day and time will be displayed in, set the text, and
      // add it to the layout
      TextView textView = new TextView(this);
      textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
          LayoutParams.WRAP_CONTENT));
      textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.course_list_item_selector));
      textView.setTextSize(18);

      // set up the display string for days of the week
      String dayStr = "";
      ArrayList<CharSequence> days = tempClassTime.getDays();
      for (int i = 0; i < days.size(); i++) {
        if (i == days.size() - 1) {
          dayStr += days.get(i);
        } else {
          dayStr += days.get(i) + ", ";
        }
      }

      textView.setText(dayStr);
      textView.append("\n" + tempClassTime.getStartTimeAsString(getApplicationContext()) + " - "
          + tempClassTime.getEndTimeAsString(getApplicationContext()));
      textView.setPadding(0, 10, 0, 10);
      textView.setLineSpacing(8, 1);
      textView.setId(counter);
      System.out.println(textView.getId());

      tempClassTime.setID(counter);
      textView.setOnLongClickListener(this);
      System.out.println(tempClassTime.getID());
      counter++;
      layout.addView(textView);

      // initialize the divider, set it's properties, and add it to the layout
      View divider = new View(this);
      divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 2));
      divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
      layout.addView(divider);

    }

  }

  @Override
  public boolean onLongClick(final View v) {
    // TODO Auto-generated method stub
    System.out.println(v.getId());
    ArrayList<ClassTime> cTs = mCourse.getClassTimes();

    for (final ClassTime cT : cTs) {
      if (v.getId() == cT.getID()) {
        final CharSequence[] mDialogList = { "Delete" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(cT.getStartTimeAsString(getApplicationContext()) + " - "
            + cT.getEndTimeAsString(getApplicationContext()));
        builder.setItems(mDialogList, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
              mCourse.removeClassTime(cT.getID());
              db.updateCourse(mCourse, getApplicationContext());
              LinearLayout layout = (LinearLayout) findViewById(R.id.addCourse_layout);
              layout.removeView(v);
            }
          }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
      }
    }
    return false;
  }

  private void createBlankNameAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.dialog_empty_name);
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
