package com.gamecockmobile.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.bumptech.glide.Glide;
import com.gamecockmobile.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {

  private List<FeedMessage> mFeed;
  private Context mContext;
  private LayoutInflater mInflater;
  static final String mURL = "http://www.dailygamecock.com/dart/feed/top-stories.xml";
  private Bitmap mBitmap;
  private DrawableManager mDrawableManager;
  private LruCache<String, Bitmap> mMemoryCache;

  public NewsAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
    mDrawableManager = new DrawableManager();

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;
    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
      @SuppressLint("NewApi")
      @Override
      protected int sizeOf(String key, Bitmap bitmap) {
        return bitmap.getByteCount() / 1024;
      }
    };

    System.out.println("Starting async task");

    try {
      mFeed = new RetrieveFeedTask().execute(mURL).get();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println(mFeed.size());
  }

  @Override
  public int getCount() {
    return mFeed.size();
  }

  @Override
  public Object getItem(int position) {
    return mFeed.get(position);
  }

  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    Bitmap bitmap = null;
    String imageUrl;
    String title;
    String author;
    FeedMessage message = mFeed.get(position);

    if (convertView == null) {
      holder = new ViewHolder();

      convertView = mInflater.inflate(R.layout.list_item_news, parent, false);
      holder.image = (ImageView) convertView.findViewById(R.id.news_photo);
      holder.title = (TextView) convertView.findViewById(R.id.news_title);
      holder.author = (TextView) convertView.findViewById(R.id.news_author);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    String url = parseURL(message.description);
    if (url != null) {
        holder.image.setColorFilter(setColorAlpha(Color.BLACK, 0.4f));
      Glide.load(url)
              .centerCrop()
              .placeholder(R.drawable.gray_background)
              .animate(R.animator.image_fade_in)
              .into(holder.image);
    } else {
        holder.image.setColorFilter(setColorAlpha(Color.BLACK, 0.0f));
        holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.garnet_background));
    }
    holder.title.setText(message.title.trim());
    holder.author.setText(message.author.trim());

    return convertView;
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

    public static int setColorAlpha(int color, float alpha) {
        int alpha_int = Math.min(Math.max((int)(alpha * 255.0f), 0), 255);
        return Color.argb(alpha_int, Color.red(color), Color.green(color), Color.blue(color));
    }

//  private InputStream openHttpConnection(String urlString) throws IOException {
//    InputStream in = null;
//    int response = -1;
//
//    URL url = new URL(urlString);
//    URLConnection conn = url.openConnection();
//
//    if (!(conn instanceof HttpURLConnection)) {
//      throw new IOException("Not an HTTP connection");
//    }
//
//    try {
//      HttpURLConnection httpConn = (HttpURLConnection) conn;
//      httpConn.setAllowUserInteraction(false);
//      httpConn.setInstanceFollowRedirects(true);
//      httpConn.setRequestMethod("GET");
//      httpConn.connect();
//      response = httpConn.getResponseCode();
//      if (response == HttpURLConnection.HTTP_OK) {
//        in = httpConn.getInputStream();
//      }
//    } catch (Exception ex) {
//      throw new IOException("Error connecting");
//    }
//
//    return in;
//  }

//  private Bitmap downloadImage(String URL) {
//    Bitmap bitmap = null;
//    InputStream in = null;
//
//    try {
//      in = openHttpConnection(URL);
//      bitmap = BitmapFactory.decodeStream(in);
//      in.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    return bitmap;
//  }

//  public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//    if (getBitmapFromMemCache(key) == null) {
//      mMemoryCache.put(key, bitmap);
//    }
//  }

//  public Bitmap getBitmapFromMemCache(String key) {
//    return mMemoryCache.get(key);
//  }
//
//  public void loadBitmap(String url, ImageView imageView) {
//    final String imageURL = String.valueOf(url);
//
//    final Bitmap bitmap = getBitmapFromMemCache(imageURL);
//
//    if (bitmap != null) {
//      imageView.setImageBitmap(bitmap);
//    } else {
//      if (url != null) {
//        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
//        task.execute(url);
//      }
//    }
//  }

