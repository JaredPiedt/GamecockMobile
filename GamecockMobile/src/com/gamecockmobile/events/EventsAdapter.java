package com.gamecockmobile.events;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import com.gamecockmobile.R;
import com.gamecockmobile.stickylistheaders.StickyListHeadersAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

  private ArrayList<Event> mEvents;
  private LayoutInflater inflater;
  private EventDatabaseHandler db;
  private Context mContext;
  private int counter = 1;
  private HashMap<String, Integer> mColorIndex;

  public EventsAdapter(Context context) {
    inflater = LayoutInflater.from(context);
    db = new EventDatabaseHandler(context);
    mEvents = db.getAllEvents();
    mContext = context;

    mColorIndex = new HashMap<String, Integer>();
    mColorIndex.put("Test", 0);
    mColorIndex.put("Quiz", 1);
    mColorIndex.put("Homework", 2);
    mColorIndex.put("Other", 3);
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
    int counter = 0;
    TypedArray colors = mContext.getResources().obtainTypedArray(R.array.event_backgrounds);

    if (convertView == null) {
      holder = new ViewHolder();
      convertView = inflater.inflate(R.layout.event_list_item, parent, false);
      holder.weekday = (TextView) convertView.findViewById(R.id.event_weekday);
      holder.monthDay = (TextView) convertView.findViewById(R.id.event_monthDay);
      holder.time = (TextView) convertView.findViewById(R.id.event_time);
      holder.title = (TextView) convertView.findViewById(R.id.event_title);
      holder.courseName = (TextView) convertView.findViewById(R.id.event_course);
      holder.container = (LinearLayout) convertView.findViewById(R.id.event_details_container);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(event.getDate());
    weekday = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
    monthDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
    System.out.println(monthDay);
    holder.weekday.setText(weekday);
    holder.monthDay.setText(monthDay);
    holder.time.setText("8:30 - 9:45");
    holder.title.setText(event.getName());
    holder.courseName.setText(event.getCourse());

    System.out.println("counter: " + counter % 4);

    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
      holder.container.setBackgroundDrawable(colors.getDrawable(event.getType()));
    } else {
      holder.container.setBackground(colors.getDrawable(event.getType()));
    }
    counter++;
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
    String headerText = "" + String.valueOf(mEvents.get(position).getDate());
    System.out.println(headerText);
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
    return mEvents.get(position).getDate();
  }

  class HeaderViewHolder {
    TextView text;
  }

  class ViewHolder {
    TextView weekday;
    TextView monthDay;
    TextView time;
    TextView title;
    TextView courseName;
    LinearLayout container;
  }
}
