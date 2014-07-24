package com.gamecockmobile.buses;

import com.gamecockmobile.R;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BusesFragment extends ListFragment {
  
  ArrayAdapter<String> mAdapter;
  String[] mBusNames = new String[]{"Yellow Bus", "Red Bus"};

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mAdapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,
        mBusNames);
    setListAdapter(mAdapter);
    return super.onCreateView(inflater, container, savedInstanceState);

  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.course_list, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }


}
