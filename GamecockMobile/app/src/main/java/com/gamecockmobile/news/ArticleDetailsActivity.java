package com.gamecockmobile.news;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.presenter.target.SimpleTarget;
import com.bumptech.glide.presenter.target.Target;
import com.gamecockmobile.R;
import com.gamecockmobile.util.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.gamecockmobile.util.LogUtils.LOGD;
import static com.gamecockmobile.util.LogUtils.makeLogTag;


/**
 * Created by piedt on 8/12/14.
 */
public class ArticleDetailsActivity extends Activity implements ObservableScrollView.Callbacks {

    private static final String TAG = makeLogTag(ArticleDetailsActivity.class);
    Bundle mBundle;

    private static final float PHOTO_ASPECT_RATIO = 1.7777777f;

    private Handler mHandler = new Handler();

    private ViewGroup mRootView;
    private View mScrollViewChild;
    private TextView mTitle;
    private TextView mAuthor;

    private ObservableScrollView mScrollView;

    private TextView mArticleContent;
    private View mHeaderBox;
    private View mHeaderContentBox;
    private View mHeaderBackgroundBox;
    private View mHeaderShadow;
    private View mDetailsContainer;

    private int mHeaderTopClearance;
    private int mPhotoHeightPixels;
    private int mHeaderHeightPixels;

    private boolean mHasPhoto;
    private View mPhotoViewContainer;
    private ImageView mPhotoView;
    boolean mGapFillShown;

    private static final float GAP_FILL_DISTANCE_MULTIPLIER = 1.5f;

    private static final String PHOTO_URL = "photo_url";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String ARTICLE_URL = "article_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);

        mHandler = new Handler();

        mScrollViewChild = (View) findViewById(R.id.scroll_view_child);
        mScrollViewChild.setVisibility(View.INVISIBLE);

        mDetailsContainer = findViewById(R.id.details_container);
        mHeaderBox = (View) findViewById(R.id.header_article);
        mHeaderContentBox = (LinearLayout) findViewById(R.id.header_article_contents);
        mHeaderBackgroundBox = (View) findViewById(R.id.header_background);
        mHeaderShadow = (View) findViewById(R.id.header_shadow);
        mTitle = (TextView) findViewById(R.id.article_title);
        mAuthor = (TextView) findViewById(R.id.article_author);
        mPhotoViewContainer = findViewById(R.id.article_photo_container);
        mPhotoView = (ImageView) findViewById(R.id.article_photo);
        mArticleContent = (TextView) findViewById(R.id.article_text);

        setupCustomScrolling();

        mBundle = getIntent().getExtras();

