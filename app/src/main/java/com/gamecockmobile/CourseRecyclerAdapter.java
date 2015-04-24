package com.gamecockmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
    static Activity mActivity;
    static Fragment mFragment;
    private static final String MYCOURSE_ID = "course_id";

    public CourseRecyclerAdapter(Activity activity, ArrayList<Course> courses, int itemLayout) {
        mActivity = activity;
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

    public void updateAdapter(ArrayList<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        courses.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, courses.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView name;
        public TextView times;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.course_list_item_title);
            name = (TextView) itemView.findViewById(R.id.course_list_item_name);
            times = (TextView) itemView.findViewById(R.id.course_list_item_times);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Course course = courses.get(getAdapterPosition());
            int id = course.getId();
            System.out.println(id);
            System.out.println(mActivity);
            Intent intent = new Intent(mActivity, CourseDetailsActivity.class);
            intent.putExtra(MYCOURSE_ID, id);
            intent.putExtra("position", getAdapterPosition());
            mActivity.startActivityForResult(intent, 2);
        }
    }
}
