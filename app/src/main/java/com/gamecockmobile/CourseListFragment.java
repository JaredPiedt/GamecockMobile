package com.gamecockmobile;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gamecockmobile.provider.ScheduleDatabase;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class CourseListFragment extends Fragment implements AdapterView.OnItemClickListener, OnNavigationListener,
    OnItemLongClickListener {

    FloatingActionButton mButtonAddCourse;
    ListView mCourseListLayout;
    //ArrayAdapter<String> mAdapter;
    CourseListAdapter mAdapter;

    ScheduleDatabase mDB;

    RecyclerView mRecyclerView;

    private static final String MYCOURSE_ID = "course_id";

 private ArrayList<String> mCourses = new ArrayList<String>();

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      System.out.println("On create view");
      ActionBar actionBar = getActivity().getActionBar();

      View view = inflater.inflate(R.layout.courses_fragment, container, false);

      mDB = new ScheduleDatabase(getActivity());

//    // must call this method in order for the fragment to add items to the action bar
//    setHasOptionsMenu(true);
//
//    db = new DatabaseHandler(getActivity());

      ArrayList<Course> courses = mDB.getMyCourses();
      //mAdapter = new CourseRecyclerAdapter(getActivity(), courses, R.layout.course_list_item);
      mAdapter = new CourseListAdapter(getActivity());
//
//    ArrayList<Course> courses = db.getAllCourses();
//    ArrayList<String> courseList = new ArrayList<String>();
//
//    Course tempCourse = null;
//
//    for (int i = 0; i < courses.size(); i++) {
//      tempCourse = courses.get(i);
//      courseList.add(tempCourse.getCourseName());
//    }
//
//    mAdapter = new CourseListAdapter(getActivity(), R.layout.course_list_item,
//        courses);
//    setListAdapter(mAdapter);

    //return super.onCreateView(inflater, container, savedInstanceState);
      return view;
  }

  /**
   * You must use this method in order to set the onItemLongClickListener.
   */
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

      mButtonAddCourse = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_course);
      mButtonAddCourse.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getActivity(), AddCourseActivity.class);
              startActivityForResult(intent, 1);
          }
      });

      mCourseListLayout = (ListView) getActivity().findViewById(R.id.layout_courseList);
      mCourseListLayout.setAdapter(mAdapter);
      mCourseListLayout.setOnItemClickListener(this);
//      mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.course_recycler_view);
//      mRecyclerView.setHasFixedSize(true);
//      mRecyclerView.setAdapter(mAdapter);
//      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//      mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    // call the ListView to set the long click listener for courses
//    getListView().setOnItemLongClickListener(this);
//    getListView().setDivider(null);
//    getListView().setBackgroundColor(getResources().getColor(R.color.gray_background));
  }

  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.course_list, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

//  public void onListItemClick(ListView l, View v, int position, long id) {
//    Intent intent = new Intent(getActivity(), AddCourseActivity.class);
//    Course c = db.getCourse(position + 1);
//
//    intent.putExtra("course", c);
//    startActivityForResult(intent, 1);
//  }

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

      System.out.println("Enter on activity result. requestCode = " + requestCode +  "  resultCode = " + resultCode);
    // check if the request code is same as what is passed here it is 2 and check that the Intent is
    // not equal to null so app doesn't crash when back button is pressed
    if (requestCode == 1 && data != null) {
      if (mAdapter != null) {
        mAdapter.updateResults();
      }
    }
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
    // TODO Auto-generated method stub
//    final Course course = db.getCourse(position + 1);
//    final CharSequence[] mDialogList = { "Delete", "Edit" };
//
//    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//    builder.setTitle(course.getCourseName());
//    builder.setItems(mDialogList, new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialog, int which) {
//        if (which == 0) {
//          db.deleteCourse(course);
//          // reset the list of courses
//          updateAdapter();
//        } else if (which == 1) {
//          // the edit option was chosen
//          Intent intent = new Intent(getActivity(), AddCourseActivity.class);
//          intent.putExtra("course", course);
//          startActivityForResult(intent, 1);
//        }
//      }
//    });
//    AlertDialog dialog = builder.create();
//    dialog.show();

    return true;
  }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Item clicked");
        Course course = (Course) mAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), CourseDetailsActivity.class);
        intent.putExtra(MYCOURSE_ID, course.getId());
        startActivityForResult(intent, 1);
    }
}
