package com.bitgig.bitgig.model;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by anish_khattar25 on 2/7/15.
 */
public class RequestModel {

    private Context appContext;
    private FBInfo fbInfo;

    public RequestModel(Context context) {
        this.appContext = context;
        this.fbInfo = new FBInfo();
    }


    public void sendGigRequest(String gigName, String gigDescription, String moneyString, int durationMin,double latitude, double longitude){
        ParseObject newGigRequest = new ParseObject("GigRequests");
        ParseGeoPoint point = new ParseGeoPoint(latitude,longitude);
        new ParseGeoPoint();
        newGigRequest.put("RequesterName",fbInfo.getName());
        newGigRequest.put("RequesterFBId",fbInfo.getFBid());
        newGigRequest.put("RequesterEmail",fbInfo.getEmail());
        newGigRequest.put("gigName",gigName);
        newGigRequest.put("gigDescription",gigDescription);
        newGigRequest.put("moneyAmount",moneyString);
        newGigRequest.put("requestDur",durationMin);
        newGigRequest.put("reqLocation",point);
        newGigRequest.saveInBackground();

    }

    public void getAllRequestsInRadius(int radius, double currentLat,double currentLong){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GigRequests");
        ParseGeoPoint userLocation = new ParseGeoPoint(currentLat,currentLong);
        query.whereWithinMiles("reqLocation",userLocation,radius);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> requestList, ParseException e) {
                if (e == null) {
                    Log.v("Parse Object", "Retrieved " + requestList.size() + " requests");
                    for(ParseObject po : requestList){
                        printParseObject(po);
                        Date requestDate = po.getCreatedAt();
                        int requestLim = po.getInt("requestDur") * 1000;
                        Date currDate = new Date();
                        if(requestDate.getTime() + requestLim > currDate.getTime()){
                            po.deleteInBackground();
                        }
                    }
                }
            }
        });
    }

    public void getAllRequests(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GigRequests");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> requestList, ParseException e) {
                if (e == null) {
                    Log.v("Parse Object", "Retrieved " + requestList.size() + " requests");
                    for(ParseObject po : requestList){
                        printParseObject(po);
                    }
                }
            }
        });
    }

    private void printParseObject(ParseObject po){
        String requestStr = "Requester Name: " + po.get("RequesterName")  +  " Gig Name: " + po.get("gigName");
        Log.v("Parse Object",requestStr);
    }




}