        if(mBundle != null){
            String photoURL = mBundle.getString(PHOTO_URL);
            String title = mBundle.getString(TITLE);
            String author = mBundle.getString(AUTHOR);
            String articleURL = mBundle.getString(ARTICLE_URL);
            setUpArticleDetails(photoURL, title, author, articleURL);
        }


    }

    private void setupCustomScrolling() {
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        mScrollView.addCallbacks(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();

        if(vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mScrollView == null){
            return;
        }

        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if(vto.isAlive()){
            vto.removeGlobalOnLayoutListener(mGlobalLayoutListener);
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            recomputePhotoAndScrollingMetrics();
        }
    };

    private void recomputePhotoAndScrollingMetrics() {
        LOGD(TAG, "Enter recomputePhotoAndScrollingMetrics");

        final int actionBarSize = UIUtils.calculateActionBarSize(this);
        mHeaderTopClearance = actionBarSize - mHeaderContentBox.getPaddingTop();

        LOGD(TAG, "***** mHeaderTopClearance = " + mHeaderTopClearance);
        mHeaderHeightPixels = mHeaderContentBox.getHeight();

        mPhotoHeightPixels = mHeaderTopClearance;
        if(mHasPhoto) {
            mPhotoHeightPixels = (int) (mPhotoView.getWidth() / PHOTO_ASPECT_RATIO);
            mPhotoHeightPixels = Math.min(mPhotoHeightPixels, getWindowManager().getDefaultDisplay().getHeight() * 2 / 3);
        }

        ViewGroup.LayoutParams lp;
        lp = mPhotoViewContainer.getLayoutParams();
        if(lp.height != mPhotoHeightPixels) {
            lp.height = mPhotoHeightPixels;
            mPhotoViewContainer.setLayoutParams(lp);
        }

        lp = mHeaderBackgroundBox.getLayoutParams();
        if(lp.height != mHeaderHeightPixels) {
            lp.height = mHeaderHeightPixels;
            LOGD(TAG, "***** Background height is: " + mHeaderHeightPixels);
            mHeaderBackgroundBox.setLayoutParams(lp);
        }

        LOGD(TAG, "****Background height is: " + mHeaderBackgroundBox.getHeight());

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)
                mDetailsContainer.getLayoutParams();
        if(mlp.topMargin != mHeaderHeightPixels + mPhotoHeightPixels) {
            mlp.topMargin = mHeaderHeightPixels + mPhotoHeightPixels;
            mDetailsContainer.setLayoutParams(mlp);
        }

        onScrollChanged(0, 0); // trigger scroll handling

        LOGD(TAG, "Exit recomputePhotoAndScrollingMetrics");
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        LOGD(TAG, "Enter onScrollChanged");
        // Reposition the header bar == it's normally anchored to the top of the content
        // but locks to the top of the screen on scroll
        int scrollY = mScrollView.getScrollY();

        float newTop = Math.max(mPhotoHeightPixels, scrollY + mHeaderTopClearance);
        mHeaderBox.setTranslationY(newTop);

        mHeaderBackgroundBox.setPivotY(mHeaderHeightPixels);
        int gapFillDistance = (int) (mHeaderTopClearance * GAP_FILL_DISTANCE_MULTIPLIER);
        boolean showGapFill = !mHasPhoto || (scrollY > (mPhotoHeightPixels - gapFillDistance));
        float desiredHeaderScaleY = showGapFill ?
                ((mHeaderHeightPixels + gapFillDistance + 1) * 1f / mHeaderHeightPixels)
                : 1f;
        if(!mHasPhoto){
            mHeaderBackgroundBox.setScaleY(desiredHeaderScaleY);
            LOGD(TAG, "Header being shown with no photo. Height = " + desiredHeaderScaleY);
        } else if (mGapFillShown != showGapFill) {
            mHeaderBackgroundBox.setScaleY(desiredHeaderScaleY);

            // TO-DO figure out issue with animate
//            mHeaderBackgroundBox.animate()
//                    .scaleY(desiredHeaderScaleY)
//                    .setInterpolator(new DecelerateInterpolator(2f))
//                    .setDuration(250)
//                    .start();

        }
        LOGD(TAG, "Show gap fill = " + showGapFill);
        mGapFillShown = showGapFill;

        mHeaderShadow.setVisibility(View.VISIBLE);

        if(mHeaderTopClearance != 0) {
            // Fill the gap between status bar and header bar with color
            LOGD(TAG, "Gap between status bar and header bar is being filled");
            float gapFillProgress = Math.min(Math.max(UIUtils.getProgress(scrollY,
                    mPhotoHeightPixels - mHeaderTopClearance * 2,
                    mPhotoHeightPixels - mHeaderTopClearance), 0), 1);
            mHeaderShadow.setAlpha(gapFillProgress);
        }

        int[] locations = new int[2];
        mHeaderBackgroundBox.getLocationOnScreen(locations);
        LOGD(TAG, "Header height = " + mHeaderBackgroundBox.getHeight());
        LOGD(TAG, "Header position y = " + locations[1]);
        mTitle.getLocationOnScreen(locations);
        LOGD(TAG, "Header title position y =" + locations[1]);

        // Move background photo (parallax effect)
        mPhotoViewContainer.setTranslationY(scrollY * 0.5f);

        LOGD(TAG, "Exit onScrollChanged");
    }

    private void setUpArticleDetails(final String photoURL, String title, String author, String articleURL) {

        LOGD(TAG, "Enter setupArticleDetails");

        mHeaderBackgroundBox.setBackgroundColor(getResources().getColor(R.color.garnet));

        LOGD(TAG, "*****mHeaderBackgroundBox color = " + mHeaderBackgroundBox.getSolidColor());

        mPhotoViewContainer.setBackgroundColor(UIUtils.scaleSessionColorToDefaultBG(getResources().getColor(R.color.garnet)));

        if(photoURL != null) {
            LOGD(TAG, "Image was found, loading image...");
            mHasPhoto = true;
//            Glide.load(photoURL)
//                    .centerCrop()
//                    .placeholder(R.drawable.garnet_background)
//                    .animate(R.animator.image_fade_in)
//                    .into(new SimpleTarget() {
//                        @Override
//                        public void onImageReady(Bitmap bitmap) {
//                            LOGD(TAG, "Photo was found");
//                            mPhotoView.setImageBitmap(bitmap);
//                            recomputePhotoAndScrollingMetrics();
//                        }
//
//                        @Override
//                        public int[] getSize() {return new int[] { mPhotoView.getWidth(), mPhotoView.getHeight()};}
//                    });
            Glide.load(photoURL)
                    .listener(new Glide.RequestListener<String>() {
                        @Override
                        public void onException(Exception e, String s, Target target) {
                            LOGD(TAG, "Error loading photo with url: " + photoURL);
                            mHasPhoto = false;
                            recomputePhotoAndScrollingMetrics();
                        }

                        @Override
                        public void onImageReady(String s, Target target) {
                            LOGD(TAG, "Image was found");
                            //Trigger image transition
                            recomputePhotoAndScrollingMetrics();
                        }
                    })
                    .centerCrop()
                    .placeholder(R.drawable.garnet_background)
                    .animate(R.animator.image_fade_in)
                    .into(mPhotoView);
            LOGD(TAG, "Image loaded");
            recomputePhotoAndScrollingMetrics();

        } else {
            LOGD(TAG, "Image URL is blank");
            mHasPhoto = false;
            recomputePhotoAndScrollingMetrics();
        }

        mTitle.setText(title);
        mAuthor.setText(author);
        new Parser().execute(articleURL);


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onScrollChanged(0, 0); // trigger scroll handling
                mScrollViewChild.setVisibility(View.VISIBLE);
            }
        });

        LOGD(TAG, "Exit setUpArticleDetails");
    }

    private class Parser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls){
            String url = urls[0];

            LOGD(TAG, "URL is " + url);
            Document doc = null;
            try{
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements paragraphs = doc.select("article").first().select("p");

            String s = "";

            for(Element e : paragraphs)
            {
                s += "\t" + Jsoup.parse(e.toString()).text() + "\n";

            }

            LOGD(TAG, String.valueOf(s.length()));

            return s;
        }

        @Override
        protected void onPostExecute(String results){
            LOGD(TAG, "onPostExecute " + String.valueOf(results.length()));
            mArticleContent.setText(results);
        }
    }
}
