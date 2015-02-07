package com.bitgig.bitgig;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bitgig.bitgig.common.BaseActivity;
import com.bitgig.bitgig.model.GigPost;
import com.bitgig.bitgig.ui.CreateGigPost;
import com.bitgig.bitgig.ui.GigPostFragment;
import com.bitgig.bitgig.ui.adapters.GigPostListAdapter;
import com.bitgig.bitgig.ui.adapters.GigPostRecAdapter;
import com.bitgig.bitgig.ui.widget.BezelImageView;
import com.bitgig.bitgig.ui.widget.SlidingTabLayout;
import com.bitgig.bitgig.util.FacebookManager;
import com.facebook.widget.ProfilePictureView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends BaseActivity
        implements GigPostFragment.Listener {

    // How is this Activity being used?
    private static final int MODE_EXPLORE = 0; // as top-level "Explore" screen
    private static final int MODE_TIME_FIT = 1; // showing sessions that fit in a time interval

    private static final String STATE_FILTER_0 = "STATE_FILTER_0";
    private static final String STATE_FILTER_1 = "STATE_FILTER_1";
    private static final String STATE_FILTER_2 = "STATE_FILTER_2";

    public static final String EXTRA_FILTER_TAG = "com.google.android.iosched.extra.FILTER_TAG";

    private int mMode = MODE_EXPLORE;

    // Login failed butter bar
    View mButterBar;

    // If true, we are in the wide (tablet) mode where we show conference days side by side;
    // if false, we are in narrow (handset) mode where we use a ViewPager and show only
    // one conference day at a time.
    private boolean mWideMode = false;

    private Set<GigPostFragment> myGigPostListFragments = new HashSet<>();

    // TODO: Fix the size of the adapters, and also figure out a way to create an adpater for map and listView
    // The adapters that serves as the source of data for the UI, indicating the available
    // items. We have one adapter per feed. When we push new data into these
    // adapters, the corresponding UIs update automatically.
    private GigPostListAdapter[] mGigPostListAdapters = new GigPostListAdapter[2];

    private GigPostRecAdapter[] mGigPostRecAdapters = new GigPostRecAdapter[2];

    // View pager and adapter (for narrow mode)
    ViewPager mViewPager = null;
    OurViewPagerAdapter mViewPagerAdapter = null;
    SlidingTabLayout mSlidingTabLayout = null;
    ScrollView mScrollViewWide;

    private Context context;
    com.bitgig.bitgig.ui.widget.BezelImageView facebookImage;
    ProfilePictureView profile_image;
    public SetProfilePicture set;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.feed_layout);
        setContentView(R.layout.activity_my_schedule);


        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mScrollViewWide = (ScrollView) findViewById(R.id.main_content_wide);
        mWideMode = findViewById(R.id.my_schedule_first_day) != null;

        //TODO: Fix the number of adpater being used
        for(int i = 0; i< 2; i++){
            //mGigPostListAdapters[i] = new GigPostListAdapter(this, createList(10)); //TODO USED THIS FOR REGULAR LISTS
            mGigPostRecAdapters[i] = new GigPostRecAdapter(createList(10));
        }

        mViewPagerAdapter = new OurViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        TextView firstDayHeaderView = (TextView) findViewById(R.id.day_label_first_day);
        TextView secondDayHeaderView = (TextView) findViewById(R.id.day_label_second_day);

        if (firstDayHeaderView != null) {
            firstDayHeaderView.setText("MARKETPLACE");
        }
        if (secondDayHeaderView != null) {
            secondDayHeaderView.setText("WORLD");
        }


        //TODO: Look At MyScheduleActivity; we need to populate an array of Views
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

        setSlidingTabLayoutContentDescriptions();

        Resources res = getResources();
        overridePendingTransition(0, 0);

        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.tab_selected_strip));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        mButterBar = findViewById(R.id.butter_bar);

        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mSlidingTabLayout.announceForAccessibility(
                                getString(R.string.my_feed_page_desc_a11y,
                                        getDayName(position)));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    //TODO: Set This As False to STOP SwipeRefresh
                    //enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
                    enableDisableSwipeRefresh(false);
                }
            });
        }
        overridePendingTransition(0, 0);

        context = this;
        profile_image = (ProfilePictureView) findViewById(R.id.profile_image);
        set = new SetProfilePicture();
        FacebookManager.getInstance().setContext(this);
        /**
         Create dummy fragments**/

        fab = (FloatingActionButton)findViewById(R.id.new_gig_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CreateGigPost.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

/*
    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        if (mWideMode) {
            return ViewCompat.canScrollVertically(mScrollViewWide, -1);
        }

        for (GigPostFragment fragment : myGigPostListFragments) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                if (!fragment.getUserVisibleHint()) {
                    continue;
                }
            }
            //TODO: Fix this to work with RecyclerView
            //return ViewCompat.canScrollVertically(fragment.getListView(), -1);
        }

        return false;
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mViewPager != null) {
            /*long now = UIUtils.getCurrentTime(this);
            for (int i = 0; i < Config.CONFERENCE_DAYS.length; i++) {
                if (now >= Config.CONFERENCE_DAYS[i][0] && now <= Config.CONFERENCE_DAYS[i][1]) {
                    mViewPager.setCurrentItem(i);
                    setTimerToUpdateUI(i);
                    break;
                }
            }*/
            for(int i = 0; i < 2; i++){

            }
        }
        /*
        setProgressBarTopWhenActionBarShown((int)
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                        getResources().getDisplayMetrics()));*/
    }

    //TODO: FIX THIS LOGIC WHEN USER SELECTS A GIG
    @Override
    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        // return mMode == MODE_EXPLORE ? NAVDRAWER_ITEM_EXPLORE : NAVDRAWER_ITEM_INVALID;
        return NAVDRAWER_ITEM_WALLET;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

