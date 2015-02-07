package com.bitgig.bitgig.common;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bitgig.bitgig.MainActivity;
import com.bitgig.bitgig.R;
import com.bitgig.bitgig.ui.widget.ScrimInsetsScrollView;
import com.bitgig.bitgig.ui.widget.SuperSwipeRefreshLayout;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.util.ArrayList;

/**
 * Created by irvin on 2/7/2015.
 */
public abstract class BaseActivity extends ActionBarActivity {

    // Navigation drawer:
    private DrawerLayout mDrawerLayout;
    private ObjectAnimator mStatusBarColorAnimator;
    private LinearLayout mAccountListContainer;
    private ViewGroup mDrawerItemsListContainer;
    private FrameLayout mFrameLayout;
    private Handler mHandler;

    private ImageView mExpandAccountBoxIndicator;
    private boolean mAccountBoxExpanded = false;

    // Helper methods for L APIs
    //private LUtils mLUtils;

    // When set, these components will be shown/hidden in sync with the action bar
    // to implement the "quick recall" effect (the Action Bar and the header views disappear
    // when you scroll down a list, and reappear quickly when you scroll up).
    private ArrayList<View> mHideableHeaderViews = new ArrayList<View>();

    // Durations for certain animations we use:
    private static final int HEADER_HIDE_ANIM_DURATION = 300;
    private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;

    // symbols for navdrawer items (indices must correspond to array below). This is
    // not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.
    protected static final int NAVDRAWER_ITEM_WALLET = 0;
    protected static final int NAVDRAWER_ITEM_EXPLORE = 1;
    protected static final int NAVDRAWER_ITEM_MAP = 2;
    protected static final int NAVDRAWER_ITEM_SOCIAL = 3;
    protected static final int NAVDRAWER_ITEM_VIDEO_LIBRARY = 4;
    protected static final int NAVDRAWER_ITEM_SIGN_IN = 5;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 6;
    protected static final int NAVDRAWER_ITEM_EXPERTS_DIRECTORY = 7;
    protected static final int NAVDRAWER_ITEM_PEOPLE_IVE_MET = 8;
    protected static final int NAVDRAWER_ITEM_FACEBOOK_CONTAINER = 9;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;

    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_wallet,
            R.string.navdrawer_marketplace,
            R.string.navdrawer_world,
            R.string.navdrawer_gigs,
            R.string.navdrawer_history,
            R.string.navdrawer_cart,
            R.string.navdrawer_share,
            R.string.navdrawer_setting,
            R.string.navdrawer_about
    };

    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.wallet_dark,  // Wallet
            R.drawable.ic_bitcoin_trolley,  //
            R.drawable.ic_drawer_map, //
            R.drawable.ic_drawer_social, //
            R.drawable.ic_drawer_video_library, //
            0, //
            R.drawable.handshake_dark,
            R.drawable.ic_drawer_social,
            R.drawable.ic_drawer_settings,
            R.drawable.ic_action_about_light_theme,
    };

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;

    // SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    // variables that control the Action Bar auto hide behavior (aka "quick recall")
    private boolean mActionBarAutoHideEnabled = false;
    private int mActionBarAutoHideSensivity = 0;
    private int mActionBarAutoHideMinY = 0;
    private int mActionBarAutoHideSignal = 0;
    private boolean mActionBarShown = true;

    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;

    private int mThemedStatusBarColor;
    private int mNormalStatusBarColor;
    private int mProgressBarTopWhenActionBarShown;
    private static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();
    /////////private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*    AnalyticsManager.initializeAnalyticsTracker(getApplicationContext());
            RecentTasksStyler.styleRecentTasksEntry(this);

            PrefUtils.init(this);
            //TODO: 1.  Check if User has accepted terms and has signed up, we can save in PrefUtils
            //TODO:     If user is already signed skip Log in page
            if (!PrefUtils.isTosAccepted(this)) {
                Intent intent = new Intent(this, Signup.class);
                startActivity(intent);
                finish();
            }

            mImageLoader = new ImageLoader(this);
            mHandler = new Handler();*/

        // Enable or disable each Activity depending on the form factor. This is necessary
        // because this app uses many implicit intents where we don't name the exact Activity
        // in the Intent, so there should only be one enabled Activity that handles each
        // Intent in the app.
        ///UIUtils.enableDisableActivitiesByFormFactor(this);
