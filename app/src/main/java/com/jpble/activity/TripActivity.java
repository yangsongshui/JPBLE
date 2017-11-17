package com.jpble.activity;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jpble.R;
import com.jpble.adapter.TripAdapter;
import com.jpble.base.BaseActivity;
import com.jpble.bean.Trip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TripActivity extends BaseActivity {


    @BindView(R.id.trip_total)
    TextView tripTotal;
    @BindView(R.id.trip_exlv)
    ExpandableListView tripExlv;
    TripAdapter tripAdapter;
    List<Trip> mList;

    @Override
    protected int getContentView() {
        return R.layout.activity_trip;
    }

    @Override
    protected void init() {
        mList = new ArrayList<>();
        Trip trip = new Trip();
        trip.setDate("10/23");
        List list = new ArrayList<Trip.TripInfo>();
        list.add(new Trip.TripInfo("12:05", "10", "20"));
        list.add(new Trip.TripInfo("11:05", "12", "30"));
        list.add(new Trip.TripInfo("15:05", "41", "80"));
        trip.setmList(list);
        mList.add(trip);
        mList.add(trip);
        mList.add(trip);
        tripAdapter = new TripAdapter(mList);
        tripExlv.setAdapter(tripAdapter);
        tripExlv.setGroupIndicator(null);
        int groupCount = tripExlv.getCount();
        for (int i = 0; i < groupCount; i++) {
            tripExlv.expandGroup(i);
        }
        tripExlv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
        setTotal(mList);
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

    int total = 0;

    private void setTotal(List<Trip> data) {
        total = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).getmList().size(); j++) {
                int km = Integer.parseInt(data.get(i).getmList().get(j).getKm());
                total += km;
            }
        }
        tripTotal.setText(total + "km");
    }
}
