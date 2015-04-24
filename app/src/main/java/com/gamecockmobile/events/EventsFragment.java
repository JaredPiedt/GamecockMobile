package com.gamecockmobile.events;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.gamecockmobile.R;
import com.gamecockmobile.provider.ScheduleDatabase;
import com.gamecockmobile.stickylistheaders.StickyListHeadersListView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.AdapterView.OnItemClickListener;

/**
 * The 'EventsFragment' class is used for setting up the fragment when the 'Events' navigation
 * drawer is selected. It loads all of the events, organizing by date, into the frame.
 *
 * @author Jared W. Piedt
 */
public class EventsFragment extends Fragment implements OnNavigationListener, OnItemClickListener, AdapterView.OnItemLongClickListener {

    EventDatabaseHandler eDB;
    ScheduleDatabase mDB;

    FloatingActionButton mButtonAddEvent;
    TreeMap<Long, ArrayList<Event>> mTreeMap;
    ArrayList<Event> mEventsList;
    EventsAdapter mAdapter;

    private static final String EVENT_ID = "Event ID";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //actionBar.setTitle("Events");

        // must call this method in order for the fragment to add items to the action bar
        setHasOptionsMenu(true);

        // initialize the databases
        //eDB = new EventDatabaseHandler(getActivity());
        mDB = new ScheduleDatabase(getActivity());
        mTreeMap = new TreeMap<Long, ArrayList<Event>>();

        ActionBar ab = getActivity().getActionBar();

        //mEventsList = eDB.getAllEvents();
        mEventsList = mDB.getAllEvents();
        Event tempEvent;
        long tempDate;
        ArrayList<Event> v;
        // add the events to the 'HashMap' using the date as the key and the events as the value
        for (int i = 0; i < mEventsList.size(); i++) {
            tempEvent = mEventsList.get(i);
            tempDate = mEventsList.get(i).getDate();
            v = mTreeMap.get(tempDate);

            if (v == null) {
                v = new ArrayList<Event>();
                mTreeMap.put(tempDate, v);
            }
            v.add(tempEvent);
        }

        // test method to iterate over the 'TreeMap'
        System.out.println("******Test TreeMap*******");
        for (Map.Entry<Long, ArrayList<Event>> entry : mTreeMap.entrySet()) {
            Long key = entry.getKey();
            ArrayList<Event> events = entry.getValue();
            System.out.println("Key = " + key);
            for (int i = 0; i < events.size(); i++) {
                System.out.println("Values = " + events.get(i).toString() + "\n");
            }
        }

        View view = inflater.inflate(R.layout.events_fragment, container, false);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StickyListHeadersListView stickyList = (StickyListHeadersListView) getActivity().findViewById(
                R.id.list);
        mAdapter = new EventsAdapter(getActivity());
        stickyList.setAdapter(mAdapter);
        stickyList.setOnItemClickListener(this);
        stickyList.setOnItemLongClickListener(this);

        mButtonAddEvent = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_event);
        mButtonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivityForResult(intent, 1);
            }
        });
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
            if (mAdapter != null) {
                mAdapter.updateResults();
            }
            //System.out.println(eDB.getEvent(1).toString());
        }
    }

    @Override
    public boolean onNavigationItemSelected(int arg0, long arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        Event e = (Event) mAdapter.getItem(position);
        System.out.println("***Click: " + e.getName());

        if (e != null) {
            Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
            intent.putExtra(EVENT_ID, e.getId());
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, final long id) {
//        final CharSequence[] mDialogList = {"Delete", "Edit"};
//        final Event event = (Event) mAdapter.getItem(position);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle(event.getName());
//        builder.setItems(mDialogList, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                if (which == 0) {
//                    mDB.deleteEvent(event);
//                    // reset the list of events
//                    if (mAdapter != null) {
//                        mAdapter.updateResults();
//                    }
//                } else if (which == 1) {
//                    // the "Edit" option was chosen
////                    Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
////                    intent.putExtra(EVENT_ID, event.getId());
////                    startActivityForResult(intent, 1);
//                }
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();

        return true;
    }
}
