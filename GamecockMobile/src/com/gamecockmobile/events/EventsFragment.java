package com.gamecockmobile.events;

import java.util.ArrayList;

import com.gamecockmobile.AddCourseActivity;
import com.gamecockmobile.Course;
import com.gamecockmobile.DatabaseHandler;
import com.gamecockmobile.R;

import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class EventsFragment extends ListFragment implements OnNavigationListener {

  DatabaseHandler db;
  
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    ActionBar actionBar = getActivity().getActionBar();

    // must call this method in order for the fragment to add items to the action bar
    setHasOptionsMenu(true);

    db = new DatabaseHandler(getActivity());

    ArrayList<Course> courses = db.getAllCourses();
    ArrayList<String> courseList = new ArrayList<String>();

    Course tempCourse = null;

    for (int i = 0; i < courses.size(); i++) {
      tempCourse = courses.get(i);
      courseList.add(tempCourse.getCourseName());
    }

   return super.onCreateView(inflater, container, savedInstanceState);
  }
  
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.course_list, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }
  
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.action_new:
      Intent intent = new Intent(getActivity(), AddEventActivity.class);
      startActivityForResult(intent, 1);
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }
  
  @Override
  public boolean onNavigationItemSelected(int arg0, long arg1) {
    // TODO Auto-generated method stub
    return false;
  }

}
