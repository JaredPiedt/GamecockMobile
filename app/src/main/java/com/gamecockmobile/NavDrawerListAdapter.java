package com.gamecockmobile;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

  private Context context;
  private ArrayList<NavDrawerItem> navDrawerItems;
   
  public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
      this.context = context;
      this.navDrawerItems = navDrawerItems;
  }

  @Override
  public int getCount() {
      return navDrawerItems.size();
  }

  @Override
  public Object getItem(int position) {       
      return navDrawerItems.get(position);
  }

  @Override
  public long getItemId(int position) {
      return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
      LayoutInflater mInflater = (LayoutInflater)
              context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

      if(navDrawerItems.get(position).getTitle() == "selector"){
          System.out.println("Return separator");
          convertView = mInflater.inflate(R.layout.navdrawer_separator, null);
          return convertView;
      }

      if (convertView == null) {
              convertView = mInflater.inflate(R.layout.drawer_list_item, null);
      }
        
      
      TextView txtTitle = (TextView) convertView.findViewById(R.id.drawer_title);
      ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
//      TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
//        
//      imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
      txtTitle.setText(navDrawerItems.get(position).getTitle());
      icon.setImageDrawable(context.getResources().getDrawable(navDrawerItems.get(position).getResId()));
       
      // displaying count
      // check whether it set visible or not
//      if(navDrawerItems.get(position).getCounterVisibility()){
//          txtCount.setText(navDrawerItems.get(position).getCount());
//      }else{
//          // hide the counter view
//          txtCount.setVisibility(View.GONE);
//      }
       
      return convertView;
  }


}
