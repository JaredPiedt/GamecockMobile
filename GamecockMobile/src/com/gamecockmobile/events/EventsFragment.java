package com.gamecockmobile.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.gamecockmobile.AddCourseActivity;
import com.gamecockmobile.Course;
import com.gamecockmobile.DatabaseHandler;
import com.gamecockmobile.R;
import com.gamecockmobile.stickylistheaders.StickyListHeadersListView;

import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar;
import android.app.Fragment;
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

/**
 * The 'EventsFragment' class is used for setting up the fragment when the 'Events' navigation
 * drawer is selected. It loads all of the events, organizing by date, into the frame.
 * 
 * @author Jared W. Piedt
 * 
 */
public class EventsFragment extends Fragment implements OnNavigationListener {

  DatabaseHandler db;
  EventDatabaseHandler eDB;
  TreeMap<Long, ArrayList<Event>> mTreeMap;
  ArrayList<Event> mEventsList;

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    ActionBar actionBar = getActivity().getActionBar();
    actionBar.setTitle("Events");

    // must call this method in order for the fragment to add items to the action bar
    setHasOptionsMenu(true);

    // initialize the databases
    eDB = new EventDatabaseHandler(getActivity());
    mTreeMap = new TreeMap<Long, ArrayList<Event>>();

    mEventsList = eDB.getAllEvents();
    Event tempEvent;
    long tempDate;
    ArrayList<Event> v;
    // add the events to the 'HashMap' using the date as the key and the events as the value
    for (int i = 0; i < mEventsList.size(); i++) {
      tempEvent = mEventsList.get(i);
      tempDate = mEventsList.get(i).getDate();
      v = mTreeMap.get(tempDate);
      
      if(v == null) {
        v = new ArrayList<Event>();
        mTreeMap.put(tempDate, v);
      }
      v.add(tempEvent);
    }

    // test method to iterate over the 'TreeMap'
    System.out.println("******Test TreeMap*******");
    for(Map.Entry<Long, ArrayList<Event>> entry: mTreeMap.entrySet()) {
      Long key = entry.getKey();
      ArrayList<Event> events = entry.getValue();
      System.out.println("Key = " + key);
      for(int i = 0; i < events.size(); i++){
        System.out.println("Values = " + events.get(i).toString() + "\n");
      }
    }
    
    View view = inflater.inflate(R.layout.events_fragment, container, false);    

    return view;
  }
  
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    StickyListHeadersListView stickyList = (StickyListHeadersListView) getActivity().findViewById(R.id.list);
    EventsAdapter adapter = new EventsAdapter(getActivity());
    stickyList.setAdapter(adapter);
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

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == 1) {

      System.out.println(eDB.getEvent(1).toString());
    }
  }

  @Override
  public boolean onNavigationItemSelected(int arg0, long arg1) {
    // TODO Auto-generated method stub
    return false;
  }
  
  

}
