package com.jpble.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jpble.R;
import com.jpble.app.MyApplication;
import com.jpble.widget.AMapUtil;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<LatLng> simplifiedLine;
    boolean end = false;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                polyline.remove();
                simplifiedLine = MyApplication.newInstance().latLng;
                polyline = mMap.addPolyline(new PolylineOptions()
                        .addAll(simplifiedLine)
                        .color(Color.RED));
                handler.postDelayed(this, 3000);
            }
        };
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String orbit = getIntent().getStringExtra("orbit");
        if (orbit != null) {
            simplifiedLine = AMapUtil.decode(orbit);
            end = true;
        } else {
            simplifiedLine = MyApplication.newInstance().latLng;
            end = false;
            handler.postDelayed(runnable, 3000);
        }
        findViewById(R.id.map_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Polyline polyline;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        polyline = mMap.addPolyline(new PolylineOptions()
                .addAll(simplifiedLine)
                .color(Color.RED));
        if (end) {
            mMap.addMarker(new MarkerOptions().position(simplifiedLine.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
            mMap.addMarker(new MarkerOptions().position(simplifiedLine.get(simplifiedLine.size() - 1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(simplifiedLine.get(0), 18));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
