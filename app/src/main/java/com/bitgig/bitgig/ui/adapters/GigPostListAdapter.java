package com.bitgig.bitgig.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bitgig.bitgig.R;
import com.bitgig.bitgig.model.GigPost;

import java.util.ArrayList;

/**
 * Created by irvin on 1/10/2015.
 */
public class GigPostListAdapter implements ListAdapter, AbsListView.RecyclerListener {

    private final Context mContext;

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;

    // List of items served by this adapter
    ArrayList<GigPost> mPosts = new ArrayList<GigPost>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();

    int mDefaultSessionColor;
    int mDefaultStartTimeColor;
    int mDefaultEndTimeColor;

    public GigPostListAdapter(Context context, ArrayList<GigPost> postList){
        this.mContext = context;
        this.mPosts = postList;


        mDefaultSessionColor = mContext.getResources().getColor(R.color.default_session_color);
        mDefaultStartTimeColor = mContext.getResources().getColor(R.color.body_text_2);
        mDefaultEndTimeColor = mContext.getResources().getColor(R.color.body_text_3);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    public void setContentTopClearance(int padding) {
        mContentTopClearance = padding;
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return position >= 0 && position < mPosts.size() ? mPosts.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private static final String GIG_VIEW_TAG = "GigPostListAdapter_GIG_VIEW_TAG";

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Resources res = mContext.getResources();
        int layoutResId = R.layout.feed_item_layout;

        TextView title;
        TextView username;
        TextView btc;
        TextView loc;

        if(view == null) {
            view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(layoutResId, parent, false);

            //TODO: Add on Click EVENTS;

        }
        if (position < 0 || position >= mPosts.size()) {
            //LOGE(TAG, "Invalid view position passed to MyScheduleAdapter: " + position);
            return view;
        }
        final GigPost gig = mPosts.get(position);
        title = (TextView)view.findViewById(R.id.txtStatusMsg);
        username = (TextView)view.findViewById(R.id.username);
        btc = (TextView)view.findViewById(R.id.btc);
        loc = (TextView)view.findViewById(R.id.location);
        btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosts.remove(position);
            }
        });
        return view;
    }


    //TODO: Implement this to ditinguish between GIG POST (EMPLOYER) and FREELANCER
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mPosts.isEmpty();
    }

    @Override
    public void onMovedToScrapHeap(View view) {

    }
}
