package com.bitgig.bitgig.util;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by irvin on 2/7/2015.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    GestureDetector mGestureDetector;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if(childView != null && mListener != null && mGestureDetector.onTouchEvent(motionEvent)){
            mListener.onItemClick(childView, recyclerView.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }
}

