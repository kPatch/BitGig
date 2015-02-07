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
import com.bitgig.bitgig.ui.FacebookLoginFragment;
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


    //mMyGigPostFragment
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
    MainViewPagerAdapter mViewPagerAdapter = null;
    SlidingTabLayout mSlidingTabLayout = null;
    ScrollView mScrollViewWide;

    FacebookLoginFragment facebook;
    private Context context;
    BezelImageView facebookImage;
    ProfilePictureView profile_image;
    public SetProfilePicture set;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mScrollViewWide = (ScrollView) findViewById(R.id.main_content_wide);
        mWideMode = findViewById(R.id.my_schedule_first_day) != null;

        //TODO: Fix the number of adpater being used
        for(int i = 0; i< 2; i++){
            mGigPostRecAdapters[i] = new GigPostRecAdapter(createList(10));
        }

        mViewPagerAdapter = new MainViewPagerAdapter(getFragmentManager());
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

        // Check is the ViewPager was attached properly
        if(mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mSlidingTabLayout.announceForAccessibility(
                                getString(R.string.my_feed_page_desc_a11y,
                                        getDayName(position))
                        );
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    enableDisableSwipeRefresh(true);
                }
            });
        }

        overridePendingTransition(0, 0);

        // It is supposed to set Facebook Login Box in the Sliding Drawer Layout
/*        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            facebook = new FacebookLoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.login_container, facebook)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            facebook = (FacebookLoginFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.login_container);
        }*/

        context = this;
        profile_image = (ProfilePictureView) findViewById(R.id.profile_image);
        set = new SetProfilePicture();
        FacebookManager.getInstance().setContext(this);
        /**
         Create dummy fragments**/
/*        fab = (FloatingActionButton)findViewById(R.id.new_gig_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CreateGigPost.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    @Override
    public void onFragmentViewCreated(RecyclerView mRecycler) {

    }

    @Override
    public void onFragmentAttached(GigPostFragment fragment) {

    }

    @Override
    public void onFragmentDetached(GigPostFragment fragment) {

    }


    //TODO: FIX THIS LOGIC WHEN USER SELECTS A GIG
    @Override
    protected int getSelfNavDrawerItem() {
        // we only have a nav drawer if we are in top-level Explore mode.
        // return mMode == MODE_EXPLORE ? NAVDRAWER_ITEM_EXPLORE : NAVDRAWER_ITEM_INVALID;
        return NAVDRAWER_ITEM_WALLET;
    }


    // TODO:
    private String getDayName(int position) {
        return "One";
    }


    // TODO:
    private void setSlidingTabLayoutContentDescriptions() {
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

    private class MainViewPagerAdapter extends FragmentPagerAdapter {

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            GigPostFragment frag = new GigPostFragment();
            return frag;
        }

        //TODO:
        @Override
        public int getCount() {
            /*return Config.TEMPORARY_NUMBER_OF_PAGES.length;*/ //Temporary Number of Pages in Viepager
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
