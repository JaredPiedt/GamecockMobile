package com.gamecockmobile;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseListAdapter extends ArrayAdapter<Course> {

  Context context;
  int layoutResourceId;
  List<Course> courses = null;
  DatabaseHandler db;
  private int mLastPosition = -1;

  public CourseListAdapter(Context context, int layoutResourceId, List<Course> courses) {
    super(context, layoutResourceId, courses);
    this.layoutResourceId = layoutResourceId;
    this.context = context;
    this.courses = courses;
    db = new DatabaseHandler(getContext());
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = convertView;
    CourseHolder holder = null;
    if (row == null) {
      LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
      row = inflater.inflate(layoutResourceId, parent, false);
      holder = new CourseHolder();
      holder.txtTitle = (TextView) row.findViewById(R.id.course_list_item_title);
      holder.txtTimes = (TextView) row.findViewById(R.id.course_list_item_times);

      row.setTag(holder);
    } else {
      holder = (CourseHolder) row.getTag();
    }

    Course course = courses.get(position);
    holder.txtTitle.setText(course.getCourseName());
    String s = "";

    for (ClassTime c : course.getClassTimes()) {
      s += c.toString() + ": " + c.getStartTimeAsString(getContext()) + " - "
          + c.getEndTimeAsString(getContext()) + "\n";
    }

    if(s != "")
    {
      holder.txtTimes.setText(s);
    }

    TranslateAnimation animation = null;
    animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 3.0f,
        Animation.RELATIVE_TO_SELF, 0.0f);
    animation.setDuration(600);
    animation.setStartOffset(position * 60);
    row.startAnimation(animation);
    mLastPosition = position;

    return row;
  }

  static class CourseHolder {
    TextView txtTitle;
    TextView txtTimes;
  }
}
