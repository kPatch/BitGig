package com.bitgig.bitgig.util;

import android.content.Context;
import android.util.Log;

public class FacebookManager {
    private static FacebookManager instance = null;
    private Context context;
    private String id = null;
    private com.facebook.widget.ProfilePictureView profilePictureView = null;

    private FacebookManager(){}
    public static FacebookManager getInstance() {
        if(instance == null)
            instance = new FacebookManager();
        return instance;
    }



    public void setContext(Context context) { this.context = context; }

    public String getId() {
        return id;
    }

    public com.facebook.widget.ProfilePictureView getProfilePictureView() {
        return profilePictureView;
    }

    public void setId(String id) {
        this.id = id;
        profilePictureView = new com.facebook.widget.ProfilePictureView(context);
        profilePictureView.setProfileId(id);
        Log.d("set","set");
    }
}