/*        getMenuInflater().inflate(R.menu.browse_sessions, menu);
        // remove actions when in time interval mode:
        if (mMode != MODE_EXPLORE) {
            menu.removeItem(R.id.menu_search);
            menu.removeItem(R.id.menu_refresh);
            menu.removeItem(R.id.menu_wifi);
            menu.removeItem(R.id.menu_debug);
            menu.removeItem(R.id.menu_about);
        } else {
            ///////configureStandardMenuItems(menu);
        }*/
        return true;
    }
    /*
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }
    */
    /*
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.profile);
                break;
            case 2:
                mTitle = getString(R.string.payments);
                break;
            case 3:
                mTitle = getString(R.string.promotions);
                break;
            case 4:
                mTitle = getString(R.string.share);
                break;
            case 5:
                mTitle = getString(R.string.support);
                break;
            case 6:
                mTitle = getString(R.string.about);
                break;
        }
    }
*/

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // TODO:
    private String getDayName(int position) {
        /*if (position >= 0 && position < Config.CONFERENCE_DAYS.length) {
            long timestamp = Config.CONFERENCE_DAYS[position][0];
            return TimeUtils.formatHumanFriendlyShortDate(this, timestamp);
        } else {
            return "";
        }*/
        return "One";
    }

    // TODO:
    private void setSlidingTabLayoutContentDescriptions() {
        /*for (int i = 0; i < Config.CONFERENCE_DAYS.length; i++) {
            mSlidingTabLayout.setContentDescription(i,
                    getString(R.string.my_schedule_tab_desc_a11y, getDayName(i)));
        }*/
        mSlidingTabLayout.setContentDescription(0, "THIS THE FEED");
        mSlidingTabLayout.setContentDescription(1, "SECOND FEED");
    }

    private ArrayList<GigPost> createList(int size) {
        ArrayList<GigPost> ret = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            GigPost gPost = new GigPost("This is a dummy content for a dummy post on ChitChat. This is a dummy content for a dummy post on ChitChat... "
                    + i, "Username " + i, 1093289L, "Miami, FL " + i);
            ret.add(gPost);
        }
        return ret;
    }


    @Override
    //public void onFragmentViewCreated(ListFragment fragment) {
    public void onFragmentViewCreated(RecyclerView mRecycler) {
        /*fragment.getListView().addHeaderView(
                getLayoutInflater().inflate(R.layout.reserve_action_bar_space_header_view, null));*/
        /*int dayIndex = fragment.getArguments().getInt(ARG_CONFERENCE_DAY_INDEX, 0);*/
        //fragment.setListAdapter(mScheduleAdapters[dayIndex]);
        //fragment.getListView().setRecyclerListener(mScheduleAdapters[dayIndex]);

        //TODO: FIX according to the VIEW or LISTVIEW we are on, in the ViewPager
        /*fragment.setListAdapter(mGigPostListAdapters[0]);
        fragment.getListView().setRecyclerListener(mGigPostListAdapters[0]);*/

        mRecycler.setAdapter(mGigPostRecAdapters[0]);  //TODO: FIx THIS LOOK INTO GigPostFragment,
        //TODO We should be attaching the adapter her but it doesn't take place, instead I had to hard code em inside the GigPostRecAdapter

    }

    @Override
    public void onFragmentAttached(GigPostFragment fragment) {
        myGigPostListFragments.add(fragment);
    }

    @Override
    public void onFragmentDetached(GigPostFragment fragment) {
        myGigPostListFragments.remove(fragment);
    }

    private class OurViewPagerAdapter extends FragmentPagerAdapter {

        public OurViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /*LOGD(TAG, "Creating fragment #" + position);
            MyScheduleFragment frag = new MyScheduleFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_CONFERENCE_DAY_INDEX, position);
            frag.setArguments(args);
            return frag;*/

            GigPostFragment frag = new GigPostFragment();
            return frag;
        }

        //TODO:
        @Override
        public int getCount() {
            /*return Config.CONFERENCE_DAYS.length;*/
            return 2;
        }

        //TODO:
        @Override
        public CharSequence getPageTitle(int position) {
            /*return getDayName(position);*/
            CharSequence ret = "HELLO";
            switch (position){
                case 0:
                    ret = "Marketplace";
                    break;
                case 1:
                    ret = "Cart";
                    break;
                default:
                    ret = "DEFAULT";
                    break;
            }
            return ret;
        }
    }


    private class SetProfilePicture implements Callback {
        public void doSomething() {
            class Runs implements Runnable {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            final ProfilePictureView ppv = profile_image;
                            wait(1000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String id = FacebookManager.getInstance().getId();

                                    if (id != null) {
                                        ppv.setProfileId(id);
                                    } else {
                                        Log.e("failed to set picture", "meh");
                                    }
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
            }
            new Thread(new Runs()).start();
        }
    }
}