//  public Bitmap decodeSampledBitmap(InputStream in, int width, int height) {
//    Bitmap bm = null;
//
//    final BitmapFactory.Options options = new BitmapFactory.Options();
//    options.inJustDecodeBounds = true;
//    BitmapFactory.decodeStream(in, null, options);
//
//    options.inSampleSize = calculateInSampleSize(options, width, height);
//
//    options.inJustDecodeBounds = false;
//    bm = BitmapFactory.decodeStream(in, null, options);
//
//    return bm;
//  }

//  public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//    final int height = options.outHeight;
//    final int width = options.outWidth;
//    int inSampleSize = 1;
//
//    if(height > reqHeight || width > reqWidth){
//      if(width > height){
//        inSampleSize = Math.round((float) height / (float) reqHeight);
//      } else{
//        inSampleSize = Math.round((float)width / (float)reqWidth);
//      }
//    }
//
//    return inSampleSize;
//  }

  static class ViewHolder {
    ImageView image;
    TextView title;
    TextView author;
  }

  class RetrieveFeedTask extends AsyncTask<String, Void, List<FeedMessage>> {
    private Exception exception;
    List<FeedMessage> messages = new ArrayList<FeedMessage>();
    URL url = null;
    BufferedReader br;
    InputSource is;
    RSSFeedParser parser;
    SAXParserFactory factory;
    SAXParser sp;
    XMLReader reader;

    @Override
    protected List<FeedMessage> doInBackground(String... urls) {
      System.out.println("Enter doInBackground");
      try {
        url = new URL(urls[0]);
        br = new BufferedReader(new InputStreamReader(url.openStream()));
        is = new InputSource(br);
        parser = new RSSFeedParser();
        factory = SAXParserFactory.newInstance();
        sp = factory.newSAXParser();
        reader = sp.getXMLReader();

        reader.setContentHandler(parser);
        reader.parse(is);

        messages = parser.list;
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (SAXException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println("Exit doInBackground");
      return messages;
    }

    protected void onPostExecute(List<FeedMessage> feed) {
      System.out.println("Enter onPostExecute");
      mFeed = feed;
      System.out.println("Exit onPostExecute");
    }

  }

//  class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
//    URL url = null;
//    ImageView imageView = null;
//    InputStream in = null;
//    int response = -1;
//    private final WeakReference<ImageView> imageViewReference;
//    Bitmap bitmap;
//
//    public BitmapWorkerTask(ImageView imageView) {
//      imageViewReference = new WeakReference<ImageView>(imageView);
//    }
//
//    @Override
//    protected Bitmap doInBackground(String... urls) {
//      // TODO Auto-generated method stub
//      try {
//        System.out.println("Start doInBackground");
//        url = new URL(urls[0]);
//        URLConnection conn = url.openConnection();
//
//        if (!(conn instanceof HttpURLConnection)) {
//          throw new IOException("Not an HTTP connection");
//        }
//
//        HttpURLConnection httpConn = (HttpURLConnection) conn;
//        httpConn.setAllowUserInteraction(false);
//        httpConn.setInstanceFollowRedirects(true);
//        httpConn.setRequestMethod("GET");
//        httpConn.connect();
//        response = httpConn.getResponseCode();
//
//        if (response == HttpURLConnection.HTTP_OK) {
//          in = httpConn.getInputStream();
//        }
//        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(in), 150, 100, true);
//        in.close();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//      if (bitmap != null) {
//        addBitmapToMemoryCache(urls[0], bitmap);
//      }
////      final Bitmap bitmap = decodeSampledBitmap(urls[0], 100, 100);
////      if (bitmap != null) {
////        addBitmapToMemoryCache(urls[0], bitmap);
////      }
//      return bitmap;
//    }
//
//    protected void onPostExecute(Bitmap bitmap) {
//      if (imageViewReference != null && bitmap != null) {
//        final ImageView imageView = (ImageView) imageViewReference.get();
//
//        if (imageView != null) {
//          imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//          imageView.setImageBitmap(bitmap);
//        }
//      }
//    }
//  }
}
