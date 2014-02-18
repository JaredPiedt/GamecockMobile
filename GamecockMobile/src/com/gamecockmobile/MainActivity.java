package com.gamecockmobile;

import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * This is the main class for the app thats runs the dashboard for all of the resources.
 */
public class MainActivity extends Activity implements OnItemClickListener {

  GridView mGridView;
  static final String[] ICON_DATA = new String[] { "Schedule" };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    getApplicationContext().deleteFile("courses");

    mGridView = (GridView) findViewById(R.id.home_gridview);
    mGridView.setAdapter(new GridItemAdapter(this, ICON_DATA));
    mGridView.setOnItemClickListener(this);

    mGridView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // TODO Auto-generated method stub
        switch (position) {
        case 0:
          Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
          startActivity(intent);
          break;
        default:
          break;
        }

      }

    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    // TODO Auto-generated method stub

  }

}
