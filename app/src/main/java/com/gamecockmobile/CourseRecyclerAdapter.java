package com.gamecockmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piedt on 4/18/15.
 */
public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {
    private ArrayList<Course> courses;
    private int itemLayout;

    public CourseRecyclerAdapter(ArrayList<Course> courses, int itemLayout) {
        this.courses = courses;
        this.itemLayout = itemLayout;

        System.out.println("Num of course = " + courses.size());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.title.setText(course.getDept() + " " + course.getNumber());
        holder.name.setText(course.getName());
        holder.times.setText(course.getSection() + " (" + course.getTime() + ")");
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView name;
        public TextView times;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.course_list_item_title);
            name = (TextView) itemView.findViewById(R.id.course_list_item_name);
            times = (TextView) itemView.findViewById(R.id.course_list_item_times);
        }
    }
}
