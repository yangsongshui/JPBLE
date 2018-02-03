package com.jpble.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.adapter.TripAdapter;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Trip;
import com.jpble.ble.DeviceInfo;
import com.jpble.presenter.GetRidePresenterImp;
import com.jpble.view.RideMsgView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jpble.utils.DateUtil.timeSub;

public class TripActivity extends BaseActivity implements RideMsgView {


    @BindView(R.id.trip_total)
    TextView tripTotal;
    @BindView(R.id.trip_exlv)
    ExpandableListView tripExlv;
    TripAdapter tripAdapter;
    List<Trip> mList;
    GetRidePresenterImp getRidePresenterImp;
    private ProgressDialog pd;

    @Override
    protected int getContentView() {
        return R.layout.activity_trip;
    }

    @Override
    protected void init() {
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.device_done8));
        mList = new ArrayList<>();

        tripAdapter = new TripAdapter(mList);
        tripExlv.setAdapter(tripAdapter);
        tripExlv.setGroupIndicator(null);
        final int groupCount = tripExlv.getCount();
        for (int i = 0; i < groupCount; i++) {
            tripExlv.expandGroup(i);
        }

        tripExlv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String orbit = mList.get(groupPosition).getmList().get(childPosition).getOrbit();

                if (orbit != null)
                    startActivity(new Intent(TripActivity.this, MapActivity.class).putExtra("orbit", orbit));


                return false;
            }
        });

        getRidePresenterImp = new GetRidePresenterImp(this, this);
        getRidePresenterImp.register(MyApplication.newInstance().getUser().getData().getToken());
    }


    @OnClick({R.id.trip_return, R.id.trip_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.trip_return:
                finish();
                break;
            case R.id.trip_share:
                break;
            default:
                break;
        }
    }

    double total = 0;

    private void setTotal(List<Trip> data) {
        total = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).getmList().size(); j++) {
                double km = Double.parseDouble(data.get(i).getmList().get(j).getKm());
                total += km;
            }
        }
        BigDecimal b = new BigDecimal(total);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tripTotal.setText(f1 + "km");
    }

    @Override
    public void showProgress() {
        pd.show();
    }

    @Override
    public void disimissProgress() {
        if (pd.isShowing())
            pd.dismiss();
    }

    Map<String, Trip> map = new HashMap<>();

    @Override
    public void loadDataSuccess(DeviceInfo tData) {

        map.clear();
        mList.clear();
        for (int i = 0; i < tData.getData().size(); i++) {
            String date = tData.getData().get(i).getStartTimeStr().substring(0, 10);
            String time = tData.getData().get(i).getStartTimeStr().substring(11, 16);
            //Log.e("key", date);
            if (map.get(date) == null) {
                List<Trip.TripInfo> mlist = new ArrayList<>();
                Trip trip = new Trip(date, mlist);
                map.put(date, trip);
                trip.getmList().add(new Trip.TripInfo(time, String.valueOf(tData.getData().get(i).getDistance()), gettime(tData.getData().get(i)), tData.getData().get(i).getOrbit()));
                Log.e("Trip", tData.getData().get(i).getDistance() + "km");
            } else {
                Trip trip = map.get(date);
                trip.getmList().add(new Trip.TripInfo(time, String.valueOf(tData.getData().get(i).getDistance()), gettime(tData.getData().get(i)), tData.getData().get(i).getOrbit()));
                Log.e("Trip", tData.getData().get(i).getDistance() + "km");
            }

        }
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();

            mList.add(map.get(key));
            Log.e("key", key + " " + map.get(key).getmList().size());
        }
        setTotal(mList);
        tripAdapter.notifyDataSetChanged();
        for (int i = 0; i < mList.size(); i++) {
            tripExlv.expandGroup(i);
        }

    }

    private String gettime(DeviceInfo.DataBean tData) {
        if (tData.getStartTimeStr() != null && tData.getEndTimeStr() != null) {
            long time = timeSub(tData.getStartTimeStr(), tData.getEndTimeStr());
            if (time % 60 > 0) {
                String min = String.valueOf((time / 60) + 1) + " min";
                return min;
            } else {
                return "0 min";
            }
        }
        return "0 min";
    }

    @Override
    public void loadDataError(Throwable throwable) {
        showToastor(getString(R.string.login_msg10));
    }


}
