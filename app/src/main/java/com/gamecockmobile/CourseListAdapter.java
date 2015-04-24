package com.gamecockmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gamecockmobile.provider.ScheduleDatabase;

import java.util.ArrayList;

/**
 * Created by piedt on 4/23/15.
 */
public class CourseListAdapter extends BaseAdapter {

    private ArrayList<Course> mCourses;
    private LayoutInflater mInflater;
    private ScheduleDatabase mDB;
    private Context mContext;

    public CourseListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDB = new ScheduleDatabase(context);
        mCourses = mDB.getMyCourses();

        System.out.print(getCount());
    }

    @Override
    public int getCount() {
        return mCourses.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = mCourses.get(position);
        System.out.println("Starting getView " + convertView);
        ViewHolder holder;
        if(convertView == null) {
            System.out.println("Setting convertView");
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.course_list_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.course_list_item_title);
            holder.name = (TextView) convertView.findViewById(R.id.course_list_item_name);
            holder.times = (TextView) convertView.findViewById(R.id.course_list_item_times);

            holder.title.setText(course.getDept() + " " + course.getNumber());
            holder.name.setText(course.getName());
            holder.times.setText(course.getTime());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public void updateResults() {
        mCourses = mDB.getMyCourses();
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView title;
        public TextView name;
        public TextView times;
    }
}
