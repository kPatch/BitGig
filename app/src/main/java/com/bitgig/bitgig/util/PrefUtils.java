package com.bitgig.bitgig.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by irvin on 2/7/2015.
 */
public class PrefUtils {

    private static final String TAG = "PrefUtils";


    /** Boolean indicating whether Terms of Service (ToS) has been accepted */
    public static final String PREF_TOS_ACCEPTED = "pref_tos_accepted";


    /**Boolean indicating whether we performed the (one-time) welcome flow.*/
    public static final String PREF_WELCOME_DONE = "pref_welcome_done";


    //TODO: STEP: Store user values here momentarily
    public static void init(final Context context) {
        // Check what year we're configured for
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        /*int conferenceYear = sp.getInt(PREF_CONFERENCE_YEAR, 0);
        if (conferenceYear != Config.CONFERENCE_YEAR) {
            LOGD(TAG, "App not yet set up for " + PREF_CONFERENCE_YEAR + ". Resetting data.");
            // Application is configured for a different conference year. Reset preferences.
            sp.edit().clear().putInt(PREF_CONFERENCE_YEAR, Config.CONFERENCE_YEAR).commit();
        }*/
    }


    public static boolean isTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_TOS_ACCEPTED, false);
    }

    public static void markTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_TOS_ACCEPTED, true).commit();
    }



    public static boolean isWelcomeDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_WELCOME_DONE, false);
    }

    public static void markWelcomeDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_WELCOME_DONE, true).commit();
    }
}
