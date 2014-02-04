package com.gamecockmobile;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddCourse extends Activity implements OnClickListener {

  TextView mAddDayAndTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_course);

    final LayoutInflater inflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View customActionBarView = inflater.inflate(R.layout.action_bar_add_course, null);
    customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // "Done"
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

    mAddDayAndTime = (TextView) findViewById(R.id.addNewTime_button);

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
      Intent intent = new Intent(this, AddDayAndTime.class);
      startActivityForResult(intent, 2);
      break;

    default:
      break;
    }
  }

  /**
   * This method does the work for adding a new class time to the 'AddCourse' activity
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // check if the request code is same as what is passed here it is 2 and check that the Intent is
    // not equal to null so app doesn't crash when back button is pressed
    if (requestCode == 2 && data != null) {
      
      ClassTime tempClassTime;
      String tempDays = data.getStringExtra("Days");
      String tempStartTime = data.getStringExtra("StartTime");
      String tempEndTime = data.getStringExtra("EndTime");
      
      tempClassTime = new ClassTime(tempDays, tempStartTime, tempEndTime);

      // initialize the layout that the new text view will be added to
      LinearLayout layout = (LinearLayout) findViewById(R.id.addCourse_layout);

      // initialize the divider, set it's properties, and add it to the layout
      View divider = new View(this);
      divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 2));
      divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
      layout.addView(divider);

      // initialize the TextView that the class day and time will be displayed in, set the text, and
      // add it to the layout
      TextView textView = new TextView(this);
      textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      textView.setTextSize(18);
      textView.setText(data.getStringExtra("Days"));
      textView.append("\n" + data.getStringExtra("StartTime") + " - "
          + data.getStringExtra("EndTime"));
      textView.setPadding(0, 10, 0, 10);
      textView.setLineSpacing(8, 1);
      layout.addView(textView);
    }

  }

}
