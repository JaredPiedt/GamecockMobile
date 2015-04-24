package com.gamecockmobile.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
public class ArticleDetailsActivity extends ActionBarActivity implements ObservableScrollView.Callbacks {

    private static final String TAG = makeLogTag(ArticleDetailsActivity.class);
    Bundle mBundle;

    private static final float PHOTO_ASPECT_RATIO = 1.7777777f;

    public static final String TRANSITION_NAME_PHOTO = "photo";

    private Handler mHandler = new Handler();

    private Toolbar mActionBarToolbar;
    private ViewGroup mRootView;
    private View mScrollViewChild;
    private TextView mTitle;
    private TextView mAuthor;

    private ObservableScrollView mScrollView;

    private TextView mArticleContent;
    private View mHeaderBox;
    private View mDetailsContainer;

    private ProgressBar mProgressBar;

    private int mHeaderTopClearance;
    private int mPhotoHeightPixels;
    private int mHeaderHeightPixels;

    private boolean mHasPhoto;
    private View mPhotoViewContainer;
    private ImageView mPhotoView;
    boolean mGapFillShown;

    private float mMaxHeaderElevation;

    private static final float GAP_FILL_DISTANCE_MULTIPLIER = 1.5f;

    private static final String PHOTO_URL = "photo_url";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String ARTICLE_URL = "article_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_details);
        setProgressBarIndeterminateVisibility(true);

        final Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mHandler.post(new Runnable() {
           @Override
            public void run() {
               toolbar.setTitle("");
           }
        });

        mMaxHeaderElevation = getResources().getDimensionPixelSize(R.dimen.article_details_max_header_elevation);

        mHandler = new Handler();

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        mScrollView.addCallbacks(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if(vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }

        mScrollViewChild = (View) findViewById(R.id.scroll_view_child);
        mScrollViewChild.setVisibility(View.INVISIBLE);

        mDetailsContainer = findViewById(R.id.details_container);
        mHeaderBox = (View) findViewById(R.id.header_article);
        mTitle = (TextView) findViewById(R.id.article_title);
        mAuthor = (TextView) findViewById(R.id.article_author);
        mPhotoViewContainer = findViewById(R.id.article_photo_container);
        mPhotoView = (ImageView) findViewById(R.id.article_photo);
        mArticleContent = (TextView) findViewById(R.id.article_text);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_articleDetails);

        mBundle = getIntent().getExtras();

        if(mBundle != null){
            String photoURL = mBundle.getString(PHOTO_URL);
            String title = mBundle.getString(TITLE);
            String author = mBundle.getString(AUTHOR);
            String articleURL = mBundle.getString(ARTICLE_URL);
            setUpArticleDetails(photoURL, title, author, articleURL);

            //mProgressBar.setVisibility(View.INVISIBLE);
        }

        ViewCompat.setTransitionName(mPhotoView, TRANSITION_NAME_PHOTO);
    }

    protected Toolbar getActionBarToolbar() {
        if(mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if(mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            recomputePhotoAndScrollingMetrics();
        }
    };

    private void recomputePhotoAndScrollingMetrics() {
        mHeaderHeightPixels = mHeaderBox.getHeight();

        mPhotoHeightPixels = 0;
        if(mHasPhoto) {
            mPhotoHeightPixels = (int) (mPhotoView.getWidth() / PHOTO_ASPECT_RATIO);
            mPhotoHeightPixels = Math.min(mPhotoHeightPixels, mScrollView.getHeight() * 2 / 3);
        }

        ViewGroup.LayoutParams lp;
        lp = mPhotoViewContainer.getLayoutParams();
        if(lp.height != mPhotoHeightPixels) {
            lp.height = mPhotoHeightPixels;
            mPhotoViewContainer.setLayoutParams(lp);
        }

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)
                mDetailsContainer.getLayoutParams();
        if(mlp.topMargin != mHeaderHeightPixels + mPhotoHeightPixels) {
            mlp.topMargin = mHeaderHeightPixels + mPhotoHeightPixels;
            mDetailsContainer.setLayoutParams(mlp);
        }

        onScrollChanged(0, 0); // trigger scroll handling
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        // Reposition the header bar == it's normally anchored to the top of the content
        // but locks to the top of the screen on scroll
        int scrollY = mScrollView.getScrollY();

        float newTop = Math.max(mPhotoHeightPixels, scrollY);
        mHeaderBox.setTranslationY(newTop);

        float gapFillProgress = 1;
        if(mPhotoHeightPixels != 0) {
            gapFillProgress = Math.min(Math.max(UIUtils.getProgress(scrollY, 0, mPhotoHeightPixels), 0), 1);
        }

        ViewCompat.setElevation(mHeaderBox, gapFillProgress * mMaxHeaderElevation);

        // Move background photo (parallax effect)
        mPhotoViewContainer.setTranslationY(scrollY * 0.5f);
    }

    private void setUpArticleDetails(final String photoURL, String title, String author, String articleURL) {
        mHeaderBox.setBackgroundColor(getResources().getColor(R.color.garnet));
        mPhotoViewContainer.setBackgroundColor(UIUtils.scaleSessionColorToDefaultBG(getResources().getColor(R.color.garnet)));

        if(photoURL != null) {
            mHasPhoto = true;
            Glide.load(photoURL)
                    .listener(new Glide.RequestListener<String>() {
                        @Override
                        public void onException(Exception e, String s, Target target) {
                            mHasPhoto = false;
                            recomputePhotoAndScrollingMetrics();
                        }

                        @Override
                        public void onImageReady(String s, Target target) {
                            //Trigger image transition
                            recomputePhotoAndScrollingMetrics();
                        }
                    })
                    .centerCrop()
                    .placeholder(R.drawable.garnet_background)
                    .animate(R.animator.image_fade_in)
                    .into(mPhotoView);
            recomputePhotoAndScrollingMetrics();

        } else {
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
                s += "\t\t\t" + Jsoup.parse(e.toString()).text() + "\n";

            }

            LOGD(TAG, String.valueOf(s.length()));

            return s;
        }

        @Override
        protected void onPostExecute(String results){
            LOGD(TAG, "onPostExecute " + String.valueOf(results.length()));
            mArticleContent.setText(results);
            setProgressBarIndeterminateVisibility(false);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
