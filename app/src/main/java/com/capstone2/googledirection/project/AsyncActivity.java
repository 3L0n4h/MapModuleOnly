package com.capstone2.googledirection.project;


import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AsyncActivity extends AsyncTask<Object,String,String> {

    private String googlePlaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap) objects[0];
        url=(String)objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlaceData = downloadURL.ReadTheURL(url); //use the DL class to read the web page returned by url request
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlaceData; //json
    }


    @Override
    protected void onPostExecute(String s){ //takes return of doInBackground
        List<HashMap<String,String>> PlacesList = null;
        DataParser dataParser = new DataParser();
        PlacesList = dataParser.parse(s); //s is jSON
        //nearbyPlaceList has the list of all location searched

        getDetailsOfPlace(PlacesList);
    }

    private void getDetailsOfPlace(List<HashMap<String,String>> PlacesList){ //show all places in the list
        for(int i = 0; i<PlacesList.size(); i++){
            //MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googleNearbyPlaces= PlacesList.get(i);
            String nameOfPlace = googleNearbyPlaces.get("place_name"); //place_name, vicinity, is in DataParser
            String vicinity = googleNearbyPlaces.get("vicinity");
            double lat = Double.parseDouble(googleNearbyPlaces.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlaces.get("lng"));
            LatLng latLng = new LatLng(lat,lng); //lat long
          /*  markerOptions.position(latLng);
            markerOptions.title(nameOfPlace+" : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(markerOptions);*/
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        }

    }
}