package com.jpble.activity;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.bean.Log;
import com.jpble.presenter.GetInfoPresenterImp;
import com.jpble.view.LogView;

import static com.jpble.utils.Constant.BASE_URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LogView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    String lat = "";
    String lng = "";
    GetInfoPresenterImp getInfoPresenterImp;
    ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getInfoPresenterImp = new GetInfoPresenterImp(this, this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        String id = getIntent().getStringExtra("id");
        lng = getIntent().getStringExtra("lng");
        lat = getIntent().getStringExtra("lat");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        findViewById(R.id.map_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (id != null && !id.isEmpty()) {
            String token = MyApplication.newInstance().getUser().getData().getToken();
            String url = BASE_URL + "user/lock/10009/" + id;
            getInfoPresenterImp.register(url, id, token);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (lat != null && !lat.isEmpty() && lng != null && !lng.isEmpty()) {
            Double lat = Double.parseDouble(this.lat);
            Double lng = Double.parseDouble(this.lng);
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_blue)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        }else {
            getlo();
        }


    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void disimissProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void loadDataSuccess(Log tData) {
        if (tData.getCode() == 200) {
            this.lat = tData.getData().getLockVo().getGLat().toString();
            this.lng = tData.getData().getLockVo().getGLat().toString();
            if (lat != null && !lat.isEmpty() && lng != null && !lng.isEmpty()) {
                Double lat = Double.parseDouble(this.lat);
                Double lng = Double.parseDouble(this.lng);
                LatLng sydney = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_blue)).title("My Geo"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            }else {
                getlo();
            }

        }


    }

    public void getlo() {
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_red)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getlo();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
