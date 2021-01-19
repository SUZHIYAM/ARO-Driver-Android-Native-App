package com.tn74travellers.aroDriver.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tn74travellers.aroDriver.Common.CommonFunctions;
import com.tn74travellers.aroDriver.Common.CommonVariables;
import com.tn74travellers.aroDriver.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Context context = MapActivity.this;

    /***************XML Variables Declaration ************************/

    ImageView iv_back_arrow;

    MapFragment mapFragment;

    /****************Normal Variables Declaration ************************/

    CommonFunctions commonFunctions = new CommonFunctions();

    AlertDialog.Builder alert_dialog_builder;
    AlertDialog alert_dialog;

    ProgressDialog progress_dialog;

    JSONObject jo_journey_details;

    private GoogleMap mMap;

    double lat[];
    double lng[];

    double passenger_pickup_lat[] = new double[1];
    double passenger_pickup_lng[] = new double[1];

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    boolean select_location = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /**> Initially Calling Methods <*/

        viewInit();

        initialWorks();

        clickListenersfunc();

        getCurrentLocation();
    }


    private void viewInit()
    {
        iv_back_arrow = findViewById(R.id.iv_back_arrow);

        mapFragment                 = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

    }

    @SuppressLint("SetTextI18n")
    private void initialWorks()
    {
        mapFragment.getMapAsync(this);

        try 
        {
            jo_journey_details = new JSONObject(commonFunctions.getSharedPrefString(context,"journey_details"));


           passenger_pickup_lat[0] = Double.parseDouble(jo_journey_details.getString("pickupLocation_lat"));
           passenger_pickup_lng[0] = Double.parseDouble(jo_journey_details.getString("pickupLocation_long"));

//            passenger_pickup_lat[0] = 8.256565;
//            passenger_pickup_lng[0] = 77.521827;


            if(jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_AIRPORT_TRIP))
            {

                //MEMORY ALLOCATION FOR JAVA DOUBLE ARRAY
                lat = new double[jo_journey_details.getJSONArray("locationList").length()];
                lng = new double[jo_journey_details.getJSONArray("locationList").length()];


                if(jo_journey_details.getJSONArray("locationList").length()>0)
                {

                    Geocoder coder = new Geocoder(context);
                    List<Address> address;
                    LatLng lat_long = null;

                    try
                    {
                        // May throw an IOException
                        address = coder.getFromLocationName(jo_journey_details.getJSONArray("locationList").getString(0), 5);
                        if (address != null)
                        {
                            Address location = address.get(0);

                            lat[0] = location.getLatitude();
                            lng[0] = location.getLongitude();

                            Log.e("LAT" , lat[0]+"");
                            Log.e("LONG" , lng[0]+"");

                        }

                    } catch (IOException ex) {

                        ex.printStackTrace();
                    }

                }
            }

            if(jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_RENTAL_TRIP))
            {

                lat = new double[jo_journey_details.getJSONArray("locationList").length()];
                lng = new double[jo_journey_details.getJSONArray("locationList").length()];

            }

            if(jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ONE_WAY_TRIP) || jo_journey_details.getString(CommonVariables.URL_TRIP_TYPE).equalsIgnoreCase(CommonVariables.TRIP_TYPE_ROUND_TRIP))
            {

                //MEMORY ALLOCATION FOR JAVA DOUBLE ARRAY
                lat = new double[jo_journey_details.getJSONArray("locationList").length()];
                lng = new double[jo_journey_details.getJSONArray("locationList").length()];

                if(jo_journey_details.getJSONArray("locationList").length()>0)
                {
                    for(int i=0;i<jo_journey_details.getJSONArray("locationList").length();i++)
                    {
                        Geocoder coder = new Geocoder(context);
                        List<Address> address;
                        LatLng lat_long = null;

                        try
                        {
                            // May throw an IOException
                            address = coder.getFromLocationName(jo_journey_details.getJSONArray("locationList").getString(i), 5);
                            if (address != null)
                            {

                                Address location = address.get(0);

                                lat[i] = location.getLatitude();
                                lng[i] = location.getLongitude();


                                Log.e("LAT" , lat[i]+"");
                                Log.e("LONG" , lng[i]+"");
                            }

                        } catch (IOException ex) {

                            ex.printStackTrace();
                        }

                    }

                }
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clickListenersfunc()
    {
        setUpGClient();

        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,JourneyDetailsActivity.class));
            }
        });

    }

    private void setUpGClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void getCurrentLocation()
    {
        if(commonFunctions.checkNetConnectivity(context))
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
        }
        else
        {
            //commonFunctions.set_custom_toast_msg(context,CommonVariables.MSG_NETWORK_UNAVAILABLE,toastView);
            Toast.makeText(context,CommonVariables.MSG_NETWORK_UNAVAILABLE,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED)
        {
            getMyLocation();


        }
    }


    private void getMyLocation()
    {
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,  MapActivity.this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            Log.e("status.getStatusCode()",status.getStatusCode()+"");
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(context,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED)
                                    {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(MapActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.

                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        try
        {
            mMap = googleMap;

            LatLng[] lat_lng = new LatLng[lat.length];

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            ArrayList<String> listofcountries = new ArrayList<>();

            String location = null;

            String pickup_location = null;

            for(int i=0;i<lat.length;i++)
            {
                lat_lng[i] = new LatLng(lat[i], lng[i]);

                Geocoder geocoder       = new Geocoder(this, Locale.US);

                List<Address> addresses;
                try
                {
                    Log.e("lat", "["+i+"]"+lat[i]+"");
                    Log.e("lng", "["+i+"]"+lng[i]+"");

                    addresses = geocoder.getFromLocation(lat[i], lng[i], 1);

                    Log.e("addresses : ",""+addresses);

                    Address address        = addresses.get(0);

                    location = address.getLocality();

                    Log.e("Current Location : ",location);


                    listofcountries.add(location);

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }


                mMap.addMarker(new MarkerOptions().position(lat_lng[i]).title(location))
                        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                builder.include(lat_lng[i]);
            }

            Collections.sort(listofcountries);

            Log.e("listofcountries : ",listofcountries+"");

            LatLng[] pass_pickup_lat_lng = new LatLng[passenger_pickup_lat.length];

            pass_pickup_lat_lng[0] = new LatLng(passenger_pickup_lat[0], passenger_pickup_lng[0]);

            Geocoder geocoder       = new Geocoder(this, Locale.US);

            List<Address> pickUpAddress;

            try
            {
                pickUpAddress = geocoder.getFromLocation(passenger_pickup_lat[0], passenger_pickup_lng[0], 1);

                Address address        = pickUpAddress.get(0);

                pickup_location = address.getLocality();

            } catch (IOException e) {
                e.printStackTrace();
            }


            //mMap.addMarker(new MarkerOptions().position(pass_pickup_lat_lng[0]).title(location).anchor(0.5f, 0.5f)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_black_with_truck));

            mMap.addMarker(new MarkerOptions().position(pass_pickup_lat_lng[0]).title(pickup_location).anchor(0.5f, 0.5f))
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            builder.include(pass_pickup_lat_lng[0]);


            LatLngBounds bounds = builder.build();

            int padding = 10; // offset from edges of the map in pixels
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            List<PatternItem> pattern = Arrays.asList(new Dot(), new Gap(15));
            PolylineOptions options = new PolylineOptions().pattern(pattern);
            mMap.addPolyline(options.add(lat_lng));


            mMap.setOnMapLoadedCallback(() -> {
                mMap.moveCamera(cu);

                mMap.animateCamera(cu);
            });
        }
        catch (Exception e) {
            e.printStackTrace();

            Log.e("onMapReady Exception : ", e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.e("locationText : ","Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        mylocation = location;
        if (mylocation != null)
        {
            if(!select_location)
            {
                try
                {
                    Geocoder geocoder       = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String curr_loc        = ""+addresses.get(0).getAddressLine(0);

                    Log.e("curr_loc : ",curr_loc);

                    LatLng[] current_loc_lat_lng = new LatLng[1];

                    current_loc_lat_lng[0] = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(current_loc_lat_lng[0]).title(curr_loc).anchor(0.5f, 0.5f))
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.truck_marker));

                }
                catch(Exception e)
                {
                    Log.e("E : "," Error : "+e.getMessage());
                }

                select_location = true;

            }

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    private void checkPermissions()
    {
        int permissionLocation = ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionLocation != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty())
            {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
            else
            {
                Log.e("listPermissionsNeeded",listPermissionsNeeded+"");
            }

        }
        else
        {
            getMyLocation();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServiceTripAcceptClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_TRIP_ACCEPT);

                jo_mobile_no_validate_request.put(CommonVariables.URL_TRIP_ID,jo_journey_details.getString("tripId"));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,jo_journey_details.getString("passangerId"));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            String data = jo_mobile_no_validate_request.toString();

            assert data != null ;

            Log.e("data",data+"");

            return commonFunctions.send_http_request_func(params[0],
                    data,
                    CommonVariables.NO_HEADER,
                    CommonVariables.POST,
                    CommonVariables.JSON,
                    CommonVariables.BLANK_STRING,
                    jo_header.toString());

        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            commonFunctions.cancelLoadingProgress(progress_dialog);

            Log.e("mobile_no_result : ", ""+result);

            try
            {
                if(result != null)
                {
                    switch (result)
                    {
                        case CommonVariables.SERVICE_RESPONSE_CODE_404:
                        {
                            Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();

                            break;
                        }
                        default:
                        {
                            JSONObject jo_mobile_no_response = new JSONObject(result);

                            switch (jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))
                            {
                                case CommonVariables.URLRQ_SUCCESS:
                                {
                                    JSONObject jo_response_data = jo_mobile_no_response.getJSONObject("responseData");

                                    if(jo_response_data.getString("tripStatus").equalsIgnoreCase("true"))
                                    {
                                        commonFunctions.setSharedPrefString(context,"from","back_to_cal");
                                        Toast.makeText(context, "Trip Accepted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(context,DashboardActivity.class));

                                        //startActivity(new Intent(context, TripConfirmationActivity.class));

                                    }

                                    break;
                                }
                                default:
                                {
                                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForHttpServicePickTripClass extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            JSONObject jo_mobile_no_validate_request    = new JSONObject();
            JSONObject jo_header                        = new JSONObject();

            try
            {
                jo_header.put(CommonVariables.FROM, CommonVariables.CASE_PICK_TRIP);

                jo_mobile_no_validate_request.put(CommonVariables.URL_TRIP_ID,jo_journey_details.getString("tripId"));
                jo_mobile_no_validate_request.put(CommonVariables.URLRQ_DRIVER_ID,commonFunctions.getSharedPrefString(context,CommonVariables.SP_DRIVER_ID));
                jo_mobile_no_validate_request.put("pick_this_trip","true");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            String data = jo_mobile_no_validate_request.toString();

            assert data != null ;

            Log.e("data",data+"");

            return commonFunctions.send_http_request_func(params[0],
                    data,
                    CommonVariables.NO_HEADER,
                    CommonVariables.POST,
                    CommonVariables.JSON,
                    CommonVariables.BLANK_STRING,
                    jo_header.toString());

        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            commonFunctions.cancelLoadingProgress(progress_dialog);

            Log.e("mobile_no_result : ", ""+result);

            try
            {
                if(result != null)
                {
                    switch (result)
                    {
                        case CommonVariables.SERVICE_RESPONSE_CODE_404:
                        {
                            Toast.makeText(context, CommonVariables.EM_INVALID_USER, Toast.LENGTH_SHORT).show();

                            break;
                        }
                        default:
                        {
                            JSONObject jo_mobile_no_response = new JSONObject(result);

                            switch (jo_mobile_no_response.getString(CommonVariables.URLRS_RESPONSE_MSG))
                            {
                                case CommonVariables.URLRQ_SUCCESS:
                                {
                                    JSONObject jo_response_data = jo_mobile_no_response.getJSONObject("responseData");

                                    if(jo_response_data.getString("Pick_this_trip").equalsIgnoreCase("true"))
                                    {
                                        Toast.makeText(context,"New Trip Picked Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(context, DashboardActivity.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"You Have Not Yet Complete Your Previous Trip", Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                                }
                                default:
                                {
                                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                Toast.makeText(context, CommonVariables.EM_SORRY_TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,JourneyDetailsActivity.class));
    }
}