package com.gamecockmobile.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.gamecockmobile.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

  public NewsAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);

    // mFeed = getFeed();
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

      convertView = mInflater.inflate(R.layout.news_list_item, parent, false);
      holder.image = (ImageView) convertView.findViewById(R.id.news_imageView);
      holder.title = (TextView) convertView.findViewById(R.id.news_title_textView);
      holder.author = (TextView) convertView.findViewById(R.id.news_author_textView);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    String url = parseURL(message.description);
    if (url != null) {
      try {
        bitmap = new RetrieveImageTask().execute(url).get();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ExecutionException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      if (bitmap != null) {
        holder.image.setImageBitmap(bitmap);
      }
      //bitmap.recycle();
    }
    // holder.image.setImageBitmap(bitmap);
    // bitmap.recycle();
    // }
    holder.title.setText(message.title.trim());
    holder.author.setText(message.author.trim());

    return convertView;
  }

  private ArrayList<FeedMessage> getFeed() {
    List<FeedMessage> messages = null;
    URL url = null;
    BufferedReader br;
    InputSource is;
    RSSFeedParser parser;
    SAXParserFactory factory;
    SAXParser sp;
    XMLReader reader;

    try {
      url = new URL(mURL);
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

    return (ArrayList<FeedMessage>) messages;
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

  private InputStream openHttpConnection(String urlString) throws IOException {
    InputStream in = null;
    int response = -1;

    URL url = new URL(urlString);
    URLConnection conn = url.openConnection();

    if (!(conn instanceof HttpURLConnection)) {
      throw new IOException("Not an HTTP connection");
    }

    try {
      HttpURLConnection httpConn = (HttpURLConnection) conn;
      httpConn.setAllowUserInteraction(false);
      httpConn.setInstanceFollowRedirects(true);
      httpConn.setRequestMethod("GET");
      httpConn.connect();
      response = httpConn.getResponseCode();
      if (response == HttpURLConnection.HTTP_OK) {
        in = httpConn.getInputStream();
      }
    } catch (Exception ex) {
      throw new IOException("Error connecting");
    }

    return in;
  }

  private Bitmap downloadImage(String URL) {
    Bitmap bitmap = null;
    InputStream in = null;

    try {
      in = openHttpConnection(URL);
      bitmap = BitmapFactory.decodeStream(in);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return bitmap;
  }

  class ViewHolder {
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

  class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {
    URL url = null;
    Bitmap bitmap = null;
    InputStream in = null;
    int response = -1;

    @Override
    protected Bitmap doInBackground(String... urls) {
      // TODO Auto-generated method stub
      try {
        url = new URL(urls[0]);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection)) {
          throw new IOException("Not an HTTP connection");
        }

        HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setAllowUserInteraction(false);
        httpConn.setInstanceFollowRedirects(true);
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        response = httpConn.getResponseCode();

        if (response == HttpURLConnection.HTTP_OK) {
          in = httpConn.getInputStream();
        }
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(in), 60, 60, true);
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      return bitmap;
    }

    // protected void onPostExecute(Bitmap bitmap){
    // mBitmap = bitmap;
    // }

  }
}
