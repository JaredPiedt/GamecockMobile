package com.gamecockmobile;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CourseListFragment extends ListFragment implements OnNavigationListener,
    OnItemLongClickListener {
  
  DatabaseHandler db;
  ArrayAdapter<String> mAdapter;

 private ArrayList<String> mCourses = new ArrayList<String>();

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

    mAdapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,
        courseList);
    setListAdapter(mAdapter);

    return super.onCreateView(inflater, container, savedInstanceState);
  }

  /**
   * You must use this method in order to set the onItemLongClickListener.
   */
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // call the ListView to set the long click listener for courses
    getListView().setOnItemLongClickListener(this);
  }

  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.course_list, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  public void onListItemClick(ListView l, View v, int position, long id) {
    Intent intent = new Intent(getActivity(), AddCourseActivity.class);
    Course c = db.getCourse(position + 1);

    intent.putExtra("course", c);
    startActivityForResult(intent, 1);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.action_new:
      Intent intent = new Intent(getActivity(), AddCourseActivity.class);
      startActivityForResult(intent, 1);
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    // TODO Auto-generated method stub
    return false;
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // check if the request code is same as what is passed here it is 2 and check that the Intent is
    // not equal to null so app doesn't crash when back button is pressed
    if (requestCode == 1 && data != null) {

      if (mAdapter != null) {
        updateAdapter();
      }
    }
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
    // TODO Auto-generated method stub
    final Course course = db.getCourse(position + 1);
    final CharSequence[] mDialogList = { "Delete", "Edit" };

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    builder.setTitle(course.getCourseName());
    builder.setItems(mDialogList, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        if (which == 0) {
          db.deleteCourse(course);
          // reset the list of courses
          updateAdapter();
        } else if (which == 1) {
          // the edit option was chosen
          Intent intent = new Intent(getActivity(), AddCourseActivity.class);
          intent.putExtra("course", course);
          startActivityForResult(intent, 1);
        }
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();

    return true;
  }

  /**
   * Method to update the adapter.
   * 
   * Called whenever a course is added or deleted. Clear the adapter and then loop through the
   * database of courses, adding the course names to the adapter and then notify the adapter that
   * the data set was changed.
   */
  public void updateAdapter() {
    mAdapter.clear();

    int count = db.getCoursesCount();

    for (int i = 1; i <= count; i++) {
      String name = db.getCourse(i).getCourseName();
      System.out.println(name);
      mAdapter.add(name);
    }

    mAdapter.notifyDataSetChanged();
  }
}
