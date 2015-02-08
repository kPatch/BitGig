package com.bitgig.bitgig.ui.maps;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.bitgig.bitgig.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by irvin on 2/8/2015.
 */
public class MyMapFragment extends com.google.android.gms.maps.MapFragment {
    // Markers stored by id
    private GoogleMap mMap;

    // Cached size of view
    private int mWidth, mHeight;

    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);
        layout.addView(mapView, 0);
        setUpMapIfNeeded();
        return v;
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }



    private void setUpMap() {
        Log.v("MAPS","setting up map");
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(37.228310,-80.423432))
                .radius(1000).fillColor(0x553385FF);

        LatLng latLng = new LatLng(37.228310,-80.423432);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mMap.addCircle(circleOptions);
        //getAllRequestsInRadius(10000,37.228310,-80.423432);


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
                        ParseGeoPoint geoPoint = (ParseGeoPoint)po.get("reqLocation");
                        LatLng latLng = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(po.getString("gigName")).snippet(po.getString("gigDescription")));
                    }
                }
            }
        });
    }
}