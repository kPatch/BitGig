package com.bitgig.bitgig.model;

import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anish_khattar25 on 2/7/15.
 */
public class FBInfo {

    ParseUser currentUser;


    public FBInfo() {
        currentUser = ParseUser.getCurrentUser();


    }

    public String getFBid(){
        JSONObject userProfile = currentUser.getJSONObject("profile");
        try {
            return userProfile.getString("facebookId");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName(){
        JSONObject userProfile = currentUser.getJSONObject("profile");
        try {
            return userProfile.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getEmail(){
        JSONObject userProfile = currentUser.getJSONObject("profile");
        try {
            return userProfile.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}