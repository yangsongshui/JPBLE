package com.jpble.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.bean.Code;
import com.jpble.bean.DeviceMsg;
import com.jpble.presenter.GetDevicePresenterImp;
import com.jpble.presenter.TrackingPresenterImp;
import com.jpble.utils.Toastor;
import com.jpble.view.CodeView;
import com.jpble.view.DeviceMsgView;
import com.jpble.widget.AMapUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.Constant.BASE_URL;
import static com.jpble.utils.Constant.DATA_MAP;
import static com.jpble.utils.Constant.END_RIDING;
import static com.jpble.utils.Constant.GPS;
import static com.jpble.utils.Constant.STARTED_RIDING;


public class MapFragment extends BaseFragment implements OnMapReadyCallback, CodeView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private MapView mapView;
    private GoogleMap map;
    @BindView(R.id.map_person)
    ImageView mapPerson;
    @BindView(R.id.map_share)
    ImageView mapShare;

    TrackingPresenterImp trackingPresenterImp;
    Toastor toastor;
    ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    boolean isRinding = false;
    GetDevicePresenterImp getDevicePresenterImp;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        mapView = (MapView) layout.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.login_msg7));
        toastor = new Toastor(getActivity());
        MapsInitializer.initialize(getActivity());
        mapView.getMapAsync(this);
        trackingPresenterImp = new TrackingPresenterImp(this, getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GPS);
        intentFilter.addAction(STARTED_RIDING);
        intentFilter.addAction(END_RIDING);
        getActivity().registerReceiver(notifyReceiver, intentFilter);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
        getDevicePresenterImp = new GetDevicePresenterImp(new DeviceMsgView() {
            @Override
            public void showProgress() {

            }

            @Override
            public void disimissProgress() {

            }


            @Override
            public void loadDataSuccess(DeviceMsg tData) {
                LatLng sydney = new LatLng(tData.getData().getGLat(), tData.getData().getGLng());
                if (marker != null)
                    marker.remove();
                marker = map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_blue)).title("My Geo"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
            }

            @Override
            public void loadDataError(Throwable throwable) {

            }
        }, getActivity());

    }

    Marker lMarker;

    protected void placeMarkerOnMap(LatLng location) {
        if (lMarker != null)
            lMarker.remove();      // 1
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.point_red)).position(location);
        // 2
        lMarker = map.addMarker(markerOptions);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_map;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onStart() {
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

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

       /* if (LocationServices.FusedLocationApi != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        getActivity().unregisterReceiver(notifyReceiver);

    }

    @OnClick({R.id.map_person, R.id.map_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_person:
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                break;
            case R.id.map_share:
                String token = MyApplication.newInstance().getUser().getData().getToken();
                String url = BASE_URL + "user/lock/10007/" + MyApplication.newInstance().id;
                getDevicePresenterImp.register(url, MyApplication.newInstance().id, token);
                break;
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
    public void loadDataSuccess(Code tData) {

    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        toastor.showSingletonToast(getString(R.string.login_msg10));
    }

    Marker marker;
    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (STARTED_RIDING.equals(intent.getAction())) {
                isRinding = true;
                MyApplication.newInstance().latLng.add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            } else if (END_RIDING.equals(intent.getAction())) {
                isRinding = false;
                MyApplication.newInstance().latLng.add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            } else if (GPS.equals(intent.getAction())) {
                Log.e("notifyReceiver", MyApplication.newInstance().id);
                String token = MyApplication.newInstance().getUser().getData().getToken();
                String url = BASE_URL + "user/lock/10007/" + MyApplication.newInstance().id;
                getDevicePresenterImp.register(url, MyApplication.newInstance().id, token);
            }

        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                placeMarkerOnMap(currentLocation);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));

            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                MapFragment.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    int speed = 0;

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        placeMarkerOnMap(latLng);
        if (isRinding) {
            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                float distance = AMapUtil.calculateLineDistance(mLastLocation.getLatitude(), mLastLocation.getLongitude(), location.getLatitude(), location.getLongitude());
                if (distance > 50) {
                    // 如果距离大于50， 则认为是 偏移太大造成的。
                } else if (distance < 5) {
                    // 如果距离 <5， 则认为是 站在原地  偏移太小造成。
                } else {
                    // 小于 100 的认为是在原地没有动，产生的位移认为是原地漂移的位移
                    //5s间隔  距离大于100 认为是在真实的运动
                    speed = (int) (distance / 2);
                    MyApplication.newInstance().latLng.add(latLng);
                    Intent intent = new Intent();
                    intent.putExtra("distance", distance);
                    intent.putExtra("sped", speed);
                    intent.setAction(DATA_MAP);
                    getActivity().sendBroadcast(intent);
                    Log.e("---------", " 偏移");
                    MyApplication.newInstance().latLng.add(latLng);

                }

            }
        }
        mLastLocation = location;
        //  Log.e("定位信息", location.toString());
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        //定位间隔
        mLocationRequest.setInterval(2000);
        // 接收定位信息间隔
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                                MapFragment.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


}
