package com.gamecockmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is the class that does all of the work with the dashboard. 
 */
public class GridItemAdapter extends BaseAdapter {
	
  private Context mContext;
	private final String [] iconValues;
  protected View mGridView;
  protected TextView mTextView;
  protected ImageView mImageView;
  private String mArrLabel;
  
	public GridItemAdapter(Context c, String[] iconValues) {
		mContext = c;
		this.iconValues = iconValues;
	}

	public int getCount() {
		return iconValues.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

		
		if (convertView == null) {
			mGridView = new View(mContext);
			mGridView = inflater.inflate(R.layout.main_icon, null);
			
			mTextView = (TextView) mGridView.findViewById(R.id.home_textview);
			mTextView.setText(iconValues[position]);
			
			mImageView = (ImageView) mGridView.findViewById(R.id.home_imageview);
			
			mArrLabel = iconValues[position];
			
			if(mArrLabel.equals("Schedule")){
				mImageView.setImageResource(R.drawable.ic_launcher);
			}
			
		} else {
			mGridView = (View) convertView;
		}
		
		return mGridView;
	}
}
