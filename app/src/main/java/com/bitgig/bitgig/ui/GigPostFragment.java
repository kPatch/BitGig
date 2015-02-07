package com.bitgig.bitgig.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitgig.bitgig.R;
import com.bitgig.bitgig.model.GigPost;
import com.bitgig.bitgig.ui.adapters.GigPostRecAdapter;
import com.bitgig.bitgig.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irvin on 2/7/2015.
 */
public class GigPostFragment extends Fragment {
    private String mContentDescription = null;
    private View mRoot = null;
    private RecyclerView mRecycler;
    private GigPostRecAdapter mAdapter;
    private Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public interface Listener {
        //public void onFragmentViewCreated(ListFragment fragment);
        public void onFragmentViewCreated(RecyclerView mRecycler);
        public void onFragmentAttached(GigPostFragment fragment);
        public void onFragmentDetached(GigPostFragment fragment);
    }

    public static GigPostFragment newInstance(String param1, String param2) {
        GigPostFragment fragment = new GigPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     * (e.g. upon screen orientation changes).*/
    public GigPostFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_my_schedule, container, false);
        context = getActivity();
        final FragmentActivity c = (FragmentActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);

        mAdapter = new GigPostRecAdapter(createList(20));

        mRecycler = (RecyclerView) mRoot.findViewById(R.id.recyclerViewList);
        mRecycler.setLayoutManager(layoutManager);
        if (mContentDescription != null) {
            mRoot.setContentDescription(mContentDescription);
        }

        mRecycler.setAdapter(mAdapter); //TODO BAD!!! WE NEED TO SET THE ADAPTER FROM the MainActivity via a CallBack

        mRecycler.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                FragmentManager fm = getChildFragmentManager();
                GigPostDialogFragment dialogFragment = new GigPostDialogFragment();
                dialogFragment.show(fm, "GigPostDialogFragment");
            }
        })
        );

        return mRoot;
    }

    public void setContentDescription(String desc) {
        mContentDescription = desc;
        if (mRoot != null) {
            mRoot.setContentDescription(mContentDescription);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof Listener) {
            //((Listener) getActivity()).onFragmentViewCreated(this);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentAttached(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentDetached(this);
        }
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
}
