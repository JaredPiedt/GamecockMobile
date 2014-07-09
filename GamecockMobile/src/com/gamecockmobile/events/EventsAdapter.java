package com.gamecockmobile.events;

import java.util.ArrayList;

import com.gamecockmobile.R;
import com.gamecockmobile.stickylistheaders.StickyListHeadersAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

  private ArrayList<Event> mEvents;
  private LayoutInflater inflater;
  private EventDatabaseHandler db;

  public EventsAdapter(Context context) {
    inflater = LayoutInflater.from(context);
    db = new EventDatabaseHandler(context);
    mEvents = db.getAllEvents();
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

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;

    if (convertView == null) {
      holder = new ViewHolder();
      convertView = inflater.inflate(R.layout.test_list_item_layout, parent, false);
      holder.text = (TextView) convertView.findViewById(R.id.text);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.text.setText(mEvents.get(position).getName());

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
    TextView text;
  }
}
