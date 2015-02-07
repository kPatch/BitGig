package com.bitgig.bitgig;

import android.app.Application;
import android.util.Log;

import com.bitgig.bitgig.model.GigParse;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(GigParse.class);
        Parse.initialize(this, "ohGBSlhgpCtPxv5f0ARrPuG2cfvS2vC2RkEWUSy6", "0Fiy3uB7WEsa7FWMvYvNyrVl7X68OiYD9bnCwm1z"); // Your Application ID and Client Key are defined elsewhere

        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}