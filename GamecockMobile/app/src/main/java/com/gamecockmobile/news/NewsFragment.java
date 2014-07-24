package com.gamecockmobile.news;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends ListFragment {
  NewsAdapter mAdapter;
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
    mAdapter = new NewsAdapter(getActivity());
    setListAdapter(mAdapter);
    
    return super.onCreateView(inflater, container, savedInstanceState);
  }
  
}
