package com.jpble.activity;

import android.view.View;

import com.jpble.R;
import com.jpble.base.BaseActivity;
import com.jpble.widget.ListenerKeyBackEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class EquipmentActivity extends BaseActivity {
    @BindView(R.id.time_ed)
    ListenerKeyBackEditText timeEd;
    @BindView(R.id.intensity_ed)
    ListenerKeyBackEditText intensityEd;

    @Override
    protected int getContentView() {
        return R.layout.activity_equipment;
    }

    @Override
    protected void init() {

    }


    @OnClick({R.id.trip_return, R.id.trip_share, R.id.equipment_time, R.id.equipment_intensity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.trip_return:
                finish();
                break;
            case R.id.trip_share:
                break;
            case R.id.equipment_time:
                break;
            case R.id.equipment_intensity:
                break;
            default:
                break;
        }
    }
}
