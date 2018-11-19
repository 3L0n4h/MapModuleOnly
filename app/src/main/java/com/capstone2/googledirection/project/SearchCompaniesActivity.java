package com.capstone2.googledirection.project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.capstone2.googledirection.model.Waypoint;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SearchCompaniesActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spnr_Op_Hr, spnr_Op_Min, spnr_Op_day,
    spnr_Clos_Hr, spnr_Clos_Min, spnr_Clos_day;
    private Button btn_show_route;

    List<String> listAddrs;//list of Address
    public static ArrayList<LatLng> listCoord;//list of coordinates

    //db things
    ListView listViewResult;
    DatabaseReference databaseref;
    List<CompanyInfo> companyList;

    private TextView txtResult;
    private AlertDialog.Builder alertDialogBuilder;

    //for geocode
    String theAddress="";
    EditText editAddress;
    ProgressBar progressBar;
    boolean fetchAddress;
    int fetchType = Constants.USE_ADDRESS_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_companies);
   /*     txtResult = (TextView) findViewById(R.id.txt_result);
        editAddress = (EditText)findViewById(R.id.editAddress);*/
        findViewById(R.id.btn_showRoute).setOnClickListener(this);


        alertDialogBuilder = new AlertDialog.Builder(this);

        listAddrs = new ArrayList<String>();
        listCoord = new ArrayList<LatLng>();

        listViewResult = (ListView) findViewById(R.id.listViewResult);
        companyList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseref = FirebaseDatabase.getInstance().getReference("companyInfo");


        //adds mock address to addressList
        listAddrs.add("Ortigas Center, Quezon City, 1110 Metro Manila ");
        listAddrs.add("Boni Ave, Barangka, Mandaluyong, Metro Manila");
        listAddrs.add("8/F Tower 2, The Rockwell Business Center, 1600, Ortigas Ave, Pasig, Metro Manila");
        listAddrs.add("Lot 5 Block 2 E-Commerce Road Eastwood, Bagumbayan, Quezon City, 1110 Metro Manila");
     /*
     // to get value
      Bundle b = getIntent().getExtras();
        double result = b.getDouble("key");*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot companySnapshot : dataSnapshot.getChildren()) {
                    companyList.clear();
                    Log.d("TheList: ",""+companySnapshot); //ok
                 /*   CompanyInfo ci = companySnapshot.child("Address").getValue(CompanyInfo.class);
                    companyList.add(ci);
                    Log.d("TheList: " ,""+companyList);*/
                }
                DestinationsList adapter = new DestinationsList(SearchCompaniesActivity.this, companyList);
                listViewResult.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_showRoute) {
       /*    if(editAddress.getText().length() == 0) {
                Toast.makeText(this, "Please add at least one address", Toast.LENGTH_LONG).show(); //change this to alert
            }else{*/
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String errorMessage = "";
            List<Address> addresses = null;
            for (int i=0; i<listAddrs.size();i++){

                listCoord.add(doGeocode(listAddrs.get(i)));  //CALLS GEOCODE FUNCTION
               /* alertDialogBuilder.setMessage(listCoord.get(i)+"");
                alertDialogBuilder.show();*/
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("coordinates", listCoord);
            Intent intent = new Intent(SearchCompaniesActivity.this, WaypointsDirectionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void openActivity(Class<?> cs) {
        startActivity(new Intent(SearchCompaniesActivity.this, cs));
    }
    public LatLng doGeocode(String str){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(str, 1);
        } catch (IOException e) {
            Log.e("geocoderErr1: ", errorMessage, e);
        }
        return new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //gets latitude show in textview
                        progressBar.setVisibility(View.GONE);
                        txtResult.setText("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      /*  progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));*/
                    }
                });
            }
        }
    }

}