/*
            if (savedInstanceState == null) {
                registerGCMClient();
            }*/

            /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            sp.registerOnSharedPreferenceChangeListener(this);*/

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        //mLUtils = LUtils.getInstance(this);
        mThemedStatusBarColor = getResources().getColor(R.color.theme_primary_dark);
        mNormalStatusBarColor = mThemedStatusBarColor;
    }


    private void trySetupSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //TODO: Request MORE DATA!
                    //requestDataRefresh();
                }
            });

            if (mSwipeRefreshLayout instanceof SuperSwipeRefreshLayout) {
                SuperSwipeRefreshLayout sswrl = (SuperSwipeRefreshLayout) mSwipeRefreshLayout;
                // TODO: Make THI
                sswrl.setCanChildScrollUpCallback(new SuperSwipeRefreshLayout.CanChildScrollUpCallback() {
                    @Override
                    public boolean canSwipeRefreshChildScrollUp() {
                        return false;
                    }
                });
            }
        }
    }

    protected void setProgressBarTopWhenActionBarShown(int progressBarTopWhenActionBarShown) {
        mProgressBarTopWhenActionBarShown = progressBarTopWhenActionBarShown;
        updateSwipeRefreshProgressBarTop();
    }

    private void updateSwipeRefreshProgressBarTop() {
        if (mSwipeRefreshLayout == null) {
            return;
        }

        int progressBarStartMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_start_margin);
        int progressBarEndMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_end_margin);
        int top = mActionBarShown ? mProgressBarTopWhenActionBarShown : 0;
        mSwipeRefreshLayout.setProgressViewOffset(false,
                top + progressBarStartMargin, top + progressBarEndMargin);
    }

    /**
     * Initializes the Action Bar auto-hide (aka Quick Recall) effect.
     */
    private void initActionBarAutoHide() {
        mActionBarAutoHideEnabled = true;
        mActionBarAutoHideMinY = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_min_y);
        mActionBarAutoHideSensivity = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_sensivity);
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    /**
     * Sets up the navigation drawer as appropriate.
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();
        //TODO: SHould have to specifically declare the View
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            Log.d("BASEACTIVITY:::::::::: ", "DrawerLayout is NULL");
            return;
        }
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.theme_primary_dark));
        ScrimInsetsScrollView navDrawer = (ScrimInsetsScrollView)
                mDrawerLayout.findViewById(R.id.navdrawer);
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            Log.d("BASEACTIVITY:::::::::: ", "getSelfNavDrawerItem is NAVDRAWER_ITEM_INVALID... setting DrawerLayout NULL");
            mDrawerLayout = null;
            return;
        }

        if (navDrawer != null) {
            final View chosenAccountContentView = findViewById(R.id.chosen_account_content_view);
            final View chosenAccountView = findViewById(R.id.chosen_account_view);
            final int navDrawerChosenAccountHeight = getResources().getDimensionPixelSize(
                    R.dimen.navdrawer_chosen_account_height);
            navDrawer.setOnInsetsCallback(new ScrimInsetsScrollView.OnInsetsCallback() {
                @Override
                public void onInsetsChanged(Rect insets) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                            chosenAccountContentView.getLayoutParams();
                    lp.topMargin = insets.top;
                    chosenAccountContentView.setLayoutParams(lp);

                    ViewGroup.LayoutParams lp2 = chosenAccountView.getLayoutParams();
                    lp2.height = navDrawerChosenAccountHeight + insets.top;
                    chosenAccountView.setLayoutParams(lp2);
                }
            });
        }

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            });
        }

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
                if (mAccountBoxExpanded) {
                    mAccountBoxExpanded = false;
                    ////////setupAccountBoxToggle();
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

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        // populate the nav drawer with the correct items
        populateNavDrawer();

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        // TODO: FIX THIS TO MAKE DRAWER LAND OPEN AT THE TIME THE APP STARTS
        //if (!PrefUtils.isWelcomeDone(this)) {
        if (true) {
            // first run of the app starts with the nav drawer open
            //PrefUtils.markWelcomeDone(this);*/
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    // Subclasses can override this for custom behavior
    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        if (mActionBarAutoHideEnabled && isOpen) {
            autoShowOrHideActionBar(true);
        }
    }

    protected void onNavDrawerSlide(float offset) {
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    /**
     * Populates the navigation drawer with the appropriate items.
     */
    //TODO: Fix this to appropriately populate the NavDrawer
    private void populateNavDrawer() {
            /*boolean attendeeAtVenue = PrefUtils.isAttendeeAtVenue(this);*/

        mNavDrawerItems.clear();

        // decide which items will appear in the nav drawer
            /*if (AccountUtils.hasActiveAccount(this)) {*/
        mNavDrawerItems.add(NAVDRAWER_ITEM_WALLET);
            /*} else {
                // If no active account, show Sign In
                mNavDrawerItems.add(NAVDRAWER_ITEM_SIGN_IN);
            }*/

        // Explore is always shown
        mNavDrawerItems.add(NAVDRAWER_ITEM_EXPLORE);
    /*
            //TODO: Maybe if there is more than 3 gigs around the area, show Map on the nav drawer
            if (attendeeAtVenue) {
                mNavDrawerItems.add(NAVDRAWER_ITEM_MAP);
            }*/
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

        // Other items that are always in the nav drawer
        mNavDrawerItems.add(NAVDRAWER_ITEM_SOCIAL);
        mNavDrawerItems.add(NAVDRAWER_ITEM_VIDEO_LIBRARY);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SETTINGS);

        createNavDrawerItems();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            Log.d("BASEACTIVITY::::::::", "DrawerListContainer is NULLLLLLLLLL");
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    /**
     * TODO: Google says...
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }


    //TODO: This Must be overriden in the parent Activity
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        //setupAccountBox();

        trySetupSwipeRefresh();
        updateSwipeRefreshProgressBarTop();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            //LOGW(TAG, "No view with ID main_content to fade in.");
            Log.d("BASEACTIVITY::::: ", "No view with ID main_content to fade in.");
        }
    }

    //TODO: FIX THIS TO SHOW THE ACCOUNT ON BIWALK

    /**
     * Sets up the account box. The account box is the area at the top of the nav drawer that
     * shows which account the user is logged in as, and lets them switch accounts.
     */
        /*private void setupAccountBox() {
            mAccountListContainer = (LinearLayout) findViewById(R.id.account_list);

            if (mAccountListContainer == null) {
                //This activity does not have an account box
                return;
            }

            final View chosenAccountView = findViewById(R.id.chosen_account_view);
            Account chosenAccount = AccountUtils.getActiveAccount(this);
            if (chosenAccount == null) {
                // No account logged in; hide account box
                chosenAccountView.setVisibility(View.GONE);
                mAccountListContainer.setVisibility(View.GONE);
                return;
            } else {
                chosenAccountView.setVisibility(View.VISIBLE);
                mAccountListContainer.setVisibility(View.INVISIBLE);
            }

            AccountManager am = AccountManager.get(this);
            Account[] accountArray = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            List<Account> accounts = new ArrayList<Account>(Arrays.asList(accountArray));
            accounts.remove(chosenAccount);

            ImageView coverImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_cover_image);
            ImageView profileImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_image);
            TextView nameTextView = (TextView) chosenAccountView.findViewById(R.id.profile_name_text);
            TextView email = (TextView) chosenAccountView.findViewById(R.id.profile_email_text);
            mExpandAccountBoxIndicator = (ImageView) findViewById(R.id.expand_account_box_indicator);

            String name = AccountUtils.getPlusName(this);
            if (name == null) {
                nameTextView.setVisibility(View.GONE);
            } else {
                nameTextView.setVisibility(View.VISIBLE);
                nameTextView.setText(name);
            }

            String imageUrl = AccountUtils.getPlusImageUrl(this);
            if (imageUrl != null) {
                mImageLoader.loadImage(imageUrl, profileImageView);
            }

            String coverImageUrl = AccountUtils.getPlusCoverUrl(this);
            if (coverImageUrl != null) {
                mImageLoader.loadImage(coverImageUrl, coverImageView);
            } else {
                coverImageView.setImageResource(R.drawable.default_cover);
            }

            email.setText(chosenAccount.name);

            if (accounts.isEmpty()) {
                // There's only one account on the device, so no need for a switcher.
                mExpandAccountBoxIndicator.setVisibility(View.GONE);
                mAccountListContainer.setVisibility(View.GONE);
                chosenAccountView.setEnabled(false);
                return;
            }

            chosenAccountView.setEnabled(true);

            mExpandAccountBoxIndicator.setVisibility(View.VISIBLE);
            chosenAccountView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAccountBoxExpanded = !mAccountBoxExpanded;
                    setupAccountBoxToggle();
                }
            });
            setupAccountBoxToggle();

            populateAccountList(accounts);
        }

        private void populateAccountList(List<Account> accounts) {
            mAccountListContainer.removeAllViews();

            LayoutInflater layoutInflater = LayoutInflater.from(this);
            for (Account account : accounts) {
                View itemView = layoutInflater.inflate(R.layout.list_item_account,
                        mAccountListContainer, false);
                ((TextView) itemView.findViewById(R.id.profile_email_text))
                        .setText(account.name);
                final String accountName = account.name;
                String imageUrl = AccountUtils.getPlusImageUrl(this, accountName);
                if (!TextUtils.isEmpty(imageUrl)) {
                    mImageLoader.loadImage(imageUrl,
                            (ImageView) itemView.findViewById(R.id.profile_image));
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConnectivityManager cm = (ConnectivityManager)
                                getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        if (activeNetwork == null || !activeNetwork.isConnected()) {
                            // if there's no network, don't try to change the selected account
                            Toast.makeText(BaseActivity.this, R.string.no_connection_cant_login,
                                    Toast.LENGTH_SHORT).show();
                            mDrawerLayout.closeDrawer(Gravity.START);
                            return;
                        } else {
                            LOGD(TAG, "User requested switch to account: " + accountName);
                            AccountUtils.setActiveAccount(BaseActivity.this, accountName);
                            onAccountChangeRequested();
                            startLoginProcess();
                            mAccountBoxExpanded = false;
                            setupAccountBoxToggle();
                            mDrawerLayout.closeDrawer(Gravity.START);
                            setupAccountBox();
                        }
                    }
                });
                mAccountListContainer.addView(itemView);
            }
        }

        protected void onAccountChangeRequested() {
            // override if you want to be notified when another account has been selected account has changed
        }

        private void setupAccountBoxToggle() {
            int selfItem = getSelfNavDrawerItem();
            if (mDrawerLayout == null || selfItem == NAVDRAWER_ITEM_INVALID) {
                // this Activity does not have a nav drawer
                return;
            }
            mExpandAccountBoxIndicator.setImageResource(mAccountBoxExpanded
                    ? R.drawable.ic_drawer_accounts_collapse
                    : R.drawable.ic_drawer_accounts_expand);
            int hideTranslateY = -mAccountListContainer.getHeight() / 4; // last 25% of animation
            if (mAccountBoxExpanded && mAccountListContainer.getTranslationY() == 0) {
                // initial setup
                mAccountListContainer.setAlpha(0);
                mAccountListContainer.setTranslationY(hideTranslateY);
            }

            AnimatorSet set = new AnimatorSet();
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDrawerItemsListContainer.setVisibility(mAccountBoxExpanded
                            ? View.INVISIBLE : View.VISIBLE);
                    mAccountListContainer.setVisibility(mAccountBoxExpanded
                            ? View.VISIBLE : View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    onAnimationEnd(animation);
                }
            });

            if (mAccountBoxExpanded) {
                mAccountListContainer.setVisibility(View.VISIBLE);
                AnimatorSet subSet = new AnimatorSet();
                subSet.playTogether(
                        ObjectAnimator.ofFloat(mAccountListContainer, View.ALPHA, 1)
                                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
                        ObjectAnimator.ofFloat(mAccountListContainer, View.TRANSLATION_Y, 0)
                                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
                set.playSequentially(
                        ObjectAnimator.ofFloat(mDrawerItemsListContainer, View.ALPHA, 0)
                                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
                        subSet);
                set.start();
            } else {
                mDrawerItemsListContainer.setVisibility(View.VISIBLE);
                AnimatorSet subSet = new AnimatorSet();
                subSet.playTogether(
                        ObjectAnimator.ofFloat(mAccountListContainer, View.ALPHA, 0)
                                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
                        ObjectAnimator.ofFloat(mAccountListContainer, View.TRANSLATION_Y,
                                hideTranslateY)
                                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
                set.playSequentially(
                        subSet,
                        ObjectAnimator.ofFloat(mDrawerItemsListContainer, View.ALPHA, 1)
                                .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
                set.start();
            }

            set.start();
        }*/


    //TODO: POPULATE TO Launch SPECIFIC Activities
    private void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_WALLET:
                //intent = new Intent(this, MyScheduleActivity.class);
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_EXPLORE:
                    /*intent = new Intent(this, BrowseSessionsActivity.class);
                    startActivity(intent);
                    finish();*/
                break;
            case NAVDRAWER_ITEM_MAP:
                    /*intent = new Intent(this, UIUtils.getMapActivityClass(this));
                    startActivity(intent);
                    finish();*/
                break;
            case NAVDRAWER_ITEM_SOCIAL:
                    /*intent = new Intent(this, SocialActivity.class);
                    startActivity(intent);
                    finish();*/
                break;
            case NAVDRAWER_ITEM_EXPERTS_DIRECTORY:
                    /*intent = new Intent(this, ExpertsDirectoryActivity.class);
                    startActivity(intent);
                    finish();*/
                break;
            case NAVDRAWER_ITEM_PEOPLE_IVE_MET:
                    /*intent = new Intent(this, PeopleIveMetActivity.class);
                    startActivity(intent);
                    finish();*/
                break;
            case NAVDRAWER_ITEM_SIGN_IN:
                    /*signInOrCreateAnAccount();*/
                break;
            case NAVDRAWER_ITEM_SETTINGS:
                    /*intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);*/
                break;
            case NAVDRAWER_ITEM_VIDEO_LIBRARY:
                    /*intent = new Intent(this, VideoLibraryActivity.class);
                    startActivity(intent);
                    finish();*/
                break;
        }
    }

    //TODO: FIX TO MAKE IT A WAY TO SIGN IN
    private void signInOrCreateAnAccount() {
        //Get list of accounts on device.
        AccountManager am = AccountManager.get(BaseActivity.this);
        Account[] accountArray = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        if (accountArray.length == 0) {
            //Send the user to the "Add Account" page.
                /*Intent intent = new Intent(Settings.ACTION_ADD_ACCOUNT);
                intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[] {"com.google"});
                startActivity(intent);*/
        } else {
            //Try to log the user in with the first account on the device.
                /*startLoginProcess();
                mDrawerLayout.closeDrawer(Gravity.START);*/
        }
    }


    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        if (isSpecialItem(itemId)) {
            goToNavDrawerItem(itemId);
        } else {
            // launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNavDrawerItem(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);
            // fade out the main content
            View mainContent = findViewById(R.id.main_content);
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        }

        mDrawerLayout.closeDrawer(Gravity.START);
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == NAVDRAWER_ITEM_SETTINGS;
    }

    private boolean isSeparator(int itemId) {
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        if (selected) {
            view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
        }

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    /*        if (mGCMRegisterTask != null) {
                LOGD(TAG, "Cancelling GCM registration task.");
                mGCMRegisterTask.cancel(true);
            }

            try {
                GCMRegistrar.onDestroy(this);
            } catch (Exception e) {
                LOGW(TAG, "C2DM unregistration error", e);
            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            sp.unregisterOnSharedPreferenceChangeListener(this);*/
    }


    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    protected void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
        onActionBarAutoShowOrHide(show);
    }


    protected void enableActionBarAutoHide(final ListView listView) {
        initActionBarAutoHide();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            final static int ITEMS_THRESHOLD = 3;
            int lastFvi = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    /*onMainContentScrolled(firstVisibleItem <= ITEMS_THRESHOLD ? 0 : Integer.MAX_VALUE,
                            lastFvi - firstVisibleItem > 0 ? Integer.MIN_VALUE :
                                    lastFvi == firstVisibleItem ? 0 : Integer.MAX_VALUE
                    );
                    lastFvi = firstVisibleItem;*/
            }
        });
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
            //UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        // set icon and text
        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }


    protected void onActionBarAutoShowOrHide(boolean shown) {
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
        }
        mStatusBarColorAnimator = ObjectAnimator.ofInt(
                    /*(mDrawerLayout != null) ? mDrawerLayout : mLUtils,*/
                (mDrawerLayout != null) ? mDrawerLayout : null,
                (mDrawerLayout != null) ? "statusBarBackgroundColor" : "statusBarColor",
                shown ? Color.BLACK : mNormalStatusBarColor,
                shown ? mNormalStatusBarColor : Color.BLACK)
                .setDuration(250);
        if (mDrawerLayout != null) {
            mStatusBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewCompat.postInvalidateOnAnimation(mDrawerLayout);
                }
            });
        }
        mStatusBarColorAnimator.setEvaluator(ARGB_EVALUATOR);
        mStatusBarColorAnimator.start();

            /*updateSwipeRefreshProgressBarTop();*/

        for (View view : mHideableHeaderViews) {
            if (shown) {
                view.animate()
                        .translationY(0)
                        .alpha(1)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            } else {
                view.animate()
                        .translationY(-view.getBottom())
                        .alpha(0)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            }
        }
    }


    protected void onRefreshingStateChanged(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    protected void enableDisableSwipeRefresh(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

}