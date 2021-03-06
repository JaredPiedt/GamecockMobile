package com.gamecockmobile.news;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class DrawableManager {
  private final Map<String, Drawable> drawableMap;
  
  public DrawableManager(){
    drawableMap = new HashMap<String, Drawable>();
  }
  
  public Drawable fetchDrawable(String urlString){
    if(drawableMap.containsKey(urlString)){
      return drawableMap.get(urlString);
    }
    
    try{
      InputStream is = fetch(urlString);
      Drawable drawable = Drawable.createFromStream(is, "src");
      
      if(drawable != null){
        drawableMap.put(urlString, drawable);
      }  else {
        System.out.println("Could not get thumbnail");;
      }
      return drawable;
    }catch(MalformedURLException e){
      return null;
    }catch(IOException e){
      return null;
    }
  }
  
  public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
    if (drawableMap.containsKey(urlString)) {
        imageView.setImageDrawable(drawableMap.get(urlString));
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            imageView.setImageDrawable((Drawable) message.obj);
        }
    };

    Thread thread = new Thread() {
        @Override
        public void run() {
            //TODO : set imageView to a "pending" image
            Drawable drawable = fetchDrawable(urlString);
            Message message = handler.obtainMessage(1, drawable);
            handler.sendMessage(message);
        }
    };
    thread.start();
}

  
  private InputStream fetch(String urlString) throws MalformedURLException, IOException {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    System.out.println(urlString);
    HttpGet request = new HttpGet(urlString);
    HttpResponse response = httpClient.execute(request);
    return response.getEntity().getContent();
    
  }
}
