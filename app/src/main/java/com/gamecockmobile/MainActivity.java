package com.gamecockmobile;

import java.util.ArrayList;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gamecockmobile.buses.BusesFragment;
import com.gamecockmobile.events.EventsFragment;
import com.gamecockmobile.news.NewsFragment;
import com.gamecockmobile.social.SocialFragment;
import com.gamecockmobile.util.LUtils;
import com.gamecockmobile.widget.NavDrawerItemView;
import com.gamecockmobile.widget.ScrimInsetsScrollView;

/**
 * This is the main class for the app that runs the dashboard for all of the resources.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    //private LPreviewUtils.ActionBarDrawerToggleWrapper mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Helper methods for L APIs
    private LUtils mLUtils;

    private ObjectAnimator mStatusBarColorAnimator;

    private Handler mHandler;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    private Intent mServiceIntent;

    // slide menu items
    private String[] navMenuTitles;

    Toolbar mToolbar;

    // variables that control the Action Bar auto hide behavior (aka "quick recall")
    private boolean mActionBarAutoHideEnabled = false;

    private boolean mActionBarShown = true;

    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;

    private int mThemedStatusBarColor;

    private int mNormalStatusBarColor;

    private ViewGroup mDrawerItemsListContainer;

    protected static final int NAVDRAWER_ITEM_COURSE_LIST = 0;
    protected static final int NAVDRAWER_ITEM_EVENTS = 1;
    protected static final int NAVDRAWER_ITEM_BUSES = 2;
    protected static final int NAVDRAWER_ITEM_SOCIAL = 3;
    protected static final int NAVDRAWER_ITEM_NEWS = 4;

    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[] {
            R.string.navdrawer_item_course_list,
            R.string.navdrawer_item_events,
            R.string.navdrawer_item_buses,
            R.string.navdrawer_item_social,
            R.string.navdrawer_item_news
    };

    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
            R.drawable.ic_schedule_grey, // Course List
            R.drawable.ic_schedule_grey, // Events
            R.drawable.ic_schedule_grey, // Buses
            R.drawable.ic_people_grey, // Social
            R.drawable.ic_trending_up_grey // News
    };

    // fade in and fade out durations for the main content when switching between different Fragmnets of the app throught the Nav Drawer
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;

    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 350;

    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;

    private ScrimInsetsScrollView navDrawer;

    private static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();

    private int mNavDrawerSelectedItem = NAVDRAWER_ITEM_COURSE_LIST;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // deleteDatabase("CoursesManager.db");
        super.onCreate(savedInstanceState);

        System.out.println("Starting...");
        setContentView(R.layout.activity_main);

//        mServiceIntent = new Intent(this, CourseDatabaseService.class);
//        this.startService(mServiceIntent);
        mTitle = getTitle();

        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();

        mHandler = new Handler();

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //displayView(0);
        }
        getSupportActionBar().setTitle("Courses");

        mLUtils = LUtils.getInstance(this);
        mThemedStatusBarColor = getResources().getColor(R.color.stacked_garnet);
        mNormalStatusBarColor = mThemedStatusBarColor;
    }

//    /**
//     * Slide menu item click listener
//     */
//    private class SlideMenuClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            // display view for selected nav drawer item
//            displayView(position);
//        }
//    }

    protected int getSelfNavDrawerItem() {
        return mNavDrawerSelectedItem;
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(mDrawerLayout == null) {
            return;
        }
       mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.stacked_garnet));
        ScrimInsetsScrollView navDrawer = (ScrimInsetsScrollView)
                mDrawerLayout.findViewById(R.id.fragment_navigation_drawer);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if(mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
                onNavDrawerStateChanged(false, false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                onNavDrawerSlide(slideOffset);
            }
        });

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // populate the nav drawer with the correct items
        populateNavDrawer();
    }

    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        if(mActionBarAutoHideEnabled && isOpen) {
            autoShowOrHideActionBar(true);
        }
    }
    protected void onNavDrawerSlide(float offset) {

    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if(mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Defines the Navigation Drawer items to display by updating {@code mNavDrawerItems} then
     * forces the Navigation Drawer to redraw itself.
     */
    private void populateNavDrawer() {
        mNavDrawerItems.clear();

        mNavDrawerItems.add(NAVDRAWER_ITEM_COURSE_LIST);
        mNavDrawerItems.add(NAVDRAWER_ITEM_EVENTS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_NEWS);

        createNavDrawerItems();
    }

    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if(mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for(int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to the selected state.
     */
    private void setSelectedNavDrawerItems(int itemId) {
        if(mNavDrawerItemViews != null) {
            for(int i = 0; i < mNavDrawerItemViews.length; i++) {
                if(i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    mNavDrawerItemViews[i].setActivated(itemId == thisItemId);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
//        // Handle action bar actions click
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
//            default:
                return super.onOptionsItemSelected(item);
//        }

    }

    private void onNavDrawerItemClicked(final int itemId) {
        if(itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavDrawerItem(itemId);
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        // change the active item on the listso the user can see the item changed
        setSelectedNavDrawerItems(itemId);
        // fade out the main content
        View mainContent = findViewById(R.id.frame_container);
        if(mainContent != null) {
            mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
//        if (isSeparator(itemId)) {
//
//        }

        NavDrawerItemView item = (NavDrawerItemView) getLayoutInflater().inflate(
                R.layout.navdrawer_item, container, false);
        item.setContent(NAVDRAWER_ICON_RES_ID[itemId], NAVDRAWER_TITLE_RES_ID[itemId]);
        item.setActivated(getSelfNavDrawerItem() == itemId);
        if(item.isActivated()) {
            item.setContentDescription(getString(NAVDRAWER_TITLE_RES_ID[itemId]));
        } else {
            item.setContentDescription(getString(NAVDRAWER_TITLE_RES_ID[itemId]));
        }

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });
        return item;
    }

    public LUtils getLUtils() {
        return mLUtils;
    }

    protected void autoShowOrHideActionBar(boolean show) {
        if(show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
        onActionBarAutoShowOrHide(show);
    }

    protected void onActionBarAutoShowOrHide(boolean shown) {
        if(mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
        }
        mStatusBarColorAnimator = ObjectAnimator.ofInt(
                (mDrawerLayout != null) ? mDrawerLayout : mLUtils,
                (mDrawerLayout != null) ? "statusBarBackgroundColor" : "statusBarColor",
                shown ? Color.BLACK : mNormalStatusBarColor,
                shown ? mNormalStatusBarColor: Color.BLACK)
                .setDuration(250);
        if(mDrawerLayout != null) {
            mStatusBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewCompat.postInvalidateOnAnimation(mDrawerLayout);
                }
            });
        }
        mStatusBarColorAnimator.setEvaluator(ARGB_EVALUATOR);
        mStatusBarColorAnimator.start();
    }

    private void goToNavDrawerItem(int item) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (item) {
            case NAVDRAWER_ITEM_COURSE_LIST:
                fragment = new CourseListFragment();
                break;
            case NAVDRAWER_ITEM_EVENTS:
                fragment = new EventsFragment();
                break;
            case NAVDRAWER_ITEM_BUSES:
                fragment = new BusesFragment();
                break;
            case NAVDRAWER_ITEM_SOCIAL:
                fragment = new SocialFragment();
                break;
            case NAVDRAWER_ITEM_NEWS:
                fragment = new NewsFragment();
            default:
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        View mainContent = findViewById(R.id.frame_container);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        }
    }
//
//    private void setSelectedNavDrawerItem(int itemId) {
//        mDrawerList.setItemChecked(itemId, true);
//        mDrawerList.setSelection(itemId);
//
//        setTitle(navMenuTitles[itemId]);
//    }
//
//    /**
//     * Diplaying fragment view for selected nav drawer list item
//     */
//    private void displayView(final int position) {
//        // Launch the target Activity after a short delay, to allow the close animation to play
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                goToNavDrawerItem(position);
//            }
//        }, NAVDRAWER_LAUNCH_DELAY);
//
//        // change the active item on the list so the user can see the item changed
//        setSelectedNavDrawerItem(position);
//
//        // fade out the main content
//        View mainContent = findViewById(R.id.frame_container);
//        if (mainContent != null) {
//            mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
//        }
//
//        mDrawerLayout.closeDrawer(mDrawerList);
//
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mToolbar.setTitle(mTitle);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }
}