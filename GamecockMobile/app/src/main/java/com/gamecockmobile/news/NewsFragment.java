package com.gamecockmobile.news;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gamecockmobile.R;

public class NewsFragment extends Fragment {
  NewsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.news_fragment, container, false);

      GridView mGridView = (GridView) view.findViewById(R.id.news_gridView);
    
      mAdapter = new NewsAdapter(getActivity());
      mGridView.setAdapter(mAdapter);
    
    return view;
  }
  
}
