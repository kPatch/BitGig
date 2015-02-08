package com.bitgig.bitgig.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bitgig.bitgig.R;
import com.bitgig.bitgig.model.GigPost;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by irvin on 1/11/2015.
 */
public class GigPostRecAdapter extends RecyclerView.Adapter<GigPostRecAdapter.GigPostViewHolder> {
    private ArrayList<GigPost> gigList;
    Random random = new Random();
    public GigPostRecAdapter(ArrayList<GigPost> gigList) {
        this.gigList = gigList;
    }

    @Override
    public int getItemCount() {
        return gigList.size();
    }

    public void add(GigPost gig){
        gigList.add(0, gig);
/*
        notifyItemInserted(0);
*/
/*        gigList.add(gig);
       gigList.add(2, gig);
        notifyItemInserted(2);
        Log.d("=============", "==========");*/
    }

    public void updateItems(ArrayList<GigPost> items) {
        //mItems.clear();
        if (items != null) {
            for (GigPost item : items) {
                gigList.add((GigPost) item.clone());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(GigPostViewHolder contactViewHolder, int i) {
        /*int ran = random.nextInt(4);
        String status = "";
        String price = "";
        String name = "";
        switch (ran){
            case 0:
                status = "BRING ME A SODA!  @Miami Bitcoin Hackathon!";
                price = "0.12";
                name = "Uwece";
                break;
            case 1:
                status = "Take a Photo of TACO for me";
                price = "0.50";
                name = "Shao";
                break;
            case 2:
                status = "I need a ride to school";
                price = "5.00";
                name = "Anonymous";
                break;
            case 3:
                status = "A horse walking into a bar, the bartender says...";
                price = "0.20";
                name = "Irvss";
                break;
        }
        contactViewHolder.title.setText(gig.status);
        contactViewHolder.username.setText(name);
        //contactViewHolder.btc.setText(Btc.satoshiToBtc(gig.getBtc()));
        contactViewHolder.btc.setText(price);
        contactViewHolder.loc.setText(gig.getLoc());
        */
        GigPost gig = gigList.get(i);
        contactViewHolder.title.setText(gig.getTitle());
        contactViewHolder.username.setText(gig.getUserName());
        //contactViewHolder.btc.setText(Btc.satoshiToBtc(gig.getBtc()));
        contactViewHolder.btc.setText(String.valueOf(gig.getBtc()));
        contactViewHolder.loc.setText(gig.getLoc());
    }

    @Override
    public GigPostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.feed_item_layout, viewGroup, false);

        return new GigPostViewHolder(itemView);
    }

    public class GigPostViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        protected TextView title;
        protected TextView username;
        protected TextView btc;
        protected TextView loc;

        public GigPostViewHolder( View v) {
            super(v);
            title = (TextView)v.findViewById(R.id.txtStatusMsg);
            username = (TextView)v.findViewById(R.id.username);
            btc = (TextView)v.findViewById(R.id.btc);
            loc = (TextView)v.findViewById(R.id.location);
            btc.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            gigList.remove(getPosition());
            notifyItemRemoved(getPosition());
            notifyItemRangeChanged(getPosition(), gigList.size());
        }
    }
}