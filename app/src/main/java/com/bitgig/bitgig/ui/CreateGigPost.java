package com.bitgig.bitgig.ui;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitgig.bitgig.ui.maps.LocationHelper;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitgig.bitgig.model.RequestModel;

/*****/
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
/****/
import com.bitgig.bitgig.ui.maps.LocationHelper.LocationResult;

import com.bitgig.bitgig.R;

import android.location.Location;
public class CreateGigPost extends ActionBarActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private final String TAG = "com.bitgig.bitgig.createGiPost";
    private Location lastLocation;
    private Location currentLocation;

    LocationResult locationResult;
    LocationHelper locationHelper;

    private RequestModel requestModel;
    double lat;
    double lon;

    private EditText gigPostTitle, gigPostDesciption, gigPostPrice;
    String gigPostTitleStr, gigPostDescriptionStr, gigPostPriceStr;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig_post_2);

        requestModel = new RequestModel(this.getApplicationContext());

        // to get location updates, initialize LocationResult
        this.locationResult = new LocationResult(){
            @Override
            public void gotLocation(Location location){

                //Got the location!
                if(location!=null){

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    lat = location.getLatitude();
                    lon = location.getLongitude();

                    Log.e(TAG, "lat: " + latitude + ", long: " + longitude);

                    // here you can save the latitude and longitude values
                    // maybe in your text file or database

                }else{
                    Log.e(TAG, "Location is null.");
                }

            }

        };
        // initialize our useful class,
        this.locationHelper = new LocationHelper();

        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        findViewById(R.id.gig_post_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView)v;
                textView.setText("");
            }
        });

        Spinner spinner = (Spinner)    findViewById(R.id.gig_types);
        ArrayAdapter<CharSequence> gigTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.gig_types, android.R.layout.simple_spinner_item);
        gigTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(gigTypeAdapter);

        // Fetch Facebook user info if the session is active
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            ///makeMeRequest();
        }

        Button btnSendRequest = (Button) findViewById(R.id.btn_send_request);
        btnSendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        gigPostTitle = (EditText)findViewById(R.id.gig_post_title);
        gigPostDesciption = (EditText)findViewById(R.id.gig_post_description);
        gigPostPrice = (EditText)findViewById(R.id.gig_post_price);

        gigPostTitleStr = gigPostTitle.getText().toString();
        gigPostDescriptionStr = gigPostDesciption.getText().toString();
        gigPostPriceStr = gigPostPrice.getText().toString();

        requestModel.sendGigRequest(
                gigPostTitleStr,
                gigPostDescriptionStr,
                gigPostPriceStr,
                30,
                lat,lon);

    }

    /*
   * Get the current location
   */
/*
    private Location getLocation() {
        // If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            return locationClient.getLastLocation();
        } else {
            return null;
        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_gig_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
