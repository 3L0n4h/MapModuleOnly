package com.capstone2.googledirection.project;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.capstone2.googledirection.DirectionCallback;
import com.capstone2.googledirection.GoogleDirection;
import com.capstone2.googledirection.config.GoogleDirectionConfiguration;
import com.capstone2.googledirection.constant.TransportMode;
import com.capstone2.googledirection.model.Direction;
import com.capstone2.googledirection.model.Leg;
import com.capstone2.googledirection.model.Route;
import com.capstone2.googledirection.model.Step;
import com.capstone2.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.capstone2.googledirection.model.Waypoint;
import com.capstone2.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//api key is on manifest and below.
public class WaypointsDirectionActivity extends AppCompatActivity implements
        View.OnClickListener, OnMapReadyCallback, DirectionCallback,
        OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerClickListener,
        OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private ImageView simpleImageButtonHome;
    private ImageView btnRequestDirection;
    private Button btnSearchCompanies;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyD7z_O9pRgwsdDRqtEJZf5sTLkIafoQM0M";
    private LatLng urLng;
    private Double urLat, urLong;
    ArrayList<LatLng> coordinates;

    private FusedLocationProviderClient mFusedLocationProviderClient;//currLoc redo
    private boolean mLocationPermissionGranted;
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private CameraPosition mCameraPosition;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //currLoc redo

    //for search
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(3.72, 119.90), new LatLng(20.08, 124.82));
    private Marker mMarker;
    String selectedAddress;
    //forsearch

    boolean btn_traffc_clicked=false;
    Intent intent;
    private AlertDialog.Builder alertDialogBuilder;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoints_direction);
        findViewById(R.id.btn_companies).setOnClickListener(this);


        //curLoc redo
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //curLoc redo

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }


         btnRequestDirection = findViewById(R.id.btn_request_direction);
      //  btnRequestDirection.setEnabled(false);
    //    btnRequestDirection.setOnClickListener(this);
    //    btnSearchCompanies = findViewById(R.id.btn_request_direction);
     //   btnSearchCompanies.setOnClickListener(this);
        alertDialogBuilder = new AlertDialog.Builder(this);

       coordinates = getIntent().getParcelableArrayListExtra("coordinates");
        ImageView btn_shRoute = (ImageView) findViewById(R.id.btn_request_direction);
        if(coordinates==null){
            btn_shRoute.setEnabled(false);
        }else{
            btn_shRoute.setEnabled(true);
           /* for(int i=0; i<coordinates.size(); i++) {
                LatLng theCoord = coordinates.get(i);
                // Do something with the value
            }*/
        }

         /*   for (int i = 0; i < coordinates.size(); i++) {
                alertDialogBuilder.setMessage(coordinates.get(i)+" coord");
                alertDialogBuilder.show();
            }*/

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("PH")
                .build();


        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedAddress = place.getAddress().toString();
             //   Toast.makeText(WaypointsDirectionActivity.this,"Place: " + place.getName() +" address: "+selectedAddress,Toast.LENGTH_LONG).show();
                MarkerOptions userMarkerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude); //lat long
                userMarkerOptions.position(latLng);
                userMarkerOptions.title(selectedAddress);
                userMarkerOptions.icon(BitmapDescriptorFactory
                        .fromBitmap(resizeMapIcons("thepin",100,100)));

                googleMap.addMarker(userMarkerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    //end of permission check
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
       // enableMyLocation();
        //currLoc redo
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        //currLoc redo
        googleMap.setOnMarkerClickListener(this);


    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
 /*       Object transferData[] = new Object[2];
        String url = getUrl(marker.getId());
        transferData[0] = googleMap;
        transferData[1] = url;

        AsyncActivity asyncA = new AsyncActivity();*/
     /*   String locAddress = marker.getId();
        Log.d("placeId",marker.getId());
        alertDialogBuilder.setMessage(" locAddress: "+locAddress);
        alertDialogBuilder.show();*/
        return true;
    }

    private void getDeviceLocation() {
        /** Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.*/
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d("getDLoc", "Current location is null. Using defaults.");
                            Log.e("getDLoc", "Exception: %s", task.getException());
                            alertDialogBuilder.setMessage("Current location is null. Using defaults.");
                            alertDialogBuilder.show();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.*/
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /*** Enables the My Location layer if the fine location permission has been granted.*/
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_request_direction) {
            requestDirection();
        } else if (id == R.id.btn_companies) {
            openActivity(SearchCompaniesActivity.class);
            finish();
        }else if(id== R.id.btn_traffc){
            if(btn_traffc_clicked)
            { btn_traffc_clicked=false;
                googleMap.setTrafficEnabled(false);
            }
            else
            {btn_traffc_clicked=true;
                googleMap.setTrafficEnabled(true);
            }
        }
    }
    public void openActivity(Class<?> cs) {
        startActivity(new Intent(this, cs));
    }
    public void requestDirection() {
        LatLng urLoc = new LatLng(mLastKnownLocation.getLatitude(),
                mLastKnownLocation.getLongitude());
        Toast.makeText(this, "Requesting Direction...", Toast.LENGTH_SHORT).show();
    //    Snackbar.make(btnRequestDirection, "Requesting Direction...", Snackbar.LENGTH_SHORT).show();
        GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
        GoogleDirection.withServerKey(serverKey)
                //.from(urLng)
                .from(urLoc)
                .and(coordinates.get(1))
                .and(coordinates.get(3))
                .to(coordinates.get(2))
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            int legCount = route.getLegList().size();
            for (int index = 0; index < legCount; index++) {
                Leg leg = route.getLegList().get(index);
                googleMap.addMarker(new MarkerOptions()
                        .position(leg.getStartLocation()
                                .getCoordination())
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(resizeMapIcons("thepin",100,100))));
                if (index == legCount - 1) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(leg.getEndLocation()
                                    .getCoordination())
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(resizeMapIcons("thepin",100,100)))); //endpin
                }

                List<Step> stepList = leg.getStepList();
                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter
                        .createTransitPolyline(this, stepList, 5, Color.rgb(161, 66, 244), 3, Color.BLUE);
                for (PolylineOptions polylineOption : polylineOptionList) {
                    googleMap.addPolyline(polylineOption);
                }
            }
            setCameraWithCoordinationBounds(route);
         //   btnRequestDirection.setVisibility(View.GONE); //rquestDirection is removed here
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Showing current location...", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "You are here", Toast.LENGTH_LONG).show();
      //  Toast.makeText(this, "You are here" + location, Toast.LENGTH_LONG).show();
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }
    /*** Displays a dialog with error message explaining that the location permission is missing.*/

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                urLat = mLastLocation.getLatitude();
                urLong = mLastLocation.getLongitude();
                urLng = new LatLng(urLat,urLong);
                Log.d("onConnected", " url is "+ urLng.toString());
            } else {
                Log.d("onConnected", " Unable to get Location");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    //for requesting directions
    private String getUrl(String c){

      //  placeid=ChIJrTLr-GyuEmsRBfy61i59si0
      /*  Double la,lo;
        la = c.latitude;
        lo = c.longitude;*/
        StringBuilder googleUrL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrL.append("placeid="+c);
      //  googleUrL.append("location=" + la +","+ lo); //appends selected loc lat and long
        googleUrL.append("&key="+ "AIzaSyD7z_O9pRgwsdDRqtEJZf5sTLkIafoQM0M");
        Log.d("getUrl","url="+ googleUrL.toString());
        return googleUrL.toString();
        // StringBuilder googleUrL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyAICdIMoPcFTpJd9TgVKEaNH6YSUAzCcTE");
        //pass here the address https://maps.googleapis.com/maps/api/place/autocomplete/json?input=THE_ADDRESS_YOU_WANT_TO_GIVE&sensor=false&key=YOUR_API_KEY
        //https://maps.googleapis.com/maps/api/place/autocomplete/json?input=1600+Amphitheatre&key=<API_KEY>&sessiontoken=1234567890

    }

}
