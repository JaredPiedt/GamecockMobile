package com.gamecockmobile.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import com.gamecockmobile.R;
import com.gamecockmobile.provider.ScheduleDatabase;
import com.gamecockmobile.stickylistheaders.StickyListHeadersAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

  private ArrayList<Event> mEvents;
  private LayoutInflater inflater;
  private EventDatabaseHandler db;
  private ScheduleDatabase mDB;
  private Context mContext;
  private int counter = 1;
  private HashMap<String, Integer> mColorIndex;
  private HashMap<Long, Integer> mHashMap;

  public EventsAdapter(Context context) {
    inflater = LayoutInflater.from(context);
    db = new EventDatabaseHandler(context);
    mDB = new ScheduleDatabase(context);
    mEvents = mDB.getAllEvents();
    mContext = context;

    mColorIndex = new HashMap<String, Integer>();
    mColorIndex.put("Test", 0);
    mColorIndex.put("Quiz", 1);
    mColorIndex.put("Homework", 2);
    mColorIndex.put("Other", 3);

    mHashMap = createHashMap();
  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return mEvents.size();
  }

  @Override
  public Object getItem(int position) {
    // TODO Auto-generated method stub
    return mEvents.get(position);
  }

  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
  }



  @SuppressWarnings("deprecation")
  @SuppressLint("NewApi")
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    String weekday;
    String monthDay;
    String time;
    Event event = mEvents.get(position);
    TypedArray colors = mContext.getResources().obtainTypedArray(R.array.event_backgrounds);
    //TypedArray colors = mContext.getResources().obtainTypedArray(R.array.event_colors);

    if (convertView == null) {

      holder = new ViewHolder();

      // inflate the view and initialize all of the view holder variables
      convertView = inflater.inflate(R.layout.event_list_item, parent, false);
      holder.weekday = (TextView) convertView.findViewById(R.id.event_weekday);
      holder.monthDay = (TextView) convertView.findViewById(R.id.event_monthDay);
      holder.title = (TextView) convertView.findViewById(R.id.event_title);
      holder.time = (TextView) convertView.findViewById(R.id.event_time);
      holder.courseName = (TextView) convertView.findViewById(R.id.event_course);
      holder.container = (LinearLayout) convertView.findViewById(R.id.event_details_container);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(event.getDate());

    if (mHashMap.get(event.getDate()) == position) {
      weekday = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
      monthDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
      time = event.getStartTimeAsString(mContext) + " - " + event.getEndTimeAsString(mContext);
      holder.weekday.setText(weekday);
      holder.monthDay.setText(monthDay);
      holder.title.setText(event.getName());
      holder.time.setText(time);
      holder.courseName.setText(event.getCourse());
    } else {
      weekday = "";
      monthDay = "";
      time = event.getStartTimeAsString(mContext) + " - " + event.getEndTimeAsString(mContext);
      holder.weekday.setText(weekday);
      holder.monthDay.setText(monthDay);
      holder.title.setText(event.getName());
      holder.time.setText(time);
      holder.courseName.setText(event.getCourse());
    }

    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
      holder.container.setBackgroundDrawable(colors.getDrawable(event.getType()));
    } else {
      holder.container.setBackground(colors.getDrawable(event.getType()));
      //holder.container.setBackgroundColor(colors.getColor(event.getType(), 0));
    }
    counter++;
    colors.recycle();

    return convertView;
  }

  @Override
  public View getHeaderView(int position, View convertView, ViewGroup parent) {
    HeaderViewHolder holder;

    if (convertView == null) {
      holder = new HeaderViewHolder();
      convertView = inflater.inflate(R.layout.header, parent, false);
      holder.text = (TextView) convertView.findViewById(R.id.text1);
      convertView.setTag(holder);
    } else {
      holder = (HeaderViewHolder) convertView.getTag();
    }

    // set header text as first char in name
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(mEvents.get(position).getDate());
    String headerText = "" + cal.getDisplayName(cal.MONTH, cal.LONG, Locale.US);

    if (headerText != "") {
      holder.text.setText(headerText);
    } else {
      holder.text.setText("1");
    }

    return convertView;
  }

  @Override
  public long getHeaderId(int position) {
    // TODO Auto-generated method stub
    //return mEvents.get(position).getDate();
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(mEvents.get(position).getDate());
   // Calendar returnCal = new GregorianCalendar();
   // returnCal.set(cal.YEAR, cal.MONTH, 1);
    return Long.valueOf(cal.get(Calendar.MONTH) + cal.get(Calendar.YEAR));
  }

  private HashMap<Long, Integer> createHashMap() {
    HashMap<Long, Integer> map = new HashMap<Long, Integer>();

    for (int i = 0; i < mEvents.size(); i++) {
      Event e = mEvents.get(i);
      long date = e.getDate();

      if (!map.containsKey(date)) {
        System.out.println("Map doesn't contain key" + date);
        map.put(date, i);
      } else {
        int position = map.get(date);

        if (e.compareTo(mEvents.get(position)) == 1) {
          map.remove(date);
          map.put(date, i);
        }
      }
    }

    return map;
  }

  public void updateResults() {
    mEvents = mDB.getAllEvents();
    mHashMap = createHashMap();
    notifyDataSetChanged();
  }

  class HeaderViewHolder {
    TextView text;
  }

  class ViewHolder {
    TextView weekday;
    TextView monthDay;
    TextView title;
    TextView time;
    TextView courseName;
    LinearLayout container;
  }
}
