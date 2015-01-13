package com.gamecockmobile.news;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gamecockmobile.R;
import com.gamecockmobile.events.Event;
import com.gamecockmobile.events.EventDetailsActivity;
import com.gamecockmobile.util.LogUtils;

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {
  NewsAdapter mAdapter;

    private static final String PHOTO_URL = "photo_url";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String ARTICLE_URL = "article_url";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.news_fragment, container, false);

      GridView mGridView = (GridView) view.findViewById(R.id.news_gridView);
    
      mAdapter = new NewsAdapter(getActivity());
      mGridView.setAdapter(mAdapter);
      mGridView.setOnItemClickListener(this);
    
    return view;
  }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // TODO Auto-generated method stub
        FeedMessage message = (FeedMessage) mAdapter.getItem(position);
        System.out.println("***Click: " + message.getTitle());

        if(message != null){
            Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
            intent.putExtra(PHOTO_URL, parseURL(message.getDescription()));
            intent.putExtra(TITLE, message.getTitle());
            intent.putExtra(AUTHOR, message.getAuthor());
            intent.putExtra(ARTICLE_URL, message.getGuid());

            System.out.println("Url is " +  message.getGuid());
            startActivity(intent);
        }

    }

    private String parseURL(String description) {
        int start = description.indexOf('"');
        int end = description.indexOf('"', start + 1);

        if (start == -1 || end == -1) {
            return null;
        } else {
            return description.substring(start + 1, end);
        }
    }
}
