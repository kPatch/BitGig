package com.bitgig.bitgig.ui.maps;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.Toast;

import com.bitgig.bitgig.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;



/**
 * Created by irvin on 2/8/2015.
 */
public class MyMapFragment extends com.google.android.gms.maps.MapFragment {
    // Markers stored by id
    private GoogleMap mMap;
    GoogleMapOptions options;

    // Cached size of view
    private int mWidth, mHeight;

    private Location location;
    private LatLng myLocation;

    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpMap();
        centerMapOnMyLocation();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        Log.v("MAPS","setUpMapIfNeeded");
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = getMap();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                centerMapOnMyLocation();
            }
        }
        centerMapOnMyLocation();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);
        layout.addView(mapView, 0);
        setUpMapIfNeeded();

 /*       options = new GoogleMapOptions();
        options
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);*/
        setUpMap();
        centerMapOnMyLocation();
        return v;
    }


    public LatLng translateCoordinates(final double distance, final LatLng origpoint, final double angle) {
        final double distanceNorth = Math.sin(angle) * distance;
        final double distanceEast = Math.cos(angle) * distance;

        final double earthRadius = 6371000;

        final double newLat = origpoint.latitude + (distanceNorth / earthRadius) * 180 / Math.PI;
        final double newLon = origpoint.longitude + (distanceEast / (earthRadius * Math.cos(newLat * 180 / Math.PI))) * 180 / Math.PI;

        return new LatLng(newLat, newLon);
    }

    private void centerMapOnMyLocation() {
        Toast.makeText(getActivity(), "!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT);
        /*
        mMap.setMyLocationEnabled(true);
        location = mMap.getMyLocation();

        if (location != null) {
            myLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 8));
        */
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLocation)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            if (myLocation != null) {
                mMap.addMarker(new MarkerOptions().position(myLocation));
            }
            int maxDistance = 500;
            int minDistance = 50;
            Random r = new Random();
            for (int i = 0; i < 3; i++) {
                double distance = r.nextInt(maxDistance - minDistance + 1) + minDistance;
                double angle = r.nextInt(360);
                mMap.addMarker(new MarkerOptions().position(translateCoordinates(distance, myLocation, angle)));
/*
                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
*/
            }
        }
        else {
            Log.d("MAPPPPPPPPPPPPPPPPPPPPPPPPP", "Location NULL");
            Toast.makeText(getActivity(), "Couldn't Zoom!", Toast.LENGTH_LONG);
        }
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