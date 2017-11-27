package com.jpble.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jpble.R;
import com.jpble.base.BaseFragment;


public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(View layout, LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        mapView = (MapView) layout.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity());
       mapView.getMapAsync(this);

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_map;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
