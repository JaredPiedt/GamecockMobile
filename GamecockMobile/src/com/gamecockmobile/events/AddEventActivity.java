package com.gamecockmobile.events;

import com.gamecockmobile.R;
import com.gamecockmobile.R.layout;
import com.gamecockmobile.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddEventActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_event);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.add_event, menu);
    return true;
  }

}
