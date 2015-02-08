package com.bitgig.bitgig.ui.maps;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.bitgig.bitgig.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by irvin on 2/8/2015.
 */
public class MyMapFragment extends com.google.android.gms.maps.MapFragment implements GoogleMap.OnMarkerClickListener {
    // Markers stored by id
    private HashMap<String, MarkerModel> mMarkers = new HashMap<String, MarkerModel>();
    private GoogleMap mMap;

    // Cached size of view
    private int mWidth, mHeight;
//    private MapInfoWindowAdapter mInfoAdapter;




    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* [ANALYTICS:SCREEN]
         * TRIGGER:   View the Map screen.
         * LABEL:     'Map'
         * [/ANALYTICS]
         */
/*        AnalyticsManager.sendScreenView(SCREEN_LABEL);

        // get DPI
        mDPI = getActivity().getResources().getDisplayMetrics().densityDpi / 160f;

        // setup the query handler to populate info windows
        mQueryHandler = createInfowindowHandler(getActivity().getContentResolver());

        mIconGenerator = new IconGenerator(getActivity());*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);

        layout.addView(mapView, 0);

        // get the height and width of the view
        mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {
                        final View v = getView();
                        mHeight = v.getHeight();
                        mWidth = v.getWidth();

                        // also requires width and height
//                        enableMapElements();

                        if (v.getViewTreeObserver().isAlive()) {
                            // remove this layout listener
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                v.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                v.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                        }
                    }
                }
        );

        clearMap();

        if (mMap == null) {
            setupMap(true);
        }

/*        setMapInsets(mMapInsets);

        // load all markers
        LoaderManager lm = getLoaderManager();
        lm.initLoader(MarkerQuery._TOKEN, null, this);

        // load the tile overlays
        lm.initLoader(OverlayQuery._TOKEN, null, this);*/

        return v;
    }


    /**
     * Clears the map and initialises all map variables that hold markers and overlays.
     */
    private void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }

        // Clear all map elements
/*        mTileProviders.clear();
        mTileOverlays.clear();

        mMarkers.clear();
        mMarkersFloor.clear();

        mFloor = INVALID_FLOOR;*/
    }


    private void setupMap(boolean resetCamera) {
  /*      mInfoAdapter = new MapInfoWindowAdapter(LayoutInflater.from(getActivity()), getResources(),
                mMarkers);*/
        mMap = getMap();

        // Add a Marker for Moscone
/*        mMosconeMaker = mMap.addMarker(MapUtils.createMosconeMarker(mIconGenerator,
                MOSCONE, getActivity()).visible(false));*/

        mMap.setOnMarkerClickListener(this);
//        mMap.setOnInfoWindowClickListener(this);
//        mMap.setOnIndoorStateChangeListener(this);
//        mMap.setOnMapLoadedCallback(this);
//        mMap.setInfoWindowAdapter(mInfoAdapter);

        /*
        if (resetCamera) {
            // Move camera directly to Moscone
            centerOnMoscone(false);
        }

        mMap.setIndoorEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMyLocationEnabled(false);

        Bundle data = getArguments();
        if (data != null && data.containsKey(BaseMapActivity.EXTRA_ROOM)) {
            mHighlightedRoom = data.getString(BaseMapActivity.EXTRA_ROOM);
        }

        LOGD(TAG, "Map setup complete.");*/
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        final String snippet = marker.getSnippet();
        final String title = marker.getTitle();
/*
        // Log clicks on session and partner markers
        if (TYPE_SESSION.equals(snippet) || TYPE_PARTNER.equals(snippet)) {
            *//* [ANALYTICS:EVENT]
             * TRIGGER:   Click on a marker on the map.
             * CATEGORY:  'Map'
             * ACTION:    'markerclick'
             * LABEL:     marker ID (for example room UUID or partner marker id)
             * [/ANALYTICS]
             *//*
            AnalyticsManager.sendEvent("Map", "markerclick", title, 0L);
        }

        if(marker.equals(mMosconeMaker)){
            // Return camera to Moscone
            LOGD(TAG, "Clicked on Moscone marker, return to initial display.");
            centerOnMoscone(true);
        } else if (TYPE_SESSION.equals(snippet)) {
            final long time = UIUtils.getCurrentTime(getActivity());
            Uri uri = ScheduleContract.Sessions.buildSessionsInRoomAfterUri(title, time);
            final String order = ScheduleContract.Sessions.SESSION_START + " ASC";

            mQueryHandler.startQuery(SessionAfterQuery._TOKEN, title, uri,
                    SessionAfterQuery.PROJECTION, null, null, order);
        } else if (TYPE_PARTNER.equals(snippet)) {
            mCallbacks.onShowPartners();
        } else if (TYPE_PLAIN_SESSION.equals(snippet)) {
            // Show a basic info window with a title only
            marker.showInfoWindow();
        }
        // ignore other markers

        //centerMap(marker.getPosition());*/
        return true;
    }


    /**
     * A structure to store information about a Marker.
     */
    public static class MarkerModel {
        String id;
        int floor;
        String type;
        String label;
        Marker marker;

        public MarkerModel(String id, int floor, String type, String label, Marker marker) {
            this.id = id;
            this.floor = floor;
            this.type = type;
            this.label = label;
            this.marker = marker;
        }
    }
}